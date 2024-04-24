package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;

public class GetEntityName extends Block {

    public GetEntityName() {
        super("GetEntityName", MainBlockType.String, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        ddOptions.add("getName");
        ddOptions.add("getDisplayName");
        ddOptions.add("gameProfileName");
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns Name of entity" + "\n" + "(Entity).getName() + (Entity).getDisplayName() + (Entity).gameProfileName";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "getName":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getName();
            case "getDisplayName": {
                if (dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof EntityItem)
                    return ((EntityItem) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getItem().getDisplayName();
                else
                    return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getDisplayName();
            }
            case "gameProfileName":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getGameProfile().getName();


        }
        return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getName();
    }

}
