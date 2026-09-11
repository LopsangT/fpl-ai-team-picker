"""
Historical FPL Data Ingestion:
The current database only has live/current season data. The model needs past data to train on.
So historical FPL gameweek data will be downloaded from vaastav/Fantasy-Premier-League GitHub archive which
gives real completed-season data with known outcomes to train the model on.
"""

import pandas as pd
import os 

SEASON = "2025-26" # Most recent completed season avaliable in the Vaastav archive
BASE_URL = f"https://raw.githubusercontent.com/vaastav/Fantasy-Premier-League/master/data/{SEASON}"


OUTPUT_DIR = os.path.join(os.path.dirname(__file__), SEASON)
os.makedirs(OUTPUT_DIR, exist_ok=True)

url = f"{BASE_URL }/gws/merged_gw.csv"
print(f"Downloading {url}...")

df = pd.read_csv(url, encoding="utf-8-sig")
output_path = os.path.join(OUTPUT_DIR, "merged_gw.csv")
df.to_csv(output_path, index=False)

print(f"Saved {len(df)} rows to {output_path}")
print(f"Columns: {list(df.columns)}")

