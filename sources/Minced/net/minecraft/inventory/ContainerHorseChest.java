// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.util.text.ITextComponent;

public class ContainerHorseChest extends InventoryBasic
{
    public ContainerHorseChest(final String inventoryTitle, final int slotCount) {
        super(inventoryTitle, false, slotCount);
    }
    
    public ContainerHorseChest(final ITextComponent inventoryTitle, final int slotCount) {
        super(inventoryTitle, slotCount);
    }
}
