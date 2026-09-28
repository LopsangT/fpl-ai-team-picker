package com.fplai.backend.config;

/**
 *  Central season identifier for data ingestion and prediction features.
 *  Must match persisted fixture and gameweek records. Update prior to season kickoff.
 */

public class SeasonConfig {

    public static final String CURRENT_SEASON = "2026-27";

    private SeasonConfig() {}
}
