"""
Builds the training feature set from raw gameweek data. Each row becomes
one (player, gameweek) training example with features known BEFORE that
gameweek was played, and total_points as the label to predict.
"""
import pandas as pd

df = pd.read_csv("ml-service/data/2025-26/merged_gw.csv")
teams_df = pd.read_csv("ml-service/data/2025-26/teams.csv")

df = df.sort_values(["element", "GW"]).reset_index(drop=True)


def calculate_form(group, window=3):
    """
    Form = average total_points over the past `window` calendar gameweeks.
    Uses .shift(1) so the CURRENT gameweek's points are never included -
    critical to avoid data leakage, since form must only reflect what
    was knowable BEFORE this gameweek was played.
    """
    return group["total_points"].shift(1).rolling(window=window, min_periods=1).mean()


df["form_before_gameweek"] = df.groupby("element", group_keys=False).apply(
    lambda g: calculate_form(g)
)

# Build a lookup: team id -> their home/away strength ratings
team_strength = teams_df.set_index("id")[["strength_overall_home", "strength_overall_away"]]


def get_opponent_difficulty(row):
    """
    Fixture difficulty = the OPPONENT's relevant strength rating.
    If we're playing at home, the opponent is travelling, so we use
    their AWAY strength (how strong they are on the road). If we're
    away, we use the opponent's HOME strength (how strong they are on
    their own turf) - this reflects the actual conditions of the match.
    """
    opponent_id = row["opponent_team"]
    if opponent_id not in team_strength.index:
        return None
    if row["was_home"]:
        return team_strength.loc[opponent_id, "strength_overall_away"]
    else:
        return team_strength.loc[opponent_id, "strength_overall_home"]


df["opponent_difficulty"] = df.apply(get_opponent_difficulty, axis=1)

# Drop rows with no form history yet, or no opponent difficulty found
features_df = df.dropna(subset=["form_before_gameweek", "opponent_difficulty"])

feature_columns = [
    "element", "GW", "was_home", "minutes",
    "form_before_gameweek", "opponent_difficulty", "position"
]
label_column = "total_points"

output = features_df[feature_columns + [label_column]]
output.to_csv("ml-service/data/2025-26/training_features.csv", index=False)

print(f"Built {len(output)} training rows (dropped {len(df) - len(output)})")
print(output.head(10))
