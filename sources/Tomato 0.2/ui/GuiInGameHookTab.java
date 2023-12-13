/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.ui;

import cow.milkgod.cheese.ui.GuiIngameHook;
import cow.milkgod.cheese.utils.Wrapper;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;

public class GuiInGameHookTab
extends GuiIngame {
    public GuiInGameHookTab(Minecraft mcIn) {
        super(mcIn);
    }

    @Override
    public void func_175180_a(float p_175180_1_) {
        super.func_175180_a(p_175180_1_);
        if (!Wrapper.mc.gameSettings.showDebugInfo && Objects.nonNull(Wrapper.mc.thePlayer) && Objects.nonNull(Wrapper.mc.theWorld)) {
            GuiIngameHook.drawTabGui();
        }
    }
}

