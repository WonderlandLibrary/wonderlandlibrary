package net.minecraft.util;

public class ChatComponentSelector extends ChatComponentStyle {
    /**
     * The selector used to find the matching entities of this text component
     */
    private final String selector;

    public ChatComponentSelector(String selectorIn) {
        this.selector = selectorIn;
    }

    /**
     * Gets the selector of this component, in plain text.
     */
    public String getSelector() {
        return this.selector;
    }

    /**
     * Gets the text of this component, without any special formatting codes added, for chat.
     */
    public String getUnformattedTextForChat() {
        return this.selector;
    }

    /**
     * Creates a copy of this component.  Almost a deep copy, except the style is shallow-copied.
     */
    public ChatComponentSelector createCopy() {
        ChatComponentSelector chatcomponentselector = new ChatComponentSelector(this.selector);
        chatcomponentselector.setChatStyle(this.getChatStyle().createShallowCopy());

        for (IChatComponent ichatcomponent : this.getSiblings()) {
            chatcomponentselector.appendSibling(ichatcomponent.createCopy());
        }

        return chatcomponentselector;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof ChatComponentSelector)) {
            return false;
        } else {
            ChatComponentSelector chatcomponentselector = (ChatComponentSelector) o;
            return this.selector.equals(chatcomponentselector.selector) && super.equals(o);
        }
    }

    public String toString() {
        return "SelectorComponent{pattern=\'" + this.selector + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
}
