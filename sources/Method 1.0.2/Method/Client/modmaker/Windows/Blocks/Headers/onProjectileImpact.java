package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.event.entity.ProjectileImpactEvent;

public class onProjectileImpact extends Block {
    public onProjectileImpact() {
        super( "onProjectileImpact", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.ProjectileImpactEvent);
        this.description = "Called when a projectile hits a entity.";
    }

    public static class Entityact extends Block {
        public Entityact() {
            super( "Entityact", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.ProjectileImpactEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((ProjectileImpactEvent)event).getEntity();
        }
    }


}
