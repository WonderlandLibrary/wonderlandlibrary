/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.texture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LayeredColorMaskTexture
extends AbstractTexture {
    private final List<EnumDyeColor> field_174950_i;
    private final List<String> field_174949_h;
    private final ResourceLocation textureLocation;
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void loadTexture(IResourceManager iResourceManager) throws IOException {
        BufferedImage bufferedImage;
        this.deleteGlTexture();
        try {
            BufferedImage bufferedImage2 = TextureUtil.readBufferedImage(iResourceManager.getResource(this.textureLocation).getInputStream());
            int n = bufferedImage2.getType();
            if (n == 0) {
                n = 6;
            }
            bufferedImage = new BufferedImage(bufferedImage2.getWidth(), bufferedImage2.getHeight(), n);
            Graphics graphics = bufferedImage.getGraphics();
            graphics.drawImage(bufferedImage2, 0, 0, null);
            int n2 = 0;
            while (n2 < 17 && n2 < this.field_174949_h.size() && n2 < this.field_174950_i.size()) {
                InputStream inputStream;
                BufferedImage bufferedImage3;
                String string = this.field_174949_h.get(n2);
                MapColor mapColor = this.field_174950_i.get(n2).getMapColor();
                if (string != null && (bufferedImage3 = TextureUtil.readBufferedImage(inputStream = iResourceManager.getResource(new ResourceLocation(string)).getInputStream())).getWidth() == bufferedImage.getWidth() && bufferedImage3.getHeight() == bufferedImage.getHeight() && bufferedImage3.getType() == 6) {
                    int n3 = 0;
                    while (n3 < bufferedImage3.getHeight()) {
                        int n4 = 0;
                        while (n4 < bufferedImage3.getWidth()) {
                            int n5 = bufferedImage3.getRGB(n4, n3);
                            if ((n5 & 0xFF000000) != 0) {
                                int n6 = (n5 & 0xFF0000) << 8 & 0xFF000000;
                                int n7 = bufferedImage2.getRGB(n4, n3);
                                int n8 = MathHelper.func_180188_d(n7, mapColor.colorValue) & 0xFFFFFF;
                                bufferedImage3.setRGB(n4, n3, n6 | n8);
                            }
                            ++n4;
                        }
                        ++n3;
                    }
                    bufferedImage.getGraphics().drawImage(bufferedImage3, 0, 0, null);
                }
                ++n2;
            }
        }
        catch (IOException iOException) {
            LOG.error("Couldn't load layered image", (Throwable)iOException);
            return;
        }
        TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedImage);
    }

    public LayeredColorMaskTexture(ResourceLocation resourceLocation, List<String> list, List<EnumDyeColor> list2) {
        this.textureLocation = resourceLocation;
        this.field_174949_h = list;
        this.field_174950_i = list2;
    }
}

