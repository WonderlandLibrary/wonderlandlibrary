package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;

public class GetStackFromSlot extends Block {

    public GetStackFromSlot() {
        super("GetStackFromSlot", MainBlockType.ItemStack, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);

        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        for (EntityEquipmentSlot value : EntityEquipmentSlot.values()) {
            ddOptions.add(value.toString());
        }
        this.description = "Given a Player returns Itemstack from Slot   " + "\n" + "(EntityPlayer).getItemStackFromSlot"

                + "\n" +
                "Chest, Feet, Head, Legs, MainHand, OffHand";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "CHEST":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            case "FEET":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getItemStackFromSlot(EntityEquipmentSlot.FEET);
            case "HEAD":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            case "LEGS":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getItemStackFromSlot(EntityEquipmentSlot.LEGS);
            case "MAINHAND":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
            case "OFFHAND":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
        }

        return null;
    }

}
