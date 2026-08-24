# FPL AI Team Picker

I am building this project to predict Fantasy Premier League (FPL) player points using machine learning,
with the ultimate goal of earning the highest points in the league. The AI will build the optimal 15 player squad and
will field the most optimal starting 11 during each game week while strictly following the FPL's rules 
(£100m budget, max 3 players per club and valid formation rules).

## Why I am building this

I am a huge football fan who plays FPL every season, I want to see if an AI - driven model can outperform
human managers in decision making by combining predictive modeling with a clean microservice architecture.

## Architecture

I have split the system into three services so each one has a single responsibility:
- React frontend: Contains an interactive dashboard where the most optimal team is displayed and users
  are free to compare and swap players to build their own custom lineups.
- Spring Boot backend: Responsible for fetching data from the FPL API, orchestrates calls to the ML service
  and handles persistence with PostgreSQL.
- FastAPI ML service: Uses a Python microservice that serves predictions from a scikit-learn model which
  is kept separate from the Java backend so the ML logic can evolve independently.

## Tech Stack

- **Frontend:** React
- **Backend:** Spring Boot (Java)
- **ML service:** FastAPI + scikit-learn + Pandas
- **Database:** PostgreSQL
- **Data source:** [FPL public API](https://fantasy.premierleague.com/api/)

## Planned Features

- **Weekly starting XI and captaincy recommendations** - Ai automatically chooses the best 11 of the 15 owned players gameweek by factoring in predicted points, historical performance against upcoming opponents and injury/playing time status.
- **Chip strategy recommendations** - (Bench Boost, Triple Captain, Wildcard & Free Hit) using the predicted points model Ai will determine which game week to play each chip in.

## Potential Features
- **AWS deployment** - deploy the backend & ML service (ECS), database (RDS), and frontend (S3 + CloudFront) so the project is live.
- **User Accounts** - give the user the option to create an account where they can save their own team and get personalized recommendations by using JWT authentication and Spring Security.

## Status
🚧 Currently in progress - see commit history for full progress log
