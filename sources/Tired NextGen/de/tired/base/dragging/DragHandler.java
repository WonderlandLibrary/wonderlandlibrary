package de.tired.base.dragging;

import com.google.gson.*;
import de.tired.base.module.Module;
import de.tired.Tired;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DragHandler {
    public static HashMap<String, Draggable> draggableHashMap = new HashMap<>();

    private static File DIR = new File(Minecraft.getMinecraft().mcDataDir + "/" + Tired.INSTANCE.CLIENT_NAME, "data");

    public DragHandler() {
        if (!DIR.exists()) DIR.mkdirs();
    }

    public static Draggable setupDrag(Module module, String name, float startX, float startY, boolean resizeCompatible) {
        draggableHashMap.put(name, new Draggable(name, module, startX, startY, resizeCompatible));
        return draggableHashMap.get(name);
    }

    public static void save() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File fDir = DIR;
        if (fDir.exists()) {
            final FileWriter fileWriter;
            try {
                fileWriter = new FileWriter(DIR + "\\" + "Drag" + ".json");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            gson.toJson(saveDraggables(), fileWriter);
            try {
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void loadDraggables() {
        File f = new File(DIR + "\\" + "Drag" + ".json");
        if (f.exists()) {
            try {
                load(new JsonParser().parse(new FileReader(f)));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private static boolean load(final JsonElement jsonElement) {

        for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
            final JsonObject jsonModule = (JsonObject) entry.getValue();
            if (jsonModule.has("Data")) {
                String x = jsonModule.get("Data").getAsJsonObject().get("x").getAsString();
                String y = jsonModule.get("Data").getAsJsonObject().get("y").getAsString();
                String name = entry.getKey();
                final Draggable currentDrag = draggableHashMap.get(name);
                if (currentDrag == null) return false;
                currentDrag.setXPosition(Float.parseFloat(x));
                currentDrag.setYPosition(Float.parseFloat(y));
                currentDrag.setStartX(Float.parseFloat(x));
                currentDrag.setStartY(Float.parseFloat(y));
                currentDrag.screenPosition = ScreenPosition.fromAbsolute((int) currentDrag.getXPosition(), (int) currentDrag.getYPosition());
            }
        }

        return false;
    }

    public static JsonObject saveDraggables() {
        final JsonObject jsonObject = new JsonObject();
        DragHandler.draggableHashMap.forEach((s, draggable) -> {
            JsonObject nameJson = new JsonObject();
            nameJson.addProperty("DragName", draggable.getName());
            final JsonObject jsonObject1 = new JsonObject();
            jsonObject1.addProperty("x", draggable.getXPosition());
            jsonObject1.addProperty("y", draggable.getYPosition());

            nameJson.add("Data", jsonObject1);
            jsonObject.add(draggable.getName(), nameJson);
        });
        return jsonObject;
    }

}
