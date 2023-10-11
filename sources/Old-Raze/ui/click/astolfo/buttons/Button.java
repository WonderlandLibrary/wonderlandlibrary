package markgg.ui.click.astolfo.buttons;


public abstract class Button {

    public Button(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float x, y, width, height;

    public abstract void draw(int x, int y);
    public abstract void action(int x, int y, boolean click, int button);
    public abstract void key(char typedChar, int key);

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY > y && mouseY < y + height;
    }
}
