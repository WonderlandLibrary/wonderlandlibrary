/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.movement;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.movement.MovementUtil;
import net.minecraft.client.Minecraft;

public class Jesus
extends Module {
    public Jesus() {
        super("Jesus", Category.Movement);
        ArrayList<String> jesus = new ArrayList<String>();
        jesus.add("MiniJump");
        jesus.add("Matrix");
        Main.instance.setmgr.rSetting(new Setting("Jesus Mode", this, "Matrix", jesus));
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        String mode = Main.instance.setmgr.getSettingByName("Jesus Mode").getValString();
        if (mode.equalsIgnoreCase("MiniJump")) {
            if (Minecraft.player.isInWater()) {
                if (Minecraft.player.motionY < 0.0) {
                    Minecraft.player.jump();
                    MovementUtil.setSpeed(0.3);
                }
            }
        }
        if (mode.equalsIgnoreCase("Matrix")) {
            if (!Minecraft.player.isInLiquid2()) {
                return;
            }
            if (!Minecraft.player.isCollidedHorizontally) {
                Jesus.mc.gameSettings.keyBindJump.pressed = false;
            }
            MovementUtil.setSpeed(0.12);
            if (Minecraft.player.ticksExisted % 1 == 0) {
                Minecraft.player.motionY = 0.001;
            }
            if (Minecraft.player.ticksExisted % 2 == 0) {
                Minecraft.player.motionY = -0.001;
            }
            if (Minecraft.player.isCollidedHorizontally && Jesus.mc.gameSettings.keyBindJump.isKeyDown()) {
                Minecraft.player.motionY = 0.2;
            }
        }
    }
}

