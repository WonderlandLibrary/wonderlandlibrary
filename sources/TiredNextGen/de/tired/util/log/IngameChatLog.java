package de.tired.util.log;

import de.tired.base.interfaces.IHook;
import de.tired.Tired;
import net.minecraft.util.ChatComponentText;

public enum IngameChatLog implements IHook {

    INGAME_CHAT_LOG;

    public void doLog(String text) {
        MC.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§7[§9" + "TIRED-" + "§fnextgen." + "§7] §l" + text));
    }


}
