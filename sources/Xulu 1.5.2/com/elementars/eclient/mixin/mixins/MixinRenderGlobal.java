// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import com.elementars.eclient.util.Wrapper;
import net.minecraft.client.renderer.RenderHelper;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.entity.EntityLivingBase;
import com.elementars.eclient.util.OutlineUtils2;
import com.elementars.eclient.module.render.StorageESP;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.elementars.eclient.module.Module;
import com.elementars.eclient.module.misc.AntiSound;
import com.elementars.eclient.Xulu;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { RenderGlobal.class }, priority = 9999)
public class MixinRenderGlobal
{
    @Shadow
    public ShaderGroup entityOutlineShader;
    @Shadow
    public boolean entityOutlinesRendered;
    @Shadow
    public WorldClient world;
    
    @Redirect(method = { "broadcastSound" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;playSound(DDDLnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FFZ)V"))
    private void playWitherSpawn(final WorldClient worldClient, final double x, final double y, final double z, final SoundEvent soundIn, final SoundCategory category, final float volume, final float pitch, final boolean distanceDelay) {
        if (!Xulu.MODULE_MANAGER.getModule(AntiSound.class).isToggled() || !Xulu.MODULE_MANAGER.getModuleT(AntiSound.class).witherSpawn.getValue()) {
            this.world.playSound(x, y, z, soundIn, category, volume, pitch, distanceDelay);
        }
    }
    
    @Redirect(method = { "playEvent" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;playSound(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FFZ)V", ordinal = 22))
    private void playWitherShoot(final WorldClient worldClient, final BlockPos pos, final SoundEvent soundIn, final SoundCategory category, final float volume, final float pitch, final boolean distanceDelay) {
        if (!Xulu.MODULE_MANAGER.getModule(AntiSound.class).isToggled() || !Xulu.MODULE_MANAGER.getModuleT(AntiSound.class).wither.getValue()) {
            this.world.playSound(pos, soundIn, category, volume, pitch, distanceDelay);
        }
    }
    
    @Inject(method = { "renderEntities" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;preRenderDamagedBlocks()V", shift = At.Shift.BEFORE) })
    public void renderEntities(final Entity entity, final ICamera camera, final float n, final CallbackInfo callbackInfo) {
        if (Xulu.MODULE_MANAGER.getModuleByName("StorageESP") != null && Xulu.MODULE_MANAGER.getModuleByName("StorageESP").isToggled() && Xulu.VALUE_MANAGER.getValueByMod(Xulu.MODULE_MANAGER.getModuleByName("StorageESP"), "Mode").getValue().equalsIgnoreCase("Shader")) {
            final StorageESP storageESP = (StorageESP)Xulu.MODULE_MANAGER.getModuleByName("StorageESP");
            StorageESP.renderNormal(n);
            OutlineUtils2.VZWQ(Xulu.VALUE_MANAGER.getValueByMod(storageESP, "Line Width").getValue());
            StorageESP.renderNormal(n);
            OutlineUtils2.JLYv();
            StorageESP.renderColor(n);
            OutlineUtils2.feKn();
            OutlineUtils2.mptE(null);
            StorageESP.renderColor(n);
            OutlineUtils2.VdOT();
        }
    }
    
    public void renderNormal(final float n) {
        RenderHelper.enableStandardItemLighting();
        for (final Entity e : Wrapper.getMinecraft().world.loadedEntityList) {
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            Wrapper.getMinecraft().renderGlobal.renderManager.renderEntity(e, e.posX - Wrapper.getMinecraft().renderManager.renderPosX, e.posY - Wrapper.getMinecraft().renderManager.renderPosY, e.posZ - Wrapper.getMinecraft().renderManager.renderPosZ, e.rotationYaw, n, false);
            GL11.glPopMatrix();
        }
    }
}
