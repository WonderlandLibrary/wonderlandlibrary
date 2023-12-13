/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.movement;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;

public class Strafe
extends Module {
    public static Setting speed;

    public Strafe() {
        super("Strafe", Category.Movement);
        speed = new Setting("Speed", this, 50.0, 20.0, 100.0, false);
        Main.instance.setmgr.rSetting(speed);
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate e) {
        block13: {
            float zD;
            float var9;
            block14: {
                double speed1 = speed.getValDouble();
                this.setSuffix(speed1 + "%");
                if (Minecraft.player.isInWater()) break block13;
                float forward = MovementInput.moveForward;
                float strafe = MovementInput.moveStrafe;
                float dir = Minecraft.player.rotationYaw;
                if (forward == 0.0f && strafe == 0.0f) {
                    Minecraft.player.motionX = 0.0;
                    Minecraft.player.motionZ = 0.0;
                } else if (forward != 0.0f) {
                    if (strafe >= 1.0f) {
                        dir += (float)(forward > 0.0f ? -45 : 45);
                    } else if (strafe <= -1.0f) {
                        dir += (float)(forward > 0.0f ? 45 : -45);
                    }
                }
                double da = 0.004079999999999999 * speed.getValDouble();
                if (Minecraft.player.isSprinting()) {
                    da *= 1.3190000119209289;
                }
                if (Minecraft.player.isSneaking()) {
                    da *= 0.3;
                }
                if (Minecraft.player.onGround) {
                    da *= 0.011000000000000001 * speed.getValDouble();
                }
                var9 = (float)((double)((float)Math.cos((double)(dir + 90.0f) * Math.PI / 180.0)) * da);
                zD = (float)((double)((float)Math.sin((double)(dir + 90.0f) * Math.PI / 180.0)) * da);
                if (Strafe.mc.gameSettings.keyBindForward.pressed || Strafe.mc.gameSettings.keyBindLeft.pressed) break block14;
                if (!Strafe.mc.gameSettings.keyBindRight.pressed) break block13;
            }
            if (!Strafe.mc.gameSettings.keyBindBack.pressed) {
                Minecraft.player.motionX = var9;
                Minecraft.player.motionZ = zD;
            }
        }
    }
}

