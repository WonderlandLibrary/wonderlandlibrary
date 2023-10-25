package fun.expensive.client.ui.element;
import net.minecraft.client.Minecraft;
import ru.alone.ui.IEventListener;

public class Element implements IEventListener {

    protected Minecraft mc = Minecraft.getMinecraft();

    public double x, y, width, height;

    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getWidth() {
        return width;
    }
    public void setWidth(double width) {
        this.width = width;
    }
    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }

    public boolean collided(int mouseX, int mouseY){
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }
    public boolean collided(final int mouseX, final int mouseY, double posX, double posY, float width, float height) {
        return mouseX >= posX && mouseX <= posX + width && mouseY >= posY && mouseY <= posY + height;
    }

    @Override
    public void handleMouseInput() {}

    @Override
    public void mouseClicked(int x, int y, int button) {}

    @Override
    public void render(int width, int height, int x, int y, float ticks) {	}

    @Override
    public void mouseRealesed(int x, int y, int button) {}

    @Override
    public void keypressed(char c, int key) {}



}
