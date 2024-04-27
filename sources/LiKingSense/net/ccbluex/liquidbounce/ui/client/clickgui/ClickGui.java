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
import java.util.List;
import java.util.Objects;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.Panel;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ButtonElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.Element;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ModuleElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.Style;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ClickGui
extends WrappedGuiScreen {
    public final List<Panel> panels;
    private final IResourceLocation hudIcon;
    public Style style;
    private Panel clickedPanel;
    private int mouseX;
    private int mouseY;
    private int scroll;

    /*
     * Exception decompiling
     */
    public ClickGui() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl46 : INVOKEINTERFACE - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Color color;
        if (Mouse.isButtonDown((int)0) && mouseX >= 5 && mouseX <= 50 && mouseY <= this.representedScreen.getHeight() - 5 && mouseY >= this.representedScreen.getHeight() - 50) {
            mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiHudDesigner()));
        }
        AWTFontRenderer.Companion.setAssumeNonVolatile(true);
        double scale = ((Float)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).scaleValue.get()).floatValue();
        GlStateManager.func_179109_b((float)0.0f, (float)this.scroll, (float)0.0f);
        mouseY -= this.scroll;
        mouseX = (int)((double)mouseX / scale);
        mouseY = (int)((double)mouseY / scale);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        ScaledResolution sr = new ScaledResolution(minecraft);
        float f = 0.0f;
        float f2 = 0.0f;
        RenderUtils.drawRect((float)sr.func_78326_a(), (float)sr.func_78328_b(), (float)color, (float)color, (Color)0);
        IResourceLocation iResourceLocation = this.hudIcon;
        int n = 9;
        int n2 = this.representedScreen.getHeight() - 41;
        int n3 = 0;
        GL11.glScaled((double)scale, (double)scale, (double)scale);
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
            if (wheel < 0) {
                this.scroll -= 15;
            } else if (wheel > 0) {
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
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        double scale = ((Float)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).scaleValue.get()).floatValue();
        mouseY -= this.scroll;
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
        mouseY -= this.scroll;
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

