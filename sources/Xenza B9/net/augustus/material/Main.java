// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.material;

import java.awt.Font;
import org.lwjgl.opengl.GL11;
import net.augustus.utils.skid.tomorrow.RenderUtil;
import net.augustus.utils.skid.tomorrow.ColorUtils;
import java.io.IOException;
import java.util.Iterator;
import net.augustus.material.Tabs.ModuleTab;
import net.augustus.modules.Module;
import net.augustus.Augustus;
import net.augustus.utils.skid.tomorrow.Rect;
import net.augustus.clickgui.screens.ConfigGui;
import net.augustus.material.themes.Dark;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.Color;
import net.augustus.utils.skid.tomorrow.AnimationUtils;
import java.util.ArrayList;
import net.augustus.material.button.CButton;
import net.augustus.font.UnicodeFontRenderer;
import net.minecraft.client.gui.GuiScreen;

public class Main extends GuiScreen
{
    private static UnicodeFontRenderer arial18;
    private static UnicodeFontRenderer arial16;
    public static float windowX;
    public static float windowY;
    public static float windowWidth;
    public static float windowHeight;
    public CButton Blist;
    public CButton Bconfigs;
    public float mouseX;
    public float mouseY;
    public ArrayList<Category> categories;
    public static float animListX;
    public float listRoll2;
    public float listRoll;
    public static AnimationUtils listAnim;
    public static AnimationUtils rollAnim;
    public float bg;
    public static AnimationUtils bgAnim;
    public static ArrayList<Tab> tabs;
    public static Tab currentTab;
    public static Color clientColor;
    ScaledResolution sr;
    public static Object currentObj;
    public float mouseDX;
    public float mouseDY;
    public boolean drag;
    public float mouseDX2;
    public float mouseDY2;
    public boolean drag2;
    
    public Main() {
        this.categories = new ArrayList<Category>();
        this.listRoll2 = 0.0f;
        this.listRoll = 0.0f;
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final Iterator<Tab> iterator = Main.tabs.iterator();
        Tab t = null;
        while (iterator.hasNext()) {
            t = iterator.next();
            t.mouseClicked((float)mouseX, (float)mouseY);
        }
        if (!isHovered(Main.windowX + Main.windowWidth - 5.0f, Main.windowY + Main.windowHeight - 5.0f, Main.windowX + Main.windowWidth + 5.0f, Main.windowY + Main.windowHeight + 5.0f, (float)mouseX, (float)mouseY) && Mouse.isButtonDown(0)) {
            this.drag2 = false;
            this.mouseDX2 = 0.0f;
            this.mouseDY2 = 0.0f;
        }
        this.Blist.onMouseClicked((float)mouseX, (float)mouseY);
        this.Bconfigs.onMouseClicked((float)mouseX, (float)mouseY);
        if (this.Bconfigs.realized) {
            this.mc.displayGuiScreen(new ConfigGui(new Dark()));
            this.Bconfigs.realized = false;
        }
        float modsY = Main.windowY + 35.0f + this.listRoll;
        for (final Category mt : this.categories) {
            if (mt.show) {
                new Rect(Main.windowX, modsY, Main.animListX, 20.0f, new Color(255, 255, 255), () -> {
                    if (Mouse.isButtonDown(0)) {
                        if (!mt.show) {
                            mt.show = true;
                        }
                        else {
                            mt.needRemove = !mt.needRemove;
                        }
                    }
                    return;
                }).render((float)mouseX, (float)mouseY);
                modsY += 25.0f;
                for (final Module m : Augustus.getInstance().getModuleManager().getModules(mt.moduleType)) {
                    if (modsY > Main.windowY + 30.0f && modsY < Main.windowY + Main.windowHeight && !mt.needRemove) {
                        new Rect(Main.windowX, modsY, Main.animListX, 15.0f, new Color(255, 255, 255), () -> {
                            if (Mouse.isButtonDown(0)) {
                                m.setToggled(!m.isToggled());
                            }
                            else if (Mouse.isButtonDown(1)) {
                                final ModuleTab modT = new ModuleTab(m);
                                Main.tabs.iterator();
                                final Iterator iterator6;
                                while (iterator6.hasNext()) {
                                    final Tab m2 = iterator6.next();
                                    if (m2.name.equals(modT.name)) {
                                        Main.currentTab = m2;
                                        return;
                                    }
                                }
                                Main.tabs.add(modT);
                                Main.currentTab = modT;
                            }
                            return;
                        }).render((float)mouseX, (float)mouseY);
                    }
                    Main.arial18.drawString(m.getName(), Main.windowX + Main.animListX - 120.0f, modsY + 5.0f, new Color(50, 50, 50).getRGB());
                    modsY += 20.0f;
                }
            }
            else {
                new Rect(Main.windowX, modsY, Main.animListX, 20.0f, new Color(255, 255, 255), () -> {
                    if (Mouse.isButtonDown(0)) {
                        mt.show = !mt.show;
                    }
                    return;
                }).render((float)mouseX, (float)mouseY);
            }
            modsY += 25.0f;
        }
        final ArrayList<Tab> tabs2 = new ArrayList<Tab>();
        float x = 4.0f;
        for (final Tab t2 : Main.tabs) {
            final float swidth = (float)(Main.arial16.getStringWidth(t2.name) + 14);
            t2.x = t2.animationUtils.animate(Main.windowX + x + Main.animListX, t2.x, this.drag ? 2.0f : 0.1f);
            new Rect(t2.x, Main.windowY + 30.0f, swidth, 20.0f, new Color(0, 0, 0, 0), () -> {
                if (Mouse.isButtonDown(0)) {
                    Main.currentTab = t;
                }
                return;
            }).render((float)mouseX, (float)mouseY);
            if (isHovered(t2.x + swidth - 4.0f, Main.windowY + 30.0f, t2.x + swidth + 4.0f, Main.windowY + 50.0f, (float)mouseX, (float)mouseY) && Mouse.isButtonDown(0)) {
                tabs2.add(t2);
            }
            x += swidth;
        }
        for (final Tab tab : tabs2) {
            Main.tabs.remove(tab);
        }
    }
    
    @Override
    public void initGui() {
        this.sr = new ScaledResolution(this.mc);
        if (this.sr.getScaledWidth() < 550 && this.sr.getScaledHeight() < 300) {
            Main.windowWidth = (float)(this.sr.getScaledWidth() - 10);
            Main.windowHeight = (float)(this.sr.getScaledHeight() - 10);
            Main.windowX = (this.sr.getScaledWidth() - Main.windowWidth) / 2.0f;
            Main.windowY = (this.sr.getScaledHeight() - Main.windowHeight) / 2.0f;
        }
        if (Main.windowWidth == 0.0f) {
            Main.windowWidth = 550.0f;
        }
        if (Main.windowHeight == 0.0f) {
            Main.windowHeight = 300.0f;
        }
        if (Main.windowX == 0.0f) {
            Main.windowX = (this.sr.getScaledWidth() - Main.windowWidth) / 2.0f;
        }
        if (Main.windowY == 0.0f) {
            Main.windowY = (this.sr.getScaledHeight() - Main.windowHeight) / 2.0f;
        }
        if (Main.tabs.size() == 0) {}
        super.initGui();
    }
    
    public void drawWindow(final float mouseX, final float mouseY) {
    }
    
    public void drawMWindow(final int mouseX, final int mouseY) {
        if (Main.currentObj == null) {
            this.mouseX = (float)mouseX;
            this.mouseY = (float)mouseY;
            if (this.mouseDX != 0.0f && this.drag && Mouse.isButtonDown(0)) {
                Main.windowX = this.mouseX - this.mouseDX;
            }
            else {
                this.drag = false;
                this.mouseDX = 0.0f;
                this.mouseDY = 0.0f;
            }
            if (this.mouseDY != 0.0f && this.drag && Mouse.isButtonDown(0)) {
                Main.windowY = this.mouseY - this.mouseDY;
            }
            else {
                this.drag = false;
                this.mouseDX = 0.0f;
                this.mouseDY = 0.0f;
            }
        }
        this.drawWindow(this.mouseX, this.mouseY);
    }
    
    public void drawTasksBar() {
    }
    
    public void drawBar(final float mouseX, final float mouseY) {
        this.Blist.onRender(Main.windowX + 8.0f, Main.windowY + 8.0f, mouseX, mouseY);
        this.Bconfigs.onRender(Main.windowX + Main.windowWidth - 20.0f, Main.windowY + 8.0f, mouseX, mouseY);
    }
    
    public void drawBG() {
        this.bg = Main.bgAnim.animate((float)this.sr.getScaledWidth(), this.bg, 0.01f);
        RenderUtil.drawCircle(this.sr.getScaledWidth() / 2.0f, this.sr.getScaledHeight() / 2.0f, this.bg, ColorUtils.reAlpha(Main.clientColor.getRGB(), 0.1f));
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        Main.clientColor = Augustus.getInstance().getClientColor();
        this.drawBG();
        this.drawMWindow(mouseX, mouseY);
        this.drawBar((float)mouseX, (float)mouseY);
        GL11.glEnable(3089);
        RenderUtil.doGlScissor(Main.windowX, Main.windowY + 30.0f, Main.windowWidth, Main.windowHeight - 30.0f);
        if ((isHovered(Main.windowX + Main.windowWidth - 5.0f, Main.windowY + Main.windowHeight - 5.0f, Main.windowX + Main.windowWidth + 5.0f, Main.windowY + Main.windowHeight + 5.0f, (float)mouseX, (float)mouseY) && Mouse.isButtonDown(0)) || this.drag2) {
            if (Main.windowWidth > 560.0f) {
                Main.windowWidth = mouseX - Main.windowX - this.mouseDX2;
            }
            else if (mouseX - Main.windowX > Main.windowWidth) {
                Main.windowWidth = mouseX - Main.windowX - this.mouseDX2;
            }
            if (Main.windowHeight > 310.0f) {
                Main.windowHeight = mouseY - Main.windowY - this.mouseDY2;
            }
            else if (mouseY - Main.windowY > Main.windowHeight) {
                Main.windowHeight = mouseY - Main.windowY - this.mouseDY2;
            }
        }
        if (isHovered(Main.windowX + Main.windowWidth - 5.0f, Main.windowY + Main.windowHeight - 5.0f, Main.windowX + Main.windowWidth + 5.0f, Main.windowY + Main.windowHeight + 5.0f, (float)mouseX, (float)mouseY) && Mouse.isButtonDown(0)) {
            this.drag2 = true;
            this.mouseDX2 = mouseX - (Main.windowX + Main.windowWidth);
            this.mouseDY2 = mouseY - (Main.windowY + Main.windowHeight);
        }
        if (!Mouse.isButtonDown(0)) {
            this.drag2 = false;
            this.mouseDX2 = 0.0f;
            this.mouseDY2 = 0.0f;
        }
        this.drawTasksBar();
        this.drawList((float)mouseX, (float)mouseY);
        GL11.glDisable(3089);
    }
    
    public void drawList(final float mouseX, final float mouseY) {
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
    
    public static boolean isHovered(final float x, final float y, final float x2, final float y2, final float mouseX, final float mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
    
    static {
        try {
            Main.arial18 = new UnicodeFontRenderer(Font.createFont(0, Main.class.getResourceAsStream("/ressources/arial.ttf")).deriveFont(18.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            Main.arial16 = new UnicodeFontRenderer(Font.createFont(0, Main.class.getResourceAsStream("/ressources/arial.ttf")).deriveFont(16.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        Main.listAnim = new AnimationUtils();
        Main.rollAnim = new AnimationUtils();
        Main.bgAnim = new AnimationUtils();
        Main.tabs = new ArrayList<Tab>();
    }
}
