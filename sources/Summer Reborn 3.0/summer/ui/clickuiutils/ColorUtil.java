package summer.ui.clickuiutils;


import org.lwjgl.opengl.GL11;
import summer.Summer;
import summer.cheat.cheats.render.ClickGUI;

import java.awt.*;

/**
 * @author: AmirCC
 * 01:36 pm, 11/10/2020, Wednesday
 **/
public enum ColorUtil {
    INSTANCE;

    public static void glColor(Color color) {
        GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f);
    }

    public static void glColor(int hex) {
        float alpha = (float) (hex >> 24 & 255) / 255.0f;
        float red = (float) (hex >> 16 & 255) / 255.0f;
        float green = (float) (hex >> 8 & 255) / 255.0f;
        float blue = (float) (hex & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }


    public enum Colors {

        NONE(new Color(255, 255, 255, 0).getRGB()),
        TRANSPARENT(new Color(0, 0, 0, 0).getRGB()),
        WHITE(new Color(255, 255, 255, 255).getRGB()),
        CLOUD(new Color(236, 240, 241, 255).getRGB()),
        DARK_GRAY(new Color(44, 44, 44, 255).getRGB()),
        GREEN(new Color(39, 174, 96, 255).getRGB()),
        TURQUOISE(new Color(22, 160, 133).getRGB()),
        BLUE(new Color(3, 152, 252, 255).getRGB()),
        GOLD(new Color(255, 215, 0, 255).getRGB()),
        RED(new Color(231, 76, 60, 255).getRGB());

        int rgb;
        Colors(int rgb){
            this.rgb = rgb;
        }

        public int getRGB() {
            return rgb;
        }
    }


    public static Color fade(long offset, float fade) {
        float hue = (float) (System.nanoTime() + offset) / 1.0E10F % 1.0F;
        long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()),
                16);
        Color c = new Color((int) color);
        return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade,
                c.getAlpha() / 155.0F);
    }

    public static int getRainbow(int speed, int offset) {
        float hue = (float) ((System.currentTimeMillis() * 1 + offset / 2) % speed * 2);
        hue /= speed;
        float saturation = ClickGUI.guiSaturation.getValFloat();
        return Color.getHSBColor(hue, saturation, 1.0F).getRGB();
    }

}
