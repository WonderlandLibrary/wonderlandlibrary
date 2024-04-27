package me.aquavit.liquidsense.ui.client.clickgui.neverlose.hud;

import com.google.gson.*;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.file.FileManager;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.value.Value;

public class Config {

    private JsonArray jsonArray;

    public Config() {
        this.jsonArray = new JsonArray();
        for (final Module module :  LiquidSense.moduleManager.getModules()) {
            if (module.getCategory() == ModuleCategory.HUD) {
                final JsonObject elementObject = new JsonObject();
                elementObject.addProperty("Type", module.getName());

                if (!FileManager.loadDefault) {
                    elementObject.addProperty("X", module.getRenderx());
                    elementObject.addProperty("Y", module.getRendery());
                } else {
                    elementObject.addProperty("X", module.getDefaultx());
                    elementObject.addProperty("Y", module.getDefaulty());
                    module.setRenderx(module.getDefaultx());
                    module.setRendery(module.getDefaulty());
                }


                for (Value<?> value : module.getValues())
                    elementObject.add(value.getName(), value.toJson());

                jsonArray.add(elementObject);
            }
        }
    }

    public String toJson() {
        if (jsonArray == null) {
            return "";
        }
        return new GsonBuilder().setPrettyPrinting().create().toJson(this.jsonArray);
    }
}
