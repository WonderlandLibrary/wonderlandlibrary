package today.flux.gui.clickgui.classic.component;

import lombok.Getter;
import lombok.Setter;
import today.flux.gui.clickgui.classic.ClickGUI;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.window.Window;
import today.flux.utility.AnimationTimer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Box extends today.flux.gui.clickgui.classic.component.Component {
    private List<today.flux.gui.clickgui.classic.component.Component> children = new ArrayList<>();
    private int maxHeight;
    @Setter
    @Getter
    private boolean virtual_visible;
    private AnimationTimer openAnimation = new AnimationTimer(15);
    @Getter @Setter
    private boolean lastBox;

    public Box(Window window, int offX, int offY, String title) {
        super(window, offX, offY, title);
        this.width = ClickGUI.settingsWidth;
        this.height = 0;
        this.type = "Box";
    }

    public void addChild(today.flux.gui.clickgui.classic.component.Component component) {
        this.reposition();

        component.box = this;
        component.x = this.x + 10;
        this.children.add(component);
    }

    public void render(int mouseX, int mouseY) {
        if (!virtual_visible && this.openAnimation.getValue() == 0) {
            return;
        }

        final float cropX = this.x + 5;
        final float cropY = Math.min((float) this.parent.y + (float) ((double) (this.parent.height - ClickGUI.defaultHeight)) + (float) ClickGUI.defaultHeight, this.height + this.y);
        final float cropWidth = this.width - 5 * 2;
        final float cropHeight = Math.min((this.y + this.height) - (this.parent.y + 12), this.height);

        if (cropHeight <= 0 || this.height <= (lastBox ? 4.5f : 1f))
            return;

        //render border
        GuiRenderUtils.drawBorderedRect(this.x + 5, this.y, this.width - 5 * 2, this.height - (lastBox ? 4.5f : 0), 0.5f, new Color(0, 0, 0, 1).getRGB(), new Color(175, 175, 175, 220).getRGB());

        GuiRenderUtils.endCrop();
        GuiRenderUtils.beginCrop(cropX, cropY, cropWidth, cropHeight, 2);
        this.children.forEach(c -> c.render(mouseX, mouseY));

        GuiRenderUtils.endCrop();
        GuiRenderUtils.beginCrop(this.parent.crX, this.parent.crY, this.parent.crXX, this.parent.crYY, 2);
    }

    @Override
    public void mouseUpdates(int var1, int var2, boolean var3) {
        if (!virtual_visible && this.openAnimation.getValue() == 0) {
            return;
        }

        this.children.forEach(c -> c.mouseUpdates(var1, var2, var3));
    }

    @Override
    public void update(int mouseX, int mouseY) {
        super.update(mouseX, mouseY);

        this.children.forEach(c -> c.update(mouseX, mouseY));

        //reposition components
        int y = this.y;
        int x = this.x;
        for (Component child : this.children) {
            child.y = y;
            child.x = x;
            y += child.height;
        }

        //animation
        this.openAnimation.update(this.virtual_visible);
        if (this.openAnimation.getValue() == 1.0)
            return;
        this.height = (int) (this.openAnimation.getValue() * (double) this.maxHeight);
        this.parent.repositionComponents();
    }

    public void recalcHeight() {
        this.maxHeight = this.children.stream().mapToInt(c -> c.height).sum() + (lastBox ? 10 : 5);
    }
}
