/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.ModuleRender;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.OtcClickGUi;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.Position;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.math.MathUtil;
import net.ccbluex.liquidbounce.utils.misc.Direction;
import net.ccbluex.liquidbounce.utils.render.Animation;
import net.ccbluex.liquidbounce.utils.render.RenderUtilsFlux;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class CategoryScreen {
    private float maxScroll = Float.MAX_VALUE;
    private float minScroll = 0.0f;
    private float rawScroll;
    private float scroll;
    public Position pos;
    private ModuleCategory category = ModuleCategory.COMBAT;
    private float x;
    private float categoryX;
    private float categoryY;
    private boolean selected;
    private final List<ModuleRender> moduleList = new CopyOnWriteArrayList<ModuleRender>();
    private Animation scrollAnimation = new Animation(0, 0.0, Direction.BACKWARDS){

        @Override
        protected double getEquation(double p0) {
            return 0.0;
        }
    };

    public String newcatename(ModuleCategory moduleCategory) {
        if (moduleCategory.getDisplayName().equals("Combat")) {
            return "a";
        }
        if (moduleCategory.getDisplayName().equals("Player")) {
            return "d";
        }
        if (moduleCategory.getDisplayName().equals("Movement")) {
            return "c";
        }
        if (moduleCategory.getDisplayName().equals("Render")) {
            return "J";
        }
        if (moduleCategory.getDisplayName().equals("World")) {
            return "e";
        }
        if (moduleCategory.getDisplayName().equals("Other")) {
            return "g";
        }
        return "";
    }

    public CategoryScreen(ModuleCategory category, float x) {
        this.category = category;
        this.x = x;
        this.pos = new Position(0.0f, 0.0f, 0.0f, 0.0f);
        int count = 0;
        int leftAdd = 0;
        int rightAdd = 0;
        for (Module module : LiquidBounce.moduleManager.getModuleInCategory(this.category)) {
            float posWidth = 0.0f;
            float posX = this.pos.x + (float)(count % 2 == 0 ? 0 : 145);
            float posY = this.pos.y + (float)(count % 2 == 0 ? leftAdd : rightAdd);
            Position pos = new Position(posX, posY, 0.0f, 30.0f);
            ModuleRender otlM = new ModuleRender(module, pos.x, pos.y, pos.width, pos.height);
            pos.height = otlM.height;
            if (count % 2 == 0) {
                leftAdd += (int)(pos.height + 20.0f);
            } else {
                rightAdd += (int)(pos.height + 20.0f);
            }
            this.moduleList.add(otlM);
            ++count;
        }
    }

    public void drawScreen(int mouseX, int mouseY) {
        try {
            this.categoryX = OtcClickGUi.getMainx();
            this.categoryY = OtcClickGUi.getMainy();
            if (this.selected) {
                double scrolll = this.getScroll();
                for (ModuleRender module2 : this.moduleList) {
                    module2.scrollY = (int)MathUtil.roundToHalf(scrolll);
                }
                this.onScroll(30);
                this.maxScroll = Math.max(0.0f, this.moduleList.get(this.moduleList.size() - 1).getY() + (float)(this.moduleList.get((int)(this.moduleList.size() - 1)).height * 2) + 2500.0f);
            }
            Fonts.flux.drawString(this.newcatename(this.category), this.x + this.categoryX + 77.0f, this.categoryY - 29.0f, -1);
            if (this.selected) {
                RoundedUtil.drawRound(this.x + this.categoryX + 76.0f, this.categoryY - 29.0f - 3.0f, Fonts.flux.func_78256_a(this.newcatename(this.category)) + 2, 9.0f, 1.0f, new Color(255, 255, 255, 60));
                GL11.glPushMatrix();
                RenderUtilsFlux.scissor(0.0, OtcClickGUi.getMainy(), 1920.0, 300.0);
                GL11.glEnable((int)3089);
                this.moduleList.stream().sorted((o1, o2) -> Boolean.compare(o1.isSelected(), o2.isSelected())).forEach(module -> module.drawScreen(mouseX, mouseY));
                GL11.glDisable((int)3089);
                GL11.glPopMatrix();
            }
            if (this.isHovered(mouseX, mouseY)) {
                RoundedUtil.drawRound(this.x + this.categoryX + 76.0f, this.categoryY - 29.0f - 3.0f, Fonts.flux.func_78256_a(this.newcatename(this.category)) + 2, 9.0f, 1.0f, new Color(255, 255, 255, 60));
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void onScroll(int ms) {
        this.scroll = (float)((double)this.rawScroll - this.scrollAnimation.getOutput());
        this.rawScroll += (float)Mouse.getDWheel() / 4.0f;
        this.rawScroll = Math.max(Math.min(this.minScroll, this.rawScroll), -this.maxScroll);
        this.scrollAnimation = new Animation(ms, this.rawScroll - this.scroll, Direction.BACKWARDS){

            @Override
            protected double getEquation(double p0) {
                return 0.0;
            }
        };
    }

    public float getScroll() {
        this.scroll = (float)((double)this.rawScroll - this.scrollAnimation.getOutput());
        return this.scroll;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (float)mouseX >= this.x + this.categoryX + 76.0f && (float)mouseX <= this.x + this.categoryX + 76.0f + (float)Fonts.flux.func_78256_a(this.newcatename(this.category)) + 2.0f && (float)mouseY >= this.categoryY - 29.0f - 3.0f && (float)mouseY <= this.categoryY - 29.0f - 3.0f + 9.0f;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.moduleList.forEach(s -> s.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.moduleList.forEach(e -> e.mouseReleased(mouseX, mouseY, state));
    }

    public void keyTyped(char typedChar, int keyCode) {
        this.moduleList.forEach(e -> e.keyTyped(typedChar, keyCode));
    }

    public void setSelected(boolean s) {
        this.selected = s;
    }

    public boolean isSelected() {
        return this.selected;
    }
}

