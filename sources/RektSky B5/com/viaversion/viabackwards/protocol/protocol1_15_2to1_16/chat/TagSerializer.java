/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.chat;

import com.google.common.base.Preconditions;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.gson.JsonPrimitive;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.Map;
import java.util.regex.Pattern;

@Deprecated
public class TagSerializer {
    private static final Pattern PLAIN_TEXT = Pattern.compile("[A-Za-z0-9._+-]+");

    public static String toString(JsonObject object) {
        StringBuilder builder = new StringBuilder("{");
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            Preconditions.checkArgument(entry.getValue().isJsonPrimitive());
            if (builder.length() != 1) {
                builder.append(',');
            }
            String escapedText = TagSerializer.escape(entry.getValue().getAsString());
            builder.append(entry.getKey()).append(':').append(escapedText);
        }
        return builder.append('}').toString();
    }

    public static JsonObject toJson(CompoundTag tag) {
        JsonObject object = new JsonObject();
        for (Map.Entry<String, Tag> entry : tag.entrySet()) {
            object.add(entry.getKey(), TagSerializer.toJson(entry.getValue()));
        }
        return object;
    }

    private static JsonElement toJson(Tag tag) {
        if (tag instanceof CompoundTag) {
            return TagSerializer.toJson((CompoundTag)tag);
        }
        if (tag instanceof ListTag) {
            ListTag list = (ListTag)tag;
            JsonArray array = new JsonArray();
            for (Tag listEntry : list) {
                array.add(TagSerializer.toJson(listEntry));
            }
            return array;
        }
        return new JsonPrimitive(tag.getValue().toString());
    }

    public static String escape(String s2) {
        if (PLAIN_TEXT.matcher(s2).matches()) {
            return s2;
        }
        StringBuilder builder = new StringBuilder(" ");
        int currentQuote = 0;
        for (int i2 = 0; i2 < s2.length(); ++i2) {
            int c2 = s2.charAt(i2);
            if (c2 == 92) {
                builder.append('\\');
            } else if (c2 == 34 || c2 == 39) {
                if (currentQuote == 0) {
                    int n2 = currentQuote = c2 == 34 ? 39 : 34;
                }
                if (currentQuote == c2) {
                    builder.append('\\');
                }
            }
            builder.append((char)c2);
        }
        if (currentQuote == 0) {
            currentQuote = 34;
        }
        builder.setCharAt(0, (char)currentQuote);
        builder.append((char)currentQuote);
        return builder.toString();
    }
}

