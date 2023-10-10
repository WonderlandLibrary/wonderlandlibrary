/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods;

import me.AveReborn.Client;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.ui.click.UIClick;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui
extends Mod {
    public ClickGui() {
        super("ClickGui", Category.RENDER);
        this.setKey(54);
    }

    @Override
    public void onEnable() {
        this.mc.displayGuiScreen(Client.instance.showClickGui());
        this.set(false);
    }
}

