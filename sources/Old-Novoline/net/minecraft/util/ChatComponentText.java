package net.minecraft.util;

public class ChatComponentText extends ChatComponentStyle {

    private String text;

    public ChatComponentText(String msg) {
        this.text = msg;
    }

    /**
     * Gets the text value of this ChatComponentText.
     */
    public String getChatComponentText_TextValue() {
        return this.text;
    }

    /**
     * Gets the text of this component, without any special formatting codes added, for chat.
     */
    public String getUnformattedTextForChat() {
        return this.text;
    }

    /**
     * Creates a copy of this component.  Almost a deep copy, except the style is shallow-copied.
     */
    public ChatComponentText createCopy() {
        ChatComponentText chatcomponenttext = new ChatComponentText(this.text);
        chatcomponenttext.setChatStyle(this.getChatStyle().createShallowCopy());

        for (IChatComponent ichatcomponent : this.getSiblings()) {
            chatcomponenttext.appendSibling(ichatcomponent.createCopy());
        }

        return chatcomponenttext;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof ChatComponentText)) {
            return false;
        } else {
            ChatComponentText chatcomponenttext = (ChatComponentText) o;
            return this.text.equals(chatcomponenttext.getChatComponentText_TextValue()) && super.equals(o);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString() {
        return "TextComponent{text=\'" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }

}
