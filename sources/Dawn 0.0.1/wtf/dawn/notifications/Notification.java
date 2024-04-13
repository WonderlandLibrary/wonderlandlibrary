package wtf.dawn.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import wtf.dawn.Dawn;
import wtf.dawn.utils.font.FontUtil;

import java.awt.*;

import static wtf.dawn.ui.util.DrawUtility.setColor;
import static org.lwjgl.opengl.GL11.*;

public class Notification {
    private NotificationType type;
    private String title;
    public String notifMode;
    private String message;
    private long start;

    private long fadeIn;
    private long fadeOut;
    private long end;

    public Notification(NotificationType type, String title, String message, int length) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.notifMode = NotificationManager.notifMode;

        fadeIn = 450 * length;
        fadeOut = fadeIn + 450 * length;
        end = fadeIn + fadeOut;
    }

    public void show() {
        start = System.currentTimeMillis();
    }

    public boolean isShown() {
        return getTime() <= end;
    }

    private long getTime() {
        return System.currentTimeMillis() - start;
    }

    public void render() {
        double offset = 2;
        int width = 130;
        int height = 30;
        int radius = 10;
        long time = getTime();

        if(time < fadeIn) {
            offset = Math.tanh(time / (double) (fadeIn) * 3.0) * width;
        } else if(time > fadeOut) {
            offset = (Math.tanh(3.0 - (time - fadeOut) / (double) (end - fadeOut) * 3.0) * width);
        } else { offset = width; }

        Color color1;
        Color color;

        if(notifMode == "LIGHT") {
            color = new Color(255, 255, 255, 200);
            color1 = new Color(0, 0, 0, 200);
        } else {
            color1 = new Color(255, 255, 255, 200);
            color = new Color(0, 0, 0, 200);
        }
        if(type == NotificationType.INFO)
            color1 = new Color(0, 255, 255, 200);
        else if(type == NotificationType.ERROR)
            color1 = new Color(255, 0, 0, 200);
        else if(type == NotificationType.WARNING)
            color1 = new Color(255, 0, 0, 200);
        else if(type == NotificationType.ENABLED)
            color1 = new Color(0, 255, 0, 200);
        else if(type == NotificationType.DISABLED)
            color1 = new Color(255, 0, 0, 200);

            drawRoundedRect((float) (GuiScreen.width - offset), GuiScreen.height - 5 - height, GuiScreen.width - 2, GuiScreen.height - 2, radius, color.getRGB());

            FontUtil.normal.drawStringWithShadow(title, GuiScreen.width - offset + 5, GuiScreen.height - height + 1, color1.getRGB());
            FontUtil.normal.drawStringWithShadow(message, GuiScreen.width - offset + 5, GuiScreen.height - 17, -1);
    }

    public static void drawRoundedRect(float x, float y, float x1, float y1, float radius, int color) {
        glPushAttrib(0);
        glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        setColor(color);
        glEnable(GL_LINE_SMOOTH);
        glBegin(GL_POLYGON);
        int i;
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius);
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glScaled(2.0D, 2.0D, 2.0D);
        glEnable(GL_BLEND);
        glPopAttrib();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
