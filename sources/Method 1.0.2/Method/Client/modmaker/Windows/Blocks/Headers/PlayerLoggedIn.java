package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerLoggedIn extends Block {
    public PlayerLoggedIn() {
        super( "PlayerLoggedIn", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.PlayerLoggedInEvent);
        this.description = "Called when a player logs in";
    }

    public static class PlayerdIn extends Block {
        public PlayerdIn() {
            super( "PlayerdIn", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.PlayerLoggedInEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerEvent.PlayerLoggedInEvent)event).player;
        }
    }


}
