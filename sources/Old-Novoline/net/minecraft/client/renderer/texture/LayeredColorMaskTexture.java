package net.minecraft.client.renderer.texture;

import net.minecraft.block.material.MapColor;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class LayeredColorMaskTexture extends AbstractTexture {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * The location of the texture.
     */
    private final ResourceLocation textureLocation;
    private final List<String> field_174949_h;
    private final List<EnumDyeColor> field_174950_i;

    public LayeredColorMaskTexture(ResourceLocation textureLocationIn, List<String> p_i46101_2_, List<EnumDyeColor> p_i46101_3_) {
        this.textureLocation = textureLocationIn;
        this.field_174949_h = p_i46101_2_;
        this.field_174950_i = p_i46101_3_;
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        final BufferedImage bufferedimage;

        try {
            final BufferedImage bufferedimage1 = TextureUtil.readBufferedImage(resourceManager.getResource(this.textureLocation).getInputStream());
            int i = bufferedimage1.getType();

            if (i == 0) {
                i = 6;
            }

            bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), i);
            final Graphics graphics = bufferedimage.getGraphics();
            graphics.drawImage(bufferedimage1, 0, 0, null);

            for (int j = 0; j < 17 && j < this.field_174949_h.size() && j < this.field_174950_i.size(); ++j) {
                final String s = this.field_174949_h.get(j);
                final MapColor mapcolor = this.field_174950_i.get(j).getMapColor();

                if (s != null) {
                    final InputStream inputStream = resourceManager.getResource(new ResourceLocation(s)).getInputStream();
                    final BufferedImage bufferedImage = TextureUtil.readBufferedImage(inputStream);

                    if (bufferedImage.getWidth() == bufferedimage.getWidth() && bufferedImage.getHeight() == bufferedimage.getHeight() && bufferedImage.getType() == 6) {
                        for (int k = 0; k < bufferedImage.getHeight(); ++k) {
                            for (int l = 0; l < bufferedImage.getWidth(); ++l) {
                                final int i1 = bufferedImage.getRGB(l, k);

                                if ((i1 & -16777216) != 0) {
                                    final int j1 = (i1 & 16711680) << 8 & -16777216;
                                    final int k1 = bufferedimage1.getRGB(l, k);
                                    final int l1 = MathHelper.func_180188_d(k1, mapcolor.colorValue) & 16777215;
                                    bufferedImage.setRGB(l, k, j1 | l1);
                                }
                            }
                        }

                        bufferedimage.getGraphics().drawImage(bufferedImage, 0, 0, null);
                    }
                }
            }
        } catch (IOException ioexception) {
            LOGGER.error("Couldn't load layered image", ioexception);
            return;
        }

        TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
    }

}
