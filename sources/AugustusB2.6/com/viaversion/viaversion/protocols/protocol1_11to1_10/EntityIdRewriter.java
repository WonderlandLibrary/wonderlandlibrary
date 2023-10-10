// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_11to1_10;

import com.google.common.collect.HashBiMap;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.google.common.collect.BiMap;

public class EntityIdRewriter
{
    private static final BiMap<String, String> oldToNewNames;
    
    public static void toClient(final CompoundTag tag) {
        toClient(tag, false);
    }
    
    public static void toClient(final CompoundTag tag, final boolean backwards) {
        final Tag idTag = tag.get("id");
        if (idTag instanceof StringTag) {
            final StringTag id = (StringTag)idTag;
            final String newName = backwards ? EntityIdRewriter.oldToNewNames.inverse().get(id.getValue()) : EntityIdRewriter.oldToNewNames.get(id.getValue());
            if (newName != null) {
                id.setValue(newName);
            }
        }
    }
    
    public static void toClientSpawner(final CompoundTag tag) {
        toClientSpawner(tag, false);
    }
    
    public static void toClientSpawner(final CompoundTag tag, final boolean backwards) {
        if (tag == null) {
            return;
        }
        final Tag spawnDataTag = tag.get("SpawnData");
        if (spawnDataTag != null) {
            toClient((CompoundTag)spawnDataTag, backwards);
        }
    }
    
    public static void toClientItem(final Item item) {
        toClientItem(item, false);
    }
    
    public static void toClientItem(final Item item, final boolean backwards) {
        if (hasEntityTag(item)) {
            toClient(item.tag().get("EntityTag"), backwards);
        }
        if (item != null && item.amount() <= 0) {
            item.setAmount(1);
        }
    }
    
    public static void toServerItem(final Item item) {
        toServerItem(item, false);
    }
    
    public static void toServerItem(final Item item, final boolean backwards) {
        if (!hasEntityTag(item)) {
            return;
        }
        final CompoundTag entityTag = item.tag().get("EntityTag");
        final Tag idTag = entityTag.get("id");
        if (idTag instanceof StringTag) {
            final StringTag id = (StringTag)idTag;
            final String newName = backwards ? EntityIdRewriter.oldToNewNames.get(id.getValue()) : EntityIdRewriter.oldToNewNames.inverse().get(id.getValue());
            if (newName != null) {
                id.setValue(newName);
            }
        }
    }
    
    private static boolean hasEntityTag(final Item item) {
        if (item == null || item.identifier() != 383) {
            return false;
        }
        final CompoundTag tag = item.tag();
        if (tag == null) {
            return false;
        }
        final Tag entityTag = tag.get("EntityTag");
        return entityTag instanceof CompoundTag && ((CompoundTag)entityTag).get("id") instanceof StringTag;
    }
    
    static {
        (oldToNewNames = HashBiMap.create()).put("AreaEffectCloud", "minecraft:area_effect_cloud");
        EntityIdRewriter.oldToNewNames.put("ArmorStand", "minecraft:armor_stand");
        EntityIdRewriter.oldToNewNames.put("Arrow", "minecraft:arrow");
        EntityIdRewriter.oldToNewNames.put("Bat", "minecraft:bat");
        EntityIdRewriter.oldToNewNames.put("Blaze", "minecraft:blaze");
        EntityIdRewriter.oldToNewNames.put("Boat", "minecraft:boat");
        EntityIdRewriter.oldToNewNames.put("CaveSpider", "minecraft:cave_spider");
        EntityIdRewriter.oldToNewNames.put("Chicken", "minecraft:chicken");
        EntityIdRewriter.oldToNewNames.put("Cow", "minecraft:cow");
        EntityIdRewriter.oldToNewNames.put("Creeper", "minecraft:creeper");
        EntityIdRewriter.oldToNewNames.put("Donkey", "minecraft:donkey");
        EntityIdRewriter.oldToNewNames.put("DragonFireball", "minecraft:dragon_fireball");
        EntityIdRewriter.oldToNewNames.put("ElderGuardian", "minecraft:elder_guardian");
        EntityIdRewriter.oldToNewNames.put("EnderCrystal", "minecraft:ender_crystal");
        EntityIdRewriter.oldToNewNames.put("EnderDragon", "minecraft:ender_dragon");
        EntityIdRewriter.oldToNewNames.put("Enderman", "minecraft:enderman");
        EntityIdRewriter.oldToNewNames.put("Endermite", "minecraft:endermite");
        EntityIdRewriter.oldToNewNames.put("EntityHorse", "minecraft:horse");
        EntityIdRewriter.oldToNewNames.put("EyeOfEnderSignal", "minecraft:eye_of_ender_signal");
        EntityIdRewriter.oldToNewNames.put("FallingSand", "minecraft:falling_block");
        EntityIdRewriter.oldToNewNames.put("Fireball", "minecraft:fireball");
        EntityIdRewriter.oldToNewNames.put("FireworksRocketEntity", "minecraft:fireworks_rocket");
        EntityIdRewriter.oldToNewNames.put("Ghast", "minecraft:ghast");
        EntityIdRewriter.oldToNewNames.put("Giant", "minecraft:giant");
        EntityIdRewriter.oldToNewNames.put("Guardian", "minecraft:guardian");
        EntityIdRewriter.oldToNewNames.put("Husk", "minecraft:husk");
        EntityIdRewriter.oldToNewNames.put("Item", "minecraft:item");
        EntityIdRewriter.oldToNewNames.put("ItemFrame", "minecraft:item_frame");
        EntityIdRewriter.oldToNewNames.put("LavaSlime", "minecraft:magma_cube");
        EntityIdRewriter.oldToNewNames.put("LeashKnot", "minecraft:leash_knot");
        EntityIdRewriter.oldToNewNames.put("MinecartChest", "minecraft:chest_minecart");
        EntityIdRewriter.oldToNewNames.put("MinecartCommandBlock", "minecraft:commandblock_minecart");
        EntityIdRewriter.oldToNewNames.put("MinecartFurnace", "minecraft:furnace_minecart");
        EntityIdRewriter.oldToNewNames.put("MinecartHopper", "minecraft:hopper_minecart");
        EntityIdRewriter.oldToNewNames.put("MinecartRideable", "minecraft:minecart");
        EntityIdRewriter.oldToNewNames.put("MinecartSpawner", "minecraft:spawner_minecart");
        EntityIdRewriter.oldToNewNames.put("MinecartTNT", "minecraft:tnt_minecart");
        EntityIdRewriter.oldToNewNames.put("Mule", "minecraft:mule");
        EntityIdRewriter.oldToNewNames.put("MushroomCow", "minecraft:mooshroom");
        EntityIdRewriter.oldToNewNames.put("Ozelot", "minecraft:ocelot");
        EntityIdRewriter.oldToNewNames.put("Painting", "minecraft:painting");
        EntityIdRewriter.oldToNewNames.put("Pig", "minecraft:pig");
        EntityIdRewriter.oldToNewNames.put("PigZombie", "minecraft:zombie_pigman");
        EntityIdRewriter.oldToNewNames.put("PolarBear", "minecraft:polar_bear");
        EntityIdRewriter.oldToNewNames.put("PrimedTnt", "minecraft:tnt");
        EntityIdRewriter.oldToNewNames.put("Rabbit", "minecraft:rabbit");
        EntityIdRewriter.oldToNewNames.put("Sheep", "minecraft:sheep");
        EntityIdRewriter.oldToNewNames.put("Shulker", "minecraft:shulker");
        EntityIdRewriter.oldToNewNames.put("ShulkerBullet", "minecraft:shulker_bullet");
        EntityIdRewriter.oldToNewNames.put("Silverfish", "minecraft:silverfish");
        EntityIdRewriter.oldToNewNames.put("Skeleton", "minecraft:skeleton");
        EntityIdRewriter.oldToNewNames.put("SkeletonHorse", "minecraft:skeleton_horse");
        EntityIdRewriter.oldToNewNames.put("Slime", "minecraft:slime");
        EntityIdRewriter.oldToNewNames.put("SmallFireball", "minecraft:small_fireball");
        EntityIdRewriter.oldToNewNames.put("Snowball", "minecraft:snowball");
        EntityIdRewriter.oldToNewNames.put("SnowMan", "minecraft:snowman");
        EntityIdRewriter.oldToNewNames.put("SpectralArrow", "minecraft:spectral_arrow");
        EntityIdRewriter.oldToNewNames.put("Spider", "minecraft:spider");
        EntityIdRewriter.oldToNewNames.put("Squid", "minecraft:squid");
        EntityIdRewriter.oldToNewNames.put("Stray", "minecraft:stray");
        EntityIdRewriter.oldToNewNames.put("ThrownEgg", "minecraft:egg");
        EntityIdRewriter.oldToNewNames.put("ThrownEnderpearl", "minecraft:ender_pearl");
        EntityIdRewriter.oldToNewNames.put("ThrownExpBottle", "minecraft:xp_bottle");
        EntityIdRewriter.oldToNewNames.put("ThrownPotion", "minecraft:potion");
        EntityIdRewriter.oldToNewNames.put("Villager", "minecraft:villager");
        EntityIdRewriter.oldToNewNames.put("VillagerGolem", "minecraft:villager_golem");
        EntityIdRewriter.oldToNewNames.put("Witch", "minecraft:witch");
        EntityIdRewriter.oldToNewNames.put("WitherBoss", "minecraft:wither");
        EntityIdRewriter.oldToNewNames.put("WitherSkeleton", "minecraft:wither_skeleton");
        EntityIdRewriter.oldToNewNames.put("WitherSkull", "minecraft:wither_skull");
        EntityIdRewriter.oldToNewNames.put("Wolf", "minecraft:wolf");
        EntityIdRewriter.oldToNewNames.put("XPOrb", "minecraft:xp_orb");
        EntityIdRewriter.oldToNewNames.put("Zombie", "minecraft:zombie");
        EntityIdRewriter.oldToNewNames.put("ZombieHorse", "minecraft:zombie_horse");
        EntityIdRewriter.oldToNewNames.put("ZombieVillager", "minecraft:zombie_villager");
    }
}
