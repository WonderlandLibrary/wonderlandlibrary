package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.client.event.GuiScreenEvent;

public class GuiScreenEventInit extends Block {
    public GuiScreenEventInit() {
        super( "GuiScreenEventInit", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.GuiScreenEventInit);
        this.description = "Calls every time the GUI is initialized";
    }

    public static class getGuinit extends Block {
        public getGuinit() {
            super( "getGuinit", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.GuiScreenEventInit);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((GuiScreenEvent.InitGuiEvent.Post)event).getGui();
        }
    }

}
