/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 */
package net.ccbluex.liquidbounce.features.module.modules.client;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.cape.GuiCapeManager;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

@ModuleInfo(name="CapeManager", category=ModuleCategory.RENDER, canEnable=false, description="e")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016\u00a8\u0006\u0005"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/client/CapeManager;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onEnable", "", "LiKingSense"})
public final class CapeManager
extends Module {
    @Override
    public void onEnable() {
        CapeManager.access$getMinecraft$p$s1046033730().func_147108_a((GuiScreen)GuiCapeManager.INSTANCE);
    }

    public static final /* synthetic */ Minecraft access$getMinecraft$p$s1046033730() {
        return MinecraftInstance.minecraft;
    }
}

