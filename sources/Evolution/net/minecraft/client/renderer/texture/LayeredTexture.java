package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.ShadersTex;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LayeredTexture extends AbstractTexture
{
    private static final Logger LOGGER = LogManager.getLogger();
    public final List<String> layeredTextureNames;
    private ResourceLocation textureLocation;

    public LayeredTexture(String... textureNames)
    {
        this.layeredTextureNames = Lists.newArrayList(textureNames);

        if (textureNames.length > 0 && textureNames[0] != null)
        {
            this.textureLocation = new ResourceLocation(textureNames[0]);
        }
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException
    {
        this.deleteGlTexture();
        BufferedImage bufferedimage = null;

        for (String s : this.layeredTextureNames)
        {
            IResource iresource = null;

            try
            {
                if (s != null)
                {
                    iresource = resourceManager.getResource(new ResourceLocation(s));
                    BufferedImage bufferedimage1 = TextureUtil.readBufferedImage(iresource.getInputStream());

                    if (bufferedimage == null)
                    {
                        bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), 2);
                    }

                    bufferedimage.getGraphics().drawImage(bufferedimage1, 0, 0, (ImageObserver)null);
                }

                continue;
            }
            catch (IOException ioexception1)
            {
                LOGGER.error("Couldn't load layered image", (Throwable)ioexception1);
            }
            finally
            {
                IOUtils.closeQuietly((Closeable)iresource);
            }

            return;
        }

        if (Config.isShaders())
        {
            ShadersTex.loadSimpleTexture(this.getGlTextureId(), bufferedimage, false, false, resourceManager, this.textureLocation, this.getMultiTexID());
        }
        else
        {
            TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
        }
    }
}
