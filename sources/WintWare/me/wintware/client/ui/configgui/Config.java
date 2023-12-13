/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.ui.configgui;

import me.wintware.client.Main;
import me.wintware.client.module.ModuleManager;
import me.wintware.client.utils.other.ChatUtils;
import net.minecraft.client.Minecraft;

public class Config {
    public final ModuleManager modManager;
    public final Minecraft mc;
    String name;

    public Config(String name) {
        this.modManager = Main.instance.moduleManager;
        this.mc = Minecraft.getMinecraft();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void loadConfig() {
        ChatUtils.addChatMessage("Config loaded: " + this.getName());
    }
}

