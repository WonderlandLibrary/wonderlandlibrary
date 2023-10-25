package net.minecraft.entity.item;

import com.google.common.collect.Maps;
import java.util.Map;

public static enum EntityMinecart.EnumMinecartType {
    RIDEABLE(0, "MinecartRideable"),
    CHEST(1, "MinecartChest"),
    FURNACE(2, "MinecartFurnace"),
    TNT(3, "MinecartTNT"),
    SPAWNER(4, "MinecartSpawner"),
    HOPPER(5, "MinecartHopper"),
    COMMAND_BLOCK(6, "MinecartCommandBlock");

    private static final Map<Integer, EntityMinecart.EnumMinecartType> ID_LOOKUP;
    private final int networkID;
    private final String name;

    private EntityMinecart.EnumMinecartType(int networkID, String name) {
        this.networkID = networkID;
        this.name = name;
    }

    public int getNetworkID() {
        return this.networkID;
    }

    public String getName() {
        return this.name;
    }

    public static EntityMinecart.EnumMinecartType byNetworkID(int id) {
        EntityMinecart.EnumMinecartType entityminecart$enumminecarttype = (EntityMinecart.EnumMinecartType)((Object)ID_LOOKUP.get((Object)id));
        return entityminecart$enumminecarttype == null ? RIDEABLE : entityminecart$enumminecarttype;
    }

    static {
        ID_LOOKUP = Maps.newHashMap();
        for (EntityMinecart.EnumMinecartType entityminecart$enumminecarttype : EntityMinecart.EnumMinecartType.values()) {
            ID_LOOKUP.put((Object)entityminecart$enumminecarttype.getNetworkID(), (Object)entityminecart$enumminecarttype);
        }
    }
}
