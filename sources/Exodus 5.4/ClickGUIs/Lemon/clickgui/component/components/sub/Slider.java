/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package ClickGUIs.Lemon.clickgui.component.components.sub;

import ClickGUIs.Lemon.clickgui.component.Component;
import ClickGUIs.Lemon.clickgui.component.components.Button;
import de.Hero.settings.Setting;
import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class Slider
extends Component {
    private int offset;
    private boolean dragging = false;
    private int y;
    private double renderWidth;
    private int x;
    private boolean hovered;
    private Button parent;
    private Setting set;

    @Override
    public void setOff(int n) {
        this.offset = n;
    }

    public Slider(Setting setting, Button button, int n) {
        this.set = setting;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = n;
    }

    @Override
    public void updateComponent(int n, int n2) {
        this.hovered = this.isMouseOnButtonD(n, n2) || this.isMouseOnButtonI(n, n2);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
        double d = Math.min(88, Math.max(0, n - this.x));
        double d2 = this.set.getMin();
        double d3 = this.set.getMax();
        this.renderWidth = 88.0 * (this.set.getValDouble() - d2) / (d3 - d2);
        if (this.dragging) {
            if (d == 0.0) {
                this.set.setValDouble(this.set.getMin());
            } else {
                double d4 = Slider.roundToPlace(d / 88.0 * (d3 - d2) + d2, 2);
                this.set.setValDouble(d4);
            }
        }
    }

    public boolean isMouseOnButtonI(int n, int n2) {
        return n > this.x + this.parent.parent.getWidth() / 2 && n < this.x + this.parent.parent.getWidth() && n2 > this.y && n2 < this.y + 12;
    }

    private static double roundToPlace(double d, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(d);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public boolean isMouseOnButtonD(int n, int n2) {
        return n > this.x && n < this.x + (this.parent.parent.getWidth() / 2 + 1) && n2 > this.y && n2 < this.y + 12;
    }

    @Override
    public void renderComponent() {
        Gui.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 12, this.hovered ? -14540254 : -15658735);
        int n = (int)(this.set.getValDouble() / this.set.getMax() * (double)this.parent.parent.getWidth());
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + (int)this.renderWidth, this.parent.parent.getY() + this.offset + 12, this.hovered ? -11184811 : -12303292);
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 12, -15658735);
        GL11.glPushMatrix();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        Minecraft.getMinecraft();
        Minecraft.fontRendererObj.drawStringWithShadow(String.valueOf(this.set.getName()) + ": " + this.set.getValDouble(), this.parent.parent.getX() * 2 + 15, (this.parent.parent.getY() + this.offset + 2) * 2 + 5, -1);
        GL11.glPopMatrix();
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        if (this.isMouseOnButtonD(n, n2) && n3 == 0 && this.parent.open) {
            this.dragging = true;
        }
        if (this.isMouseOnButtonI(n, n2) && n3 == 0 && this.parent.open) {
            this.dragging = true;
        }
    }

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        this.dragging = false;
    }
}

