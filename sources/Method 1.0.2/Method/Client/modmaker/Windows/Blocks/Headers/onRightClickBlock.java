package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class onRightClickBlock extends Block {
    public onRightClickBlock() {
        super( "onRightClickBlock", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.onRightClickBlock);
        this.description = "Called when the player right clicks a block";
    }

    public static class onRPlayer extends Block {
        public onRPlayer() {
            super( "onRPlayer", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.onRightClickBlock);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerInteractEvent.RightClickBlock)event).getEntityPlayer();
        }
    }

    public static class hitVecock extends Block {
        public hitVecock() {
            super( "hitVecock", MainBlockType.Vec3d, Tabs.Sub, BlockObjects.Name, Headers.onRightClickBlock);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerInteractEvent.RightClickBlock)event).getHitVec();
        }
    }
    public static class getFaceock extends Block {
        public getFaceock() {
            super( "getFaceock", MainBlockType.Facing, Tabs.Sub, BlockObjects.Name, Headers.onRightClickBlock);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerInteractEvent.RightClickBlock)event).getFace();
        }
    }
    public static class getHandock extends Block {
        public getHandock() {
            super( "getHandock", MainBlockType.EnumHand, Tabs.Sub, BlockObjects.Name, Headers.onRightClickBlock);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerInteractEvent.RightClickBlock)event).getHand();
        }
    }
}
