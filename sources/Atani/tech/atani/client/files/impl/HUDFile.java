package tech.atani.client.files.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import tech.atani.client.feature.theme.ThemeObject;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.data.enums.ScreenType;
import tech.atani.client.feature.theme.storage.ThemeStorage;
import tech.atani.client.files.LocalFile;
import tech.atani.client.files.data.FileData;

import java.util.Map;

@FileData(fileName = "hud")
public class HUDFile extends LocalFile {

    @Override
    public void save(Gson gson) {
        JsonObject object = new JsonObject();

        JsonObject themesObject = new JsonObject();

        JsonObject elementsObject = new JsonObject();

        for(ElementType elementType : ElementType.values()) {
            JsonObject elementObject = new JsonObject();
            for(ThemeObject themeObject : ThemeStorage.getInstance().getThemeObjects(elementType)) {
                elementObject.add(themeObject.getName(), themeObject.save());
            }
            elementsObject.add(elementType.getName(), elementObject);
        }

        JsonObject screensObject = new JsonObject();

        for(ScreenType screenType : ScreenType.values()) {
            JsonObject screenObject = new JsonObject();
            for(ThemeObject themeObject : ThemeStorage.getInstance().getThemeObjects(screenType)) {
                screenObject.add(themeObject.getName(), themeObject.save());
            }
            elementsObject.add(screenType.getName(), screenObject);
        }

        themesObject.add("Elements", elementsObject);
        themesObject.add("Screens", screensObject);

        object.add("HUD", themesObject);

        writeFile(gson.toJson(object), file);
    }

    // This is honestly a bit of mess
    @Override
    public void load(Gson gson) {
        if (!file.exists()) {
            return;
        }

        JsonObject jsonObject = gson.fromJson(readFile(file), JsonObject.class);
        JsonObject themesObject = jsonObject.getAsJsonObject("HUD");

        if (themesObject != null) {
            JsonObject elementsObject = themesObject.getAsJsonObject("Elements");
            if (elementsObject != null) {
                for (ElementType elementType : ElementType.values()) {
                    JsonObject elementObject = elementsObject.getAsJsonObject(elementType.getName());
                    if (elementObject != null) {
                        for (Map.Entry<String, JsonElement> entry : elementObject.entrySet()) {
                            String themeObjectName = entry.getKey();
                            JsonElement themeObjectJson = entry.getValue();
                            ThemeObject themeObject = ThemeStorage.getInstance().getThemeObject(themeObjectName, elementType);
                            if (themeObject != null) {
                                themeObject.load(themeObjectJson.getAsJsonObject());
                            }
                        }
                    }
                }
            }

            JsonObject screensObject = themesObject.getAsJsonObject("Screens");
            if (screensObject != null) {
                for (ScreenType screenType : ScreenType.values()) {
                    JsonObject screenObject = screensObject.getAsJsonObject(screenType.getName());
                    if (screenObject != null) {
                        for (Map.Entry<String, JsonElement> entry : screenObject.entrySet()) {
                            String themeObjectName = entry.getKey();
                            JsonElement themeObjectJson = entry.getValue();
                            ThemeObject themeObject = ThemeStorage.getInstance().getThemeObject(themeObjectName, screenType);
                            if (themeObject != null) {
                                themeObject.load(themeObjectJson.getAsJsonObject());
                            }
                        }
                    }
                }
            }
        }
    }

}