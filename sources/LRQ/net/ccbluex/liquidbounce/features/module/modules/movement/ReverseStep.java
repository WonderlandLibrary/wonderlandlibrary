/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Reflection
 *  kotlin.reflect.KDeclarationContainer
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="ReverseStep", description="Allows you to step down blocks faster.", category=ModuleCategory.MOVEMENT)
public final class ReverseStep
extends Module {
    private final FloatValue motionValue = new FloatValue("Motion", 1.0f, 0.21f, 1.0f);
    private boolean jumped;

    @EventTarget(ignoreCondition=true)
    public final void onUpdate(@Nullable UpdateEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (thePlayer.getOnGround()) {
            this.jumped = false;
        }
        if (thePlayer.getMotionY() > 0.0) {
            this.jumped = true;
        }
        if (!this.getState()) {
            return;
        }
        if (BlockUtils.collideBlock(thePlayer.getEntityBoundingBox(), (Function1<? super IBlock, Boolean>)((Function1)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

            public final boolean invoke(@Nullable Object p1) {
                return ((IClassProvider)this.receiver).isBlockLiquid(p1);
            }

            public final KDeclarationContainer getOwner() {
                return Reflection.getOrCreateKotlinClass(IClassProvider.class);
            }

            public final String getName() {
                return "isBlockLiquid";
            }

            public final String getSignature() {
                return "isBlockLiquid(Ljava/lang/Object;)Z";
            }
        })) || BlockUtils.collideBlock(MinecraftInstance.classProvider.createAxisAlignedBB(thePlayer.getEntityBoundingBox().getMaxX(), thePlayer.getEntityBoundingBox().getMaxY(), thePlayer.getEntityBoundingBox().getMaxZ(), thePlayer.getEntityBoundingBox().getMinX(), thePlayer.getEntityBoundingBox().getMinY() - 0.01, thePlayer.getEntityBoundingBox().getMinZ()), (Function1<? super IBlock, Boolean>)((Function1)new Function1<Object, Boolean>(MinecraftInstance.classProvider){

            public final boolean invoke(@Nullable Object p1) {
                return ((IClassProvider)this.receiver).isBlockLiquid(p1);
            }

            public final KDeclarationContainer getOwner() {
                return Reflection.getOrCreateKotlinClass(IClassProvider.class);
            }

            public final String getName() {
                return "isBlockLiquid";
            }

            public final String getSignature() {
                return "isBlockLiquid(Ljava/lang/Object;)Z";
            }
        }))) {
            return;
        }
        if (!MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown() && !thePlayer.getOnGround() && !thePlayer.getMovementInput().getJump() && thePlayer.getMotionY() <= 0.0 && thePlayer.getFallDistance() <= 1.0f && !this.jumped) {
            thePlayer.setMotionY(-((Number)this.motionValue.get()).floatValue());
        }
    }

    @EventTarget(ignoreCondition=true)
    public final void onJump(@Nullable JumpEvent event) {
        this.jumped = true;
    }
}

