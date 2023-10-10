// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.entities;

import com.viaversion.viaversion.util.EntityTypeUtil;

public enum Entity1_17Types implements EntityType
{
    ENTITY(-1), 
    AREA_EFFECT_CLOUD(0, (EntityType)Entity1_17Types.ENTITY), 
    END_CRYSTAL(19, (EntityType)Entity1_17Types.ENTITY), 
    EVOKER_FANGS(24, (EntityType)Entity1_17Types.ENTITY), 
    EXPERIENCE_ORB(25, (EntityType)Entity1_17Types.ENTITY), 
    EYE_OF_ENDER(26, (EntityType)Entity1_17Types.ENTITY), 
    FALLING_BLOCK(27, (EntityType)Entity1_17Types.ENTITY), 
    FIREWORK_ROCKET(28, (EntityType)Entity1_17Types.ENTITY), 
    ITEM(41, (EntityType)Entity1_17Types.ENTITY), 
    LLAMA_SPIT(47, (EntityType)Entity1_17Types.ENTITY), 
    TNT(69, (EntityType)Entity1_17Types.ENTITY), 
    SHULKER_BULLET(76, (EntityType)Entity1_17Types.ENTITY), 
    FISHING_BOBBER(112, (EntityType)Entity1_17Types.ENTITY), 
    LIVINGENTITY(-1, (EntityType)Entity1_17Types.ENTITY), 
    ARMOR_STAND(1, (EntityType)Entity1_17Types.LIVINGENTITY), 
    MARKER(49, (EntityType)Entity1_17Types.ENTITY), 
    PLAYER(111, (EntityType)Entity1_17Types.LIVINGENTITY), 
    ABSTRACT_INSENTIENT(-1, (EntityType)Entity1_17Types.LIVINGENTITY), 
    ENDER_DRAGON(20, (EntityType)Entity1_17Types.ABSTRACT_INSENTIENT), 
    BEE(5, (EntityType)Entity1_17Types.ABSTRACT_INSENTIENT), 
    ABSTRACT_CREATURE(-1, (EntityType)Entity1_17Types.ABSTRACT_INSENTIENT), 
    ABSTRACT_AGEABLE(-1, (EntityType)Entity1_17Types.ABSTRACT_CREATURE), 
    VILLAGER(98, (EntityType)Entity1_17Types.ABSTRACT_AGEABLE), 
    WANDERING_TRADER(100, (EntityType)Entity1_17Types.ABSTRACT_AGEABLE), 
    ABSTRACT_ANIMAL(-1, (EntityType)Entity1_17Types.ABSTRACT_AGEABLE), 
    AXOLOTL(3, (EntityType)Entity1_17Types.ABSTRACT_ANIMAL), 
    DOLPHIN(14, (EntityType)Entity1_17Types.ABSTRACT_INSENTIENT), 
    CHICKEN(10, (EntityType)Entity1_17Types.ABSTRACT_ANIMAL), 
    COW(12, (EntityType)Entity1_17Types.ABSTRACT_ANIMAL), 
    MOOSHROOM(58, (EntityType)Entity1_17Types.COW), 
    PANDA(61, (EntityType)Entity1_17Types.ABSTRACT_INSENTIENT), 
    PIG(64, (EntityType)Entity1_17Types.ABSTRACT_ANIMAL), 
    POLAR_BEAR(68, (EntityType)Entity1_17Types.ABSTRACT_ANIMAL), 
    RABBIT(71, (EntityType)Entity1_17Types.ABSTRACT_ANIMAL), 
    SHEEP(74, (EntityType)Entity1_17Types.ABSTRACT_ANIMAL), 
    TURTLE(96, (EntityType)Entity1_17Types.ABSTRACT_ANIMAL), 
    FOX(29, (EntityType)Entity1_17Types.ABSTRACT_ANIMAL), 
    GOAT(34, (EntityType)Entity1_17Types.ABSTRACT_ANIMAL), 
    ABSTRACT_TAMEABLE_ANIMAL(-1, (EntityType)Entity1_17Types.ABSTRACT_ANIMAL), 
    CAT(8, (EntityType)Entity1_17Types.ABSTRACT_TAMEABLE_ANIMAL), 
    OCELOT(59, (EntityType)Entity1_17Types.ABSTRACT_TAMEABLE_ANIMAL), 
    WOLF(105, (EntityType)Entity1_17Types.ABSTRACT_TAMEABLE_ANIMAL), 
    ABSTRACT_PARROT(-1, (EntityType)Entity1_17Types.ABSTRACT_TAMEABLE_ANIMAL), 
    PARROT(62, (EntityType)Entity1_17Types.ABSTRACT_PARROT), 
    ABSTRACT_HORSE(-1, (EntityType)Entity1_17Types.ABSTRACT_ANIMAL), 
    CHESTED_HORSE(-1, (EntityType)Entity1_17Types.ABSTRACT_HORSE), 
    DONKEY(15, (EntityType)Entity1_17Types.CHESTED_HORSE), 
    MULE(57, (EntityType)Entity1_17Types.CHESTED_HORSE), 
    LLAMA(46, (EntityType)Entity1_17Types.CHESTED_HORSE), 
    TRADER_LLAMA(94, (EntityType)Entity1_17Types.CHESTED_HORSE), 
    HORSE(37, (EntityType)Entity1_17Types.ABSTRACT_HORSE), 
    SKELETON_HORSE(79, (EntityType)Entity1_17Types.ABSTRACT_HORSE), 
    ZOMBIE_HORSE(108, (EntityType)Entity1_17Types.ABSTRACT_HORSE), 
    ABSTRACT_GOLEM(-1, (EntityType)Entity1_17Types.ABSTRACT_CREATURE), 
    SNOW_GOLEM(82, (EntityType)Entity1_17Types.ABSTRACT_GOLEM), 
    IRON_GOLEM(40, (EntityType)Entity1_17Types.ABSTRACT_GOLEM), 
    SHULKER(75, (EntityType)Entity1_17Types.ABSTRACT_GOLEM), 
    ABSTRACT_FISHES(-1, (EntityType)Entity1_17Types.ABSTRACT_CREATURE), 
    COD(11, (EntityType)Entity1_17Types.ABSTRACT_FISHES), 
    PUFFERFISH(70, (EntityType)Entity1_17Types.ABSTRACT_FISHES), 
    SALMON(73, (EntityType)Entity1_17Types.ABSTRACT_FISHES), 
    TROPICAL_FISH(95, (EntityType)Entity1_17Types.ABSTRACT_FISHES), 
    ABSTRACT_MONSTER(-1, (EntityType)Entity1_17Types.ABSTRACT_CREATURE), 
    BLAZE(6, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    CREEPER(13, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    ENDERMITE(22, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    ENDERMAN(21, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    GIANT(31, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    SILVERFISH(77, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    VEX(97, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    WITCH(101, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    WITHER(102, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    RAVAGER(72, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    ABSTRACT_PIGLIN(-1, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    PIGLIN(65, (EntityType)Entity1_17Types.ABSTRACT_PIGLIN), 
    PIGLIN_BRUTE(66, (EntityType)Entity1_17Types.ABSTRACT_PIGLIN), 
    HOGLIN(36, (EntityType)Entity1_17Types.ABSTRACT_ANIMAL), 
    STRIDER(88, (EntityType)Entity1_17Types.ABSTRACT_ANIMAL), 
    ZOGLIN(106, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    ABSTRACT_ILLAGER_BASE(-1, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    ABSTRACT_EVO_ILLU_ILLAGER(-1, (EntityType)Entity1_17Types.ABSTRACT_ILLAGER_BASE), 
    EVOKER(23, (EntityType)Entity1_17Types.ABSTRACT_EVO_ILLU_ILLAGER), 
    ILLUSIONER(39, (EntityType)Entity1_17Types.ABSTRACT_EVO_ILLU_ILLAGER), 
    VINDICATOR(99, (EntityType)Entity1_17Types.ABSTRACT_ILLAGER_BASE), 
    PILLAGER(67, (EntityType)Entity1_17Types.ABSTRACT_ILLAGER_BASE), 
    ABSTRACT_SKELETON(-1, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    SKELETON(78, (EntityType)Entity1_17Types.ABSTRACT_SKELETON), 
    STRAY(87, (EntityType)Entity1_17Types.ABSTRACT_SKELETON), 
    WITHER_SKELETON(103, (EntityType)Entity1_17Types.ABSTRACT_SKELETON), 
    GUARDIAN(35, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    ELDER_GUARDIAN(18, (EntityType)Entity1_17Types.GUARDIAN), 
    SPIDER(85, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    CAVE_SPIDER(9, (EntityType)Entity1_17Types.SPIDER), 
    ZOMBIE(107, (EntityType)Entity1_17Types.ABSTRACT_MONSTER), 
    DROWNED(17, (EntityType)Entity1_17Types.ZOMBIE), 
    HUSK(38, (EntityType)Entity1_17Types.ZOMBIE), 
    ZOMBIFIED_PIGLIN(110, (EntityType)Entity1_17Types.ZOMBIE), 
    ZOMBIE_VILLAGER(109, (EntityType)Entity1_17Types.ZOMBIE), 
    ABSTRACT_FLYING(-1, (EntityType)Entity1_17Types.ABSTRACT_INSENTIENT), 
    GHAST(30, (EntityType)Entity1_17Types.ABSTRACT_FLYING), 
    PHANTOM(63, (EntityType)Entity1_17Types.ABSTRACT_FLYING), 
    ABSTRACT_AMBIENT(-1, (EntityType)Entity1_17Types.ABSTRACT_INSENTIENT), 
    BAT(4, (EntityType)Entity1_17Types.ABSTRACT_AMBIENT), 
    ABSTRACT_WATERMOB(-1, (EntityType)Entity1_17Types.ABSTRACT_INSENTIENT), 
    SQUID(86, (EntityType)Entity1_17Types.ABSTRACT_WATERMOB), 
    GLOW_SQUID(33, (EntityType)Entity1_17Types.SQUID), 
    SLIME(80, (EntityType)Entity1_17Types.ABSTRACT_INSENTIENT), 
    MAGMA_CUBE(48, (EntityType)Entity1_17Types.SLIME), 
    ABSTRACT_HANGING(-1, (EntityType)Entity1_17Types.ENTITY), 
    LEASH_KNOT(44, (EntityType)Entity1_17Types.ABSTRACT_HANGING), 
    ITEM_FRAME(42, (EntityType)Entity1_17Types.ABSTRACT_HANGING), 
    GLOW_ITEM_FRAME(32, (EntityType)Entity1_17Types.ITEM_FRAME), 
    PAINTING(60, (EntityType)Entity1_17Types.ABSTRACT_HANGING), 
    ABSTRACT_LIGHTNING(-1, (EntityType)Entity1_17Types.ENTITY), 
    LIGHTNING_BOLT(45, (EntityType)Entity1_17Types.ABSTRACT_LIGHTNING), 
    ABSTRACT_ARROW(-1, (EntityType)Entity1_17Types.ENTITY), 
    ARROW(2, (EntityType)Entity1_17Types.ABSTRACT_ARROW), 
    SPECTRAL_ARROW(84, (EntityType)Entity1_17Types.ABSTRACT_ARROW), 
    TRIDENT(93, (EntityType)Entity1_17Types.ABSTRACT_ARROW), 
    ABSTRACT_FIREBALL(-1, (EntityType)Entity1_17Types.ENTITY), 
    DRAGON_FIREBALL(16, (EntityType)Entity1_17Types.ABSTRACT_FIREBALL), 
    FIREBALL(43, (EntityType)Entity1_17Types.ABSTRACT_FIREBALL), 
    SMALL_FIREBALL(81, (EntityType)Entity1_17Types.ABSTRACT_FIREBALL), 
    WITHER_SKULL(104, (EntityType)Entity1_17Types.ABSTRACT_FIREBALL), 
    PROJECTILE_ABSTRACT(-1, (EntityType)Entity1_17Types.ENTITY), 
    SNOWBALL(83, (EntityType)Entity1_17Types.PROJECTILE_ABSTRACT), 
    ENDER_PEARL(90, (EntityType)Entity1_17Types.PROJECTILE_ABSTRACT), 
    EGG(89, (EntityType)Entity1_17Types.PROJECTILE_ABSTRACT), 
    POTION(92, (EntityType)Entity1_17Types.PROJECTILE_ABSTRACT), 
    EXPERIENCE_BOTTLE(91, (EntityType)Entity1_17Types.PROJECTILE_ABSTRACT), 
    MINECART_ABSTRACT(-1, (EntityType)Entity1_17Types.ENTITY), 
    CHESTED_MINECART_ABSTRACT(-1, (EntityType)Entity1_17Types.MINECART_ABSTRACT), 
    CHEST_MINECART(51, (EntityType)Entity1_17Types.CHESTED_MINECART_ABSTRACT), 
    HOPPER_MINECART(54, (EntityType)Entity1_17Types.CHESTED_MINECART_ABSTRACT), 
    MINECART(50, (EntityType)Entity1_17Types.MINECART_ABSTRACT), 
    FURNACE_MINECART(53, (EntityType)Entity1_17Types.MINECART_ABSTRACT), 
    COMMAND_BLOCK_MINECART(52, (EntityType)Entity1_17Types.MINECART_ABSTRACT), 
    TNT_MINECART(56, (EntityType)Entity1_17Types.MINECART_ABSTRACT), 
    SPAWNER_MINECART(55, (EntityType)Entity1_17Types.MINECART_ABSTRACT), 
    BOAT(7, (EntityType)Entity1_17Types.ENTITY);
    
    private static final EntityType[] TYPES;
    private final int id;
    private final EntityType parent;
    
    private Entity1_17Types(final int id) {
        this.id = id;
        this.parent = null;
    }
    
    private Entity1_17Types(final int id, final EntityType parent) {
        this.id = id;
        this.parent = parent;
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    @Override
    public EntityType getParent() {
        return this.parent;
    }
    
    public static EntityType getTypeFromId(final int typeId) {
        return EntityTypeUtil.getTypeFromId(Entity1_17Types.TYPES, typeId, Entity1_17Types.ENTITY);
    }
    
    static {
        TYPES = EntityTypeUtil.toOrderedArray(values());
    }
}
