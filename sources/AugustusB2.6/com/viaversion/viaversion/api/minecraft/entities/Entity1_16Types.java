// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.entities;

import com.viaversion.viaversion.util.EntityTypeUtil;

public enum Entity1_16Types implements EntityType
{
    ENTITY(-1), 
    AREA_EFFECT_CLOUD(0, (EntityType)Entity1_16Types.ENTITY), 
    END_CRYSTAL(18, (EntityType)Entity1_16Types.ENTITY), 
    EVOKER_FANGS(23, (EntityType)Entity1_16Types.ENTITY), 
    EXPERIENCE_ORB(24, (EntityType)Entity1_16Types.ENTITY), 
    EYE_OF_ENDER(25, (EntityType)Entity1_16Types.ENTITY), 
    FALLING_BLOCK(26, (EntityType)Entity1_16Types.ENTITY), 
    FIREWORK_ROCKET(27, (EntityType)Entity1_16Types.ENTITY), 
    ITEM(37, (EntityType)Entity1_16Types.ENTITY), 
    LLAMA_SPIT(43, (EntityType)Entity1_16Types.ENTITY), 
    TNT(63, (EntityType)Entity1_16Types.ENTITY), 
    SHULKER_BULLET(70, (EntityType)Entity1_16Types.ENTITY), 
    FISHING_BOBBER(106, (EntityType)Entity1_16Types.ENTITY), 
    LIVINGENTITY(-1, (EntityType)Entity1_16Types.ENTITY), 
    ARMOR_STAND(1, (EntityType)Entity1_16Types.LIVINGENTITY), 
    PLAYER(105, (EntityType)Entity1_16Types.LIVINGENTITY), 
    ABSTRACT_INSENTIENT(-1, (EntityType)Entity1_16Types.LIVINGENTITY), 
    ENDER_DRAGON(19, (EntityType)Entity1_16Types.ABSTRACT_INSENTIENT), 
    BEE(4, (EntityType)Entity1_16Types.ABSTRACT_INSENTIENT), 
    ABSTRACT_CREATURE(-1, (EntityType)Entity1_16Types.ABSTRACT_INSENTIENT), 
    ABSTRACT_AGEABLE(-1, (EntityType)Entity1_16Types.ABSTRACT_CREATURE), 
    VILLAGER(92, (EntityType)Entity1_16Types.ABSTRACT_AGEABLE), 
    WANDERING_TRADER(94, (EntityType)Entity1_16Types.ABSTRACT_AGEABLE), 
    ABSTRACT_ANIMAL(-1, (EntityType)Entity1_16Types.ABSTRACT_AGEABLE), 
    DOLPHIN(13, (EntityType)Entity1_16Types.ABSTRACT_INSENTIENT), 
    CHICKEN(9, (EntityType)Entity1_16Types.ABSTRACT_ANIMAL), 
    COW(11, (EntityType)Entity1_16Types.ABSTRACT_ANIMAL), 
    MOOSHROOM(53, (EntityType)Entity1_16Types.COW), 
    PANDA(56, (EntityType)Entity1_16Types.ABSTRACT_INSENTIENT), 
    PIG(59, (EntityType)Entity1_16Types.ABSTRACT_ANIMAL), 
    POLAR_BEAR(62, (EntityType)Entity1_16Types.ABSTRACT_ANIMAL), 
    RABBIT(65, (EntityType)Entity1_16Types.ABSTRACT_ANIMAL), 
    SHEEP(68, (EntityType)Entity1_16Types.ABSTRACT_ANIMAL), 
    TURTLE(90, (EntityType)Entity1_16Types.ABSTRACT_ANIMAL), 
    FOX(28, (EntityType)Entity1_16Types.ABSTRACT_ANIMAL), 
    ABSTRACT_TAMEABLE_ANIMAL(-1, (EntityType)Entity1_16Types.ABSTRACT_ANIMAL), 
    CAT(7, (EntityType)Entity1_16Types.ABSTRACT_TAMEABLE_ANIMAL), 
    OCELOT(54, (EntityType)Entity1_16Types.ABSTRACT_TAMEABLE_ANIMAL), 
    WOLF(99, (EntityType)Entity1_16Types.ABSTRACT_TAMEABLE_ANIMAL), 
    ABSTRACT_PARROT(-1, (EntityType)Entity1_16Types.ABSTRACT_TAMEABLE_ANIMAL), 
    PARROT(57, (EntityType)Entity1_16Types.ABSTRACT_PARROT), 
    ABSTRACT_HORSE(-1, (EntityType)Entity1_16Types.ABSTRACT_ANIMAL), 
    CHESTED_HORSE(-1, (EntityType)Entity1_16Types.ABSTRACT_HORSE), 
    DONKEY(14, (EntityType)Entity1_16Types.CHESTED_HORSE), 
    MULE(52, (EntityType)Entity1_16Types.CHESTED_HORSE), 
    LLAMA(42, (EntityType)Entity1_16Types.CHESTED_HORSE), 
    TRADER_LLAMA(88, (EntityType)Entity1_16Types.CHESTED_HORSE), 
    HORSE(33, (EntityType)Entity1_16Types.ABSTRACT_HORSE), 
    SKELETON_HORSE(73, (EntityType)Entity1_16Types.ABSTRACT_HORSE), 
    ZOMBIE_HORSE(102, (EntityType)Entity1_16Types.ABSTRACT_HORSE), 
    ABSTRACT_GOLEM(-1, (EntityType)Entity1_16Types.ABSTRACT_CREATURE), 
    SNOW_GOLEM(76, (EntityType)Entity1_16Types.ABSTRACT_GOLEM), 
    IRON_GOLEM(36, (EntityType)Entity1_16Types.ABSTRACT_GOLEM), 
    SHULKER(69, (EntityType)Entity1_16Types.ABSTRACT_GOLEM), 
    ABSTRACT_FISHES(-1, (EntityType)Entity1_16Types.ABSTRACT_CREATURE), 
    COD(10, (EntityType)Entity1_16Types.ABSTRACT_FISHES), 
    PUFFERFISH(64, (EntityType)Entity1_16Types.ABSTRACT_FISHES), 
    SALMON(67, (EntityType)Entity1_16Types.ABSTRACT_FISHES), 
    TROPICAL_FISH(89, (EntityType)Entity1_16Types.ABSTRACT_FISHES), 
    ABSTRACT_MONSTER(-1, (EntityType)Entity1_16Types.ABSTRACT_CREATURE), 
    BLAZE(5, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    CREEPER(12, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    ENDERMITE(21, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    ENDERMAN(20, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    GIANT(30, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    SILVERFISH(71, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    VEX(91, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    WITCH(95, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    WITHER(96, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    RAVAGER(66, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    PIGLIN(60, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    HOGLIN(32, (EntityType)Entity1_16Types.ABSTRACT_ANIMAL), 
    STRIDER(82, (EntityType)Entity1_16Types.ABSTRACT_ANIMAL), 
    ZOGLIN(100, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    ABSTRACT_ILLAGER_BASE(-1, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    ABSTRACT_EVO_ILLU_ILLAGER(-1, (EntityType)Entity1_16Types.ABSTRACT_ILLAGER_BASE), 
    EVOKER(22, (EntityType)Entity1_16Types.ABSTRACT_EVO_ILLU_ILLAGER), 
    ILLUSIONER(35, (EntityType)Entity1_16Types.ABSTRACT_EVO_ILLU_ILLAGER), 
    VINDICATOR(93, (EntityType)Entity1_16Types.ABSTRACT_ILLAGER_BASE), 
    PILLAGER(61, (EntityType)Entity1_16Types.ABSTRACT_ILLAGER_BASE), 
    ABSTRACT_SKELETON(-1, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    SKELETON(72, (EntityType)Entity1_16Types.ABSTRACT_SKELETON), 
    STRAY(81, (EntityType)Entity1_16Types.ABSTRACT_SKELETON), 
    WITHER_SKELETON(97, (EntityType)Entity1_16Types.ABSTRACT_SKELETON), 
    GUARDIAN(31, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    ELDER_GUARDIAN(17, (EntityType)Entity1_16Types.GUARDIAN), 
    SPIDER(79, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    CAVE_SPIDER(8, (EntityType)Entity1_16Types.SPIDER), 
    ZOMBIE(101, (EntityType)Entity1_16Types.ABSTRACT_MONSTER), 
    DROWNED(16, (EntityType)Entity1_16Types.ZOMBIE), 
    HUSK(34, (EntityType)Entity1_16Types.ZOMBIE), 
    ZOMBIFIED_PIGLIN(104, (EntityType)Entity1_16Types.ZOMBIE), 
    ZOMBIE_VILLAGER(103, (EntityType)Entity1_16Types.ZOMBIE), 
    ABSTRACT_FLYING(-1, (EntityType)Entity1_16Types.ABSTRACT_INSENTIENT), 
    GHAST(29, (EntityType)Entity1_16Types.ABSTRACT_FLYING), 
    PHANTOM(58, (EntityType)Entity1_16Types.ABSTRACT_FLYING), 
    ABSTRACT_AMBIENT(-1, (EntityType)Entity1_16Types.ABSTRACT_INSENTIENT), 
    BAT(3, (EntityType)Entity1_16Types.ABSTRACT_AMBIENT), 
    ABSTRACT_WATERMOB(-1, (EntityType)Entity1_16Types.ABSTRACT_INSENTIENT), 
    SQUID(80, (EntityType)Entity1_16Types.ABSTRACT_WATERMOB), 
    SLIME(74, (EntityType)Entity1_16Types.ABSTRACT_INSENTIENT), 
    MAGMA_CUBE(44, (EntityType)Entity1_16Types.SLIME), 
    ABSTRACT_HANGING(-1, (EntityType)Entity1_16Types.ENTITY), 
    LEASH_KNOT(40, (EntityType)Entity1_16Types.ABSTRACT_HANGING), 
    ITEM_FRAME(38, (EntityType)Entity1_16Types.ABSTRACT_HANGING), 
    PAINTING(55, (EntityType)Entity1_16Types.ABSTRACT_HANGING), 
    ABSTRACT_LIGHTNING(-1, (EntityType)Entity1_16Types.ENTITY), 
    LIGHTNING_BOLT(41, (EntityType)Entity1_16Types.ABSTRACT_LIGHTNING), 
    ABSTRACT_ARROW(-1, (EntityType)Entity1_16Types.ENTITY), 
    ARROW(2, (EntityType)Entity1_16Types.ABSTRACT_ARROW), 
    SPECTRAL_ARROW(78, (EntityType)Entity1_16Types.ABSTRACT_ARROW), 
    TRIDENT(87, (EntityType)Entity1_16Types.ABSTRACT_ARROW), 
    ABSTRACT_FIREBALL(-1, (EntityType)Entity1_16Types.ENTITY), 
    DRAGON_FIREBALL(15, (EntityType)Entity1_16Types.ABSTRACT_FIREBALL), 
    FIREBALL(39, (EntityType)Entity1_16Types.ABSTRACT_FIREBALL), 
    SMALL_FIREBALL(75, (EntityType)Entity1_16Types.ABSTRACT_FIREBALL), 
    WITHER_SKULL(98, (EntityType)Entity1_16Types.ABSTRACT_FIREBALL), 
    PROJECTILE_ABSTRACT(-1, (EntityType)Entity1_16Types.ENTITY), 
    SNOWBALL(77, (EntityType)Entity1_16Types.PROJECTILE_ABSTRACT), 
    ENDER_PEARL(84, (EntityType)Entity1_16Types.PROJECTILE_ABSTRACT), 
    EGG(83, (EntityType)Entity1_16Types.PROJECTILE_ABSTRACT), 
    POTION(86, (EntityType)Entity1_16Types.PROJECTILE_ABSTRACT), 
    EXPERIENCE_BOTTLE(85, (EntityType)Entity1_16Types.PROJECTILE_ABSTRACT), 
    MINECART_ABSTRACT(-1, (EntityType)Entity1_16Types.ENTITY), 
    CHESTED_MINECART_ABSTRACT(-1, (EntityType)Entity1_16Types.MINECART_ABSTRACT), 
    CHEST_MINECART(46, (EntityType)Entity1_16Types.CHESTED_MINECART_ABSTRACT), 
    HOPPER_MINECART(49, (EntityType)Entity1_16Types.CHESTED_MINECART_ABSTRACT), 
    MINECART(45, (EntityType)Entity1_16Types.MINECART_ABSTRACT), 
    FURNACE_MINECART(48, (EntityType)Entity1_16Types.MINECART_ABSTRACT), 
    COMMAND_BLOCK_MINECART(47, (EntityType)Entity1_16Types.MINECART_ABSTRACT), 
    TNT_MINECART(51, (EntityType)Entity1_16Types.MINECART_ABSTRACT), 
    SPAWNER_MINECART(50, (EntityType)Entity1_16Types.MINECART_ABSTRACT), 
    BOAT(6, (EntityType)Entity1_16Types.ENTITY);
    
    private static final EntityType[] TYPES;
    private final int id;
    private final EntityType parent;
    
    private Entity1_16Types(final int id) {
        this.id = id;
        this.parent = null;
    }
    
    private Entity1_16Types(final int id, final EntityType parent) {
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
        return EntityTypeUtil.getTypeFromId(Entity1_16Types.TYPES, typeId, Entity1_16Types.ENTITY);
    }
    
    static {
        TYPES = EntityTypeUtil.toOrderedArray(values());
    }
}
