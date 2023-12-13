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
import java.util.List;
import java.util.Map.Entry;

public interface IChatComponent extends Iterable<IChatComponent> {
    IChatComponent setChatStyle(ChatStyle style);

    ChatStyle getChatStyle();

    /**
     * Appends the given text to the end of this component.
     */
    IChatComponent appendText(String text);

    /**
     * Appends the given component to the end of this one.
     */
    IChatComponent appendSibling(IChatComponent component);

    /**
     * Gets the text of this component, without any special formatting codes added, for chat.
     */
    String getUnformattedTextForChat();

    /**
     * Get the text of this component, <em>and all child components</em>, with all special formatting codes removed.
     */
    String getUnformattedText();

    /**
     * Gets the text of this component, with formatting codes added for rendering.
     */
    String getFormattedText();

    List<IChatComponent> getSiblings();

    /**
     * Creates a copy of this component.  Almost a deep copy, except the style is shallow-copied.
     */
    IChatComponent createCopy();

    class Serializer implements JsonDeserializer<IChatComponent>, JsonSerializer<IChatComponent> {
        private static final Gson GSON;

        public IChatComponent deserialize(JsonElement element, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            if (element.isJsonPrimitive())
                return new ChatComponentText(element.getAsString());
            else if (!element.isJsonObject()) {
                if (element.isJsonArray()) {
                    JsonArray jsonarray1 = element.getAsJsonArray();
                    IChatComponent iChatComponent = null;

                    for (JsonElement jsonelement : jsonarray1) {
                        IChatComponent iChatComponent1 = deserialize(jsonelement, jsonelement.getClass(), p_deserialize_3_);

                        if (iChatComponent == null)
                            iChatComponent = iChatComponent1;
                        else
                            iChatComponent.appendSibling(iChatComponent1);
                    }

                    return iChatComponent;
                } else
                    throw new JsonParseException("Don't know how to turn " + element.toString() + " into a Component");
            } else {
                JsonObject jsonObject = element.getAsJsonObject();
                IChatComponent iChatComponent;

                if (jsonObject.has("text"))
                    iChatComponent = new ChatComponentText(jsonObject.get("text").getAsString());
                else if (jsonObject.has("translate")) {
                    String s = jsonObject.get("translate").getAsString();

                    if (jsonObject.has("with")) {
                        JsonArray jsonarray = jsonObject.getAsJsonArray("with");
                        Object[] aobject = new Object[jsonarray.size()];

                        for (int i = 0; i < aobject.length; ++i) {
                            aobject[i] = deserialize(jsonarray.get(i), p_deserialize_2_, p_deserialize_3_);

                            if (aobject[i] instanceof ChatComponentText) {
                                ChatComponentText chatcomponenttext = (ChatComponentText) aobject[i];

                                if (chatcomponenttext.getChatStyle().isEmpty() && chatcomponenttext.getSiblings().isEmpty())
                                    aobject[i] = chatcomponenttext.getChatComponentText_TextValue();
                            }
                        }

                        iChatComponent = new ChatComponentTranslation(s, aobject);
                    } else
                        iChatComponent = new ChatComponentTranslation(s);
                } else if (jsonObject.has("score")) {
                    JsonObject jsonobject1 = jsonObject.getAsJsonObject("score");

                    if (!jsonobject1.has("name") || !jsonobject1.has("objective")) {
                        throw new JsonParseException("A score component needs a least a name and an objective");
                    }

                    iChatComponent = new ChatComponentScore(JsonUtils.getString(jsonobject1, "name"), JsonUtils.getString(jsonobject1, "objective"));

                    if (jsonobject1.has("value")) {
                        ((ChatComponentScore) iChatComponent).setValue(JsonUtils.getString(jsonobject1, "value"));
                    }
                } else {
                    if (!jsonObject.has("selector")) {
                        throw new JsonParseException("Don't know how to turn " + element.toString() + " into a Component");
                    }

                    iChatComponent = new ChatComponentSelector(JsonUtils.getString(jsonObject, "selector"));
                }

                if (jsonObject.has("extra")) {
                    JsonArray jsonarray2 = jsonObject.getAsJsonArray("extra");

                    if (jsonarray2.size() <= 0)
                        throw new JsonParseException("Unexpected empty array of components");

                    for (int j = 0; j < jsonarray2.size(); ++j)
                        iChatComponent.appendSibling(deserialize(jsonarray2.get(j), p_deserialize_2_, p_deserialize_3_));
                }

                iChatComponent.setChatStyle(p_deserialize_3_.deserialize(element, ChatStyle.class));
                return iChatComponent;
            }
        }

        private void serializeChatStyle(ChatStyle style, JsonObject object, JsonSerializationContext ctx) {
            JsonElement jsonelement = ctx.serialize(style);

            if (jsonelement.isJsonObject()) {
                JsonObject jsonobject = (JsonObject) jsonelement;

                for (Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                    object.add(entry.getKey(), entry.getValue());
                }
            }
        }

        public JsonElement serialize(IChatComponent component, Type type, JsonSerializationContext context) {
            if (component instanceof ChatComponentText && component.getChatStyle().isEmpty() && component.getSiblings().isEmpty())
                return new JsonPrimitive(((ChatComponentText) component).getChatComponentText_TextValue());
            else {
                JsonObject jsonObject1 = new JsonObject();

                if (!component.getChatStyle().isEmpty())
                    serializeChatStyle(component.getChatStyle(), jsonObject1, context);

                if (!component.getSiblings().isEmpty()) {
                    JsonArray jsonArray = new JsonArray();

                    for (IChatComponent ichatcomponent : component.getSiblings())
                        jsonArray.add(serialize(ichatcomponent, ichatcomponent.getClass(), context));

                    jsonObject1.add("extra", jsonArray);
                }

                if (component instanceof ChatComponentText)
                    jsonObject1.addProperty("text", ((ChatComponentText) component).getChatComponentText_TextValue());
                else if (component instanceof ChatComponentTranslation) {
                    ChatComponentTranslation translation = (ChatComponentTranslation) component;
                    jsonObject1.addProperty("translate", translation.getKey());

                    if (translation.getFormatArgs() != null && translation.getFormatArgs().length > 0) {
                        JsonArray array = new JsonArray();

                        for (Object object : translation.getFormatArgs()) {
                            if (object instanceof IChatComponent)
                                array.add(serialize((IChatComponent) object, object.getClass(), context));
                            else
                                array.add(new JsonPrimitive(String.valueOf(object)));
                        }

                        jsonObject1.add("with", array);
                    }
                } else if (component instanceof ChatComponentScore) {
                    ChatComponentScore score = (ChatComponentScore) component;
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("name", score.getName());
                    jsonObject.addProperty("objective", score.getObjective());
                    jsonObject.addProperty("value", score.getUnformattedTextForChat());
                    jsonObject1.add("score", jsonObject);
                } else {
                    if (!(component instanceof ChatComponentSelector))
                        throw new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");

                    ChatComponentSelector selector = (ChatComponentSelector) component;
                    jsonObject1.addProperty("selector", selector.getSelector());
                }

                return jsonObject1;
            }
        }

        public static String componentToJson(IChatComponent component) {
            return GSON.toJson(component);
        }

        public static IChatComponent jsonToComponent(String json) {
            return GSON.fromJson(json, IChatComponent.class);
        }

        static {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeHierarchyAdapter(IChatComponent.class, new IChatComponent.Serializer());
            builder.registerTypeHierarchyAdapter(ChatStyle.class, new ChatStyle.Serializer());
            builder.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
            GSON = builder.create();
        }
    }
}