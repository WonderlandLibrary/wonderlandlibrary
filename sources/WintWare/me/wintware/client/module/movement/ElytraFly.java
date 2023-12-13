package me.wintware.client.module.movement;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdateLiving;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.movement.MovementUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ElytraFly
extends Module {
    Setting motion = new Setting("Speed", this, 30.0, 20.0, 100.0, false);

    public ElytraFly() {
        super("ElytraFly", Category.Movement);
        Main.instance.setmgr.rSetting(this.motion);
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventTarget
    public void onUpdateLiving(EventUpdateLiving e) {
        ItemStack chest = Minecraft.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (chest.getItem() != Items.ELYTRA) {
            return;
        }
        if (!Minecraft.player.isElytraFlying()) return;
        Minecraft.player.motionY = 0.0;
        Minecraft.player.capabilities.isFlying = true;
        Minecraft.player.capabilities.setFlySpeed(0.6f);
        MovementUtil.setSpeed(0.4);
        if (ElytraFly.mc.gameSettings.keyBindForward.pressed) {
            if (Minecraft.player.getPosition().getY() < 256) {
                float yaw = (float)Math.toRadians(Minecraft.player.rotationYaw);
                EntityPlayerSP player3 = Minecraft.player;
                player3.motionX -= Math.sin(yaw) * (double)0.05f / 100.0 * this.motion.getValDouble();
                EntityPlayerSP player4 = Minecraft.player;
                player4.motionZ += Math.cos(yaw) * (double)0.05f / 100.0 * this.motion.getValDouble();
                return;
            }
        }
        if (!ElytraFly.mc.gameSettings.keyBindBack.pressed) return;
        if (Minecraft.player.getPosition().getY() >= 256) return;
        float yaw = (float)Math.toRadians(Minecraft.player.rotationYaw);
        EntityPlayerSP player5 = Minecraft.player;
        player5.motionX += Math.sin(yaw) * (double)0.05f;
        EntityPlayerSP player6 = Minecraft.player;
        player6.motionZ -= Math.cos(yaw) * (double)0.05f;
    }
}

