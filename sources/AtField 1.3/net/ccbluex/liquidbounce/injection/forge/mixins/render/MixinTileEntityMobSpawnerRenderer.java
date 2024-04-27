/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.tileentity.TileEntityMobSpawnerRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityPainting
 *  net.minecraft.tileentity.MobSpawnerBaseLogic
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import net.minecraft.client.renderer.tileentity.TileEntityMobSpawnerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={TileEntityMobSpawnerRenderer.class})
public class MixinTileEntityMobSpawnerRenderer {
    @Inject(method={"renderMob"}, cancellable=true, at={@At(value="HEAD")})
    private static void injectPaintingSpawnerFix(MobSpawnerBaseLogic mobSpawnerBaseLogic, double d, double d2, double d3, float f, CallbackInfo callbackInfo) {
        Entity entity = mobSpawnerBaseLogic.func_184994_d();
        if (entity == null || entity instanceof EntityPainting) {
            callbackInfo.cancel();
        }
    }
}

