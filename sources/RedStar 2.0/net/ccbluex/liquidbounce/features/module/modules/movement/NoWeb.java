package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="NoWeb", description="Prevents you from getting slowed down in webs.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000,\n\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J0\f2\r0HR0X¢\n\u0000R08VX¢\b\bR\t0\nX¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/NoWeb;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "tag", "", "getTag", "()Ljava/lang/String;", "usedTimer", "", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class NoWeb
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"None", "AAC", "LAAC", "Rewi", "Matrix", "Spartan", "AAC5"}, "None");
    private boolean usedTimer;

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (this.usedTimer) {
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
            this.usedTimer = false;
        }
        if (!thePlayer.isInWeb()) {
            return;
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "none": {
                thePlayer.setInWeb(false);
                break;
            }
            case "aac": {
                thePlayer.setJumpMovementFactor(0.59f);
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) break;
                thePlayer.setMotionY(0.0);
                break;
            }
            case "laac": {
                thePlayer.setJumpMovementFactor(thePlayer.getMovementInput().getMoveStrafe() != 0.0f ? 1.0f : 1.21f);
                if (!MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    thePlayer.setMotionY(0.0);
                }
                if (!thePlayer.getOnGround()) break;
                thePlayer.jump();
                break;
            }
            case "rewi": {
                thePlayer.setJumpMovementFactor(0.42f);
                if (!thePlayer.getOnGround()) break;
                thePlayer.jump();
                break;
            }
            case "spartan": {
                MovementUtils.strafe(0.27f);
                MinecraftInstance.mc.getTimer().setTimerSpeed(3.7f);
                if (!MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP2.setMotionY(0.0);
                }
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP3.getTicksExisted() % 2 == 0) {
                    MinecraftInstance.mc.getTimer().setTimerSpeed(1.7f);
                }
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP4.getTicksExisted() % 40 == 0) {
                    MinecraftInstance.mc.getTimer().setTimerSpeed(3.0f);
                }
                this.usedTimer = true;
                break;
            }
            case "matrix": {
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP5.setJumpMovementFactor(0.12425f);
                IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP6 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP6.setMotionY(-0.0125);
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                    IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP7 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP7.setMotionY(-0.1625);
                }
                IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP8 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP8.getTicksExisted() % 40 != 0) break;
                MinecraftInstance.mc.getTimer().setTimerSpeed(3.0f);
                this.usedTimer = true;
                break;
            }
            case "aac5": {
                IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP9 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP9.setJumpMovementFactor(0.42f);
                IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP10 == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP10.getOnGround()) break;
                IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP11 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP11.jump();
                break;
            }
        }
    }

    @Override
    @Nullable
    public String getTag() {
        return (String)this.modeValue.get();
    }
}
