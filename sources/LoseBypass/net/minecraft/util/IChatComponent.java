/*
 * Decompiled with CFR 0.152.
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
import java.lang.reflect.Type;
import java.util.Iterator;
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
extends Iterable<IChatComponent> {
    public IChatComponent setChatStyle(ChatStyle var1);

    public ChatStyle getChatStyle();

    public IChatComponent appendText(String var1);

    public IChatComponent appendSibling(IChatComponent var1);

    public String getUnformattedTextForChat();

    public String getUnformattedText();

    public String getFormattedText();

    public List<IChatComponent> getSiblings();

    public IChatComponent createCopy();

    public static class Serializer
    implements JsonDeserializer<IChatComponent>,
    JsonSerializer<IChatComponent> {
        private static final Gson GSON;

        @Override
        public IChatComponent deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            if (p_deserialize_1_.isJsonPrimitive()) {
                return new ChatComponentText(p_deserialize_1_.getAsString());
            }
            if (p_deserialize_1_.isJsonObject()) {
                ChatComponentStyle ichatcomponent;
                JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
                if (jsonobject.has("text")) {
                    ichatcomponent = new ChatComponentText(jsonobject.get("text").getAsString());
                } else if (jsonobject.has("translate")) {
                    String s = jsonobject.get("translate").getAsString();
                    if (jsonobject.has("with")) {
                        JsonArray jsonarray = jsonobject.getAsJsonArray("with");
                        Object[] aobject = new Object[jsonarray.size()];
                        for (int i = 0; i < aobject.length; ++i) {
                            ChatComponentText chatcomponenttext;
                            aobject[i] = this.deserialize(jsonarray.get(i), p_deserialize_2_, p_deserialize_3_);
                            if (!(aobject[i] instanceof ChatComponentText) || !(chatcomponenttext = (ChatComponentText)aobject[i]).getChatStyle().isEmpty() || !chatcomponenttext.getSiblings().isEmpty()) continue;
                            aobject[i] = chatcomponenttext.getChatComponentText_TextValue();
                        }
                        ichatcomponent = new ChatComponentTranslation(s, aobject);
                    } else {
                        ichatcomponent = new ChatComponentTranslation(s, new Object[0]);
                    }
                } else if (jsonobject.has("score")) {
                    JsonObject jsonobject1 = jsonobject.getAsJsonObject("score");
                    if (!jsonobject1.has("name")) throw new JsonParseException("A score component needs a least a name and an objective");
                    if (!jsonobject1.has("objective")) {
                        throw new JsonParseException("A score component needs a least a name and an objective");
                    }
                    ichatcomponent = new ChatComponentScore(JsonUtils.getString(jsonobject1, "name"), JsonUtils.getString(jsonobject1, "objective"));
                    if (jsonobject1.has("value")) {
                        ((ChatComponentScore)ichatcomponent).setValue(JsonUtils.getString(jsonobject1, "value"));
                    }
                } else {
                    if (!jsonobject.has("selector")) {
                        throw new JsonParseException("Don't know how to turn " + p_deserialize_1_.toString() + " into a Component");
                    }
                    ichatcomponent = new ChatComponentSelector(JsonUtils.getString(jsonobject, "selector"));
                }
                if (jsonobject.has("extra")) {
                    JsonArray jsonarray2 = jsonobject.getAsJsonArray("extra");
                    if (jsonarray2.size() <= 0) {
                        throw new JsonParseException("Unexpected empty array of components");
                    }
                    for (int j = 0; j < jsonarray2.size(); ++j) {
                        ichatcomponent.appendSibling(this.deserialize(jsonarray2.get(j), p_deserialize_2_, p_deserialize_3_));
                    }
                }
                ichatcomponent.setChatStyle((ChatStyle)p_deserialize_3_.deserialize(p_deserialize_1_, (Type)((Object)ChatStyle.class)));
                return ichatcomponent;
            }
            if (!p_deserialize_1_.isJsonArray()) throw new JsonParseException("Don't know how to turn " + p_deserialize_1_.toString() + " into a Component");
            JsonArray jsonarray1 = p_deserialize_1_.getAsJsonArray();
            IChatComponent ichatcomponent1 = null;
            Iterator<JsonElement> iterator = jsonarray1.iterator();
            while (iterator.hasNext()) {
                JsonElement jsonelement = iterator.next();
                IChatComponent ichatcomponent2 = this.deserialize(jsonelement, jsonelement.getClass(), p_deserialize_3_);
                if (ichatcomponent1 == null) {
                    ichatcomponent1 = ichatcomponent2;
                    continue;
                }
                ichatcomponent1.appendSibling(ichatcomponent2);
            }
            return ichatcomponent1;
        }

        private void serializeChatStyle(ChatStyle style, JsonObject object, JsonSerializationContext ctx) {
            JsonElement jsonelement = ctx.serialize(style);
            if (!jsonelement.isJsonObject()) return;
            JsonObject jsonobject = (JsonObject)jsonelement;
            Iterator<Map.Entry<String, JsonElement>> iterator = jsonobject.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonElement> entry = iterator.next();
                object.add(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public JsonElement serialize(IChatComponent p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
            if (p_serialize_1_ instanceof ChatComponentText && p_serialize_1_.getChatStyle().isEmpty() && p_serialize_1_.getSiblings().isEmpty()) {
                return new JsonPrimitive(((ChatComponentText)p_serialize_1_).getChatComponentText_TextValue());
            }
            JsonObject jsonobject = new JsonObject();
            if (!p_serialize_1_.getChatStyle().isEmpty()) {
                this.serializeChatStyle(p_serialize_1_.getChatStyle(), jsonobject, p_serialize_3_);
            }
            if (!p_serialize_1_.getSiblings().isEmpty()) {
                JsonArray jsonarray = new JsonArray();
                for (IChatComponent ichatcomponent : p_serialize_1_.getSiblings()) {
                    jsonarray.add(this.serialize(ichatcomponent, (Type)ichatcomponent.getClass(), p_serialize_3_));
                }
                jsonobject.add("extra", jsonarray);
            }
            if (p_serialize_1_ instanceof ChatComponentText) {
                jsonobject.addProperty("text", ((ChatComponentText)p_serialize_1_).getChatComponentText_TextValue());
                return jsonobject;
            }
            if (!(p_serialize_1_ instanceof ChatComponentTranslation)) {
                if (p_serialize_1_ instanceof ChatComponentScore) {
                    ChatComponentScore chatcomponentscore = (ChatComponentScore)p_serialize_1_;
                    JsonObject jsonobject1 = new JsonObject();
                    jsonobject1.addProperty("name", chatcomponentscore.getName());
                    jsonobject1.addProperty("objective", chatcomponentscore.getObjective());
                    jsonobject1.addProperty("value", chatcomponentscore.getUnformattedTextForChat());
                    jsonobject.add("score", jsonobject1);
                    return jsonobject;
                }
                if (!(p_serialize_1_ instanceof ChatComponentSelector)) {
                    throw new IllegalArgumentException("Don't know how to serialize " + p_serialize_1_ + " as a Component");
                }
                ChatComponentSelector chatcomponentselector = (ChatComponentSelector)p_serialize_1_;
                jsonobject.addProperty("selector", chatcomponentselector.getSelector());
                return jsonobject;
            }
            ChatComponentTranslation chatcomponenttranslation = (ChatComponentTranslation)p_serialize_1_;
            jsonobject.addProperty("translate", chatcomponenttranslation.getKey());
            if (chatcomponenttranslation.getFormatArgs() == null) return jsonobject;
            if (chatcomponenttranslation.getFormatArgs().length <= 0) return jsonobject;
            JsonArray jsonarray1 = new JsonArray();
            Object[] objectArray = chatcomponenttranslation.getFormatArgs();
            int n = objectArray.length;
            int n2 = 0;
            while (true) {
                if (n2 >= n) {
                    jsonobject.add("with", jsonarray1);
                    return jsonobject;
                }
                Object object = objectArray[n2];
                if (object instanceof IChatComponent) {
                    jsonarray1.add(this.serialize((IChatComponent)object, (Type)object.getClass(), p_serialize_3_));
                } else {
                    jsonarray1.add(new JsonPrimitive(String.valueOf(object)));
                }
                ++n2;
            }
        }

        public static String componentToJson(IChatComponent component) {
            return GSON.toJson(component);
        }

        public static IChatComponent jsonToComponent(String json) {
            return GSON.fromJson(json, IChatComponent.class);
        }

        static {
            GsonBuilder gsonbuilder = new GsonBuilder();
            gsonbuilder.registerTypeHierarchyAdapter(IChatComponent.class, new Serializer());
            gsonbuilder.registerTypeHierarchyAdapter(ChatStyle.class, new ChatStyle.Serializer());
            gsonbuilder.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
            GSON = gsonbuilder.create();
        }
    }
}

