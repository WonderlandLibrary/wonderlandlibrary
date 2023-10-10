// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.entities;

import com.viaversion.viaversion.util.EntityTypeUtil;

public enum Entity1_14Types implements EntityType
{
    ENTITY(-1), 
    AREA_EFFECT_CLOUD(0, (EntityType)Entity1_14Types.ENTITY), 
    END_CRYSTAL(17, (EntityType)Entity1_14Types.ENTITY), 
    EVOKER_FANGS(21, (EntityType)Entity1_14Types.ENTITY), 
    EXPERIENCE_ORB(23, (EntityType)Entity1_14Types.ENTITY), 
    EYE_OF_ENDER(24, (EntityType)Entity1_14Types.ENTITY), 
    FALLING_BLOCK(25, (EntityType)Entity1_14Types.ENTITY), 
    FIREWORK_ROCKET(26, (EntityType)Entity1_14Types.ENTITY), 
    ITEM(34, (EntityType)Entity1_14Types.ENTITY), 
    LLAMA_SPIT(39, (EntityType)Entity1_14Types.ENTITY), 
    TNT(58, (EntityType)Entity1_14Types.ENTITY), 
    SHULKER_BULLET(63, (EntityType)Entity1_14Types.ENTITY), 
    FISHING_BOBBER(101, (EntityType)Entity1_14Types.ENTITY), 
    LIVINGENTITY(-1, (EntityType)Entity1_14Types.ENTITY), 
    ARMOR_STAND(1, (EntityType)Entity1_14Types.LIVINGENTITY), 
    PLAYER(100, (EntityType)Entity1_14Types.LIVINGENTITY), 
    ABSTRACT_INSENTIENT(-1, (EntityType)Entity1_14Types.LIVINGENTITY), 
    ENDER_DRAGON(18, (EntityType)Entity1_14Types.ABSTRACT_INSENTIENT), 
    ABSTRACT_CREATURE(-1, (EntityType)Entity1_14Types.ABSTRACT_INSENTIENT), 
    ABSTRACT_AGEABLE(-1, (EntityType)Entity1_14Types.ABSTRACT_CREATURE), 
    VILLAGER(84, (EntityType)Entity1_14Types.ABSTRACT_AGEABLE), 
    WANDERING_TRADER(88, (EntityType)Entity1_14Types.ABSTRACT_AGEABLE), 
    ABSTRACT_ANIMAL(-1, (EntityType)Entity1_14Types.ABSTRACT_AGEABLE), 
    DOLPHIN(13, (EntityType)Entity1_14Types.ABSTRACT_INSENTIENT), 
    CHICKEN(8, (EntityType)Entity1_14Types.ABSTRACT_ANIMAL), 
    COW(10, (EntityType)Entity1_14Types.ABSTRACT_ANIMAL), 
    MOOSHROOM(49, (EntityType)Entity1_14Types.COW), 
    PANDA(52, (EntityType)Entity1_14Types.ABSTRACT_INSENTIENT), 
    PIG(54, (EntityType)Entity1_14Types.ABSTRACT_ANIMAL), 
    POLAR_BEAR(57, (EntityType)Entity1_14Types.ABSTRACT_ANIMAL), 
    RABBIT(59, (EntityType)Entity1_14Types.ABSTRACT_ANIMAL), 
    SHEEP(61, (EntityType)Entity1_14Types.ABSTRACT_ANIMAL), 
    TURTLE(77, (EntityType)Entity1_14Types.ABSTRACT_ANIMAL), 
    FOX(27, (EntityType)Entity1_14Types.ABSTRACT_ANIMAL), 
    ABSTRACT_TAMEABLE_ANIMAL(-1, (EntityType)Entity1_14Types.ABSTRACT_ANIMAL), 
    CAT(6, (EntityType)Entity1_14Types.ABSTRACT_TAMEABLE_ANIMAL), 
    OCELOT(50, (EntityType)Entity1_14Types.ABSTRACT_TAMEABLE_ANIMAL), 
    WOLF(93, (EntityType)Entity1_14Types.ABSTRACT_TAMEABLE_ANIMAL), 
    ABSTRACT_PARROT(-1, (EntityType)Entity1_14Types.ABSTRACT_TAMEABLE_ANIMAL), 
    PARROT(53, (EntityType)Entity1_14Types.ABSTRACT_PARROT), 
    ABSTRACT_HORSE(-1, (EntityType)Entity1_14Types.ABSTRACT_ANIMAL), 
    CHESTED_HORSE(-1, (EntityType)Entity1_14Types.ABSTRACT_HORSE), 
    DONKEY(12, (EntityType)Entity1_14Types.CHESTED_HORSE), 
    MULE(48, (EntityType)Entity1_14Types.CHESTED_HORSE), 
    LLAMA(38, (EntityType)Entity1_14Types.CHESTED_HORSE), 
    TRADER_LLAMA(75, (EntityType)Entity1_14Types.CHESTED_HORSE), 
    HORSE(31, (EntityType)Entity1_14Types.ABSTRACT_HORSE), 
    SKELETON_HORSE(66, (EntityType)Entity1_14Types.ABSTRACT_HORSE), 
    ZOMBIE_HORSE(95, (EntityType)Entity1_14Types.ABSTRACT_HORSE), 
    ABSTRACT_GOLEM(-1, (EntityType)Entity1_14Types.ABSTRACT_CREATURE), 
    SNOW_GOLEM(69, (EntityType)Entity1_14Types.ABSTRACT_GOLEM), 
    IRON_GOLEM(85, (EntityType)Entity1_14Types.ABSTRACT_GOLEM), 
    SHULKER(62, (EntityType)Entity1_14Types.ABSTRACT_GOLEM), 
    ABSTRACT_FISHES(-1, (EntityType)Entity1_14Types.ABSTRACT_CREATURE), 
    COD(9, (EntityType)Entity1_14Types.ABSTRACT_FISHES), 
    PUFFERFISH(55, (EntityType)Entity1_14Types.ABSTRACT_FISHES), 
    SALMON(60, (EntityType)Entity1_14Types.ABSTRACT_FISHES), 
    TROPICAL_FISH(76, (EntityType)Entity1_14Types.ABSTRACT_FISHES), 
    ABSTRACT_MONSTER(-1, (EntityType)Entity1_14Types.ABSTRACT_CREATURE), 
    BLAZE(4, (EntityType)Entity1_14Types.ABSTRACT_MONSTER), 
    CREEPER(11, (EntityType)Entity1_14Types.ABSTRACT_MONSTER), 
    ENDERMITE(20, (EntityType)Entity1_14Types.ABSTRACT_MONSTER), 
    ENDERMAN(19, (EntityType)Entity1_14Types.ABSTRACT_MONSTER), 
    GIANT(29, (EntityType)Entity1_14Types.ABSTRACT_MONSTER), 
    SILVERFISH(64, (EntityType)Entity1_14Types.ABSTRACT_MONSTER), 
    VEX(83, (EntityType)Entity1_14Types.ABSTRACT_MONSTER), 
    WITCH(89, (EntityType)Entity1_14Types.ABSTRACT_MONSTER), 
    WITHER(90, (EntityType)Entity1_14Types.ABSTRACT_MONSTER), 
    RAVAGER(98, (EntityType)Entity1_14Types.ABSTRACT_MONSTER), 
    ABSTRACT_ILLAGER_BASE(-1, (EntityType)Entity1_14Types.ABSTRACT_MONSTER), 
    ABSTRACT_EVO_ILLU_ILLAGER(-1, (EntityType)Entity1_14Types.ABSTRACT_ILLAGER_BASE), 
    EVOKER(22, (EntityType)Entity1_14Types.ABSTRACT_EVO_ILLU_ILLAGER), 
    ILLUSIONER(33, (EntityType)Entity1_14Types.ABSTRACT_EVO_ILLU_ILLAGER), 
    VINDICATOR(86, (EntityType)Entity1_14Types.ABSTRACT_ILLAGER_BASE), 
    PILLAGER(87, (EntityType)Entity1_14Types.ABSTRACT_ILLAGER_BASE), 
    ABSTRACT_SKELETON(-1, (EntityType)Entity1_14Types.ABSTRACT_MONSTER), 
    SKELETON(65, (EntityType)Entity1_14Types.ABSTRACT_SKELETON), 
    STRAY(74, (EntityType)Entity1_14Types.ABSTRACT_SKELETON), 
    WITHER_SKELETON(91, (EntityType)Entity1_14Types.ABSTRACT_SKELETON), 
    GUARDIAN(30, (EntityType)Entity1_14Types.ABSTRACT_MONSTER), 
    ELDER_GUARDIAN(16, (EntityType)Entity1_14Types.GUARDIAN), 
    SPIDER(72, (EntityType)Entity1_14Types.ABSTRACT_MONSTER), 
    CAVE_SPIDER(7, (EntityType)Entity1_14Types.SPIDER), 
    ZOMBIE(94, (EntityType)Entity1_14Types.ABSTRACT_MONSTER), 
    DROWNED(15, (EntityType)Entity1_14Types.ZOMBIE), 
    HUSK(32, (EntityType)Entity1_14Types.ZOMBIE), 
    ZOMBIE_PIGMAN(56, (EntityType)Entity1_14Types.ZOMBIE), 
    ZOMBIE_VILLAGER(96, (EntityType)Entity1_14Types.ZOMBIE), 
    ABSTRACT_FLYING(-1, (EntityType)Entity1_14Types.ABSTRACT_INSENTIENT), 
    GHAST(28, (EntityType)Entity1_14Types.ABSTRACT_FLYING), 
    PHANTOM(97, (EntityType)Entity1_14Types.ABSTRACT_FLYING), 
    ABSTRACT_AMBIENT(-1, (EntityType)Entity1_14Types.ABSTRACT_INSENTIENT), 
    BAT(3, (EntityType)Entity1_14Types.ABSTRACT_AMBIENT), 
    ABSTRACT_WATERMOB(-1, (EntityType)Entity1_14Types.ABSTRACT_INSENTIENT), 
    SQUID(73, (EntityType)Entity1_14Types.ABSTRACT_WATERMOB), 
    SLIME(67, (EntityType)Entity1_14Types.ABSTRACT_INSENTIENT), 
    MAGMA_CUBE(40, (EntityType)Entity1_14Types.SLIME), 
    ABSTRACT_HANGING(-1, (EntityType)Entity1_14Types.ENTITY), 
    LEASH_KNOT(37, (EntityType)Entity1_14Types.ABSTRACT_HANGING), 
    ITEM_FRAME(35, (EntityType)Entity1_14Types.ABSTRACT_HANGING), 
    PAINTING(51, (EntityType)Entity1_14Types.ABSTRACT_HANGING), 
    ABSTRACT_LIGHTNING(-1, (EntityType)Entity1_14Types.ENTITY), 
    LIGHTNING_BOLT(99, (EntityType)Entity1_14Types.ABSTRACT_LIGHTNING), 
    ABSTRACT_ARROW(-1, (EntityType)Entity1_14Types.ENTITY), 
    ARROW(2, (EntityType)Entity1_14Types.ABSTRACT_ARROW), 
    SPECTRAL_ARROW(71, (EntityType)Entity1_14Types.ABSTRACT_ARROW), 
    TRIDENT(82, (EntityType)Entity1_14Types.ABSTRACT_ARROW), 
    ABSTRACT_FIREBALL(-1, (EntityType)Entity1_14Types.ENTITY), 
    DRAGON_FIREBALL(14, (EntityType)Entity1_14Types.ABSTRACT_FIREBALL), 
    FIREBALL(36, (EntityType)Entity1_14Types.ABSTRACT_FIREBALL), 
    SMALL_FIREBALL(68, (EntityType)Entity1_14Types.ABSTRACT_FIREBALL), 
    WITHER_SKULL(92, (EntityType)Entity1_14Types.ABSTRACT_FIREBALL), 
    PROJECTILE_ABSTRACT(-1, (EntityType)Entity1_14Types.ENTITY), 
    SNOWBALL(70, (EntityType)Entity1_14Types.PROJECTILE_ABSTRACT), 
    ENDER_PEARL(79, (EntityType)Entity1_14Types.PROJECTILE_ABSTRACT), 
    EGG(78, (EntityType)Entity1_14Types.PROJECTILE_ABSTRACT), 
    POTION(81, (EntityType)Entity1_14Types.PROJECTILE_ABSTRACT), 
    EXPERIENCE_BOTTLE(80, (EntityType)Entity1_14Types.PROJECTILE_ABSTRACT), 
    MINECART_ABSTRACT(-1, (EntityType)Entity1_14Types.ENTITY), 
    CHESTED_MINECART_ABSTRACT(-1, (EntityType)Entity1_14Types.MINECART_ABSTRACT), 
    CHEST_MINECART(42, (EntityType)Entity1_14Types.CHESTED_MINECART_ABSTRACT), 
    HOPPER_MINECART(45, (EntityType)Entity1_14Types.CHESTED_MINECART_ABSTRACT), 
    MINECART(41, (EntityType)Entity1_14Types.MINECART_ABSTRACT), 
    FURNACE_MINECART(44, (EntityType)Entity1_14Types.MINECART_ABSTRACT), 
    COMMAND_BLOCK_MINECART(43, (EntityType)Entity1_14Types.MINECART_ABSTRACT), 
    TNT_MINECART(47, (EntityType)Entity1_14Types.MINECART_ABSTRACT), 
    SPAWNER_MINECART(46, (EntityType)Entity1_14Types.MINECART_ABSTRACT), 
    BOAT(5, (EntityType)Entity1_14Types.ENTITY);
    
    private static final EntityType[] TYPES;
    private final int id;
    private final EntityType parent;
    
    private Entity1_14Types(final int id) {
        this.id = id;
        this.parent = null;
    }
    
    private Entity1_14Types(final int id, final EntityType parent) {
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
        return EntityTypeUtil.getTypeFromId(Entity1_14Types.TYPES, typeId, Entity1_14Types.ENTITY);
    }
    
    static {
        TYPES = EntityTypeUtil.toOrderedArray(values());
    }
}
