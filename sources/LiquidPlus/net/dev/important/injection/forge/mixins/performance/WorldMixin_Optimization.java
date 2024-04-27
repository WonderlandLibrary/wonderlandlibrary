/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.particle.EntityFX
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityFallingBlock
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityTNTPrimed
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.world.World
 */
package net.dev.important.injection.forge.mixins.performance;

import java.util.List;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value={World.class})
public class WorldMixin_Optimization {
    @Shadow
    @Final
    public boolean field_72995_K;

    @ModifyVariable(method={"updateEntityWithOptionalForce"}, at=@At(value="STORE"), ordinal=1)
    private boolean patcher$checkIfWorldIsRemoteBeforeForceUpdating(boolean isForced) {
        return isForced && !this.field_72995_K;
    }

    @Inject(method={"getCollidingBoundingBoxes"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/World;getEntitiesWithinAABBExcludingEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;")}, cancellable=true, locals=LocalCapture.CAPTURE_FAILSOFT)
    private void patcher$filterEntities(Entity entityIn, AxisAlignedBB bb, CallbackInfoReturnable<List<AxisAlignedBB>> cir, List<AxisAlignedBB> list) {
        if (entityIn instanceof EntityTNTPrimed || entityIn instanceof EntityFallingBlock || entityIn instanceof EntityItem || entityIn instanceof EntityFX) {
            cir.setReturnValue(list);
        }
    }
}

