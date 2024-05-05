package net.minecraft.block;

import net.minecraft.util.IStringSerializable;

public static enum BlockDoublePlant.EnumPlantType implements IStringSerializable
{
    SUNFLOWER(0, "sunflower"),
    SYRINGA(1, "syringa"),
    GRASS(2, "double_grass", "grass"),
    FERN(3, "double_fern", "fern"),
    ROSE(4, "double_rose", "rose"),
    PAEONIA(5, "paeonia");

    private static final BlockDoublePlant.EnumPlantType[] META_LOOKUP;
    private final int meta;
    private final String name;
    private final String unlocalizedName;

    private BlockDoublePlant.EnumPlantType(int meta, String name) {
        this(meta, name, name);
    }

    private BlockDoublePlant.EnumPlantType(int meta, String name, String unlocalizedName) {
        this.meta = meta;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
    }

    public int getMeta() {
        return this.meta;
    }

    public String toString() {
        return this.name;
    }

    public static BlockDoublePlant.EnumPlantType byMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }
        return META_LOOKUP[meta];
    }

    public String getName() {
        return this.name;
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    static {
        META_LOOKUP = new BlockDoublePlant.EnumPlantType[BlockDoublePlant.EnumPlantType.values().length];
        BlockDoublePlant.EnumPlantType[] enumPlantTypeArray = BlockDoublePlant.EnumPlantType.values();
        int n = enumPlantTypeArray.length;
        for (int i = 0; i < n; ++i) {
            BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype;
            BlockDoublePlant.EnumPlantType.META_LOOKUP[blockdoubleplant$enumplanttype.getMeta()] = blockdoubleplant$enumplanttype = enumPlantTypeArray[i];
        }
    }
}
