// Add these variables to your class
private int totalWorms = 0;
private long startTime = System.currentTimeMillis();

// This method listens for the specific chat message
@SubscribeEvent
public void onChatReceived(ClientChatReceivedEvent event) {
    // Get the unformatted text to avoid issues with color codes
    String message = event.message.getUnformattedText();

    if (message.contains("You hear the sound of something approaching...")) {
        totalWorms++;
        // Optional: Play a sound so you know it registered
        Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1.0F, 1.0F);
    }
}

// Helper methods for the HUD to call
public int getTotalWorms() {
    return totalWorms;
}

public double getWormsPerHour() {
    long elapsedMillis = System.currentTimeMillis() - startTime;
    // Prevent division by zero if the session just started
    if (elapsedMillis < 1000) return 0.0; 
    
    double hours = elapsedMillis / 3600000.0;
    return totalWorms / hours;
}
