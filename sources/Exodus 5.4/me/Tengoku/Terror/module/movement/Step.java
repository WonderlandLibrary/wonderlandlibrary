/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.movement;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.PlayerUtils;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Step
extends Module {
    TimerUtils timer = new TimerUtils();

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("NCP");
        arrayList.add("AAC");
        arrayList.add("Packet");
        arrayList.add("Vanilla");
        arrayList.add("Hypixel");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Step Mode", (Module)this, "NCP", arrayList));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.thePlayer.stepHeight = 0.5f;
        Step.mc.timer.timerSpeed = 1.0f;
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Step Mode").getValString();
        this.setDisplayName("Step \ufffdf" + string);
        if (string.equalsIgnoreCase("AAC")) {
            Minecraft.thePlayer.stepHeight = 0.5f;
            if (PlayerUtils.canStep(1.5) || PlayerUtils.canStep(1.0)) {
                Step.mc.timer.timerSpeed = 0.15f;
                Minecraft.thePlayer.motionY = 0.47;
            } else {
                Step.mc.timer.timerSpeed = 1.0f;
            }
        }
        if (string.equalsIgnoreCase("Vanilla")) {
            Minecraft.thePlayer.stepHeight = 2.0f;
        }
        if (string.equalsIgnoreCase("Packet")) {
            if (PlayerUtils.canStep(1.0)) {
                Minecraft.gameSettings.keyBindJump.pressed = false;
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.42, Minecraft.thePlayer.posZ, Minecraft.thePlayer.onGround));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.75, Minecraft.thePlayer.posZ, Minecraft.thePlayer.onGround));
                Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.72, Minecraft.thePlayer.posZ);
            } else {
                Step.mc.timer.timerSpeed = 1.0f;
            }
        }
    }

    public Step() {
        super("Step", 38, Category.MOVEMENT, "Automatically steps on blocks for you.");
    }

    @EventTarget
    public void onPre(EventMotion eventMotion) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Step Mode").getValString();
        if (string.equalsIgnoreCase("Hypixel") && PlayerUtils.canStep(1.0)) {
            Minecraft.thePlayer.motionY = 0.37;
        }
        if (string.equalsIgnoreCase("NCP")) {
            Minecraft.thePlayer.stepHeight = 0.5f;
            if (PlayerUtils.canStep(1.0)) {
                Step.mc.timer.timerSpeed = 2.0f;
                Minecraft.thePlayer.motionY = 0.37;
            } else if (!Minecraft.thePlayer.isCollidedHorizontally) {
                Step.mc.timer.timerSpeed = 1.0f;
            }
        }
    }
}

