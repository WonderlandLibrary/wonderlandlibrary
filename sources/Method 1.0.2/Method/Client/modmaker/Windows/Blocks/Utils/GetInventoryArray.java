package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class GetInventoryArray extends Block {

    public GetInventoryArray() {
        super("GetInventoryArray", MainBlockType.Array, Tabs.Utils, BlockObjects.Name, BlockObjects.Words, BlockObjects.BooleanTextEnter,
                BlockObjects.Words, BlockObjects.BooleanTextEnter,
                BlockObjects.Words, BlockObjects.BooleanTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));

        this.words[0] = "Hotbar";
        this.words[1] = "Inventory";
        this.words[2] = "Offhand";


        this.description = "Returns Array of Itemstacks from Inventory or Hotbar";

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        ArrayList<ItemStack> bloc = new ArrayList<>();
        boolean inv = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
        boolean hotbar = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
        boolean offhand = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 2, event);
        if (inv) {
            Minecraft.getMinecraft().player.inventory.mainInventory.subList(9, 36).forEach(itemStack -> {
                if (itemStack != null && itemStack.getItem() != Items.AIR) {
                    bloc.add(itemStack);
                }
            });
        }

        if (hotbar) {
            Minecraft.getMinecraft().player.inventory.mainInventory.subList(0, 9).forEach(itemStack -> {
                if (itemStack != null && itemStack.getItem() != Items.AIR) {
                    bloc.add(itemStack);
                }
            });
        }

        if (offhand) {
            ItemStack offhandStack = Minecraft.getMinecraft().player.getHeldItemOffhand();
            if (offhandStack.getItem() != Items.AIR) {
                bloc.add(offhandStack);
            }
        }


        return bloc;
    }


}
