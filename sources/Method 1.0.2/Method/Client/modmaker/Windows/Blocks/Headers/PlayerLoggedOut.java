package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerLoggedOut extends Block {
    public PlayerLoggedOut() {
        super( "PlayerLoggedOut", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.PlayerLoggedOutEvent);
        this.description = "Called when a player logs out";
    }

    public static class Playeroutd extends Block {
        public Playeroutd() {
            super( "Playeroutd", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.PlayerLoggedOutEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerEvent.PlayerLoggedOutEvent)event).player;
        }
    }


}
