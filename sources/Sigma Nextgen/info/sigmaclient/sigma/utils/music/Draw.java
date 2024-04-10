package info.sigmaclient.sigma.utils.music;

import info.sigmaclient.sigma.utils.render.rendermanagers.DynamicTexture;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import static net.minecraft.client.gui.AbstractGui.drawModalRectWithCustomSizedTexture;

import top.fl0wowp4rty.phantomshield.annotations.Native;
@Native

public class Draw {

public static HashMap<BufferedImage, DynamicTexture> caches = new HashMap<>();
static boolean downloading = false;
public static void rectTextureMasked(float x, float y, float w, float h, int width, int height
, BufferedImage buffer) {
	final DynamicTexture[] id = new DynamicTexture[1];
	if(!caches.containsKey(buffer)){
		id[0] = new DynamicTexture(buffer);
		caches.put(buffer, id[0]);
	}else{
		id[0] = caches.get(buffer);
		GlStateManager.enableTexture2D();
		GlStateManager.bindTexture(id[0].getGlTextureId());
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		drawModalRectWithCustomSizedTexture(x, y, 0,0,
				w + width, h + height,
				w+width
				, h + height);
	}
}
	public static void rectTextureMasked2(float x, float y, float w, float h,float u,float v, float width, float height
			, BufferedImage buffer) {
		final DynamicTexture[] id = new DynamicTexture[1];
		if(!caches.containsKey(buffer)){
			id[0] = new DynamicTexture(buffer);
			caches.put(buffer, id[0]);
		}else{
			id[0] = caches.get(buffer);
			GlStateManager.enableTexture2D();
			GlStateManager.bindTexture(id[0].getGlTextureId());
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			drawModalRectWithCustomSizedTexture(x, y, u,v,
					w, h,
					width
					, height);
		}
	}


	public static void rectTextureMasked(float x, float y, float w, float h, int width, int height
			, BufferedImage buffer , int a) {
		DynamicTexture id;
		if(!caches.containsKey(buffer)){
			id = new DynamicTexture(buffer);
			caches.put(buffer,id);
		}else{
			id = caches.get(buffer);
		}
		GlStateManager.enableTexture2D();
		GlStateManager.bindTexture(id.getGlTextureId());
		drawModalRectWithCustomSizedTexture(x, y, 0,0,
				w + width, h + height,
				w+width
				, h + height);
	}
}
