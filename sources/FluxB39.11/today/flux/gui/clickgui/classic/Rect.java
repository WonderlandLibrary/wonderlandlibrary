package today.flux.gui.clickgui.classic;

import lombok.Data;

@Data
public class Rect {
    private float x;
    private float y;
    private float width;
    private float height;

    public Rect(){

    }

    public Rect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
