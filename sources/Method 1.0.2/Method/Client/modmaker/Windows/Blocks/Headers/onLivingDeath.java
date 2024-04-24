package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class onLivingDeath extends Block {
    public onLivingDeath() {
        super( "onLivingDeath", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.LivingDeathEvent);
        this.description = "Called when a living entity dies";
    }

    public static class Playerath extends Block {
        public Playerath() {
            super("Playerath", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.LivingDeathEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((LivingDeathEvent)event).getEntity();
        }
    }


}
