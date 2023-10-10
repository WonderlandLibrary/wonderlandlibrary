// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_12to1_11_1;

import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.JsonPrimitive;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.libs.gson.JsonElement;
import java.util.regex.Pattern;

public class ChatItemRewriter
{
    private static final Pattern indexRemoval;
    
    public static void toClient(final JsonElement element, final UserConnection user) {
        if (element instanceof JsonObject) {
            final JsonObject obj = (JsonObject)element;
            if (obj.has("hoverEvent")) {
                if (obj.get("hoverEvent") instanceof JsonObject) {
                    final JsonObject hoverEvent = (JsonObject)obj.get("hoverEvent");
                    if (hoverEvent.has("action") && hoverEvent.has("value")) {
                        final String type = hoverEvent.get("action").getAsString();
                        if (type.equals("show_item") || type.equals("show_entity")) {
                            final JsonElement value = hoverEvent.get("value");
                            if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()) {
                                final String newValue = ChatItemRewriter.indexRemoval.matcher(value.getAsString()).replaceAll("");
                                hoverEvent.addProperty("value", newValue);
                            }
                            else if (value.isJsonArray()) {
                                final JsonArray newArray = new JsonArray();
                                for (final JsonElement valueElement : value.getAsJsonArray()) {
                                    if (valueElement.isJsonPrimitive() && valueElement.getAsJsonPrimitive().isString()) {
                                        final String newValue2 = ChatItemRewriter.indexRemoval.matcher(valueElement.getAsString()).replaceAll("");
                                        newArray.add(new JsonPrimitive(newValue2));
                                    }
                                }
                                hoverEvent.add("value", newArray);
                            }
                        }
                    }
                }
            }
            else if (obj.has("extra")) {
                toClient(obj.get("extra"), user);
            }
        }
        else if (element instanceof JsonArray) {
            final JsonArray array = (JsonArray)element;
            for (final JsonElement value2 : array) {
                toClient(value2, user);
            }
        }
    }
    
    static {
        indexRemoval = Pattern.compile("(?<![\\w-.+])\\d+:(?=([^\"\\\\]*(\\\\.|\"([^\"\\\\]*\\\\.)*[^\"\\\\]*\"))*[^\"]*$)");
    }
}
