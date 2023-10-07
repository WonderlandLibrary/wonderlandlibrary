/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.ScaledResolution
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.render;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.report.liquidware.modules.render.HudColors;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUti;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Health", category=ModuleCategory.RENDER, description="Health", array=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007J\b\u0010\u000b\u001a\u00020\bH\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\f"}, d2={"Lme/report/liquidware/modules/render/Health;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "waterMark", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getWaterMark", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "onRender2D", "", "event", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "renderWatermark", "KyinoClient"})
public final class Health
extends Module {
    @NotNull
    private final BoolValue waterMark = new BoolValue("Watermark", true);

    @NotNull
    public final BoolValue getWaterMark() {
        return this.waterMark;
    }

    @EventTarget
    public final void onRender2D(@NotNull Render2DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.waterMark.get()).booleanValue()) {
            this.renderWatermark();
        }
        ScaledResolution scaledResolution = new ScaledResolution(Health.access$getMc$p$s1046033730());
        double width = scaledResolution.func_78327_c();
        double height = scaledResolution.func_78324_d();
        EntityPlayerSP entityPlayerSP = Health.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        float Playerhealth = entityPlayerSP.func_110143_aJ();
        if (Playerhealth < (float)10) {
            Health.access$getMc$p$s1046033730().field_71466_p.func_175063_a(String.valueOf((int)Playerhealth), (float)(width - width / (double)2 - (double)5), (float)(height - height / (double)2 - (double)14), 0xFF0000);
        } else {
            Health.access$getMc$p$s1046033730().field_71466_p.func_175063_a(String.valueOf((int)Playerhealth), (float)(width - width / (double)2 - (double)5), (float)(height - height / (double)2 - (double)14), 52480);
        }
    }

    private final void renderWatermark() {
        int width = 3;
        HudColors hud = (HudColors)LiquidBounce.INSTANCE.getModuleManager().getModule(HudColors.class);
        if (hud != null) {
            Health.access$getMc$p$s1046033730().field_71466_p.func_175063_a("Kyino", 3.0f, 3.0f, ColorUti.rainbow(36000000000L, 255, 0.5f).getRGB());
        }
        Health.access$getMc$p$s1046033730().field_71466_p.func_175063_a("Sense", (float)(width += Health.access$getMc$p$s1046033730().field_71466_p.func_78256_a("Kyino")), 3.0f, -1);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

