package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventCancel extends Block {

    public EventCancel() {
        super("EventCancel", MainBlockType.Default, Tabs.Utils, BlockObjects.Name);
        this.description = "Cancels the current event.";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ((Event)event).setCanceled(true);
      //  super.runCode(dragableBlock, event);
    }

}
