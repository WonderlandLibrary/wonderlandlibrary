package net.optifine.entity.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.entity.model.CustomEntityModelParser;
import net.optifine.entity.model.CustomEntityRenderer;
import net.optifine.entity.model.CustomModelRegistry;
import net.optifine.entity.model.CustomModelRenderer;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.entity.model.anim.IModelResolver;
import net.optifine.entity.model.anim.ModelResolver;
import net.optifine.entity.model.anim.ModelUpdater;

public class CustomEntityModels {
    private static boolean active = false;
    private static Map<Class, Render> originalEntityRenderMap = null;
    private static Map<Class, TileEntitySpecialRenderer> originalTileEntityRenderMap = null;

    public static void update() {
        Map<Class, Render> map = CustomEntityModels.getEntityRenderMap();
        Map<Class, TileEntitySpecialRenderer> map1 = CustomEntityModels.getTileEntityRenderMap();
        if (map == null) {
            Config.warn((String)"Entity render map not found, custom entity models are DISABLED.");
        } else if (map1 == null) {
            Config.warn((String)"Tile entity render map not found, custom entity models are DISABLED.");
        } else {
            active = false;
            map.clear();
            map1.clear();
            map.putAll(originalEntityRenderMap);
            map1.putAll(originalTileEntityRenderMap);
            if (Config.isCustomEntityModels()) {
                ResourceLocation[] aresourcelocation = CustomEntityModels.getModelLocations();
                for (int i = 0; i < aresourcelocation.length; ++i) {
                    Class oclass;
                    ResourceLocation resourcelocation = aresourcelocation[i];
                    Config.dbg((String)("CustomEntityModel: " + resourcelocation.getResourcePath()));
                    IEntityRenderer ientityrenderer = CustomEntityModels.parseEntityRender(resourcelocation);
                    if (ientityrenderer == null || (oclass = ientityrenderer.getEntityClass()) == null) continue;
                    if (ientityrenderer instanceof Render) {
                        map.put((Object)oclass, (Object)((Render)ientityrenderer));
                    } else if (ientityrenderer instanceof TileEntitySpecialRenderer) {
                        map1.put((Object)oclass, (Object)((TileEntitySpecialRenderer)ientityrenderer));
                    } else {
                        Config.warn((String)("Unknown renderer type: " + ientityrenderer.getClass().getName()));
                    }
                    active = true;
                }
            }
        }
    }

    private static Map<Class, Render> getEntityRenderMap() {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        Map map = rendermanager.getEntityRenderMap();
        if (map == null) {
            return null;
        }
        if (originalEntityRenderMap == null) {
            originalEntityRenderMap = new HashMap(map);
        }
        return map;
    }

    private static Map<Class, TileEntitySpecialRenderer> getTileEntityRenderMap() {
        Map map = TileEntityRendererDispatcher.instance.mapSpecialRenderers;
        if (originalTileEntityRenderMap == null) {
            originalTileEntityRenderMap = new HashMap(map);
        }
        return map;
    }

    private static ResourceLocation[] getModelLocations() {
        String s = "optifine/cem/";
        String s1 = ".jem";
        ArrayList list = new ArrayList();
        String[] astring = CustomModelRegistry.getModelNames();
        for (int i = 0; i < astring.length; ++i) {
            String s2 = astring[i];
            String s3 = s + s2 + s1;
            ResourceLocation resourcelocation = new ResourceLocation(s3);
            if (!Config.hasResource((ResourceLocation)resourcelocation)) continue;
            list.add((Object)resourcelocation);
        }
        ResourceLocation[] aresourcelocation = (ResourceLocation[])list.toArray((Object[])new ResourceLocation[list.size()]);
        return aresourcelocation;
    }

    private static IEntityRenderer parseEntityRender(ResourceLocation location) {
        try {
            JsonObject jsonobject = CustomEntityModelParser.loadJson((ResourceLocation)location);
            IEntityRenderer ientityrenderer = CustomEntityModels.parseEntityRender(jsonobject, location.getResourcePath());
            return ientityrenderer;
        }
        catch (IOException ioexception) {
            Config.error((String)("" + ioexception.getClass().getName() + ": " + ioexception.getMessage()));
            return null;
        }
        catch (JsonParseException jsonparseexception) {
            Config.error((String)("" + jsonparseexception.getClass().getName() + ": " + jsonparseexception.getMessage()));
            return null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private static IEntityRenderer parseEntityRender(JsonObject obj, String path) {
        CustomEntityRenderer customentityrenderer = CustomEntityModelParser.parseEntityRender((JsonObject)obj, (String)path);
        String s = customentityrenderer.getName();
        ModelAdapter modeladapter = CustomModelRegistry.getModelAdapter((String)s);
        CustomEntityModels.checkNull(modeladapter, "Entity not found: " + s);
        Class oclass = modeladapter.getEntityClass();
        CustomEntityModels.checkNull(oclass, "Entity class not found: " + s);
        IEntityRenderer ientityrenderer = CustomEntityModels.makeEntityRender(modeladapter, customentityrenderer);
        if (ientityrenderer == null) {
            return null;
        }
        ientityrenderer.setEntityClass(oclass);
        return ientityrenderer;
    }

    private static IEntityRenderer makeEntityRender(ModelAdapter modelAdapter, CustomEntityRenderer cer) {
        ModelBase modelbase;
        ResourceLocation resourcelocation = cer.getTextureLocation();
        CustomModelRenderer[] acustommodelrenderer = cer.getCustomModelRenderers();
        float f = cer.getShadowSize();
        if (f < 0.0f) {
            f = modelAdapter.getShadowSize();
        }
        if ((modelbase = modelAdapter.makeModel()) == null) {
            return null;
        }
        ModelResolver modelresolver = new ModelResolver(modelAdapter, modelbase, acustommodelrenderer);
        if (!CustomEntityModels.modifyModel(modelAdapter, modelbase, acustommodelrenderer, modelresolver)) {
            return null;
        }
        IEntityRenderer ientityrenderer = modelAdapter.makeEntityRender(modelbase, f);
        if (ientityrenderer == null) {
            throw new JsonParseException("Entity renderer is null, model: " + modelAdapter.getName() + ", adapter: " + modelAdapter.getClass().getName());
        }
        if (resourcelocation != null) {
            ientityrenderer.setLocationTextureCustom(resourcelocation);
        }
        return ientityrenderer;
    }

    private static boolean modifyModel(ModelAdapter modelAdapter, ModelBase model, CustomModelRenderer[] modelRenderers, ModelResolver mr) {
        for (int i = 0; i < modelRenderers.length; ++i) {
            CustomModelRenderer custommodelrenderer = modelRenderers[i];
            if (CustomEntityModels.modifyModel(modelAdapter, model, custommodelrenderer, mr)) continue;
            return false;
        }
        return true;
    }

    private static boolean modifyModel(ModelAdapter modelAdapter, ModelBase model, CustomModelRenderer customModelRenderer, ModelResolver modelResolver) {
        String s = customModelRenderer.getModelPart();
        ModelRenderer modelrenderer = modelAdapter.getModelRenderer(model, s);
        if (modelrenderer == null) {
            Config.warn((String)("Model part not found: " + s + ", model: " + model));
            return false;
        }
        if (!customModelRenderer.isAttach()) {
            if (modelrenderer.cubeList != null) {
                modelrenderer.cubeList.clear();
            }
            if (modelrenderer.spriteList != null) {
                modelrenderer.spriteList.clear();
            }
            if (modelrenderer.childModels != null) {
                Object[] amodelrenderer = modelAdapter.getModelRenderers(model);
                Set set = Collections.newSetFromMap((Map)new IdentityHashMap());
                set.addAll((Collection)Arrays.asList((Object[])amodelrenderer));
                List list = modelrenderer.childModels;
                Iterator iterator = list.iterator();
                while (iterator.hasNext()) {
                    ModelRenderer modelrenderer1 = (ModelRenderer)iterator.next();
                    if (set.contains((Object)modelrenderer1)) continue;
                    iterator.remove();
                }
            }
        }
        modelrenderer.addChild(customModelRenderer.getModelRenderer());
        ModelUpdater modelupdater = customModelRenderer.getModelUpdater();
        if (modelupdater != null) {
            modelResolver.setThisModelRenderer(customModelRenderer.getModelRenderer());
            modelResolver.setPartModelRenderer(modelrenderer);
            if (!modelupdater.initialize((IModelResolver)modelResolver)) {
                return false;
            }
            customModelRenderer.getModelRenderer().setModelUpdater(modelupdater);
        }
        return true;
    }

    private static void checkNull(Object obj, String msg) {
        if (obj == null) {
            throw new JsonParseException(msg);
        }
    }

    public static boolean isActive() {
        return active;
    }
}
