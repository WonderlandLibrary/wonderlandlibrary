package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.texture.TextureManager.1;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.src.Config;
import net.minecraft.src.RandomMobs;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadersmod.client.ShadersTex;

public class TextureManager implements ITickable, IResourceManagerReloadListener {
   private static final Logger logger = LogManager.getLogger();
   private final Map mapTextureObjects = Maps.newHashMap();
   private final List listTickables = Lists.newArrayList();
   private final Map mapTextureCounters = Maps.newHashMap();
   private IResourceManager theResourceManager;
   private static final String __OBFID = "CL_00001064";

   public TextureManager(IResourceManager resourceManager) {
      this.theResourceManager = resourceManager;
   }

   public ITextureObject getTexture(ResourceLocation textureLocation) {
      return (ITextureObject)this.mapTextureObjects.get(textureLocation);
   }

   public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
      boolean flag = true;
      ITextureObject itextureobject = textureObj;

      try {
         textureObj.loadTexture(this.theResourceManager);
      } catch (IOException var8) {
         logger.warn("Failed to load texture: " + textureLocation, var8);
         itextureobject = TextureUtil.missingTexture;
         this.mapTextureObjects.put(textureLocation, itextureobject);
         flag = false;
      } catch (Throwable var9) {
         CrashReport crashreport = CrashReport.makeCrashReport(var9, "Registering texture");
         CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
         crashreportcategory.addCrashSection("Resource location", textureLocation);
         crashreportcategory.addCrashSectionCallable("Texture object class", new 1(this, textureObj));
         throw new ReportedException(crashreport);
      }

      this.mapTextureObjects.put(textureLocation, itextureobject);
      return flag;
   }

   public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
      if(name.equals("logo")) {
         texture = Config.getMojangLogoTexture(texture);
      }

      Integer integer = (Integer)this.mapTextureCounters.get(name);
      if(integer == null) {
         integer = Integer.valueOf(1);
      } else {
         integer = Integer.valueOf(integer.intValue() + 1);
      }

      this.mapTextureCounters.put(name, integer);
      ResourceLocation resourcelocation = new ResourceLocation(String.format("dynamic/%s_%d", new Object[]{name, integer}));
      this.loadTexture(resourcelocation, texture);
      return resourcelocation;
   }

   public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
      if(this.loadTexture(textureLocation, textureObj)) {
         this.listTickables.add(textureObj);
         return true;
      } else {
         return false;
      }
   }

   public void tick() {
      for(ITickable itickable : this.listTickables) {
         itickable.tick();
      }

   }

   public void bindTexture(ResourceLocation resource) {
      if(Config.isRandomMobs()) {
         resource = RandomMobs.getTextureLocation(resource);
      }

      Object object = (ITextureObject)this.mapTextureObjects.get(resource);
      if(object == null) {
         object = new SimpleTexture(resource);
         this.loadTexture(resource, (ITextureObject)object);
      }

      if(Config.isShaders()) {
         ShadersTex.bindTexture((ITextureObject)object);
      } else {
         TextureUtil.bindTexture(((ITextureObject)object).getGlTextureId());
      }

   }

   public void deleteTexture(ResourceLocation textureLocation) {
      ITextureObject itextureobject = this.getTexture(textureLocation);
      if(itextureobject != null) {
         TextureUtil.deleteTexture(itextureobject.getGlTextureId());
      }

   }

   public void onResourceManagerReload(IResourceManager resourceManager) {
      Config.dbg("*** Reloading textures ***");
      Config.log("Resource packs: " + Config.getResourcePackNames());
      Iterator<ResourceLocation> iterator = this.mapTextureObjects.keySet().iterator();

      while(iterator.hasNext()) {
         ResourceLocation resourcelocation = (ResourceLocation)iterator.next();
         if(resourcelocation.getResourcePath().startsWith("mcpatcher/")) {
            ITextureObject itextureobject = (ITextureObject)this.mapTextureObjects.get(resourcelocation);
            if(itextureobject instanceof AbstractTexture) {
               AbstractTexture abstracttexture = (AbstractTexture)itextureobject;
               abstracttexture.deleteGlTexture();
            }

            iterator.remove();
         }
      }

      for(Entry<ResourceLocation, ITextureObject> entry : this.mapTextureObjects.entrySet()) {
         this.loadTexture((ResourceLocation)entry.getKey(), (ITextureObject)entry.getValue());
      }

   }
}
