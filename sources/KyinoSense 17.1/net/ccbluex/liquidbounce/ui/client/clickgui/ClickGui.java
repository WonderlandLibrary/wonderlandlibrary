/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.ui.client.clickgui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.ccbluex.liquidbounce.LiquidBounce;
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
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class ClickGui
extends GuiScreen {
    public final List<Panel> panels = new ArrayList<Panel>();
    private final ResourceLocation hudIcon = new ResourceLocation("LiquidBounce".toLowerCase() + "/custom_hud_icon.png");
    public Style style = new SlowlyStyle();
    private Panel clickedPanel;
    private int mouseX;
    private int mouseY;

    public ClickGui() {
        int width = 100;
        int height = 18;
        int yPos = 5;
        for (final ModuleCategory category : ModuleCategory.values()) {
            this.panels.add(new Panel(category.name(), 100, yPos, 100, 18, false){

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
                            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
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
                            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
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
                            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
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
                            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
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
                            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
                        }
                    }
                });
            }
        });
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        if (Mouse.isButtonDown((int)0) && mouseX >= 5 && mouseX <= 50 && mouseY <= this.field_146295_m - 5 && mouseY >= this.field_146295_m - 50) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiHudDesigner());
        }
        AWTFontRenderer.Companion.setAssumeNonVolatile(true);
        ClickGUI cfr_ignored_0 = (ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class));
        double scale = ((Float)ClickGUI.scaleValue.get()).floatValue();
        mouseX = (int)((double)mouseX / scale);
        mouseY = (int)((double)mouseY / scale);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.func_146276_q_();
        RenderUtils.drawImage(this.hudIcon, 9, this.field_146295_m - 41, 32, 32);
        GlStateManager.func_179139_a((double)scale, (double)scale, (double)scale);
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
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            for (int i = this.panels.size() - 1; i >= 0 && !this.panels.get(i).handleScroll(mouseX, mouseY, wheel); --i) {
            }
        }
        GlStateManager.func_179140_f();
        RenderHelper.func_74518_a();
        GlStateManager.func_179152_a((float)1.0f, (float)1.0f, (float)1.0f);
        AWTFontRenderer.Companion.setAssumeNonVolatile(false);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        ClickGUI cfr_ignored_0 = (ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class));
        double scale = ((Float)ClickGUI.scaleValue.get()).floatValue();
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
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    protected void func_146286_b(int mouseX, int mouseY, int state) {
        ClickGUI cfr_ignored_0 = (ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class));
        double scale = ((Float)ClickGUI.scaleValue.get()).floatValue();
        mouseX = (int)((double)mouseX / scale);
        mouseY = (int)((double)mouseY / scale);
        for (Panel panel : this.panels) {
            panel.mouseReleased(mouseX, mouseY, state);
        }
        super.func_146286_b(mouseX, mouseY, state);
    }

    public void func_73876_c() {
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
        super.func_73876_c();
    }

    public void func_146281_b() {
        LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.clickGuiConfig);
    }

    public boolean func_73868_f() {
        return false;
    }
}

