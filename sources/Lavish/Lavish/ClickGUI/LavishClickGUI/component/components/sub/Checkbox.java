// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.ClickGUI.LavishClickGUI.component.components.sub;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import Lavish.ClickGUI.LavishClickGUI.component.components.Button;
import Lavish.ClickGUI.settings.Setting;
import Lavish.ClickGUI.LavishClickGUI.component.Component;

public class Checkbox extends Component
{
    private boolean hovered;
    private Setting op;
    private Button parent;
    private int offset;
    private int x;
    private int y;
    
    public Checkbox(final Setting option, final Button button, final int offset) {
        this.op = option;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
    }
    
    @Override
    public void renderComponent() {
        Gui.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth() * 1, this.parent.parent.getY() + this.offset + 12, this.hovered ? -14540254 : -15658735);
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 12, -15658735);
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.op.getName(), (float)((this.parent.parent.getX() + 10 + 4) * 2 + 5), (float)((this.parent.parent.getY() + this.offset + 2) * 2 + 4), -1);
        GL11.glPopMatrix();
        Gui.drawRect(this.parent.parent.getX() + 3 + 4, this.parent.parent.getY() + this.offset + 3, this.parent.parent.getX() + 9 + 4, this.parent.parent.getY() + this.offset + 9, -6710887);
        if (this.op.getValBoolean()) {
            Gui.drawRect(this.parent.parent.getX() + 4 + 4, this.parent.parent.getY() + this.offset + 4, this.parent.parent.getX() + 8 + 4, this.parent.parent.getY() + this.offset + 8, -10066330);
        }
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.op.setValBoolean(!this.op.getValBoolean());
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}
