/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.ui.client.clickgui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jx.utils.EaseUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.Panel;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ButtonElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.Element;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ModuleElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.Style;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.SlowlyStyle;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.input.Mouse;

public class ClickGui
extends WrappedGuiScreen {
    public final List<Panel> panels = new ArrayList<Panel>();
    private final IResourceLocation hudIcon = classProvider.createResourceLocation("liquidbounce/custom_hud_icon.png");
    public Style style = new SlowlyStyle();
    private Panel clickedPanel;
    private int mouseX;
    private int mouseY;
    private double slide;
    private double progress = 0.0;

    public ClickGui() {
        int width = 100;
        int height = 18;
        int yPos = 5;
        for (final ModuleCategory category : ModuleCategory.values()) {
            this.panels.add(new Panel(category.getDisplayName(), 100, yPos, 100, 18, false){

                @Override
                public void setupItems() {
                    for (Module module : LiquidBounce.moduleManager.getModules()) {
                        if (module.getCategory() != category) continue;
                        this.getElements().add(new ModuleElement(module));
                    }
                }
            });
            yPos += 20;
        }
        this.panels.add(new Panel("Targets", 100, yPos += 20, 100, 18, false){

            @Override
            public void setupItems() {
                this.getElements().add(new ButtonElement("Players"){

                    @Override
                    public void createButton(String displayName) {
                        this.color = EntityUtils.targetPlayer ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        super.createButton(displayName);
                    }

                    @Override
                    public String getDisplayName() {
                        this.displayName = "Players";
                        this.color = EntityUtils.targetPlayer ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        return super.getDisplayName();
                    }

                    @Override
                    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                        if (mouseButton == 0 && this.isHovering(mouseX, mouseY) && this.isVisible()) {
                            EntityUtils.targetPlayer = !EntityUtils.targetPlayer;
                            this.displayName = "Players";
                            this.color = EntityUtils.targetPlayer ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                    }
                });
                this.getElements().add(new ButtonElement("Mobs"){

                    @Override
                    public void createButton(String displayName) {
                        this.color = EntityUtils.targetMobs ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        super.createButton(displayName);
                    }

                    @Override
                    public String getDisplayName() {
                        this.displayName = "Mobs";
                        this.color = EntityUtils.targetMobs ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        return super.getDisplayName();
                    }

                    @Override
                    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                        if (mouseButton == 0 && this.isHovering(mouseX, mouseY) && this.isVisible()) {
                            EntityUtils.targetMobs = !EntityUtils.targetMobs;
                            this.displayName = "Mobs";
                            this.color = EntityUtils.targetMobs ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                    }
                });
                this.getElements().add(new ButtonElement("Animals"){

                    @Override
                    public void createButton(String displayName) {
                        this.color = EntityUtils.targetAnimals ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        super.createButton(displayName);
                    }

                    @Override
                    public String getDisplayName() {
                        this.displayName = "Animals";
                        this.color = EntityUtils.targetAnimals ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        return super.getDisplayName();
                    }

                    @Override
                    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                        if (mouseButton == 0 && this.isHovering(mouseX, mouseY) && this.isVisible()) {
                            EntityUtils.targetAnimals = !EntityUtils.targetAnimals;
                            this.displayName = "Animals";
                            this.color = EntityUtils.targetAnimals ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                    }
                });
                this.getElements().add(new ButtonElement("Invisible"){

                    @Override
                    public void createButton(String displayName) {
                        this.color = EntityUtils.targetInvisible ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        super.createButton(displayName);
                    }

                    @Override
                    public String getDisplayName() {
                        this.displayName = "Invisible";
                        this.color = EntityUtils.targetInvisible ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        return super.getDisplayName();
                    }

                    @Override
                    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                        if (mouseButton == 0 && this.isHovering(mouseX, mouseY) && this.isVisible()) {
                            EntityUtils.targetInvisible = !EntityUtils.targetInvisible;
                            this.displayName = "Invisible";
                            this.color = EntityUtils.targetInvisible ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                    }
                });
                this.getElements().add(new ButtonElement("Dead"){

                    @Override
                    public void createButton(String displayName) {
                        this.color = EntityUtils.targetDead ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        super.createButton(displayName);
                    }

                    @Override
                    public String getDisplayName() {
                        this.displayName = "Dead";
                        this.color = EntityUtils.targetDead ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        return super.getDisplayName();
                    }

                    @Override
                    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                        if (mouseButton == 0 && this.isHovering(mouseX, mouseY) && this.isVisible()) {
                            EntityUtils.targetDead = !EntityUtils.targetDead;
                            this.displayName = "Dead";
                            this.color = EntityUtils.targetDead ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void initGui() {
        this.progress = 0.0;
        this.slide = 0.0;
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.progress = this.progress < 1.0 ? (this.progress += 0.1 * (double)(1.0f - partialTicks)) : 1.0;
        switch (((String)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).animationValue.get()).toLowerCase()) {
            case "slidebounce": 
            case "zoombounce": {
                this.slide = EaseUtils.easeOutBack(this.progress);
                break;
            }
            case "slide": 
            case "zoom": 
            case "azura": {
                this.slide = EaseUtils.easeOutQuart(this.progress);
                break;
            }
            case "none": {
                this.slide = 1.0;
            }
        }
        if (Mouse.isButtonDown((int)0) && mouseX >= 5 && mouseX <= 50 && mouseY <= this.representedScreen.getHeight() - 5 && mouseY >= this.representedScreen.getHeight() - 50) {
            mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiHudDesigner()));
        }
        AWTFontRenderer.Companion.setAssumeNonVolatile(true);
        double scale = ((Float)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).scaleValue.get()).floatValue();
        mouseX = (int)((double)mouseX / scale);
        mouseY = (int)((double)mouseY / scale);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        switch ((String)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).backgroundValue.get()) {
            case "Default": {
                this.representedScreen.drawDefaultBackground();
            }
        }
        GlStateManager.func_179118_c();
        RenderUtils.drawImage(this.hudIcon, 9, this.representedScreen.getHeight() - 41, 32, 32);
        GlStateManager.func_179141_d();
        switch (((String)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).animationValue.get()).toLowerCase()) {
            case "azura": {
                GlStateManager.func_179137_b((double)0.0, (double)((1.0 - this.slide) * (double)this.representedScreen.getHeight() * 2.0), (double)0.0);
                GlStateManager.func_179139_a((double)scale, (double)(scale + (1.0 - this.slide) * 2.0), (double)scale);
                break;
            }
            case "slide": 
            case "slidebounce": {
                GlStateManager.func_179137_b((double)0.0, (double)((1.0 - this.slide) * (double)this.representedScreen.getHeight() * 2.0), (double)0.0);
                GlStateManager.func_179139_a((double)scale, (double)scale, (double)scale);
                break;
            }
            case "zoom": {
                GlStateManager.func_179137_b((double)((1.0 - this.slide) * ((double)this.representedScreen.getWidth() / 2.0)), (double)((1.0 - this.slide) * ((double)this.representedScreen.getHeight() / 2.0)), (double)((1.0 - this.slide) * ((double)this.representedScreen.getWidth() / 2.0)));
                GlStateManager.func_179139_a((double)(scale * this.slide), (double)(scale * this.slide), (double)(scale * this.slide));
                break;
            }
            case "zoombounce": {
                GlStateManager.func_179137_b((double)((1.0 - this.slide) * ((double)this.representedScreen.getWidth() / 2.0)), (double)((1.0 - this.slide) * ((double)this.representedScreen.getHeight() / 2.0)), (double)0.0);
                GlStateManager.func_179139_a((double)(scale * this.slide), (double)(scale * this.slide), (double)(scale * this.slide));
                break;
            }
            case "none": {
                GlStateManager.func_179139_a((double)scale, (double)scale, (double)scale);
            }
        }
        for (Panel panel : this.panels) {
            panel.updateFade(RenderUtils.deltaTime);
            panel.drawScreen(mouseX, mouseY, partialTicks);
        }
        for (Panel panel : this.panels) {
            for (Element element : panel.getElements()) {
                if (!(element instanceof ModuleElement)) continue;
                ModuleElement moduleElement = (ModuleElement)element;
                if (mouseX == 0 || mouseY == 0 || !moduleElement.isHovering(mouseX, mouseY) || !moduleElement.isVisible() || element.getY() > panel.getY() + panel.getFade()) continue;
                this.style.drawDescription(mouseX, mouseY, moduleElement.getModule().getDescription());
            }
        }
        GlStateManager.func_179140_f();
        RenderHelper.func_74518_a();
        GlStateManager.func_179152_a((float)1.0f, (float)1.0f, (float)1.0f);
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            boolean handledScroll = false;
            for (int i = this.panels.size() - 1; i >= 0; --i) {
                if (!this.panels.get(i).handleScroll(mouseX, mouseY, wheel)) continue;
                handledScroll = true;
                break;
            }
            if (!handledScroll) {
                this.hand(wheel);
            }
        }
        switch (((String)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).animationValue.get()).toLowerCase()) {
            case "azura": {
                GlStateManager.func_179137_b((double)0.0, (double)((1.0 - this.slide) * (double)this.representedScreen.getHeight() * -2.0), (double)0.0);
                break;
            }
            case "slide": 
            case "slidebounce": {
                GlStateManager.func_179137_b((double)0.0, (double)((1.0 - this.slide) * (double)this.representedScreen.getHeight() * -2.0), (double)0.0);
                break;
            }
            case "zoom": {
                GlStateManager.func_179137_b((double)(-1.0 * (1.0 - this.slide) * ((double)this.representedScreen.getWidth() / 2.0)), (double)(-1.0 * (1.0 - this.slide) * ((double)this.representedScreen.getHeight() / 2.0)), (double)(-1.0 * (1.0 - this.slide) * ((double)this.representedScreen.getWidth() / 2.0)));
                break;
            }
            case "zoombounce": {
                GlStateManager.func_179137_b((double)(-1.0 * (1.0 - this.slide) * ((double)this.representedScreen.getWidth() / 2.0)), (double)(-1.0 * (1.0 - this.slide) * ((double)this.representedScreen.getHeight() / 2.0)), (double)0.0);
            }
        }
        GlStateManager.func_179152_a((float)1.0f, (float)1.0f, (float)1.0f);
        AWTFontRenderer.Companion.setAssumeNonVolatile(false);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void hand(int wheel) {
        if (wheel == 0) {
            return;
        }
        for (Panel panel : this.panels) {
            panel.setY(panel.getY() + wheel);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        double scale = ((Float)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).scaleValue.get()).floatValue();
        mouseX = (int)((double)mouseX / scale);
        mouseY = (int)((double)mouseY / scale);
        for (Panel panel : this.panels) {
            panel.mouseClicked(mouseX, mouseY, mouseButton);
            panel.drag = false;
            if (mouseButton != 0 || !panel.isHovering(mouseX, mouseY)) continue;
            this.clickedPanel = panel;
        }
        if (this.clickedPanel != null) {
            this.clickedPanel.x2 = this.clickedPanel.x - mouseX;
            this.clickedPanel.y2 = this.clickedPanel.y - mouseY;
            this.clickedPanel.drag = true;
            this.panels.remove(this.clickedPanel);
            this.panels.add(this.clickedPanel);
            this.clickedPanel = null;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        double scale = ((Float)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).scaleValue.get()).floatValue();
        mouseX = (int)((double)mouseX / scale);
        mouseY = (int)((double)mouseY / scale);
        for (Panel panel : this.panels) {
            panel.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void updateScreen() {
        for (Panel panel : this.panels) {
            for (Element element : panel.getElements()) {
                if (element instanceof ButtonElement) {
                    ButtonElement buttonElement = (ButtonElement)element;
                    if (buttonElement.isHovering(this.mouseX, this.mouseY)) {
                        if (buttonElement.hoverTime < 7) {
                            ++buttonElement.hoverTime;
                        }
                    } else if (buttonElement.hoverTime > 0) {
                        --buttonElement.hoverTime;
                    }
                }
                if (!(element instanceof ModuleElement)) continue;
                if (((ModuleElement)element).getModule().getState()) {
                    if (((ModuleElement)element).slowlyFade < 255) {
                        ((ModuleElement)element).slowlyFade += 20;
                    }
                } else if (((ModuleElement)element).slowlyFade > 0) {
                    ((ModuleElement)element).slowlyFade -= 20;
                }
                if (((ModuleElement)element).slowlyFade > 255) {
                    ((ModuleElement)element).slowlyFade = 255;
                }
                if (((ModuleElement)element).slowlyFade >= 0) continue;
                ((ModuleElement)element).slowlyFade = 0;
            }
        }
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.clickGuiConfig);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

