package tech.drainwalk.gui.menu.window.windows.colorpickerwindow;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import tech.drainwalk.DrainWalk;
import tech.drainwalk.client.option.options.ColorOption;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.gui.menu.hovered.Hovered;
import tech.drainwalk.gui.menu.window.Window;
import tech.drainwalk.utility.color.ColorUtility;
import tech.drainwalk.utility.math.MathUtility;
import tech.drainwalk.utility.render.RenderUtility;

public class ColorPickerWindow extends Window {
    public ColorOption option;
    public int color = 0;
    public boolean isHueDrag = false;
    public int hueSliderValue = 1;
    public boolean isAlphaDrag = false;
    public int alphaSliderValue = 255;

    private final FloatOption hueSlider = new FloatOption("Hue", 1f, 1f, 360f);
    private final FloatOption alphaSlider = new FloatOption("Alpha", 255f, 0f, 255f);

    public ColorPickerWindow() {
        super(false);
        this.windowWidth = 78;
        this.windowHeight = 92;
    }


    @Override
    public void renderWindow(int mouseX, int mouseY, float partialTicks) {
        if (option != null) {
            RenderUtility.drawRoundedRect(windowX, windowY, windowWidth, windowHeight, 6, ClientColor.object);
            RenderUtility.drawRoundedOutlineRect(windowX, windowY, windowWidth, windowHeight, 6, 1.25f, ClientColor.panelLines);
            if (DrainWalk.getInstance().isRoflMode()) {
                RenderUtility.drawRoundedTexture(new ResourceLocation("drainwalk/images/deus_mode.png"), windowX, windowY, windowWidth, windowHeight, 11, 0.1f);
            }
            float[] colorValue = ColorUtility.getRGBAf(color);

            float[] settingValue = ColorUtility.getRGBAf(option.getValue());
            int hueColor = ColorUtility.HUEtoRGB(hueSliderValue);
            RenderUtility.drawRect(windowX + 3.5f - 0.5f, windowY + 3.5f - 0.5f, 71 + 1, 60 + 1, ClientColor.panelLines);

            RenderUtility.drawRoundedGradientRect(windowX + 3.5f, windowY + 3.5f, 71, 60, 0, 0, ColorUtility.rgba(255, 255, 255, 255), ColorUtility.rgb(0, 0, 0), hueColor, ColorUtility.rgb(0, 0, 0));
            if (Hovered.isHovered(mouseX, mouseY, windowX + 3f, windowY + 3f, 71, 60 + 1)) {
                if (!isHueDrag) {
                    if (!isAlphaDrag) {
                        if (Mouse.isButtonDown(0)) {
                            color = ColorUtility.getColorFromPixel(Mouse.getX(), Mouse.getY());

                        }
                    }
                }
            }
            if (color != 0) {
                option.setValue(ColorUtility.rgbaFloat(colorValue[0], colorValue[1], colorValue[2], alphaSliderValue / 255f));
            }

            if (isHueDrag) {
                hueSliderValue = createSlider(hueSlider, windowX + 3.5f, mouseX);
            }

            if (isAlphaDrag) {
                alphaSliderValue = createSlider(alphaSlider, windowX + 3.5f, mouseX);
            }
            RenderUtility.drawRect(windowX + 3.5f - 0.5f, windowY + 67 - 0.5f, 71 + 1, 4.5f + 1, ClientColor.panelLines);
            RenderUtility.drawImage(new ResourceLocation("drainwalk/images/hue_horizontal.png"), windowX + 3.5f, windowY + 67, 71, 4.5f);
            RenderUtility.drawRect(windowX + 3.5f + getPos(hueSlider, hueSliderValue), windowY + 67 - 0.5f, 1.5f, 5.5f, ClientColor.textMain);

            RenderUtility.drawRect(windowX + 3.5f - 0.5f, windowY + 73.5f - 0.5f, 71 + 1, 4.5f + 1, ClientColor.panelLines);
            RenderUtility.drawImage(new ResourceLocation("drainwalk/images/alpha_horizontal.png"), windowX + 3.5f, windowY + 73.5f, 71, 4.5f);

            GlStateManager.disableAlpha();
            RenderUtility.drawRoundedGradientRect(windowX + 3.5f, windowY + 73.5f, 71, 4.5f, 0, 0, ColorUtility.rgbaFloat(settingValue[0], settingValue[1], settingValue[2], 0), ColorUtility.rgbaFloat(settingValue[0], settingValue[1], settingValue[2], 0), ColorUtility.rgbaFloat(settingValue[0], settingValue[1], settingValue[2], 1), ColorUtility.rgbaFloat(settingValue[0], settingValue[1], settingValue[2], 1));
            GlStateManager.enableAlpha();

            RenderUtility.drawRect(windowX + 3.5f + getPos(alphaSlider, alphaSliderValue), windowY + 73.5f - 0.5f, 1.5f, 5.5f, ClientColor.textMain);
            RenderUtility.drawRoundedOutlineRect(windowX + 3.5f, windowY + 80, 2f +
                            FontManager.SEMI_BOLD_12.getStringWidth("#!" + ColorUtility.RGBtoHEXString(option.getValue()).toUpperCase() + " " + (int) ((alphaSliderValue / 255f) * 100) + "%"),
                    8, 3, 1.25f, ClientColor.panelLines);
            FontManager.SEMI_BOLD_12.drawString("#" + ColorUtility.RGBtoHEXString(option.getValue()).toUpperCase() + " " + (int) ((alphaSliderValue / 255f) * 100) + "%", windowX + 5.5f, windowY + 83, ClientColor.textStay);

        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (Hovered.isHovered(mouseX, mouseY, windowX + 3.5f, windowY + 67, 71, 4.5f) && mouseButton == 0) {
            isHueDrag = true;
        }
        if (Hovered.isHovered(mouseX, mouseY, windowX + 3.5f, windowY + 73.5f, 71, 4.5f) && mouseButton == 0) {
            isAlphaDrag = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        isHueDrag = false;
        isAlphaDrag = false;
    }

    public int createSlider(FloatOption floatSetting, float posX, int mouseX) {
        int delta = (int) (floatSetting.getMax() - floatSetting.getMin());
        float clickedX = mouseX - posX;
        float value = clickedX / 71;
        float outValue = floatSetting.getMin() + delta * value;
        return (int) MathUtility.clamp((int) MathUtility.round(outValue, floatSetting.getInc()), floatSetting.getMin(), floatSetting.getMax());
    }

    public int getPos(FloatOption floatSetting, int value) {
        int delta = (int) (floatSetting.getMax() - floatSetting.getMin());
        return (int) (70 * (value - floatSetting.getMin()) / delta);
    }
}
