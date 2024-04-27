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
import net.ccbluex.liquidbounce.features.module.modules.movement.LadderJump;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="LongJump", description="Allows you to jump further.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000F\n\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J020HJ020HJ02\b0HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0X¢\n\u0000R\t0\nX¢\n\u0000R0\fX¢\n\u0000R\r08VX¢\bR0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/LongJump;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "autoJumpValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "canBoost", "", "canMineplexBoost", "jumped", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "ncpBoostValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "tag", "", "getTag", "()Ljava/lang/String;", "teleported", "onJump", "", "event", "Lnet/ccbluex/liquidbounce/event/JumpEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class LongJump
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"NCP", "AACv1", "AACv2", "AACv3", "Mineplex", "Mineplex2", "Mineplex3", "Redesky"}, "NCP");
    private final FloatValue ncpBoostValue = new FloatValue("NCPBoost", 4.25f, 1.0f, 10.0f);
    private final BoolValue autoJumpValue = new BoolValue("AutoJump", false);
    private boolean jumped;
    private boolean canBoost;
    private boolean teleported;
    private boolean canMineplexBoost;

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        if (LadderJump.jumped) {
            MovementUtils.strafe(MovementUtils.INSTANCE.getSpeed() * 1.08f);
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (this.jumped) {
            String mode = (String)this.modeValue.get();
            if (thePlayer.getOnGround() || thePlayer.getCapabilities().isFlying()) {
                this.jumped = false;
                this.canMineplexBoost = false;
                if (StringsKt.equals(mode, "NCP", true)) {
                    thePlayer.setMotionX(0.0);
                    thePlayer.setMotionZ(0.0);
                }
                return;
            }
            LongJump longJump = this;
            boolean bl = false;
            boolean bl2 = false;
            LongJump $this$run = longJump;
            boolean bl3 = false;
            String string = mode;
            boolean bl4 = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
            switch (string3) {
                case "ncp": {
                    MovementUtils.strafe(MovementUtils.INSTANCE.getSpeed() * ($this$run.canBoost ? ((Number)$this$run.ncpBoostValue.get()).floatValue() : 1.0f));
                    $this$run.canBoost = false;
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
                    $this$run.teleported = true;
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
                    break;
                }
                case "redesky": {
                    thePlayer.setJumpMovementFactor(0.15f);
                    IEntityPlayerSP iEntityPlayerSP5 = thePlayer;
                    iEntityPlayerSP5.setMotionY(iEntityPlayerSP5.getMotionY() + (double)0.05f);
                }
            }
        }
        if (((Boolean)this.autoJumpValue.get()).booleanValue() && thePlayer.getOnGround() && MovementUtils.isMoving()) {
            this.jumped = true;
            thePlayer.jump();
        }
    }

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        String mode = (String)this.modeValue.get();
        if (StringsKt.equals(mode, "mineplex3", true)) {
            if (thePlayer.getFallDistance() != 0.0f) {
                IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
                iEntityPlayerSP2.setMotionY(iEntityPlayerSP2.getMotionY() + 0.037);
            }
        } else if (StringsKt.equals(mode, "ncp", true) && !MovementUtils.isMoving() && this.jumped) {
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
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.jumped = true;
        this.canBoost = true;
        this.teleported = false;
        if (!this.getState()) return;
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
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
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (!iEntityPlayerSP.isCollidedHorizontally()) return;
        event.setMotion(2.31f);
        this.canMineplexBoost = true;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP2.setOnGround(false);
        return;
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }
}
