package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;
import today.flux.Flux;
import today.flux.event.TickEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.ColorValue;
import today.flux.module.value.FloatValue;
import today.flux.module.value.ModeValue;

import java.awt.*;

public class Hud extends Module {

    public static String name = "Flux";

    private static final ModeValue fontPicker = new ModeValue("HUD", "Font Renderer", "TTF", "TTF", "Minecraft");

    public static ModeValue Theme = new ModeValue("Hud", "HUD Theme", "Rainbow", "Rainbow", "Rainbow2", "Color", "Fade", "Gradient", "Nostalgia", "Test");
    public static ModeValue waterMarkMode = new ModeValue("HUD", "Watermark Mode", "Text", "Text", "Onetap", "Icon", "Flux Sense", "None");
    public static ModeValue subModuleMode = new ModeValue("HUD", "Mode Color", "White", "Gray", "Nostalgia");
    public static ModeValue notifMode = new ModeValue("HUD", "Notification Mode", "New", "New", "Classical");
    public static ModeValue fadeDirection = new ModeValue("HUD", "Fade Direction", "Up", "Down");
    public static ModeValue fadeSpeed = new ModeValue("HUD", "Fade Speed", "Fast", "Slow");

    public static ColorValue watermarkColour = new ColorValue("HUD", "Watermark Color", new Color(0xFF00AA00));
    public static ColorValue arraylistColor1 = new ColorValue("HUD", "Arraylist Color 1", Color.RED);
    public static ColorValue arraylistColor2 = new ColorValue("HUD", "Arraylist Color 2", Color.CYAN);
    public static ColorValue positionColour = new ColorValue("HUD", "Position Color", Color.BLUE);

    public static FloatValue blurIntensity = new FloatValue("HUD", "Blur Strength", 5, 1, 10, 1f);
    public static FloatValue moduleRainbowSpeed = new FloatValue("HUD", "Module Color Rainbow Speed", 1, 0.1f, 10, 0.1f);
    public static FloatValue rainbowSpeed = new FloatValue("Hud", "HUD Rainbow Speed", 2f, -10f, 10f, 0.1f);
    public static FloatValue backgroundAlpha = new FloatValue("HUD", "Background Alpha", 170, 0, 255, 1);
    public static FloatValue offset = new FloatValue("HUD", "Offset", 11, 10, 20, 1);

    public static BooleanValue tabgui = new BooleanValue("HUD", "Tab GUI", true);
    public static BooleanValue notification = new BooleanValue("HUD", "Notification", true);
    public static BooleanValue arraylist = new BooleanValue("HUD", "Arraylist", true);
    public static BooleanValue background = new BooleanValue("HUD", "Background", true);
    public static BooleanValue colorbar = new BooleanValue("HUD", "Color Bar", true);
    public static BooleanValue armor = new BooleanValue("HUD", "Show Armor HUD", true);
    public static BooleanValue effect = new BooleanValue("HUD", "Show Potion Effects", true);
    public static BooleanValue position = new BooleanValue("HUD","Show Position",true);
    public static BooleanValue bps = new BooleanValue("HUD","Show Blocks/Second",true);
    public static BooleanValue NoShader = new BooleanValue("Hud", "Disable Blur", true);
    public static BooleanValue fastChat = new BooleanValue("Hud", "Fast Chat", false);
    public static BooleanValue chatAnimation = new BooleanValue("Hud", "Chat Animation", true);
    public static BooleanValue renderRenderCategory = new BooleanValue("HUD", "Only Important", false);

    public Hud() {
        super("HUD", Category.Render, false);
    }

    public static boolean isMinecraftFont, isLightMode;

    @EventTarget
    public void onTicks(TickEvent e) {
        isMinecraftFont = fontPicker.isCurrentMode("Minecraft");
        isLightMode = Flux.INSTANCE.GTheme.isCurrentMode("Light");
    }
}