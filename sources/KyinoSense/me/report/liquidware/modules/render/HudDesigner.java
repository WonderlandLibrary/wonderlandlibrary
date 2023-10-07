/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 */
package me.report.liquidware.modules.render;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

@ModuleInfo(name="HudDesigner", category=ModuleCategory.RENDER, canEnable=false, description="HUD Designer")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016\u00a8\u0006\u0005"}, d2={"Lme/report/liquidware/modules/render/HudDesigner;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onEnable", "", "KyinoClient"})
public final class HudDesigner
extends Module {
    @Override
    public void onEnable() {
        HudDesigner.access$getMc$p$s1046033730().func_147108_a((GuiScreen)new GuiHudDesigner());
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

