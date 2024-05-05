package best.azura.client.api.command;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public abstract class ACommand implements ICommand {
    protected final Minecraft mc = Minecraft.getMinecraft();
    protected final void msg(String text) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(text));
    }
}