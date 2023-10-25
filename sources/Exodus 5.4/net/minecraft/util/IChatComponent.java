/*
 * Decompiled with CFR 0.152.
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
extends Iterable<IChatComponent> {
    public List<IChatComponent> getSiblings();

    public IChatComponent setChatStyle(ChatStyle var1);

    public String getUnformattedText();

    public ChatStyle getChatStyle();

    public IChatComponent appendSibling(IChatComponent var1);

    public String getFormattedText();

    public IChatComponent appendText(String var1);

    public String getUnformattedTextForChat();

    public IChatComponent createCopy();

    public static class Serializer
    implements JsonDeserializer<IChatComponent>,
    JsonSerializer<IChatComponent> {
        private static final Gson GSON;

        public static IChatComponent jsonToComponent(String string) {
            return (IChatComponent)GSON.fromJson(string, IChatComponent.class);
        }

        public static String componentToJson(IChatComponent iChatComponent) {
            return GSON.toJson((Object)iChatComponent);
        }

        public IChatComponent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String string;
            ChatComponentStyle chatComponentStyle;
            if (jsonElement.isJsonPrimitive()) {
                return new ChatComponentText(jsonElement.getAsString());
            }
            if (!jsonElement.isJsonObject()) {
                if (jsonElement.isJsonArray()) {
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    Object object = null;
                    for (JsonElement jsonElement2 : jsonArray) {
                        Object object2 = this.deserialize(jsonElement2, jsonElement2.getClass(), jsonDeserializationContext);
                        if (object == null) {
                            object = object2;
                            continue;
                        }
                        object.appendSibling((IChatComponent)object2);
                    }
                    return object;
                }
                throw new JsonParseException("Don't know how to turn " + jsonElement.toString() + " into a Component");
            }
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("text")) {
                chatComponentStyle = new ChatComponentText(jsonObject.get("text").getAsString());
            } else if (jsonObject.has("translate")) {
                string = jsonObject.get("translate").getAsString();
                if (jsonObject.has("with")) {
                    JsonArray jsonArray = jsonObject.getAsJsonArray("with");
                    Object[] objectArray = new Object[jsonArray.size()];
                    int n = 0;
                    while (n < objectArray.length) {
                        ChatComponentText chatComponentText;
                        objectArray[n] = this.deserialize(jsonArray.get(n), type, jsonDeserializationContext);
                        if (objectArray[n] instanceof ChatComponentText && (chatComponentText = (ChatComponentText)objectArray[n]).getChatStyle().isEmpty() && chatComponentText.getSiblings().isEmpty()) {
                            objectArray[n] = chatComponentText.getChatComponentText_TextValue();
                        }
                        ++n;
                    }
                    chatComponentStyle = new ChatComponentTranslation(string, objectArray);
                } else {
                    chatComponentStyle = new ChatComponentTranslation(string, new Object[0]);
                }
            } else if (jsonObject.has("score")) {
                string = jsonObject.getAsJsonObject("score");
                if (!string.has("name") || !string.has("objective")) {
                    throw new JsonParseException("A score component needs a least a name and an objective");
                }
                chatComponentStyle = new ChatComponentScore(JsonUtils.getString((JsonObject)string, "name"), JsonUtils.getString((JsonObject)string, "objective"));
                if (string.has("value")) {
                    ((ChatComponentScore)chatComponentStyle).setValue(JsonUtils.getString((JsonObject)string, "value"));
                }
            } else {
                if (!jsonObject.has("selector")) {
                    throw new JsonParseException("Don't know how to turn " + jsonElement.toString() + " into a Component");
                }
                chatComponentStyle = new ChatComponentSelector(JsonUtils.getString(jsonObject, "selector"));
            }
            if (jsonObject.has("extra")) {
                string = jsonObject.getAsJsonArray("extra");
                if (string.size() <= 0) {
                    throw new JsonParseException("Unexpected empty array of components");
                }
                int n = 0;
                while (n < string.size()) {
                    chatComponentStyle.appendSibling((IChatComponent)this.deserialize(string.get(n), type, jsonDeserializationContext));
                    ++n;
                }
            }
            chatComponentStyle.setChatStyle((ChatStyle)jsonDeserializationContext.deserialize(jsonElement, ChatStyle.class));
            return chatComponentStyle;
        }

        static {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeHierarchyAdapter(IChatComponent.class, (Object)new Serializer());
            gsonBuilder.registerTypeHierarchyAdapter(ChatStyle.class, (Object)new ChatStyle.Serializer());
            gsonBuilder.registerTypeAdapterFactory((TypeAdapterFactory)new EnumTypeAdapterFactory());
            GSON = gsonBuilder.create();
        }

        public JsonElement serialize(IChatComponent iChatComponent, Type type, JsonSerializationContext jsonSerializationContext) {
            IChatComponent iChatComponent22;
            Object object;
            if (iChatComponent instanceof ChatComponentText && iChatComponent.getChatStyle().isEmpty() && iChatComponent.getSiblings().isEmpty()) {
                return new JsonPrimitive(((ChatComponentText)iChatComponent).getChatComponentText_TextValue());
            }
            JsonObject jsonObject = new JsonObject();
            if (!iChatComponent.getChatStyle().isEmpty()) {
                this.serializeChatStyle(iChatComponent.getChatStyle(), jsonObject, jsonSerializationContext);
            }
            if (!iChatComponent.getSiblings().isEmpty()) {
                object = new JsonArray();
                for (IChatComponent iChatComponent22 : iChatComponent.getSiblings()) {
                    object.add(this.serialize(iChatComponent22, iChatComponent22.getClass(), jsonSerializationContext));
                }
                jsonObject.add("extra", (JsonElement)object);
            }
            if (iChatComponent instanceof ChatComponentText) {
                jsonObject.addProperty("text", ((ChatComponentText)iChatComponent).getChatComponentText_TextValue());
            } else if (iChatComponent instanceof ChatComponentTranslation) {
                object = (ChatComponentTranslation)iChatComponent;
                jsonObject.addProperty("translate", ((ChatComponentTranslation)object).getKey());
                if (((ChatComponentTranslation)object).getFormatArgs() != null && ((ChatComponentTranslation)object).getFormatArgs().length > 0) {
                    iChatComponent22 = new JsonArray();
                    Object[] objectArray = ((ChatComponentTranslation)object).getFormatArgs();
                    int n = objectArray.length;
                    int n2 = 0;
                    while (n2 < n) {
                        Object object2 = objectArray[n2];
                        if (object2 instanceof IChatComponent) {
                            iChatComponent22.add(this.serialize((IChatComponent)object2, object2.getClass(), jsonSerializationContext));
                        } else {
                            iChatComponent22.add((JsonElement)new JsonPrimitive(String.valueOf(object2)));
                        }
                        ++n2;
                    }
                    jsonObject.add("with", (JsonElement)iChatComponent22);
                }
            } else if (iChatComponent instanceof ChatComponentScore) {
                object = (ChatComponentScore)iChatComponent;
                iChatComponent22 = new JsonObject();
                iChatComponent22.addProperty("name", ((ChatComponentScore)object).getName());
                iChatComponent22.addProperty("objective", ((ChatComponentScore)object).getObjective());
                iChatComponent22.addProperty("value", ((ChatComponentScore)object).getUnformattedTextForChat());
                jsonObject.add("score", (JsonElement)iChatComponent22);
            } else {
                if (!(iChatComponent instanceof ChatComponentSelector)) {
                    throw new IllegalArgumentException("Don't know how to serialize " + iChatComponent + " as a Component");
                }
                object = (ChatComponentSelector)iChatComponent;
                jsonObject.addProperty("selector", ((ChatComponentSelector)object).getSelector());
            }
            return jsonObject;
        }

        private void serializeChatStyle(ChatStyle chatStyle, JsonObject jsonObject, JsonSerializationContext jsonSerializationContext) {
            JsonElement jsonElement = jsonSerializationContext.serialize((Object)chatStyle);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject2 = (JsonObject)jsonElement;
                for (Map.Entry entry : jsonObject2.entrySet()) {
                    jsonObject.add((String)entry.getKey(), (JsonElement)entry.getValue());
                }
            }
        }
    }
}

