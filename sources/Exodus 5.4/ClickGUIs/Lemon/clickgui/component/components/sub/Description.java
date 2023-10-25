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
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class Description
extends Component {
    private int modeIndex;
    private boolean hovered;
    private int offset;
    private Setting set;
    private Button parent;
    private Module mod;
    private int y;
    private int x;

    @Override
    public void setOff(int n) {
        this.offset = n;
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
    }

    public boolean isMouseOnButton(int n, int n2) {
        return n > this.x && n < this.x + 88 && n2 > this.y && n2 < this.y + 12;
    }

    @Override
    public void renderComponent() {
        if (this.mod.getDescription().equalsIgnoreCase("")) {
            return;
        }
        Gui.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth() * 1, this.parent.parent.getY() + this.offset + 12, this.hovered ? -14540254 : -15658735);
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 12, -15658735);
        GL11.glPushMatrix();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        if (this.mod.getDescription().equalsIgnoreCase("")) {
            return;
        }
        Minecraft.getMinecraft();
        Minecraft.fontRendererObj.drawStringWithShadow(this.mod.getDescription(), (this.parent.parent.getX() + 7) * 2, (this.parent.parent.getY() + this.offset + 2) * 2 + 5, -1);
        GL11.glPopMatrix();
    }

    public Description(Button button, Module module, int n) {
        this.parent = button;
        this.mod = module;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = n;
        this.modeIndex = 0;
    }

    @Override
    public void updateComponent(int n, int n2) {
        this.hovered = this.isMouseOnButton(n, n2);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }
}

