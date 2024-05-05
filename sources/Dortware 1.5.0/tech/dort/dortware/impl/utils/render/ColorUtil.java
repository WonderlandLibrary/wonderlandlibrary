package tech.dort.dortware.impl.utils.render;

import net.minecraft.entity.EntityLivingBase;
import skidmonke.Client;
import tech.dort.dortware.impl.modules.render.Hud;

import java.awt.*;

public class ColorUtil {

    public static int getRainbow(final int speed, final int timeOffset) {
        final Hud hud = Client.INSTANCE.getModuleManager().get(Hud.class);
        final float hue = (System.currentTimeMillis() + timeOffset) % speed;
        switch (hud.rainbowMode.getValue()) {
            case NORMAL:
                return Color.HSBtoRGB(hue / speed, 1F, 1F);
            case OLD:
                return Color.HSBtoRGB(hue / (speed / 2F), 0.5F, 1F);
            case FAST: // Credits: peti
                double rainbowState = Math.ceil((double) ((System.currentTimeMillis() + timeOffset) / 4L));
                rainbowState %= 360;
                return Color.getHSBColor((float) (rainbowState / 360), 0.245F, 3.0F).getRGB();
        }

        return -1;
    }

    public static int astolfoColors(int timeOffset, int yTotal) {
        final float speed = 2900F;
        float hue = (float) (System.currentTimeMillis() % (int) speed) + ((yTotal - timeOffset) * 9);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.HSBtoRGB(hue, .65F, 1F);
    }

    public static int getHealthColor(EntityLivingBase entityLivingBase) {
        final float percentage = 100 * ((entityLivingBase.getHealth() / 2) / (entityLivingBase.getMaxHealth() / 2));
        return percentage > 75 ? 0x19ff19 : percentage > 50 ? 0xffff00 : percentage > 25 ? 0xff5500 : 0xff0900;
    }

    public static int getModeColor() {
        final Hud hud = Client.INSTANCE.getModuleManager().get(Hud.class);
        final Hud.Mode mode = hud.mode.getValue();
        final float red = hud.red.getCastedValue().floatValue();
        final float green = hud.green.getCastedValue().floatValue();
        final float blue = hud.blue.getCastedValue().floatValue();
        final boolean rainbow = hud.rainbow.getValue();

        if (rainbow) {
            return getRainbow(-6000, 0);
        }

        switch (mode) {
            case ASTOLFO:
            case VAZIAK:
                return astolfoColors(-6000, 0);

            case SECTION_1_THE_MYSTERIOUS_PORT_3000:
                return -1;

            case HEADEDWARE:
                return Color.YELLOW.getRGB();

            case MEME:
                return new Color(255, 50, 50).getRGB();

            case SKEET:
                return getRainbow(-6000, 0);
        }

        return new Color(red / 255F, green / 255F, blue / 255F).getRGB();
    }
}
