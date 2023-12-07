package Velo.api.Util.fontRenderer.Utils;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class Tff extends FontRenderer {
	private final UnicodeFont font;

	@SuppressWarnings("unchecked")
	public Tff(Font awtFont) {
		super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);

		font = new UnicodeFont(awtFont);
		font.addAsciiGlyphs();
		font.getEffects().add(new ShadowEffect(Color.BLACK, 1, 1, 1F));
		font.getEffects().add(new ColorEffect(Color.WHITE));
		try {
			font.loadGlyphs();
		} catch(SlickException exception) {
			throw new RuntimeException(exception);
		}
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		FONT_HEIGHT = font.getHeight(alphabet) / 2;
	}

	
	
	  public float drawCenteredString(String text, float x2, float y2, int color) {
	        return this.drawString(text, x2 - (float) (this.getStringWidth(text) / 2), y2, color);
	    }
	  
	  
	public int drawString(String string, float x, float y, int color, boolean dropShadow) {
		if(string == null)
			return 0;
		// glClear(256);
		// glMatrixMode(GL_PROJECTION);
		// glLoadIdentity();
		// IntBuffer buffer = BufferUtils.createIntBuffer(16);
		// glGetInteger(GL_VIEWPORT, buffer);
		// glOrtho(0, buffer.get(2), buffer.get(3), 0, 1000, 3000);
		// glMatrixMode(GL_MODELVIEW);
		// glLoadIdentity();
		// glTranslatef(0, 0, -2000);
		glPushMatrix();
		glScaled(0.5, 0.5, 0.5);

		boolean blend = glIsEnabled(GL_BLEND);
		boolean lighting = glIsEnabled(GL_LIGHTING);
		boolean texture = glIsEnabled(GL_TEXTURE_2D);
		if(!blend)
			glEnable(GL_BLEND);
		if(lighting)
			glDisable(GL_LIGHTING);
		if(texture)
			glDisable(GL_TEXTURE_2D);
		x *= 2;
		y *= 2;
		// glBegin(GL_LINES);
		// glVertex3d(x, y, 0);
		// glVertex3d(x + getStringWidth(string), y + FONT_HEIGHT, 0);
		// glEnd();

		font.drawString(x, y, string, new org.newdawn.slick.Color(color));

		if(texture)
			glEnable(GL_TEXTURE_2D);
		if(lighting)
			glEnable(GL_LIGHTING);
		if(!blend)
			glDisable(GL_BLEND);
		glPopMatrix();
		return (int) x;
	}

	public int func_175063_a(String string, float x, float y, int color) {
		return drawString(string, (int) x, (int) y, color);
	}

	@Override
	public int getCharWidth(char c) {
		return getStringWidth(Character.toString(c));
	}

	@Override
	public int getStringWidth(String string) {
		return font.getWidth(string) / 2;
	}

	public int getStringHeight(String string) {
		return font.getHeight(string) / 2;
	}



	 public List<String> wrapWords(String text, double width) {
	        ArrayList<String> finalWords = new ArrayList<>();

	        if (getStringWidth(text) > width) {
	            String[] words = text.split(" ");
	            StringBuilder currentWord = new StringBuilder();
	            char lastColorCode = 65535;

	            for (String word : words) {
	                for (int innerIndex = 0; innerIndex < word.toCharArray().length; innerIndex++) {
	                    char c = word.toCharArray()[innerIndex];

	                    if (c == '\u00a7' && innerIndex < word.toCharArray().length - 1) {
	                        lastColorCode = word.toCharArray()[innerIndex + 1];
	                    }
	                }

	                if (getStringWidth(currentWord + word + " ") < width) {
	                    currentWord.append(word).append(" ");
	                } else {
	                    finalWords.add(currentWord.toString());
	                    currentWord = new StringBuilder("\u00a7" + lastColorCode + word + " ");
	                }
	            }

	            if (currentWord.length() > 0) {
	                if (getStringWidth(currentWord.toString()) < width) {
	                    finalWords.add("\u00a7" + lastColorCode + currentWord + " ");
	                    currentWord = new StringBuilder();
	                } else {
	                    finalWords.addAll(formatString(currentWord.toString(), width));
	                }
	            }
	        } else {
	            finalWords.add(text);
	        }

	        return finalWords;
	    }
	 
	    public List<String> formatString(String string, double width) {
	        ArrayList<String> finalWords = new ArrayList<>();
	        StringBuilder currentWord = new StringBuilder();
	        char lastColorCode = 65535;
	        char[] chars = string.toCharArray();

	        for (int index = 0; index < chars.length; index++) {
	            char c = chars[index];

	            if (c == '\u00a7' && index < chars.length - 1) {
	                lastColorCode = chars[index + 1];
	            }

	            if (getStringWidth(currentWord.toString() + c) < width) {
	                currentWord.append(c);
	            } else {
	                finalWords.add(currentWord.toString());
	                currentWord = new StringBuilder("\u00a7" + lastColorCode + c);
	            }
	        }

	        if (currentWord.length() > 0) {
	            finalWords.add(currentWord.toString());
	        }

	        return finalWords;
	    }
}
