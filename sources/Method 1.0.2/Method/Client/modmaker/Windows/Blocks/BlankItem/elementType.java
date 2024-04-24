package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class elementType extends Block {

    public elementType() {
        super("elementType", MainBlockType.ElementType, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (RenderGameOverlayEvent.ElementType value : RenderGameOverlayEvent.ElementType.values()) {
            this.ddOptions.add(value.toString());
        }
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return RenderGameOverlayEvent.ElementType.valueOf(dragableBlock.dropDowns.getSelected());
    }


}
