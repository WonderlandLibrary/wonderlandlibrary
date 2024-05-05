package net.minecraft.util;

import java.util.List;
import net.minecraft.util.ChatStyle;

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
}
