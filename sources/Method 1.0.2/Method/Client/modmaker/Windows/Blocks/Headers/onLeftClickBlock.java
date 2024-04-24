package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class onLeftClickBlock extends Block {
    public onLeftClickBlock() {
        super( "onLeftClickBlock", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.LeftClickBlock);
        this.description = "Called when the player left clicks a block";
    }

    public static class Playerock extends Block {
        public Playerock() {
            super( "Playerock", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.LeftClickBlock);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerInteractEvent.LeftClickBlock)event).getEntityPlayer();
        }
    }
    public static class hitVec extends Block {
        public hitVec() {
            super( "hitVec", MainBlockType.Vec3d, Tabs.Sub, BlockObjects.Name, Headers.LeftClickBlock);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerInteractEvent.LeftClickBlock)event).getHitVec();
        }
    }

    public static class GetposLc extends Block {
        public GetposLc() {
            super( "GetposLc", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.LeftClickBlock);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerInteractEvent.LeftClickBlock)event).getPos();
        }
    }
    public static class getFace extends Block {
        public getFace() {
            super( "getFace", MainBlockType.Facing, Tabs.Sub, BlockObjects.Name, Headers.LeftClickBlock);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerInteractEvent.LeftClickBlock)event).getFace();
        }
    }
    public static class getHand extends Block {
        public getHand() {
            super( "getHand", MainBlockType.EnumHand, Tabs.Sub, BlockObjects.Name, Headers.LeftClickBlock);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerInteractEvent.LeftClickBlock)event).getHand();
        }
    }
}
