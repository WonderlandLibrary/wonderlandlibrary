// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.chat;

import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.gson.JsonPrimitive;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viabackwards.api.BackwardsProtocol;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;

public class TranslatableRewriter1_16 extends TranslatableRewriter
{
    private static final ChatColor[] COLORS;
    
    public TranslatableRewriter1_16(final BackwardsProtocol protocol) {
        super(protocol);
    }
    
    @Override
    public void processText(final JsonElement value) {
        super.processText(value);
        if (value == null || !value.isJsonObject()) {
            return;
        }
        final JsonObject object = value.getAsJsonObject();
        final JsonPrimitive color = object.getAsJsonPrimitive("color");
        if (color != null) {
            final String colorName = color.getAsString();
            if (!colorName.isEmpty() && colorName.charAt(0) == '#') {
                final int rgb = Integer.parseInt(colorName.substring(1), 16);
                final String closestChatColor = this.getClosestChatColor(rgb);
                object.addProperty("color", closestChatColor);
            }
        }
        final JsonObject hoverEvent = object.getAsJsonObject("hoverEvent");
        if (hoverEvent != null) {
            try {
                final Component component = ChatRewriter.HOVER_GSON_SERIALIZER.deserializeFromTree(object);
                final JsonObject processedHoverEvent = ((JsonObject)ChatRewriter.HOVER_GSON_SERIALIZER.serializeToTree(component)).getAsJsonObject("hoverEvent");
                processedHoverEvent.remove("contents");
                object.add("hoverEvent", processedHoverEvent);
            }
            catch (Exception e) {
                ViaBackwards.getPlatform().getLogger().severe("Error converting hover event component: " + object);
                e.printStackTrace();
            }
        }
    }
    
    private String getClosestChatColor(final int rgb) {
        final int r = rgb >> 16 & 0xFF;
        final int g = rgb >> 8 & 0xFF;
        final int b = rgb & 0xFF;
        ChatColor closest = null;
        int smallestDiff = 0;
        for (final ChatColor color : TranslatableRewriter1_16.COLORS) {
            if (color.rgb == rgb) {
                return color.colorName;
            }
            final int rAverage = (color.r + r) / 2;
            final int rDiff = color.r - r;
            final int gDiff = color.g - g;
            final int bDiff = color.b - b;
            final int diff = (2 + (rAverage >> 8)) * rDiff * rDiff + 4 * gDiff * gDiff + (2 + (255 - rAverage >> 8)) * bDiff * bDiff;
            if (closest == null || diff < smallestDiff) {
                closest = color;
                smallestDiff = diff;
            }
        }
        return closest.colorName;
    }
    
    static {
        COLORS = new ChatColor[] { new ChatColor("black", 0), new ChatColor("dark_blue", 170), new ChatColor("dark_green", 43520), new ChatColor("dark_aqua", 43690), new ChatColor("dark_red", 11141120), new ChatColor("dark_purple", 11141290), new ChatColor("gold", 16755200), new ChatColor("gray", 11184810), new ChatColor("dark_gray", 5592405), new ChatColor("blue", 5592575), new ChatColor("green", 5635925), new ChatColor("aqua", 5636095), new ChatColor("red", 16733525), new ChatColor("light_purple", 16733695), new ChatColor("yellow", 16777045), new ChatColor("white", 16777215) };
    }
    
    private static final class ChatColor
    {
        private final String colorName;
        private final int rgb;
        private final int r;
        private final int g;
        private final int b;
        
        ChatColor(final String colorName, final int rgb) {
            this.colorName = colorName;
            this.rgb = rgb;
            this.r = (rgb >> 16 & 0xFF);
            this.g = (rgb >> 8 & 0xFF);
            this.b = (rgb & 0xFF);
        }
    }
}
