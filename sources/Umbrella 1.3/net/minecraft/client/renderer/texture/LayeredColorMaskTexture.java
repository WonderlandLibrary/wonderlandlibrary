/*
 * Decompiled with CFR 0.150.
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
    private static final Logger field_174947_f = LogManager.getLogger();
    private final ResourceLocation field_174948_g;
    private final List field_174949_h;
    private final List field_174950_i;
    private static final String __OBFID = "CL_00002404";

    public LayeredColorMaskTexture(ResourceLocation p_i46101_1_, List p_i46101_2_, List p_i46101_3_) {
        this.field_174948_g = p_i46101_1_;
        this.field_174949_h = p_i46101_2_;
        this.field_174950_i = p_i46101_3_;
    }

    @Override
    public void loadTexture(IResourceManager p_110551_1_) throws IOException {
        BufferedImage var2;
        this.deleteGlTexture();
        try {
            BufferedImage var3 = TextureUtil.func_177053_a(p_110551_1_.getResource(this.field_174948_g).getInputStream());
            int var4 = var3.getType();
            if (var4 == 0) {
                var4 = 6;
            }
            var2 = new BufferedImage(var3.getWidth(), var3.getHeight(), var4);
            Graphics var5 = var2.getGraphics();
            var5.drawImage(var3, 0, 0, null);
            for (int var6 = 0; var6 < this.field_174949_h.size() && var6 < this.field_174950_i.size(); ++var6) {
                InputStream var9;
                BufferedImage var10;
                String var7 = (String)this.field_174949_h.get(var6);
                MapColor var8 = ((EnumDyeColor)this.field_174950_i.get(var6)).func_176768_e();
                if (var7 == null || (var10 = TextureUtil.func_177053_a(var9 = p_110551_1_.getResource(new ResourceLocation(var7)).getInputStream())).getWidth() != var2.getWidth() || var10.getHeight() != var2.getHeight() || var10.getType() != 6) continue;
                for (int var11 = 0; var11 < var10.getHeight(); ++var11) {
                    for (int var12 = 0; var12 < var10.getWidth(); ++var12) {
                        int var13 = var10.getRGB(var12, var11);
                        if ((var13 & 0xFF000000) == 0) continue;
                        int var14 = (var13 & 0xFF0000) << 8 & 0xFF000000;
                        int var15 = var3.getRGB(var12, var11);
                        int var16 = MathHelper.func_180188_d(var15, var8.colorValue) & 0xFFFFFF;
                        var10.setRGB(var12, var11, var14 | var16);
                    }
                }
                var2.getGraphics().drawImage(var10, 0, 0, null);
            }
        }
        catch (IOException var17) {
            field_174947_f.error("Couldn't load layered image", (Throwable)var17);
            return;
        }
        TextureUtil.uploadTextureImage(this.getGlTextureId(), var2);
    }
}

