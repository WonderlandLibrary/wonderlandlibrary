package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;

public class pSPacketUpdateTileEntity extends Block {
    public pSPacketUpdateTileEntity() {
        super( "UpdateTileEntity", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketUpdateTileEntity);
        this.packetclasses.add(SPacketUpdateTileEntity.class);
    }

    public static class getPosity extends Block {
        public getPosity() {
            super( "getPosity", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.SPacketUpdateTileEntity);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketUpdateTileEntity) event).getPos();
        }
    }

    public static class getTileEntityType extends Block {
        public getTileEntityType() {
            super( "getTileEntityType", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketUpdateTileEntity);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketUpdateTileEntity) event).getTileEntityType();
        }
    }

    public static class setPos extends Block {
        public setPos() {
            super( "setPos", MainBlockType.Default, Tabs.Sub, Headers.SPacketUpdateTileEntity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUpdateTileEntity) event).blockPos = (BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


    public static class setTileEntityType extends Block {
        public setTileEntityType() {
            super( "setTileEntityType", MainBlockType.Default, Tabs.Sub, Headers.SPacketUpdateTileEntity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUpdateTileEntity) event).tileEntityType = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
