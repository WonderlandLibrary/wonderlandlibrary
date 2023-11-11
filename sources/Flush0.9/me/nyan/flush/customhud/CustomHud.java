package me.nyan.flush.customhud;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import me.nyan.flush.Flush;
import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.customhud.component.impl.*;
import me.nyan.flush.event.EventManager;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event2D;
import me.nyan.flush.event.impl.EventKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomHud {
    private final File file = new File(Flush.getClientPath(), "customhud.json");
    private final ArrayList<Component> components = new ArrayList<>();
    private final Map<String, Class<? extends Component>> map = new HashMap<>();

    public void start() {
        EventManager.register(this);

        map.put("Text", Text.class);
        map.put("Image", Image.class);
        map.put("Shape", Shape.class);
        map.put("ArrayList", ComponentArrayList.class);
        map.put("KeyStroke", KeyStroke.class);
        map.put("KeyStrokes", KeyStrokes.class);
        map.put("Armor", Armor.class);
        map.put("Graph", Graph.class);
        map.put("MiniPlayer", MiniPlayer.class);
        map.put("Scoreboard", Scoreboard.class);
        map.put("Minimap", Minimap.class);
        map.put("Notifications", Notifications.class);
        map.put("TabGui", TabGui.class);
        map.put("TargetHUD", ComponentTargetHUD.class);
        map.put("Watermark", Watermark.class);

        Scoreboard scoreboard = new Scoreboard();
        scoreboard.setX(1);
        scoreboard.setY(0.5);
        components.add(scoreboard);

        ComponentArrayList arrayList = new ComponentArrayList();
        arrayList.setX(1);
        arrayList.setY(0);
        components.add(arrayList);

        Text watermark = new Text();
        watermark.setX(0.008);
        watermark.setY(0.012);
        components.add(watermark);

        Text fps = new Text();
        fps.text.setValue("&9FPS:&F $fps");
        fps.setX(0.008);
        fps.setY(0.988);
        components.add(fps);

        Armor armor = new Armor();
        armor.setX(0);
        armor.setY(0.5);
        components.add(armor);

        Notifications notifications = new Notifications();
        notifications.setX(0.5);
        notifications.setY(0.015);
        components.add(notifications);

        ComponentTargetHUD targetHUD = new ComponentTargetHUD();
        targetHUD.setX(0.5);
        targetHUD.setY(0.66);
        components.add(targetHUD);

        load();
    }

    @SubscribeEvent
    public void onRender2D(Event2D e) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiConfigureHud) {
            return;
        }
        for (Component component : components) {
            GlStateManager.pushMatrix();
            if (component.getResizeType() != Component.ResizeType.NONE) {
                float scaleX = component.getScaleX();
                float scaleY = component.getResizeType() != Component.ResizeType.CUSTOM ? component.getScaleX() : component.getScaleY();
                GlStateManager.scale(scaleX, scaleY, 1);
                component.draw(component.getScaledX() / scaleX, component.getScaledY() / scaleY);
            } else {
                component.draw(component.getScaledX(), component.getScaledY());
            }
            GlStateManager.popMatrix();
        }
    }

    @SubscribeEvent
    public void onKey(EventKey e) {
        for (Component component : components) {
            component.onKey(e.getKey());
        }
    }

    public void load() {
        if (!file.exists()) {
            return;
        }

        components.clear();

        try {
            JsonElement json = new JsonParser().parse(new BufferedReader(new FileReader(file)));
            if (json.isJsonArray()) {
                JsonArray array = json.getAsJsonArray();
                for (JsonElement element : array) {
                    if (!element.isJsonObject()) {
                        continue;
                    }
                    JsonObject object = element.getAsJsonObject();
                    if (object.has("id")) {
                        JsonElement id = object.get("id");
                        if (id.isJsonPrimitive()) {
                            JsonPrimitive primitive = id.getAsJsonPrimitive();
                            if (primitive.isString()) {
                                Class<? extends Component> clazz = map.get(primitive.getAsString());
                                if (clazz != null) {
                                    Component component = clazz.newInstance();
                                    component.load(object);

                                    if (object.has("scale_x")) {
                                        JsonElement scale_x = object.get("scale_x");
                                        if (scale_x.isJsonPrimitive() && scale_x.getAsJsonPrimitive().isNumber()) {
                                            component.setScaleX(scale_x.getAsNumber().floatValue());
                                        }
                                    }
                                    if (object.has("scale_y")) {
                                        JsonElement scale_y = object.get("scale_y");
                                        if (scale_y.isJsonPrimitive() && scale_y.getAsJsonPrimitive().isNumber()) {
                                            component.setScaleY(scale_y.getAsNumber().floatValue());
                                        }
                                    }
                                    if (object.has("x")) {
                                        JsonElement x = object.get("x");
                                        if (x.isJsonPrimitive() && x.getAsJsonPrimitive().isNumber()) {
                                            component.setX(x.getAsNumber().doubleValue());
                                        }
                                    }
                                    if (object.has("y")) {
                                        JsonElement y = object.get("y");
                                        if (y.isJsonPrimitive() && y.getAsJsonPrimitive().isNumber()) {
                                            component.setY(y.getAsNumber().doubleValue());
                                        }
                                    }

                                    components.add(component);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            JsonWriter writer = new JsonWriter(new PrintWriter(file));
            writer.setIndent("  ");

            writer.beginArray();

            for (Component component : components) {
                if (Double.isNaN(component.getScaleX()) || Double.isNaN(component.getScaleY()) ||
                        Double.isNaN(component.getX()) || Double.isNaN(component.getY())) {
                    continue;
                }
                writer.beginObject();
                for (Map.Entry<String, Class<? extends Component>> entry : map.entrySet()) {
                    if (entry.getValue().equals(component.getClass())) {
                        writer.name("id").value(entry.getKey());
                        break;
                    }
                }
                writer.name("scale_x").value(component.getScaleX());
                writer.name("scale_y").value(component.getScaleY());
                writer.name("x").value(component.getX());
                writer.name("y").value(component.getY());
                component.save(writer);
                writer.endObject();
            }

            writer.endArray();

            writer.close();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public Map<String, Class<? extends Component>> getMap() {
        return map;
    }

    public File getFile() {
        return file;
    }
}
