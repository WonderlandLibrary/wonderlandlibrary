// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.texture.TextureUtil;
import java.awt.image.BufferedImage;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import java.util.Set;

public class DefaultResourcePack implements IResourcePack
{
    public static final Set defaultResourceDomains;
    private final Map field_152781_b;
    private static final String __OBFID = "CL_00001073";
    
    public DefaultResourcePack(final Map p_i46346_1_) {
        this.field_152781_b = p_i46346_1_;
    }
    
    @Override
    public InputStream getInputStream(final ResourceLocation p_110590_1_) throws IOException {
        final InputStream var2 = this.getResourceStream(p_110590_1_);
        if (var2 != null) {
            return var2;
        }
        final InputStream var3 = this.func_152780_c(p_110590_1_);
        if (var3 != null) {
            return var3;
        }
        throw new FileNotFoundException(p_110590_1_.getResourcePath());
    }
    
    public InputStream func_152780_c(final ResourceLocation p_152780_1_) throws IOException {
        final File var2 = this.field_152781_b.get(p_152780_1_.toString());
        return (var2 != null && var2.isFile()) ? new FileInputStream(var2) : null;
    }
    
    private InputStream getResourceStream(final ResourceLocation p_110605_1_) {
        return DefaultResourcePack.class.getResourceAsStream("/assets/" + p_110605_1_.getResourceDomain() + "/" + p_110605_1_.getResourcePath());
    }
    
    @Override
    public boolean resourceExists(final ResourceLocation p_110589_1_) {
        return this.getResourceStream(p_110589_1_) != null || this.field_152781_b.containsKey(p_110589_1_.toString());
    }
    
    @Override
    public Set getResourceDomains() {
        return DefaultResourcePack.defaultResourceDomains;
    }
    
    @Override
    public IMetadataSection getPackMetadata(final IMetadataSerializer p_135058_1_, final String p_135058_2_) throws IOException {
        try {
            final FileInputStream var3 = new FileInputStream(this.field_152781_b.get("pack.mcmeta"));
            return AbstractResourcePack.readMetadata(p_135058_1_, var3, p_135058_2_);
        }
        catch (RuntimeException var4) {
            return null;
        }
        catch (FileNotFoundException var5) {
            return null;
        }
    }
    
    @Override
    public BufferedImage getPackImage() throws IOException {
        return TextureUtil.func_177053_a(DefaultResourcePack.class.getResourceAsStream("/" + new ResourceLocation("pack.png").getResourcePath()));
    }
    
    @Override
    public String getPackName() {
        return "Default";
    }
    
    static {
        defaultResourceDomains = (Set)ImmutableSet.of((Object)"minecraft", (Object)"realms");
    }
}
