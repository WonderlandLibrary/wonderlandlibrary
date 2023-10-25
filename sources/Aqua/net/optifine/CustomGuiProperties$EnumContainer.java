package net.optifine;

public static enum CustomGuiProperties.EnumContainer {
    ANVIL,
    BEACON,
    BREWING_STAND,
    CHEST,
    CRAFTING,
    DISPENSER,
    ENCHANTMENT,
    FURNACE,
    HOPPER,
    HORSE,
    VILLAGER,
    SHULKER_BOX,
    CREATIVE,
    INVENTORY;

    public static final CustomGuiProperties.EnumContainer[] VALUES;

    static {
        VALUES = CustomGuiProperties.EnumContainer.values();
    }
}
