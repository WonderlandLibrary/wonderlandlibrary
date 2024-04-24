package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.managers.Hole;
import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class HoleType extends Block {
    public HoleType() {
        super( "HoleType", MainBlockType.HoleType, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (Hole.HoleType value : Hole.HoleType.values()) {
            this.ddOptions.add(value.toString());
        }
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return Hole.HoleType.valueOf(dragableBlock.dropDowns.getSelected());
    }
}
