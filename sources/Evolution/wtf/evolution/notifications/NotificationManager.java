package wtf.evolution.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import wtf.evolution.helpers.ScaleUtil;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.helpers.render.Translate;

import java.awt.*;
import java.util.ArrayList;

public class NotificationManager {

    public ArrayList<Notification> notifications = new ArrayList<>();

    public void call(String head, String text, NotificationType type) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        Notification n = new Notification(text, type, head);
        n.translate = new Translate(ScaleUtil.calc(sr.getScaledWidth()) - 50, ScaleUtil.calc(sr.getScaledHeight()) - 10);
        notifications.add(n);

    }

    public void render() {
        ScaleUtil.scale_pre();
        int offset = 30;
        int x = 5;

        try {
            for (Notification n : notifications) {

if ((System.currentTimeMillis() - n.startTime) > 2100) {
    continue;
}

                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
if ((System.currentTimeMillis() - n.startTime) > 2000) {
    n.translate.interpolate(ScaleUtil.calc(sr.getScaledWidth()) + 100, ScaleUtil.calc(sr.getScaledHeight()) - offset, Minecraft.getMinecraft().deltaTime() * 10);
}
else
    n.translate.interpolate(ScaleUtil.calc(sr.getScaledWidth()) - 115, ScaleUtil.calc(sr.getScaledHeight()) - offset, Minecraft.getMinecraft().deltaTime()* 10);

                RenderUtil.drawBlurredShadow(Math.round((float) n.translate.getX()), Math.round((float) n.translate.getY()), 110, 25, 15, Color.BLACK);
                RoundedUtil.drawRound(Math.round((float) n.translate.getX()), Math.round((float) n.translate.getY()), 110, 25, 2, new Color(20, 20, 20, 255));
                Fonts.REG16.drawString(n.header, Math.round((float) n.translate.getX() + x), Math.round((float) n.translate.getY() + 5), new Color(255, 255, 255, 255).getRGB());
                Fonts.RUB14.drawString(n.text, Math.round((float) n.translate.getX() + x), Math.round((float) n.translate.getY() + 15), new Color(255, 255, 255, 255).getRGB());
                offset += 30;


            }
        } catch (Exception e) {

        }
        if (notifications.size() >= 20) {
            notifications.remove(0);
        }
        ScaleUtil.scale_post();
    }
}
