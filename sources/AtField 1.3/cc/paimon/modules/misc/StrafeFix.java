/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.util.math.MathHelper
 */
package cc.paimon.modules.misc;

import cc.paimon.utils.MathUtils2;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.util.math.MathHelper;

@ModuleInfo(name="StrafeFix", category=ModuleCategory.MOVEMENT, description="CaoNiMa")
public final class StrafeFix
extends Module {
    private boolean doFix;
    private boolean silentFix;
    private final BoolValue silentFixVaule = new BoolValue("Silent", true);
    private boolean isOverwrited;

    public final void applyForceStrafe(boolean bl, boolean bl2) {
        this.silentFix = bl;
        this.doFix = bl2;
        this.isOverwrited = true;
    }

    public final boolean getSilentFix() {
        return this.silentFix;
    }

    public final boolean getDoFix() {
        return this.doFix;
    }

    public final void updateOverwrite() {
        this.isOverwrited = false;
        this.doFix = this.getState();
        this.silentFix = (Boolean)this.silentFixVaule.get();
    }

    @Override
    public void onDisable() {
        this.doFix = false;
    }

    public final void setOverwrited(boolean bl) {
        this.isOverwrited = bl;
    }

    public final BoolValue getSilentFixVaule() {
        return this.silentFixVaule;
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        if (!this.isOverwrited) {
            this.silentFix = (Boolean)this.silentFixVaule.get();
            this.doFix = true;
        }
    }

    public final boolean isOverwrited() {
        return this.isOverwrited;
    }

    public final void runStrafeFixLoop(boolean bl, StrafeEvent strafeEvent) {
        if (strafeEvent.isCancelled()) {
            return;
        }
        Rotation rotation = RotationUtils.targetRotation;
        if (rotation == null) {
            return;
        }
        Rotation rotation2 = rotation;
        float f = rotation2.component1();
        float f2 = strafeEvent.getStrafe();
        float f3 = strafeEvent.getForward();
        float f4 = strafeEvent.getFriction();
        float f5 = f2 * f2 + f3 * f3;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        int n = (int)(((double)MathUtils2.wrapAngleTo180_float(iEntityPlayerSP.getRotationYaw() - f - 22.5f - 135.0f) + 180.0) / 45.0);
        float f6 = bl ? f + 45.0f * (float)n : f;
        float f7 = Math.max(Math.abs(f2), Math.abs(f3));
        f7 *= f7;
        float f8 = MathHelper.func_76129_c((float)(f7 / Math.min(1.0f, f7 * 2.0f)));
        if (bl) {
            switch (n) {
                case 1: 
                case 3: 
                case 5: 
                case 7: 
                case 9: {
                    if (!(!((double)Math.abs(f3) > 0.005) && !((double)Math.abs(f2) > 0.005) || (double)Math.abs(f3) > 0.005 && (double)Math.abs(f2) > 0.005)) {
                        f4 /= f8;
                        break;
                    }
                    if (!((double)Math.abs(f3) > 0.005) || !((double)Math.abs(f2) > 0.005)) break;
                    f4 *= f8;
                    break;
                }
            }
        }
        if (f5 >= 1.0E-4f) {
            if ((f5 = MathHelper.func_76129_c((float)f5)) < 1.0f) {
                f5 = 1.0f;
            }
            f5 = f4 / f5;
            f2 *= f5;
            f3 *= f5;
            float f9 = MathHelper.func_76126_a((float)((float)((double)f6 * Math.PI / (double)180.0f)));
            float f10 = MathHelper.func_76134_b((float)((float)((double)f6 * Math.PI / (double)180.0f)));
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() + (double)(f2 * f10 - f3 * f9));
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() + (double)(f3 * f10 + f2 * f9));
        }
        strafeEvent.cancelEvent();
    }

    public final void setDoFix(boolean bl) {
        this.doFix = bl;
    }

    public final void setSilentFix(boolean bl) {
        this.silentFix = bl;
    }
}

