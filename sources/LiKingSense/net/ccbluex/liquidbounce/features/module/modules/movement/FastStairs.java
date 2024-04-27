/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="FastStairs", description="Allows you to climb up stairs faster.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/FastStairs;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "canJump", "", "longJumpValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "tag", "", "getTag", "()Ljava/lang/String;", "walkingDown", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "LiKingSense"})
public final class FastStairs
extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Step", "NCP", "AAC3.1.0", "AAC3.3.6", "AAC3.3.13"}, "NCP");
    public final BoolValue longJumpValue = new BoolValue("LongJump", false);
    public boolean canJump;
    public boolean walkingDown;

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        double motion;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (!MovementUtils.isMoving() || LiquidBounce.INSTANCE.getModuleManager().get(Speed.class).getState()) {
            return;
        }
        if (thePlayer.getFallDistance() > (float)0 && !this.walkingDown) {
            this.walkingDown = 1;
        } else if (thePlayer.getPosY() > thePlayer.getPrevChasingPosY()) {
            this.walkingDown = 0;
        }
        String mode = (String)this.modeValue.get();
        if (!thePlayer.getOnGround()) {
            return;
        }
        WBlockPos blockPos = new WBlockPos(thePlayer.getPosX(), thePlayer.getEntityBoundingBox().getMinY(), thePlayer.getPosZ());
        if (MinecraftInstance.classProvider.isBlockStairs(BlockUtils.getBlock(blockPos)) && !this.walkingDown) {
            thePlayer.setPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.5, thePlayer.getPosZ());
            motion = StringsKt.equals((String)mode, (String)"NCP", (boolean)true) ? 1.4 : (StringsKt.equals((String)mode, (String)"AAC3.1.0", (boolean)true) ? 1.5 : (StringsKt.equals((String)mode, (String)"AAC3.3.13", (boolean)true) ? 1.2 : 1.0));
            IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
            iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() * motion);
            IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
            iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() * motion);
        }
        if (MinecraftInstance.classProvider.isBlockStairs(BlockUtils.getBlock(blockPos.down()))) {
            if (this.walkingDown) {
                if (StringsKt.equals((String)mode, (String)"NCP", (boolean)true)) {
                    thePlayer.setMotionY(-1.0);
                } else if (StringsKt.equals((String)mode, (String)"AAC3.3.13", (boolean)true)) {
                    IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                    iEntityPlayerSP4.setMotionY(iEntityPlayerSP4.getMotionY() - 0.014);
                }
                return;
            }
            motion = StringsKt.equals((String)mode, (String)"NCP", (boolean)true) ? 1.3 : (StringsKt.equals((String)mode, (String)"AAC3.1.0", (boolean)true) ? 1.3 : (StringsKt.equals((String)mode, (String)"AAC3.3.6", (boolean)true) ? 1.48 : (StringsKt.equals((String)mode, (String)"AAC3.3.13", (boolean)true) ? 1.52 : 1.3)));
            IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
            iEntityPlayerSP5.setMotionX(iEntityPlayerSP5.getMotionX() * motion);
            IEntityPlayerSP iEntityPlayerSP6 = thePlayer;
            iEntityPlayerSP6.setMotionZ(iEntityPlayerSP6.getMotionZ() * motion);
            this.canJump = 1;
        } else if (StringsKt.startsWith((String)mode, (String)"AAC", (boolean)true) && this.canJump) {
            if (((Boolean)this.longJumpValue.get()).booleanValue()) {
                thePlayer.jump();
                IEntityPlayerSP iEntityPlayerSP7 = thePlayer;
                iEntityPlayerSP7.setMotionX(iEntityPlayerSP7.getMotionX() * 1.35);
                IEntityPlayerSP iEntityPlayerSP8 = thePlayer;
                iEntityPlayerSP8.setMotionZ(iEntityPlayerSP8.getMotionZ() * 1.35);
            }
            this.canJump = 0;
        }
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

