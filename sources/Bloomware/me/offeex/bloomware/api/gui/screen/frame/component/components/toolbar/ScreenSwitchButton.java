package me.offeex.bloomware.api.gui.screen.frame.component.components.toolbar;

import com.mojang.blaze3d.systems.RenderSystem;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.gui.screen.frame.component.Component;
import me.offeex.bloomware.client.module.Module;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.awt.*;



public class ScreenSwitchButton extends Component {
    private final Screen screen = null;
    private Module module = null;
    private final Identifier icon;
    private boolean isPressed, isHovered;
    private int x, y;
    private final int width, height, xOffset;

    public ScreenSwitchButton(int width, int height, int xOffset, Identifier icon, Module module) {
        this.module = module;
        this.height = height;
        this.width = width;
        this.icon = icon;
        this.xOffset = xOffset;
    }

    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (isHovered(mouseX, mouseY) && button == 0) {
            if (screen != null) Bloomware.INSTANCE.getMc().setScreen(screen);
            else module.toggle();
            isPressed = true;
        }
    }

    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int mouseButton) {
        isPressed = false;
    }

    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        isHovered = isHovered(mouseX, mouseY);
    }

    @Override
    public void render(MatrixStack matrix) {
        x = Bloomware.INSTANCE.getMc().getWindow().getScaledWidth() - xOffset;
        y = Bloomware.INSTANCE.getMc().getWindow().getScaledHeight() - height;
        if (isHovered) DrawableHelper.fill(matrix, x, y, x + width, y + height, new Color(0, 0, 0, 70).getRGB());

        RenderSystem.setShaderTexture(0, icon);
        RenderSystem.enableBlend();
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        DrawableHelper.drawTexture(matrix, x, y, 0, 0, width, height, width, height);
    }

    public boolean isHovered(final double x, final double y) {
        return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
    }
}
