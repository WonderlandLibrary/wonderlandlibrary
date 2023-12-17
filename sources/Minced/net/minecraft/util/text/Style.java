// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.text;

import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import javax.annotation.Nullable;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.event.ClickEvent;

public class Style
{
    private Style parentStyle;
    private TextFormatting color;
    private Boolean bold;
    private Boolean italic;
    private Boolean underlined;
    private Boolean strikethrough;
    private Boolean obfuscated;
    private ClickEvent clickEvent;
    private HoverEvent hoverEvent;
    private String insertion;
    private static final Style ROOT;
    
    @Nullable
    public TextFormatting getColor() {
        return (this.color == null) ? this.getParent().getColor() : this.color;
    }
    
    public boolean getBold() {
        return (this.bold == null) ? this.getParent().getBold() : this.bold;
    }
    
    public boolean getItalic() {
        return (this.italic == null) ? this.getParent().getItalic() : this.italic;
    }
    
    public boolean getStrikethrough() {
        return (this.strikethrough == null) ? this.getParent().getStrikethrough() : this.strikethrough;
    }
    
    public boolean getUnderlined() {
        return (this.underlined == null) ? this.getParent().getUnderlined() : this.underlined;
    }
    
    public boolean getObfuscated() {
        return (this.obfuscated == null) ? this.getParent().getObfuscated() : this.obfuscated;
    }
    
    public boolean isEmpty() {
        return this.bold == null && this.italic == null && this.strikethrough == null && this.underlined == null && this.obfuscated == null && this.color == null && this.clickEvent == null && this.hoverEvent == null && this.insertion == null;
    }
    
    @Nullable
    public ClickEvent getClickEvent() {
        return (this.clickEvent == null) ? this.getParent().getClickEvent() : this.clickEvent;
    }
    
    @Nullable
    public HoverEvent getHoverEvent() {
        return (this.hoverEvent == null) ? this.getParent().getHoverEvent() : this.hoverEvent;
    }
    
    @Nullable
    public String getInsertion() {
        return (this.insertion == null) ? this.getParent().getInsertion() : this.insertion;
    }
    
    public Style setColor(final TextFormatting color) {
        this.color = color;
        return this;
    }
    
    public Style setBold(final Boolean boldIn) {
        this.bold = boldIn;
        return this;
    }
    
    public Style setItalic(final Boolean italic) {
        this.italic = italic;
        return this;
    }
    
    public Style setStrikethrough(final Boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }
    
    public Style setUnderlined(final Boolean underlined) {
        this.underlined = underlined;
        return this;
    }
    
    public Style setObfuscated(final Boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }
    
    public Style setClickEvent(final ClickEvent event) {
        this.clickEvent = event;
        return this;
    }
    
    public Style setHoverEvent(final HoverEvent event) {
        this.hoverEvent = event;
        return this;
    }
    
    public Style setInsertion(final String insertion) {
        this.insertion = insertion;
        return this;
    }
    
    public Style setParentStyle(final Style parent) {
        this.parentStyle = parent;
        return this;
    }
    
    public String getFormattingCode() {
        if (this.isEmpty()) {
            return (this.parentStyle != null) ? this.parentStyle.getFormattingCode() : "";
        }
        final StringBuilder stringbuilder = new StringBuilder();
        if (this.getColor() != null) {
            stringbuilder.append(this.getColor());
        }
        if (this.getBold()) {
            stringbuilder.append(TextFormatting.BOLD);
        }
        if (this.getItalic()) {
            stringbuilder.append(TextFormatting.ITALIC);
        }
        if (this.getUnderlined()) {
            stringbuilder.append(TextFormatting.UNDERLINE);
        }
        if (this.getObfuscated()) {
            stringbuilder.append(TextFormatting.OBFUSCATED);
        }
        if (this.getStrikethrough()) {
            stringbuilder.append(TextFormatting.STRIKETHROUGH);
        }
        return stringbuilder.toString();
    }
    
    private Style getParent() {
        return (this.parentStyle == null) ? Style.ROOT : this.parentStyle;
    }
    
    @Override
    public String toString() {
        return "Style{hasParent=" + (this.parentStyle != null) + ", color=" + this.color + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", obfuscated=" + this.obfuscated + ", clickEvent=" + this.getClickEvent() + ", hoverEvent=" + this.getHoverEvent() + ", insertion=" + this.getInsertion() + '}';
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof Style)) {
            return false;
        }
        final Style style = (Style)p_equals_1_;
        if (this.getBold() == style.getBold() && this.getColor() == style.getColor() && this.getItalic() == style.getItalic() && this.getObfuscated() == style.getObfuscated() && this.getStrikethrough() == style.getStrikethrough() && this.getUnderlined() == style.getUnderlined()) {
            if (this.getClickEvent() != null) {
                if (!this.getClickEvent().equals(style.getClickEvent())) {
                    return false;
                }
            }
            else if (style.getClickEvent() != null) {
                return false;
            }
            if (this.getHoverEvent() != null) {
                if (!this.getHoverEvent().equals(style.getHoverEvent())) {
                    return false;
                }
            }
            else if (style.getHoverEvent() != null) {
                return false;
            }
            if (this.getInsertion() != null) {
                if (!this.getInsertion().equals(style.getInsertion())) {
                    return false;
                }
            }
            else if (style.getInsertion() != null) {
                return false;
            }
            final boolean flag = true;
            return flag;
        }
        final boolean flag = false;
        return flag;
    }
    
    @Override
    public int hashCode() {
        int i = this.color.hashCode();
        i = 31 * i + this.bold.hashCode();
        i = 31 * i + this.italic.hashCode();
        i = 31 * i + this.underlined.hashCode();
        i = 31 * i + this.strikethrough.hashCode();
        i = 31 * i + this.obfuscated.hashCode();
        i = 31 * i + this.clickEvent.hashCode();
        i = 31 * i + this.hoverEvent.hashCode();
        i = 31 * i + this.insertion.hashCode();
        return i;
    }
    
    public Style createShallowCopy() {
        final Style style = new Style();
        style.bold = this.bold;
        style.italic = this.italic;
        style.strikethrough = this.strikethrough;
        style.underlined = this.underlined;
        style.obfuscated = this.obfuscated;
        style.color = this.color;
        style.clickEvent = this.clickEvent;
        style.hoverEvent = this.hoverEvent;
        style.parentStyle = this.parentStyle;
        style.insertion = this.insertion;
        return style;
    }
    
    public Style createDeepCopy() {
        final Style style = new Style();
        style.setBold(this.getBold());
        style.setItalic(this.getItalic());
        style.setStrikethrough(this.getStrikethrough());
        style.setUnderlined(this.getUnderlined());
        style.setObfuscated(this.getObfuscated());
        style.setColor(this.getColor());
        style.setClickEvent(this.getClickEvent());
        style.setHoverEvent(this.getHoverEvent());
        style.setInsertion(this.getInsertion());
        return style;
    }
    
    static {
        ROOT = new Style() {
            @Nullable
            @Override
            public TextFormatting getColor() {
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
            
            @Nullable
            @Override
            public ClickEvent getClickEvent() {
                return null;
            }
            
            @Nullable
            @Override
            public HoverEvent getHoverEvent() {
                return null;
            }
            
            @Nullable
            @Override
            public String getInsertion() {
                return null;
            }
            
            @Override
            public Style setColor(final TextFormatting color) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Style setBold(final Boolean boldIn) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Style setItalic(final Boolean italic) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Style setStrikethrough(final Boolean strikethrough) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Style setUnderlined(final Boolean underlined) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Style setObfuscated(final Boolean obfuscated) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Style setClickEvent(final ClickEvent event) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Style setHoverEvent(final HoverEvent event) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Style setParentStyle(final Style parent) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public String toString() {
                return "Style.ROOT";
            }
            
            @Override
            public Style createShallowCopy() {
                return this;
            }
            
            @Override
            public Style createDeepCopy() {
                return this;
            }
            
            @Override
            public String getFormattingCode() {
                return "";
            }
        };
    }
    
    public static class Serializer implements JsonDeserializer<Style>, JsonSerializer<Style>
    {
        @Nullable
        public Style deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            if (!p_deserialize_1_.isJsonObject()) {
                return null;
            }
            final Style style = new Style();
            final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            if (jsonobject == null) {
                return null;
            }
            if (jsonobject.has("bold")) {
                style.bold = jsonobject.get("bold").getAsBoolean();
            }
            if (jsonobject.has("italic")) {
                style.italic = jsonobject.get("italic").getAsBoolean();
            }
            if (jsonobject.has("underlined")) {
                style.underlined = jsonobject.get("underlined").getAsBoolean();
            }
            if (jsonobject.has("strikethrough")) {
                style.strikethrough = jsonobject.get("strikethrough").getAsBoolean();
            }
            if (jsonobject.has("obfuscated")) {
                style.obfuscated = jsonobject.get("obfuscated").getAsBoolean();
            }
            if (jsonobject.has("color")) {
                style.color = (TextFormatting)p_deserialize_3_.deserialize(jsonobject.get("color"), (Type)TextFormatting.class);
            }
            if (jsonobject.has("insertion")) {
                style.insertion = jsonobject.get("insertion").getAsString();
            }
            if (jsonobject.has("clickEvent")) {
                final JsonObject jsonobject2 = jsonobject.getAsJsonObject("clickEvent");
                if (jsonobject2 != null) {
                    final JsonPrimitive jsonprimitive = jsonobject2.getAsJsonPrimitive("action");
                    final ClickEvent.Action clickevent$action = (jsonprimitive == null) ? null : ClickEvent.Action.getValueByCanonicalName(jsonprimitive.getAsString());
                    final JsonPrimitive jsonprimitive2 = jsonobject2.getAsJsonPrimitive("value");
                    final String s = (jsonprimitive2 == null) ? null : jsonprimitive2.getAsString();
                    if (clickevent$action != null && s != null && clickevent$action.shouldAllowInChat()) {
                        style.clickEvent = new ClickEvent(clickevent$action, s);
                    }
                }
            }
            if (jsonobject.has("hoverEvent")) {
                final JsonObject jsonobject3 = jsonobject.getAsJsonObject("hoverEvent");
                if (jsonobject3 != null) {
                    final JsonPrimitive jsonprimitive3 = jsonobject3.getAsJsonPrimitive("action");
                    final HoverEvent.Action hoverevent$action = (jsonprimitive3 == null) ? null : HoverEvent.Action.getValueByCanonicalName(jsonprimitive3.getAsString());
                    final ITextComponent itextcomponent = (ITextComponent)p_deserialize_3_.deserialize(jsonobject3.get("value"), (Type)ITextComponent.class);
                    if (hoverevent$action != null && itextcomponent != null && hoverevent$action.shouldAllowInChat()) {
                        style.hoverEvent = new HoverEvent(hoverevent$action, itextcomponent);
                    }
                }
            }
            return style;
        }
        
        @Nullable
        public JsonElement serialize(final Style p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            if (p_serialize_1_.isEmpty()) {
                return null;
            }
            final JsonObject jsonobject = new JsonObject();
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
                jsonobject.add("insertion", p_serialize_3_.serialize((Object)p_serialize_1_.insertion));
            }
            if (p_serialize_1_.clickEvent != null) {
                final JsonObject jsonobject2 = new JsonObject();
                jsonobject2.addProperty("action", p_serialize_1_.clickEvent.getAction().getCanonicalName());
                jsonobject2.addProperty("value", p_serialize_1_.clickEvent.getValue());
                jsonobject.add("clickEvent", (JsonElement)jsonobject2);
            }
            if (p_serialize_1_.hoverEvent != null) {
                final JsonObject jsonobject3 = new JsonObject();
                jsonobject3.addProperty("action", p_serialize_1_.hoverEvent.getAction().getCanonicalName());
                jsonobject3.add("value", p_serialize_3_.serialize((Object)p_serialize_1_.hoverEvent.getValue()));
                jsonobject.add("hoverEvent", (JsonElement)jsonobject3);
            }
            return (JsonElement)jsonobject;
        }
    }
}
