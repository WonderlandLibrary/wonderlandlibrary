package net.minecraft.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

/*
 * Exception performing whole class analysis ignored.
 */
public static class ChatStyle.Serializer
implements JsonDeserializer<ChatStyle>,
JsonSerializer<ChatStyle> {
    public ChatStyle deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        if (p_deserialize_1_.isJsonObject()) {
            JsonObject jsonobject2;
            JsonObject jsonobject1;
            ChatStyle chatstyle = new ChatStyle();
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            if (jsonobject == null) {
                return null;
            }
            if (jsonobject.has("bold")) {
                ChatStyle.access$002((ChatStyle)chatstyle, (Boolean)jsonobject.get("bold").getAsBoolean());
            }
            if (jsonobject.has("italic")) {
                ChatStyle.access$102((ChatStyle)chatstyle, (Boolean)jsonobject.get("italic").getAsBoolean());
            }
            if (jsonobject.has("underlined")) {
                ChatStyle.access$202((ChatStyle)chatstyle, (Boolean)jsonobject.get("underlined").getAsBoolean());
            }
            if (jsonobject.has("strikethrough")) {
                ChatStyle.access$302((ChatStyle)chatstyle, (Boolean)jsonobject.get("strikethrough").getAsBoolean());
            }
            if (jsonobject.has("obfuscated")) {
                ChatStyle.access$402((ChatStyle)chatstyle, (Boolean)jsonobject.get("obfuscated").getAsBoolean());
            }
            if (jsonobject.has("color")) {
                ChatStyle.access$502((ChatStyle)chatstyle, (EnumChatFormatting)((EnumChatFormatting)p_deserialize_3_.deserialize(jsonobject.get("color"), EnumChatFormatting.class)));
            }
            if (jsonobject.has("insertion")) {
                ChatStyle.access$602((ChatStyle)chatstyle, (String)jsonobject.get("insertion").getAsString());
            }
            if (jsonobject.has("clickEvent") && (jsonobject1 = jsonobject.getAsJsonObject("clickEvent")) != null) {
                String s;
                JsonPrimitive jsonprimitive = jsonobject1.getAsJsonPrimitive("action");
                ClickEvent.Action clickevent$action = jsonprimitive == null ? null : ClickEvent.Action.getValueByCanonicalName((String)jsonprimitive.getAsString());
                JsonPrimitive jsonprimitive1 = jsonobject1.getAsJsonPrimitive("value");
                String string = s = jsonprimitive1 == null ? null : jsonprimitive1.getAsString();
                if (clickevent$action != null && s != null && clickevent$action.shouldAllowInChat()) {
                    ChatStyle.access$702((ChatStyle)chatstyle, (ClickEvent)new ClickEvent(clickevent$action, s));
                }
            }
            if (jsonobject.has("hoverEvent") && (jsonobject2 = jsonobject.getAsJsonObject("hoverEvent")) != null) {
                JsonPrimitive jsonprimitive2 = jsonobject2.getAsJsonPrimitive("action");
                HoverEvent.Action hoverevent$action = jsonprimitive2 == null ? null : HoverEvent.Action.getValueByCanonicalName((String)jsonprimitive2.getAsString());
                IChatComponent ichatcomponent = (IChatComponent)p_deserialize_3_.deserialize(jsonobject2.get("value"), IChatComponent.class);
                if (hoverevent$action != null && ichatcomponent != null && hoverevent$action.shouldAllowInChat()) {
                    ChatStyle.access$802((ChatStyle)chatstyle, (HoverEvent)new HoverEvent(hoverevent$action, ichatcomponent));
                }
            }
            return chatstyle;
        }
        return null;
    }

    public JsonElement serialize(ChatStyle p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
        if (p_serialize_1_.isEmpty()) {
            return null;
        }
        JsonObject jsonobject = new JsonObject();
        if (ChatStyle.access$000((ChatStyle)p_serialize_1_) != null) {
            jsonobject.addProperty("bold", ChatStyle.access$000((ChatStyle)p_serialize_1_));
        }
        if (ChatStyle.access$100((ChatStyle)p_serialize_1_) != null) {
            jsonobject.addProperty("italic", ChatStyle.access$100((ChatStyle)p_serialize_1_));
        }
        if (ChatStyle.access$200((ChatStyle)p_serialize_1_) != null) {
            jsonobject.addProperty("underlined", ChatStyle.access$200((ChatStyle)p_serialize_1_));
        }
        if (ChatStyle.access$300((ChatStyle)p_serialize_1_) != null) {
            jsonobject.addProperty("strikethrough", ChatStyle.access$300((ChatStyle)p_serialize_1_));
        }
        if (ChatStyle.access$400((ChatStyle)p_serialize_1_) != null) {
            jsonobject.addProperty("obfuscated", ChatStyle.access$400((ChatStyle)p_serialize_1_));
        }
        if (ChatStyle.access$500((ChatStyle)p_serialize_1_) != null) {
            jsonobject.add("color", p_serialize_3_.serialize((Object)ChatStyle.access$500((ChatStyle)p_serialize_1_)));
        }
        if (ChatStyle.access$600((ChatStyle)p_serialize_1_) != null) {
            jsonobject.add("insertion", p_serialize_3_.serialize((Object)ChatStyle.access$600((ChatStyle)p_serialize_1_)));
        }
        if (ChatStyle.access$700((ChatStyle)p_serialize_1_) != null) {
            JsonObject jsonobject1 = new JsonObject();
            jsonobject1.addProperty("action", ChatStyle.access$700((ChatStyle)p_serialize_1_).getAction().getCanonicalName());
            jsonobject1.addProperty("value", ChatStyle.access$700((ChatStyle)p_serialize_1_).getValue());
            jsonobject.add("clickEvent", (JsonElement)jsonobject1);
        }
        if (ChatStyle.access$800((ChatStyle)p_serialize_1_) != null) {
            JsonObject jsonobject2 = new JsonObject();
            jsonobject2.addProperty("action", ChatStyle.access$800((ChatStyle)p_serialize_1_).getAction().getCanonicalName());
            jsonobject2.add("value", p_serialize_3_.serialize((Object)ChatStyle.access$800((ChatStyle)p_serialize_1_).getValue()));
            jsonobject.add("hoverEvent", (JsonElement)jsonobject2);
        }
        return jsonobject;
    }
}
