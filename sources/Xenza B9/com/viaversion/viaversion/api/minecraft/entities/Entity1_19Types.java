// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.minecraft.entities;

import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.util.EntityTypeUtil;
import com.google.common.base.Preconditions;
import java.util.Locale;

public enum Entity1_19Types implements EntityType
{
    ENTITY((EntityType)null, (String)null), 
    AREA_EFFECT_CLOUD((EntityType)Entity1_19Types.ENTITY), 
    END_CRYSTAL((EntityType)Entity1_19Types.ENTITY), 
    EVOKER_FANGS((EntityType)Entity1_19Types.ENTITY), 
    EXPERIENCE_ORB((EntityType)Entity1_19Types.ENTITY), 
    EYE_OF_ENDER((EntityType)Entity1_19Types.ENTITY), 
    FALLING_BLOCK((EntityType)Entity1_19Types.ENTITY), 
    FIREWORK_ROCKET((EntityType)Entity1_19Types.ENTITY), 
    ITEM((EntityType)Entity1_19Types.ENTITY), 
    LLAMA_SPIT((EntityType)Entity1_19Types.ENTITY), 
    TNT((EntityType)Entity1_19Types.ENTITY), 
    SHULKER_BULLET((EntityType)Entity1_19Types.ENTITY), 
    FISHING_BOBBER((EntityType)Entity1_19Types.ENTITY), 
    LIVINGENTITY((EntityType)Entity1_19Types.ENTITY, (String)null), 
    ARMOR_STAND((EntityType)Entity1_19Types.LIVINGENTITY), 
    MARKER((EntityType)Entity1_19Types.ENTITY), 
    PLAYER((EntityType)Entity1_19Types.LIVINGENTITY), 
    ABSTRACT_INSENTIENT((EntityType)Entity1_19Types.LIVINGENTITY, (String)null), 
    ENDER_DRAGON((EntityType)Entity1_19Types.ABSTRACT_INSENTIENT), 
    BEE((EntityType)Entity1_19Types.ABSTRACT_INSENTIENT), 
    ABSTRACT_CREATURE((EntityType)Entity1_19Types.ABSTRACT_INSENTIENT, (String)null), 
    ABSTRACT_AGEABLE((EntityType)Entity1_19Types.ABSTRACT_CREATURE, (String)null), 
    VILLAGER((EntityType)Entity1_19Types.ABSTRACT_AGEABLE), 
    WANDERING_TRADER((EntityType)Entity1_19Types.ABSTRACT_AGEABLE), 
    ABSTRACT_ANIMAL((EntityType)Entity1_19Types.ABSTRACT_AGEABLE, (String)null), 
    AXOLOTL((EntityType)Entity1_19Types.ABSTRACT_ANIMAL), 
    DOLPHIN((EntityType)Entity1_19Types.ABSTRACT_INSENTIENT), 
    CHICKEN((EntityType)Entity1_19Types.ABSTRACT_ANIMAL), 
    COW((EntityType)Entity1_19Types.ABSTRACT_ANIMAL), 
    MOOSHROOM((EntityType)Entity1_19Types.COW), 
    PANDA((EntityType)Entity1_19Types.ABSTRACT_INSENTIENT), 
    PIG((EntityType)Entity1_19Types.ABSTRACT_ANIMAL), 
    POLAR_BEAR((EntityType)Entity1_19Types.ABSTRACT_ANIMAL), 
    RABBIT((EntityType)Entity1_19Types.ABSTRACT_ANIMAL), 
    SHEEP((EntityType)Entity1_19Types.ABSTRACT_ANIMAL), 
    TURTLE((EntityType)Entity1_19Types.ABSTRACT_ANIMAL), 
    FOX((EntityType)Entity1_19Types.ABSTRACT_ANIMAL), 
    FROG((EntityType)Entity1_19Types.ABSTRACT_ANIMAL), 
    GOAT((EntityType)Entity1_19Types.ABSTRACT_ANIMAL), 
    ABSTRACT_TAMEABLE_ANIMAL((EntityType)Entity1_19Types.ABSTRACT_ANIMAL, (String)null), 
    CAT((EntityType)Entity1_19Types.ABSTRACT_TAMEABLE_ANIMAL), 
    OCELOT((EntityType)Entity1_19Types.ABSTRACT_TAMEABLE_ANIMAL), 
    WOLF((EntityType)Entity1_19Types.ABSTRACT_TAMEABLE_ANIMAL), 
    ABSTRACT_PARROT((EntityType)Entity1_19Types.ABSTRACT_TAMEABLE_ANIMAL, (String)null), 
    PARROT((EntityType)Entity1_19Types.ABSTRACT_PARROT), 
    ABSTRACT_HORSE((EntityType)Entity1_19Types.ABSTRACT_ANIMAL, (String)null), 
    CHESTED_HORSE((EntityType)Entity1_19Types.ABSTRACT_HORSE, (String)null), 
    DONKEY((EntityType)Entity1_19Types.CHESTED_HORSE), 
    MULE((EntityType)Entity1_19Types.CHESTED_HORSE), 
    LLAMA((EntityType)Entity1_19Types.CHESTED_HORSE), 
    TRADER_LLAMA((EntityType)Entity1_19Types.CHESTED_HORSE), 
    HORSE((EntityType)Entity1_19Types.ABSTRACT_HORSE), 
    SKELETON_HORSE((EntityType)Entity1_19Types.ABSTRACT_HORSE), 
    ZOMBIE_HORSE((EntityType)Entity1_19Types.ABSTRACT_HORSE), 
    ABSTRACT_GOLEM((EntityType)Entity1_19Types.ABSTRACT_CREATURE, (String)null), 
    SNOW_GOLEM((EntityType)Entity1_19Types.ABSTRACT_GOLEM), 
    IRON_GOLEM((EntityType)Entity1_19Types.ABSTRACT_GOLEM), 
    SHULKER((EntityType)Entity1_19Types.ABSTRACT_GOLEM), 
    ABSTRACT_FISHES((EntityType)Entity1_19Types.ABSTRACT_CREATURE, (String)null), 
    COD((EntityType)Entity1_19Types.ABSTRACT_FISHES), 
    PUFFERFISH((EntityType)Entity1_19Types.ABSTRACT_FISHES), 
    SALMON((EntityType)Entity1_19Types.ABSTRACT_FISHES), 
    TROPICAL_FISH((EntityType)Entity1_19Types.ABSTRACT_FISHES), 
    ABSTRACT_MONSTER((EntityType)Entity1_19Types.ABSTRACT_CREATURE, (String)null), 
    BLAZE((EntityType)Entity1_19Types.ABSTRACT_MONSTER), 
    CREEPER((EntityType)Entity1_19Types.ABSTRACT_MONSTER), 
    ENDERMITE((EntityType)Entity1_19Types.ABSTRACT_MONSTER), 
    ENDERMAN((EntityType)Entity1_19Types.ABSTRACT_MONSTER), 
    GIANT((EntityType)Entity1_19Types.ABSTRACT_MONSTER), 
    SILVERFISH((EntityType)Entity1_19Types.ABSTRACT_MONSTER), 
    VEX((EntityType)Entity1_19Types.ABSTRACT_MONSTER), 
    WITCH((EntityType)Entity1_19Types.ABSTRACT_MONSTER), 
    WITHER((EntityType)Entity1_19Types.ABSTRACT_MONSTER), 
    RAVAGER((EntityType)Entity1_19Types.ABSTRACT_MONSTER), 
    ABSTRACT_PIGLIN((EntityType)Entity1_19Types.ABSTRACT_MONSTER, (String)null), 
    PIGLIN((EntityType)Entity1_19Types.ABSTRACT_PIGLIN), 
    PIGLIN_BRUTE((EntityType)Entity1_19Types.ABSTRACT_PIGLIN), 
    HOGLIN((EntityType)Entity1_19Types.ABSTRACT_ANIMAL), 
    STRIDER((EntityType)Entity1_19Types.ABSTRACT_ANIMAL), 
    TADPOLE((EntityType)Entity1_19Types.ABSTRACT_FISHES), 
    ZOGLIN((EntityType)Entity1_19Types.ABSTRACT_MONSTER), 
    WARDEN((EntityType)Entity1_19Types.ABSTRACT_MONSTER), 
    ABSTRACT_ILLAGER_BASE((EntityType)Entity1_19Types.ABSTRACT_MONSTER, (String)null), 
    ABSTRACT_EVO_ILLU_ILLAGER((EntityType)Entity1_19Types.ABSTRACT_ILLAGER_BASE, (String)null), 
    EVOKER((EntityType)Entity1_19Types.ABSTRACT_EVO_ILLU_ILLAGER), 
    ILLUSIONER((EntityType)Entity1_19Types.ABSTRACT_EVO_ILLU_ILLAGER), 
    VINDICATOR((EntityType)Entity1_19Types.ABSTRACT_ILLAGER_BASE), 
    PILLAGER((EntityType)Entity1_19Types.ABSTRACT_ILLAGER_BASE), 
    ABSTRACT_SKELETON((EntityType)Entity1_19Types.ABSTRACT_MONSTER, (String)null), 
    SKELETON((EntityType)Entity1_19Types.ABSTRACT_SKELETON), 
    STRAY((EntityType)Entity1_19Types.ABSTRACT_SKELETON), 
    WITHER_SKELETON((EntityType)Entity1_19Types.ABSTRACT_SKELETON), 
    GUARDIAN((EntityType)Entity1_19Types.ABSTRACT_MONSTER), 
    ELDER_GUARDIAN((EntityType)Entity1_19Types.GUARDIAN), 
    SPIDER((EntityType)Entity1_19Types.ABSTRACT_MONSTER), 
    CAVE_SPIDER((EntityType)Entity1_19Types.SPIDER), 
    ZOMBIE((EntityType)Entity1_19Types.ABSTRACT_MONSTER), 
    DROWNED((EntityType)Entity1_19Types.ZOMBIE), 
    HUSK((EntityType)Entity1_19Types.ZOMBIE), 
    ZOMBIFIED_PIGLIN((EntityType)Entity1_19Types.ZOMBIE), 
    ZOMBIE_VILLAGER((EntityType)Entity1_19Types.ZOMBIE), 
    ABSTRACT_FLYING((EntityType)Entity1_19Types.ABSTRACT_INSENTIENT, (String)null), 
    GHAST((EntityType)Entity1_19Types.ABSTRACT_FLYING), 
    PHANTOM((EntityType)Entity1_19Types.ABSTRACT_FLYING), 
    ABSTRACT_AMBIENT((EntityType)Entity1_19Types.ABSTRACT_INSENTIENT, (String)null), 
    BAT((EntityType)Entity1_19Types.ABSTRACT_AMBIENT), 
    ALLAY((EntityType)Entity1_19Types.ABSTRACT_CREATURE), 
    ABSTRACT_WATERMOB((EntityType)Entity1_19Types.ABSTRACT_INSENTIENT, (String)null), 
    SQUID((EntityType)Entity1_19Types.ABSTRACT_WATERMOB), 
    GLOW_SQUID((EntityType)Entity1_19Types.SQUID), 
    SLIME((EntityType)Entity1_19Types.ABSTRACT_INSENTIENT), 
    MAGMA_CUBE((EntityType)Entity1_19Types.SLIME), 
    ABSTRACT_HANGING((EntityType)Entity1_19Types.ENTITY, (String)null), 
    LEASH_KNOT((EntityType)Entity1_19Types.ABSTRACT_HANGING), 
    ITEM_FRAME((EntityType)Entity1_19Types.ABSTRACT_HANGING), 
    GLOW_ITEM_FRAME((EntityType)Entity1_19Types.ITEM_FRAME), 
    PAINTING((EntityType)Entity1_19Types.ABSTRACT_HANGING), 
    ABSTRACT_LIGHTNING((EntityType)Entity1_19Types.ENTITY, (String)null), 
    LIGHTNING_BOLT((EntityType)Entity1_19Types.ABSTRACT_LIGHTNING), 
    ABSTRACT_ARROW((EntityType)Entity1_19Types.ENTITY, (String)null), 
    ARROW((EntityType)Entity1_19Types.ABSTRACT_ARROW), 
    SPECTRAL_ARROW((EntityType)Entity1_19Types.ABSTRACT_ARROW), 
    TRIDENT((EntityType)Entity1_19Types.ABSTRACT_ARROW), 
    ABSTRACT_FIREBALL((EntityType)Entity1_19Types.ENTITY, (String)null), 
    DRAGON_FIREBALL((EntityType)Entity1_19Types.ABSTRACT_FIREBALL), 
    FIREBALL((EntityType)Entity1_19Types.ABSTRACT_FIREBALL), 
    SMALL_FIREBALL((EntityType)Entity1_19Types.ABSTRACT_FIREBALL), 
    WITHER_SKULL((EntityType)Entity1_19Types.ABSTRACT_FIREBALL), 
    PROJECTILE_ABSTRACT((EntityType)Entity1_19Types.ENTITY, (String)null), 
    SNOWBALL((EntityType)Entity1_19Types.PROJECTILE_ABSTRACT), 
    ENDER_PEARL((EntityType)Entity1_19Types.PROJECTILE_ABSTRACT), 
    EGG((EntityType)Entity1_19Types.PROJECTILE_ABSTRACT), 
    POTION((EntityType)Entity1_19Types.PROJECTILE_ABSTRACT), 
    EXPERIENCE_BOTTLE((EntityType)Entity1_19Types.PROJECTILE_ABSTRACT), 
    MINECART_ABSTRACT((EntityType)Entity1_19Types.ENTITY, (String)null), 
    CHESTED_MINECART_ABSTRACT((EntityType)Entity1_19Types.MINECART_ABSTRACT, (String)null), 
    CHEST_MINECART((EntityType)Entity1_19Types.CHESTED_MINECART_ABSTRACT), 
    HOPPER_MINECART((EntityType)Entity1_19Types.CHESTED_MINECART_ABSTRACT), 
    MINECART((EntityType)Entity1_19Types.MINECART_ABSTRACT), 
    FURNACE_MINECART((EntityType)Entity1_19Types.MINECART_ABSTRACT), 
    COMMAND_BLOCK_MINECART((EntityType)Entity1_19Types.MINECART_ABSTRACT), 
    TNT_MINECART((EntityType)Entity1_19Types.MINECART_ABSTRACT), 
    SPAWNER_MINECART((EntityType)Entity1_19Types.MINECART_ABSTRACT), 
    BOAT((EntityType)Entity1_19Types.ENTITY), 
    CHEST_BOAT((EntityType)Entity1_19Types.BOAT);
    
    private static final EntityType[] TYPES;
    private final EntityType parent;
    private final String identifier;
    private int id;
    
    private Entity1_19Types(final EntityType parent) {
        this.id = -1;
        this.parent = parent;
        this.identifier = "minecraft:" + this.name().toLowerCase(Locale.ROOT);
    }
    
    private Entity1_19Types(final EntityType parent, final String identifier) {
        this.id = -1;
        this.parent = parent;
        this.identifier = identifier;
    }
    
    @Override
    public int getId() {
        if (this.id == -1) {
            throw new IllegalStateException("Ids have not been initialized yet (type " + this.name() + ")");
        }
        return this.id;
    }
    
    @Override
    public String identifier() {
        Preconditions.checkArgument(this.identifier != null, (Object)"Called identifier method on abstract type");
        return this.identifier;
    }
    
    @Override
    public EntityType getParent() {
        return this.parent;
    }
    
    @Override
    public boolean isAbstractType() {
        return this.identifier == null;
    }
    
    public static EntityType getTypeFromId(final int typeId) {
        return EntityTypeUtil.getTypeFromId(Entity1_19Types.TYPES, typeId, Entity1_19Types.ENTITY);
    }
    
    public static void initialize(final Protocol<?, ?, ?, ?> protocol) {
        EntityTypeUtil.initialize(values(), Entity1_19Types.TYPES, protocol, (type, id) -> type.id = id);
    }
    
    static {
        TYPES = EntityTypeUtil.createSizedArray(values());
    }
}
