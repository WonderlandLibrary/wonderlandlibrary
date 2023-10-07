/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.tileentity.TileEntityMobSpawnerRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityPainting
 *  net.minecraft.tileentity.MobSpawnerBaseLogic
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
    private static void injectPaintingSpawnerFix(MobSpawnerBaseLogic mobSpawnerLogic, double posX, double posY, double posZ, float partialTicks, CallbackInfo ci) {
        Entity entity = mobSpawnerLogic.func_180612_a(mobSpawnerLogic.func_98271_a());
        if (entity == null || entity instanceof EntityPainting) {
            ci.cancel();
        }
    }
}

