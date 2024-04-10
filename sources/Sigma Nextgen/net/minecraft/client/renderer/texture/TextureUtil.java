package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.util.SharedConstants;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;

import static com.mojang.blaze3d.platform.GlStateManager.deleteTexture;
import static net.optifine.Config.getMipmapType;

public class TextureUtil
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static int generateTextureId()
    {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);

        if (SharedConstants.developmentMode)
        {
            int[] aint = new int[ThreadLocalRandom.current().nextInt(15) + 1];
            GlStateManager.genTextures(aint);
            int i = GlStateManager.genTexture();
            GlStateManager.deleteTextures(aint);
            return i;
        }
        else
        {
            return GlStateManager.genTexture();
        }
    }

    public static void releaseTextureId(int textureId)
    {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        deleteTexture(textureId);
    }

    public static void prepareImage(int textureId, int width, int height)
    {
        prepareImage(NativeImage.PixelFormatGLCode.RGBA, textureId, 0, width, height);
    }

    public static void prepareImage(NativeImage.PixelFormatGLCode pixelFormat, int textureId, int width, int height)
    {
        prepareImage(pixelFormat, textureId, 0, width, height);
    }

    public static void prepareImage(int textureId, int mipmapLevel, int width, int height)
    {
        prepareImage(NativeImage.PixelFormatGLCode.RGBA, textureId, mipmapLevel, width, height);
    }

    public static void prepareImage(NativeImage.PixelFormatGLCode pixelFormat, int textureId, int mipmapLevel, int width, int height)
    {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        bindTexture(textureId);

        if (mipmapLevel >= 0)
        {
            GlStateManager.texParameter(3553, 33085, mipmapLevel);
            GlStateManager.texParameter(3553, 33082, 0);
            GlStateManager.texParameter(3553, 33083, mipmapLevel);
            GlStateManager.texParameter(3553, 34049, 0.0F);
        }

        for (int i = 0; i <= mipmapLevel; ++i)
        {
            GlStateManager.texImage2D(3553, i, pixelFormat.getGlFormat(), width >> i, height >> i, 0, 6408, 5121, (IntBuffer)null);
        }
    }

    private static void bindTexture(int textureId)
    {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        GlStateManager.bindTexture(textureId);
    }

    public static ByteBuffer readToBuffer(InputStream inputStreamIn) throws IOException
    {
        ByteBuffer bytebuffer;

        if (inputStreamIn instanceof FileInputStream)
        {
            FileInputStream fileinputstream = (FileInputStream)inputStreamIn;
            FileChannel filechannel = fileinputstream.getChannel();
            bytebuffer = MemoryUtil.memAlloc((int)filechannel.size() + 1);

            while (filechannel.read(bytebuffer) != -1)
            {
            }
        }
        else
        {
            bytebuffer = MemoryUtil.memAlloc(8192);
            ReadableByteChannel readablebytechannel = Channels.newChannel(inputStreamIn);

            while (readablebytechannel.read(bytebuffer) != -1)
            {
                if (bytebuffer.remaining() == 0)
                {
                    bytebuffer = MemoryUtil.memRealloc(bytebuffer, bytebuffer.capacity() * 2);
                }
            }
        }

        return bytebuffer;
    }

    public static String readResourceAsString(InputStream inputStreamIn)
    {
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);
        ByteBuffer bytebuffer = null;

        try
        {
            bytebuffer = readToBuffer(inputStreamIn);
            int i = bytebuffer.position();
            ((Buffer)bytebuffer).rewind();
            return MemoryUtil.memASCII(bytebuffer, i);
        }
        catch (IOException ioexception)
        {
        }
        finally
        {
            if (bytebuffer != null)
            {
                MemoryUtil.memFree(bytebuffer);
            }
        }

        return null;
    }

    public static void initTexture(IntBuffer bufferIn, int width, int height)
    {
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);
        GL11.glPixelStorei(GL11.GL_UNPACK_SWAP_BYTES, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_LSB_FIRST, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, bufferIn);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
    }

    public static void uploadTexture(int textureId, int[] p_110988_1_, int p_110988_2_, int p_110988_3_)
    {
        bindTexture(textureId);
        uploadTextureSub(0, p_110988_1_, p_110988_2_, p_110988_3_, 0, 0, false, false, false);
    }


    public static void allocateTexture(int textureId, int width, int height)
    {
        allocateTextureImpl(textureId, 0, width, height);
    }

    public static void allocateTextureImpl(int glTextureId, int mipmapLevels, int width, int height)
    {
        Object object = TextureUtil.class;

        synchronized (object)
        {
            deleteTexture(glTextureId);
            bindTexture(glTextureId);
        }

        if (mipmapLevels >= 0)
        {
            info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager.glTexParameteri(3553, 33085, mipmapLevels);
            info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager.glTexParameteri(3553, 33082, 0);
            info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager.glTexParameteri(3553, 33083, mipmapLevels);
            info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager.glTexParameterf(3553, 34049, 0.0F);
        }

        for (int i = 0; i <= mipmapLevels; ++i)
        {
            info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager.glTexImage2D(3553, i, 6408, width >> i, height >> i, 0, 32993, 33639, (IntBuffer)null);
        }
    }

    private static void setTextureBlurred(boolean p_147951_0_)
    {
        setTextureBlurMipmap(p_147951_0_, false);
    }

    public static int uploadTextureImageAllocate(int textureId, BufferedImage texture, boolean blur, boolean clamp)
    {
        allocateTexture(textureId, texture.getWidth(), texture.getHeight());
        return uploadTextureImageSub(textureId, texture, 0, 0, blur, clamp);
    }

    public static int uploadTextureImageSub(int textureId, BufferedImage p_110995_1_, int p_110995_2_, int p_110995_3_, boolean p_110995_4_, boolean p_110995_5_)
    {
        bindTexture(textureId);
        uploadTextureImageSubImpl(p_110995_1_, p_110995_2_, p_110995_3_, p_110995_4_, p_110995_5_);
        return textureId;
    }

    private static void uploadTextureImageSubImpl(BufferedImage p_110993_0_, int p_110993_1_, int p_110993_2_, boolean p_110993_3_, boolean p_110993_4_)
    {
        int i = p_110993_0_.getWidth();
        int j = p_110993_0_.getHeight();
        int k = 4194304 / i;
        int[] aint = new int[k * i];
        setTextureBlurred(p_110993_3_);
        setTextureClamped(p_110993_4_);

        for (int l = 0; l < i * j; l += i * k)
        {
            int i1 = l / i;
            int j1 = Math.min(k, j - i1);
            int k1 = i * j1;
            p_110993_0_.getRGB(0, i1, i, j1, aint, 0, i);
            copyToBuffer(aint, k1);
            info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager.glTexSubImage2D(3553, 0, p_110993_1_, p_110993_2_ + i1, i, j1, 32993, 33639, dataBuffer);
        }
    }

    public static BufferedImage readBufferedImage(InputStream imageStream) throws IOException
    {
        if (imageStream == null)
        {
            return null;
        }
        else
        {
            BufferedImage bufferedimage;

            try
            {
                bufferedimage = ImageIO.read(imageStream);
            }
            finally
            {
                IOUtils.closeQuietly(imageStream);
            }

            return bufferedimage;
        }
    }
    private static void copyToBuffer(int[] p_110990_0_, int p_110990_1_)
    {
        copyToBufferPos(p_110990_0_, 0, p_110990_1_);
    }


    public static void setTextureBlurMipmap(boolean p_147954_0_, boolean p_147954_1_)
    {
        if (p_147954_0_)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, p_147954_1_ ? GL11.GL_LINEAR_MIPMAP_LINEAR : GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        }
        else
        {
            int i = getMipmapType();
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, p_147954_1_ ? i : GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }
    }
    public static void setTextureClamped(boolean p_110997_0_)
    {
        if (p_110997_0_)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        }
        else
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        }
    }
    private static void uploadTextureSub(int p_147947_0_, int[] p_147947_1_, int p_147947_2_, int p_147947_3_, int p_147947_4_, int p_147947_5_, boolean p_147947_6_, boolean p_147947_7_, boolean p_147947_8_)
    {
        int i = 4194304 / p_147947_2_;
        setTextureBlurMipmap(p_147947_6_, p_147947_8_);
        setTextureClamped(p_147947_7_);
        int j;

        for (int k = 0; k < p_147947_2_ * p_147947_3_; k += p_147947_2_ * j)
        {
            int l = k / p_147947_2_;
            j = Math.min(i, p_147947_3_ - l);
            int i1 = p_147947_2_ * j;
            copyToBufferPos(p_147947_1_, k, i1);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, p_147947_0_, p_147947_4_, p_147947_5_ + l, p_147947_2_, j, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, (IntBuffer)dataBuffer);
        }
    }
    private static final IntBuffer dataBuffer = GLAllocation.createDirectIntBuffer(4194304);
    private static void copyToBufferPos(int[] p_110994_0_, int p_110994_1_, int p_110994_2_)
    {

        dataBuffer.clear();
        dataBuffer.put(p_110994_0_, p_110994_1_, p_110994_2_);
        dataBuffer.position(0).limit(p_110994_2_);
    }


    public static void func_147953_a(int[] p_147953_0_, int p_147953_1_, int p_147953_2_)
    {
        int[] var3 = new int[p_147953_1_];
        int var4 = p_147953_2_ / 2;

        for (int var5 = 0; var5 < var4; ++var5)
        {
            System.arraycopy(p_147953_0_, var5 * p_147953_1_, var3, 0, p_147953_1_);
            System.arraycopy(p_147953_0_, (p_147953_2_ - 1 - var5) * p_147953_1_, p_147953_0_, var5 * p_147953_1_, p_147953_1_);
            System.arraycopy(var3, 0, p_147953_0_, (p_147953_2_ - 1 - var5) * p_147953_1_, p_147953_1_);
        }
    }
}
