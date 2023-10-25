/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.render;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.event.events.EventPacket;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate
extends Module {
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("NoRotate Mode").getValString();
        this.setDisplayName("No Rotate \ufffdf" + string);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onPre(EventMotion eventMotion) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("NoRotate Mode").getValString();
        if (string.equalsIgnoreCase("Client")) {
            Minecraft.thePlayer.rotationYawHead = EventMotion.getYaw();
            Minecraft.thePlayer.renderYawOffset = EventMotion.getYaw();
            eventMotion.setPitch(0.0f);
            if (Exodus.INSTANCE.moduleManager.getModuleByName("Head Rotations").isToggled()) {
                Minecraft.thePlayer.rotationPitchHead = EventMotion.getPitch();
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Packet");
        arrayList.add("Client");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("NoRotate Mode", (Module)this, "Packet", arrayList));
    }

    public NoRotate() {
        super("No Rotate", 0, Category.MISC, "");
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        EventPacket eventPacket2;
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("NoRotate Mode").getValString();
        if (string.equalsIgnoreCase("Client") && eventPacket.isRecieving() && eventPacket.getPacket() instanceof S08PacketPlayerPosLook) {
            Minecraft.thePlayer.rotationPitch = 0.0f;
        }
        if (string.equalsIgnoreCase("Packet") && (eventPacket2 = eventPacket).isRecieving() && eventPacket2.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook s08PacketPlayerPosLook = (S08PacketPlayerPosLook)eventPacket2.getPacket();
            s08PacketPlayerPosLook.yaw = Minecraft.thePlayer.rotationYaw;
            s08PacketPlayerPosLook.pitch = Minecraft.thePlayer.rotationPitch;
            if (Exodus.INSTANCE.moduleManager.getModuleByName("Head Rotations").isToggled()) {
                Minecraft.thePlayer.rotationYawHead = s08PacketPlayerPosLook.yaw;
                Minecraft.thePlayer.renderYawOffset = s08PacketPlayerPosLook.yaw;
                Minecraft.thePlayer.rotationPitchHead = s08PacketPlayerPosLook.pitch;
            }
        }
    }
}

