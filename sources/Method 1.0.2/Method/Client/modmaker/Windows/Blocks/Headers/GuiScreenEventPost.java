package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.client.event.GuiScreenEvent;

public class GuiScreenEventPost extends Block {
    public GuiScreenEventPost() {
        super( "GuiScreenEventPost", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.GuiScreenEventPost);
        this.description = "Calls every time the GUI is posted";
    }

    public static class getGuiost extends Block {
        public getGuiost() {
            super( "getGuiost", MainBlockType.Gui, Tabs.Sub, BlockObjects.Name, Headers.GuiScreenEventPost);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((GuiScreenEvent.ActionPerformedEvent.Post)event).getGui();
        }
    }

}
