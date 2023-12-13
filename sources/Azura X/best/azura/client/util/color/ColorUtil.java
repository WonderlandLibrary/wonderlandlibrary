package best.azura.client.util.color;

import best.azura.client.impl.Client;
import best.azura.client.impl.module.impl.render.HUD;
import net.minecraft.entity.EntityLivingBase;

import java.awt.*;

public class ColorUtil {

    private static Color lastHudColor = Color.WHITE;

    public static Color getRainbow(float speed, double offset, float saturation) {
        float hue = (float) (((System.currentTimeMillis() + offset) % speed) / speed);
        return new Color(Color.HSBtoRGB(hue, saturation, 1.0f));
    }

    public static Color colorFade(Color startColor, Color endColor, double progress) {
        int red = (int) (startColor.getRed() + (endColor.getRed() - startColor.getRed()) * progress);
        int green = (int) (startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * progress);
        int blue = (int) (startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * progress);
        int alpha = (int) (startColor.getAlpha() + (endColor.getAlpha() - startColor.getAlpha()) * progress);
        return new Color(red, green, blue, alpha);
    }

    public static Color getFadingColor(Color colorIn, Color colorOut, int count, Double colorDiff, float speedVal) {
        float speed = (speedVal * 1000);
        //double function = ((System.nanoTime() / 1000000.0f + (count * colorDiff)) % speed) / speed;
        double function = (((System.currentTimeMillis() + (count * colorDiff)) % speed) / speed);
        function *= 2;
        function -= 1;
        function = (float) (-1 * Math.sqrt(function * function) + 1);
        function = Math.max(0, Math.min(1, function));
        return colorFade(new Color(colorIn.getRed(), colorIn.getGreen(), colorIn.getBlue(), colorIn.getAlpha()), new Color(colorOut.getRed(), colorOut.getGreen(), colorOut.getBlue(), colorOut.getAlpha()), function);
    }

    public static Color astolfoColors(int yOffset, double speed) {
        //float hue = (float) (((System.nanoTime() / 1000000.0f + yOffset) % speed) / speed);
        float hue = (float) (((System.currentTimeMillis() + yOffset) % speed) / speed);
        if (hue > 0.5) hue = 0.5F - (hue - 0.5f);
        hue += 0.5F;
        return Color.getHSBColor(hue, 0.5f, 1F);
    }

    public static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static Color getColorFromHealth(final EntityLivingBase entity) {
        return getColorFromHealth(entity, entity.getHealth());
    }

    public static Color getColorFromHealth(final EntityLivingBase entity, final float health) {
        return getColorFromHealth(health, entity.getMaxHealth());
    }

    public static Color getColorFromHealth(final double maxHealth, final float health) {
        Color hpColor = new Color(0x63F632);
        final double percentage = (health / maxHealth);
        if (percentage <= 0.7) hpColor = new Color(0x63F632);
        if (percentage <= 0.6) hpColor = new Color(0x94F632);
        if (percentage <= 0.5) hpColor = new Color(0xD2F632);
        if (percentage <= 0.4) hpColor = new Color(0xF6DC32);
        if (percentage <= 0.3) hpColor = new Color(0xF68725);
        if (percentage <= 0.2) hpColor = new Color(0xF3471C);
        return hpColor;
    }

    public static Color getLastHudColor() {
        return lastHudColor;
    }

    public static Color getHudColor(int count) {
        final HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule(HUD.class);
        if (hud == null) return lastHudColor;
        final Color color;
        final Color colorIn = new Color(hud.arrayListColor.getObject().getColor().getRed(),
                hud.arrayListColor.getObject().getColor().getGreen(),
                hud.arrayListColor.getObject().getColor().getBlue(),
                hud.alphaValue.getObject());

        final Color colorOut = new Color(hud.arrayListColorEnd.getObject().getColor().getRed(),
                hud.arrayListColorEnd.getObject().getColor().getGreen(),
                hud.arrayListColorEnd.getObject().getColor().getBlue(),
                hud.alphaValueOut.getObject());

        switch (hud.colorValue.getObject()) {
            case "Static":
                color = colorIn;
                break;
            case "Gradient":
                color = getFadingColor(colorIn, colorOut, count, hud.colorStep.getObject(), hud.colorSpeed.getObject());
                break;
            case "Rainbow":
                color = getRainbow(hud.colorSpeed.getObject() * 1000, count * hud.colorStep.getObject(), hud.arrayListColor.getObject().getSaturation());
                break;
            case "Astolfo":
                color = astolfoColors(count * hud.colorStep.getObject().intValue(), hud.colorSpeed.getObject() * 1000);
                break;
            default:
                color = lastHudColor;
                break;
        }
        return lastHudColor = color;
    }
}
