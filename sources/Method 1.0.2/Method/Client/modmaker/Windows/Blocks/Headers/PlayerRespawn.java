package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerRespawn extends Block {
    public PlayerRespawn() {
        super( "PlayerRespawn", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.PlayerRespawnEvent);
        this.description = "Called when a player respawns";
    }

    public static class Playerawn extends Block {
        public Playerawn() {
            super( "Respawn/Player", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.PlayerRespawnEvent);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((PlayerEvent.PlayerRespawnEvent)event).player.getEntityId();
        }
    }

    public static class End extends Block {
        public End() {
            super( "Respawn/End", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.PlayerRespawnEvent);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((PlayerEvent.PlayerRespawnEvent)event).isEndConquered();
        }
    }
}
