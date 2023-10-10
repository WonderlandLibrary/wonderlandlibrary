package net.minecraft.client.resources;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleReloadableResourceManager implements IReloadableResourceManager {
   private final IMetadataSerializer rmMetadataSerializer;
   private static final Joiner joinerResourcePacks = Joiner.on(", ");
   private static final Logger logger = LogManager.getLogger();
   private final List reloadListeners = Lists.newArrayList();
   private final Set setResourceDomains = Sets.newLinkedHashSet();
   private final Map domainResourceManagers = Maps.newHashMap();

   public void reloadResourcePack(IResourcePack var1) {
      FallbackResourceManager var4;
      for(Iterator var3 = var1.getResourceDomains().iterator(); var3.hasNext(); var4.addResourcePack(var1)) {
         String var2 = (String)var3.next();
         this.setResourceDomains.add(var2);
         var4 = (FallbackResourceManager)this.domainResourceManagers.get(var2);
         if (var4 == null) {
            var4 = new FallbackResourceManager(this.rmMetadataSerializer);
            this.domainResourceManagers.put(var2, var4);
         }
      }

   }

   public void registerReloadListener(IResourceManagerReloadListener var1) {
      this.reloadListeners.add(var1);
      var1.onResourceManagerReload(this);
   }

   private void notifyReloadListeners() {
      Iterator var2 = this.reloadListeners.iterator();

      while(var2.hasNext()) {
         IResourceManagerReloadListener var1 = (IResourceManagerReloadListener)var2.next();
         var1.onResourceManagerReload(this);
      }

   }

   public IResource getResource(ResourceLocation var1) throws IOException {
      IResourceManager var2 = (IResourceManager)this.domainResourceManagers.get(var1.getResourceDomain());
      if (var2 != null) {
         return var2.getResource(var1);
      } else {
         throw new FileNotFoundException(var1.toString());
      }
   }

   public List getAllResources(ResourceLocation var1) throws IOException {
      IResourceManager var2 = (IResourceManager)this.domainResourceManagers.get(var1.getResourceDomain());
      if (var2 != null) {
         return var2.getAllResources(var1);
      } else {
         throw new FileNotFoundException(var1.toString());
      }
   }

   public SimpleReloadableResourceManager(IMetadataSerializer var1) {
      this.rmMetadataSerializer = var1;
   }

   public void reloadResources(List var1) {
      this.clearResources();
      logger.info("Reloading ResourceManager: " + joinerResourcePacks.join(Iterables.transform(var1, new Function(this) {
         final SimpleReloadableResourceManager this$0;

         public Object apply(Object var1) {
            return this.apply((IResourcePack)var1);
         }

         public String apply(IResourcePack var1) {
            return var1.getPackName();
         }

         {
            this.this$0 = var1;
         }
      })));
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         IResourcePack var2 = (IResourcePack)var3.next();
         this.reloadResourcePack(var2);
      }

      this.notifyReloadListeners();
   }

   public Set getResourceDomains() {
      return this.setResourceDomains;
   }

   private void clearResources() {
      this.domainResourceManagers.clear();
      this.setResourceDomains.clear();
   }
}
