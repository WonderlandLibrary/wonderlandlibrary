package wtf.diablo.gui.notifications;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.AnimationUtil;
import wtf.diablo.utils.render.ColorUtil;
import wtf.diablo.utils.render.RenderUtil;

public class Notification {

    public String title;
    public String description;
    public double duration;
    public NotificationType type;
    public int count;
    public int lastCount = -1;
    public boolean hiding = false;
    public long animationSlideInStart;
    public long startTime;
    public double x, y, width = 350, height = 100;
    public boolean reverseAnimationSlide = false;
    public long animationSlideVerticalStart;

    public Notification(String title, String description, double duration, NotificationType type) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.type = type;
        this.startTime = System.currentTimeMillis();
        this.animationSlideInStart = System.currentTimeMillis();
    }

    public void drawNotification(int count, ScaledResolution sr) {
        double animationSlideDuration = 300;
        this.count = count;
        width = 200;
        height = 40;
        x = sr.getScaledWidth() - width - 5;
        y = sr.getScaledHeight() - ((height + 5) * count) - 60;
        GlStateManager.pushMatrix();
        double animationWidth = (width + 5);
        double animationShit = AnimationUtil.getDoubleFromPercentage(AnimationUtil.getPercentage(animationSlideDuration, animationSlideInStart), animationWidth);
        double animationVertical = 0;
        if (lastCount == -1) {
            lastCount = count;
        }
        if (lastCount != count) {
            double animationSlideVerticalDuration = 200;
            double difference = ((height + 5) * lastCount) - ((height + 5) * count);
            double percentage = AnimationUtil.getPercentage(animationSlideVerticalDuration, animationSlideVerticalStart);
            animationVertical = height - (height - AnimationUtil.getDoubleFromPercentage(percentage, difference)) - height;

            if (percentage >= 100) {
                lastCount = count;
            }
        } else {
            this.animationSlideVerticalStart = System.currentTimeMillis();
        }
        GlStateManager.translate(0, animationVertical, 0);
        if (!reverseAnimationSlide && System.currentTimeMillis() < animationSlideInStart + animationSlideDuration) {
            GlStateManager.translate(width + -animationShit, 0, 0);
        } else if (System.currentTimeMillis() > duration + startTime - animationSlideDuration) {
            if (!reverseAnimationSlide) {
                animationSlideInStart = System.currentTimeMillis();
                reverseAnimationSlide = true;
            } else
                GlStateManager.translate(animationShit, 0, 0);
            //hiding = true;
        }
        RenderUtil.drawRect(x, y, x + width, y + height, 0x80000000);
        long percentile = System.currentTimeMillis() - startTime;
        long total = (long) duration;
        long percent = (long) ((width * percentile) / total);
        RenderUtil.drawRect(x, y + height - 2.5, x + (percent), y + height, ColorUtil.getColor((int) y));
        Fonts.SFReg24.drawStringWithShadow(title, x + 10, y + 10, -1);
        Fonts.SFReg18.drawStringWithShadow(description, x + 10, y + 15 + Fonts.SFReg24.getHeight(), -1);
        GlStateManager.popMatrix();
    }

}
