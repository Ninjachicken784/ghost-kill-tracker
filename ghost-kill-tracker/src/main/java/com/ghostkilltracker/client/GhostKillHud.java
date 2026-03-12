package com.ghostkilltracker.client;

public class KillSession {
    private int totalKills = 0;
    private int sessionKills = 0;
    private long startTime = System.currentTimeMillis();
    private boolean running = false;
    private boolean paused = false;
    private long pausedAt = 0;
    private long totalPausedTime = 0;

    // --- ADD THIS METHOD SO THE OTHER FILES WORK ---
    public long getElapsedTime() {
        if (!running) return 0;
        long now = paused ? pausedAt : System.currentTimeMillis();
        return (now - startTime) - totalPausedTime;
    }

    public void addKill() {
        if (running && !paused) {
            totalKills++;
            sessionKills++;
        }
    }

    public void start() {
        if (!running) {
            startTime = System.currentTimeMillis();
            totalPausedTime = 0;
            running = true;
            paused = false;
        } else if (paused) {
            totalPausedTime += (System.currentTimeMillis() - pausedAt);
            paused = false;
        }
    }

    public void pause() {
        if (running && !paused) {
            paused = true;
            pausedAt = System.currentTimeMillis();
        }
    }

    public void resetSession() {
        sessionKills = 0;
        startTime = System.currentTimeMillis();
        totalPausedTime = 0;
        paused = false;
    }

    public int getTotalKills() { return totalKills; }
    public int getSessionKills() { return sessionKills; }
    public boolean isRunning() { return running; }
    public boolean isPaused() { return paused; }

    public double getTotalKillsPerHour() {
        long elapsed = getElapsedTime();
        return (totalKills / (Math.max(1, elapsed) / 3600000.0));
    }

    public double getSessionKillsPerHour() {
        long elapsed = getElapsedTime();
        return (sessionKills / (Math.max(1, elapsed) / 3600000.0));
    }

    // Placeholders for your Sorrow/Plasma logic
    public int getTotalSorrow() { return 0; }
    public int getTotalPlasma() { return 0; }
    public int getSessionSorrow() { return 0; }
    public int getSessionPlasma() { return 0; }
    public String getTotalUptime() { return formatTime(getElapsedTime()); }
    public String getSessionUptime() { return formatTime(getElapsedTime()); }

    private String formatTime(long ms) {
        long sec = (ms / 1000) % 60;
        long min = (ms / (1000 * 60)) % 60;
        long hr = (ms / (1000 * 60 * 60));
        return String.format("%02d:%02d:%02d", hr, min, sec);
    }
}
