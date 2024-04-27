/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

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
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="LongJump", description="Allows you to jump further.", category=ModuleCategory.MOVEMENT)
public final class LongJump
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"NCP", "AACv1", "AACv2", "AACv3", "Mineplex", "Mineplex2", "Mineplex3"}, "NCP");
    private boolean teleported;
    private boolean canBoost;
    private final FloatValue ncpBoostValue = new FloatValue("NCPBoost", 4.25f, 1.0f, 10.0f);
    private boolean jumped;
    private final BoolValue autoJumpValue = new BoolValue("AutoJump", false);
    private boolean canMineplexBoost;

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (this.jumped) {
            String string = (String)this.modeValue.get();
            if (iEntityPlayerSP2.getOnGround() || iEntityPlayerSP2.getCapabilities().isFlying()) {
                this.jumped = false;
                this.canMineplexBoost = false;
                if (StringsKt.equals((String)string, (String)"NCP", (boolean)true)) {
                    iEntityPlayerSP2.setMotionX(0.0);
                    iEntityPlayerSP2.setMotionZ(0.0);
                }
                return;
            }
            LongJump longJump = this;
            boolean bl = false;
            boolean bl2 = false;
            LongJump longJump2 = longJump;
            boolean bl3 = false;
            String string2 = string;
            boolean bl4 = false;
            String string3 = string2;
            if (string3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            switch (string3.toLowerCase()) {
                case "ncp": {
                    MovementUtils.strafe(MovementUtils.INSTANCE.getSpeed() * (longJump2.canBoost ? ((Number)longJump2.ncpBoostValue.get()).floatValue() : 1.0f));
                    longJump2.canBoost = false;
                    break;
                }
                case "aacv1": {
                    IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                    iEntityPlayerSP3.setMotionY(iEntityPlayerSP3.getMotionY() + 0.05999);
                    MovementUtils.strafe(MovementUtils.INSTANCE.getSpeed() * 1.08f);
                    break;
                }
                case "mineplex3": 
                case "aacv2": {
                    iEntityPlayerSP2.setJumpMovementFactor(0.09f);
                    IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
                    iEntityPlayerSP4.setMotionY(iEntityPlayerSP4.getMotionY() + 0.01321);
                    iEntityPlayerSP2.setJumpMovementFactor(0.08f);
                    MovementUtils.strafe$default(0.0f, 1, null);
                    break;
                }
                case "aacv3": {
                    if (!(iEntityPlayerSP2.getFallDistance() > 0.5f) || longJump2.teleported) break;
                    double d = 3.0;
                    IEnumFacing iEnumFacing = iEntityPlayerSP2.getHorizontalFacing();
                    double d2 = 0.0;
                    double d3 = 0.0;
                    if (iEnumFacing.isNorth()) {
                        d3 = -d;
                    } else if (iEnumFacing.isEast()) {
                        d2 = d;
                    } else if (iEnumFacing.isSouth()) {
                        d3 = d;
                    } else if (iEnumFacing.isWest()) {
                        d2 = -d;
                    }
                    iEntityPlayerSP2.setPosition(iEntityPlayerSP2.getPosX() + d2, iEntityPlayerSP2.getPosY(), iEntityPlayerSP2.getPosZ() + d3);
                    longJump2.teleported = true;
                    break;
                }
                case "mineplex": {
                    IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP2;
                    iEntityPlayerSP5.setMotionY(iEntityPlayerSP5.getMotionY() + 0.01321);
                    iEntityPlayerSP2.setJumpMovementFactor(0.08f);
                    MovementUtils.strafe$default(0.0f, 1, null);
                    break;
                }
                case "mineplex2": {
                    if (!longJump2.canMineplexBoost) break;
                    iEntityPlayerSP2.setJumpMovementFactor(0.1f);
                    if (iEntityPlayerSP2.getFallDistance() > 1.5f) {
                        iEntityPlayerSP2.setJumpMovementFactor(0.0f);
                        iEntityPlayerSP2.setMotionY(-10.0f);
                    }
                    MovementUtils.strafe$default(0.0f, 1, null);
                }
            }
        }
        if (((Boolean)this.autoJumpValue.get()).booleanValue() && iEntityPlayerSP2.getOnGround() && MovementUtils.isMoving()) {
            this.jumped = true;
            iEntityPlayerSP2.jump();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget(ignoreCondition=true)
    public final void onJump(JumpEvent jumpEvent) {
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
        string = string2.toLowerCase();
        switch (string.hashCode()) {
            case 706904560: {
                if (!string.equals("mineplex2")) return;
                break;
            }
            case -1362669950: {
                if (!string.equals("mineplex")) return;
                jumpEvent.setMotion(jumpEvent.getMotion() * 4.08f);
                return;
            }
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (!iEntityPlayerSP.isCollidedHorizontally()) return;
        jumpEvent.setMotion(2.31f);
        this.canMineplexBoost = true;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP2.setOnGround(false);
        return;
    }

    @EventTarget
    public final void onMove(MoveEvent moveEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        String string = (String)this.modeValue.get();
        if (StringsKt.equals((String)string, (String)"mineplex3", (boolean)true)) {
            if (iEntityPlayerSP2.getFallDistance() != 0.0f) {
                IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                iEntityPlayerSP3.setMotionY(iEntityPlayerSP3.getMotionY() + 0.037);
            }
        } else if (StringsKt.equals((String)string, (String)"ncp", (boolean)true) && !MovementUtils.isMoving() && this.jumped) {
            iEntityPlayerSP2.setMotionX(0.0);
            iEntityPlayerSP2.setMotionZ(0.0);
            moveEvent.zeroXZ();
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

