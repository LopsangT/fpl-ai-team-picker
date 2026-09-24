import joblib
import pandas as pd
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI(title="FPL Points Predictor")

model = joblib.load("model.pkl")

FEATURE_COLUMNS = [
	"was_home", "minutes", "form_before_gameweek", "opponent_difficulty",
	"position_FWD", "position_GK", "position_MID"
]


class PredictionRequest(BaseModel):
	was_home: bool
	minutes: int
	form_before_gameweek: float
	opponent_difficulty: int
	position: str


class PredictionResponse(BaseModel):
	predicted_points: float


@app.get("/health")
def health_check():
	return{"status": "ok"}


@app.post("/predict", response_model=PredictionResponse)
def predict(request: PredictionRequest):
	row = {
		"was_home": int(request.was_home),
		"minutes": request.minutes,
		"form_before_gameweek": request.form_before_gameweek,
		"opponent_difficulty": request.opponent_difficulty,
		"position_FWD": 1 if request.position == "FWD" else 0,
		"position_GK": 1 if request.position == "GK" else 0,
		"position_MID": 1 if request.position == "MID" else 0,
	}
	
	X = pd.DataFrame([row])[FEATURE_COLUMNS]
	prediction = model.predict(X)[0]
	return PredictionResponse(predicted_points=round(float(prediction), 2))

