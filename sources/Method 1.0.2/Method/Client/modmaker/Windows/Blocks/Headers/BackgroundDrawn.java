package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.client.event.GuiScreenEvent;

public class BackgroundDrawn extends Block {
    public BackgroundDrawn() {
        super( "BackgroundDrawn", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.BackgroundDrawnEvent);
        this.description = "GuiScreenEvent.BackgroundDrawnEvent";
    }

    public static class Gui extends Block {
        public Gui() {
            super( "Gui", MainBlockType.Gui, Tabs.Sub, BlockObjects.Name, Headers.BackgroundDrawnEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((GuiScreenEvent.BackgroundDrawnEvent) event).getGui();
        }
    }

}
