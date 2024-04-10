package info.sigmaclient.sigma.modules.misc;

import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.WindowUpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import org.lwjgl.glfw.GLFW;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class GameIdler extends Module {
    public GameIdler() {
        super("GameIdler", Category.Misc, "Save memory when your focus on other windows.");
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.getMainWindow().setFramerateLimit(mc.gameSettings.framerateLimit);
        super.onDisable();
    }

    @Override
    public void onWindowUpdateEvent(WindowUpdateEvent event) {
        if (GLFW.glfwGetWindowAttrib(mc.getMainWindow().getHandle(), 131073) == 1) {
            mc.getMainWindow().setFramerateLimit(mc.gameSettings.framerateLimit);
        }
        else {
            mc.getMainWindow().setFramerateLimit(5);
        }
        super.onWindowUpdateEvent(event);
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        super.onPacketEvent(event);
    }
}
