package com.ghostkilltracker.client;

public class KillSession {
    private int totalKills = 0;
    private int totalSorrow = 0;
    private int totalPlasma = 0;
    private int sessionKills = 0;
    private int sessionSorrow = 0;
    private int sessionPlasma = 0;
    private long sessionStartTime = System.currentTimeMillis();
    private long pausedElapsed = 0;
    private long pauseStartTime = -1;
    private boolean paused = false;
    private boolean running = false;

    public void start() {
        if (!running) {
            running = true; paused = false;
            sessionStartTime = System.currentTimeMillis();
            pausedElapsed = 0; pauseStartTime = -1;
        } else if (paused) {
            paused = false;
            if (pauseStartTime != -1) { pausedElapsed += System.currentTimeMillis() - pauseStartTime; pauseStartTime = -1; }
        }
    }

    public void pause() {
        if (running && !paused) { paused = true; pauseStartTime = System.currentTimeMillis(); }
    }

    public void resetSession() {
        sessionKills = 0; sessionSorrow = 0; sessionPlasma = 0;
        sessionStartTime = System.currentTimeMillis();
        pausedElapsed = 0; pauseStartTime = -1; paused = false;
    }

    public void addKill() { if (!running || paused) return; totalKills++; sessionKills++; }
    public void addSorrow() { if (!running || paused) return; totalSorrow++; sessionSorrow++; }
    public void addPlasma() { if (!running || paused) return; totalPlasma++; sessionPlasma++; }

    public boolean isRunning() { return running; }
    public boolean isPaused() { return paused; }
    public int getTotalKills() { return totalKills; }
    public int getTotalSorrow() { return totalSorrow; }
    public int getTotalPlasma() { return totalPlasma; }
    public int getSessionKills() { return sessionKills; }
    public int getSessionSorrow() { return sessionSorrow; }
    public int getSessionPlasma() { return sessionPlasma; }

    private long getActiveMs() {
        if (!running) return 0;
        long now = System.currentTimeMillis();
        long elapsed = now - sessionStartTime - pausedElapsed;
        if (paused && pauseStartTime != -1) elapsed -= (now - pauseStartTime);
        return Math.max(elapsed, 0);
    }

    public double getSessionKillsPerHour() {
        long ms = getActiveMs(); if (ms < 1000) return 0;
        return sessionKills / (ms / 3600000.0);
    }

    public double getTotalKillsPerHour() {
        long ms = getActiveMs(); if (ms < 1000) return 0;
        return totalKills / (ms / 3600000.0);
    }
}
