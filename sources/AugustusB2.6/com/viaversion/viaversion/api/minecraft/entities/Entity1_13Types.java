// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.viaversion.viaversion.api.Via;

public class Entity1_13Types
{
    public static EntityType getTypeFromId(final int typeID, final boolean isObject) {
        Optional<EntityType> type;
        if (isObject) {
            type = ObjectType.getPCEntity(typeID);
        }
        else {
            type = EntityType.findById(typeID);
        }
        if (!type.isPresent()) {
            Via.getPlatform().getLogger().severe("Could not find 1.13 type id " + typeID + " isObject=" + isObject);
            return EntityType.ENTITY;
        }
        return type.get();
    }
    
    public enum EntityType implements com.viaversion.viaversion.api.minecraft.entities.EntityType
    {
        ENTITY(-1), 
        AREA_EFFECT_CLOUD(0, EntityType.ENTITY), 
        END_CRYSTAL(16, EntityType.ENTITY), 
        EVOKER_FANGS(20, EntityType.ENTITY), 
        EXPERIENCE_ORB(22, EntityType.ENTITY), 
        EYE_OF_ENDER(23, EntityType.ENTITY), 
        FALLING_BLOCK(24, EntityType.ENTITY), 
        FIREWORK_ROCKET(25, EntityType.ENTITY), 
        ITEM(32, EntityType.ENTITY), 
        LLAMA_SPIT(37, EntityType.ENTITY), 
        TNT(55, EntityType.ENTITY), 
        SHULKER_BULLET(60, EntityType.ENTITY), 
        FISHING_BOBBER(93, EntityType.ENTITY), 
        LIVINGENTITY(-1, EntityType.ENTITY), 
        ARMOR_STAND(1, EntityType.LIVINGENTITY), 
        PLAYER(92, EntityType.LIVINGENTITY), 
        ABSTRACT_INSENTIENT(-1, EntityType.LIVINGENTITY), 
        ENDER_DRAGON(17, EntityType.ABSTRACT_INSENTIENT), 
        ABSTRACT_CREATURE(-1, EntityType.ABSTRACT_INSENTIENT), 
        ABSTRACT_AGEABLE(-1, EntityType.ABSTRACT_CREATURE), 
        VILLAGER(79, EntityType.ABSTRACT_AGEABLE), 
        ABSTRACT_ANIMAL(-1, EntityType.ABSTRACT_AGEABLE), 
        CHICKEN(7, EntityType.ABSTRACT_ANIMAL), 
        COW(9, EntityType.ABSTRACT_ANIMAL), 
        MOOSHROOM(47, EntityType.COW), 
        PIG(51, EntityType.ABSTRACT_ANIMAL), 
        POLAR_BEAR(54, EntityType.ABSTRACT_ANIMAL), 
        RABBIT(56, EntityType.ABSTRACT_ANIMAL), 
        SHEEP(58, EntityType.ABSTRACT_ANIMAL), 
        TURTLE(73, EntityType.ABSTRACT_ANIMAL), 
        ABSTRACT_TAMEABLE_ANIMAL(-1, EntityType.ABSTRACT_ANIMAL), 
        OCELOT(48, EntityType.ABSTRACT_TAMEABLE_ANIMAL), 
        WOLF(86, EntityType.ABSTRACT_TAMEABLE_ANIMAL), 
        ABSTRACT_PARROT(-1, EntityType.ABSTRACT_TAMEABLE_ANIMAL), 
        PARROT(50, EntityType.ABSTRACT_PARROT), 
        ABSTRACT_HORSE(-1, EntityType.ABSTRACT_ANIMAL), 
        CHESTED_HORSE(-1, EntityType.ABSTRACT_HORSE), 
        DONKEY(11, EntityType.CHESTED_HORSE), 
        MULE(46, EntityType.CHESTED_HORSE), 
        LLAMA(36, EntityType.CHESTED_HORSE), 
        HORSE(29, EntityType.ABSTRACT_HORSE), 
        SKELETON_HORSE(63, EntityType.ABSTRACT_HORSE), 
        ZOMBIE_HORSE(88, EntityType.ABSTRACT_HORSE), 
        ABSTRACT_GOLEM(-1, EntityType.ABSTRACT_CREATURE), 
        SNOW_GOLEM(66, EntityType.ABSTRACT_GOLEM), 
        IRON_GOLEM(80, EntityType.ABSTRACT_GOLEM), 
        SHULKER(59, EntityType.ABSTRACT_GOLEM), 
        ABSTRACT_FISHES(-1, EntityType.ABSTRACT_CREATURE), 
        COD(8, EntityType.ABSTRACT_FISHES), 
        PUFFERFISH(52, EntityType.ABSTRACT_FISHES), 
        SALMON(57, EntityType.ABSTRACT_FISHES), 
        TROPICAL_FISH(72, EntityType.ABSTRACT_FISHES), 
        ABSTRACT_MONSTER(-1, EntityType.ABSTRACT_CREATURE), 
        BLAZE(4, EntityType.ABSTRACT_MONSTER), 
        CREEPER(10, EntityType.ABSTRACT_MONSTER), 
        ENDERMITE(19, EntityType.ABSTRACT_MONSTER), 
        ENDERMAN(18, EntityType.ABSTRACT_MONSTER), 
        GIANT(27, EntityType.ABSTRACT_MONSTER), 
        SILVERFISH(61, EntityType.ABSTRACT_MONSTER), 
        VEX(78, EntityType.ABSTRACT_MONSTER), 
        WITCH(82, EntityType.ABSTRACT_MONSTER), 
        WITHER(83, EntityType.ABSTRACT_MONSTER), 
        ABSTRACT_ILLAGER_BASE(-1, EntityType.ABSTRACT_MONSTER), 
        ABSTRACT_EVO_ILLU_ILLAGER(-1, EntityType.ABSTRACT_ILLAGER_BASE), 
        EVOKER(21, EntityType.ABSTRACT_EVO_ILLU_ILLAGER), 
        ILLUSIONER(31, EntityType.ABSTRACT_EVO_ILLU_ILLAGER), 
        VINDICATOR(81, EntityType.ABSTRACT_ILLAGER_BASE), 
        ABSTRACT_SKELETON(-1, EntityType.ABSTRACT_MONSTER), 
        SKELETON(62, EntityType.ABSTRACT_SKELETON), 
        STRAY(71, EntityType.ABSTRACT_SKELETON), 
        WITHER_SKELETON(84, EntityType.ABSTRACT_SKELETON), 
        GUARDIAN(28, EntityType.ABSTRACT_MONSTER), 
        ELDER_GUARDIAN(15, EntityType.GUARDIAN), 
        SPIDER(69, EntityType.ABSTRACT_MONSTER), 
        CAVE_SPIDER(6, EntityType.SPIDER), 
        ZOMBIE(87, EntityType.ABSTRACT_MONSTER), 
        DROWNED(14, EntityType.ZOMBIE), 
        HUSK(30, EntityType.ZOMBIE), 
        ZOMBIE_PIGMAN(53, EntityType.ZOMBIE), 
        ZOMBIE_VILLAGER(89, EntityType.ZOMBIE), 
        ABSTRACT_FLYING(-1, EntityType.ABSTRACT_INSENTIENT), 
        GHAST(26, EntityType.ABSTRACT_FLYING), 
        PHANTOM(90, EntityType.ABSTRACT_FLYING), 
        ABSTRACT_AMBIENT(-1, EntityType.ABSTRACT_INSENTIENT), 
        BAT(3, EntityType.ABSTRACT_AMBIENT), 
        ABSTRACT_WATERMOB(-1, EntityType.ABSTRACT_INSENTIENT), 
        SQUID(70, EntityType.ABSTRACT_WATERMOB), 
        DOLPHIN(12, EntityType.ABSTRACT_WATERMOB), 
        SLIME(64, EntityType.ABSTRACT_INSENTIENT), 
        MAGMA_CUBE(38, EntityType.SLIME), 
        ABSTRACT_HANGING(-1, EntityType.ENTITY), 
        LEASH_KNOT(35, EntityType.ABSTRACT_HANGING), 
        ITEM_FRAME(33, EntityType.ABSTRACT_HANGING), 
        PAINTING(49, EntityType.ABSTRACT_HANGING), 
        ABSTRACT_LIGHTNING(-1, EntityType.ENTITY), 
        LIGHTNING_BOLT(91, EntityType.ABSTRACT_LIGHTNING), 
        ABSTRACT_ARROW(-1, EntityType.ENTITY), 
        ARROW(2, EntityType.ABSTRACT_ARROW), 
        SPECTRAL_ARROW(68, EntityType.ABSTRACT_ARROW), 
        TRIDENT(94, EntityType.ABSTRACT_ARROW), 
        ABSTRACT_FIREBALL(-1, EntityType.ENTITY), 
        DRAGON_FIREBALL(13, EntityType.ABSTRACT_FIREBALL), 
        FIREBALL(34, EntityType.ABSTRACT_FIREBALL), 
        SMALL_FIREBALL(65, EntityType.ABSTRACT_FIREBALL), 
        WITHER_SKULL(85, EntityType.ABSTRACT_FIREBALL), 
        PROJECTILE_ABSTRACT(-1, EntityType.ENTITY), 
        SNOWBALL(67, EntityType.PROJECTILE_ABSTRACT), 
        ENDER_PEARL(75, EntityType.PROJECTILE_ABSTRACT), 
        EGG(74, EntityType.PROJECTILE_ABSTRACT), 
        POTION(77, EntityType.PROJECTILE_ABSTRACT), 
        EXPERIENCE_BOTTLE(76, EntityType.PROJECTILE_ABSTRACT), 
        MINECART_ABSTRACT(-1, EntityType.ENTITY), 
        CHESTED_MINECART_ABSTRACT(-1, EntityType.MINECART_ABSTRACT), 
        CHEST_MINECART(40, EntityType.CHESTED_MINECART_ABSTRACT), 
        HOPPER_MINECART(43, EntityType.CHESTED_MINECART_ABSTRACT), 
        MINECART(39, EntityType.MINECART_ABSTRACT), 
        FURNACE_MINECART(42, EntityType.MINECART_ABSTRACT), 
        COMMAND_BLOCK_MINECART(41, EntityType.MINECART_ABSTRACT), 
        TNT_MINECART(45, EntityType.MINECART_ABSTRACT), 
        SPAWNER_MINECART(44, EntityType.MINECART_ABSTRACT), 
        BOAT(5, EntityType.ENTITY);
        
        private static final Map<Integer, EntityType> TYPES;
        private final int id;
        private final EntityType parent;
        
        private EntityType(final int id) {
            this.id = id;
            this.parent = null;
        }
        
        private EntityType(final int id, final EntityType parent) {
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
        
        public static Optional<EntityType> findById(final int id) {
            if (id == -1) {
                return Optional.empty();
            }
            return Optional.ofNullable(EntityType.TYPES.get(id));
        }
        
        static {
            TYPES = new HashMap<Integer, EntityType>();
            for (final EntityType type : values()) {
                EntityType.TYPES.put(type.id, type);
            }
        }
    }
    
    public enum ObjectType implements com.viaversion.viaversion.api.minecraft.entities.ObjectType
    {
        BOAT(1, EntityType.BOAT), 
        ITEM(2, EntityType.ITEM), 
        AREA_EFFECT_CLOUD(3, EntityType.AREA_EFFECT_CLOUD), 
        MINECART(10, EntityType.MINECART), 
        TNT_PRIMED(50, EntityType.TNT), 
        ENDER_CRYSTAL(51, EntityType.END_CRYSTAL), 
        TIPPED_ARROW(60, EntityType.ARROW), 
        SNOWBALL(61, EntityType.SNOWBALL), 
        EGG(62, EntityType.EGG), 
        FIREBALL(63, EntityType.FIREBALL), 
        SMALL_FIREBALL(64, EntityType.SMALL_FIREBALL), 
        ENDER_PEARL(65, EntityType.ENDER_PEARL), 
        WITHER_SKULL(66, EntityType.WITHER_SKULL), 
        SHULKER_BULLET(67, EntityType.SHULKER_BULLET), 
        LLAMA_SPIT(68, EntityType.LLAMA_SPIT), 
        FALLING_BLOCK(70, EntityType.FALLING_BLOCK), 
        ITEM_FRAME(71, EntityType.ITEM_FRAME), 
        EYE_OF_ENDER(72, EntityType.EYE_OF_ENDER), 
        POTION(73, EntityType.POTION), 
        EXPERIENCE_BOTTLE(75, EntityType.EXPERIENCE_BOTTLE), 
        FIREWORK_ROCKET(76, EntityType.FIREWORK_ROCKET), 
        LEASH(77, EntityType.LEASH_KNOT), 
        ARMOR_STAND(78, EntityType.ARMOR_STAND), 
        EVOKER_FANGS(79, EntityType.EVOKER_FANGS), 
        FISHIHNG_HOOK(90, EntityType.FISHING_BOBBER), 
        SPECTRAL_ARROW(91, EntityType.SPECTRAL_ARROW), 
        DRAGON_FIREBALL(93, EntityType.DRAGON_FIREBALL), 
        TRIDENT(94, EntityType.TRIDENT);
        
        private static final Map<Integer, ObjectType> TYPES;
        private final int id;
        private final EntityType type;
        
        private ObjectType(final int id, final EntityType type) {
            this.id = id;
            this.type = type;
        }
        
        @Override
        public int getId() {
            return this.id;
        }
        
        @Override
        public EntityType getType() {
            return this.type;
        }
        
        public static Optional<ObjectType> findById(final int id) {
            if (id == -1) {
                return Optional.empty();
            }
            return Optional.ofNullable(ObjectType.TYPES.get(id));
        }
        
        public static Optional<EntityType> getPCEntity(final int id) {
            final Optional<ObjectType> output = findById(id);
            if (!output.isPresent()) {
                return Optional.empty();
            }
            return Optional.of(output.get().type);
        }
        
        public static Optional<ObjectType> fromEntityType(final EntityType type) {
            for (final ObjectType ent : values()) {
                if (ent.type == type) {
                    return Optional.of(ent);
                }
            }
            return Optional.empty();
        }
        
        static {
            TYPES = new HashMap<Integer, ObjectType>();
            for (final ObjectType type : values()) {
                ObjectType.TYPES.put(type.id, type);
            }
        }
    }
}
