/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.clickgui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ClickGui
extends WrappedGuiScreen {
    private Panel clickedPanel;
    private int scroll;
    private int mouseY;
    private int mouseX;
    private final IResourceLocation hudIcon;
    public final List panels = new ArrayList();
    public Style style;

    @Override
    public void mouseClicked(int n, int n2, int n3) throws IOException {
        double d = ((Float)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).scaleValue.get()).floatValue();
        n2 -= this.scroll;
        n = (int)((double)n / d);
        n2 = (int)((double)n2 / d);
        for (Panel panel : this.panels) {
            panel.mouseClicked(n, n2, n3);
            panel.drag = false;
            if (n3 != 0 || !panel.isHovering(n, n2)) continue;
            this.clickedPanel = panel;
        }
        if (this.clickedPanel != null) {
            this.clickedPanel.x2 = this.clickedPanel.x - n;
            this.clickedPanel.y2 = this.clickedPanel.y - n2;
            this.clickedPanel.drag = true;
            this.panels.remove(this.clickedPanel);
            this.panels.add(this.clickedPanel);
            this.clickedPanel = null;
        }
        super.mouseClicked(n, n2, n3);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        if (Mouse.isButtonDown((int)0) && n >= 5 && n <= 50 && n2 <= this.representedScreen.getHeight() - 5 && n2 >= this.representedScreen.getHeight() - 50) {
            mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiHudDesigner()));
        }
        AWTFontRenderer.Companion.setAssumeNonVolatile(true);
        double d = ((Float)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).scaleValue.get()).floatValue();
        GlStateManager.func_179109_b((float)0.0f, (float)this.scroll, (float)0.0f);
        n2 -= this.scroll;
        n = (int)((double)n / d);
        n2 = (int)((double)n2 / d);
        this.mouseX = n;
        this.mouseY = n2;
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        RenderUtils.drawRect(0.0f, 0.0f, (float)scaledResolution.func_78326_a(), (float)scaledResolution.func_78328_b(), new Color(0, 0, 0, 150));
        RenderUtils.drawImage(this.hudIcon, 9, this.representedScreen.getHeight() - 41, 32, 32);
        GL11.glScaled((double)d, (double)d, (double)d);
        for (Panel panel : this.panels) {
            panel.updateFade(RenderUtils.deltaTime);
            panel.drawScreen(n, n2, f);
        }
        for (Panel panel : this.panels) {
            for (Element element : panel.getElements()) {
                if (!(element instanceof ModuleElement)) continue;
                ModuleElement moduleElement = (ModuleElement)element;
                if (n == 0 || n2 == 0 || !moduleElement.isHovering(n, n2) || !moduleElement.isVisible() || element.getY() > panel.getY() + panel.getFade()) continue;
                this.style.drawDescription(n, n2, moduleElement.getModule().getDescription());
            }
        }
        if (Mouse.hasWheel()) {
            int n3 = Mouse.getDWheel();
            for (int i = this.panels.size() - 1; i >= 0 && !((Panel)this.panels.get(i)).handleScroll(n, n2, n3); --i) {
            }
            if (n3 < 0) {
                this.scroll -= 15;
            } else if (n3 > 0) {
                this.scroll += 15;
                if (this.scroll > 0) {
                    this.scroll = 0;
                }
            }
        }
        classProvider.getGlStateManager().disableLighting();
        functions.disableStandardItemLighting();
        GL11.glScaled((double)1.0, (double)1.0, (double)1.0);
        AWTFontRenderer.Companion.setAssumeNonVolatile(false);
        super.drawScreen(n, n2, f);
    }

    public ClickGui() {
        this.hudIcon = classProvider.createResourceLocation("atfield/custom_hud_icon.png");
        this.style = new SlowlyStyle();
        int n = 100;
        int n2 = 18;
        int n3 = 5;
        for (ModuleCategory moduleCategory : ModuleCategory.values()) {
            this.panels.add(new Panel(this, moduleCategory.getDisplayName(), 100, n3, 100, 18, false, moduleCategory){
                final ModuleCategory val$category;
                final ClickGui this$0;

                @Override
                public void setupItems() {
                    for (Module module : LiquidBounce.moduleManager.getModules()) {
                        if (module.getCategory() != this.val$category) continue;
                        this.getElements().add(new ModuleElement(module));
                    }
                }
                {
                    this.this$0 = clickGui;
                    this.val$category = moduleCategory;
                    super(string, n, n2, n3, n4, bl);
                }
            });
            n3 += 20;
        }
        this.panels.add(new Panel(this, "Targets", 100, n3 += 20, 100, 18, false){
            final ClickGui this$0;
            {
                this.this$0 = clickGui;
                super(string, n, n2, n3, n4, bl);
            }

            @Override
            public void setupItems() {
                this.getElements().add(new ButtonElement(this, "Players"){
                    final 2 this$1;

                    @Override
                    public void createButton(String string) {
                        this.color = EntityUtils.targetPlayer ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        super.createButton(string);
                    }
                    {
                        this.this$1 = var1_1;
                        super(string);
                    }

                    @Override
                    public String getDisplayName() {
                        this.displayName = "Players";
                        this.color = EntityUtils.targetPlayer ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        return super.getDisplayName();
                    }

                    @Override
                    public void mouseClicked(int n, int n2, int n3) {
                        if (n3 == 0 && this.isHovering(n, n2) && this.isVisible()) {
                            EntityUtils.targetPlayer = !EntityUtils.targetPlayer;
                            this.displayName = "Players";
                            this.color = EntityUtils.targetPlayer ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                    }
                });
                this.getElements().add(new ButtonElement(this, "Mobs"){
                    final 2 this$1;

                    @Override
                    public String getDisplayName() {
                        this.displayName = "Mobs";
                        this.color = EntityUtils.targetMobs ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        return super.getDisplayName();
                    }
                    {
                        this.this$1 = var1_1;
                        super(string);
                    }

                    @Override
                    public void mouseClicked(int n, int n2, int n3) {
                        if (n3 == 0 && this.isHovering(n, n2) && this.isVisible()) {
                            EntityUtils.targetMobs = !EntityUtils.targetMobs;
                            this.displayName = "Mobs";
                            this.color = EntityUtils.targetMobs ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                    }

                    @Override
                    public void createButton(String string) {
                        this.color = EntityUtils.targetMobs ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        super.createButton(string);
                    }
                });
                this.getElements().add(new ButtonElement(this, "Animals"){
                    final 2 this$1;

                    @Override
                    public void createButton(String string) {
                        this.color = EntityUtils.targetAnimals ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        super.createButton(string);
                    }
                    {
                        this.this$1 = var1_1;
                        super(string);
                    }

                    @Override
                    public String getDisplayName() {
                        this.displayName = "Animals";
                        this.color = EntityUtils.targetAnimals ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        return super.getDisplayName();
                    }

                    @Override
                    public void mouseClicked(int n, int n2, int n3) {
                        if (n3 == 0 && this.isHovering(n, n2) && this.isVisible()) {
                            EntityUtils.targetAnimals = !EntityUtils.targetAnimals;
                            this.displayName = "Animals";
                            this.color = EntityUtils.targetAnimals ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                    }
                });
                this.getElements().add(new ButtonElement(this, "Invisible"){
                    final 2 this$1;

                    @Override
                    public void mouseClicked(int n, int n2, int n3) {
                        if (n3 == 0 && this.isHovering(n, n2) && this.isVisible()) {
                            EntityUtils.targetInvisible = !EntityUtils.targetInvisible;
                            this.displayName = "Invisible";
                            this.color = EntityUtils.targetInvisible ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                    }
                    {
                        this.this$1 = var1_1;
                        super(string);
                    }

                    @Override
                    public String getDisplayName() {
                        this.displayName = "Invisible";
                        this.color = EntityUtils.targetInvisible ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        return super.getDisplayName();
                    }

                    @Override
                    public void createButton(String string) {
                        this.color = EntityUtils.targetInvisible ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        super.createButton(string);
                    }
                });
                this.getElements().add(new ButtonElement(this, "Dead"){
                    final 2 this$1;

                    @Override
                    public void mouseClicked(int n, int n2, int n3) {
                        if (n3 == 0 && this.isHovering(n, n2) && this.isVisible()) {
                            EntityUtils.targetDead = !EntityUtils.targetDead;
                            this.displayName = "Dead";
                            this.color = EntityUtils.targetDead ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                    }

                    @Override
                    public String getDisplayName() {
                        this.displayName = "Dead";
                        this.color = EntityUtils.targetDead ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        return super.getDisplayName();
                    }

                    @Override
                    public void createButton(String string) {
                        this.color = EntityUtils.targetDead ? ClickGUI.generateColor().getRGB() : Integer.MAX_VALUE;
                        super.createButton(string);
                    }
                    {
                        this.this$1 = var1_1;
                        super(string);
                    }
                });
            }
        });
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
    public void mouseReleased(int n, int n2, int n3) {
        double d = ((Float)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).scaleValue.get()).floatValue();
        n2 -= this.scroll;
        n = (int)((double)n / d);
        n2 = (int)((double)n2 / d);
        for (Panel panel : this.panels) {
            panel.mouseReleased(n, n2, n3);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

