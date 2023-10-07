// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.texture;

import org.apache.logging.log4j.LogManager;
import java.io.InputStream;
import net.minecraft.block.material.MapColor;
import java.awt.Graphics;
import java.io.IOException;
import net.minecraft.util.MathHelper;
import net.minecraft.item.EnumDyeColor;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.BufferedImage;
import net.minecraft.client.resources.IResourceManager;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class LayeredColorMaskTexture extends AbstractTexture
{
    private static final Logger field_174947_f;
    private final ResourceLocation field_174948_g;
    private final List field_174949_h;
    private final List field_174950_i;
    private static final String __OBFID = "CL_00002404";
    
    public LayeredColorMaskTexture(final ResourceLocation p_i46101_1_, final List p_i46101_2_, final List p_i46101_3_) {
        this.field_174948_g = p_i46101_1_;
        this.field_174949_h = p_i46101_2_;
        this.field_174950_i = p_i46101_3_;
    }
    
    @Override
    public void loadTexture(final IResourceManager p_110551_1_) throws IOException {
        this.deleteGlTexture();
        BufferedImage var5;
        try {
            final BufferedImage var3 = TextureUtil.func_177053_a(p_110551_1_.getResource(this.field_174948_g).getInputStream());
            int var4 = var3.getType();
            if (var4 == 0) {
                var4 = 6;
            }
            var5 = new BufferedImage(var3.getWidth(), var3.getHeight(), var4);
            final Graphics var6 = var5.getGraphics();
            var6.drawImage(var3, 0, 0, null);
            for (int var7 = 0; var7 < this.field_174949_h.size() && var7 < this.field_174950_i.size(); ++var7) {
                final String var8 = this.field_174949_h.get(var7);
                final MapColor var9 = this.field_174950_i.get(var7).func_176768_e();
                if (var8 != null) {
                    final InputStream var10 = p_110551_1_.getResource(new ResourceLocation(var8)).getInputStream();
                    final BufferedImage var11 = TextureUtil.func_177053_a(var10);
                    if (var11.getWidth() == var5.getWidth() && var11.getHeight() == var5.getHeight() && var11.getType() == 6) {
                        for (int var12 = 0; var12 < var11.getHeight(); ++var12) {
                            for (int var13 = 0; var13 < var11.getWidth(); ++var13) {
                                final int var14 = var11.getRGB(var13, var12);
                                if ((var14 & 0xFF000000) != 0x0) {
                                    final int var15 = (var14 & 0xFF0000) << 8 & 0xFF000000;
                                    final int var16 = var3.getRGB(var13, var12);
                                    final int var17 = MathHelper.func_180188_d(var16, var9.colorValue) & 0xFFFFFF;
                                    var11.setRGB(var13, var12, var15 | var17);
                                }
                            }
                        }
                        var5.getGraphics().drawImage(var11, 0, 0, null);
                    }
                }
            }
        }
        catch (IOException var18) {
            LayeredColorMaskTexture.field_174947_f.error("Couldn't load layered image", (Throwable)var18);
            return;
        }
        TextureUtil.uploadTextureImage(this.getGlTextureId(), var5);
    }
    
    static {
        field_174947_f = LogManager.getLogger();
    }
}
