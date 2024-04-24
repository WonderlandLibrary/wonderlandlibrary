package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class EntityArmorList extends Block {

    public EntityArmorList() {
        super("EntityArmorList", MainBlockType.Array, Tabs.Entity, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns Array of armor on entity (Entity Living)" + "\n" + "EntityLiving.getArmorInventoryList()";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        ArrayList<ItemStack> objects = new ArrayList<>();
        for (ItemStack stack : ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getArmorInventoryList()) {
            objects.add(stack);
        }
        return objects;
    }


}
