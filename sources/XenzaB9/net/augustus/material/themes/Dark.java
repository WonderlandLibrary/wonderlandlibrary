// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.material.themes;

import java.awt.Font;
import net.augustus.modules.Module;
import net.augustus.Augustus;
import java.util.Iterator;
import net.augustus.utils.skid.tomorrow.Rect;
import org.lwjgl.input.Mouse;
import java.awt.Color;
import net.augustus.material.Tab;
import net.augustus.utils.skid.tomorrow.RenderUtil;
import net.augustus.utils.skid.tomorrow.ColorUtils;
import net.augustus.material.Category;
import net.augustus.modules.Categorys;
import net.augustus.material.button.CButton;
import net.augustus.font.UnicodeFontRenderer;
import net.augustus.material.Main;

public class Dark extends Main
{
    private static UnicodeFontRenderer arial18;
    private static UnicodeFontRenderer arial16;
    
    @Override
    public void initGui() {
        super.initGui();
        this.Blist = new CButton("Modules", "client/clickgui/modules.png", 2.0f, 4.0f, 12.0f, 8.0f);
        this.Bconfigs = new CButton("Modules", "client/clickgui/settings.png", 3.0f, 3.0f, 11.0f, 11.0f);
        this.categories.clear();
        for (final Categorys mt : Categorys.values()) {
            this.categories.add(new Category(mt, 0, false));
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void drawTasksBar() {
        super.drawTasksBar();
        RenderUtil.drawRect(Dark.windowX + Dark.animListX, Dark.windowY + 30.0f, Dark.windowX + Dark.windowWidth, Dark.windowY + 50.0f, ColorUtils.reAlpha(Dark.clientColor.getRGB(), 0.6f));
        float x = 4.0f;
        for (final Tab t : Dark.tabs) {
            if (Dark.currentTab == t) {
                t.render(this.mouseX, this.mouseY);
            }
            final float swidth = (float)(Dark.arial16.getStringWidth(t.name) + 14);
            t.x = t.animationUtils.animate(Dark.windowX + x + Dark.animListX, t.x, this.drag ? 2.0f : 0.1f);
            new Rect(t.x, Dark.windowY + 30.0f, swidth, 20.0f, new Color(0, 0, 0, 0), new Runnable() {
                @Override
                public void run() {
                    if (Mouse.isButtonDown(0)) {
                        Main.currentTab = t;
                    }
                }
            }).render(this.mouseX, this.mouseY);
            if (Main.isHovered(t.x + swidth - 4.0f, Dark.windowY + 30.0f, t.x + swidth + 4.0f, Dark.windowY + 50.0f, this.mouseX, this.mouseY)) {
                Dark.arial18.drawString("-", t.x + swidth - 6.0f, Dark.windowY + 40.0f, new Color(255, 0, 0).getRGB());
            }
            else {
                Dark.arial18.drawString("-", t.x + swidth - 6.0f, Dark.windowY + 40.0f, new Color(255, 255, 255).getRGB());
            }
            if (t == Dark.currentTab) {
                Dark.arial16.drawString(t.name, t.x + 2.0f, Dark.windowY + 40.0f, -1);
                RenderUtil.drawRect(t.x, Dark.windowY + 48.0f, t.x + swidth, Dark.windowY + 50.0f, Dark.clientColor.getRGB());
            }
            else {
                Dark.arial16.drawString(t.name, t.x + 2.0f, Dark.windowY + 40.0f, new Color(34, 34, 34, 150).getRGB());
            }
            x += swidth;
        }
    }
    
    @Override
    public void drawList(final float mouseX, final float mouseY) {
        super.drawList(mouseX, mouseY);
        if (this.Blist.realized) {
            Dark.animListX = Dark.listAnim.animate(140.0f, Dark.animListX, 0.2f);
        }
        else {
            Dark.animListX = Dark.listAnim.animate(0.0f, Dark.animListX, 0.2f);
        }
        if (Dark.animListX != 0.0f) {
            RenderUtil.drawRect(Dark.windowX, Dark.windowY + 30.0f, Dark.windowX + Dark.animListX, Dark.windowY + Dark.windowHeight, new Color(34, 34, 34));
            RenderUtil.drawGradientSideways(Dark.windowX + Dark.animListX, Dark.windowY + 30.0f, Dark.windowX + Dark.animListX + 3.0f, Dark.windowY + Dark.windowHeight, new Color(50, 50, 50, 100).getRGB(), new Color(255, 255, 255, 0).getRGB());
            final float dWheel = (float)Mouse.getDWheel();
            if (dWheel > 0.0f && this.listRoll2 < 0.0f) {
                this.listRoll2 += 32.0f;
            }
            else if (dWheel < 0.0f) {
                this.listRoll2 -= 32.0f;
            }
            this.listRoll = Dark.rollAnim.animate(this.listRoll2, this.listRoll, 0.05f);
            float modsY = Dark.windowY + 35.0f + this.listRoll;
            for (final Category mt : this.categories) {
                if (mt.show || mt.needRemove) {
                    new Rect(Dark.windowX, modsY, Dark.animListX, 20.0f, new Color(34, 34, 34), () -> {}).render(mouseX, mouseY);
                    Dark.arial18.drawString(mt.moduleType.name(), Dark.windowX + Dark.animListX - 130.0f, modsY + 5.0f, new Color(255, 255, 255).getRGB());
                    modsY += 25.0f;
                    mt.modsY2 = 0.0f;
                    for (final Module m : Augustus.getInstance().getModuleManager().getModules(mt.moduleType)) {
                        new Rect(Dark.windowX, modsY + mt.modsY2, Dark.animListX, 15.0f, new Color(34, 34, 34), new Runnable() {
                            @Override
                            public void run() {
                            }
                        }).render(mouseX, mouseY);
                        if (modsY + 5.0f + mt.modsY2 < modsY + mt.modsY3 + 25.0f) {
                            Dark.arial18.drawString(m.getName(), Dark.windowX + Dark.animListX - 120.0f, modsY + 5.0f + mt.modsY2, new Color(200, 200, 200).getRGB());
                        }
                        final Category category = mt;
                        category.modsY2 += 20.0f;
                    }
                    if (mt.needRemove) {
                        mt.modsY3 = mt.rollAnim2.animate(-25.0f, mt.modsY3, 0.1f);
                        if (mt.modsY3 == -25.0f) {
                            mt.needRemove = false;
                            mt.show = false;
                        }
                    }
                    else {
                        mt.modsY3 = mt.rollAnim2.animate(mt.modsY2, mt.modsY3, 0.1f);
                    }
                    modsY += mt.modsY3;
                }
                else {
                    new Rect(Dark.windowX, modsY, Dark.animListX, 20.0f, new Color(34, 34, 34), new Runnable() {
                        @Override
                        public void run() {
                        }
                    }).render(mouseX, mouseY);
                    Dark.arial18.drawString(mt.moduleType.name(), Dark.windowX + Dark.animListX - 130.0f, modsY + 5.0f, new Color(255, 255, 255).getRGB());
                }
                modsY += 25.0f;
            }
        }
    }
    
    @Override
    public void drawWindow(final float mouseX, final float mouseY) {
        RenderUtil.drawRoundedRect(Dark.windowX, Dark.windowY, Dark.windowX + Dark.windowWidth, Dark.windowY + Dark.windowHeight, 2, new Color(34, 34, 34).getRGB());
        new Rect(Dark.windowX, Dark.windowY, Dark.windowWidth, 30.0f, Dark.clientColor, new Runnable() {
            @Override
            public void run() {
                if (!Mouse.isButtonDown(0)) {
                    return;
                }
                Dark.this.drag = true;
                if (Dark.this.mouseDX == 0.0f) {
                    Dark.this.mouseDX = mouseX - Main.windowX;
                }
                if (Dark.this.mouseDY == 0.0f) {
                    Dark.this.mouseDY = mouseY - Main.windowY;
                }
            }
        }).render(mouseX, mouseY);
    }
    
    static {
        try {
            Dark.arial18 = new UnicodeFontRenderer(Font.createFont(0, Dark.class.getResourceAsStream("/ressources/arial.ttf")).deriveFont(18.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            Dark.arial16 = new UnicodeFontRenderer(Font.createFont(0, Dark.class.getResourceAsStream("/ressources/arial.ttf")).deriveFont(16.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
