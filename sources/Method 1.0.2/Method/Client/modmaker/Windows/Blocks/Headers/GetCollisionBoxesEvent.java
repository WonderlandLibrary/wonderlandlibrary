package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class GetCollisionBoxesEvent extends Block {
    public GetCollisionBoxesEvent() {
        super( "GetCollisionBoxesEvent", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.GetCollisionBoxesEvent);
        this.description = "GetCollisionBoxesEvent";
    }

    public static class Entity extends Block {
        public Entity() {
            super( "Entity", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.GetCollisionBoxesEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((net.minecraftforge.event.world.GetCollisionBoxesEvent)event).getEntity();
        }
    }

    public static class EndConquered extends Block {
        public EndConquered() {
            super( "EndConquered", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.GetCollisionBoxesEvent);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((PlayerEvent.PlayerRespawnEvent)event).isEndConquered();
        }
    }
}
