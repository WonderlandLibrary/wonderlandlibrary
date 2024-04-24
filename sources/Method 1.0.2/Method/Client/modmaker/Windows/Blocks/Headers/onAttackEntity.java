package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class onAttackEntity extends Block {
    public onAttackEntity() {
        super( "onAttackEntity", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.AttackEntityEvent);
        this.description = "Called when the player attacks an entity";
    }

    public static class getEntity extends Block {
        public getEntity() {
            super( "getEntity", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.AttackEntityEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((AttackEntityEvent)event).getEntity();
        }
    }

    public static class getTarget extends Block {
        public getTarget() {
            super( "getTarget", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.AttackEntityEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((AttackEntityEvent)event).getTarget();
        }
    }
}
