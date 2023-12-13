// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.ClickGUI.RiseClickGUI;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import java.io.IOException;
import java.util.Iterator;
import Lavish.modules.Module;
import Lavish.Client;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import Lavish.utils.fonts.FontUtil;
import Lavish.utils.render.Render;
import Lavish.utils.misc.Timer;
import Lavish.utils.animations.impl.DecelerateAnimation;
import Lavish.modules.Category;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends GuiScreen
{
    public static Category activeCat;
    DecelerateAnimation ani;
    public int ylevel;
    public boolean inProgress;
    public int color;
    public boolean enabled;
    public int slideanim;
    public int animation;
    Timer timer;
    Timer timer2;
    Timer timer3;
    
    public ClickGui() {
        this.ylevel = 100;
        this.inProgress = false;
        this.slideanim = 0;
        this.animation = 20;
        this.timer = new Timer();
        this.timer2 = new Timer();
        this.timer3 = new Timer();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        if (this.timer2.hasTimeElapsed(5.0, true)) {
            if (this.slideanim < 20) {
                ++this.slideanim;
            }
            if (this.slideanim > 20) {
                --this.slideanim;
            }
        }
        Render.colorRGBA(-14276048);
        Render.drawRoundedRect(226.0, 80 + this.slideanim, 500.0, 300.0, 10.0f);
        Render.colorRGBA(-14276564);
        Render.drawRoundedRect(226.0, 80 + this.slideanim, 90.0, 300.0, 10.0f);
        FontUtil.cleanlarge.drawString("Lavish", 245.0, 83 + this.slideanim, -1);
        int catOffset = 0;
        Category[] values;
        for (int length = (values = Category.values()).length, i = 0; i < length; ++i) {
            final Category catRect = values[i];
            if (ClickGui.activeCat == null) {
                ClickGui.activeCat = Category.Combat;
            }
            if (ClickGui.activeCat == Category.Combat && this.timer.hasTimeElapsed(2.0, true)) {
                if (this.ylevel < 125) {
                    ++this.ylevel;
                }
                if (this.ylevel > 125) {
                    --this.ylevel;
                }
            }
            if (ClickGui.activeCat == Category.Movement && this.timer.hasTimeElapsed(2.0, true)) {
                if (this.ylevel < 150) {
                    ++this.ylevel;
                }
                if (this.ylevel > 150) {
                    --this.ylevel;
                }
            }
            if (ClickGui.activeCat == Category.Player && this.timer.hasTimeElapsed(2.0, true)) {
                if (this.ylevel < 175) {
                    ++this.ylevel;
                }
                if (this.ylevel > 175) {
                    --this.ylevel;
                }
            }
            if (ClickGui.activeCat == Category.Render && this.timer.hasTimeElapsed(2.0, true)) {
                if (this.ylevel < 200) {
                    ++this.ylevel;
                }
                if (this.ylevel > 200) {
                    --this.ylevel;
                }
            }
            if (ClickGui.activeCat == Category.Misc && this.timer.hasTimeElapsed(2.0, true)) {
                if (this.ylevel < 225) {
                    ++this.ylevel;
                }
                if (this.ylevel > 225) {
                    --this.ylevel;
                }
            }
            if (ClickGui.activeCat == Category.Exploit && this.timer.hasTimeElapsed(2.0, true)) {
                if (this.ylevel < 250) {
                    ++this.ylevel;
                }
                if (this.ylevel > 250) {
                    --this.ylevel;
                }
            }
            Gui.drawRect(226.0, this.ylevel - 20 + this.slideanim, 315.0, this.ylevel + 25 - 20 + this.slideanim, -12679425);
        }
        this.mc.fontRendererObj.drawString(this.mc.getSession().getUsername(), 260, 358 + this.slideanim, -1);
        this.drawFace(230.0, 350 + this.slideanim, 8.0f, 8.0f, 8, 8, 25, 25, 64.0f, 64.0f, this.mc.thePlayer);
        Category[] values2;
        for (int length2 = (values2 = Category.values()).length, j = 0; j < length2; ++j) {
            final Category cat = values2[j];
            FontUtil.cleanmedium.drawString(cat.toString(), 250.0, 135 + catOffset * 25 - 20 + this.slideanim, -1);
            ++catOffset;
            if (cat == Category.Combat) {
                FontUtil.csgoFont.drawString("a", 230.0, 113 + this.slideanim, -1);
            }
            if (cat == Category.Movement) {
                FontUtil.csgoFont.drawString("b", 230.0, 137 + this.slideanim, -1);
            }
            if (cat == Category.Player) {
                FontUtil.csgoFont.drawString("f", 230.0, 161 + this.slideanim, -1);
            }
            if (cat == Category.Render) {
                FontUtil.csgoFont.drawString("c", 230.0, 187 + this.slideanim, -1);
            }
            if (cat == Category.Misc) {
                FontUtil.coolFont.drawString("I", 230.0, 212 + this.slideanim, -1);
            }
            if (cat == Category.Exploit) {
                FontUtil.csgoFont.drawString("d", 230.0, 237 + this.slideanim, -1);
            }
        }
        if (ClickGui.activeCat != null) {
            int modOffset = 0;
            for (final Module m : Client.instance.moduleManager.getModulesInCategory(ClickGui.activeCat)) {
                Client.instance.font18.drawString(m.getName(), 320.0f, (float)(130 + modOffset * 20 - 20 + this.slideanim), m.isEnabled() ? -9733439 : -6052437, false);
                Render.colorRGBA(-1);
                Render.drawRoundedRect(620.0, 130 + modOffset * 20 - 20 + this.slideanim, 18.0, 10.0, 5.0f);
                Render.colorRGBA(-9733439);
                Render.drawRoundedRect(600 + this.animation, 130 + modOffset * 20 - 20 + this.slideanim, 10.0, 10.0, 5.0f);
                if (m.isEnabled()) {
                    if (this.animation != 30) {
                        this.animation = 30;
                    }
                }
                else if (this.animation != 20) {
                    this.animation = 20;
                }
                ++modOffset;
            }
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseButton == 0) {
            int catOffset = 0;
            Category[] values;
            for (int length = (values = Category.values()).length, i = 0; i < length; ++i) {
                final Category cat = values[i];
                if (this.isMouseOnCategory(mouseX, mouseY, cat.toString(), 135 + catOffset * 25)) {
                    ClickGui.activeCat = cat;
                }
                ++catOffset;
            }
            if (ClickGui.activeCat != null) {
                int modOffset = 0;
                for (final Module m : Client.instance.moduleManager.getModulesInCategory(ClickGui.activeCat)) {
                    if (this.isMouseOnModule(mouseX, mouseY, m.getName(), 130 + modOffset * 20)) {
                        m.toggle();
                    }
                    ++modOffset;
                }
            }
        }
    }
    
    public boolean isMouseOnCategory(final int x, final int y, final String nameofCategory, final int catY) {
        return x > 250 && x < 300 && y > catY && y < catY + 12;
    }
    
    public boolean isMouseOnModule(final int x, final int y, final String nameofModule, final int modY) {
        return x > 320 && x < 320 + Client.instance.font18.getStringWidth(nameofModule) + 2 && y > modY && y < modY + 10;
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    private void drawFace(final double x, final double y, final float u, final float v, final int uWidth, final int vHeight, final int width, final int height, final float tileWidth, final float tileHeight, final AbstractClientPlayer target) {
        try {
            final ResourceLocation skin = target.getLocationSkin();
            this.mc.getTextureManager().bindTexture(skin);
            GL11.glEnable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            Gui.drawScaledCustomSizeModalRect((int)x, (int)y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable(3042);
        }
        catch (Exception ex) {}
    }
}
