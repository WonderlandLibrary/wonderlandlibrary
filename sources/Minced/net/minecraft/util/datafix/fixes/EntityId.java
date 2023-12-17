// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Map;
import net.minecraft.util.datafix.IFixableData;

public class EntityId implements IFixableData
{
    private static final Map<String, String> OLD_TO_NEW_ID_MAP;
    
    @Override
    public int getFixVersion() {
        return 704;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        final String s = EntityId.OLD_TO_NEW_ID_MAP.get(compound.getString("id"));
        if (s != null) {
            compound.setString("id", s);
        }
        return compound;
    }
    
    static {
        (OLD_TO_NEW_ID_MAP = Maps.newHashMap()).put("AreaEffectCloud", "minecraft:area_effect_cloud");
        EntityId.OLD_TO_NEW_ID_MAP.put("ArmorStand", "minecraft:armor_stand");
        EntityId.OLD_TO_NEW_ID_MAP.put("Arrow", "minecraft:arrow");
        EntityId.OLD_TO_NEW_ID_MAP.put("Bat", "minecraft:bat");
        EntityId.OLD_TO_NEW_ID_MAP.put("Blaze", "minecraft:blaze");
        EntityId.OLD_TO_NEW_ID_MAP.put("Boat", "minecraft:boat");
        EntityId.OLD_TO_NEW_ID_MAP.put("CaveSpider", "minecraft:cave_spider");
        EntityId.OLD_TO_NEW_ID_MAP.put("Chicken", "minecraft:chicken");
        EntityId.OLD_TO_NEW_ID_MAP.put("Cow", "minecraft:cow");
        EntityId.OLD_TO_NEW_ID_MAP.put("Creeper", "minecraft:creeper");
        EntityId.OLD_TO_NEW_ID_MAP.put("Donkey", "minecraft:donkey");
        EntityId.OLD_TO_NEW_ID_MAP.put("DragonFireball", "minecraft:dragon_fireball");
        EntityId.OLD_TO_NEW_ID_MAP.put("ElderGuardian", "minecraft:elder_guardian");
        EntityId.OLD_TO_NEW_ID_MAP.put("EnderCrystal", "minecraft:ender_crystal");
        EntityId.OLD_TO_NEW_ID_MAP.put("EnderDragon", "minecraft:ender_dragon");
        EntityId.OLD_TO_NEW_ID_MAP.put("Enderman", "minecraft:enderman");
        EntityId.OLD_TO_NEW_ID_MAP.put("Endermite", "minecraft:endermite");
        EntityId.OLD_TO_NEW_ID_MAP.put("EyeOfEnderSignal", "minecraft:eye_of_ender_signal");
        EntityId.OLD_TO_NEW_ID_MAP.put("FallingSand", "minecraft:falling_block");
        EntityId.OLD_TO_NEW_ID_MAP.put("Fireball", "minecraft:fireball");
        EntityId.OLD_TO_NEW_ID_MAP.put("FireworksRocketEntity", "minecraft:fireworks_rocket");
        EntityId.OLD_TO_NEW_ID_MAP.put("Ghast", "minecraft:ghast");
        EntityId.OLD_TO_NEW_ID_MAP.put("Giant", "minecraft:giant");
        EntityId.OLD_TO_NEW_ID_MAP.put("Guardian", "minecraft:guardian");
        EntityId.OLD_TO_NEW_ID_MAP.put("Horse", "minecraft:horse");
        EntityId.OLD_TO_NEW_ID_MAP.put("Husk", "minecraft:husk");
        EntityId.OLD_TO_NEW_ID_MAP.put("Item", "minecraft:item");
        EntityId.OLD_TO_NEW_ID_MAP.put("ItemFrame", "minecraft:item_frame");
        EntityId.OLD_TO_NEW_ID_MAP.put("LavaSlime", "minecraft:magma_cube");
        EntityId.OLD_TO_NEW_ID_MAP.put("LeashKnot", "minecraft:leash_knot");
        EntityId.OLD_TO_NEW_ID_MAP.put("MinecartChest", "minecraft:chest_minecart");
        EntityId.OLD_TO_NEW_ID_MAP.put("MinecartCommandBlock", "minecraft:commandblock_minecart");
        EntityId.OLD_TO_NEW_ID_MAP.put("MinecartFurnace", "minecraft:furnace_minecart");
        EntityId.OLD_TO_NEW_ID_MAP.put("MinecartHopper", "minecraft:hopper_minecart");
        EntityId.OLD_TO_NEW_ID_MAP.put("MinecartRideable", "minecraft:minecart");
        EntityId.OLD_TO_NEW_ID_MAP.put("MinecartSpawner", "minecraft:spawner_minecart");
        EntityId.OLD_TO_NEW_ID_MAP.put("MinecartTNT", "minecraft:tnt_minecart");
        EntityId.OLD_TO_NEW_ID_MAP.put("Mule", "minecraft:mule");
        EntityId.OLD_TO_NEW_ID_MAP.put("MushroomCow", "minecraft:mooshroom");
        EntityId.OLD_TO_NEW_ID_MAP.put("Ozelot", "minecraft:ocelot");
        EntityId.OLD_TO_NEW_ID_MAP.put("Painting", "minecraft:painting");
        EntityId.OLD_TO_NEW_ID_MAP.put("Pig", "minecraft:pig");
        EntityId.OLD_TO_NEW_ID_MAP.put("PigZombie", "minecraft:zombie_pigman");
        EntityId.OLD_TO_NEW_ID_MAP.put("PolarBear", "minecraft:polar_bear");
        EntityId.OLD_TO_NEW_ID_MAP.put("PrimedTnt", "minecraft:tnt");
        EntityId.OLD_TO_NEW_ID_MAP.put("Rabbit", "minecraft:rabbit");
        EntityId.OLD_TO_NEW_ID_MAP.put("Sheep", "minecraft:sheep");
        EntityId.OLD_TO_NEW_ID_MAP.put("Shulker", "minecraft:shulker");
        EntityId.OLD_TO_NEW_ID_MAP.put("ShulkerBullet", "minecraft:shulker_bullet");
        EntityId.OLD_TO_NEW_ID_MAP.put("Silverfish", "minecraft:silverfish");
        EntityId.OLD_TO_NEW_ID_MAP.put("Skeleton", "minecraft:skeleton");
        EntityId.OLD_TO_NEW_ID_MAP.put("SkeletonHorse", "minecraft:skeleton_horse");
        EntityId.OLD_TO_NEW_ID_MAP.put("Slime", "minecraft:slime");
        EntityId.OLD_TO_NEW_ID_MAP.put("SmallFireball", "minecraft:small_fireball");
        EntityId.OLD_TO_NEW_ID_MAP.put("SnowMan", "minecraft:snowman");
        EntityId.OLD_TO_NEW_ID_MAP.put("Snowball", "minecraft:snowball");
        EntityId.OLD_TO_NEW_ID_MAP.put("SpectralArrow", "minecraft:spectral_arrow");
        EntityId.OLD_TO_NEW_ID_MAP.put("Spider", "minecraft:spider");
        EntityId.OLD_TO_NEW_ID_MAP.put("Squid", "minecraft:squid");
        EntityId.OLD_TO_NEW_ID_MAP.put("Stray", "minecraft:stray");
        EntityId.OLD_TO_NEW_ID_MAP.put("ThrownEgg", "minecraft:egg");
        EntityId.OLD_TO_NEW_ID_MAP.put("ThrownEnderpearl", "minecraft:ender_pearl");
        EntityId.OLD_TO_NEW_ID_MAP.put("ThrownExpBottle", "minecraft:xp_bottle");
        EntityId.OLD_TO_NEW_ID_MAP.put("ThrownPotion", "minecraft:potion");
        EntityId.OLD_TO_NEW_ID_MAP.put("Villager", "minecraft:villager");
        EntityId.OLD_TO_NEW_ID_MAP.put("VillagerGolem", "minecraft:villager_golem");
        EntityId.OLD_TO_NEW_ID_MAP.put("Witch", "minecraft:witch");
        EntityId.OLD_TO_NEW_ID_MAP.put("WitherBoss", "minecraft:wither");
        EntityId.OLD_TO_NEW_ID_MAP.put("WitherSkeleton", "minecraft:wither_skeleton");
        EntityId.OLD_TO_NEW_ID_MAP.put("WitherSkull", "minecraft:wither_skull");
        EntityId.OLD_TO_NEW_ID_MAP.put("Wolf", "minecraft:wolf");
        EntityId.OLD_TO_NEW_ID_MAP.put("XPOrb", "minecraft:xp_orb");
        EntityId.OLD_TO_NEW_ID_MAP.put("Zombie", "minecraft:zombie");
        EntityId.OLD_TO_NEW_ID_MAP.put("ZombieHorse", "minecraft:zombie_horse");
        EntityId.OLD_TO_NEW_ID_MAP.put("ZombieVillager", "minecraft:zombie_villager");
    }
}
