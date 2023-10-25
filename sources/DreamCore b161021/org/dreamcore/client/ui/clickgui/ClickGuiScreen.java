package org.dreamcore.client.ui.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.feature.impl.hud.ClickGui;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.ScreenHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.ui.button.ImageButton;
import org.dreamcore.client.ui.clickgui.component.Component;
import org.dreamcore.client.ui.clickgui.component.ExpandableComponent;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends GuiScreen {

    public static boolean escapeKeyInUse;
    public static GuiSearcher search;
    public List<Panel> components = new ArrayList<>();
    public ScreenHelper screenHelper;
    public boolean exit = false;
    public Type type;
    protected ArrayList<ImageButton> imageButtons = new ArrayList<>();
    private Component selectedPanel;

    public ClickGuiScreen() {
        int height = 20;
        int x = 20;
        int y = 80;
        for (Type type : Type.values()) {
            this.type = type;
            this.components.add(new Panel(type, x, y));
            selectedPanel = new Panel(type, x, y);
            y += height + 6;
        }
        this.screenHelper = new ScreenHelper(0, 0);
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

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        ScaledResolution sr = new ScaledResolution(mc);
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
            case "Category":
                color = new Color(type.getColor());
                break;
            case "Static":
                color = onecolor;
                break;
        }
        Color color1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
        if (ClickGui.background.getBoolValue()) {
            this.drawDefaultBackground();
            drawGradientRect(0, 0, width, height, color1.getRGB(), color1.brighter().getRGB());
        }

        if (exit) {

        } else {
            screenHelper.interpolate(width, height, 6 * Minecraft.frameTime / 6);
        }

        GlStateManager.pushMatrix();
        GL11.glTranslatef(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
        GL11.glScaled(screenHelper.getX() / sr.getScaledWidth(), screenHelper.getY() / sr.getScaledHeight(), 1);
        GL11.glTranslatef((float) (-sr.getScaledWidth()) / 2.0f, (float) (-sr.getScaledHeight()) / 2.0f, 0.0f);

        for (Panel panel : components) {
            panel.drawComponent(sr, mouseX, mouseY);
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
        GlStateManager.popMatrix();

        updateMouseWheel();

        if (exit) {
            screenHelper.interpolate(0, 0, 2);
            if (screenHelper.getY() < 200) {
                exit = false;
                this.mc.displayGuiScreen(null);
                if (this.mc.currentScreen == null) {
                    this.mc.setIngameFocus();
                }
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void updateMouseWheel() {
        int scrollWheel = Mouse.getDWheel();
        for (Component panel : components) {
            if (scrollWheel > 0) {
                panel.setY(panel.getY() + 15);
            }
            if (scrollWheel < 0) {
                panel.setY(panel.getY() - 15);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1)
            exit = true;

        if (exit)
            return;

        selectedPanel.onKeyPress(keyCode);

        if (!escapeKeyInUse) {
            super.keyTyped(typedChar, keyCode);
        }
        search.textboxKeyTyped(typedChar, keyCode);

        if ((typedChar == '\t' || typedChar == '\r') && search.isFocused()) {
            search.setFocused(!search.isFocused());
        }

        escapeKeyInUse = false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        search.setFocused(false);
        search.setText("");
        search.mouseClicked(mouseX, mouseY, mouseButton);

        for (Component component : components) {
            int x = component.getX();
            int y = component.getY();
            int cHeight = component.getHeight();
            if (component instanceof ExpandableComponent) {
                ExpandableComponent expandableComponent = (ExpandableComponent) component;
                if (expandableComponent.isExpanded())
                    cHeight = expandableComponent.getHeightWithExpand();
            }
            if (mouseX > x && mouseY > y && mouseX < x + component.getWidth() && mouseY < y + cHeight) {
                selectedPanel = component;
                component.onMouseClick(mouseX, mouseY, mouseButton);
                break;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        selectedPanel.onMouseRelease(state);
    }

    @Override
    public void onGuiClosed() {
        this.screenHelper = new ScreenHelper(0, 0);
        mc.entityRenderer.theShaderGroup = null;
        super.onGuiClosed();
    }
}
