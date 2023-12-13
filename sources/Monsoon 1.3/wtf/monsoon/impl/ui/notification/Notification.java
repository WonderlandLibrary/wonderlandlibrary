package wtf.monsoon.impl.ui.notification;

import java.awt.Color;

import wtf.monsoon.Monsoon;
import wtf.monsoon.api.util.font.FontUtil;
import wtf.monsoon.api.util.font.MinecraftFontRenderer;
import wtf.monsoon.api.util.misc.Timer;
import wtf.monsoon.api.util.render.AnimationUtil;
import wtf.monsoon.api.util.render.DrawUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

public class Notification {
    private NotificationType type;
    private String title;
    private String messsage;
    private long start;

    private long fadedIn;
    private long fadeOut;
    public long end;

    public Timer timer = new Timer();


    public Notification(NotificationType type, String title, String messsage, int length) {
        this.type = type;
        this.title = title;
        this.messsage = messsage;

        timer.reset();
        end = (long) (Monsoon.INSTANCE.manager.notifs.time.getValue() * 1000);
    }

    public void show() {
        start = System.currentTimeMillis();
    }

    public boolean isShown() {
        return getTime() <= end;
    }

    long getTime() {
        return System.currentTimeMillis() - start;
    }

    public void render(int count) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        //FontRenderer fr = Monsoon.INSTANCE.getFont();
        MinecraftFontRenderer fr = getSelectedFont();
        int width = 120;
        double offset = width;
        int height = 0;
        long time = getTime();
        float barThickness = 3;

        Color color = new Color(0, 0, 0, 200);
        Color color1 = new Color(0, 140, 255);

        if (type == NotificationType.INFO) {
            color1 = new Color(255, 255, 255);
        }
        else if (type == NotificationType.WARNING)
            color1 = new Color(255, 255, 0);
        else if (type == NotificationType.ERROR) {
        	color1 = new Color(204, 0, 18);
            int i = Math.max(0, Math.min(255, (int) (Math.sin(time / 100.0) * 255.0 / 2 + 127.5)));
            color = new Color(i, 0, 0, 220);
        }
        else if (type == NotificationType.SUCCESS) {
            color1 = new Color(0, 255, 0);
        }
        else if (type == NotificationType.FAIL) {
            color1 = new Color(255, 0, 0);
        }

        double x = sr.getScaledWidth() - 45 - fr.getStringWidth(messsage),
                y = sr.getScaledHeight() - 47 * count + 2,
                w = sr.getScaledWidth() - 5,
                h = 30;

        float health = timer.getTime();
        double hpPercentage = health / end;
        hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
        final double hpWidth = (45 + fr.getStringWidth(messsage)) * hpPercentage;
        double progress = AnimationUtil.INSTANCE.animate(hpWidth, 5, end / 1000);


        Gui.drawRect(x, y, w, y + h, color.getRGB());

        //Gui.drawRect(x + 2, y + 3, x + 24, y + 24, -1);

        Gui.drawRect(x, y + 30, x + progress, y + 28, color1.getRGB());

        fr.drawStringWithShadow(title,(float) x + 28, (float) y + 5, -1);
        fr.drawStringWithShadow(messsage, (float) x + 28, (float) y + 15, -1);

        if (type == NotificationType.INFO) {

            DrawUtil.drawBorderedRoundedRect((float) x +4, (float) y +5, (float) x + 22, (float) y + 22, 19, 2, -1, new Color(0,0,0,0).getRGB());
            mc.fontRendererObj.drawString("i", x + 12.5f, y + 9.5f, new Color(0,0,0,255).getRGB());

        } else if (type == NotificationType.WARNING) {

           // DrawUtil.drawRoundedRect((float) x + 2, (float) y + 3, (float) x + 24, (float) y + 24, 19, new Color(255,255,0,255).getRGB());
            mc.fontRendererObj.drawString("\u26A0", x + 12.5f, y + 9.5f, new Color(255,255,0,255).getRGB());

        } else if (type == NotificationType.ERROR) {

            mc.fontRendererObj.drawString("\u26A0", x + 10f, y + 9.5f, new Color(204,0,18,255).getRGB());

        } else if (type == NotificationType.SUCCESS) {

            //DrawUtil.drawRoundedRect((float) x + 2, (float) y + 3, (float) x + 24, (float) y + 24, 19, new Color(0,255,0,255).getRGB());
            mc.fontRendererObj.drawString("\u2714", x + 9.5f, y + 9.5f, new Color(0,255,0,255).getRGB());

        } else if (type == NotificationType.FAIL) {
           // DrawUtil.drawRoundedRect((float) x + 2, (float) y + 3, (float) x + 24, (float) y + 24, 19, new Color(255,0,0,255).getRGB());
            mc.fontRendererObj.drawString("\u2716", x + 9.5f, y + 9.5f, new Color(255,0,0,255).getRGB());
        }
    }

    private static MinecraftFontRenderer getSelectedFont() {
        switch (Monsoon.INSTANCE.manager.notifs.font.getMode()) {
            case "Monsoon":
                return FontUtil.monsoon_arrayList;
            case "Moon":
                return FontUtil.moon;
        }
        return FontUtil.monsoon_arrayList;
    }



}
