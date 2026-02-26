package com.ghostkilltracker.client;

public class KillSession {
    private int totalKills = 0;
    private long totalCoins = 0;
    private long sessionStartTime = System.currentTimeMillis();

    public void reset() {
        totalKills = 0;
        totalCoins = 0;
        sessionStartTime = System.currentTimeMillis();
    }

    public void addKill(long coinsEarned) {
        totalKills++;
        totalCoins += coinsEarned;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public double getKillsPerHour() {
        long elapsedMs = System.currentTimeMillis() - sessionStartTime;
        if (elapsedMs < 1000) return 0;
        double elapsedHours = elapsedMs / 3600000.0;
        return totalKills / elapsedHours;
    }

    public double getAvgCoinsPerKill() {
        if (totalKills == 0) return 0;
        return (double) totalCoins / totalKills;
    }

    public long getTotalCoins() {
        return totalCoins;
    }

    public long getSessionStartTime() {
        return sessionStartTime;
    }
}
