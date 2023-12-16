package wtf.expensive.notification;

import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector4f;
import org.joml.Vector4i;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.impl.render.HudFunction;
import wtf.expensive.util.animations.Animation;
import wtf.expensive.util.animations.Direction;
import wtf.expensive.util.animations.impl.DecelerateAnimation;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.BloomHelper;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import static wtf.expensive.util.IMinecraft.mc;
import static wtf.expensive.util.render.ColorUtil.rgba;

public class NotificationManager {

    private final CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();

    public void add(String text, String content, int time) {
        notifications.add(new Notification(text, content, time));
    }


    public void draw(MatrixStack stack) {
        int yOffset = 0;
        for (Notification notification : notifications) {

            if (System.currentTimeMillis() - notification.getTime() > (notification.time2 * 1000L) - 300) {
                notification.animation.setDirection(Direction.BACKWARDS);
            } else {
                notification.yAnimation.setDirection(Direction.FORWARDS);
                notification.animation.setDirection(Direction.FORWARDS);
            }
            notification.alpha = (float) notification.animation.getOutput();
            if (System.currentTimeMillis() - notification.getTime() > notification.time2 * 1000L) {
                notification.yAnimation.setDirection(Direction.BACKWARDS);
            }
            if (notification.yAnimation.finished(Direction.BACKWARDS)) {
                notifications.remove(notification);
                continue;
            }
            float x = mc.getMainWindow().scaledWidth() - (Fonts.gilroyBold[14].getWidth(notification.getText()) + 8) - 10;
            float y = mc.getMainWindow().scaledHeight() - 30 -  (mc.player.getActivePotionEffects().stream().sorted(Comparator.comparing(EffectInstance::getDuration)).toList().size() * 16);
            notification.yAnimation.setEndPoint(yOffset);
            notification.yAnimation.setDuration(300);
            y -= (float) (notification.draw(stack) * notification.yAnimation.getOutput());
            notification.setX(x);
            notification.setY(AnimationMath.fast(notification.getY(), y, 15));
            yOffset++;
        }
    }


    private class Notification {
        @Getter
        @Setter
        private float x, y = mc.getMainWindow().scaledHeight() + 24;

        @Getter
        private String text;
        @Getter
        private String content;

        @Getter
        private long time = System.currentTimeMillis();

        public Animation animation = new DecelerateAnimation(500, 1, Direction.FORWARDS);
        public Animation yAnimation = new DecelerateAnimation(500, 1, Direction.FORWARDS);
        float alpha;
        int time2 = 3;

        public Notification(String text, String content, int time) {
            this.text = text;
            this.content = content;
            time2 = time;
        }

        public float draw(MatrixStack stack) {
            float width = Fonts.gilroyBold[14].getWidth(text) + 8;

            int firstColor = RenderUtil.reAlphaInt(Managment.STYLE_MANAGER.getCurrentStyle().getColor(0), (int) (255 * alpha));
            int secondColor = RenderUtil.reAlphaInt(Managment.STYLE_MANAGER.getCurrentStyle().getColor(100), (int) (255 * alpha));
            int thirdColor = RenderUtil.reAlphaInt(Managment.STYLE_MANAGER.getCurrentStyle().getColor(0), (int) (255 * alpha));
            int fourthColor = RenderUtil.reAlphaInt(Managment.STYLE_MANAGER.getCurrentStyle().getColor(100), (int) (255 * alpha));

            if (Managment.FUNCTION_MANAGER.hud2.glowing.get())
                BloomHelper.registerRenderCall(() -> RenderUtil.Render2D.drawRoundedCorner(x + width, y, 4, 20, new Vector4f(0, 0, 3, 3), new Vector4i(firstColor, secondColor, thirdColor, fourthColor)));

            RenderUtil.Render2D.drawRoundedCorner(x + width, y, 4, 20, new Vector4f(0, 0, 3, 3), new Vector4i(firstColor, secondColor, thirdColor, fourthColor));

            if (Managment.FUNCTION_MANAGER.hud2.shadow.get())
                RenderUtil.Render2D.drawShadow(x, y, width, 20, 10, ColorUtil.rgba(0, 0, 0, 64 * alpha));
            RenderUtil.Render2D.drawRoundedCorner(x, y, width, 20, new Vector4f(3, 3, 0, 0), ColorUtil.rgba(0, 0, 0, 128 * alpha));

            Fonts.msMedium[14].drawString(stack, content, x + 4, y + 5, RenderUtil.reAlphaInt(-1, (int) (255 * alpha)));
            Fonts.msMedium[12].drawString(stack, text, x + 4, y + 13, RenderUtil.reAlphaInt(-1, (int) (255 * alpha)));
            return 24;
        }
    }
}
