/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.client.renderer.tileentity.TileEntityMobSpawnerRenderer;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.init.Blocks;

public class RenderMinecartMobSpawner
extends RenderMinecart<EntityMinecartMobSpawner> {
    public RenderMinecartMobSpawner(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected void func_180560_a(EntityMinecartMobSpawner entityMinecartMobSpawner, float f, IBlockState iBlockState) {
        super.func_180560_a(entityMinecartMobSpawner, f, iBlockState);
        if (iBlockState.getBlock() == Blocks.mob_spawner) {
            TileEntityMobSpawnerRenderer.renderMob(entityMinecartMobSpawner.func_98039_d(), entityMinecartMobSpawner.posX, entityMinecartMobSpawner.posY, entityMinecartMobSpawner.posZ, f);
        }
    }
}

