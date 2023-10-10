// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.viaversion.viaversion.api.Via;

public class Entity1_11Types
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
            Via.getPlatform().getLogger().severe("Could not find 1.11 type id " + typeID + " isObject=" + isObject);
            return EntityType.ENTITY;
        }
        return type.get();
    }
    
    public enum EntityType implements com.viaversion.viaversion.api.minecraft.entities.EntityType
    {
        ENTITY(-1), 
        DROPPED_ITEM(1, EntityType.ENTITY), 
        EXPERIENCE_ORB(2, EntityType.ENTITY), 
        LEASH_HITCH(8, EntityType.ENTITY), 
        PAINTING(9, EntityType.ENTITY), 
        ARROW(10, EntityType.ENTITY), 
        SNOWBALL(11, EntityType.ENTITY), 
        FIREBALL(12, EntityType.ENTITY), 
        SMALL_FIREBALL(13, EntityType.ENTITY), 
        ENDER_PEARL(14, EntityType.ENTITY), 
        ENDER_SIGNAL(15, EntityType.ENTITY), 
        THROWN_EXP_BOTTLE(17, EntityType.ENTITY), 
        ITEM_FRAME(18, EntityType.ENTITY), 
        WITHER_SKULL(19, EntityType.ENTITY), 
        PRIMED_TNT(20, EntityType.ENTITY), 
        FALLING_BLOCK(21, EntityType.ENTITY), 
        FIREWORK(22, EntityType.ENTITY), 
        SPECTRAL_ARROW(24, EntityType.ARROW), 
        SHULKER_BULLET(25, EntityType.ENTITY), 
        DRAGON_FIREBALL(26, EntityType.FIREBALL), 
        EVOCATION_FANGS(33, EntityType.ENTITY), 
        ENTITY_LIVING(-1, EntityType.ENTITY), 
        ENTITY_INSENTIENT(-1, EntityType.ENTITY_LIVING), 
        ENTITY_AGEABLE(-1, EntityType.ENTITY_INSENTIENT), 
        ENTITY_TAMEABLE_ANIMAL(-1, EntityType.ENTITY_AGEABLE), 
        ENTITY_HUMAN(-1, EntityType.ENTITY_LIVING), 
        ARMOR_STAND(30, EntityType.ENTITY_LIVING), 
        EVOCATION_ILLAGER(34, EntityType.ENTITY_INSENTIENT), 
        VEX(35, EntityType.ENTITY_INSENTIENT), 
        VINDICATION_ILLAGER(36, EntityType.ENTITY_INSENTIENT), 
        MINECART_ABSTRACT(-1, EntityType.ENTITY), 
        MINECART_COMMAND(40, EntityType.MINECART_ABSTRACT), 
        BOAT(41, EntityType.ENTITY), 
        MINECART_RIDEABLE(42, EntityType.MINECART_ABSTRACT), 
        MINECART_CHEST(43, EntityType.MINECART_ABSTRACT), 
        MINECART_FURNACE(44, EntityType.MINECART_ABSTRACT), 
        MINECART_TNT(45, EntityType.MINECART_ABSTRACT), 
        MINECART_HOPPER(46, EntityType.MINECART_ABSTRACT), 
        MINECART_MOB_SPAWNER(47, EntityType.MINECART_ABSTRACT), 
        CREEPER(50, EntityType.ENTITY_INSENTIENT), 
        ABSTRACT_SKELETON(-1, EntityType.ENTITY_INSENTIENT), 
        SKELETON(51, EntityType.ABSTRACT_SKELETON), 
        WITHER_SKELETON(5, EntityType.ABSTRACT_SKELETON), 
        STRAY(6, EntityType.ABSTRACT_SKELETON), 
        SPIDER(52, EntityType.ENTITY_INSENTIENT), 
        GIANT(53, EntityType.ENTITY_INSENTIENT), 
        ZOMBIE(54, EntityType.ENTITY_INSENTIENT), 
        HUSK(23, EntityType.ZOMBIE), 
        ZOMBIE_VILLAGER(27, EntityType.ZOMBIE), 
        SLIME(55, EntityType.ENTITY_INSENTIENT), 
        GHAST(56, EntityType.ENTITY_INSENTIENT), 
        PIG_ZOMBIE(57, EntityType.ZOMBIE), 
        ENDERMAN(58, EntityType.ENTITY_INSENTIENT), 
        CAVE_SPIDER(59, EntityType.SPIDER), 
        SILVERFISH(60, EntityType.ENTITY_INSENTIENT), 
        BLAZE(61, EntityType.ENTITY_INSENTIENT), 
        MAGMA_CUBE(62, EntityType.SLIME), 
        ENDER_DRAGON(63, EntityType.ENTITY_INSENTIENT), 
        WITHER(64, EntityType.ENTITY_INSENTIENT), 
        BAT(65, EntityType.ENTITY_INSENTIENT), 
        WITCH(66, EntityType.ENTITY_INSENTIENT), 
        ENDERMITE(67, EntityType.ENTITY_INSENTIENT), 
        GUARDIAN(68, EntityType.ENTITY_INSENTIENT), 
        ELDER_GUARDIAN(4, EntityType.GUARDIAN), 
        IRON_GOLEM(99, EntityType.ENTITY_INSENTIENT), 
        SHULKER(69, EntityType.IRON_GOLEM), 
        PIG(90, EntityType.ENTITY_AGEABLE), 
        SHEEP(91, EntityType.ENTITY_AGEABLE), 
        COW(92, EntityType.ENTITY_AGEABLE), 
        CHICKEN(93, EntityType.ENTITY_AGEABLE), 
        SQUID(94, EntityType.ENTITY_INSENTIENT), 
        WOLF(95, EntityType.ENTITY_TAMEABLE_ANIMAL), 
        MUSHROOM_COW(96, EntityType.COW), 
        SNOWMAN(97, EntityType.IRON_GOLEM), 
        OCELOT(98, EntityType.ENTITY_TAMEABLE_ANIMAL), 
        ABSTRACT_HORSE(-1, EntityType.ENTITY_AGEABLE), 
        HORSE(100, EntityType.ABSTRACT_HORSE), 
        SKELETON_HORSE(28, EntityType.ABSTRACT_HORSE), 
        ZOMBIE_HORSE(29, EntityType.ABSTRACT_HORSE), 
        CHESTED_HORSE(-1, EntityType.ABSTRACT_HORSE), 
        DONKEY(31, EntityType.CHESTED_HORSE), 
        MULE(32, EntityType.CHESTED_HORSE), 
        LIAMA(103, EntityType.CHESTED_HORSE), 
        RABBIT(101, EntityType.ENTITY_AGEABLE), 
        POLAR_BEAR(102, EntityType.ENTITY_AGEABLE), 
        VILLAGER(120, EntityType.ENTITY_AGEABLE), 
        ENDER_CRYSTAL(200, EntityType.ENTITY), 
        SPLASH_POTION(-1, EntityType.ENTITY), 
        LINGERING_POTION(-1, EntityType.SPLASH_POTION), 
        AREA_EFFECT_CLOUD(-1, EntityType.ENTITY), 
        EGG(-1, EntityType.ENTITY), 
        FISHING_HOOK(-1, EntityType.ENTITY), 
        LIGHTNING(-1, EntityType.ENTITY), 
        WEATHER(-1, EntityType.ENTITY), 
        PLAYER(-1, EntityType.ENTITY_HUMAN), 
        COMPLEX_PART(-1, EntityType.ENTITY), 
        LIAMA_SPIT(-1, EntityType.ENTITY);
        
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
        ITEM(2, EntityType.DROPPED_ITEM), 
        AREA_EFFECT_CLOUD(3, EntityType.AREA_EFFECT_CLOUD), 
        MINECART(10, EntityType.MINECART_RIDEABLE), 
        TNT_PRIMED(50, EntityType.PRIMED_TNT), 
        ENDER_CRYSTAL(51, EntityType.ENDER_CRYSTAL), 
        TIPPED_ARROW(60, EntityType.ARROW), 
        SNOWBALL(61, EntityType.SNOWBALL), 
        EGG(62, EntityType.EGG), 
        FIREBALL(63, EntityType.FIREBALL), 
        SMALL_FIREBALL(64, EntityType.SMALL_FIREBALL), 
        ENDER_PEARL(65, EntityType.ENDER_PEARL), 
        WITHER_SKULL(66, EntityType.WITHER_SKULL), 
        SHULKER_BULLET(67, EntityType.SHULKER_BULLET), 
        LIAMA_SPIT(68, EntityType.LIAMA_SPIT), 
        FALLING_BLOCK(70, EntityType.FALLING_BLOCK), 
        ITEM_FRAME(71, EntityType.ITEM_FRAME), 
        ENDER_SIGNAL(72, EntityType.ENDER_SIGNAL), 
        POTION(73, EntityType.SPLASH_POTION), 
        THROWN_EXP_BOTTLE(75, EntityType.THROWN_EXP_BOTTLE), 
        FIREWORK(76, EntityType.FIREWORK), 
        LEASH(77, EntityType.LEASH_HITCH), 
        ARMOR_STAND(78, EntityType.ARMOR_STAND), 
        EVOCATION_FANGS(79, EntityType.EVOCATION_FANGS), 
        FISHIHNG_HOOK(90, EntityType.FISHING_HOOK), 
        SPECTRAL_ARROW(91, EntityType.SPECTRAL_ARROW), 
        DRAGON_FIREBALL(93, EntityType.DRAGON_FIREBALL);
        
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
        
        static {
            TYPES = new HashMap<Integer, ObjectType>();
            for (final ObjectType type : values()) {
                ObjectType.TYPES.put(type.id, type);
            }
        }
    }
}
