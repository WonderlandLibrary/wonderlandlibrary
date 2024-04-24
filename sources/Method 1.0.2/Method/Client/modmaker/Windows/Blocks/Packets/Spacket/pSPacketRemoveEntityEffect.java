package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.potion.Potion;

public class pSPacketRemoveEntityEffect extends Block {
    public pSPacketRemoveEntityEffect() {
        super( "RemoveEntityEffect", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketRemoveEntityEffect);
        this.packetclasses.add(SPacketRemoveEntityEffect.class);
    }

    public static class getEntityect extends Block {
        public getEntityect() {
            super( "getEntityect", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.SPacketRemoveEntityEffect);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketRemoveEntityEffect) event).getEntity(Minecraft.getMinecraft().world);
        }
    }

    public static class getPotion extends Block {
        public getPotion() {
            super( "getPotion", MainBlockType.Potion, Tabs.Sub, BlockObjects.Name, Headers.SPacketRemoveEntityEffect);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketRemoveEntityEffect) event).getPotion();
        }
    }

    public static class setentityIdect extends Block {
        public setentityIdect() {
            super( "setentityIdect", MainBlockType.Default, Tabs.Sub, Headers.SPacketRemoveEntityEffect, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketRemoveEntityEffect) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class seteffectId extends Block {
        public seteffectId() {
            super( "seteffectId", MainBlockType.Default, Tabs.Sub, Headers.SPacketRemoveEntityEffect, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Potion));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketRemoveEntityEffect) event).effectId = (Potion) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


}
