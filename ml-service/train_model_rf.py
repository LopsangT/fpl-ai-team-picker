"""
Trains a Random Forest model on the same features as the Linear
Regression baseline, to test whether it can capture non-linear
interactions (e.g. opponent difficulty mattering differently by
position) that Linear Regression couldn't.
"""
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_absolute_error, r2_score

df = pd.read_csv("ml-service/data/2025-26/training_features.csv")

df["was_home"] = df["was_home"].astype(int)

position_dummies = pd.get_dummies(df["position"], prefix="position", drop_first=True)
df = pd.concat([df, position_dummies], axis=1)

feature_columns = (
    ["was_home", "minutes", "form_before_gameweek", "opponent_difficulty"]
    + list(position_dummies.columns)
)
X = df[feature_columns]
y = df["total_points"]

# Same split, same random_state as the Linear Regression run - this
# means both models are trained and tested on IDENTICAL data, so any
# difference in results is genuinely due to the model, not the data split
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)

# n_estimators=100: builds 100 individual decision trees and averages
# their predictions - more trees generally means more stable predictions
# random_state=42: makes the randomness in tree-building reproducible
model = RandomForestRegressor(n_estimators=100, random_state=42)
model.fit(X_train, y_train)

predictions = model.predict(X_test)

mae = mean_absolute_error(y_test, predictions)
r2 = r2_score(y_test, predictions)

print(f"Mean Absolute Error: {mae:.2f} points")
print(f"R² score: {r2:.3f}")

print(f"\nFeature importances:")
importances = sorted(
    zip(feature_columns, model.feature_importances_),
    key=lambda x: x[1], reverse=True
)
for feature, importance in importances:
    print(f"  {feature}: {importance:.3f}")
