package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class onItemPickup extends Block {
    public onItemPickup() {
        super( "onItemPickup", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.EntityItemPickupEvent);
        this.description = "Called when an item is picked up";
    }

    public static class getItem extends Block {
        public getItem() {
            super( "getItem", MainBlockType.Items, Tabs.Sub, BlockObjects.Name, Headers.EntityItemPickupEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((EntityItemPickupEvent)event).getItem();
        }
    }

    public static class Playerkup extends Block {
        public Playerkup() {
            super( "Playerkup", MainBlockType.Items, Tabs.Sub, BlockObjects.Name, Headers.EntityItemPickupEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((EntityItemPickupEvent)event).getEntityLiving();
        }
    }
}
