package today.flux.gui.clickgui.classic.component;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.input.Mouse;
import today.flux.gui.clickgui.classic.window.Window;
import today.flux.utility.MathUtils;

public abstract class Component {
    public int x;
    protected int y;
    public int width;
    public int height;
    public int offX;
    public int offY;
    public String title;
    protected boolean isHovered;
    @Getter @Setter
    private boolean isEnabled = true;
    protected boolean wasMousePressed;
    public Window parent;
    public String type = "Component";
    public boolean editable = true;
    public String group = "default";
    @Getter
    private boolean visible = true;

    protected Box box = null;

    public Component(Window parent, int offX, int offY, String title) {
        this.parent = parent;
        this.offX = offX;
        this.offY = offY;
        this.title = title;
    }

    protected void reposition() {
        if (this.box != null) {
            this.y = this.y - parent.scrollY;
        } else {
            this.x = parent.x + this.offX;
            this.y = parent.y + this.offY - parent.scrollY;
        }
    }

    public boolean contains(int mouseX, int mouseY) {
        float mod = group.contains("_setting") ? 5 : 0;
        return MathUtils.contains(mouseX, mouseY, this.x + mod, (float) this.y, this.x + this.width - mod, this.y + this.height);
    }

    public void noMouseUpdates() {
        this.isHovered = false;
        this.wasMousePressed = Mouse.isButtonDown(0);
    }

    public void setVisible(boolean value) {
        this.visible = value;

        if (!value && this instanceof Box) {
            this.height = 0;
        }
    }

    public void update(int mouseX, int mouseY) {
        this.reposition();
    }

    public void doRender(int var1, int var2){
        if(!this.visible) {
            return;
        }

        this.render(var1, var2);
    }

    public abstract void render(int var1, int var2);

    public abstract void mouseUpdates(int var1, int var2, boolean var3);
}
