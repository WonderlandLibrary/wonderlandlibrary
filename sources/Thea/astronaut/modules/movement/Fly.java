package astronaut.modules.movement;

import astronaut.Duckware;
import astronaut.events.EventReceivedPacket;
import astronaut.events.EventUpdate;
import astronaut.modules.Category;
import astronaut.modules.Module;
import de.Hero.settings.Setting;
import net.minecraft.client.Minecraft;
import eventapi.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class Fly extends Module {

    public Fly() {
        super("Fly", Type.Movement, 0, Category.MOVEMENT, Color.green, "Makes You Fly");
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (Duckware.setmgr.getSettingByName("Mode").getValString().equalsIgnoreCase("Creative"))
            setCapabilities(true);
        else if (Duckware.setmgr.getSettingByName("Mode").getValString().equalsIgnoreCase("Float"))
            mc.thePlayer.motionY = 0;
    }

    public void onDisable() {
        mc.timer.timerSpeed = 1;

        setCapabilities(false);

        mc.thePlayer.capabilities.setFlySpeed(0.1F);
    }

    private void setCapabilities(final boolean state) {
        mc.thePlayer.capabilities.isFlying = state;
        mc.thePlayer.capabilities.isCreativeMode = state;
        mc.thePlayer.capabilities.allowFlying = state;
        mc.thePlayer.capabilities.setFlySpeed(0.1F);
    }

    @Override
    public void onEnable() {

    }

    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Creative");
        options.add("Float");
        Duckware.setmgr.rSetting(new Setting("Mode", this, "Creative", options));
    }
}