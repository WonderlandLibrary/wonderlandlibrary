/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package ClickGUIs.Lemon.clickgui.component.components.sub;

import ClickGUIs.Lemon.clickgui.component.Component;
import ClickGUIs.Lemon.clickgui.component.components.Button;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Keybind
extends Component {
    private int offset;
    private int y;
    private boolean hovered;
    private int x;
    private Button parent;
    private boolean binding;

    public Keybind(Button button, int n) {
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = n;
    }

    @Override
    public void renderComponent() {
        Gui.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth() * 1, this.parent.parent.getY() + this.offset + 12, this.hovered ? -14540254 : -15658735);
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 12, -15658735);
        GL11.glPushMatrix();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        Minecraft.getMinecraft();
        Minecraft.fontRendererObj.drawStringWithShadow(this.binding ? "Press a key..." : "Key: " + Keyboard.getKeyName((int)this.parent.mod.getKey()), (this.parent.parent.getX() + 7) * 2, (this.parent.parent.getY() + this.offset + 2) * 2 + 5, -1);
        GL11.glPopMatrix();
    }

    @Override
    public void updateComponent(int n, int n2) {
        this.hovered = this.isMouseOnButton(n, n2);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        if (this.isMouseOnButton(n, n2) && n3 == 0 && this.parent.open) {
            this.binding = !this.binding;
        }
    }

    public boolean isMouseOnButton(int n, int n2) {
        return n > this.x && n < this.x + 88 && n2 > this.y && n2 < this.y + 12;
    }

    @Override
    public void keyTyped(char c, int n) {
        if (this.binding) {
            this.parent.mod.setKey(n);
            this.binding = false;
        }
    }

    @Override
    public void setOff(int n) {
        this.offset = n;
    }
}

