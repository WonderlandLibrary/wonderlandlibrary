package wtf.expensive.util.font;

import java.awt.image.BufferedImage;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.platform.GlStateManager;

public final class TextureHelper {

	public static int loadTexture(BufferedImage image) {
		int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);
        
        try {
	        for (int pixel : pixels) {
	            buffer.put((byte)((pixel >> 16) & 0xFF));
	            buffer.put((byte)((pixel >> 8) & 0xFF));
	            buffer.put((byte)(pixel & 0xFF));
	            buffer.put((byte)((pixel >> 24) & 0xFF));
	        }
	        buffer.flip();
        } catch (BufferOverflowException | ReadOnlyBufferException ex) {return -1;}
        
		int textureID = GlStateManager.genTexture();
		GlStateManager.bindTexture(textureID);
		GlStateManager.texParameter(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
		GlStateManager.texParameter(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, buffer);
		GlStateManager.bindTexture(0);
		
		return textureID;
	}
	
}
