/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Settings;

import java.awt.Color;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Downward;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.ModuleRender;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.OtcClickGUi;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.misc.Direction;
import net.ccbluex.liquidbounce.utils.render.Animation2323;
import net.ccbluex.liquidbounce.utils.render.RenderUtilsFlux;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.opengl.GL11;

public class ListSetting
extends Downward {
    private ListValue listValue;
    private final Animation2323 arrowAnimation = new Animation2323(250, 1.0, Direction.BACKWARDS){

        @Override
        protected double getEquation(double p0) {
            return 0.0;
        }
    };
    private float modulex;
    private float moduley;
    private float listy;

    public ListSetting(ListValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
        this.listValue = s;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int guiColor = ClickGUI.generateColor().getRGB();
        this.modulex = OtcClickGUi.getMainx();
        this.moduley = OtcClickGUi.getMainy();
        this.listy = this.pos.y + (float)this.getScrollY();
        Fonts.fontTahoma.drawString(this.listValue.getName(), this.modulex + 5.0f + this.pos.x + 4.0f, this.moduley + 17.0f + this.listy + 13.0f, new Color(200, 200, 200).getRGB());
        RenderUtilsFlux.drawRoundedRect(this.modulex + 5.0f + this.pos.x + 80.0f, this.moduley + 17.0f + this.listy + 8.0f, 50.0f, 11.0f, 1.0f, new Color(59, 63, 72).getRGB(), 1.0f, new Color(85, 90, 96).getRGB());
        if (this.isHovered(mouseX, mouseY)) {
            RenderUtilsFlux.drawRoundedRect(this.modulex + 5.0f + this.pos.x + 80.0f, this.moduley + 17.0f + this.listy + 8.0f, 50.0f, 11.0f, 1.0f, new Color(0, 0, 0, 0).getRGB(), 1.0f, guiColor);
        }
        Fonts.fontTahoma.drawString((String)this.listValue.get() + "", this.modulex + 5.0f + this.pos.x + 82.0f, this.moduley + 17.0f + this.listy + 13.0f, new Color(200, 200, 200).getRGB());
        this.arrowAnimation.setDirection(this.listValue.openList ? Direction.FORWARDS : Direction.BACKWARDS);
        RenderUtilsFlux.drawClickGuiArrow(this.modulex + 5.0f + this.pos.x + 123.5f, this.moduley + 17.0f + this.listy + 13.0f, 4.0f, this.arrowAnimation, new Color(222, 224, 236).getRGB());
        if (this.listValue.openList) {
            GL11.glTranslatef((float)0.0f, (float)0.0f, (float)2.0f);
            RenderUtilsFlux.drawBorderedRect(this.modulex + 5.0f + this.pos.x + 80.0f, this.moduley + 17.0f + this.listy + 8.0f + 13.0f, this.modulex + 5.0f + this.pos.x + 80.0f + 50.0f, this.moduley + 17.0f + this.listy + 8.0f + 13.0f + (float)this.listValue.getModes().size() * 11.0f, 1.0f, new Color(85, 90, 96).getRGB(), new Color(59, 63, 72).getRGB());
            for (String option : this.listValue.getModes()) {
                Fonts.fontTahoma.drawString(option, this.modulex + 5.0f + this.pos.x + 82.0f, this.moduley + 17.0f + this.listy + 1.0f + 13.0f + 12.0f + (float)(this.listValue.getModeListNumber(option) * 11), option.equals(this.listValue.get()) ? guiColor : new Color(200, 200, 200).getRGB());
            }
            GL11.glTranslatef((float)0.0f, (float)0.0f, (float)-2.0f);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 1 && this.isHovered(mouseX, mouseY)) {
            boolean bl = this.listValue.openList = !this.listValue.openList;
        }
        if (mouseButton == 0 && this.listValue.openList && (float)mouseX >= this.modulex + 5.0f + this.pos.x + 80.0f && (float)mouseX <= this.modulex + 5.0f + this.pos.x + 80.0f + 50.0f) {
            for (int i = 0; i < this.listValue.getModes().size(); ++i) {
                int v = (int)(this.moduley + 17.0f + this.listy + 8.0f + 13.0f + (float)(i * 11));
                if (mouseY < v || mouseY > v + 11) continue;
                this.listValue.set(this.listValue.getModeGet(i));
                this.listValue.openList = false;
            }
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (float)mouseX >= this.modulex + 5.0f + this.pos.x + 80.0f && (float)mouseX <= this.modulex + 5.0f + this.pos.x + 80.0f + 50.0f && (float)mouseY >= this.moduley + 17.0f + this.listy + 8.0f && (float)mouseY <= this.moduley + 17.0f + this.listy + 8.0f + 11.0f;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
    }
}

