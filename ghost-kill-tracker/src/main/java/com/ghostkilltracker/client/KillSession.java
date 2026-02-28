package com.ghostkilltracker.client;

public class KillSession {
    // Total - never resets
    private int totalKills = 0;
    private int totalSorrow = 0;
    private int totalPlasma = 0;
    private long totalStartTime = -1;
    private long totalPausedElapsed = 0;

    // Session - resets on R
    private int sessionKills = 0;
    private int sessionSorrow = 0;
    private int sessionPlasma = 0;
    private long sessionStartTime = -1;
    private long sessionPausedElapsed = 0;

    private long pauseStartTime = -1;
    private boolean paused = false;
    private boolean running = false;

    public void start() {
        if (!running) {
            running = true;
            paused = false;
            long now = System.currentTimeMillis();
            totalStartTime = now;
            sessionStartTime = now;
            totalPausedElapsed = 0;
            sessionPausedElapsed = 0;
            pauseStartTime = -1;
        } else if (paused) {
            paused = false;
            if (pauseStartTime != -1) {
                long pauseDur = System.currentTimeMillis() - pauseStartTime;
                totalPausedElapsed += pauseDur;
                sessionPausedElapsed += pauseDur;
                pauseStartTime = -1;
            }
        }
    }

    public void pause() {
        if (running && !paused) {
            paused = true;
            pauseStartTime = System.currentTimeMillis();
        }
    }

    public void resetSession() {
        sessionKills = 0;
        sessionSorrow = 0;
        sessionPlasma = 0;
        sessionStartTime = System.currentTimeMillis();
        sessionPausedElapsed = 0;
        if (paused && pauseStartTime != -1) {
            // Don't count current pause in new session
            pauseStartTime = System.currentTimeMillis();
        }
    }

    public void addKill()   { if (!running || paused) return; totalKills++; sessionKills++; }
    public void addSorrow() { if (!running || paused) return; totalSorrow++; sessionSorrow++; }
    public void addPlasma() { if (!running || paused) return; totalPlasma++; sessionPlasma++; }

    public boolean isRunning() { return running; }
    public boolean isPaused()  { return paused; }

    public int getTotalKills()   { return totalKills; }
    public int getTotalSorrow()  { return totalSorrow; }
    public int getTotalPlasma()  { return totalPlasma; }
    public int getSessionKills()  { return sessionKills; }
    public int getSessionSorrow() { return sessionSorrow; }
    public int getSessionPlasma() { return sessionPlasma; }

    private long getTotalActiveMs() {
        if (!running || totalStartTime < 0) return 0;
        long now = System.currentTimeMillis();
        long elapsed = now - totalStartTime - totalPausedElapsed;
        if (paused && pauseStartTime != -1) elapsed -= (now - pauseStartTime);
        return Math.max(elapsed, 0);
    }

    private long getSessionActiveMs() {
        if (!running || sessionStartTime < 0) return 0;
        long now = System.currentTimeMillis();
        long elapsed = now - sessionStartTime - sessionPausedElapsed;
        if (paused && pauseStartTime != -1) elapsed -= (now - pauseStartTime);
        return Math.max(elapsed, 0);
    }

    public double getTotalKillsPerHour() {
        long ms = getTotalActiveMs();
        if (ms < 1000) return 0;
        return totalKills / (ms / 3600000.0);
    }

    public double getSessionKillsPerHour() {
        long ms = getSessionActiveMs();
        if (ms < 1000) return 0;
        return sessionKills / (ms / 3600000.0);
    }

    public String getTotalUptime() { return formatTime(getTotalActiveMs()); }
    public String getSessionUptime() { return formatTime(getSessionActiveMs()); }

    private String formatTime(long ms) {
        long s = ms / 1000;
        long h = s / 3600;
        long m = (s % 3600) / 60;
        long sec = s % 60;
        if (h > 0) return String.format("%dh %02dm", h, m);
        return String.format("%dm %02ds", m, sec);
    }
}
