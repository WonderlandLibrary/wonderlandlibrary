package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.EntityLivingBase;

public class EntityHandItem extends Block {

    public EntityHandItem() {
        super("EntityHandItem", MainBlockType.ItemStack, Tabs.Entity, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);

        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        ddOptions.add("MainHand");
        ddOptions.add("OffHand");
        this.description = "Returns Itemstack held by Entity " + "\n" + "EntityLivingBase.getHeldItemMainhand()" + "\n" + " or EntityLivingBase.getHeldItemOffhand()";

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        if (dragableBlock.dropDowns.getSelected().equals("MainHand"))
            return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getHeldItemMainhand();
        else
            return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getHeldItemOffhand();

    }


}
