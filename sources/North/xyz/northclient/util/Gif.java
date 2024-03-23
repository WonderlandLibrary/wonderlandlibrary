package xyz.northclient.util;

import com.madgag.gif.fmsware.GifDecoder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.lwjgl.opengl.GL11.GL_QUADS;

public class Gif {
    private Texture[] textures;
    private int frame;
    public Gif(String path, long speed) {
        try {
            GifDecoder decoder = new GifDecoder();
            decoder.read(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(path)).getInputStream());
            int frameCount = decoder.getFrameCount();
            BufferedImage[] frames = new BufferedImage[frameCount];
            for (int i = 0; i < frameCount; i++) {
                frames[i] = decoder.getFrame(i);
            }

            textures = new Texture[frameCount];
            for (int i = 0; i < frameCount; i++) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(frames[i], "png", baos);
                textures[i] = TextureLoader.getTexture("PNG", new ByteArrayInputStream(baos.toByteArray()));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            while (true) {
                try {
                    frame++;
                    if (frame == textures.length) {
                        frame = 0;
                    }
                    Thread.sleep(speed);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        //});
        }).start();
    }

    public void render(int x,int y, int width,int height) {
        GlStateManager.bindTexture(textures[frame].getTextureID());
        GL11.glBegin(GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(x, y + height);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(x + width, y + height);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
    }

    public void bind() {
        GlStateManager.bindTexture(textures[frame].getTextureID());
    }
}
