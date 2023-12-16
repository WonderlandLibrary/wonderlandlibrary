package net.minecraft.inventory;

public enum EquipmentSlotType
{
    MAINHAND(Group.HAND, 0, 0, "mainhand"),
    OFFHAND(Group.HAND, 1, 5, "offhand"),
    FEET(Group.ARMOR, 0, 1, "feet"),
    LEGS(Group.ARMOR, 1, 2, "legs"),
    CHEST(Group.ARMOR, 2, 3, "chest"),
    HEAD(Group.ARMOR, 3, 4, "head");

    private final Group slotType;
    private final int index;
    private final int slotIndex;
    private final String name;

    private EquipmentSlotType(Group slotTypeIn, int indexIn, int slotIndexIn, String nameIn)
    {
        this.slotType = slotTypeIn;
        this.index = indexIn;
        this.slotIndex = slotIndexIn;
        this.name = nameIn;
    }

    public Group getSlotType()
    {
        return this.slotType;
    }

    public int getIndex()
    {
        return this.index;
    }

    /**
     * Gets the actual slot index.
     */
    public int getSlotIndex()
    {
        return this.slotIndex;
    }

    public String getName()
    {
        return this.name;
    }

    public static EquipmentSlotType fromString(String targetName)
    {
        for (EquipmentSlotType equipmentslottype : values())
        {
            if (equipmentslottype.getName().equals(targetName))
            {
                return equipmentslottype;
            }
        }

        throw new IllegalArgumentException("Invalid slot '" + targetName + "'");
    }

    /**
     * Returns the slot type based on the slot group and the index inside of that group.
     */
    public static EquipmentSlotType fromSlotTypeAndIndex(Group slotTypeIn, int slotIndexIn)
    {
        for (EquipmentSlotType equipmentslottype : values())
        {
            if (equipmentslottype.getSlotType() == slotTypeIn && equipmentslottype.getIndex() == slotIndexIn)
            {
                return equipmentslottype;
            }
        }

        throw new IllegalArgumentException("Invalid slot '" + slotTypeIn + "': " + slotIndexIn);
    }

    public static enum Group {
        HAND,
        ARMOR;
    }
}
