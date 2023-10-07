package cc.novoline.gui.group2;

import static cc.novoline.utils.RenderUtils.drawRect;

/**
 * @author xDelsy
 */
public final class BasicGroup extends AbstractGroup {

    /* constructors */
    public BasicGroup(int color, int x, int y, int width, int height) {
        super(color, x, y, width, height);
    }

    /* methods */
    @Override
    public void onDraw(int mouseX, int mouseY) {
        drawRect(this.x, this.y, this.width, this.height, this.color);
    }

}
