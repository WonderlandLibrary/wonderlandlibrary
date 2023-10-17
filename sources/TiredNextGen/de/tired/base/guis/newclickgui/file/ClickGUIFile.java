package de.tired.base.guis.newclickgui.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import de.tired.base.guis.newclickgui.renderables.RenderableCategory;
import de.tired.base.interfaces.IHook;
import de.tired.base.module.ModuleCategory;

import java.io.*;

public class ClickGUIFile {

    public static void save(RenderableCategory category, int posX, int posY) {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File fDir = new File(IHook.MC.mcDataDir, "Tired-NextGen");
        File f = new File(fDir, fDir + "ClickGuiData.json");
        final FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gson.toJson(saveJson(category, posX, posY), fileWriter);
        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonObject saveJson(RenderableCategory category, int posX, int posY) {
        final JsonObject jsonObject = new JsonObject();

        for (ModuleCategory category1 : ModuleCategory.values()) {
            final JsonObject emailPassJson = new JsonObject();
            emailPassJson.addProperty("PosX", posX);
            emailPassJson.addProperty("PosY", posY);
            jsonObject.add(category.category.displayName, emailPassJson);
        }

        return jsonObject;
    }

}
