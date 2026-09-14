"""
Quick exploration of the historical gameweek data before building
features or training anything - confirms the data looks as expected
and surfaces any data quality issues early.
"""
import pandas as pd

df = pd.read_csv("ml-service/data/2025-26/merged_gw.csv")

print("Shape:", df.shape)
print("\nGameweeks covered:", sorted(df["GW"].unique()))
print("\nPositions:", df["position"].unique())
print("\nMissing values per column:")
print(df.isnull().sum()[df.isnull().sum() > 0])

print("\ntotal_points summary:")
print(df["total_points"].describe())

print("\nSample row:")
print(df.iloc[0])
