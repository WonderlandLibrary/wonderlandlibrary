/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonPrimitive
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 *  com.google.gson.TypeAdapterFactory
 */
package net.minecraft.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapterFactory;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import net.minecraft.util.ChatComponentScore;
import net.minecraft.util.ChatComponentSelector;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.JsonUtils;

public interface IChatComponent
extends Iterable {
    public IChatComponent setChatStyle(ChatStyle var1);

    public ChatStyle getChatStyle();

    public IChatComponent appendText(String var1);

    public IChatComponent appendSibling(IChatComponent var1);

    public String getUnformattedTextForChat();

    public String getUnformattedText();

    public String getFormattedText();

    public List getSiblings();

    public IChatComponent createCopy();

    public static class Serializer
    implements JsonDeserializer,
    JsonSerializer {
        private static final Gson GSON;
        private static final String __OBFID = "CL_00001263";

        static {
            GsonBuilder var0 = new GsonBuilder();
            var0.registerTypeHierarchyAdapter(IChatComponent.class, (Object)new Serializer());
            var0.registerTypeHierarchyAdapter(ChatStyle.class, (Object)new ChatStyle.Serializer());
            var0.registerTypeAdapterFactory((TypeAdapterFactory)new EnumTypeAdapterFactory());
            GSON = var0.create();
        }

        public IChatComponent deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) {
            ChatComponentStyle var5;
            if (p_deserialize_1_.isJsonPrimitive()) {
                return new ChatComponentText(p_deserialize_1_.getAsString());
            }
            if (!p_deserialize_1_.isJsonObject()) {
                if (p_deserialize_1_.isJsonArray()) {
                    JsonArray var11 = p_deserialize_1_.getAsJsonArray();
                    IChatComponent var12 = null;
                    for (JsonElement var17 : var11) {
                        IChatComponent var18 = this.deserialize(var17, var17.getClass(), p_deserialize_3_);
                        if (var12 == null) {
                            var12 = var18;
                            continue;
                        }
                        var12.appendSibling(var18);
                    }
                    return var12;
                }
                throw new JsonParseException("Don't know how to turn " + p_deserialize_1_.toString() + " into a Component");
            }
            JsonObject var4 = p_deserialize_1_.getAsJsonObject();
            if (var4.has("text")) {
                var5 = new ChatComponentText(var4.get("text").getAsString());
            } else if (var4.has("translate")) {
                String var6 = var4.get("translate").getAsString();
                if (var4.has("with")) {
                    JsonArray var7 = var4.getAsJsonArray("with");
                    Object[] var8 = new Object[var7.size()];
                    for (int var9 = 0; var9 < var8.length; ++var9) {
                        ChatComponentText var10;
                        var8[var9] = this.deserialize(var7.get(var9), p_deserialize_2_, p_deserialize_3_);
                        if (!(var8[var9] instanceof ChatComponentText) || !(var10 = (ChatComponentText)var8[var9]).getChatStyle().isEmpty() || !var10.getSiblings().isEmpty()) continue;
                        var8[var9] = var10.getChatComponentText_TextValue();
                    }
                    var5 = new ChatComponentTranslation(var6, var8);
                } else {
                    var5 = new ChatComponentTranslation(var6, new Object[0]);
                }
            } else if (var4.has("score")) {
                JsonObject var13 = var4.getAsJsonObject("score");
                if (!var13.has("name") || !var13.has("objective")) {
                    throw new JsonParseException("A score component needs a least a name and an objective");
                }
                var5 = new ChatComponentScore(JsonUtils.getJsonObjectStringFieldValue(var13, "name"), JsonUtils.getJsonObjectStringFieldValue(var13, "objective"));
                if (var13.has("value")) {
                    ((ChatComponentScore)var5).func_179997_b(JsonUtils.getJsonObjectStringFieldValue(var13, "value"));
                }
            } else {
                if (!var4.has("selector")) {
                    throw new JsonParseException("Don't know how to turn " + p_deserialize_1_.toString() + " into a Component");
                }
                var5 = new ChatComponentSelector(JsonUtils.getJsonObjectStringFieldValue(var4, "selector"));
            }
            if (var4.has("extra")) {
                JsonArray var14 = var4.getAsJsonArray("extra");
                if (var14.size() <= 0) {
                    throw new JsonParseException("Unexpected empty array of components");
                }
                for (int var16 = 0; var16 < var14.size(); ++var16) {
                    ((IChatComponent)var5).appendSibling(this.deserialize(var14.get(var16), p_deserialize_2_, p_deserialize_3_));
                }
            }
            ((IChatComponent)var5).setChatStyle((ChatStyle)p_deserialize_3_.deserialize(p_deserialize_1_, ChatStyle.class));
            return var5;
        }

        private void serializeChatStyle(ChatStyle style, JsonObject object, JsonSerializationContext ctx) {
            JsonElement var4 = ctx.serialize((Object)style);
            if (var4.isJsonObject()) {
                JsonObject var5 = (JsonObject)var4;
                for (Map.Entry var7 : var5.entrySet()) {
                    object.add((String)var7.getKey(), (JsonElement)var7.getValue());
                }
            }
        }

        public JsonElement serialize(IChatComponent p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
            if (p_serialize_1_ instanceof ChatComponentText && p_serialize_1_.getChatStyle().isEmpty() && p_serialize_1_.getSiblings().isEmpty()) {
                return new JsonPrimitive(((ChatComponentText)p_serialize_1_).getChatComponentText_TextValue());
            }
            JsonObject var4 = new JsonObject();
            if (!p_serialize_1_.getChatStyle().isEmpty()) {
                this.serializeChatStyle(p_serialize_1_.getChatStyle(), var4, p_serialize_3_);
            }
            if (!p_serialize_1_.getSiblings().isEmpty()) {
                JsonArray var5 = new JsonArray();
                for (IChatComponent var7 : p_serialize_1_.getSiblings()) {
                    var5.add(this.serialize(var7, var7.getClass(), p_serialize_3_));
                }
                var4.add("extra", (JsonElement)var5);
            }
            if (p_serialize_1_ instanceof ChatComponentText) {
                var4.addProperty("text", ((ChatComponentText)p_serialize_1_).getChatComponentText_TextValue());
            } else if (p_serialize_1_ instanceof ChatComponentTranslation) {
                ChatComponentTranslation var11 = (ChatComponentTranslation)p_serialize_1_;
                var4.addProperty("translate", var11.getKey());
                if (var11.getFormatArgs() != null && var11.getFormatArgs().length > 0) {
                    JsonArray var14 = new JsonArray();
                    for (Object var10 : var11.getFormatArgs()) {
                        if (var10 instanceof IChatComponent) {
                            var14.add(this.serialize((IChatComponent)var10, var10.getClass(), p_serialize_3_));
                            continue;
                        }
                        var14.add((JsonElement)new JsonPrimitive(String.valueOf(var10)));
                    }
                    var4.add("with", (JsonElement)var14);
                }
            } else if (p_serialize_1_ instanceof ChatComponentScore) {
                ChatComponentScore var12 = (ChatComponentScore)p_serialize_1_;
                JsonObject var15 = new JsonObject();
                var15.addProperty("name", var12.func_179995_g());
                var15.addProperty("objective", var12.func_179994_h());
                var15.addProperty("value", var12.getUnformattedTextForChat());
                var4.add("score", (JsonElement)var15);
            } else {
                if (!(p_serialize_1_ instanceof ChatComponentSelector)) {
                    throw new IllegalArgumentException("Don't know how to serialize " + p_serialize_1_ + " as a Component");
                }
                ChatComponentSelector var13 = (ChatComponentSelector)p_serialize_1_;
                var4.addProperty("selector", var13.func_179992_g());
            }
            return var4;
        }

        public static String componentToJson(IChatComponent component) {
            return GSON.toJson((Object)component);
        }

        public static IChatComponent jsonToComponent(String json) {
            return (IChatComponent)GSON.fromJson(json, IChatComponent.class);
        }

        public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
            return this.serialize((IChatComponent)p_serialize_1_, p_serialize_2_, p_serialize_3_);
        }
    }
}

