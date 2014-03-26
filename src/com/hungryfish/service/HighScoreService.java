package com.hungryfish.service;

import java.util.List;

/**
 * User: Breku
 * Date: 24.03.14
 */
public class HighScoreService extends BaseService {

    public void addScore(Integer score) {
        databaseHelper.addScore(score);
    }

    public List<Integer> getHighScores() {
        return databaseHelper.getHighScores();
    }

}
