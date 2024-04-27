package me.aquavit.liquidsense.module.modules.render;

import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.ListValue;
import me.aquavit.liquidsense.value.Value;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleInfo(name = "Chams", description = "Allows you to see targets through blocks.", category = ModuleCategory.RENDER)
public class Chams extends Module {
    public static BoolValue targetsValue = new BoolValue("Targets", true);
    public static BoolValue chestsValue = new BoolValue("Chests", true);
    public static BoolValue itemsValue = new BoolValue("Items", true);

    public static ListValue modeValue = new ListValue("Mode", new String[] {"Normal", "Color", "CSGO"}, "CSGO");

    public static Value<Float> colorRedValue = new FloatValue("R", 200.0f, 0.0f, 255.0f).displayable(() ->
            !modeValue.get().equalsIgnoreCase("Normal"));
    public static Value<Float> colorGreenValue = new FloatValue("G", 100.0f, 0.0f, 255.0f).displayable(() ->
            !modeValue.get().equalsIgnoreCase("Normal"));
    public static Value<Float> colorBlueValue = new FloatValue("B", 100.0f, 0.0f, 255.0f).displayable(() ->
            !modeValue.get().equalsIgnoreCase("Normal"));
    public static Value<Float> colorAValue = new FloatValue("A", 100.0f, 0.0f, 255.0f).displayable(() ->
            !modeValue.get().equalsIgnoreCase("Normal"));
    public static Value<Float> colorRed2Value = new FloatValue("R2", 100.0f, 0.0f, 255.0f).displayable(() ->
            !modeValue.get().equalsIgnoreCase("Normal"));
    public static Value<Float> colorGreen2Value = new FloatValue("G2", 100.0f, 0.0f, 255.0f).displayable(() ->
            !modeValue.get().equalsIgnoreCase("Normal"));
    public static Value<Float> colorBlue2Value = new FloatValue("B2", 200.0f, 0.0f, 255.0f).displayable(() ->
            !modeValue.get().equalsIgnoreCase("Normal"));
    public static Value<Float> colorA2Value = new FloatValue("A2", 200.0f, 0.0f, 255.0f).displayable(() ->
            !modeValue.get().equalsIgnoreCase("Normal"));


    public static Value<Float> saturationValue = new FloatValue("Saturation", 0.4f, 0.0f, 1.0f).displayable(() ->
            !modeValue.get().equalsIgnoreCase("Normal"));
    public static Value<Float> brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f).displayable(() ->
            !modeValue.get().equalsIgnoreCase("Normal"));

    public static BoolValue rainbow = new BoolValue("Rainbow", false);

    public static BoolValue all = new BoolValue("AllEntity", false);
    private static BoolValue handValue = new BoolValue("Hand", false);

    public static void preHandRender() {
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(2896);
        new Color(255, 255, 255, 55);
    }

    public static void postHandRender() {
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static boolean shouldRenderHand() {
        return handValue.get();
    }
}
