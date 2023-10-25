package org.dreamcore.client.ui.newclickgui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.feature.impl.hud.ClickGui;
import org.dreamcore.client.helpers.Helper;
import org.dreamcore.client.helpers.Scrollbar;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.helpers.render.ScreenHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.ui.button.ImageButton;
import org.dreamcore.client.ui.clickgui.GuiSearcher;
import org.dreamcore.client.ui.newclickgui.settings.Component;
import org.dreamcore.client.ui.newclickgui.settings.KeybindButton;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickGuiScreen extends GuiScreen {

    public static GuiSearcher search;
    public ArrayList<Panel> panels = new ArrayList<>();
    public Theme hud = new Theme();
    public boolean exit = false;
    public boolean usingSetting = false;
    public int x = 20;
    public int y = 80;
    public ScreenHelper screenHelper;
    public ScreenHelper screenHelperSetting;
    public Scrollbar scrollbar = new Scrollbar();
    protected ArrayList<ImageButton> imageButtons = new ArrayList<>();

    public ClickGuiScreen() {
        for (Type modCategory : Type.values()) {
            panels.add(new Panel(modCategory, x, y));
            y += height + 30;
        }
        this.screenHelper = new ScreenHelper(0, 0);
        this.screenHelperSetting = new ScreenHelper(0, 0);

    }

    @Override
    public void onGuiClosed() {
        mc.entityRenderer.theShaderGroup = null;
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        ScaledResolution sr = new ScaledResolution(mc);

        if (exit) {

        } else {
            if (!usingSetting)
                screenHelper.interpolate(sr.getScaledWidth(), sr.getScaledHeight(), 6);
        }


        Color color = Color.WHITE;
        Color onecolor = new Color(ClickGui.color.getColorValue());
        Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
        double speed = ClickGui.speed.getNumberValue();
        switch (ClickGui.clickGuiColor.currentMode) {
            case "Client":
                color = ClientHelper.getClientColor();
                break;
            case "Fade":
                color = new Color(ClickGui.color.getColorValue());
                break;
            case "Astolfo":
                color = PaletteHelper.astolfo(true, width);
                break;
            case "Color Two":
                color = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + height * 6L / 60 * 2) % 2) - 1)));
                break;
            case "Rainbow":
                color = PaletteHelper.rainbow(300, 1, 1);
                break;
            case "Static":
                color = onecolor;
                break;
        }
        Color color1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
        if (ClickGui.background.getBoolValue()) {
            drawDefaultBackground();
            drawGradientRect(0, 0, width, height, color1.getRGB(), color1.brighter().getRGB());
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(sr.getScaledWidth() / 2F, sr.getScaledHeight() / 2F, 0);
        GL11.glScaled(screenHelper.getX() / sr.getScaledWidth(), screenHelper.getY() / sr.getScaledHeight(), 1);
        GL11.glTranslatef(-sr.getScaledWidth() / 2F, -sr.getScaledHeight() / 2F, 0);

        for (Panel panel : panels) {
            if (!usingSetting) {
                updateMouseWheel();
            }
            panel.drawScreen(mouseX, mouseY);
        }

        search.drawTextBox();

        RectHelper.drawGradientRect(width - mc.fontRenderer.getStringWidth("Search Feature...") - 90, sr.getScaledHeight() - mc.fontRendererObj.FONT_HEIGHT - 92, sr.getScaledWidth() - 10, sr.getScaledHeight() - mc.fontRendererObj.FONT_HEIGHT - 90, color.getRGB(), color.darker().getRGB());

        if (search.getText().isEmpty() && !search.isFocused()) {
            mc.fontRenderer.drawStringWithShadow("Search Feature...", width - mc.fontRenderer.getStringWidth("Search Feature...") - 50, sr.getScaledHeight() - mc.fontRendererObj.FONT_HEIGHT - 84, -1);
        }

        for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (Mouse.isButtonDown(0)) {
                imageButton.onClick(mouseX, mouseY);
            }
        }


        GL11.glPopMatrix();
        if (usingSetting) {
            for (Panel panel : panels) {

                for (FeaturePanel featurePanel : panel.featurePanel) {
                    if (featurePanel.usingSettings) {
                        screenHelperSetting.interpolate(sr.getScaledWidth(), sr.getScaledHeight(), 8);

                        GlStateManager.pushMatrix();
                        GlStateManager.translate(sr.getScaledWidth() / 2F, sr.getScaledHeight() / 2F, 0);
                        GlStateManager.scale(screenHelperSetting.getX() / sr.getScaledWidth(), screenHelperSetting.getY() / sr.getScaledHeight(), 1);
                        GlStateManager.translate((float) (-sr.getScaledWidth()) / 2, (float) (-sr.getScaledHeight()) / 2.0f, 0.0f);

                        RectHelper.drawRoundedRect(sr.getScaledWidth() / 2F - 151, 49, 150 * 2 + 2, sr.getScaledHeight() - 98, 4, new Color(48, 48, 48, 255));
                        RectHelper.drawRoundedRect(sr.getScaledWidth() / 2F - 150, 50, 150 * 2, sr.getScaledHeight() - 100, 4, new Color(17, 17, 17));

                        RectHelper.drawRoundedRect(sr.getScaledWidth() / 2 - 130, 74, 131 * 2, 1, 0, new Color(49, 48, 48));

                        Helper.mc.circleregular.drawStringWithOutline(featurePanel.feature.getLabel() + " Settings", sr.getScaledWidth() / 2F - 37, 56, Color.gray.getRGB());

                        GlStateManager.pushMatrix();
                        GlStateManager.enable(GL11.GL_SCISSOR_TEST);
                        RenderHelper.scissorRect(sr.getScaledWidth() / 2F - 150, 76, sr.getScaledWidth() / 2F + 155, sr.getScaledHeight() - 47);

                        if (featurePanel.yOffset + 25 > sr.getScaledHeight() - 150) {
                            scrollbar.setInformation(sr.getScaledWidth() / 2F - 147, 76, (sr.getScaledHeight() - 53) - 125, featurePanel.scrollY, featurePanel.yOffset - sr.getScaledHeight() / 2F - 125, 0);
                            scrollbar.drawScrollBar();
                        }

                        for (Component component : featurePanel.components) {
                            if (component.setting.isVisible()) {
                                component.drawScreen(mouseX, mouseY);
                            }
                        }

                        for (KeybindButton keybindButton : featurePanel.keybindButtons) {
                            keybindButton.drawScreen();
                        }

                        GlStateManager.disable(GL11.GL_SCISSOR_TEST);
                        GlStateManager.popMatrix();

                        GlStateManager.popMatrix();
                    }
                }
            }
        } else {
            this.screenHelperSetting = new ScreenHelper(0, 0);
        }
        if (exit) {
            screenHelper.interpolate(0, 0, 2);
            if (screenHelper.getY() < 300) {
                exit = false;
                mc.displayGuiScreen(null);
                if (mc.currentScreen == null) {
                    mc.setIngameFocus();
                }
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void updateMouseWheel() {
        int scrollWheel = Mouse.getDWheel();
        for (Panel panel : panels) {
            if (scrollWheel > 0) {
                panel.setY(panel.getY() + 15);
            }
            if (scrollWheel < 0) {
                panel.setY(panel.getY() - 15);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        search.setFocused(false);
        search.setText("");
        search.mouseClicked(mouseX, mouseY, mouseButton);

        ScaledResolution sr = new ScaledResolution(mc);

        for (Panel panel : panels) {
            if (panel.isWithinHeader(mouseX, mouseY) && mouseButton == 1 && !usingSetting) {
                panel.setOpen(!panel.isOpen());
            }
            panel.mouseClicked(mouseX, mouseY, mouseButton);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (Panel panel : panels) {
            panel.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (Panel panel : panels) {
            for (FeaturePanel featurePanel : panel.featurePanel) {
                if (usingSetting && featurePanel.usingSettings) {
                    featurePanel.keyTyped(typedChar, keyCode);
                    return;
                }
            }
        }

        search.textboxKeyTyped(typedChar, keyCode);

        if ((typedChar == '\t' || typedChar == '\r') && search.isFocused()) {
            search.setFocused(!search.isFocused());
        }


        if (keyCode == 1 && !usingSetting) {
            exit = true;
        }

        if (exit)
            return;

        super.keyTyped(typedChar, keyCode);

    }

    @Override
    public void initGui() {
        this.screenHelper = new ScreenHelper(0, 0);

        ScaledResolution sr = new ScaledResolution(mc);
        this.imageButtons.clear();
        this.imageButtons.add(new ImageButton(new ResourceLocation("dreamcore/brush.png"), (sr.getScaledWidth() - (mc.fontRendererObj.getStringWidth("Welcome"))) - 70, sr.getScaledHeight() - mc.fontRendererObj.FONT_HEIGHT - 50, 40, 40, "", 18));
        this.imageButtons.add(new ImageButton(new ResourceLocation("dreamcore/config.png"), (sr.getScaledWidth() - (mc.fontRendererObj.getStringWidth("Welcome"))) - 19, sr.getScaledHeight() - mc.fontRendererObj.FONT_HEIGHT - 50, 40, 40, "", 22));
        this.imageButtons.add(new ImageButton(new ResourceLocation("dreamcore/capeicon.png"), (sr.getScaledWidth() - (mc.fontRendererObj.getStringWidth("Welcome"))) - 115, sr.getScaledHeight() - mc.fontRendererObj.FONT_HEIGHT - 50, 40, 40, "", 23));
        search = new GuiSearcher(1337, mc.fontRendererObj, width - mc.fontRenderer.getStringWidth("Search Feature...") - 90, sr.getScaledHeight() - mc.fontRendererObj.FONT_HEIGHT - 90, 150, 18);

        if (ClickGui.blur.getBoolValue()) {
            mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }

        super.initGui();
    }
}
