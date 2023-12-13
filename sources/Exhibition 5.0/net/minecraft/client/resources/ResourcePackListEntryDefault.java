// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.EnumChatFormatting;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.data.PackMetadataSection;
import java.io.IOException;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class ResourcePackListEntryDefault extends ResourcePackListEntry
{
    private static final Logger logger;
    private final IResourcePack field_148320_d;
    private final ResourceLocation field_148321_e;
    private static final String __OBFID = "CL_00000822";
    
    public ResourcePackListEntryDefault(final GuiScreenResourcePacks p_i45052_1_) {
        super(p_i45052_1_);
        this.field_148320_d = this.field_148317_a.getResourcePackRepository().rprDefaultResourcePack;
        DynamicTexture var2;
        try {
            var2 = new DynamicTexture(this.field_148320_d.getPackImage());
        }
        catch (IOException var3) {
            var2 = TextureUtil.missingTexture;
        }
        this.field_148321_e = this.field_148317_a.getTextureManager().getDynamicTextureLocation("texturepackicon", var2);
    }
    
    @Override
    protected String func_148311_a() {
        try {
            final PackMetadataSection var1 = (PackMetadataSection)this.field_148320_d.getPackMetadata(this.field_148317_a.getResourcePackRepository().rprMetadataSerializer, "pack");
            if (var1 != null) {
                return var1.func_152805_a().getFormattedText();
            }
        }
        catch (JsonParseException var2) {
            ResourcePackListEntryDefault.logger.error("Couldn't load metadata info", (Throwable)var2);
        }
        catch (IOException var3) {
            ResourcePackListEntryDefault.logger.error("Couldn't load metadata info", (Throwable)var3);
        }
        return EnumChatFormatting.RED + "Missing pack.mcmeta :(";
    }
    
    @Override
    protected boolean func_148309_e() {
        return false;
    }
    
    @Override
    protected boolean func_148308_f() {
        return false;
    }
    
    @Override
    protected boolean func_148314_g() {
        return false;
    }
    
    @Override
    protected boolean func_148307_h() {
        return false;
    }
    
    @Override
    protected String func_148312_b() {
        return "Default";
    }
    
    @Override
    protected void func_148313_c() {
        this.field_148317_a.getTextureManager().bindTexture(this.field_148321_e);
    }
    
    @Override
    protected boolean func_148310_d() {
        return false;
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
