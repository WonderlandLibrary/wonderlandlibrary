
package info.sigmaclient.sigma.utils.render.rendermanagers;

import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.resources.IResourceManager;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class DynamicTexture extends Texture
{
    private final int[] dynamicTextureData;

    /** width of this icon in pixels */
    private final int width;

    /** height of this icon in pixels */
    private final int height;
    private static final String __OBFID = "CL_00001048";
    private boolean shadersInitialized;

    public DynamicTexture(BufferedImage bufferedImage)
    {
        this(bufferedImage.getWidth(), bufferedImage.getHeight());
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(),
                bufferedImage.getHeight(), this.dynamicTextureData, 0,
                bufferedImage.getWidth());
        this.updateDynamicTexture();
    }

    public DynamicTexture(int textureWidth, int textureHeight)
    {
        this.shadersInitialized = false;
        this.width = textureWidth;
        this.height = textureHeight;
        this.dynamicTextureData = new int[textureWidth * textureHeight * 3];
        TextureUtil.prepareImage(this.getGlTextureId(), textureWidth, textureHeight);
    }


    public void updateDynamicTexture()
    {
        TextureUtil.uploadTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height);
    }

    public int[] getTextureData()
    {
        return this.dynamicTextureData;
    }

    @Override
    public void loadTexture(IResourceManager manager) throws IOException {

    }
}
