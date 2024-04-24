package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.event.entity.living.LivingEvent;

public class LivingUpdate extends Block {
    public LivingUpdate() {
        super( "LivingUpdate", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.LivingUpdateEvent);
        this.description = "Called when a living entity is updated";
    }

    public static class Entityate extends Block {
        public Entityate() {
            super( "Entityate", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.LivingUpdateEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((LivingEvent.LivingUpdateEvent)event).getEntityLiving();
        }
    }


}
