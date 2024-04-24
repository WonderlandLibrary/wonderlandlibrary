package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.client.event.RenderLivingEvent;

public class renderNamePlate extends Block {
    public renderNamePlate() {
        super( "renderNamePlate", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.RenderLivingEvent);
        this.description = "Called to render the nameplate of a living entity.";
    }

    public static class Playerate extends Block {
        public Playerate() {
            super( "Playerate", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.RenderLivingEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((RenderLivingEvent.Specials.Pre)event).getEntity();
        }
    }

}
