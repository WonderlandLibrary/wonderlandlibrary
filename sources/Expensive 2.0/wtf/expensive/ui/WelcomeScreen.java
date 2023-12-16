package wtf.expensive.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import wtf.expensive.util.animations.Direction;
import wtf.expensive.util.animations.impl.EaseBackIn;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;

public class WelcomeScreen extends Screen {

    public EaseBackIn animation = new EaseBackIn(1250, 1, 2F, Direction.BACKWARDS);

    public WelcomeScreen(ITextComponent titleIn) {
        super(titleIn);
        animation.setDirection(Direction.FORWARDS);
    }



    private void transformText() {
        GlStateManager.translated(width / 2f, height / 2f - (50), 0);
        GlStateManager.rotatef((float) (animation.getOutput() * 360), 0, 0, 1);
        float scale = (float) ((float) 2 - MathHelper.clamp(animation.getOutput(), 0, 1));
        GlStateManager.scaled(scale, scale, scale);
        GlStateManager.translated(-(width / 2f), -(height / 2f - (50)), 0);
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        RenderUtil.Render2D.drawRect(0,0,width,height, ColorUtil.rgba(21,25,32, 255));
        animation.setDirection(Direction.FORWARDS);
        GlStateManager.pushMatrix();
        transformText();
        Fonts.icons1[130].drawCenteredString(matrixStack, "H", width / 2f, height / 2f - Fonts.icons1[130].getFontHeight() / 2f + 5 - (50), RenderUtil.reAlphaInt(-1, (int) (255 * MathHelper.clamp(animation.getOutput(), 0, 1))));
        GlStateManager.popMatrix();
    }
}
