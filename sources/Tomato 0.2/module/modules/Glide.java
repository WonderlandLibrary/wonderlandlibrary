/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.commands.CommandManager;
import cow.milkgod.cheese.events.EventPacketSent;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.modules.Phase;
import cow.milkgod.cheese.utils.Logger;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerCapabilities;

public class Glide
extends Module {
    public static double bypassdelay;
    public static double warningdelay;
    public static double speed;
    public static double maxPosY;
    double delay;

    static {
        warningdelay = 0.0;
        speed = 0.0315;
    }

    public Glide() {
        super("Glide", 0, Category.MOVEMENT, 6697932, true, "Glide of ledges", new String[]{"float"});
    }

    @Override
    public void toggleModule() {
        super.toggleModule();
        if (this.getState()) {
            maxPosY = Wrapper.mc.thePlayer.posY;
            Wrapper.mc.thePlayer.capabilities.allowFlying = false;
            Cheese.getInstance();
            Cheese.commandManager.getCommandbyName("Damage").Toggle();
        }
    }

    @EventTarget
    public void onPre(EventTick event) {
        if (Glide.isGliding()) {
            boolean bypass = false;
            if (Wrapper.mc.thePlayer.isSneaking()) {
                Wrapper.mc.thePlayer.motionY = -0.4;
            } else if (Wrapper.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                Wrapper.mc.thePlayer.motionY = 0.4;
            } else {
                double speed = Glide.speed;
                if (Phase.isInsideBlock()) {
                    speed = 0.0;
                }
                Wrapper.mc.thePlayer.motionY = - speed;
            }
            if (Wrapper.mc.thePlayer.posY + 0.1 >= maxPosY && Wrapper.mc.gameSettings.keyBindJump.getIsKeyPressed() && (warningdelay += 1.0) >= 10.0) {
                Logger.logChat("Cant go any higher, you will flag if you do.");
                warningdelay = 0.0;
            }
        }
    }

    @EventTarget
    public void OnSend(EventPacketSent e2) {
    }

    public static boolean isGliding() {
        if (!Wrapper.mc.thePlayer.onGround && !Wrapper.mc.thePlayer.isCollidedVertically) {
            return true;
        }
        return false;
    }
}

