package info.sigmaclient.sigma.gui.notifications;

import info.sigmaclient.sigma.modules.gui.Shader;
import info.sigmaclient.sigma.utils.ChatUtils;
import info.sigmaclient.sigma.utils.TimerUtil;
import info.sigmaclient.sigma.utils.font.FontUtil;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;

import java.awt.*;

import static info.sigmaclient.sigma.minimap.minimap.Minimap.mc;
import static net.minecraft.util.math.MathHelper.clamp;


public class Notification {
    private final String message;
    private final String title;
    private final TimerUtil timer;
    private final Type type;
    private float height = 25;
    private final long stayTime;
    public BetterAnimation animation = new BetterAnimation();
    private float posY;
    private final float width;
    private float animationX;
    private boolean direction = false;
    private final TimerUtil animationTimer = new TimerUtil();

    public Notification(String title, String message, Type type, int time) {
        height = 30;
        ScaledResolution sr = new ScaledResolution(mc);
        stayTime = time;
        this.title = title;
        this.message = message;
        this.type = type;
        timer = new TimerUtil();
        timer.reset();
        width = FontUtil.sfuiFont18.getStringWidth(message) + 38;
        animationX = width;
        posY = sr.getScaledHeight() - height;
    }

    public void render(float getY) {
        ScaledResolution sr = new ScaledResolution(mc);
        Color icolor3 = new Color(230, 230, 230, (int) clamp((1 - animation.getAnimationd()) * 255, 0, 255));
        Color icolor2 = new Color(200, 200, 200, (int) clamp((1 - animation.getAnimationd()) * 255, 0, 255));

        direction = isFinished();
        animationX = (float) (width * animation.getAnimationd());

        posY = animate(posY, getY);

        int x1 = (int) ((sr.getScaledWidth() - 6) - width + animationX);
        int y1 = (int) posY;
        Shader.drawRoundRectWithGlowing(x1, y1, x1 + width,y1 + height, new Color(0.117647059f, 0.117647059f, 0.117647059f));
//        Shader.drawRoundRectWithGlowing(x1, y1, x1 + width, height, new Color(0.117647059f, 0.117647059f, 0.117647059f));

        FontUtil.sfuiFontBold18.drawString(title, x1 + 30, y1 + 6, icolor3.getRGB());
        FontUtil.sfuiFont16.drawString(message, x1 + 30, (int) y1 + 18, icolor2.getRGB());

        String icon = "J";
//        switch (type) {
//            case SUCCESS:
//                icon = "H";
//                break;
//            case INFO:
//                icon = "J";
//                break;
//            case ENABLED:
//                icon = "K";
//                break;
//            case DISABLED:
//            case ERROR:
//                icon = "I";
//                break;
//            case WARNING:
//                icon = "L";
//        }

        FontUtil.icon24.drawString(icon, x1 + 5, y1 + 7, icolor2.getRGB());

        if (animationTimer.hasTimeElapsed(50)) {
            animation.update(direction);
            animationTimer.reset();
        }
//        ChatUtils.sendMessageWithPrefix("WDF " + sr.getScaledWidth() + " | " + x1 + "  " + sr.getScaledHeight() + " | " + y1 + " alpha:" + (1 - animation.getAnimationd()));
    }

    private boolean isFinished() {
        return timer.hasTimeElapsed(stayTime);
    }

    public double getHeight() {
        return height;
    }

    public boolean shouldDelete() {
        return (isFinished()) && animationX >= width - 5;
    }

    public float animate(float value, float target) {
        return value + (target - value) / 8f;
    }

    public enum Type {
        SUCCESS("Success"),
        INFO("Information"),
        WARNING("Warning"),
        ERROR("Error"),
        ENABLED("Module enabled"),
        DISABLED("Module disabled");

        final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}