package vika.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import vika.Vika;
import vika.clickgui.comp.CheckBox;
import vika.clickgui.comp.Combo;
import vika.clickgui.comp.Comp;
import vika.clickgui.comp.Slider;
import vika.clickgui.setting.Setting;
import vika.features.Feature;
import vika.features.FeatureCategory;
import vika.utils.render.RoundedUtils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Clickgui extends GuiScreen {

    public double posX, posY, width, height, dragX, dragY;
    public boolean dragging;
    public FeatureCategory selectedCategory;

    private Feature selectedModule;
    public int modeIndex;

    public ArrayList<Comp> comps = new ArrayList<>();

    public Clickgui() {
        dragging = false;
        posX = getScaledRes().getScaledWidth() / 2 - 150;
        posY = getScaledRes().getScaledHeight() / 2 - 100;
        width = posX + 150 * 2;
        height = height + 200;
        selectedCategory = FeatureCategory.Combat;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (dragging) {
            posX = mouseX - dragX;
            posY = mouseY - dragY;
        }
        width = posX + 150 * 2;
        height = posY + 200;
        RoundedUtils.drawRoundedRectangle(posX, posY - 10, width, posY, 5, new Color(0, 178, 255).getRGB());
        Gui.drawRect(posX, posY, width, height, new Color(45,45,45).getRGB());
        Gui.draw2dLine(posX + 65, posY, posX + 65, height, 2, new Color(153, 153, 153));



        int offset = 0;
        int textX = (int)(posX + 32) + offset;
        int textY = (int)(posY - 8);
        for (FeatureCategory category : FeatureCategory.values()) {
            fontRendererObj.drawString(category.name(), (textX) + offset, textY, category.equals(selectedCategory) ? new Color(255, 255, 255).getRGB() : new Color(153, 153, 153).getRGB());
            offset += 55;
        }
        offset = 0;
        for (Feature m : Vika.INSTANCE.featureManager.getModulesByCategory(selectedCategory)) {
            fontRendererObj.drawString(m.getName(),(int)posX + 2, (int)(posY + 5) + offset, m.isEnabled() ? new Color(255, 255, 255).getRGB() : new Color(153, 153, 153).getRGB());
            offset += 15;
        }

        for (Comp comp : comps) {
            comp.drawScreen(mouseX, mouseY);
        }

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (Comp comp : comps) {
            comp.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isInside(mouseX, mouseY, posX, posY - 10, width, posY) && mouseButton == 0) {
            dragging = true;
            dragX = mouseX - posX;
            dragY = mouseY - posY;
        }
        int offset = 0;
        int textY = (int)(posY - 8);
        for (FeatureCategory category : FeatureCategory.values()) {
            int textX = (int)(posX + 32) + offset;
            if (isInside(mouseX, mouseY, textX, textY, textX + fontRendererObj.getStringWidth(category.name()), textY + fontRendererObj.FONT_HEIGHT) && mouseButton == 0) {
                selectedCategory = category;
            }
            offset += 55;
        }
        offset = 0;
        for (Feature m : Vika.INSTANCE.featureManager.getModulesByCategory(selectedCategory)) {
            if (isInside(mouseX, mouseY,posX,posY + 1 + offset,posX + 60,posY + 15 + offset)) {
                if (mouseButton == 0) {
                    m.toggle();
                }
                if (mouseButton == 1) {
                    int sOffset = 3;
                    comps.clear();
                    if (Vika.INSTANCE.settingsManager.getSettingsByMod(m) != null)
                        for (Setting setting : Vika.INSTANCE.settingsManager.getSettingsByMod(m)) {
                            selectedModule = m;
                            if (setting.isCombo()) {
                                comps.add(new Combo(150, sOffset, this, selectedModule, setting));
                                sOffset += 15;
                            }
                            if (setting.isCheck()) {
                                comps.add(new CheckBox(150, sOffset, this, selectedModule, setting));
                                sOffset += 15;
                            }
                            if (setting.isSlider()) {
                                comps.add(new Slider(150, sOffset, this, selectedModule, setting));
                                sOffset += 25;
                            }
                        }
                }
            }
            offset += 15;
        }
        for (Comp comp : comps) {
            comp.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
        for (Comp comp : comps) {
            comp.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        dragging = false;

        if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
            if (mc.entityRenderer.theShaderGroup != null) {
                mc.entityRenderer.theShaderGroup.deleteShaderGroup();
            }
            mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }

    @Override
    public void onGuiClosed(){
        if (mc.entityRenderer.theShaderGroup != null) {
            mc.entityRenderer.theShaderGroup.deleteShaderGroup();
            mc.entityRenderer.theShaderGroup = null;
        }
    }

    public boolean isInside(int mouseX, int mouseY, double x, double y, double x2, double y2) {
        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }

    public ScaledResolution getScaledRes() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }
}