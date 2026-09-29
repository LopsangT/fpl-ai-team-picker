"""
FastAPI service that serves player points predictions from the trained
Random Forest model. Called by the Spring Boot backend - kept as a
separate microservice so the ML logic can evolve independently of the
Java backend, per the project's architecture.
"""

import joblib
import pandas as pd
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI(title="FPL Points Predictor")

# Loaded once at startup, not per-request - reloading the model on every
# call would add unnecessary latency.
model = joblib.load("model.pkl")

# Must match the exact feature order the model was trained on.
FEATURE_COLUMNS = [
    "was_home", "minutes", "form_before_gameweek", "opponent_difficulty",
    "position_FWD", "position_GK", "position_MID"
]


class PredictionRequest(BaseModel):
    was_home: bool
    minutes: int
    form_before_gameweek: float
    opponent_difficulty: int
    position: str  # "GKP"/"GK", "DEF", "MID", or "FWD"


class PredictionResponse(BaseModel):
    predicted_points: float


class BatchPredictionRequest(BaseModel):
    players: list[PredictionRequest]


class BatchPredictionResponse(BaseModel):
    predictions: list[float]


@app.get("/health")
def health_check():
    return {"status": "ok"}


def build_feature_row(player: PredictionRequest) -> dict:
    """
    Converts a single player's request into the one-hot encoded feature
    row the model expects, matching the encoding used during training.
    """
    return {
        "was_home": int(player.was_home),
        "minutes": player.minutes,
        "form_before_gameweek": player.form_before_gameweek,
        "opponent_difficulty": player.opponent_difficulty,
        # FPL's API uses "GKP" while the historical training data used
        # "GK" - both map to the same one-hot feature.
        "position_FWD": 1 if player.position == "FWD" else 0,
        "position_GK": 1 if player.position in ("GK", "GKP") else 0,
        "position_MID": 1 if player.position == "MID" else 0,
    }


@app.post("/predict", response_model=PredictionResponse)
def predict(request: PredictionRequest):
    row = build_feature_row(request)
    X = pd.DataFrame([row])[FEATURE_COLUMNS]
    prediction = model.predict(X)[0]

    return PredictionResponse(predicted_points=round(float(prediction), 2))


@app.post("/predict/batch", response_model=BatchPredictionResponse)
def predict_batch(request: BatchPredictionRequest):
    """
    Scores multiple players in a single model call, avoiding the
    overhead of one HTTP round trip per player - used by the team
    optimiser, which needs predictions for the full player pool.
    """
    rows = [build_feature_row(player) for player in request.players]
    X = pd.DataFrame(rows)[FEATURE_COLUMNS]
    predictions = model.predict(X)

    return BatchPredictionResponse(
        predictions=[round(float(p), 2) for p in predictions]
    )
