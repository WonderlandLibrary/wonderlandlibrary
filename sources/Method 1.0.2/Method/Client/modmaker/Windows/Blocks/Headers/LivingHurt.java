package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class LivingHurt extends Block {
    public LivingHurt() {
        super("LivingHurt", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.LivingHurt);
        this.description = "Called when player is hurt";
    }

    public static class getDmgamount extends Block {
        public getDmgamount() {
            super("getDmgamount", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.LivingHurt);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((LivingHurtEvent) event).getAmount();
        }
    }

    public static class getDmgsource extends Block {
        public getDmgsource() {
            super("getDmgsource", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.LivingHurt);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((LivingHurtEvent) event).getSource().damageType;
        }
    }

    public static class getDmgsourceMob extends Block {
        public getDmgsourceMob() {
            super("getDmgsourceMob", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.LivingHurt);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((LivingHurtEvent) event).getSource().getImmediateSource();
        }
    }


}
