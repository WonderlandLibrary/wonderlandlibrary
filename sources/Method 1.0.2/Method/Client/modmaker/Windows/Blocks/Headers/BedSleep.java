package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

public class BedSleep extends Block {
    public BedSleep() {
        super( "BedSleep", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.PlayerSleepInBedEvent);
        this.description = "PlayerSleepInBedEvent";
    }

    public static class PlayerinBed extends Block {
        public PlayerinBed() {
            super( "Player", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.PlayerSleepInBedEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerSleepInBedEvent) event).getEntityPlayer();
        }
    }

    public static class getPos extends Block {
        public getPos() {
            super( "getPos", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.PlayerSleepInBedEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerSleepInBedEvent) event).getPos();
        }
    }


}
