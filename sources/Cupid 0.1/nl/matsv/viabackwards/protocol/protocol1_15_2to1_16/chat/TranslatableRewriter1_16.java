package nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.chat;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.viaversion.libs.gson.JsonPrimitive;

public class TranslatableRewriter1_16 extends TranslatableRewriter {
  private static final ChatColor[] COLORS = new ChatColor[] { 
      new ChatColor("black", 0), new ChatColor("dark_blue", 170), new ChatColor("dark_green", 43520), new ChatColor("dark_aqua", 43690), new ChatColor("dark_red", 11141120), new ChatColor("dark_purple", 11141290), new ChatColor("gold", 16755200), new ChatColor("gray", 11184810), new ChatColor("dark_gray", 5592405), new ChatColor("blue", 5592575), 
      new ChatColor("green", 5635925), new ChatColor("aqua", 5636095), new ChatColor("red", 16733525), new ChatColor("light_purple", 16733695), new ChatColor("yellow", 16777045), new ChatColor("white", 16777215) };
  
  public TranslatableRewriter1_16(BackwardsProtocol protocol) {
    super(protocol);
  }
  
  public void processText(JsonElement value) {
    super.processText(value);
    if (!value.isJsonObject())
      return; 
    JsonObject object = value.getAsJsonObject();
    JsonPrimitive color = object.getAsJsonPrimitive("color");
    if (color != null) {
      String colorName = color.getAsString();
      if (!colorName.isEmpty() && colorName.charAt(0) == '#') {
        int rgb = Integer.parseInt(colorName.substring(1), 16);
        String closestChatColor = getClosestChatColor(rgb);
        object.addProperty("color", closestChatColor);
      } 
    } 
  }
  
  protected void handleHoverEvent(JsonObject hoverEvent) {
    JsonObject item;
    JsonElement count;
    JsonObject entity, name, hoverObject;
    JsonElement contentsElement = hoverEvent.remove("contents");
    if (contentsElement == null)
      return; 
    String action = hoverEvent.getAsJsonPrimitive("action").getAsString();
    switch (action) {
      case "show_text":
        processText(contentsElement);
        hoverEvent.add("value", contentsElement);
        break;
      case "show_item":
        item = contentsElement.getAsJsonObject();
        count = item.remove("count");
        item.addProperty("Count", Byte.valueOf((count != null) ? count.getAsByte() : 1));
        hoverEvent.addProperty("value", TagSerializer.toString(item));
        break;
      case "show_entity":
        entity = contentsElement.getAsJsonObject();
        name = entity.getAsJsonObject("name");
        if (name != null) {
          processText((JsonElement)name);
          entity.addProperty("name", name.toString());
        } 
        hoverObject = new JsonObject();
        hoverObject.addProperty("text", TagSerializer.toString(entity));
        hoverEvent.add("value", (JsonElement)hoverObject);
        break;
    } 
  }
  
  private String getClosestChatColor(int rgb) {
    int r = rgb >> 16 & 0xFF;
    int g = rgb >> 8 & 0xFF;
    int b = rgb & 0xFF;
    ChatColor closest = null;
    int smallestDiff = 0;
    for (ChatColor color : COLORS) {
      if (color.rgb == rgb)
        return color.colorName; 
      int rAverage = (color.r + r) / 2;
      int rDiff = color.r - r;
      int gDiff = color.g - g;
      int bDiff = color.b - b;
      int diff = (2 + (rAverage >> 8)) * rDiff * rDiff + 4 * gDiff * gDiff + (2 + (255 - rAverage >> 8)) * bDiff * bDiff;
      if (closest == null || diff < smallestDiff) {
        closest = color;
        smallestDiff = diff;
      } 
    } 
    return closest.colorName;
  }
  
  private static final class ChatColor {
    private final String colorName;
    
    private final int rgb;
    
    private final int r;
    
    private final int g;
    
    private final int b;
    
    ChatColor(String colorName, int rgb) {
      this.colorName = colorName;
      this.rgb = rgb;
      this.r = rgb >> 16 & 0xFF;
      this.g = rgb >> 8 & 0xFF;
      this.b = rgb & 0xFF;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_15_2to1_16\chat\TranslatableRewriter1_16.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */