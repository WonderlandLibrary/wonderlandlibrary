/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonParser
 *  net.minecraft.block.Block
 */
package net.dev.important.file.configs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import net.dev.important.Client;
import net.dev.important.file.FileConfig;
import net.dev.important.file.FileManager;
import net.dev.important.modules.module.modules.render.XRay;
import net.dev.important.utils.ClientUtils;
import net.minecraft.block.Block;

public class XRayConfig
extends FileConfig {
    public XRayConfig(File file) {
        super(file);
    }

    @Override
    protected void loadConfig() throws IOException {
        XRay xRay = (XRay)Client.moduleManager.getModule(XRay.class);
        if (xRay == null) {
            ClientUtils.getLogger().error("[FileManager] Failed to find xray module.");
            return;
        }
        JsonArray jsonArray = new JsonParser().parse((Reader)new BufferedReader(new FileReader(this.getFile()))).getAsJsonArray();
        xRay.getXrayBlocks().clear();
        for (JsonElement jsonElement : jsonArray) {
            try {
                Block block = Block.func_149684_b((String)jsonElement.getAsString());
                if (xRay.getXrayBlocks().contains(block)) {
                    ClientUtils.getLogger().error("[FileManager] Skipped xray block '" + block.getRegistryName() + "' because the block is already added.");
                    continue;
                }
                xRay.getXrayBlocks().add(block);
            }
            catch (Throwable throwable) {
                ClientUtils.getLogger().error("[FileManager] Failed to add block to xray.", throwable);
            }
        }
    }

    @Override
    protected void saveConfig() throws IOException {
        XRay xRay = (XRay)Client.moduleManager.getModule(XRay.class);
        if (xRay == null) {
            ClientUtils.getLogger().error("[FileManager] Failed to find xray module.");
            return;
        }
        JsonArray jsonArray = new JsonArray();
        for (Block block : xRay.getXrayBlocks()) {
            jsonArray.add(FileManager.PRETTY_GSON.toJsonTree((Object)Block.func_149682_b((Block)block)));
        }
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson((JsonElement)jsonArray));
        printWriter.close();
    }
}

