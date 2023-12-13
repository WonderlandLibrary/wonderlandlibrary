package me.robbanrobbin.jigsaw.font;

import java.awt.Font;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;

public class Fonts {
	public static JigsawFontRenderer font55;
	public static JigsawFontRenderer font100;
	public static JigsawFontRenderer font19;
	public static JigsawFontRenderer font18;
	public static JigsawFontRenderer font18bold;
	public static JigsawFontRenderer font15;
	public static JigsawFontRenderer font25;
	
	public static Font fontFromFile;
	public static Font fontBoldFromFile;
	public static Font fontGuiFromFile;

	public static void loadFonts() {
		
		fontFromFile = unpackFont("font.ttf", Jigsaw.getFileMananger().fontFile);
		fontBoldFromFile = unpackFont("fontbold.ttf", Jigsaw.getFileMananger().fontBoldFile);
		fontGuiFromFile = unpackFont("fontgui.ttf", Jigsaw.getFileMananger().fontGuiFile);
		
		double sizeFactor = 1;
		font100 = new JigsawFontRenderer(fontFromFile.deriveFont(Font.PLAIN, (int)(100 * sizeFactor)), true, 8);
		font55 = new JigsawFontRenderer(fontFromFile.deriveFont(Font.PLAIN, (int)(55 * sizeFactor)), true, 8);
		font19 = new JigsawFontRenderer(fontFromFile.deriveFont(Font.PLAIN, (int)(38 * sizeFactor)), true, 8);
		font18 = new JigsawFontRenderer(fontFromFile.deriveFont(Font.PLAIN, (int)(36 * sizeFactor)), true, 8);
		font18bold = new JigsawFontRenderer(fontBoldFromFile.deriveFont(Font.PLAIN, (int)(36 * sizeFactor)), true, 8);
		font15 = new JigsawFontRenderer(fontFromFile.deriveFont(Font.PLAIN, (int)(30 * sizeFactor)), true, 8);
		font25 = new JigsawFontRenderer(fontFromFile.deriveFont(Font.PLAIN, (int)(50 * sizeFactor)), true, 8);
	}
	
	private static Font unpackFont(String resourceName, File srcFile) {
		try {
			DataInputStream dis = new DataInputStream(Jigsaw.class.getClassLoader().getResourceAsStream(resourceName));
			FileOutputStream outputStream = new FileOutputStream(srcFile);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = dis.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			outputStream.close();
			dis.close();
			
			return Font.createFont(Font.TRUETYPE_FONT, srcFile);
		}
		catch(Exception e) {
			System.out.println("Error unpacking font: " + resourceName);
			e.printStackTrace();
		}
		return null;
	}
	
}
