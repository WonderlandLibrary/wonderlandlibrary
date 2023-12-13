/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.player;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdateLiving;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoClip
extends Module {
    private float gamma;

    public NoClip() {
        super("NoClip", Category.Player);
    }

    @EventTarget
    public void onLivingUpdate(EventUpdateLiving event) {
        if (Minecraft.player != null) {
            Minecraft.player.noClip = true;
            Minecraft.player.motionY = 0.0;
            Minecraft.player.onGround = false;
            Minecraft.player.capabilities.isFlying = false;
            if (NoClip.mc.gameSettings.keyBindJump.isKeyDown()) {
                Minecraft.player.motionY += 0.5;
            }
            if (NoClip.mc.gameSettings.keyBindSneak.isKeyDown()) {
                Minecraft.player.motionY -= 0.5;
            }
        } else {
            double d = (double)Minecraft.player.getHorizontalFacing().getDirectionVec().getX() * 1.273197475E-15;
            double d2 = (double)Minecraft.player.getHorizontalFacing().getDirectionVec().getZ() * 1.273197475E-15;
            Minecraft.player.motionY = 0.0;
            if (NoClip.mc.gameSettings.keyBindJump.isKeyDown()) {
                Minecraft.player.motionY += 4.24399158E-15;
            }
            if (NoClip.mc.gameSettings.keyBindSneak.isKeyDown()) {
                Minecraft.player.motionY -= 4.24399158E-15;
            }
            if (Minecraft.player.isCollidedVertically) {
                Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY + 1.273197475E-14, Minecraft.player.posZ, true));
                Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX + d * 0.0, Minecraft.player.posY, Minecraft.player.posZ + d2 * 0.0, true));
                Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ, true));
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.player.onGround = false;
    }
}

