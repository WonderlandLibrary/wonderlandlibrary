package net.minecraft.client.resources;

import com.google.common.collect.ImmutableSet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import optifine.ReflectorForge;

public class DefaultResourcePack implements IResourcePack {
   public static final Set defaultResourceDomains = ImmutableSet.of("minecraft", "realms");
   private static final String __OBFID = "CL_00001073";
   private final Map mapAssets;

   public BufferedImage getPackImage() throws IOException {
      return TextureUtil.readBufferedImage(DefaultResourcePack.class.getResourceAsStream("/" + (new ResourceLocation("pack.png")).getResourcePath()));
   }

   public String getPackName() {
      return "Default";
   }

   public DefaultResourcePack(Map var1) {
      this.mapAssets = var1;
   }

   public InputStream getInputStream(ResourceLocation var1) throws IOException {
      InputStream var2 = this.getResourceStream(var1);
      if (var2 != null) {
         return var2;
      } else {
         InputStream var3 = this.getInputStreamAssets(var1);
         if (var3 != null) {
            return var3;
         } else {
            throw new FileNotFoundException(var1.getResourcePath());
         }
      }
   }

   public boolean resourceExists(ResourceLocation var1) {
      return this.getResourceStream(var1) != null || this.mapAssets.containsKey(var1.toString());
   }

   public Set getResourceDomains() {
      return defaultResourceDomains;
   }

   public IMetadataSection getPackMetadata(IMetadataSerializer var1, String var2) throws IOException {
      try {
         FileInputStream var3 = new FileInputStream((File)this.mapAssets.get("pack.mcmeta"));
         return AbstractResourcePack.readMetadata(var1, var3, var2);
      } catch (RuntimeException var4) {
         return null;
      } catch (FileNotFoundException var5) {
         return null;
      }
   }

   public InputStream getInputStreamAssets(ResourceLocation var1) throws IOException, FileNotFoundException {
      File var2 = (File)this.mapAssets.get(var1.toString());
      return var2 != null && var2.isFile() ? new FileInputStream(var2) : null;
   }

   private InputStream getResourceStream(ResourceLocation var1) {
      String var2 = "/assets/" + var1.getResourceDomain() + "/" + var1.getResourcePath();
      InputStream var3 = ReflectorForge.getOptiFineResourceStream(var2);
      return var3 != null ? var3 : DefaultResourcePack.class.getResourceAsStream("/assets/" + var1.getResourceDomain() + "/" + var1.getResourcePath());
   }
}
