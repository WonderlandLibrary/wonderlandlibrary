package info.sigmaclient.sigma.gui.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public class JelloFontUtil {

    public static void init(){

	}
	private static Font getJelloFont(float size, boolean bold) {
		Font font;
		try {
			InputStream is = Minecraft.getInstance().getResourceManager()
					.getResource(new ResourceLocation(bold ? "sigmang/fonts/jellomedium.ttf": "sigmang/fonts/jellolight.ttf")).getInputStream();
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			font = font.deriveFont(Font.PLAIN, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", Font.PLAIN, +10);
		}
		return font;
	}
	
	private static Font getJelloFontRegular(int size) {
		Font font = null;
		try {
			InputStream is = Minecraft.getInstance().getResourceManager()
					.getResource(new ResourceLocation("sigmang/fonts/jelloregular.ttf")).getInputStream();
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			font = font.deriveFont(Font.PLAIN, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", Font.PLAIN, +10);
		}
		return font;
	}
	
	private static Font getJelloFontUltralight(int size) {
		Font font = null;
		try {
			InputStream is = Minecraft.getInstance().getResourceManager()
					.getResource(new ResourceLocation("sigmang/fonts/jelloultralight.ttf")).getInputStream();
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			font = font.deriveFont(Font.PLAIN, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", Font.PLAIN, +10);
		}
		return font;
	}
	public static JelloFontRenderer jelloFont10 = JelloFontRenderer.createFontRenderer(getJelloFont(10, false));
	public static JelloFontRenderer jelloFont12 = JelloFontRenderer.createFontRenderer(getJelloFont(12, false));
	public static JelloFontRenderer jelloFont13 = JelloFontRenderer.createFontRenderer(getJelloFont(13, false));
	public static JelloFontRenderer jelloFont14 = JelloFontRenderer.createFontRenderer(getJelloFont(14, false));
	public static JelloFontRenderer jelloFont15 = JelloFontRenderer.createFontRenderer(getJelloFont(15, false));
	public static JelloFontRenderer jelloFont16 = JelloFontRenderer.createFontRenderer(getJelloFont(16, false));
	public static JelloFontRenderer jelloFont19 = JelloFontRenderer.createFontRenderer(getJelloFont(19, false));
	public static JelloFontRenderer jelloFont18 = JelloFontRenderer.createFontRenderer(getJelloFont(18, false));
	public static JelloFontRenderer jelloFont20 = JelloFontRenderer.createFontRenderer(getJelloFont(20, false));
	public static JelloFontRenderer jelloFont21 = JelloFontRenderer.createFontRenderer(getJelloFont(21, false));
	public static JelloFontRenderer jelloFont22 = JelloFontRenderer.createFontRenderer(getJelloFont(22, false));
	public static JelloFontRenderer jelloFont24 = JelloFontRenderer.createFontRenderer(getJelloFont(24, false));
	public static JelloFontRenderer jelloFont25 = JelloFontRenderer.createFontRenderer(getJelloFont(25, false));
	public static JelloFontRenderer jelloFont26 = JelloFontRenderer.createFontRenderer(getJelloFont(26, false));
	public static JelloFontRenderer jelloFont28 = JelloFontRenderer.createFontRenderer(getJelloFont(28, false));
	public static JelloFontRenderer jelloFont30 = JelloFontRenderer.createFontRenderer(getJelloFont(30, false));
	public static JelloFontRenderer jelloFont32 = JelloFontRenderer.createFontRenderer(getJelloFont(32, false));
	public static JelloFontRenderer jelloFont34 = JelloFontRenderer.createFontRenderer(getJelloFont(34, false));
	public static JelloFontRenderer jelloFont36 = JelloFontRenderer.createFontRenderer(getJelloFont(36, false));
	public static JelloFontRenderer jelloFont38 = JelloFontRenderer.createFontRenderer(getJelloFont(38, false));
	public static JelloFontRenderer jelloFont40 = JelloFontRenderer.createFontRenderer(getJelloFont(40, false));

	public static JelloFontRenderer jelloFontBoldSmall = JelloFontRenderer.createFontRenderer(getJelloFont(20, true));
	public static JelloFontRenderer jelloFontMarker = JelloFontRenderer.createFontRenderer(getJelloFont(20, false));
	public static JelloFontRenderer jelloFontBold38 = JelloFontRenderer.createFontRenderer(getJelloFont(38, true));
	public static JelloFontRenderer jelloFontBold40 = JelloFontRenderer.createFontRenderer(getJelloFont(40, true));
	public static JelloFontRenderer jelloFontBold25 = JelloFontRenderer.createFontRenderer(getJelloFont(25, true));
	public static JelloFontRenderer jelloFontBold20 = JelloFontRenderer.createFontRenderer(getJelloFont(20, true));
	public static JelloFontRenderer jelloFontBold18 = JelloFontRenderer.createFontRenderer(getJelloFont(18, true));
	public static JelloFontRenderer jelloFontBold16 = JelloFontRenderer.createFontRenderer(getJelloFont(16, true));
	public static JelloFontRenderer jelloFontBold14 = JelloFontRenderer.createFontRenderer(getJelloFont(14, true));

	public static JelloFontRenderer jelloFontBig = JelloFontRenderer.createFontRenderer(getJelloFont((int)(40), true));
	public static JelloFontRenderer jelloFontMedium = JelloFontRenderer.createFontRenderer(getJelloFont((int)(25), false));
	
}
