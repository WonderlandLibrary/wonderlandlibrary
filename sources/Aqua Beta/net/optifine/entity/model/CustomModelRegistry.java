package net.optifine.entity.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.src.Config;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.entity.model.ModelAdapterArmorStand;
import net.optifine.entity.model.ModelAdapterBanner;
import net.optifine.entity.model.ModelAdapterBat;
import net.optifine.entity.model.ModelAdapterBlaze;
import net.optifine.entity.model.ModelAdapterBoat;
import net.optifine.entity.model.ModelAdapterBook;
import net.optifine.entity.model.ModelAdapterCaveSpider;
import net.optifine.entity.model.ModelAdapterChest;
import net.optifine.entity.model.ModelAdapterChestLarge;
import net.optifine.entity.model.ModelAdapterChicken;
import net.optifine.entity.model.ModelAdapterCow;
import net.optifine.entity.model.ModelAdapterCreeper;
import net.optifine.entity.model.ModelAdapterDragon;
import net.optifine.entity.model.ModelAdapterEnderChest;
import net.optifine.entity.model.ModelAdapterEnderCrystal;
import net.optifine.entity.model.ModelAdapterEnderman;
import net.optifine.entity.model.ModelAdapterEndermite;
import net.optifine.entity.model.ModelAdapterGhast;
import net.optifine.entity.model.ModelAdapterGuardian;
import net.optifine.entity.model.ModelAdapterHeadHumanoid;
import net.optifine.entity.model.ModelAdapterHeadSkeleton;
import net.optifine.entity.model.ModelAdapterHorse;
import net.optifine.entity.model.ModelAdapterIronGolem;
import net.optifine.entity.model.ModelAdapterLeadKnot;
import net.optifine.entity.model.ModelAdapterMagmaCube;
import net.optifine.entity.model.ModelAdapterMinecart;
import net.optifine.entity.model.ModelAdapterMinecartMobSpawner;
import net.optifine.entity.model.ModelAdapterMinecartTnt;
import net.optifine.entity.model.ModelAdapterMooshroom;
import net.optifine.entity.model.ModelAdapterOcelot;
import net.optifine.entity.model.ModelAdapterPig;
import net.optifine.entity.model.ModelAdapterPigZombie;
import net.optifine.entity.model.ModelAdapterRabbit;
import net.optifine.entity.model.ModelAdapterSheep;
import net.optifine.entity.model.ModelAdapterSheepWool;
import net.optifine.entity.model.ModelAdapterSign;
import net.optifine.entity.model.ModelAdapterSilverfish;
import net.optifine.entity.model.ModelAdapterSkeleton;
import net.optifine.entity.model.ModelAdapterSlime;
import net.optifine.entity.model.ModelAdapterSnowman;
import net.optifine.entity.model.ModelAdapterSpider;
import net.optifine.entity.model.ModelAdapterSquid;
import net.optifine.entity.model.ModelAdapterVillager;
import net.optifine.entity.model.ModelAdapterWitch;
import net.optifine.entity.model.ModelAdapterWither;
import net.optifine.entity.model.ModelAdapterWitherSkull;
import net.optifine.entity.model.ModelAdapterWolf;
import net.optifine.entity.model.ModelAdapterZombie;

public class CustomModelRegistry {
    private static Map<String, ModelAdapter> mapModelAdapters = CustomModelRegistry.makeMapModelAdapters();

    private static Map<String, ModelAdapter> makeMapModelAdapters() {
        LinkedHashMap map = new LinkedHashMap();
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterArmorStand());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterBat());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterBlaze());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterBoat());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterCaveSpider());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterChicken());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterCow());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterCreeper());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterDragon());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterEnderCrystal());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterEnderman());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterEndermite());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterGhast());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterGuardian());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterHorse());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterIronGolem());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterLeadKnot());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterMagmaCube());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterMinecart());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterMinecartTnt());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterMinecartMobSpawner());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterMooshroom());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterOcelot());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterPig());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterPigZombie());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterRabbit());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterSheep());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterSilverfish());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterSkeleton());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterSlime());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterSnowman());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterSpider());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterSquid());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterVillager());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterWitch());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterWither());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterWitherSkull());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterWolf());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterZombie());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterSheepWool());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterBanner());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterBook());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterChest());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterChestLarge());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterEnderChest());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterHeadHumanoid());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterHeadSkeleton());
        CustomModelRegistry.addModelAdapter((Map<String, ModelAdapter>)map, (ModelAdapter)new ModelAdapterSign());
        return map;
    }

    private static void addModelAdapter(Map<String, ModelAdapter> map, ModelAdapter modelAdapter) {
        CustomModelRegistry.addModelAdapter(map, modelAdapter, modelAdapter.getName());
        String[] astring = modelAdapter.getAliases();
        if (astring != null) {
            for (int i = 0; i < astring.length; ++i) {
                String s = astring[i];
                CustomModelRegistry.addModelAdapter(map, modelAdapter, s);
            }
        }
        ModelBase modelbase = modelAdapter.makeModel();
        String[] astring1 = modelAdapter.getModelRendererNames();
        for (int j = 0; j < astring1.length; ++j) {
            String s1 = astring1[j];
            ModelRenderer modelrenderer = modelAdapter.getModelRenderer(modelbase, s1);
            if (modelrenderer != null) continue;
            Config.warn((String)("Model renderer not found, model: " + modelAdapter.getName() + ", name: " + s1));
        }
    }

    private static void addModelAdapter(Map<String, ModelAdapter> map, ModelAdapter modelAdapter, String name) {
        if (map.containsKey((Object)name)) {
            Config.warn((String)("Model adapter already registered for id: " + name + ", class: " + modelAdapter.getEntityClass().getName()));
        }
        map.put((Object)name, (Object)modelAdapter);
    }

    public static ModelAdapter getModelAdapter(String name) {
        return (ModelAdapter)mapModelAdapters.get((Object)name);
    }

    public static String[] getModelNames() {
        Set set = mapModelAdapters.keySet();
        String[] astring = (String[])set.toArray((Object[])new String[set.size()]);
        return astring;
    }
}
