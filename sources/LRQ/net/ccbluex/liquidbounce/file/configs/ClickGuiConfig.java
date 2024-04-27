/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package net.ccbluex.liquidbounce.file.configs;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.file.FileManager;
import net.ccbluex.liquidbounce.ui.client.clickgui.Panel;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.Element;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ModuleElement;
import net.ccbluex.liquidbounce.utils.ClientUtils;

public class ClickGuiConfig
extends FileConfig {
    public ClickGuiConfig(File file) {
        super(file);
    }

    @Override
    protected void loadConfig() throws IOException {
        JsonElement jsonElement = new JsonParser().parse((Reader)new BufferedReader(new FileReader(this.getFile())));
        if (jsonElement instanceof JsonNull) {
            return;
        }
        JsonObject jsonObject = (JsonObject)jsonElement;
        for (Panel panel : LiquidBounce.clickGui.panels) {
            if (!jsonObject.has(panel.getName())) continue;
            try {
                JsonObject panelObject = jsonObject.getAsJsonObject(panel.getName());
                panel.setOpen(panelObject.get("open").getAsBoolean());
                panel.setVisible(panelObject.get("visible").getAsBoolean());
                panel.setX(panelObject.get("posX").getAsInt());
                panel.setY(panelObject.get("posY").getAsInt());
                for (Element element : panel.getElements()) {
                    ModuleElement moduleElement;
                    if (!(element instanceof ModuleElement) || !panelObject.has((moduleElement = (ModuleElement)element).getModule().getName())) continue;
                    try {
                        JsonObject elementObject = panelObject.getAsJsonObject(moduleElement.getModule().getName());
                        moduleElement.setShowSettings(elementObject.get("Settings").getAsBoolean());
                    }
                    catch (Exception e) {
                        ClientUtils.getLogger().error("Error while loading clickgui module element with the name '" + moduleElement.getModule().getName() + "' (Panel Name: " + panel.getName() + ").", (Throwable)e);
                    }
                }
            }
            catch (Exception e) {
                ClientUtils.getLogger().error("Error while loading clickgui panel with the name '" + panel.getName() + "'.", (Throwable)e);
            }
        }
    }

    @Override
    protected void saveConfig() throws IOException {
        JsonObject jsonObject = new JsonObject();
        for (Panel panel : LiquidBounce.clickGui.panels) {
            JsonObject panelObject = new JsonObject();
            panelObject.addProperty("open", Boolean.valueOf(panel.getOpen()));
            panelObject.addProperty("visible", Boolean.valueOf(panel.isVisible()));
            panelObject.addProperty("posX", (Number)panel.getX());
            panelObject.addProperty("posY", (Number)panel.getY());
            for (Element element : panel.getElements()) {
                if (!(element instanceof ModuleElement)) continue;
                ModuleElement moduleElement = (ModuleElement)element;
                JsonObject elementObject = new JsonObject();
                elementObject.addProperty("Settings", Boolean.valueOf(moduleElement.isShowSettings()));
                panelObject.add(moduleElement.getModule().getName(), (JsonElement)elementObject);
            }
            jsonObject.add(panel.getName(), (JsonElement)panelObject);
        }
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson((JsonElement)jsonObject));
        printWriter.close();
    }
}

