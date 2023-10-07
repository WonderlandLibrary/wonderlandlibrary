// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import com.google.gson.TypeAdapterFactory;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import java.util.Map;
import com.google.gson.JsonSerializationContext;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import java.util.List;

public interface IChatComponent extends Iterable
{
    IChatComponent setChatStyle(final ChatStyle p0);
    
    ChatStyle getChatStyle();
    
    IChatComponent appendText(final String p0);
    
    IChatComponent appendSibling(final IChatComponent p0);
    
    String getUnformattedTextForChat();
    
    String getUnformattedText();
    
    String getFormattedText();
    
    List getSiblings();
    
    IChatComponent createCopy();
    
    public static class Serializer implements JsonDeserializer, JsonSerializer
    {
        private static final Gson GSON;
        private static final String __OBFID = "CL_00001263";
        
        public IChatComponent deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) {
            if (p_deserialize_1_.isJsonPrimitive()) {
                return new ChatComponentText(p_deserialize_1_.getAsString());
            }
            if (p_deserialize_1_.isJsonObject()) {
                final JsonObject var4 = p_deserialize_1_.getAsJsonObject();
                Object var5;
                if (var4.has("text")) {
                    var5 = new ChatComponentText(var4.get("text").getAsString());
                }
                else if (var4.has("translate")) {
                    final String var6 = var4.get("translate").getAsString();
                    if (var4.has("with")) {
                        final JsonArray var7 = var4.getAsJsonArray("with");
                        final Object[] var8 = new Object[var7.size()];
                        for (int var9 = 0; var9 < var8.length; ++var9) {
                            var8[var9] = this.deserialize(var7.get(var9), p_deserialize_2_, p_deserialize_3_);
                            if (var8[var9] instanceof ChatComponentText) {
                                final ChatComponentText var10 = (ChatComponentText)var8[var9];
                                if (var10.getChatStyle().isEmpty() && var10.getSiblings().isEmpty()) {
                                    var8[var9] = var10.getChatComponentText_TextValue();
                                }
                            }
                        }
                        var5 = new ChatComponentTranslation(var6, var8);
                    }
                    else {
                        var5 = new ChatComponentTranslation(var6, new Object[0]);
                    }
                }
                else if (var4.has("score")) {
                    final JsonObject var11 = var4.getAsJsonObject("score");
                    if (!var11.has("name") || !var11.has("objective")) {
                        throw new JsonParseException("A score component needs a least a name and an objective");
                    }
                    var5 = new ChatComponentScore(JsonUtils.getJsonObjectStringFieldValue(var11, "name"), JsonUtils.getJsonObjectStringFieldValue(var11, "objective"));
                    if (var11.has("value")) {
                        ((ChatComponentScore)var5).func_179997_b(JsonUtils.getJsonObjectStringFieldValue(var11, "value"));
                    }
                }
                else {
                    if (!var4.has("selector")) {
                        throw new JsonParseException("Don't know how to turn " + p_deserialize_1_.toString() + " into a Component");
                    }
                    var5 = new ChatComponentSelector(JsonUtils.getJsonObjectStringFieldValue(var4, "selector"));
                }
                if (var4.has("extra")) {
                    final JsonArray var12 = var4.getAsJsonArray("extra");
                    if (var12.size() <= 0) {
                        throw new JsonParseException("Unexpected empty array of components");
                    }
                    for (int var13 = 0; var13 < var12.size(); ++var13) {
                        ((IChatComponent)var5).appendSibling(this.deserialize(var12.get(var13), p_deserialize_2_, p_deserialize_3_));
                    }
                }
                ((IChatComponent)var5).setChatStyle((ChatStyle)p_deserialize_3_.deserialize(p_deserialize_1_, (Type)ChatStyle.class));
                return (IChatComponent)var5;
            }
            if (p_deserialize_1_.isJsonArray()) {
                final JsonArray var14 = p_deserialize_1_.getAsJsonArray();
                IChatComponent var15 = null;
                for (final JsonElement var17 : var14) {
                    final IChatComponent var18 = this.deserialize(var17, var17.getClass(), p_deserialize_3_);
                    if (var15 == null) {
                        var15 = var18;
                    }
                    else {
                        var15.appendSibling(var18);
                    }
                }
                return var15;
            }
            throw new JsonParseException("Don't know how to turn " + p_deserialize_1_.toString() + " into a Component");
        }
        
        private void serializeChatStyle(final ChatStyle style, final JsonObject object, final JsonSerializationContext ctx) {
            final JsonElement var4 = ctx.serialize((Object)style);
            if (var4.isJsonObject()) {
                final JsonObject var5 = (JsonObject)var4;
                for (final Map.Entry var7 : var5.entrySet()) {
                    object.add((String)var7.getKey(), (JsonElement)var7.getValue());
                }
            }
        }
        
        public JsonElement serialize(final IChatComponent p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            if (p_serialize_1_ instanceof ChatComponentText && p_serialize_1_.getChatStyle().isEmpty() && p_serialize_1_.getSiblings().isEmpty()) {
                return (JsonElement)new JsonPrimitive(((ChatComponentText)p_serialize_1_).getChatComponentText_TextValue());
            }
            final JsonObject var4 = new JsonObject();
            if (!p_serialize_1_.getChatStyle().isEmpty()) {
                this.serializeChatStyle(p_serialize_1_.getChatStyle(), var4, p_serialize_3_);
            }
            if (!p_serialize_1_.getSiblings().isEmpty()) {
                final JsonArray var5 = new JsonArray();
                for (final IChatComponent var7 : p_serialize_1_.getSiblings()) {
                    var5.add(this.serialize(var7, var7.getClass(), p_serialize_3_));
                }
                var4.add("extra", (JsonElement)var5);
            }
            if (p_serialize_1_ instanceof ChatComponentText) {
                var4.addProperty("text", ((ChatComponentText)p_serialize_1_).getChatComponentText_TextValue());
            }
            else if (p_serialize_1_ instanceof ChatComponentTranslation) {
                final ChatComponentTranslation var8 = (ChatComponentTranslation)p_serialize_1_;
                var4.addProperty("translate", var8.getKey());
                if (var8.getFormatArgs() != null && var8.getFormatArgs().length > 0) {
                    final JsonArray var9 = new JsonArray();
                    for (final Object var13 : var8.getFormatArgs()) {
                        if (var13 instanceof IChatComponent) {
                            var9.add(this.serialize((IChatComponent)var13, var13.getClass(), p_serialize_3_));
                        }
                        else {
                            var9.add((JsonElement)new JsonPrimitive(String.valueOf(var13)));
                        }
                    }
                    var4.add("with", (JsonElement)var9);
                }
            }
            else if (p_serialize_1_ instanceof ChatComponentScore) {
                final ChatComponentScore var14 = (ChatComponentScore)p_serialize_1_;
                final JsonObject var15 = new JsonObject();
                var15.addProperty("name", var14.func_179995_g());
                var15.addProperty("objective", var14.func_179994_h());
                var15.addProperty("value", var14.getUnformattedTextForChat());
                var4.add("score", (JsonElement)var15);
            }
            else {
                if (!(p_serialize_1_ instanceof ChatComponentSelector)) {
                    throw new IllegalArgumentException("Don't know how to serialize " + p_serialize_1_ + " as a Component");
                }
                final ChatComponentSelector var16 = (ChatComponentSelector)p_serialize_1_;
                var4.addProperty("selector", var16.func_179992_g());
            }
            return (JsonElement)var4;
        }
        
        public static String componentToJson(final IChatComponent component) {
            return Serializer.GSON.toJson((Object)component);
        }
        
        public static IChatComponent jsonToComponent(final String json) {
            return (IChatComponent)Serializer.GSON.fromJson(json, (Class)IChatComponent.class);
        }
        
        public JsonElement serialize(final Object p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            return this.serialize((IChatComponent)p_serialize_1_, p_serialize_2_, p_serialize_3_);
        }
        
        static {
            final GsonBuilder var0 = new GsonBuilder();
            var0.registerTypeHierarchyAdapter((Class)IChatComponent.class, (Object)new Serializer());
            var0.registerTypeHierarchyAdapter((Class)ChatStyle.class, (Object)new ChatStyle.Serializer());
            var0.registerTypeAdapterFactory((TypeAdapterFactory)new EnumTypeAdapterFactory());
            GSON = var0.create();
        }
    }
}
