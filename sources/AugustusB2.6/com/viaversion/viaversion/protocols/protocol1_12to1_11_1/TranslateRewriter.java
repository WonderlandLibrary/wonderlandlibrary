// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_12to1_11_1;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.data.AchievementTranslationMapping;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.rewriter.ComponentRewriter;

public class TranslateRewriter
{
    private static final ComponentRewriter achievementTextRewriter;
    
    public static void toClient(final JsonElement element, final UserConnection user) {
        if (element instanceof JsonObject) {
            final JsonObject obj = (JsonObject)element;
            final JsonElement translate = obj.get("translate");
            if (translate != null && translate.getAsString().startsWith("chat.type.achievement")) {
                TranslateRewriter.achievementTextRewriter.processText(obj);
            }
        }
    }
    
    static {
        achievementTextRewriter = new ComponentRewriter() {
            @Override
            protected void handleTranslate(final JsonObject object, final String translate) {
                final String text = AchievementTranslationMapping.get(translate);
                if (text != null) {
                    object.addProperty("translate", text);
                }
            }
            
            @Override
            protected void handleHoverEvent(final JsonObject hoverEvent) {
                final String action = hoverEvent.getAsJsonPrimitive("action").getAsString();
                if (!action.equals("show_achievement")) {
                    super.handleHoverEvent(hoverEvent);
                    return;
                }
                final JsonElement value = hoverEvent.get("value");
                String textValue;
                if (value.isJsonObject()) {
                    textValue = value.getAsJsonObject().get("text").getAsString();
                }
                else {
                    textValue = value.getAsJsonPrimitive().getAsString();
                }
                if (AchievementTranslationMapping.get(textValue) == null) {
                    final JsonObject invalidText = new JsonObject();
                    invalidText.addProperty("text", "Invalid statistic/achievement!");
                    invalidText.addProperty("color", "red");
                    hoverEvent.addProperty("action", "show_text");
                    hoverEvent.add("value", invalidText);
                    super.handleHoverEvent(hoverEvent);
                    return;
                }
                try {
                    final JsonObject newLine = new JsonObject();
                    newLine.addProperty("text", "\n");
                    final JsonArray baseArray = new JsonArray();
                    baseArray.add("");
                    final JsonObject namePart = new JsonObject();
                    final JsonObject typePart = new JsonObject();
                    baseArray.add(namePart);
                    baseArray.add(newLine);
                    baseArray.add(typePart);
                    if (textValue.startsWith("achievement")) {
                        namePart.addProperty("translate", textValue);
                        namePart.addProperty("color", AchievementTranslationMapping.isSpecial(textValue) ? "dark_purple" : "green");
                        typePart.addProperty("translate", "stats.tooltip.type.achievement");
                        final JsonObject description = new JsonObject();
                        typePart.addProperty("italic", true);
                        description.addProperty("translate", value + ".desc");
                        baseArray.add(newLine);
                        baseArray.add(description);
                    }
                    else if (textValue.startsWith("stat")) {
                        namePart.addProperty("translate", textValue);
                        namePart.addProperty("color", "gray");
                        typePart.addProperty("translate", "stats.tooltip.type.statistic");
                        typePart.addProperty("italic", true);
                    }
                    hoverEvent.addProperty("action", "show_text");
                    hoverEvent.add("value", baseArray);
                }
                catch (Exception e) {
                    Via.getPlatform().getLogger().warning("Error rewriting show_achievement: " + hoverEvent);
                    e.printStackTrace();
                    final JsonObject invalidText2 = new JsonObject();
                    invalidText2.addProperty("text", "Invalid statistic/achievement!");
                    invalidText2.addProperty("color", "red");
                    hoverEvent.addProperty("action", "show_text");
                    hoverEvent.add("value", invalidText2);
                }
                super.handleHoverEvent(hoverEvent);
            }
        };
    }
}
