/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import cn.hanabi.musicplayer.ui.MusicPlayerUI;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.minecraft.client.gui.GuiScreen;

@ModuleInfo(name="MusicPlayer", description="Netease music api", category=ModuleCategory.RENDER, canEnable=false)
public class MusicPlayer
extends Module {
    @Override
    public void onEnable() {
        minecraft.func_147108_a((GuiScreen)new MusicPlayerUI());
    }
}

