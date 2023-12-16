package wtf.expensive.util.drag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.client.MainWindow;
import wtf.expensive.modules.Function;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.ScaleMath;
import wtf.expensive.util.render.Vec2i;

public class Dragging {
    @Expose
    @SerializedName("x")
    private float xPos;
    @Expose
    @SerializedName("y")
    private float yPos;

    public float initialXVal;
    public float initialYVal;

    private float startX, startY;
    private boolean dragging;

    private float width, height;

    @Expose
    @SerializedName("name")
    private String name;

    private final Function module;


    public Dragging(Function module, String name, float initialXVal, float initialYVal) {
        this.module = module;
        this.name = name;
        this.xPos = initialXVal;
        this.yPos = initialYVal;
        this.initialXVal = initialXVal;
        this.initialYVal = initialYVal;
    }

    public Function getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return xPos;
    }

    public void setX(float x) {
        this.xPos = x;
    }

    public float getY() {
        return yPos;
    }

    public void setY(float y) {
        this.yPos = y;
    }

    public final void onDraw(int mouseX, int mouseY, MainWindow res) {
        Vec2i fixed = ScaleMath.getMouse(mouseX,mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();

        if (dragging) {
            xPos = (mouseX - startX);
            yPos = (mouseY - startY);
            if (xPos + width > res.scaledWidth()) {
                xPos = res.scaledWidth() - width;
            }
            if (yPos + height > res.scaledHeight()) {
                yPos = res.scaledHeight() - height;
            }
            if (xPos < 0) {
                xPos = 0;
            }
            if (yPos < 0) {
                yPos = 0;
            }
        }
    }

    public final void onClick(double mouseX, double mouseY, int button) {

        Vec2i fixed = ScaleMath.getMouse((int) mouseX, (int) mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();
        if (button == 0 && RenderUtil.isInRegion(mouseX, mouseY, xPos, yPos, width, height)) {

            dragging = true;
            startX = (int) (mouseX - xPos);
            startY = (int) (mouseY - yPos);
        }
    }

    public final void onRelease(int button) {
        if (button == 0) dragging = false;
    }


}
