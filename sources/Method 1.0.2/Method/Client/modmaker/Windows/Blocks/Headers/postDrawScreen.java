package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.client.event.GuiScreenEvent;

public class postDrawScreen extends Block {
    public postDrawScreen() {
        super( "postDrawScreen", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.postDrawScreen);
        this.description = "Called after the screen is drawn.";
    }

    public static class getGuieen extends Block {
        public getGuieen() {
            super( "getGuieen", MainBlockType.Gui, Tabs.Sub, BlockObjects.Name, Headers.postDrawScreen);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((GuiScreenEvent.DrawScreenEvent.Post)event).getGui();
        }
    }

}
