package me.aquavit.liquidsense.ui.client.hud;

import com.google.gson.*;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.client.hud.element.Side;
import me.aquavit.liquidsense.value.FontValue;
import me.aquavit.liquidsense.value.Value;

import java.util.Objects;

public class Config {
    private JsonArray jsonArray;

    public Config(final String config) {
        this.jsonArray = new JsonArray();
        this.jsonArray = new Gson().fromJson(config, JsonArray.class);
    }

    public Config(final HUD hud) {
        this.jsonArray = new JsonArray();
        for (final Element element : hud.getElements()) {
            final JsonObject elementObject = new JsonObject();
            elementObject.addProperty("Type", element.getName());
            elementObject.addProperty("X", element.getX());
            elementObject.addProperty("Y", element.getY());
            elementObject.addProperty("Scale", element.getScale());
            elementObject.addProperty("HorizontalFacing", element.getSide().getHorizontal().getSideName());
            elementObject.addProperty("VerticalFacing", element.getSide().getVertical().getSideName());

            for (Value<?> value : element.getValues())
                elementObject.add(value.getName(), value.toJson());

            jsonArray.add(elementObject);
        }
    }

    public String toJson() {
        if (jsonArray == null) {
            return "";
        }
        return new GsonBuilder().setPrettyPrinting().create().toJson(this.jsonArray);
    }

    public HUD toHUD() {
        final HUD hud = new HUD();

        if (jsonArray == null) {
            return HUD.Companion.createDefault();
        }

        try {
            for (final JsonElement jsonElement : this.jsonArray) {
                try {
                    if (jsonElement == null || jsonElement instanceof JsonNull || !jsonElement.isJsonObject()) {
                        continue;
                    }

                    final JsonObject jsonObject = jsonElement.getAsJsonObject();

                    if (!(jsonObject instanceof JsonObject)) continue;

                    if (!jsonObject.has("Type")) continue;

                    final String type = jsonObject.get("Type").getAsString();

                    for (Class<? extends Element> elementClass : HUD.Companion.getElements()) {
                        String classType = elementClass.getAnnotation(ElementInfo.class).name();

                        if (classType.equals(type)) {
                            Element element = elementClass.newInstance();

                            element.setX(jsonObject.get("X").getAsInt());
                            element.setY(jsonObject.get("Y").getAsInt());
                            element.setScale(jsonObject.get("Scale").getAsFloat());
                            element.setSide(new Side(Objects.requireNonNull(Side.Horizontal.Companion.getByName(jsonObject.get("HorizontalFacing").getAsString())),
                                    Objects.requireNonNull(Side.Vertical.Companion.getByName(jsonObject.get("VerticalFacing").getAsString()))));

                            for (Value<?> value : element.getValues()) {
                                if (jsonObject.has(value.getName()))
                                    value.fromJson(jsonObject.get(value.getName()));
                            }

                            // Support for old HUD files
                            if (jsonObject.has("font")) {
                                for (Value<?> value : element.getValues()) {
                                    if (value instanceof FontValue){
                                        value.fromJson(jsonObject.get("font"));
                                    }
                                }
                            }

                            hud.addElement(element);
                            break;
                        }
                    }
                } catch (Exception e) {
                    ClientUtils.getLogger().error("Error while loading custom hud element from config.", e);
                }
            }

            // Add forced elements when missing
            for (Class<? extends Element> elementClass : HUD.Companion.getElements()) {
                if (elementClass.getAnnotation(ElementInfo.class).force() && hud.elements.stream().noneMatch(it -> it.getClass() == elementClass)){
                    hud.addElement(elementClass.newInstance());
                }
            }
        } catch (Exception e) {
            ClientUtils.getLogger().error("Error while loading custom hud config.", e);
            return HUD.Companion.createDefault();
        }

        return hud;
    }
}
