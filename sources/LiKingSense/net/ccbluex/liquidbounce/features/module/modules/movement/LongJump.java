/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="LongJump", description="Allows you to jump further.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0007J\u0010\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0017H\u0007J\u0012\u0010\u0018\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0019H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u000e8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/LongJump;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "autoJumpValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "canBoost", "", "canMineplexBoost", "jumped", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "ncpBoostValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "tag", "", "getTag", "()Ljava/lang/String;", "teleported", "onJump", "", "event", "Lnet/ccbluex/liquidbounce/event/JumpEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "LiKingSense"})
public final class LongJump
extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[]{"NCP", "AACv1", "AACv2", "AACv3", "Mineplex", "Mineplex2", "Mineplex3"}, "NCP");
    public final FloatValue ncpBoostValue = new FloatValue("NCPBoost", 4.25f, 1.0f, 10.0f);
    public final BoolValue autoJumpValue = new BoolValue("AutoJump", false);
    public boolean jumped;
    public boolean canBoost;
    public boolean teleported;
    public boolean canMineplexBoost;

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (this.jumped) {
            String string;
            LongJump longJump;
            String mode = (String)this.modeValue.get();
            if (thePlayer.getOnGround() || thePlayer.getCapabilities().isFlying()) {
                this.jumped = 0;
                this.canMineplexBoost = 0;
                if (StringsKt.equals((String)mode, (String)"NCP", (boolean)true)) {
                    thePlayer.setMotionX(0.0);
                    thePlayer.setMotionZ(0.0);
                }
                return;
            }
            LongJump $this$run = longJump = this;
            String string2 = string = mode;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
            switch (string3) {
                case "ncp": {
                    MovementUtils.strafe(MovementUtils.INSTANCE.getSpeed() * ($this$run.canBoost ? ((Number)$this$run.ncpBoostValue.get()).floatValue() : 1.0f));
                    $this$run.canBoost = 0;
                    break;
                }
                case "aacv1": {
                    IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                    iEntityPlayerSP2.setMotionY(iEntityPlayerSP2.getMotionY() + 0.05999);
                    MovementUtils.strafe(MovementUtils.INSTANCE.getSpeed() * 1.08f);
                    break;
                }
                case "mineplex3": 
                case "aacv2": {
                    thePlayer.setJumpMovementFactor(0.09f);
                    IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
                    iEntityPlayerSP3.setMotionY(iEntityPlayerSP3.getMotionY() + 0.01321);
                    thePlayer.setJumpMovementFactor(0.08f);
                    MovementUtils.strafe$default(0.0f, 1, null);
                    break;
                }
                case "aacv3": {
                    if (!(thePlayer.getFallDistance() > 0.5f) || $this$run.teleported) break;
                    double value = 3.0;
                    IEnumFacing horizontalFacing = thePlayer.getHorizontalFacing();
                    double x = 0.0;
                    double z = 0.0;
                    if (horizontalFacing.isNorth()) {
                        z = -value;
                    } else if (horizontalFacing.isEast()) {
                        x = value;
                    } else if (horizontalFacing.isSouth()) {
                        z = value;
                    } else if (horizontalFacing.isWest()) {
                        x = -value;
                    }
                    thePlayer.setPosition(thePlayer.getPosX() + x, thePlayer.getPosY(), thePlayer.getPosZ() + z);
                    $this$run.teleported = 1;
                    break;
                }
                case "mineplex": {
                    IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                    iEntityPlayerSP4.setMotionY(iEntityPlayerSP4.getMotionY() + 0.01321);
                    thePlayer.setJumpMovementFactor(0.08f);
                    MovementUtils.strafe$default(0.0f, 1, null);
                    break;
                }
                case "mineplex2": {
                    if (!$this$run.canMineplexBoost) break;
                    thePlayer.setJumpMovementFactor(0.1f);
                    if (thePlayer.getFallDistance() > 1.5f) {
                        thePlayer.setJumpMovementFactor(0.0f);
                        thePlayer.setMotionY(-10.0f);
                    }
                    MovementUtils.strafe$default(0.0f, 1, null);
                }
            }
        }
        if (((Boolean)this.autoJumpValue.get()).booleanValue() && thePlayer.getOnGround() && MovementUtils.isMoving()) {
            this.jumped = 1;
            thePlayer.jump();
        }
    }

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        String mode = (String)this.modeValue.get();
        if (StringsKt.equals((String)mode, (String)"mineplex3", (boolean)true)) {
            if (thePlayer.getFallDistance() != 0.0f) {
                IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                iEntityPlayerSP2.setMotionY(iEntityPlayerSP2.getMotionY() + 0.037);
            }
        } else if (StringsKt.equals((String)mode, (String)"ncp", (boolean)true) && !MovementUtils.isMoving() && this.jumped) {
            thePlayer.setMotionX(0.0);
            thePlayer.setMotionZ(0.0);
            event.zeroXZ();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget(ignoreCondition=true)
    public final void onJump(@NotNull JumpEvent event) {
        String string;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        this.jumped = 1;
        this.canBoost = 1;
        this.teleported = 0;
        if (!this.getState()) return;
        String string2 = string = (String)this.modeValue.get();
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        string = string3;
        switch (string.hashCode()) {
            case 706904560: {
                if (!string.equals("mineplex2")) return;
                break;
            }
            case -1362669950: {
                if (!string.equals("mineplex")) return;
                event.setMotion(event.getMotion() * 4.08f);
                return;
            }
        }
        if (!MinecraftInstance.mc.getThePlayer().isCollidedHorizontally()) return;
        event.setMotion(2.31f);
        this.canMineplexBoost = 1;
        MinecraftInstance.mc.getThePlayer().setOnGround(false);
        return;
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

