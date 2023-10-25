package net.minecraft.util;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.EnumChatFormatting;

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
    private static final ChatStyle rootStyle = new /* Unavailable Anonymous Inner Class!! */;

    public EnumChatFormatting getColor() {
        return this.color == null ? this.getParent().getColor() : this.color;
    }

    public boolean getBold() {
        return this.bold == null ? this.getParent().getBold() : this.bold.booleanValue();
    }

    public boolean getItalic() {
        return this.italic == null ? this.getParent().getItalic() : this.italic.booleanValue();
    }

    public boolean getStrikethrough() {
        return this.strikethrough == null ? this.getParent().getStrikethrough() : this.strikethrough.booleanValue();
    }

    public boolean getUnderlined() {
        return this.underlined == null ? this.getParent().getUnderlined() : this.underlined.booleanValue();
    }

    public boolean getObfuscated() {
        return this.obfuscated == null ? this.getParent().getObfuscated() : this.obfuscated.booleanValue();
    }

    public boolean isEmpty() {
        return this.bold == null && this.italic == null && this.strikethrough == null && this.underlined == null && this.obfuscated == null && this.color == null && this.chatClickEvent == null && this.chatHoverEvent == null;
    }

    public ClickEvent getChatClickEvent() {
        return this.chatClickEvent == null ? this.getParent().getChatClickEvent() : this.chatClickEvent;
    }

    public HoverEvent getChatHoverEvent() {
        return this.chatHoverEvent == null ? this.getParent().getChatHoverEvent() : this.chatHoverEvent;
    }

    public String getInsertion() {
        return this.insertion == null ? this.getParent().getInsertion() : this.insertion;
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
            return this.parentStyle != null ? this.parentStyle.getFormattingCode() : "";
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
        if (this.getStrikethrough()) {
            stringbuilder.append((Object)EnumChatFormatting.STRIKETHROUGH);
        }
        return stringbuilder.toString();
    }

    private ChatStyle getParent() {
        return this.parentStyle == null ? rootStyle : this.parentStyle;
    }

    public String toString() {
        return "Style{hasParent=" + (this.parentStyle != null) + ", color=" + this.color + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", obfuscated=" + this.obfuscated + ", clickEvent=" + this.getChatClickEvent() + ", hoverEvent=" + this.getChatHoverEvent() + ", insertion=" + this.getInsertion() + '}';
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatStyle)) {
            return false;
        }
        ChatStyle chatstyle = (ChatStyle)p_equals_1_;
        if (this.getBold() != chatstyle.getBold() || this.getColor() != chatstyle.getColor() || this.getItalic() != chatstyle.getItalic() || this.getObfuscated() != chatstyle.getObfuscated() || this.getStrikethrough() != chatstyle.getStrikethrough() || this.getUnderlined() != chatstyle.getUnderlined() || (this.getChatClickEvent() == null ? chatstyle.getChatClickEvent() != null : !this.getChatClickEvent().equals((Object)chatstyle.getChatClickEvent())) || (this.getChatHoverEvent() == null ? chatstyle.getChatHoverEvent() != null : !this.getChatHoverEvent().equals((Object)chatstyle.getChatHoverEvent())) || !(this.getInsertion() != null ? this.getInsertion().equals((Object)chatstyle.getInsertion()) : chatstyle.getInsertion() == null)) {
            boolean flag = false;
            return flag;
        }
        boolean flag = true;
        return flag;
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
        i = 31 * i + this.insertion.hashCode();
        return i;
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

    static /* synthetic */ Boolean access$002(ChatStyle x0, Boolean x1) {
        x0.bold = x1;
        return x0.bold;
    }

    static /* synthetic */ Boolean access$102(ChatStyle x0, Boolean x1) {
        x0.italic = x1;
        return x0.italic;
    }

    static /* synthetic */ Boolean access$202(ChatStyle x0, Boolean x1) {
        x0.underlined = x1;
        return x0.underlined;
    }

    static /* synthetic */ Boolean access$302(ChatStyle x0, Boolean x1) {
        x0.strikethrough = x1;
        return x0.strikethrough;
    }

    static /* synthetic */ Boolean access$402(ChatStyle x0, Boolean x1) {
        x0.obfuscated = x1;
        return x0.obfuscated;
    }

    static /* synthetic */ EnumChatFormatting access$502(ChatStyle x0, EnumChatFormatting x1) {
        x0.color = x1;
        return x0.color;
    }

    static /* synthetic */ String access$602(ChatStyle x0, String x1) {
        x0.insertion = x1;
        return x0.insertion;
    }

    static /* synthetic */ ClickEvent access$702(ChatStyle x0, ClickEvent x1) {
        x0.chatClickEvent = x1;
        return x0.chatClickEvent;
    }

    static /* synthetic */ HoverEvent access$802(ChatStyle x0, HoverEvent x1) {
        x0.chatHoverEvent = x1;
        return x0.chatHoverEvent;
    }

    static /* synthetic */ Boolean access$000(ChatStyle x0) {
        return x0.bold;
    }

    static /* synthetic */ Boolean access$100(ChatStyle x0) {
        return x0.italic;
    }

    static /* synthetic */ Boolean access$200(ChatStyle x0) {
        return x0.underlined;
    }

    static /* synthetic */ Boolean access$300(ChatStyle x0) {
        return x0.strikethrough;
    }

    static /* synthetic */ Boolean access$400(ChatStyle x0) {
        return x0.obfuscated;
    }

    static /* synthetic */ EnumChatFormatting access$500(ChatStyle x0) {
        return x0.color;
    }

    static /* synthetic */ String access$600(ChatStyle x0) {
        return x0.insertion;
    }

    static /* synthetic */ ClickEvent access$700(ChatStyle x0) {
        return x0.chatClickEvent;
    }

    static /* synthetic */ HoverEvent access$800(ChatStyle x0) {
        return x0.chatHoverEvent;
    }
}
