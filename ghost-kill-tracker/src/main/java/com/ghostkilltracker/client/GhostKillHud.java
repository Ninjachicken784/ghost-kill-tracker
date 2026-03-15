@SubscribeEvent
public void onRenderGui(RenderGameOverlayEvent.Post event) {
    if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

    FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
    
    // Get data from your Client class
    int total = trackerClient.getTotalWorms();
    double rate = trackerClient.getWormsPerHour();

    // Format the strings
    String totalStr = "Total Worms: §a" + total;
    String rateStr = String.format("Worms/h: §e%.2f", rate);

    // Draw to screen (adjust X and Y coordinates as needed)
    fr.drawStringWithShadow(totalStr, 10, 10, 0xFFFFFF);
    fr.drawStringWithShadow(rateStr, 10, 20, 0xFFFFFF);
}
