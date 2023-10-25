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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class Checkbox
extends Component {
    private Button parent;
    private boolean hovered;
    private int offset;
    private Setting op;
    private int x;
    private int y;

    @Override
    public void renderComponent() {
        Gui.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth() * 1, this.parent.parent.getY() + this.offset + 12, this.hovered ? -14540254 : -15658735);
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 12, -15658735);
        if (this.op.getValBoolean()) {
            Gui.drawRect((float)this.parent.parent.getX() + 87.5f, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 12 - 9, this.parent.parent.getY() + this.offset + 12, this.hovered && !this.op.getValBoolean() ? -14540254 : -10066330);
        }
        GL11.glPushMatrix();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        Minecraft.getMinecraft();
        Minecraft.fontRendererObj.drawStringWithShadow(this.op.getName(), (this.parent.parent.getX() + 4) * 2 + 5, (this.parent.parent.getY() + this.offset + 3) * 2 + 4, -1);
        GL11.glPopMatrix();
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        if (this.isMouseOnButton(n, n2) && n3 == 0 && this.parent.open) {
            this.op.setValBoolean(!this.op.getValBoolean());
        }
    }

    public Checkbox(Setting setting, Button button, int n) {
        this.op = setting;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = n;
    }

    @Override
    public void updateComponent(int n, int n2) {
        this.hovered = this.isMouseOnButton(n, n2);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }

    @Override
    public void setOff(int n) {
        this.offset = n;
    }

    public boolean isMouseOnButton(int n, int n2) {
        return n > this.x && n < this.x + 88 && n2 > this.y && n2 < this.y + 12;
    }
}

