package sudo.module.settings;

import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class ColorSetting extends Setting {

    public float hue;
    public float sat;
    public float bri;
    public float alpha;

    public boolean rainbow = false;

    protected float defaultHue;
    protected float defaultSat;
    protected float defaultBri;

    public ColorSetting(String name, Color color) {
        super(name);
        this.name = name;
        float[] vals = rgbToHsv(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        this.setHSV(vals[0], vals[1], vals[2]);
        this.hue = vals[0];
        this.sat = vals[1];
        this.bri = vals[2];
        this.alpha = vals[3];

        this.defaultHue = hue;
        this.defaultSat = sat;
        this.defaultBri = bri;
    }


    public int getRGB() {
        Color c = new Color(MathHelper.hsvToRgb(hue, sat, bri));
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) alpha).getRGB();
    }

    public float[] getRGBFloat() {
        int col = MathHelper.hsvToRgb(hue, sat, bri);
        return new float[]{(col >> 16 & 255) / 255f, (col >> 8 & 255) / 255f, (col & 255) / 255f, alpha};
    }

    public void setRGB(float r, float g, float b, float a) {
        float[] vals = rgbToHsv(r, g, b, a);
        this.hue = vals[0];
        this.sat = vals[1];
        this.bri = vals[2];
        this.alpha = vals[3];
    }

    public String getHSVString() {
        StringBuilder string = new StringBuilder();
        string.append(getRGBFloat()[0] + "-");
        string.append(getRGBFloat()[1] + "-");
        string.append(getRGBFloat()[2]);
        return string.toString();
    }

    public void setHSV(float h, float s, float v) {
        this.hue = h;
        this.sat = s;
        this.bri = v;
    }

    public Color getColor() {
        return new Color(getRGB());
    }
    public String getHex() {
        return Integer.toHexString(getRGB());
    }

    public String rgbToHex(int rgb) {
        return Integer.toHexString(rgb);
    }

    public float[] rgbToHsv(float r, float g, float b, float a) {
        float[] hsv = Color.RGBtoHSB((int) r, (int) g, (int) b, null);
        return new float[]{hsv[0], hsv[1], hsv[2], a / 255f};
    }

	public int[] hexToRgbInt(String hex) {
		try {
			Color color = Color.decode("#" + hex.replace("#", ""));
			return new int[] {color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()};
		} catch(NumberFormatException e) {
			System.err.println("Invalid hex string!");
			return new int[] {Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), Color.WHITE.getAlpha()};
		}
	}
}