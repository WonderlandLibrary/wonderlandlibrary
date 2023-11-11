package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.RandomMobs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadersmod.client.ShadersTex;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TextureManager implements ITickable, IResourceManagerReloadListener {
    private static final Logger logger = LogManager.getLogger();
    private final Map<ResourceLocation, ITextureObject> mapTextureObjects = Maps.newHashMap();
    private final List<ITickableTextureObject> listTickables = Lists.newArrayList();
    private final Map<String, Integer> mapTextureCounters = Maps.newHashMap();
    private final IResourceManager theResourceManager;

    public TextureManager(IResourceManager resourceManager) {
        this.theResourceManager = resourceManager;
    }

    public void bindTexture(ResourceLocation resource) {
        if (Config.isRandomMobs()) resource = RandomMobs.getTextureLocation(resource);

        ITextureObject object = this.mapTextureObjects.get(resource);

        if (object == null) {
            object = new SimpleTexture(resource);
            this.loadTexture(resource, object);
        }

        if (Config.isShaders())
            ShadersTex.bindTexture(object);
        else
            TextureUtil.bindTexture(object.getGlTextureId());
    }

    public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
        if (this.loadTexture(textureLocation, textureObj)) {
            this.listTickables.add(textureObj);
            return true;
        }
        return false;
    }

    public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
        boolean flag = true;
        ITextureObject itextureobject = textureObj;

        try {
            textureObj.loadTexture(this.theResourceManager);
        } catch (IOException ioexception) {
            logger.warn("Failed to load texture: " + textureLocation, ioexception);
            itextureobject = TextureUtil.missingTexture;
            this.mapTextureObjects.put(textureLocation, itextureobject);
            flag = false;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", textureLocation);
            crashreportcategory.addCrashSectionCallable("Texture object class", () -> textureObj.getClass().getName());
            throw new ReportedException(crashreport);
        }

        this.mapTextureObjects.put(textureLocation, itextureobject);
        return flag;
    }

    public ITextureObject getTexture(ResourceLocation textureLocation) {
        return this.mapTextureObjects.get(textureLocation);
    }

    public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
        if (name.equals("logo"))
            texture = Config.getMojangLogoTexture(texture);

        Integer integer = this.mapTextureCounters.get(name);

        if (integer == null)
            integer = 1;
        else
            integer = integer + 1;

        this.mapTextureCounters.put(name, integer);
        ResourceLocation resourcelocation = new ResourceLocation(String.format("dynamic/%s_%d", name, integer));
        this.loadTexture(resourcelocation, texture);
        return resourcelocation;
    }

    public void tick() {
        for (Object itickable : this.listTickables) {
            ((ITickable) itickable).tick();
        }
    }

    public void deleteTexture(ResourceLocation textureLocation) {
        ITextureObject itextureobject = this.getTexture(textureLocation);

        if (itextureobject != null) {
            this.mapTextureObjects.remove(textureLocation);
            TextureUtil.deleteTexture(itextureobject.getGlTextureId());
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        Config.dbg("*** Reloading textures ***");
        Config.log("Resource packs: " + Config.getResourcePackNames());
        Iterator<ResourceLocation> iterator = this.mapTextureObjects.keySet().iterator();

        while (iterator.hasNext()) {
            ResourceLocation resourcelocation = iterator.next();
            if (resourcelocation == null) continue;
            String s = resourcelocation.getResourcePath();

            if (s.startsWith("mcpatcher/") || s.startsWith("optifine/")) {
                ITextureObject itextureobject = this.mapTextureObjects.get(resourcelocation);

                if (itextureobject instanceof AbstractTexture) {
                    AbstractTexture abstracttexture = (AbstractTexture) itextureobject;
                    abstracttexture.deleteGlTexture();
                }

                iterator.remove();
            }
        }

        for (Entry<ResourceLocation, ITextureObject> entry : this.mapTextureObjects.entrySet())
            this.loadTexture(entry.getKey(), entry.getValue());
    }

    public void reloadBannerTextures() {
        for (Entry<ResourceLocation, ITextureObject> entry : this.mapTextureObjects.entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            ITextureObject itextureobject = entry.getValue();

            if (itextureobject instanceof LayeredColorMaskTexture)
                this.loadTexture(resourcelocation, itextureobject);
        }
    }
}
