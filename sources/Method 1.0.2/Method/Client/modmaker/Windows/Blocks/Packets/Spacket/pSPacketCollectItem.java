package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketCollectItem;

public class pSPacketCollectItem extends Block {
    public pSPacketCollectItem() {
        super( "CollectItem", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketCollectItem);
        this.packetclasses.add(SPacketCollectItem.class);
    }

    public static class getCollectedItemEntityID extends Block {
        public getCollectedItemEntityID() {
            super( "getCollectedItemEntityID", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketCollectItem);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((SPacketCollectItem) event).getCollectedItemEntityID();

        }
    }
    public static class getEntityIDtem extends Block {
        public getEntityIDtem() {
            super( "getEntityIDtem", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketCollectItem);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((SPacketCollectItem) event).getEntityID();

        }
    }
    public static class getAmount extends Block {
        public getAmount() {
            super( "getAmount", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketCollectItem);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((SPacketCollectItem) event).getAmount();

        }
    }


    public static class setcollectedItemEntityId extends Block {
        public setcollectedItemEntityId() {
            super( "setcollectedItemEntityId", MainBlockType.Default, Tabs.Sub,Headers.SPacketCollectItem, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCollectItem) event).collectedItemEntityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setentityIdtem extends Block {
        public setentityIdtem() {
            super( "setentityIdtem", MainBlockType.Default, Tabs.Sub,Headers.SPacketCollectItem, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCollectItem) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setcollectedQuantity extends Block {
        public setcollectedQuantity() {
            super( "setcollectedQuantity", MainBlockType.Default, Tabs.Sub,Headers.SPacketCollectItem, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCollectItem) event).collectedQuantity = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
