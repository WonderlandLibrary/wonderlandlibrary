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
    public double getViewerPosX() {
        return this.wrapped.field_78730_l;
    }

    @Override
    public void setPlayerViewY(float f) {
        this.wrapped.func_178631_a(f);
    }

    @Override
    public float getPlayerViewX() {
        return this.wrapped.field_78732_j;
    }

    public final RenderManager getWrapped() {
        return this.wrapped;
    }

    @Override
    public void renderEntityAt(ITileEntity iTileEntity, double d, double d2, double d3, float f) {
        ITileEntity iTileEntity2 = iTileEntity;
        TileEntityRendererDispatcher tileEntityRendererDispatcher = TileEntityRendererDispatcher.field_147556_a;
        boolean bl = false;
        TileEntity tileEntity = ((TileEntityImpl)iTileEntity2).getWrapped();
        tileEntityRendererDispatcher.func_147549_a(tileEntity, d, d2, d3, f);
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof RenderManagerImpl && ((RenderManagerImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public boolean renderEntityWithPosYaw(IEntityLivingBase iEntityLivingBase, double d, double d2, double d3, float f, float f2) {
        IEntityLivingBase iEntityLivingBase2 = iEntityLivingBase;
        RenderManager renderManager = this.wrapped;
        boolean bl = false;
        EntityLivingBase entityLivingBase = (EntityLivingBase)((EntityLivingBaseImpl)iEntityLivingBase2).getWrapped();
        renderManager.func_188391_a((Entity)entityLivingBase, d, d2, d3, f, f2, true);
        return true;
    }

    @Override
    public double getRenderPosY() {
        return this.wrapped.field_78726_c;
    }

    @Override
    public double getViewerPosY() {
        return this.wrapped.field_78731_m;
    }

    @Override
    public void setRenderShadow(boolean bl) {
        this.wrapped.func_178633_a(bl);
    }

    @Override
    public double getRenderPosZ() {
        return this.wrapped.field_78723_d;
    }

    @Override
    public double getViewerPosZ() {
        return this.wrapped.field_78728_n;
    }

    @Override
    public double getRenderPosX() {
        return this.wrapped.field_78725_b;
    }

    @Override
    public boolean renderEntityStatic(IEntity iEntity, float f, boolean bl) {
        IEntity iEntity2 = iEntity;
        RenderManager renderManager = this.wrapped;
        boolean bl2 = false;
        Entity entity = ((EntityImpl)iEntity2).getWrapped();
        renderManager.func_188388_a(entity, f, bl);
        return true;
    }

    public RenderManagerImpl(RenderManager renderManager) {
        this.wrapped = renderManager;
    }

    @Override
    public float getPlayerViewY() {
        return this.wrapped.field_78735_i;
    }
}

