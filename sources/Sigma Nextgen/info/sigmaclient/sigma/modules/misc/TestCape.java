package info.sigmaclient.sigma.modules.misc;

import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.WindowUpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.glfw.GLFW;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class TestCape extends Module {
    public TestCape() {
        super("TestCape", Category.Misc, "TestCape.");
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onWindowUpdateEvent(WindowUpdateEvent event) {
        mc.player.setElytraOfCape(false);
//        mc.player.isWearing();
        mc.player.setLocationOfCape(new ResourceLocation("sigmang/images/jelloblur.png"));
        super.onWindowUpdateEvent(event);
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        super.onPacketEvent(event);
    }
}
