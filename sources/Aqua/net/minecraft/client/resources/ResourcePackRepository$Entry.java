package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

public class ResourcePackRepository.Entry {
    private final File resourcePackFile;
    private IResourcePack reResourcePack;
    private PackMetadataSection rePackMetadataSection;
    private BufferedImage texturePackIcon;
    private ResourceLocation locationTexturePackIcon;

    private ResourcePackRepository.Entry(File resourcePackFileIn) {
        this.resourcePackFile = resourcePackFileIn;
    }

    public void updateResourcePack() throws IOException {
        this.reResourcePack = this.resourcePackFile.isDirectory() ? new FolderResourcePack(this.resourcePackFile) : new FileResourcePack(this.resourcePackFile);
        this.rePackMetadataSection = (PackMetadataSection)this.reResourcePack.getPackMetadata(ResourcePackRepository.this.rprMetadataSerializer, "pack");
        try {
            this.texturePackIcon = this.reResourcePack.getPackImage();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        if (this.texturePackIcon == null) {
            this.texturePackIcon = ResourcePackRepository.this.rprDefaultResourcePack.getPackImage();
        }
        this.closeResourcePack();
    }

    public void bindTexturePackIcon(TextureManager textureManagerIn) {
        if (this.locationTexturePackIcon == null) {
            this.locationTexturePackIcon = textureManagerIn.getDynamicTextureLocation("texturepackicon", new DynamicTexture(this.texturePackIcon));
        }
        textureManagerIn.bindTexture(this.locationTexturePackIcon);
    }

    public void closeResourcePack() {
        if (this.reResourcePack instanceof Closeable) {
            IOUtils.closeQuietly((Closeable)((Closeable)this.reResourcePack));
        }
    }

    public IResourcePack getResourcePack() {
        return this.reResourcePack;
    }

    public String getResourcePackName() {
        return this.reResourcePack.getPackName();
    }

    public String getTexturePackDescription() {
        return this.rePackMetadataSection == null ? EnumChatFormatting.RED + "Invalid pack.mcmeta (or missing 'pack' section)" : this.rePackMetadataSection.getPackDescription().getFormattedText();
    }

    public int func_183027_f() {
        return this.rePackMetadataSection.getPackFormat();
    }

    public boolean equals(Object p_equals_1_) {
        return this == p_equals_1_ ? true : (p_equals_1_ instanceof ResourcePackRepository.Entry ? this.toString().equals((Object)p_equals_1_.toString()) : false);
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public String toString() {
        return String.format((String)"%s:%s:%d", (Object[])new Object[]{this.resourcePackFile.getName(), this.resourcePackFile.isDirectory() ? "folder" : "zip", this.resourcePackFile.lastModified()});
    }
}
