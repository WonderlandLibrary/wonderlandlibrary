package me.robbanrobbin.jigsaw.font;

import java.awt.Font;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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

	public static void loadFonts() {
		try {
			DataInputStream dis = new DataInputStream(Jigsaw.class.getClassLoader().getResourceAsStream("font.ttf"));
			FileOutputStream outputStream = new FileOutputStream(Jigsaw.getFileMananger().fontFile);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = dis.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			outputStream.close();
			dis.close();
			
			fontFromFile = Font.createFont(Font.TRUETYPE_FONT, Jigsaw.getFileMananger().fontFile);
		}
		catch(Exception e) {
			System.out.println("Error making fonts! :(");
			e.printStackTrace();
		}

		font100 = new JigsawFontRenderer(fontFromFile.deriveFont(Font.PLAIN, 100), true, 8);
		font55 = new JigsawFontRenderer(fontFromFile.deriveFont(Font.PLAIN, 55), true, 8);
		font19 = new JigsawFontRenderer(fontFromFile.deriveFont(Font.PLAIN, 38), true, 8);
		font18 = new JigsawFontRenderer(fontFromFile.deriveFont(Font.PLAIN, 36), true, 8);
		font18bold = new JigsawFontRenderer(fontFromFile.deriveFont(Font.BOLD, 36), true, 8);
		font15 = new JigsawFontRenderer(fontFromFile.deriveFont(Font.PLAIN, 30), true, 8);
		font25 = new JigsawFontRenderer(fontFromFile.deriveFont(Font.PLAIN, 50), true, 8);
	}
}
