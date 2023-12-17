// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.text.TextFormatting;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.data.PackMetadataSection;
import java.io.IOException;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class ResourcePackListEntryServer extends ResourcePackListEntry
{
    private static final Logger LOGGER;
    private final IResourcePack resourcePack;
    private final ResourceLocation resourcePackIcon;
    
    public ResourcePackListEntryServer(final GuiScreenResourcePacks resourcePacksGUIIn, final IResourcePack resourcePackIn) {
        super(resourcePacksGUIIn);
        this.resourcePack = resourcePackIn;
        DynamicTexture dynamictexture;
        try {
            dynamictexture = new DynamicTexture(resourcePackIn.getPackImage());
        }
        catch (IOException var5) {
            dynamictexture = TextureUtil.MISSING_TEXTURE;
        }
        this.resourcePackIcon = this.mc.getTextureManager().getDynamicTextureLocation("texturepackicon", dynamictexture);
    }
    
    @Override
    protected int getResourcePackFormat() {
        return 3;
    }
    
    @Override
    protected String getResourcePackDescription() {
        try {
            final PackMetadataSection packmetadatasection = this.resourcePack.getPackMetadata(this.mc.getResourcePackRepository().rprMetadataSerializer, "pack");
            if (packmetadatasection != null) {
                return packmetadatasection.getPackDescription().getFormattedText();
            }
        }
        catch (JsonParseException jsonparseexception) {
            ResourcePackListEntryServer.LOGGER.error("Couldn't load metadata info", (Throwable)jsonparseexception);
        }
        catch (IOException ioexception) {
            ResourcePackListEntryServer.LOGGER.error("Couldn't load metadata info", (Throwable)ioexception);
        }
        return TextFormatting.RED + "Missing pack.mcmeta :(";
    }
    
    @Override
    protected boolean canMoveRight() {
        return false;
    }
    
    @Override
    protected boolean canMoveLeft() {
        return false;
    }
    
    @Override
    protected boolean canMoveUp() {
        return false;
    }
    
    @Override
    protected boolean canMoveDown() {
        return false;
    }
    
    @Override
    protected String getResourcePackName() {
        return "Server";
    }
    
    @Override
    protected void bindResourcePackIcon() {
        this.mc.getTextureManager().bindTexture(this.resourcePackIcon);
    }
    
    @Override
    protected boolean showHoverOverlay() {
        return false;
    }
    
    @Override
    public boolean isServerPack() {
        return true;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
