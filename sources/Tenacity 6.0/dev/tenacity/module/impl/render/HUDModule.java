package dev.tenacity.module.impl.render;

import dev.tenacity.Tenacity;
import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.event.impl.render.ShaderEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.BooleanSetting;
import dev.tenacity.setting.impl.ColorSetting;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.setting.impl.NumberSetting;
import dev.tenacity.util.player.MovementUtil;
import dev.tenacity.util.render.ColorUtil;
import dev.tenacity.util.render.GradientUtils;
import dev.tenacity.util.render.Theme;
import dev.tenacity.util.render.font.CustomFont;
import dev.tenacity.util.render.font.FontUtil;
import dev.tenacity.util.tuples.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.Packet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.layer.GenLayerEdge;
import org.lwjgl.opengl.GL11;
import java.awt.*;

import static dev.tenacity.Tenacity.BLANK;
import static dev.tenacity.command.impl.NameCommand.CN;
import static dev.tenacity.command.impl.NameCommand.CV;

public final class HUDModule extends Module {

    public static final ModeSetting theme = new ModeSetting("Theme", "Tenacity", "Christmas", "Guff", "Skeet", "Snowy Sky", "Winter", "Orange Juice", "Java", "Thoq", "Water", "Coral Pink", "Steel", "Batman", "Custom");
    public static final ColorSetting primaryColor = new ColorSetting("Primary Color", Color.BLUE);
    public static final ColorSetting blurtint = new ColorSetting("Blur tint", Color.BLACK);
    public static final NumberSetting opacity = new NumberSetting("Opacity", 0.1, 0, 1, 0.1);
    public static final ColorSetting secondaryColor = new ColorSetting("Secondary Color", Color.PINK);
    public static final ModeSetting displayMode = new ModeSetting("Logo", "Text", "Image");
    public static final BooleanSetting guicolor = new BooleanSetting("GUI", true);
    public static final BooleanSetting watermark = new BooleanSetting("Watermark", true);
    public static final BooleanSetting xyz = new BooleanSetting("Coordinates", true);
    private boolean guicolorinit = false;


    public HUDModule() {
        super("HUD", "Renders the Heads Up Display", ModuleCategory.RENDER);
        primaryColor.addParent(theme, setting -> setting.isMode("Custom"));
        secondaryColor.addParent(theme, setting -> setting.isMode("Custom"));
        initializeSettings(theme, primaryColor, secondaryColor, displayMode, guicolor, watermark, xyz);
    }

    private final IEventListener<Render2DEvent> onRender2DEvent = event -> {
        if (watermark.isEnabled()) {
            CustomFont font = FontUtil.getFont("OpenSans-SemiBold", 20);
            if (font != null) {
                int screenWidth = event.getScaledResolution().getScaledWidth();
                int screenHeight = event.getScaledResolution().getScaledHeight();

                float textWidth = font.getStringWidth("Tenacity.rip");
                float textHeight = font.getHeight();
                float padding = 4;
                float xPosition = screenWidth - textWidth - padding;
                float yPosition = screenHeight - textHeight - padding;

                font.drawString("Tenacity.rip", xPosition, yPosition, Color.WHITE.getRGB());
            }
        }
        if (xyz.isEnabled()) {
            CustomFont font = FontUtil.getFont("OpenSans-SemiBold", 20);
            if (font != null) {
                int screenWidth = event.getScaledResolution().getScaledWidth();
                int screenHeight = event.getScaledResolution().getScaledHeight();

                String xyzText = "XYZ:" + BLANK + String.format("%.1f", mc.thePlayer.posX) + "," + BLANK + String.format("%.1f", mc.thePlayer.posY) + "," + BLANK + String.format("%.1f", mc.thePlayer.posZ);
                float textWidth = font.getStringWidth(xyzText);
                float textHeight = font.getHeight();
                float padding = 4;
                float xPosition = padding;

                float yPosition = screenHeight - textHeight - padding;

                font.drawString(xyzText, xPosition, yPosition, Color.WHITE.getRGB());
            }
        }
        String currentMode = displayMode.getCurrentMode();
        if (guicolor.isEnabled() && !guicolorinit) {
            initializeSettings(blurtint, opacity);
            guicolorinit = true;
        }

        switch (currentMode.toLowerCase()) {
            case "text":
                renderText();
                break;
            case "image":
                renderImage();
                break;
            default:
                break;
        }
    };

    private final IEventListener<ShaderEvent> onShaderEvent = event -> {
        String currentMode = displayMode.getCurrentMode();
        switch (currentMode.toLowerCase()) {
            case "Both":
                renderText();
                break;
            case "text":
           //     renderText();
                break;
            case "image":
          //      renderImage();
                break;
            default:
                break;
        }
    };

    private void renderText() {
        Pair<Color, Color> clientColors = Theme.getThemeColors(theme.getCurrentMode());
        CustomFont title = FontUtil.getFont("OpenSans-Medium", 34);
        CustomFont small = FontUtil.getFont("OpenSans-Medium", (int) (32 / 1.4));

        GradientUtils.applyGradientCornerLR(27, 23, 110 / 2.0F, (110 / 2.0F) - 28, 1, clientColors.getSecond(), clientColors.getFirst(), () -> {
            title.drawString(CN, 7, 7, 0);
            small.drawString(CV, 7 + title.getStringWidth(CN) + 2, 6, 0);
        });
        ColorUtil.setPrimaryColor(primaryColor.getColor());
    }

    private void renderImage() {
        CustomFont font = FontUtil.getFont("OpenSans-SemiBold", 30);
        if (font != null && !MovementUtil.isMoving()) {
            font.drawString(CN, 60, 26, Color.white.getRGB());
        } else if (MovementUtil.isMoving()) {
            font.drawString(" ", 60, 26, Color.white.getRGB());
        }
        try {
            ResourceLocation imageResource = new ResourceLocation("minecraft", "Tenacity\\logo-shadow.png");
            int x = 0;
            int y = 0;
            int width = 64;
            int height = 64;

            drawImage(imageResource, x, y, width, height);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
    }
}