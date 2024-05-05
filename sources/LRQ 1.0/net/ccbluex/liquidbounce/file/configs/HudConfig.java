/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.FileUtils
 */
package net.ccbluex.liquidbounce.file.configs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.ui.client.hud.Config;
import org.apache.commons.io.FileUtils;

public class HudConfig
extends FileConfig {
    public HudConfig(File file) {
        super(file);
    }

    @Override
    protected void loadConfig() throws IOException {
        LiquidBounce.hud.clearElements();
        LiquidBounce.hud = new Config(FileUtils.readFileToString((File)this.getFile())).toHUD();
    }

    @Override
    protected void saveConfig() throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(new Config(LiquidBounce.hud).toJson());
        printWriter.close();
    }
}

