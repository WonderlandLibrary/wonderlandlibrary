package tech.atani.client.feature.guis.screens.mainmenu.atani.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.utility.render.animation.advanced.Direction;
import tech.atani.client.utility.render.animation.advanced.impl.DecelerateAnimation;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.shader.render.ingame.RenderableShaders;
import tech.atani.client.utility.render.shader.shaders.RoundedShader;
import tech.atani.client.feature.module.impl.hud.PostProcessing;

import java.awt.*;

public class AtaniButton extends GuiButton
{

    private DecelerateAnimation decelerateAnimation = new DecelerateAnimation(200, 1, Direction.BACKWARDS);

    public AtaniButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public AtaniButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible)
        {
            if(RenderUtil.isHovered(mouseX, mouseY, xPosition, yPosition, width, height))
                this.decelerateAnimation.setDirection(Direction.FORWARDS);
            else
                this.decelerateAnimation.setDirection(Direction.BACKWARDS);
            RenderableShaders.render(true, true, () -> {
                RoundedShader.drawRound(this.xPosition, this.yPosition, this.width, this.height, 5, new Color(0, 0, 0, 205 + (int) (this.decelerateAnimation.getOutput() * 50)));
            });
            if(!PostProcessing.getInstance().isEnabled() || !PostProcessing.getInstance().bloom.getValue())
                RoundedShader.drawRound(this.xPosition, this.yPosition, this.width, this.height, 5, new Color(0, 0, 0, 50));
            FontRenderer fontRenderer = FontStorage.getInstance().findFont("Roboto", 19);
            fontRenderer.drawTotalCenteredString(displayString, this.xPosition + this.width / 2, this.yPosition + this.height / 2, -1);
        }
    }

}
