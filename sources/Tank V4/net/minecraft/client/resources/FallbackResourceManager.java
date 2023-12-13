package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FallbackResourceManager implements IResourceManager {
   private static final Logger logger = LogManager.getLogger();
   private final IMetadataSerializer frmMetadataSerializer;
   protected final List resourcePacks = Lists.newArrayList();

   public List getAllResources(ResourceLocation var1) throws IOException {
      ArrayList var2 = Lists.newArrayList();
      ResourceLocation var3 = getLocationMcmeta(var1);
      Iterator var5 = this.resourcePacks.iterator();

      while(var5.hasNext()) {
         IResourcePack var4 = (IResourcePack)var5.next();
         if (var4.resourceExists(var1)) {
            InputStream var6 = var4.resourceExists(var3) ? this.getInputStream(var3, var4) : null;
            var2.add(new SimpleResource(var4.getPackName(), var1, this.getInputStream(var1, var4), var6, this.frmMetadataSerializer));
         }
      }

      if (var2.isEmpty()) {
         throw new FileNotFoundException(var1.toString());
      } else {
         return var2;
      }
   }

   public FallbackResourceManager(IMetadataSerializer var1) {
      this.frmMetadataSerializer = var1;
   }

   static ResourceLocation getLocationMcmeta(ResourceLocation var0) {
      return new ResourceLocation(var0.getResourceDomain(), var0.getResourcePath() + ".mcmeta");
   }

   static Logger access$0() {
      return logger;
   }

   public Set getResourceDomains() {
      return null;
   }

   protected InputStream getInputStream(ResourceLocation var1, IResourcePack var2) throws IOException {
      InputStream var3 = var2.getInputStream(var1);
      return (InputStream)(logger.isDebugEnabled() ? new FallbackResourceManager.InputStreamLeakedResourceLogger(var3, var1, var2.getPackName()) : var3);
   }

   public void addResourcePack(IResourcePack var1) {
      this.resourcePacks.add(var1);
   }

   public IResource getResource(ResourceLocation var1) throws IOException {
      IResourcePack var2 = null;
      ResourceLocation var3 = getLocationMcmeta(var1);

      for(int var4 = this.resourcePacks.size() - 1; var4 >= 0; --var4) {
         IResourcePack var5 = (IResourcePack)this.resourcePacks.get(var4);
         if (var2 == null && var5.resourceExists(var3)) {
            var2 = var5;
         }

         if (var5.resourceExists(var1)) {
            InputStream var6 = null;
            if (var2 != null) {
               var6 = this.getInputStream(var3, var2);
            }

            return new SimpleResource(var5.getPackName(), var1, this.getInputStream(var1, var5), var6, this.frmMetadataSerializer);
         }
      }

      throw new FileNotFoundException(var1.toString());
   }

   static class InputStreamLeakedResourceLogger extends InputStream {
      private final String field_177328_b;
      private final InputStream field_177330_a;
      private boolean field_177329_c = false;

      public InputStreamLeakedResourceLogger(InputStream var1, ResourceLocation var2, String var3) {
         this.field_177330_a = var1;
         ByteArrayOutputStream var4 = new ByteArrayOutputStream();
         (new Exception()).printStackTrace(new PrintStream(var4));
         this.field_177328_b = "Leaked resource: '" + var2 + "' loaded from pack: '" + var3 + "'\n" + var4.toString();
      }

      public void close() throws IOException {
         this.field_177330_a.close();
         this.field_177329_c = true;
      }

      public int read() throws IOException {
         return this.field_177330_a.read();
      }

      protected void finalize() throws Throwable {
         if (!this.field_177329_c) {
            FallbackResourceManager.access$0().warn(this.field_177328_b);
         }

         super.finalize();
      }
   }
}
