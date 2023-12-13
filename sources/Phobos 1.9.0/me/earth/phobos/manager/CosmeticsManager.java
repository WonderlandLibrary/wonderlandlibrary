package me.earth.phobos.manager;

import me.earth.phobos.features.modules.client.Cosmetics;
import me.earth.phobos.util.Util;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CosmeticsManager
        implements Util {
    public Map<String, List<ModelBase>> cosmeticsUserMap = new HashMap<String, List<ModelBase>>();

    public CosmeticsManager() {
        this.cosmeticsUserMap.put("a5e36d37-5fbe-4481-b5be-1f06baee1f1c", Arrays.asList(Cosmetics.INSTANCE.santaHatModel, Cosmetics.INSTANCE.glassesModel));
        this.cosmeticsUserMap.put("19bf3f1f-fe06-4c86-bea5-3dad5df89714", Arrays.asList(new ModelBase[]{Cosmetics.INSTANCE.cloutGoggles}));
        this.cosmeticsUserMap.put("b0836db9-2472-4ba6-a1b7-92c605f5e80d", Arrays.asList(new ModelBase[]{Cosmetics.INSTANCE.cloutGoggles}));
        this.cosmeticsUserMap.put("811c9272-9793-4fdd-980d-778e8ad2e54c", Arrays.asList(new ModelBase[]{Cosmetics.INSTANCE.squidLauncher}));
        this.cosmeticsUserMap.put("09410a87-dfc8-476c-9acb-04bd07126c6e", Arrays.asList(new ModelBase[]{Cosmetics.INSTANCE.squidLauncher}));
        this.cosmeticsUserMap.put("2eb88d28-7a26-43ad-81aa-113bd818d977", Arrays.asList(new ModelBase[]{Cosmetics.INSTANCE.squidLauncher}));
        this.cosmeticsUserMap.put("e75a0d3c-3442-4945-aae3-dc74dc54d8b9", Arrays.asList(Cosmetics.INSTANCE.glassesModel, Cosmetics.INSTANCE.squidLauncher));
        this.cosmeticsUserMap.put("58526350-29f5-4065-96b6-e4a05be9ec5b", Arrays.asList(new ModelBase[]{Cosmetics.INSTANCE.santaHatModel}));
    }

    public List<ModelBase> getRenderModels(EntityPlayer player) {
        return this.cosmeticsUserMap.get(player.getUniqueID().toString());
    }

    public boolean hasCosmetics(EntityPlayer player) {
        return this.cosmeticsUserMap.containsKey(player.getUniqueID().toString());
    }
}

