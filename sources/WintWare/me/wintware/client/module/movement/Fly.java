package me.wintware.client.module.movement;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdateLiving;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.movement.MovementUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class Fly
extends Module {
    Setting speed;

    public Fly() {
        super("Fly", Category.Movement);
        ArrayList<String> mode = new ArrayList<String>();
        mode.add("Vanilla");
        mode.add("Motion");
        Main.instance.setmgr.rSetting(new Setting("Fly Mode", this, "Vanilla", mode));
        this.speed = new Setting("Speed", this, 1.0, 0.1, 15.0, false);
        Main.instance.setmgr.rSetting(this.speed);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.player.speedInAir = 0.02f;
        Fly.mc.timer.timerSpeed = 1.0f;
        Minecraft.player.capabilities.isFlying = false;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdateLiving e) {
        String mode = Main.instance.setmgr.getSettingByName("Fly Mode").getValString();
        if (mode.equalsIgnoreCase("Motion")) {
            Minecraft.player.fallDistance = 0.0f;
            Minecraft.player.motionX = 0.0;
            Minecraft.player.motionY = 0.0;
            Minecraft.player.motionZ = 0.0;
            Minecraft.player.posY += 0.1;
            Minecraft.player.posY -= 0.1;
            MovementUtil.setSpeed(this.speed.getValDouble());
            if (Fly.mc.gameSettings.keyBindJump.isKeyDown()) {
                EntityPlayerSP player = Minecraft.player;
                Minecraft.player.motionY += 1.0;
            }
            if (Fly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                Minecraft.player.motionY -= 1.0;
            }
        }
        if (mode.equalsIgnoreCase("Vanilla")) {
            Minecraft.player.capabilities.isFlying = true;
            Minecraft.player.capabilities.setFlySpeed((float)this.speed.getValDouble());
        }
    }
}

