/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.tileentity.TileEntity
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.renderer.entity.IRenderManager;
import net.ccbluex.liquidbounce.api.minecraft.tileentity.ITileEntity;
import net.ccbluex.liquidbounce.injection.backend.EntityImpl;
import net.ccbluex.liquidbounce.injection.backend.EntityLivingBaseImpl;
import net.ccbluex.liquidbounce.injection.backend.TileEntityImpl;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.Nullable;

public final class RenderManagerImpl
implements IRenderManager {
    private final RenderManager wrapped;

    @Override
    public boolean isRenderShadow() {
        return this.wrapped.func_178627_a();
    }

    @Override
    public void setRenderShadow(boolean value) {
        this.wrapped.func_178633_a(value);
    }

    @Override
    public double getViewerPosX() {
        return this.wrapped.field_78730_l;
    }

    @Override
    public double getViewerPosY() {
        return this.wrapped.field_78731_m;
    }

    @Override
    public double getViewerPosZ() {
        return this.wrapped.field_78728_n;
    }

    @Override
    public float getPlayerViewX() {
        return this.wrapped.field_78732_j;
    }

    @Override
    public float getPlayerViewY() {
        return this.wrapped.field_78735_i;
    }

    @Override
    public void setPlayerViewY(float value) {
        this.wrapped.func_178631_a(value);
    }

    @Override
    public double getRenderPosX() {
        return this.wrapped.field_78725_b;
    }

    @Override
    public double getRenderPosY() {
        return this.wrapped.field_78726_c;
    }

    @Override
    public double getRenderPosZ() {
        return this.wrapped.field_78723_d;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean renderEntityStatic(IEntity entity, float renderPartialTicks, boolean hideDebugBox) {
        void $this$unwrap$iv;
        IEntity iEntity = entity;
        RenderManager renderManager = this.wrapped;
        boolean $i$f$unwrap = false;
        Object t = ((EntityImpl)$this$unwrap$iv).getWrapped();
        renderManager.func_188388_a(t, renderPartialTicks, hideDebugBox);
        return true;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void renderEntityAt(ITileEntity entity, double x, double y, double z, float partialTicks) {
        void $this$unwrap$iv;
        ITileEntity iTileEntity = entity;
        TileEntityRendererDispatcher tileEntityRendererDispatcher = TileEntityRendererDispatcher.field_147556_a;
        boolean $i$f$unwrap = false;
        TileEntity tileEntity = ((TileEntityImpl)$this$unwrap$iv).getWrapped();
        tileEntityRendererDispatcher.func_147549_a(tileEntity, x, y, z, partialTicks);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean renderEntityWithPosYaw(IEntityLivingBase entityLivingBase, double d, double d1, double d2, float fl, float fl1) {
        void $this$unwrap$iv;
        IEntityLivingBase iEntityLivingBase = entityLivingBase;
        RenderManager renderManager = this.wrapped;
        boolean $i$f$unwrap = false;
        EntityLivingBase entityLivingBase2 = (EntityLivingBase)((EntityLivingBaseImpl)$this$unwrap$iv).getWrapped();
        renderManager.func_188391_a((Entity)entityLivingBase2, d, d1, d2, fl, fl1, true);
        return true;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof RenderManagerImpl && ((RenderManagerImpl)other).wrapped.equals(this.wrapped);
    }

    public final RenderManager getWrapped() {
        return this.wrapped;
    }

    public RenderManagerImpl(RenderManager wrapped) {
        this.wrapped = wrapped;
    }
}

