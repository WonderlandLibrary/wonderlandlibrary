package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FindItemInSlot extends Block {

    public FindItemInSlot() {
        super("FindItemInSlot", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.Items, MainBlockType.ItemStack));

        ddOptions.add("Hotbar");
        ddOptions.add("Inventory");
        ddOptions.add("Hotbar+inv");

        this.description = "Finds first Item in slot, returns -1 if not found"
                + "\n Hotbar = 0-9, Inventory = 9-36, Hotbar+inv = 0-36";

    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        Item i = null;
        ItemStack istack = null;
        if (dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof Item)
            i = (Item) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
        else if (dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof ItemStack)
            istack = (ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);


        switch (dragableBlock.dropDowns.getSelected()) {
            case "Hotbar":
                return finditem(i, istack, 0, 9);
            case "Inventory":
                return finditem(i, istack, 9, 36);
            case "Hotbar+inv":
                return finditem(i, istack, 0, 36);
        }
        return -1;
    }


    private int finditem(Item item, ItemStack istack, int start, int end) {
        for (int i = start; i < end; i++) {
            final ItemStack stack = Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
            if (item != null)
                if (stack.getItem() == item)
                    return i;
            if (istack != null)
                if (stack == istack)
                    return i;

        }
        return -1;
    }

}
