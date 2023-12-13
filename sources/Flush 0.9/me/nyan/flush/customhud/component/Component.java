package me.nyan.flush.customhud.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import me.nyan.flush.Flush;
import me.nyan.flush.customhud.CustomHud;
import me.nyan.flush.customhud.setting.Setting;
import me.nyan.flush.customhud.setting.impl.*;
import me.nyan.flush.utils.other.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public abstract class Component {
    protected final CustomHud customHud = Flush.getInstance().getCustomHud();
    protected final Flush flush = Flush.getInstance();

    protected final Minecraft mc = Minecraft.getMinecraft();
    protected float scaleX = 1;
    protected float scaleY = 1;
    protected double x;
    protected double y;
    protected final ArrayList<Setting> settings = new ArrayList<>();
    protected ScaledResolution sr = new ScaledResolution(mc);

    public Component() {
        onAdded();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = Math.min(Math.max(x, 0), 1);
    }

    public void setY(double y) {
        this.y = Math.min(Math.max(y, 0), 1);
    }

    public float getScaledX() {
        sr = new ScaledResolution(Minecraft.getMinecraft());
        return (float) (x * (sr.getScaledWidth() - getWidth()));
    }

    public float getScaledY() {
        sr = new ScaledResolution(Minecraft.getMinecraft());
        return (float) (y * (sr.getScaledHeight() - getHeight()));
    }

    public void setScaledX(float x) {
        sr = new ScaledResolution(Minecraft.getMinecraft());
        x = (float) MathUtils.snapToStep(x, sr.getScaleFactor());
        setX(x / (float) (sr.getScaledWidth() - width()));
    }

    public void setScaledY(float y) {
        sr = new ScaledResolution(Minecraft.getMinecraft());
        x = (float) MathUtils.snapToStep(x, sr.getScaleFactor());
        setY(y / (float) (sr.getScaledHeight() - height()));
    }

    public int getWidth() {
        return (int) (width() * (getResizeType() != ResizeType.NONE ? scaleX : 1));
    }

    public int getHeight() {
        return (int) (height() * (getResizeType() == ResizeType.CUSTOM ? scaleY : getResizeType() == ResizeType.NORMAL ? scaleX : 1));
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = Math.max(scaleX, 0.1F);
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = Math.max(scaleY, 0.1F);
    }

    public abstract void draw(float x, float y);
    public abstract int width();
    public abstract int height();

    public ResizeType getResizeType() {
        return ResizeType.NONE;
    }

    public Position getXPosition() {
        return getX() < 0.33 ? Position.LEFT : getX() > 0.66 ? Position.RIGHT : Position.MIDDLE;
    }

    public Position getYPosition() {
        return getY() < 0.5 ? Position.TOP : Position.BOTTOM;
    }

    public void onKey(int key) {

    }

    public void dispose() {

    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public Setting getSetting(String name) {
        for (Setting setting : settings) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return setting;
            }
        }

        return null;
    }

    public void load(JsonObject o) {
        if (!o.has("settings")) {
            return;
        }
        JsonObject settings = o.getAsJsonObject("settings");
        for (Map.Entry<String, JsonElement> entry : settings.entrySet()) {
            if (entry.getValue() instanceof JsonPrimitive) {
                Setting setting = getSetting(entry.getKey());
                JsonPrimitive primitive = entry.getValue().getAsJsonPrimitive();
                if (setting != null) {
                    if (setting instanceof NumberSetting) {
                        ((NumberSetting) setting).setValue(primitive.getAsNumber().doubleValue());
                    }
                    if (setting instanceof ModeSetting) {
                        ((ModeSetting) setting).setValue(primitive.getAsString());
                    }
                    if (setting instanceof TextSetting) {
                        ((TextSetting) setting).setValue(primitive.getAsString());
                    }
                    if (setting instanceof BooleanSetting) {
                        ((BooleanSetting) setting).setValue(primitive.getAsBoolean());
                    }
                    if (setting instanceof ColorSetting) {
                        ((ColorSetting) setting).setValue(primitive.getAsInt());
                    }
                    if (setting instanceof FontSetting) {
                        String text = primitive.getAsString();
                        String[] split = text.split(":");
                        ((FontSetting) setting).setFont(split[0]);
                        ((FontSetting) setting).setSize(Integer.parseInt(split[1]));
                        ((FontSetting) setting).setMinecraftFont(Boolean.parseBoolean(split[2]));
                    }
                }
            }
        }
    }

    public void save(JsonWriter w) throws IOException {
        w.name("settings").beginObject();
        for (Setting setting : settings) {
            w.name(setting.getName());
            if (setting instanceof NumberSetting) {
                w.value(((NumberSetting) setting).getValue());
            }
            if (setting instanceof ModeSetting) {
                w.value(((ModeSetting) setting).getValue());
            }
            if (setting instanceof TextSetting) {
                w.value(((TextSetting) setting).getValue());
            }
            if (setting instanceof BooleanSetting) {
                w.value(((BooleanSetting) setting).getValue());
            }
            if (setting instanceof ColorSetting) {
                w.value(((ColorSetting) setting).getRGB());
            }
            if (setting instanceof FontSetting) {
                FontSetting font = (FontSetting) setting;
                w.value(font.getFont() + ":" + font.getSize() + ":" + font.isMinecraftFont());
            }
        }
        w.endObject();
    }

    public void onAdded() {

    }

    public enum Position {
        LEFT,
        MIDDLE,
        RIGHT,
        TOP,
        BOTTOM
    }

    public enum ResizeType {
        NONE,
        NORMAL,
        CUSTOM
    }
}
