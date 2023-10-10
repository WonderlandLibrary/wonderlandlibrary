/*
 * Decompiled with CFR 0.152.
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
    private ChatStyle parentStyle;
    private EnumChatFormatting color;
    private Boolean bold;
    private Boolean italic;
    private Boolean underlined;
    private Boolean strikethrough;
    private Boolean obfuscated;
    private ClickEvent chatClickEvent;
    private HoverEvent chatHoverEvent;
    private String insertion;
    private static final ChatStyle rootStyle = new ChatStyle(){

        @Override
        public EnumChatFormatting getColor() {
            return null;
        }

        @Override
        public boolean getBold() {
            return false;
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
        public boolean getUnderlined() {
            return false;
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

        @Override
        public String getInsertion() {
            return null;
        }

        @Override
        public ChatStyle setColor(EnumChatFormatting color) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setBold(Boolean boldIn) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setItalic(Boolean italic) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setStrikethrough(Boolean strikethrough) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setUnderlined(Boolean underlined) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setObfuscated(Boolean obfuscated) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setChatClickEvent(ClickEvent event) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setChatHoverEvent(HoverEvent event) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setParentStyle(ChatStyle parent) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "Style.ROOT";
        }

        @Override
        public ChatStyle createShallowCopy() {
            return this;
        }

        @Override
        public ChatStyle createDeepCopy() {
            return this;
        }

        @Override
        public String getFormattingCode() {
            return "";
        }
    };

    public EnumChatFormatting getColor() {
        EnumChatFormatting enumChatFormatting;
        if (this.color == null) {
            enumChatFormatting = this.getParent().getColor();
            return enumChatFormatting;
        }
        enumChatFormatting = this.color;
        return enumChatFormatting;
    }

    public boolean getBold() {
        boolean bl;
        if (this.bold == null) {
            bl = this.getParent().getBold();
            return bl;
        }
        bl = this.bold;
        return bl;
    }

    public boolean getItalic() {
        boolean bl;
        if (this.italic == null) {
            bl = this.getParent().getItalic();
            return bl;
        }
        bl = this.italic;
        return bl;
    }

    public boolean getStrikethrough() {
        boolean bl;
        if (this.strikethrough == null) {
            bl = this.getParent().getStrikethrough();
            return bl;
        }
        bl = this.strikethrough;
        return bl;
    }

    public boolean getUnderlined() {
        boolean bl;
        if (this.underlined == null) {
            bl = this.getParent().getUnderlined();
            return bl;
        }
        bl = this.underlined;
        return bl;
    }

    public boolean getObfuscated() {
        boolean bl;
        if (this.obfuscated == null) {
            bl = this.getParent().getObfuscated();
            return bl;
        }
        bl = this.obfuscated;
        return bl;
    }

    public boolean isEmpty() {
        if (this.bold != null) return false;
        if (this.italic != null) return false;
        if (this.strikethrough != null) return false;
        if (this.underlined != null) return false;
        if (this.obfuscated != null) return false;
        if (this.color != null) return false;
        if (this.chatClickEvent != null) return false;
        if (this.chatHoverEvent != null) return false;
        return true;
    }

    public ClickEvent getChatClickEvent() {
        ClickEvent clickEvent;
        if (this.chatClickEvent == null) {
            clickEvent = this.getParent().getChatClickEvent();
            return clickEvent;
        }
        clickEvent = this.chatClickEvent;
        return clickEvent;
    }

    public HoverEvent getChatHoverEvent() {
        HoverEvent hoverEvent;
        if (this.chatHoverEvent == null) {
            hoverEvent = this.getParent().getChatHoverEvent();
            return hoverEvent;
        }
        hoverEvent = this.chatHoverEvent;
        return hoverEvent;
    }

    public String getInsertion() {
        String string;
        if (this.insertion == null) {
            string = this.getParent().getInsertion();
            return string;
        }
        string = this.insertion;
        return string;
    }

    public ChatStyle setColor(EnumChatFormatting color) {
        this.color = color;
        return this;
    }

    public ChatStyle setBold(Boolean boldIn) {
        this.bold = boldIn;
        return this;
    }

    public ChatStyle setItalic(Boolean italic) {
        this.italic = italic;
        return this;
    }

    public ChatStyle setStrikethrough(Boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }

    public ChatStyle setUnderlined(Boolean underlined) {
        this.underlined = underlined;
        return this;
    }

    public ChatStyle setObfuscated(Boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    public ChatStyle setChatClickEvent(ClickEvent event) {
        this.chatClickEvent = event;
        return this;
    }

    public ChatStyle setChatHoverEvent(HoverEvent event) {
        this.chatHoverEvent = event;
        return this;
    }

    public ChatStyle setInsertion(String insertion) {
        this.insertion = insertion;
        return this;
    }

    public ChatStyle setParentStyle(ChatStyle parent) {
        this.parentStyle = parent;
        return this;
    }

    public String getFormattingCode() {
        if (this.isEmpty()) {
            if (this.parentStyle == null) return "";
            String string = this.parentStyle.getFormattingCode();
            return string;
        }
        StringBuilder stringbuilder = new StringBuilder();
        if (this.getColor() != null) {
            stringbuilder.append((Object)this.getColor());
        }
        if (this.getBold()) {
            stringbuilder.append((Object)EnumChatFormatting.BOLD);
        }
        if (this.getItalic()) {
            stringbuilder.append((Object)EnumChatFormatting.ITALIC);
        }
        if (this.getUnderlined()) {
            stringbuilder.append((Object)EnumChatFormatting.UNDERLINE);
        }
        if (this.getObfuscated()) {
            stringbuilder.append((Object)EnumChatFormatting.OBFUSCATED);
        }
        if (!this.getStrikethrough()) return stringbuilder.toString();
        stringbuilder.append((Object)EnumChatFormatting.STRIKETHROUGH);
        return stringbuilder.toString();
    }

    private ChatStyle getParent() {
        ChatStyle chatStyle;
        if (this.parentStyle == null) {
            chatStyle = rootStyle;
            return chatStyle;
        }
        chatStyle = this.parentStyle;
        return chatStyle;
    }

    public String toString() {
        boolean bl;
        StringBuilder stringBuilder = new StringBuilder().append("Style{hasParent=");
        if (this.parentStyle != null) {
            bl = true;
            return stringBuilder.append(bl).append(", color=").append((Object)this.color).append(", bold=").append(this.bold).append(", italic=").append(this.italic).append(", underlined=").append(this.underlined).append(", obfuscated=").append(this.obfuscated).append(", clickEvent=").append(this.getChatClickEvent()).append(", hoverEvent=").append(this.getChatHoverEvent()).append(", insertion=").append(this.getInsertion()).append('}').toString();
        }
        bl = false;
        return stringBuilder.append(bl).append(", color=").append((Object)this.color).append(", bold=").append(this.bold).append(", italic=").append(this.italic).append(", underlined=").append(this.underlined).append(", obfuscated=").append(this.obfuscated).append(", clickEvent=").append(this.getChatClickEvent()).append(", hoverEvent=").append(this.getChatHoverEvent()).append(", insertion=").append(this.getInsertion()).append('}').toString();
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatStyle)) {
            return false;
        }
        ChatStyle chatstyle = (ChatStyle)p_equals_1_;
        if (this.getBold() != chatstyle.getBold()) return false;
        if (this.getColor() != chatstyle.getColor()) return false;
        if (this.getItalic() != chatstyle.getItalic()) return false;
        if (this.getObfuscated() != chatstyle.getObfuscated()) return false;
        if (this.getStrikethrough() != chatstyle.getStrikethrough()) return false;
        if (this.getUnderlined() != chatstyle.getUnderlined()) return false;
        if ((this.getChatClickEvent() == null ? chatstyle.getChatClickEvent() != null : !this.getChatClickEvent().equals(chatstyle.getChatClickEvent())) || (this.getChatHoverEvent() == null ? chatstyle.getChatHoverEvent() != null : !this.getChatHoverEvent().equals(chatstyle.getChatHoverEvent()))) return false;
        if (this.getInsertion() != null) {
            if (!this.getInsertion().equals(chatstyle.getInsertion())) return false;
            return true;
        } else if (chatstyle.getInsertion() != null) return false;
        return true;
    }

    public int hashCode() {
        int i = this.color.hashCode();
        i = 31 * i + this.bold.hashCode();
        i = 31 * i + this.italic.hashCode();
        i = 31 * i + this.underlined.hashCode();
        i = 31 * i + this.strikethrough.hashCode();
        i = 31 * i + this.obfuscated.hashCode();
        i = 31 * i + this.chatClickEvent.hashCode();
        i = 31 * i + this.chatHoverEvent.hashCode();
        return 31 * i + this.insertion.hashCode();
    }

    public ChatStyle createShallowCopy() {
        ChatStyle chatstyle = new ChatStyle();
        chatstyle.bold = this.bold;
        chatstyle.italic = this.italic;
        chatstyle.strikethrough = this.strikethrough;
        chatstyle.underlined = this.underlined;
        chatstyle.obfuscated = this.obfuscated;
        chatstyle.color = this.color;
        chatstyle.chatClickEvent = this.chatClickEvent;
        chatstyle.chatHoverEvent = this.chatHoverEvent;
        chatstyle.parentStyle = this.parentStyle;
        chatstyle.insertion = this.insertion;
        return chatstyle;
    }

    public ChatStyle createDeepCopy() {
        ChatStyle chatstyle = new ChatStyle();
        chatstyle.setBold(this.getBold());
        chatstyle.setItalic(this.getItalic());
        chatstyle.setStrikethrough(this.getStrikethrough());
        chatstyle.setUnderlined(this.getUnderlined());
        chatstyle.setObfuscated(this.getObfuscated());
        chatstyle.setColor(this.getColor());
        chatstyle.setChatClickEvent(this.getChatClickEvent());
        chatstyle.setChatHoverEvent(this.getChatHoverEvent());
        chatstyle.setInsertion(this.getInsertion());
        return chatstyle;
    }

    public static class Serializer
    implements JsonDeserializer<ChatStyle>,
    JsonSerializer<ChatStyle> {
        @Override
        public ChatStyle deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            JsonObject jsonobject1;
            if (!p_deserialize_1_.isJsonObject()) return null;
            ChatStyle chatstyle = new ChatStyle();
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            if (jsonobject == null) {
                return null;
            }
            if (jsonobject.has("bold")) {
                chatstyle.bold = jsonobject.get("bold").getAsBoolean();
            }
            if (jsonobject.has("italic")) {
                chatstyle.italic = jsonobject.get("italic").getAsBoolean();
            }
            if (jsonobject.has("underlined")) {
                chatstyle.underlined = jsonobject.get("underlined").getAsBoolean();
            }
            if (jsonobject.has("strikethrough")) {
                chatstyle.strikethrough = jsonobject.get("strikethrough").getAsBoolean();
            }
            if (jsonobject.has("obfuscated")) {
                chatstyle.obfuscated = jsonobject.get("obfuscated").getAsBoolean();
            }
            if (jsonobject.has("color")) {
                chatstyle.color = (EnumChatFormatting)((Object)p_deserialize_3_.deserialize(jsonobject.get("color"), (Type)((Object)EnumChatFormatting.class)));
            }
            if (jsonobject.has("insertion")) {
                chatstyle.insertion = jsonobject.get("insertion").getAsString();
            }
            if (jsonobject.has("clickEvent") && (jsonobject1 = jsonobject.getAsJsonObject("clickEvent")) != null) {
                String s;
                JsonPrimitive jsonprimitive = jsonobject1.getAsJsonPrimitive("action");
                ClickEvent.Action clickevent$action = jsonprimitive == null ? null : ClickEvent.Action.getValueByCanonicalName(jsonprimitive.getAsString());
                JsonPrimitive jsonprimitive1 = jsonobject1.getAsJsonPrimitive("value");
                String string = s = jsonprimitive1 == null ? null : jsonprimitive1.getAsString();
                if (clickevent$action != null && s != null && clickevent$action.shouldAllowInChat()) {
                    chatstyle.chatClickEvent = new ClickEvent(clickevent$action, s);
                }
            }
            if (!jsonobject.has("hoverEvent")) return chatstyle;
            JsonObject jsonobject2 = jsonobject.getAsJsonObject("hoverEvent");
            if (jsonobject2 == null) return chatstyle;
            JsonPrimitive jsonprimitive2 = jsonobject2.getAsJsonPrimitive("action");
            HoverEvent.Action hoverevent$action = jsonprimitive2 == null ? null : HoverEvent.Action.getValueByCanonicalName(jsonprimitive2.getAsString());
            IChatComponent ichatcomponent = (IChatComponent)p_deserialize_3_.deserialize(jsonobject2.get("value"), (Type)((Object)IChatComponent.class));
            if (hoverevent$action == null) return chatstyle;
            if (ichatcomponent == null) return chatstyle;
            if (!hoverevent$action.shouldAllowInChat()) return chatstyle;
            chatstyle.chatHoverEvent = new HoverEvent(hoverevent$action, ichatcomponent);
            return chatstyle;
        }

        @Override
        public JsonElement serialize(ChatStyle p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
            if (p_serialize_1_.isEmpty()) {
                return null;
            }
            JsonObject jsonobject = new JsonObject();
            if (p_serialize_1_.bold != null) {
                jsonobject.addProperty("bold", p_serialize_1_.bold);
            }
            if (p_serialize_1_.italic != null) {
                jsonobject.addProperty("italic", p_serialize_1_.italic);
            }
            if (p_serialize_1_.underlined != null) {
                jsonobject.addProperty("underlined", p_serialize_1_.underlined);
            }
            if (p_serialize_1_.strikethrough != null) {
                jsonobject.addProperty("strikethrough", p_serialize_1_.strikethrough);
            }
            if (p_serialize_1_.obfuscated != null) {
                jsonobject.addProperty("obfuscated", p_serialize_1_.obfuscated);
            }
            if (p_serialize_1_.color != null) {
                jsonobject.add("color", p_serialize_3_.serialize((Object)p_serialize_1_.color));
            }
            if (p_serialize_1_.insertion != null) {
                jsonobject.add("insertion", p_serialize_3_.serialize(p_serialize_1_.insertion));
            }
            if (p_serialize_1_.chatClickEvent != null) {
                JsonObject jsonobject1 = new JsonObject();
                jsonobject1.addProperty("action", p_serialize_1_.chatClickEvent.getAction().getCanonicalName());
                jsonobject1.addProperty("value", p_serialize_1_.chatClickEvent.getValue());
                jsonobject.add("clickEvent", jsonobject1);
            }
            if (p_serialize_1_.chatHoverEvent == null) return jsonobject;
            JsonObject jsonobject2 = new JsonObject();
            jsonobject2.addProperty("action", p_serialize_1_.chatHoverEvent.getAction().getCanonicalName());
            jsonobject2.add("value", p_serialize_3_.serialize(p_serialize_1_.chatHoverEvent.getValue()));
            jsonobject.add("hoverEvent", jsonobject2);
            return jsonobject;
        }
    }
}

