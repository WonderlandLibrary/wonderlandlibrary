/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.tileentity.TileEntity
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0010\u0006\n\u0002\b\u0010\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010#\u001a\u00020\u00062\b\u0010$\u001a\u0004\u0018\u00010%H\u0096\u0002J0\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\u00142\u0006\u0010+\u001a\u00020\u00142\u0006\u0010,\u001a\u00020\u00142\u0006\u0010-\u001a\u00020\fH\u0016J \u0010.\u001a\u00020\u00062\u0006\u0010(\u001a\u00020/2\u0006\u00100\u001a\u00020\f2\u0006\u00101\u001a\u00020\u0006H\u0016J8\u00102\u001a\u00020\u00062\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u00020\u00142\u0006\u00106\u001a\u00020\u00142\u0006\u00107\u001a\u00020\u00142\u0006\u00108\u001a\u00020\f2\u0006\u00109\u001a\u00020\fH\u0016R$\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000eR$\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0010\u0010\u000e\"\u0004\b\u0011\u0010\u0012R\u0014\u0010\u0013\u001a\u00020\u00148VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0017\u001a\u00020\u00148VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u0016R\u0014\u0010\u0019\u001a\u00020\u00148VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001a\u0010\u0016R\u0014\u0010\u001b\u001a\u00020\u00148VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\u0016R\u0014\u0010\u001d\u001a\u00020\u00148VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001e\u0010\u0016R\u0014\u0010\u001f\u001a\u00020\u00148VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"\u00a8\u0006:"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/RenderManagerImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/renderer/entity/IRenderManager;", "wrapped", "Lnet/minecraft/client/renderer/entity/RenderManager;", "(Lnet/minecraft/client/renderer/entity/RenderManager;)V", "value", "", "isRenderShadow", "()Z", "setRenderShadow", "(Z)V", "playerViewX", "", "getPlayerViewX", "()F", "playerViewY", "getPlayerViewY", "setPlayerViewY", "(F)V", "renderPosX", "", "getRenderPosX", "()D", "renderPosY", "getRenderPosY", "renderPosZ", "getRenderPosZ", "viewerPosX", "getViewerPosX", "viewerPosY", "getViewerPosY", "viewerPosZ", "getViewerPosZ", "getWrapped", "()Lnet/minecraft/client/renderer/entity/RenderManager;", "equals", "other", "", "renderEntityAt", "", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/tileentity/ITileEntity;", "x", "y", "z", "partialTicks", "renderEntityStatic", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "renderPartialTicks", "hideDebugBox", "renderEntityWithPosYaw", "entityLivingBase", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "d", "d1", "d2", "fl", "fl1", "LiKingSense"})
public final class RenderManagerImpl
implements IRenderManager {
    @NotNull
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
    public boolean renderEntityStatic(@NotNull IEntity entity, float renderPartialTicks, boolean hideDebugBox) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)entity, (String)"entity");
        IEntity iEntity = entity;
        RenderManager renderManager = this.wrapped;
        boolean $i$f$unwrap = false;
        Object t2 = ((EntityImpl)$this$unwrap$iv).getWrapped();
        renderManager.func_188388_a(t2, renderPartialTicks, hideDebugBox);
        return true;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void renderEntityAt(@NotNull ITileEntity entity, double x, double y, double z, float partialTicks) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)entity, (String)"entity");
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
    public boolean renderEntityWithPosYaw(@NotNull IEntityLivingBase entityLivingBase, double d, double d1, double d2, float fl, float fl1) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)entityLivingBase, (String)"entityLivingBase");
        IEntityLivingBase iEntityLivingBase = entityLivingBase;
        RenderManager renderManager = this.wrapped;
        boolean $i$f$unwrap = false;
        EntityLivingBase entityLivingBase2 = (EntityLivingBase)((EntityLivingBaseImpl)$this$unwrap$iv).getWrapped();
        renderManager.func_188391_a((Entity)entityLivingBase2, d, d1, d2, fl, fl1, true);
        return true;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof RenderManagerImpl && Intrinsics.areEqual((Object)((RenderManagerImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final RenderManager getWrapped() {
        return this.wrapped;
    }

    public RenderManagerImpl(@NotNull RenderManager wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

