/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonPrimitive
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 */
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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatStyle {
    private EnumChatFormatting color;
    private String insertion;
    private Boolean strikethrough;
    private Boolean underlined;
    private static final ChatStyle rootStyle = new ChatStyle(){

        @Override
        public EnumChatFormatting getColor() {
            return null;
        }

        @Override
        public ChatStyle createShallowCopy() {
            return this;
        }

        @Override
        public ChatStyle setItalic(Boolean bl) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean getUnderlined() {
            return false;
        }

        @Override
        public ChatStyle setChatClickEvent(ClickEvent clickEvent) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle createDeepCopy() {
            return this;
        }

        @Override
        public ChatStyle setObfuscated(Boolean bl) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getFormattingCode() {
            return "";
        }

        @Override
        public boolean getBold() {
            return false;
        }

        @Override
        public ChatStyle setBold(Boolean bl) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getInsertion() {
            return null;
        }

        @Override
        public String toString() {
            return "Style.ROOT";
        }

        @Override
        public ChatStyle setColor(EnumChatFormatting enumChatFormatting) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setUnderlined(Boolean bl) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean getItalic() {
            return false;
        }

        @Override
        public boolean getStrikethrough() {
            return false;
        }

        @Override
        public ChatStyle setStrikethrough(Boolean bl) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setChatHoverEvent(HoverEvent hoverEvent) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setParentStyle(ChatStyle chatStyle) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean getObfuscated() {
            return false;
        }

        @Override
        public ClickEvent getChatClickEvent() {
            return null;
        }

        @Override
        public HoverEvent getChatHoverEvent() {
            return null;
        }
    };
    private Boolean obfuscated;
    private ClickEvent chatClickEvent;
    private ChatStyle parentStyle;
    private Boolean bold;
    private Boolean italic;
    private HoverEvent chatHoverEvent;

    public String getFormattingCode() {
        if (this.isEmpty()) {
            return this.parentStyle != null ? this.parentStyle.getFormattingCode() : "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (this.getColor() != null) {
            stringBuilder.append((Object)this.getColor());
        }
        if (this.getBold()) {
            stringBuilder.append((Object)EnumChatFormatting.BOLD);
        }
        if (this.getItalic()) {
            stringBuilder.append((Object)EnumChatFormatting.ITALIC);
        }
        if (this.getUnderlined()) {
            stringBuilder.append((Object)EnumChatFormatting.UNDERLINE);
        }
        if (this.getObfuscated()) {
            stringBuilder.append((Object)EnumChatFormatting.OBFUSCATED);
        }
        if (this.getStrikethrough()) {
            stringBuilder.append((Object)EnumChatFormatting.STRIKETHROUGH);
        }
        return stringBuilder.toString();
    }

    public ChatStyle createShallowCopy() {
        ChatStyle chatStyle = new ChatStyle();
        chatStyle.bold = this.bold;
        chatStyle.italic = this.italic;
        chatStyle.strikethrough = this.strikethrough;
        chatStyle.underlined = this.underlined;
        chatStyle.obfuscated = this.obfuscated;
        chatStyle.color = this.color;
        chatStyle.chatClickEvent = this.chatClickEvent;
        chatStyle.chatHoverEvent = this.chatHoverEvent;
        chatStyle.parentStyle = this.parentStyle;
        chatStyle.insertion = this.insertion;
        return chatStyle;
    }

    public ChatStyle createDeepCopy() {
        ChatStyle chatStyle = new ChatStyle();
        chatStyle.setBold(this.getBold());
        chatStyle.setItalic(this.getItalic());
        chatStyle.setStrikethrough(this.getStrikethrough());
        chatStyle.setUnderlined(this.getUnderlined());
        chatStyle.setObfuscated(this.getObfuscated());
        chatStyle.setColor(this.getColor());
        chatStyle.setChatClickEvent(this.getChatClickEvent());
        chatStyle.setChatHoverEvent(this.getChatHoverEvent());
        chatStyle.setInsertion(this.getInsertion());
        return chatStyle;
    }

    public boolean getItalic() {
        return this.italic == null ? this.getParent().getItalic() : this.italic.booleanValue();
    }

    public boolean getBold() {
        return this.bold == null ? this.getParent().getBold() : this.bold.booleanValue();
    }

    public ClickEvent getChatClickEvent() {
        return this.chatClickEvent == null ? this.getParent().getChatClickEvent() : this.chatClickEvent;
    }

    public ChatStyle setChatHoverEvent(HoverEvent hoverEvent) {
        this.chatHoverEvent = hoverEvent;
        return this;
    }

    public boolean getUnderlined() {
        return this.underlined == null ? this.getParent().getUnderlined() : this.underlined.booleanValue();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ChatStyle)) {
            return false;
        }
        ChatStyle chatStyle = (ChatStyle)object;
        if (this.getBold() != chatStyle.getBold() || this.getColor() != chatStyle.getColor() || this.getItalic() != chatStyle.getItalic() || this.getObfuscated() != chatStyle.getObfuscated() || this.getStrikethrough() != chatStyle.getStrikethrough() || this.getUnderlined() != chatStyle.getUnderlined() || (this.getChatClickEvent() == null ? chatStyle.getChatClickEvent() != null : !this.getChatClickEvent().equals(chatStyle.getChatClickEvent())) || (this.getChatHoverEvent() == null ? chatStyle.getChatHoverEvent() != null : !this.getChatHoverEvent().equals(chatStyle.getChatHoverEvent())) || !(this.getInsertion() != null ? this.getInsertion().equals(chatStyle.getInsertion()) : chatStyle.getInsertion() == null)) {
            boolean bl = false;
            return bl;
        }
        boolean bl = true;
        return bl;
    }

    public EnumChatFormatting getColor() {
        return this.color == null ? this.getParent().getColor() : this.color;
    }

    public HoverEvent getChatHoverEvent() {
        return this.chatHoverEvent == null ? this.getParent().getChatHoverEvent() : this.chatHoverEvent;
    }

    public ChatStyle setParentStyle(ChatStyle chatStyle) {
        this.parentStyle = chatStyle;
        return this;
    }

    public ChatStyle setObfuscated(Boolean bl) {
        this.obfuscated = bl;
        return this;
    }

    public String getInsertion() {
        return this.insertion == null ? this.getParent().getInsertion() : this.insertion;
    }

    public boolean getStrikethrough() {
        return this.strikethrough == null ? this.getParent().getStrikethrough() : this.strikethrough.booleanValue();
    }

    public ChatStyle setBold(Boolean bl) {
        this.bold = bl;
        return this;
    }

    public ChatStyle setChatClickEvent(ClickEvent clickEvent) {
        this.chatClickEvent = clickEvent;
        return this;
    }

    public int hashCode() {
        int n = this.color.hashCode();
        n = 31 * n + this.bold.hashCode();
        n = 31 * n + this.italic.hashCode();
        n = 31 * n + this.underlined.hashCode();
        n = 31 * n + this.strikethrough.hashCode();
        n = 31 * n + this.obfuscated.hashCode();
        n = 31 * n + this.chatClickEvent.hashCode();
        n = 31 * n + this.chatHoverEvent.hashCode();
        n = 31 * n + this.insertion.hashCode();
        return n;
    }

    public boolean getObfuscated() {
        return this.obfuscated == null ? this.getParent().getObfuscated() : this.obfuscated.booleanValue();
    }

    public ChatStyle setInsertion(String string) {
        this.insertion = string;
        return this;
    }

    public String toString() {
        return "Style{hasParent=" + (this.parentStyle != null) + ", color=" + (Object)((Object)this.color) + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", obfuscated=" + this.obfuscated + ", clickEvent=" + this.getChatClickEvent() + ", hoverEvent=" + this.getChatHoverEvent() + ", insertion=" + this.getInsertion() + '}';
    }

    private ChatStyle getParent() {
        return this.parentStyle == null ? rootStyle : this.parentStyle;
    }

    public boolean isEmpty() {
        return this.bold == null && this.italic == null && this.strikethrough == null && this.underlined == null && this.obfuscated == null && this.color == null && this.chatClickEvent == null && this.chatHoverEvent == null;
    }

    public ChatStyle setColor(EnumChatFormatting enumChatFormatting) {
        this.color = enumChatFormatting;
        return this;
    }

    public ChatStyle setUnderlined(Boolean bl) {
        this.underlined = bl;
        return this;
    }

    public ChatStyle setItalic(Boolean bl) {
        this.italic = bl;
        return this;
    }

    public ChatStyle setStrikethrough(Boolean bl) {
        this.strikethrough = bl;
        return this;
    }

    public static class Serializer
    implements JsonDeserializer<ChatStyle>,
    JsonSerializer<ChatStyle> {
        public JsonElement serialize(ChatStyle chatStyle, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject;
            if (chatStyle.isEmpty()) {
                return null;
            }
            JsonObject jsonObject2 = new JsonObject();
            if (chatStyle.bold != null) {
                jsonObject2.addProperty("bold", chatStyle.bold);
            }
            if (chatStyle.italic != null) {
                jsonObject2.addProperty("italic", chatStyle.italic);
            }
            if (chatStyle.underlined != null) {
                jsonObject2.addProperty("underlined", chatStyle.underlined);
            }
            if (chatStyle.strikethrough != null) {
                jsonObject2.addProperty("strikethrough", chatStyle.strikethrough);
            }
            if (chatStyle.obfuscated != null) {
                jsonObject2.addProperty("obfuscated", chatStyle.obfuscated);
            }
            if (chatStyle.color != null) {
                jsonObject2.add("color", jsonSerializationContext.serialize((Object)chatStyle.color));
            }
            if (chatStyle.insertion != null) {
                jsonObject2.add("insertion", jsonSerializationContext.serialize((Object)chatStyle.insertion));
            }
            if (chatStyle.chatClickEvent != null) {
                jsonObject = new JsonObject();
                jsonObject.addProperty("action", chatStyle.chatClickEvent.getAction().getCanonicalName());
                jsonObject.addProperty("value", chatStyle.chatClickEvent.getValue());
                jsonObject2.add("clickEvent", (JsonElement)jsonObject);
            }
            if (chatStyle.chatHoverEvent != null) {
                jsonObject = new JsonObject();
                jsonObject.addProperty("action", chatStyle.chatHoverEvent.getAction().getCanonicalName());
                jsonObject.add("value", jsonSerializationContext.serialize((Object)chatStyle.chatHoverEvent.getValue()));
                jsonObject2.add("hoverEvent", (JsonElement)jsonObject);
            }
            return jsonObject2;
        }

        public ChatStyle deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (jsonElement.isJsonObject()) {
                Object object;
                ClickEvent.Action action;
                JsonPrimitive jsonPrimitive;
                JsonObject jsonObject;
                ChatStyle chatStyle = new ChatStyle();
                JsonObject jsonObject2 = jsonElement.getAsJsonObject();
                if (jsonObject2 == null) {
                    return null;
                }
                if (jsonObject2.has("bold")) {
                    chatStyle.bold = jsonObject2.get("bold").getAsBoolean();
                }
                if (jsonObject2.has("italic")) {
                    chatStyle.italic = jsonObject2.get("italic").getAsBoolean();
                }
                if (jsonObject2.has("underlined")) {
                    chatStyle.underlined = jsonObject2.get("underlined").getAsBoolean();
                }
                if (jsonObject2.has("strikethrough")) {
                    chatStyle.strikethrough = jsonObject2.get("strikethrough").getAsBoolean();
                }
                if (jsonObject2.has("obfuscated")) {
                    chatStyle.obfuscated = jsonObject2.get("obfuscated").getAsBoolean();
                }
                if (jsonObject2.has("color")) {
                    chatStyle.color = (EnumChatFormatting)((Object)jsonDeserializationContext.deserialize(jsonObject2.get("color"), EnumChatFormatting.class));
                }
                if (jsonObject2.has("insertion")) {
                    chatStyle.insertion = jsonObject2.get("insertion").getAsString();
                }
                if (jsonObject2.has("clickEvent") && (jsonObject = jsonObject2.getAsJsonObject("clickEvent")) != null) {
                    String string;
                    jsonPrimitive = jsonObject.getAsJsonPrimitive("action");
                    action = jsonPrimitive == null ? null : ClickEvent.Action.getValueByCanonicalName(jsonPrimitive.getAsString());
                    object = jsonObject.getAsJsonPrimitive("value");
                    String string2 = string = object == null ? null : object.getAsString();
                    if (action != null && string != null && action.shouldAllowInChat()) {
                        chatStyle.chatClickEvent = new ClickEvent(action, string);
                    }
                }
                if (jsonObject2.has("hoverEvent") && (jsonObject = jsonObject2.getAsJsonObject("hoverEvent")) != null) {
                    jsonPrimitive = jsonObject.getAsJsonPrimitive("action");
                    action = jsonPrimitive == null ? null : HoverEvent.Action.getValueByCanonicalName(jsonPrimitive.getAsString());
                    object = (IChatComponent)jsonDeserializationContext.deserialize(jsonObject.get("value"), IChatComponent.class);
                    if (action != null && object != null && ((HoverEvent.Action)((Object)action)).shouldAllowInChat()) {
                        chatStyle.chatHoverEvent = new HoverEvent((HoverEvent.Action)((Object)action), (IChatComponent)object);
                    }
                }
                return chatStyle;
            }
            return null;
        }
    }
}

