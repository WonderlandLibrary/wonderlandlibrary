/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.world;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdateLiving;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public class FreeCam
extends Module {
    private float yaw;
    private float pitch;
    private float yawHead;
    private float gamma;
    private EntityOtherPlayerMP other;
    private float old;
    private EntityOtherPlayerMP fakePlayer = null;
    private double oldX;
    private double oldY;
    private double oldZ;
    public Setting speed = new Setting("Speed", this, 0.1, 0.01, 10.0, false);

    public FreeCam() {
        super("FreeCam", Category.World);
        Main.instance.setmgr.rSetting(this.speed);
    }

    @Override
    public void onDisable() {
        Minecraft.player.capabilities.isFlying = false;
        Minecraft.player.capabilities.setFlySpeed(this.old);
        Minecraft.player.rotationPitch = this.pitch;
        Minecraft.player.rotationYaw = this.yaw;
        FreeCam.mc.world.removeEntityFromWorld(-1);
        Minecraft.player.noClip = false;
        FreeCam.mc.renderGlobal.loadRenderers();
        Minecraft.getMinecraft();
        Minecraft.player.noClip = false;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.player.setPositionAndRotation(this.oldX, this.oldY, this.oldZ, Minecraft.player.rotationYaw, Minecraft.player.rotationPitch);
        Minecraft.getMinecraft().world.removeEntityFromWorld(-69);
        this.fakePlayer = null;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        Minecraft.getMinecraft();
        this.oldX = Minecraft.player.posX;
        Minecraft.getMinecraft();
        this.oldY = Minecraft.player.posY;
        Minecraft.getMinecraft();
        this.oldZ = Minecraft.player.posZ;
        Minecraft.getMinecraft();
        Minecraft.player.noClip = true;
        Minecraft.getMinecraft();
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(Minecraft.getMinecraft().world, Minecraft.player.getGameProfile());
        Minecraft.getMinecraft();
        fakePlayer.copyLocationAndAnglesFrom(Minecraft.player);
        fakePlayer.posY -= 0.0;
        Minecraft.getMinecraft();
        fakePlayer.rotationYawHead = Minecraft.player.rotationYawHead;
        Minecraft.getMinecraft().world.addEntityToWorld(-69, fakePlayer);
        super.onEnable();
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventTarget
    public void g(EventUpdateLiving e) {
        Minecraft.player.noClip = true;
        Minecraft.player.onGround = false;
        Minecraft.player.capabilities.setFlySpeed((float)this.speed.getValDouble());
        Minecraft.player.capabilities.isFlying = true;
        if (!Minecraft.player.isInsideOfMaterial(Material.AIR)) {
            if (!Minecraft.player.isInsideOfMaterial(Material.LAVA)) {
                if (!Minecraft.player.isInsideOfMaterial(Material.WATER)) {
                    if (!(FreeCam.mc.gameSettings.gammaSetting < 100.0f)) return;
                    FreeCam.mc.gameSettings.gammaSetting += 0.08f;
                    return;
                }
            }
        }
        FreeCam.mc.gameSettings.gammaSetting = this.gamma;
    }
}

