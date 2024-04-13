package wtf.dawn.ui.components;

import wtf.dawn.ui.annotations.Component;
import wtf.dawn.ui.util.DrawUtility;

public abstract class BasicComponent {

    public int x, y, width, height;
    boolean outline;
    int typeId;
    int componentId = getClass().getAnnotation(Component.class).componentId();
    String componentName = getClass().getAnnotation(Component.class).name();

    public BasicComponent(int x, int y, int w, int h, boolean outline, int typeId) {
        this.x = x;
        this.y =y;
        this.width = w;
        this.height = h;
        this.outline = outline;
        this.typeId = typeId;
    }

    public void renderComponent(int mouseX, int mouseY) {

        if(outline) {
            DrawUtility.drawRoundedOutline(x, y, x + width, y + height, 1, 5, -1);
        }

    }

    public void onClick(int mouseX, int mouseY, int mouseButton) {
        // TODO add code
    }
}
