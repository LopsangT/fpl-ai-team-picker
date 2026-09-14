"""
Trains a Linear Regression model to predict total_points from
pre-gameweek features, then evaluates it on unseen data to get an
honest sense of how well it generalizes.
"""
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_absolute_error, r2_score

df = pd.read_csv("ml-service/data/2025-26/training_features.csv")

df["was_home"] = df["was_home"].astype(int)

# One-hot encode position: turns one text column ("GK"/"DEF"/"MID"/"FWD")
# into separate 0/1 columns, since Linear Regression needs numeric input
# and can't interpret category names directly. drop_first=True drops one
# category (GK) to avoid redundancy - its effect is captured in the
# intercept instead, since the dropped category becomes the baseline.
position_dummies = pd.get_dummies(df["position"], prefix="position", drop_first=True)
df = pd.concat([df, position_dummies], axis=1)

feature_columns = (
    ["was_home", "minutes", "form_before_gameweek", "opponent_difficulty"]
    + list(position_dummies.columns)
)
X = df[feature_columns]
y = df["total_points"]

X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)

model = LinearRegression()
model.fit(X_train, y_train)

predictions = model.predict(X_test)

mae = mean_absolute_error(y_test, predictions)
r2 = r2_score(y_test, predictions)

print(f"Mean Absolute Error: {mae:.2f} points")
print(f"R² score: {r2:.3f}")
print(f"\nModel coefficients:")
for feature, coef in zip(feature_columns, model.coef_):
    print(f"  {feature}: {coef:.3f}")
print(f"  intercept: {model.intercept_:.3f}")
