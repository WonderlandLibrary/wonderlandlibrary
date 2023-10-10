package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.RandomMobs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadersmod.client.ShadersTex;

public class TextureManager implements ITickable, IResourceManagerReloadListener {
   private final Map mapTextureObjects = Maps.newHashMap();
   private final List listTickables = Lists.newArrayList();
   private static final Logger logger = LogManager.getLogger();
   private IResourceManager theResourceManager;
   private final Map mapTextureCounters = Maps.newHashMap();
   private static final String __OBFID = "CL_00001064";

   public void bindTexture(ResourceLocation var1) {
      if (Config.isRandomMobs()) {
         var1 = RandomMobs.getTextureLocation(var1);
      }

      Object var2 = (ITextureObject)this.mapTextureObjects.get(var1);
      if (var2 == null) {
         var2 = new SimpleTexture(var1);
         this.loadTexture(var1, (ITextureObject)var2);
      }

      if (Config.isShaders()) {
         ShadersTex.bindTexture((ITextureObject)var2);
      } else {
         TextureUtil.bindTexture(((ITextureObject)var2).getGlTextureId());
      }

   }

   public boolean loadTickableTexture(ResourceLocation var1, ITickableTextureObject var2) {
      if (var1 == var2) {
         this.listTickables.add(var2);
         return true;
      } else {
         return false;
      }
   }

   public ResourceLocation getDynamicTextureLocation(String var1, DynamicTexture var2) {
      if (var1.equals("logo")) {
         var2 = Config.getMojangLogoTexture(var2);
      }

      Integer var3 = (Integer)this.mapTextureCounters.get(var1);
      if (var3 == null) {
         var3 = 1;
      } else {
         var3 = var3 + 1;
      }

      this.mapTextureCounters.put(var1, var3);
      ResourceLocation var4 = new ResourceLocation(String.format("dynamic/%s_%d", var1, var3));
      this.loadTexture(var4, var2);
      return var4;
   }

   public void onResourceManagerReload(IResourceManager var1) {
      Config.dbg("*** Reloading textures ***");
      Config.log("Resource packs: " + Config.getResourcePackNames());
      Iterator var2 = this.mapTextureObjects.keySet().iterator();

      while(true) {
         ResourceLocation var3;
         String var4;
         do {
            if (!var2.hasNext()) {
               Iterator var8 = this.mapTextureObjects.entrySet().iterator();

               while(var8.hasNext()) {
                  Object var7 = var8.next();
                  this.loadTexture((ResourceLocation)((Entry)var7).getKey(), (ITextureObject)((Entry)var7).getValue());
               }

               return;
            }

            var3 = (ResourceLocation)var2.next();
            var4 = var3.getResourcePath();
         } while(!var4.startsWith("mcpatcher/") && !var4.startsWith("optifine/"));

         ITextureObject var5 = (ITextureObject)this.mapTextureObjects.get(var3);
         if (var5 instanceof AbstractTexture) {
            AbstractTexture var6 = (AbstractTexture)var5;
            var6.deleteGlTexture();
         }

         var2.remove();
      }
   }

   public void deleteTexture(ResourceLocation var1) {
      ITextureObject var2 = this.getTexture(var1);
      if (var2 != null) {
         this.mapTextureObjects.remove(var1);
         TextureUtil.deleteTexture(var2.getGlTextureId());
      }

   }

   public void tick() {
      Iterator var2 = this.listTickables.iterator();

      while(var2.hasNext()) {
         Object var1 = var2.next();
         ((ITickable)var1).tick();
      }

   }

   public ITextureObject getTexture(ResourceLocation var1) {
      return (ITextureObject)this.mapTextureObjects.get(var1);
   }

   public TextureManager(IResourceManager var1) {
      this.theResourceManager = var1;
   }

   public void reloadBannerTextures() {
      Iterator var2 = this.mapTextureObjects.entrySet().iterator();

      while(var2.hasNext()) {
         Object var1 = var2.next();
         ResourceLocation var3 = (ResourceLocation)((Entry)var1).getKey();
         ITextureObject var4 = (ITextureObject)((Entry)var1).getValue();
         if (var4 instanceof LayeredColorMaskTexture) {
            this.loadTexture(var3, var4);
         }
      }

   }
}
