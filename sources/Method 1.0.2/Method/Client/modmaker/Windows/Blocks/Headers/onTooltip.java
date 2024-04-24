package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class onTooltip extends Block {
    public onTooltip() {
        super( "onTooltip", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.ItemTooltipEvent);
        this.description = "Called when the player is hovering over an item.";
    }

    public static class getItemStack extends Block {
        public getItemStack() {
            super( "getItemStack", MainBlockType.Items, Tabs.Sub, BlockObjects.Name, Headers.ItemTooltipEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((ItemTooltipEvent)event).getItemStack();
        }
    }

}
