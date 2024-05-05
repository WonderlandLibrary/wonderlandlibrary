package best.azura.client.impl.command.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.command.ACommand;
import best.azura.client.api.module.Module;
import best.azura.client.util.render.ShaderManager;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;

public class ReloadCommand extends ACommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Load or save configs";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "rl" };
    }


    @Override
    public void handleCommand(String[] args) {
        Client.INSTANCE.unload(true);
        for (Module m : Client.INSTANCE.getModuleManager().getRegistered()) m.setEnabled(false);
        Client.INSTANCE.load();
        mc.thePlayer.addChatMessage(new ChatComponentText(Client.PREFIX + "Reloaded client."));
        Display.setTitle("Minecraft 1.8.9");
        ShaderManager.reloadShaders();
    }
}
