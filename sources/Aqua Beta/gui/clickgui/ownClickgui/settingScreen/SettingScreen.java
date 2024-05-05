package gui.clickgui.ownClickgui.settingScreen;

import de.Hero.settings.GuiColorChooser2;
import de.Hero.settings.Setting;
import gui.clickgui.ownClickgui.ClickguiScreen;
import gui.clickgui.ownClickgui.settingScreen.SettingScreen;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.fr.lavache.anime.Animate;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.visual.Blur;
import intent.AquaDev.aqua.modules.visual.Shadow;
import intent.AquaDev.aqua.utils.ColorUtils;
import intent.AquaDev.aqua.utils.RenderUtil;
import intent.AquaDev.aqua.utils.Translate;
import intent.AquaDev.aqua.utils.UnicodeFontRenderer;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class SettingScreen
extends GuiScreen {
    private GuiScreen parent;
    private ResourceLocation resourceLocation;
    private int scrollAdd = 0;
    private int x;
    private int y;
    private Module module;
    private float scroll;
    private GuiColorChooser2 colorChooser2;
    private int modX;
    private int modY;
    private int modWidth;
    private int modHeight;
    private final Animate animate = new Animate();
    Translate translate;

    public SettingScreen(Module module, GuiScreen parent) {
        this.parent = parent;
        this.module = module;
        this.colorChooser2 = new GuiColorChooser2(this.modX, this.modY + 10);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.resourceLocation == null) {
            return;
        }
        ScaledResolution scaledRes = new ScaledResolution(this.mc);
        float posX = (float)Aqua.setmgr.getSetting("GuiElementsPosX").getCurrentNumber();
        float posY = (float)Aqua.setmgr.getSetting("GuiElementsPosY").getCurrentNumber();
        float width1 = (float)Aqua.setmgr.getSetting("GuiElementsWidth").getCurrentNumber();
        float height1 = (float)Aqua.setmgr.getSetting("GuiElementsHeight").getCurrentNumber();
        float alpha1 = (float)Aqua.setmgr.getSetting("GuiElementsBackgroundAlpha").getCurrentNumber();
        if (Aqua.moduleManager.getModuleByName("GuiElements").isToggled()) {
            if (Aqua.setmgr.getSetting("GuiElementsCustomPic").isState()) {
                RenderUtil.drawImage((int)((int)((float)scaledRes.getScaledWidth() - this.animate.getValue() - posX)), (int)((int)((float)scaledRes.getScaledHeight() - posY)), (int)((int)width1), (int)((int)height1), (ResourceLocation)this.resourceLocation);
            }
            if (Aqua.moduleManager.getModuleByName("Blur").isToggled()) {
                int color = Aqua.setmgr.getSetting("HUDColor").getColor();
                Color colorAlpha = ColorUtils.getColorAlpha((int)color, (int)((int)alpha1));
                if (Aqua.setmgr.getSetting("GuiElementsBackgroundColor").isState()) {
                    Gui.drawRect2((double)0.0, (double)0.0, (double)this.mc.displayWidth, (double)this.mc.displayHeight, (int)colorAlpha.getRGB());
                }
                Blur.drawBlurred(() -> Gui.drawRect((int)0, (int)0, (int)this.mc.displayWidth, (int)this.mc.displayHeight, (int)-1), (boolean)false);
            }
            if (Aqua.setmgr.getSetting("GuiElementsCustomPic").isState()) {
                RenderUtil.drawImage((int)((int)((float)scaledRes.getScaledWidth() - this.animate.getValue() - posX)), (int)((int)((float)scaledRes.getScaledHeight() - posY)), (int)((int)width1), (int)((int)height1), (ResourceLocation)this.resourceLocation);
            }
        }
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.translate.interpolate((float)width, (float)height, 4.0);
        double xmod = (float)width / 2.0f - this.translate.getX() / 2.0f;
        double ymod = (float)height / 2.0f - this.translate.getY() / 2.0f;
        GlStateManager.translate((double)xmod, (double)ymod, (double)0.0);
        GlStateManager.scale((float)(this.translate.getX() / (float)width), (float)(this.translate.getY() / (float)height), (float)1.0f);
        int hudColor = Aqua.setmgr.getSetting("HUDColor").getColor();
        float leftWindowBorder = (float)sr.getScaledWidth() / 2.0f - 95.0f;
        float rightWindowBorder = leftWindowBorder + 190.0f;
        float windowWidth = 190.0f;
        float windowY = 180.0f;
        float windowHeight = 150.0f;
        RenderUtil.drawRoundedRect2Alpha((double)leftWindowBorder, (double)windowY, (double)windowWidth, (double)windowHeight, (double)4.0, (Color)new Color(0, 0, 0, 100));
        int textColor = Color.GRAY.getRGB();
        ArrayList settings = Aqua.setmgr.getSettingsFromModule(this.module);
        GL11.glEnable((int)3089);
        GL11.glScissor((int)((int)(leftWindowBorder * (float)sr.getScaleFactor())), (int)((int)(windowY * (float)sr.getScaleFactor())), (int)((int)(windowWidth * (float)sr.getScaleFactor())), (int)((int)(windowHeight * (float)sr.getScaleFactor())));
        float offset = 0.0f;
        for (Setting setting : settings) {
            float y = windowY + offset + 10.0f + this.scroll;
            boolean doRender = y > windowY && y < windowY + windowHeight;
            offset += this.drawSettingOwn(setting, leftWindowBorder + 10.0f, y, rightWindowBorder, mouseX, mouseY, doRender) + 3.0f;
        }
        if (Mouse.hasWheel()) {
            this.scroll = (float)((double)this.scroll + (double)Aqua.INSTANCE.mouseWheelUtil.mouseDelta / 9.0);
        }
        if (-offset - 10.0f + windowHeight > this.scroll) {
            this.scroll = -offset - 10.0f + windowHeight;
        }
        if (this.scroll > 0.0f || offset + 10.0f < windowHeight) {
            this.scroll = 0.0f;
        }
        GL11.glDisable((int)3089);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void initGui() {
        try {
            File file = new File(System.getProperty((String)"user.dir") + "/" + Aqua.name + "//pic/" + Aqua.setmgr.getSetting("GuiElementsMode").getCurrentMode() + ".png");
            BufferedImage bi = ImageIO.read((File)file);
            this.resourceLocation = Minecraft.getMinecraft().getRenderManager().renderEngine.getDynamicTextureLocation("name", new DynamicTexture(bi));
        }
        catch (Exception file) {
            // empty catch block
        }
        this.translate = new Translate(0.0f, 0.0f);
        ScaledResolution sr = new ScaledResolution(this.mc);
    }

    /*
     * WARNING - void declaration
     */
    public float drawSettingOwn(Setting setting, float nameX, float nameY, float windowBorderX, int mouseX, int mouseY, boolean doRender) {
        int textColor = Color.WHITE.getRGB();
        String camelCase = setting.getDisplayName().substring(0, 1).toUpperCase() + setting.getDisplayName().substring(1);
        UnicodeFontRenderer font = Aqua.INSTANCE.comfortaa5;
        UnicodeFontRenderer font2 = Aqua.INSTANCE.comfortaa5;
        float height = font.getHeight(camelCase) + 3.0f;
        int distanceToBorder = 10;
        switch (1.$SwitchMap$de$Hero$settings$Setting$Type[setting.type.ordinal()]) {
            case 1: {
                Color color;
                int rectWidth = 30;
                int rectHeight = 10;
                float checkBoxXmin = windowBorderX - (float)distanceToBorder - (float)rectWidth;
                float checkBoxYmin = nameY;
                boolean hovered = this.mouseOver(mouseX, mouseY, (int)checkBoxXmin, (int)checkBoxYmin, rectWidth, rectHeight);
                setting.setHovered(hovered && doRender);
                Color color2 = color = setting.isState() ? Color.GREEN : Color.RED;
                if (hovered) {
                    Color color3 = color.darker();
                }
                if (doRender) {
                    void var19_33;
                    Shadow.drawGlow(() -> font.drawString(camelCase, nameX, nameY, Color.WHITE.getRGB()), (boolean)false);
                    font.drawString(camelCase, nameX, nameY, textColor);
                    Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)checkBoxXmin, (double)nameY, (double)rectWidth, (double)rectHeight, (double)1.0, (int)new Color(0, 0, 0, 255).getRGB()), (boolean)false);
                    if (setting.isState()) {
                        void finalColor = var19_33;
                        Shadow.drawGlow(() -> SettingScreen.lambda$drawSettingOwn$3(checkBoxXmin, nameY, rectWidth, rectHeight, (Color)finalColor), (boolean)false);
                        RenderUtil.drawRoundedRect2Alpha((double)checkBoxXmin, (double)nameY, (double)((float)rectWidth / 2.0f), (double)rectHeight, (double)1.0, (Color)var19_33);
                        font.drawString("On", checkBoxXmin + 1.3f, nameY, -1);
                    } else {
                        void finalColor1 = var19_33;
                        Shadow.drawGlow(() -> SettingScreen.lambda$drawSettingOwn$4(windowBorderX, distanceToBorder, rectWidth, nameY, rectHeight, (Color)finalColor1), (boolean)false);
                        RenderUtil.drawRoundedRect2Alpha((double)(windowBorderX - (float)distanceToBorder - (float)rectWidth / 2.0f - 1.0f), (double)nameY, (double)((float)rectWidth / 2.0f + 1.0f), (double)rectHeight, (double)1.0, (Color)var19_33);
                        font.drawString("OFF", windowBorderX - (float)distanceToBorder - (float)rectWidth / 2.0f, nameY, -1);
                    }
                }
                height = Math.max((float)rectHeight, (float)height);
                break;
            }
            case 2: {
                double percentage = (setting.getCurrentNumber() - setting.getMin()) / (setting.getMax() - setting.getMin());
                int rectWidth = 100;
                int rectHeight = 6;
                float sliderMinX = windowBorderX - (float)distanceToBorder - (float)rectWidth;
                float f = nameY + height / 2.0f - (float)(rectHeight / 2);
                setting.setHovered(this.mouseOver(mouseX, mouseY, (int)sliderMinX, (int)f, rectWidth, rectHeight) && doRender);
                setting.setSliderMinX(sliderMinX);
                setting.setSliderMaxX(sliderMinX + (float)rectWidth);
                if (doRender) {
                    Shadow.drawGlow(() -> font.drawString(camelCase, nameX, nameY, Color.WHITE.getRGB()), (boolean)false);
                    font.drawString(camelCase, nameX, nameY, textColor);
                    Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)sliderMinX, (double)sliderY, (double)rectWidth, (double)rectHeight, (double)1.0, (int)Color.black.getRGB()), (boolean)false);
                    RenderUtil.drawRoundedRect2Alpha((double)sliderMinX, (double)f, (double)rectWidth, (double)rectHeight, (double)1.0, (Color)new Color(0, 0, 0, 60));
                    Color color = new Color(Aqua.setmgr.getSetting("HUDColor").getColor());
                    if (setting.isHovered()) {
                        color = color.darker();
                    }
                    if (percentage > 0.0) {
                        Color finalColor = color;
                        Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)sliderMinX, (double)sliderY, (double)((double)rectWidth * percentage), (double)rectHeight, (double)1.0, (int)finalColor.getRGB()), (boolean)false);
                        RenderUtil.drawRoundedRect2Alpha((double)sliderMinX, (double)f, (double)((double)rectWidth * percentage), (double)rectHeight, (double)1.0, (Color)color);
                    }
                    String value = SettingScreen.round(setting.getCurrentNumber(), 2);
                    Shadow.drawGlow(() -> font2.drawString(value, sliderMinX + (float)rectWidth / 2.0f - font2.getWidth(value) / 2.0f, sliderY + (float)rectHeight / 2.0f - font2.getHeight(value) / 2.0f, Color.WHITE.getRGB()), (boolean)false);
                    font2.drawString(value, sliderMinX + (float)rectWidth / 2.0f - font2.getWidth(value) / 2.0f, f + (float)rectHeight / 2.0f - font2.getHeight(value) / 2.0f, Color.white.getRGB());
                }
                height = Math.max((float)height, (float)rectHeight);
                break;
            }
            case 3: {
                this.colorChooser2.x = nameX - 5.0f;
                this.colorChooser2.y = nameY + 5.0f;
                height = 10 + this.colorChooser2.getHeight();
                this.colorChooser2.draw(mouseX, mouseY);
                setting.color = this.colorChooser2.color;
                this.colorChooser2.setWidth(180);
                break;
            }
            case 4: {
                if (setting.getModes() == null) {
                    return 0.0f;
                }
                float rectHeight = font2.getHeight(setting.getCurrentMode()) + 3.0f;
                setting.setHovered(false);
                if (doRender) {
                    Shadow.drawGlow(() -> font.drawString(camelCase, nameX, nameY, Color.WHITE.getRGB()), (boolean)false);
                    font.drawString(camelCase, nameX, nameY, textColor);
                    float rectWidth = 0.0f;
                    for (String string : setting.getModes()) {
                        rectWidth = Math.max((float)font2.getWidth(string), (float)rectWidth) + 4.0f;
                    }
                    float minX = windowBorderX - (float)distanceToBorder - rectWidth;
                    setting.setHovered(this.mouseOver(mouseX, mouseY, (int)minX, (int)nameY, (int)rectWidth, (int)rectHeight));
                    Color color = new Color(0, 0, 0, 60);
                    if (setting.isHovered()) {
                        color = color.darker();
                    }
                    float offset = 0.0f;
                    if (setting.isComboExtended()) {
                        for (String mode : setting.getModes()) {
                            offset += font2.getHeight(mode) + 1.0f;
                        }
                    }
                    float f = rectWidth;
                    float finalRectHeight = rectHeight;
                    float finalOffset = offset;
                    Color finalColor = color;
                    Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)minX, (double)nameY, (double)finalRectWidth, (double)(finalRectHeight + finalOffset), (double)1.0, (int)finalColor.getRGB()), (boolean)false);
                    RenderUtil.drawRoundedRect2Alpha((double)minX, (double)nameY, (double)rectWidth, (double)(rectHeight + offset), (double)1.0, (Color)color);
                    setting.setComboHoverIndex(-1);
                    if (setting.isComboExtended()) {
                        Color color1 = new Color(Aqua.setmgr.getSetting("HUDColor").getColor());
                        Shadow.drawGlow(() -> font2.drawString(setting.getCurrentMode(), minX + 1.0f, nameY, color1.getRGB()), (boolean)false);
                        font2.drawString(setting.getCurrentMode(), minX + 1.0f, nameY, color1.getRGB());
                        offset = font2.getHeight(setting.getCurrentMode()) + 1.0f;
                        for (int i = 0; i < setting.getModes().length; ++i) {
                            String mode = setting.getModes()[i];
                            Color stringColor = Color.WHITE;
                            if (setting.getCurrentMode().equalsIgnoreCase(mode)) continue;
                            boolean hovered = this.mouseOver(mouseX, mouseY, (int)(minX + 1.0f), (int)(nameY + offset), (int)rectWidth, (int)(font2.getHeight(mode) + 1.0f));
                            if (hovered) {
                                setting.setComboHoverIndex(i);
                                stringColor = stringColor.darker();
                            }
                            float finalOffset1 = offset;
                            Color finalStringColor = stringColor;
                            Shadow.drawGlow(() -> font2.drawString(mode, minX + 1.0f, nameY + finalOffset1, finalStringColor.getRGB()), (boolean)false);
                            font2.drawString(mode, minX + 1.0f, nameY + offset, Color.white.getRGB());
                            if (i + 1 >= setting.getModes().length) continue;
                            offset += font2.getHeight(mode) + 1.0f;
                        }
                        rectHeight += offset;
                    } else {
                        float finalRectHeight1 = rectHeight;
                        Shadow.drawGlow(() -> font2.drawString(setting.getCurrentMode(), minX + 1.0f, nameY + finalRectHeight1 / 2.0f - font2.getHeight(setting.getCurrentMode()) / 2.0f, Color.WHITE.getRGB()), (boolean)false);
                        font2.drawString(setting.getCurrentMode(), minX + 1.0f, nameY + rectHeight / 2.0f - font2.getHeight(setting.getCurrentMode()) / 2.0f, Color.WHITE.getRGB());
                    }
                }
                height = Math.max((float)height, (float)rectHeight);
                break;
            }
        }
        return height;
    }

    public static String round(double v, int places) {
        double f = Math.pow((double)10.0, (double)places);
        return String.valueOf((double)((double)Math.round((double)(v * f)) / f));
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(this.mc);
        if (!this.mouseOver(mouseX, mouseY, (int)((float)sr.getScaledWidth() / 2.0f - 95.0f), 180, 190, 150) && mouseButton == 0) {
            this.mc.displayGuiScreen((GuiScreen)new ClickguiScreen(null));
        } else {
            ArrayList settings = Aqua.setmgr.getSettingsFromModule(this.module);
            for (Setting setting : settings) {
                if (setting.isHovered()) {
                    switch (1.$SwitchMap$de$Hero$settings$Setting$Type[setting.type.ordinal()]) {
                        case 1: {
                            setting.setState(!setting.isState());
                            break;
                        }
                        case 4: {
                            setting.setComboExtended(!setting.isComboExtended());
                            break;
                        }
                        case 3: {
                            setting.color = this.colorChooser2.color;
                        }
                    }
                }
                if (setting.type != Setting.Type.STRING || !setting.isComboExtended() || setting.getComboHoverIndex() < 0 || setting.getModes() == null) continue;
                setting.setCurrentMode(setting.getModes()[setting.getComboHoverIndex()]);
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (clickedMouseButton == 0) {
            ArrayList settings = Aqua.setmgr.getSettingsFromModule(this.module);
            for (Setting setting : settings) {
                if (!setting.isHovered()) continue;
                switch (1.$SwitchMap$de$Hero$settings$Setting$Type[setting.type.ordinal()]) {
                    case 1: {
                        setting.setState(!setting.isState());
                        break;
                    }
                    case 2: {
                        double percentage = (double)((float)mouseX - setting.getSliderMinX()) / (double)(setting.getSliderMaxX() - setting.getSliderMinX());
                        setting.setCurrentNumber(setting.getMin() + (setting.getMax() - setting.getMin()) * percentage);
                    }
                }
            }
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(this.parent);
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException {
    }

    private boolean mouseOver(int x, int y, int modX, int modY, int modWidth, int modHeight) {
        return x >= modX && x <= modX + modWidth && y >= modY && y <= modY + modHeight;
    }

    public int getX() {
        return this.x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return this.y;
    }
}
