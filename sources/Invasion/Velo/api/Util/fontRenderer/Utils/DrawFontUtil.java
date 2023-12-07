package Velo.api.Util.fontRenderer.Utils;

import java.awt.Font;

import Velo.api.Util.fontRenderer.FontRenderer;
import Velo.api.Util.fontRenderer.Utils.FontUtils;

public class DrawFontUtil {
	
	public static FontRenderer renderer1;
	public static FontRenderer renderer2;
	public static FontRenderer renderer3;
	public static FontRenderer renderer4;
	public static FontRenderer renderer5;
	public static FontRenderer renderer6;
	public static FontRenderer renderer7;
	public static FontRenderer renderer8;
	public static FontRenderer renderer9;
	public static FontRenderer renderer10;
	public static FontRenderer head;
	public static FontRenderer head2;
	public static void loadFonts() {
		loadRenderer1();
		loadRenderer2();
		loadRenderer3();
		loadhead();
		loadhead2();
	}
	
	private static void loadRenderer1() {
		char[] chars = new char[256];
		
		for(int i = 0; i < chars.length; i++) {
			chars[i] = (char) i;
		}
		
		FontUtils glyphPage = new FontUtils(new Font("Arial", Font.PLAIN, 20), true, true);
		
		glyphPage.generateGlyphPage(chars);
		glyphPage.setupTexture();
		
		renderer1 = new FontRenderer(glyphPage, glyphPage, glyphPage, glyphPage);
	}
	
	private static void loadRenderer2() {
		char[] chars = new char[256];
		
		for(int i = 0; i < chars.length; i++) {
			chars[i] = (char) i;
		}
		
		FontUtils glyphPage = new FontUtils(new Font("Arial", Font.PLAIN, 35), true, true);
		
		glyphPage.generateGlyphPage(chars);
		glyphPage.setupTexture();
		
		renderer2 = new FontRenderer(glyphPage, glyphPage, glyphPage, glyphPage);
	}
	
	private static void loadRenderer3() {
		char[] chars = new char[256];
		
		for(int i = 0; i < chars.length; i++) {
			chars[i] = (char) i;
		}
		
		FontUtils glyphPage = new FontUtils(new Font("Arial", Font.PLAIN, 22), true, true);
		
		glyphPage.generateGlyphPage(chars);
		glyphPage.setupTexture();
		
		renderer3 = new FontRenderer(glyphPage, glyphPage, glyphPage, glyphPage);
	}
	private static void loadhead() {
		char[] chars = new char[256];
		
		for(int i = 0; i < chars.length; i++) {
			chars[i] = (char) i;
		}
		
		FontUtils glyphPage = new FontUtils(new Font("Arial", Font.PLAIN, 15), true, true);
		
		glyphPage.generateGlyphPage(chars);
		glyphPage.setupTexture();
		
		head = new FontRenderer(glyphPage, glyphPage, glyphPage, glyphPage);
	}
	private static void loadhead2() {
		char[] chars = new char[256];
		
		for(int i = 0; i < chars.length; i++) {
			chars[i] = (char) i;
		}
		
		FontUtils glyphPage = new FontUtils(new Font("Arial", Font.PLAIN, 15), true, true);
		
		glyphPage.generateGlyphPage(chars);
		glyphPage.setupTexture();
		
		head2 = new FontRenderer(glyphPage, glyphPage, glyphPage, glyphPage);
	}
	
}
