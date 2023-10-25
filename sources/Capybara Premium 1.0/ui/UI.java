package fun.expensive.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.alone.Alone;
import ru.alone.module.imp.render.HUD;
import ru.alone.utils.AnimationUtils;
import ru.alone.utils.MathUtils;
import ru.alone.utils.RenderUtils;
import ru.alone.utils.other.Vec2i;

import java.awt.*;
import java.io.IOException;

public class UI extends GuiScreen {

    private Panel panel;

    public UI() {
        this.panel = new Panel(this, 150, 50, 400);
    }

    @Override
    public void handleMouseInput() throws IOException {
        panel.handleMouseInput();
        super.handleMouseInput();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

        Vec2i calc = Alone.scaleUtils.getMouse(mouseX, mouseY, scaledResolution);
        int height = Alone.scaleUtils.calc(scaledResolution.getScaledHeight());
        int width = Alone.scaleUtils.calc(scaledResolution.getScaledWidth());
        drawDefaultBackground();

        Alone.scaleUtils.pushScale();
        panel.render(width, height, calc.getX(), calc.getY(), partialTicks);
        Alone.scaleUtils.popScale();

        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    @Override
    public void updateScreen() {
        panel.updateScreen();
        super.updateScreen();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        Vec2i calc = Alone.scaleUtils.getMouse(mouseX, mouseY, new ScaledResolution(mc));
        panel.mouseClicked(calc.getX(), calc.getY(), mouseButton);
        super.mouseClicked(calc.getX(), calc.getY(), mouseButton);
    }

    @Override
    public void onGuiClosed() {

        if (mc.entityRenderer.getShaderGroup() != null) {
            mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        Vec2i calc = Alone.scaleUtils.getMouse(mouseX, mouseY, new ScaledResolution(mc));
        panel.mouseRealesed(calc.getX(), calc.getY(), state);
        super.mouseReleased(calc.getX(), calc.getY(), state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        panel.keypressed(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }
}


