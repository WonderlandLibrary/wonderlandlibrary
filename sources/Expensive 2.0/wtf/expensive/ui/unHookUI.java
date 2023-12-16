package wtf.expensive.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.impl.util.UnHookFunction;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.IMinecraft;
import wtf.expensive.util.animations.Direction;
import wtf.expensive.util.animations.impl.EaseBackIn;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.BloomHelper;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.GaussianBlur;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

public class unHookUI extends Screen implements IMinecraft {
    public unHookUI(ITextComponent titleIn) {
        super(titleIn);
        animation.setDirection(Direction.FORWARDS);
    }

    public EaseBackIn animation = new EaseBackIn(1250, 1, 2F);
    public boolean textOpen;
    public float animationText;

    public int text;

    public float blur = 0;
    public boolean dir = true;

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        // Плавное изменение значения blur
        updateBlur();

        // Затемнение экрана с размытием
        renderBlurredBackground();

        // Плавное появление текста
        animateTextAppearance();

        UnHookFunction unHookFunction = Managment.FUNCTION_MANAGER.unhook;

        GlStateManager.pushMatrix();

        // Трансформация текста
        transformText();

        // Отрисовка текста
        drawCenteredText(matrixStack);

        // Логика по времени
        handleTimers(unHookFunction);

        // Закрыть экран при завершении анимации
        closeScreenIfAnimationComplete();

        GlStateManager.popMatrix();

        // Отображение времени до самоуничтожения
        displaySelfDestructTimer(matrixStack, unHookFunction);

        // Регистрация отрисовки с эффектом bloom
        registerBloomRenderCall(matrixStack);
    }

    private void updateBlur() {
        blur = AnimationMath.lerp(blur, dir ? 1 : 0, 2);
    }

    private void renderBlurredBackground() {
        RenderUtil.Render2D.drawRect(0, 0, width, height, ColorUtil.rgba(0, 0, 0, 64 * blur));

        if (blur > 0.01) {
            GaussianBlur.startBlur();
            RenderUtil.Render2D.drawRect(0, 0, width, height, -1);
            GaussianBlur.endBlur(blur * 25, 1);
        }
    }

    private void animateTextAppearance() {
        animationText = AnimationMath.lerp(animationText, textOpen ? 1 : 0, 5);
    }

    private void transformText() {
        GlStateManager.translated(width / 2f, height / 2f - (50 * animationText), 0);
        GlStateManager.rotatef((float) (animation.getOutput() * 360), 0, 0, 1);
        float scale = (float) ((float) 2 - MathHelper.clamp(animation.getOutput(), 0, 1));
        GlStateManager.scaled(scale, scale, scale);
        GlStateManager.translated(-(width / 2f), -(height / 2f - (50 * animationText)), 0);
    }

    private void drawCenteredText(MatrixStack matrixStack) {
        Fonts.icons1[130].drawCenteredString(matrixStack, "H", width / 2f, height / 2f - Fonts.icons1[130].getFontHeight() / 2f + 5 - (50 * animationText), RenderUtil.reAlphaInt(-1, (int) (255 * MathHelper.clamp(animation.getOutput(), 0, 1))));
    }

    private void handleTimers(UnHookFunction unHookFunction) {
        if (Managment.FUNCTION_MANAGER.unhook.timerUtil.hasTimeElapsed(1500)) {
            textOpen = true;
        }
        if (Managment.FUNCTION_MANAGER.unhook.timerUtil.hasTimeElapsed(5000)) {
            animation.setDirection(Direction.BACKWARDS);
            ClientUtil.legitMode = true;
        }
        if (Managment.FUNCTION_MANAGER.unhook.timerUtil.hasTimeElapsed(6000)) {
            dir = false;
            textOpen = false;
            unHookFunction.onUnhook();
            unHookFunction.timerUtil.reset();
        }
    }

    private void closeScreenIfAnimationComplete() {
        if (animation.getOutput() == 0) {
            mc.player.closeScreen();
        }
    }

    private void displaySelfDestructTimer(MatrixStack matrixStack, UnHookFunction unHookFunction) {
        long time = MathHelper.clamp((5000 - Managment.FUNCTION_MANAGER.unhook.timerUtil.getTime()), 0, 5000) / 1000;
        float alphaAnimation = MathHelper.clamp(unHookFunction.timerUtil.getTime(), 0, 5000) / 5000f;

        if (animationText > 0.01) {
            String text = (time > 1) ? "секунды" : (time == 1) ? "секунду" : "секунд";

            // Отображение времени до самоуничтожения
            Fonts.msSemiBold[20].drawCenteredString(matrixStack, "Самоуничтожение через: " + time + " " + text, width / 2f, height / 2f - ((Fonts.icons1[130].getFontHeight() / 2f + 15) * (1 - animationText)), RenderUtil.reAlphaInt(-1, (int) ((255 * animationText) * (1 - alphaAnimation))));
        }

        if (time == 0 && alphaAnimation == 1) {
            textOpen = false;
        }
    }

    private void registerBloomRenderCall(MatrixStack matrixStack) {
        BloomHelper.registerRenderCall(() -> {
            GlStateManager.pushMatrix();
            GlStateManager.translated(width / 2f, height / 2f - (50 * animationText), 0);
            GlStateManager.rotatef((float) (animation.getOutput() * 360), 0, 0, 1);
            GlStateManager.translated(-(width / 2f), -(height / 2f - (50 * animationText)), 0);
            Fonts.icons1[130].drawCenteredString(matrixStack, "H", width / 2f, height / 2f - Fonts.icons1[130].getFontHeight() / 2f + 5 - (50 * animationText), RenderUtil.reAlphaInt(-1, (int) (255 * MathHelper.clamp(animation.getOutput(), 0, 1))));
            GlStateManager.popMatrix();
        });
    }

}
