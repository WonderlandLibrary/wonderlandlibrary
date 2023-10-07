/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.render;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="FPSHurtCam", description=":/", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0007R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\r"}, d2={"Lme/report/liquidware/modules/render/FPSHurtCam;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "alpha", "", "getAlpha", "()I", "setAlpha", "(I)V", "onRender2D", "", "event", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "KyinoClient"})
public final class FPSHurtCam
extends Module {
    private int alpha;

    public final int getAlpha() {
        return this.alpha;
    }

    public final void setAlpha(int n) {
        this.alpha = n;
    }

    @EventTarget
    public final void onRender2D(@NotNull Render2DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        ScaledResolution scaledResolution = new ScaledResolution(FPSHurtCam.access$getMc$p$s1046033730());
        double width = scaledResolution.func_78327_c();
        double height = scaledResolution.func_78324_d();
        if (FPSHurtCam.access$getMc$p$s1046033730().field_71439_g.field_70737_aN > 0) {
            if (this.alpha < 100) {
                this.alpha += 5;
            }
        } else if (this.alpha > 0) {
            this.alpha -= 5;
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

