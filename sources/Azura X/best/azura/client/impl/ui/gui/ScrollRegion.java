package best.azura.client.impl.ui.gui;

import best.azura.client.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ScrollRegion {

    public int x, y, width, height;
    public int mouse = 0, mouseDelta = 0, offset = 0;
    private boolean hovered = false;

    public ScrollRegion(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    public void end() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public void onTick() {
        this.mouse += mouseDelta;
        this.mouseDelta *= 0.7;

        if(this.mouse > 0) {
            this.mouse -= (this.mouse) / 5;
        }
        if(this.mouse < offset) {
            this.mouse += (offset - this.mouse) / 5;
        }
    }

    public void render(int mouseX, int mouseY) {
        this.hovered = RenderUtil.INSTANCE.isHoveredScaled(x, y, width, height, mouseX, mouseY, 1.0);
        GL11.glScissor(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()/2 < x ? 200 : 0,
                Minecraft.getMinecraft().displayHeight - (y + this.height),
                (this.width + x),
                (this.height));
    }

    public boolean isHovered() {
        return hovered;
    }

    public void handleMouseInput() {
        if(!this.hovered) return;
        int change = Mouse.getEventDWheel();
        if(change > 0) {
            mouseDelta += 10;
        } else if(change < 0) {
            mouseDelta -= 10;
        }
    }

}
