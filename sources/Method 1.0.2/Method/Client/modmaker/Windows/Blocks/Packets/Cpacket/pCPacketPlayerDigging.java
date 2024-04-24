package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class pCPacketPlayerDigging extends Block {
    public pCPacketPlayerDigging() {
        super( "PacketPlayerDigging", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketPlayerDigging);
        this.packetclasses.add(CPacketPlayerDigging.class);
        this.description = "Sends a player digging packet to the server.";
    }

    public static class getActioning extends Block {
        public getActioning() {
            super( "getActioning", MainBlockType.PlayerDigging, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerDigging);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerDigging) event).getAction();

        }
    }

    public static class getFacing extends Block {
        public getFacing() {
            super( "getFacing", MainBlockType.Facing, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerDigging);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerDigging) event).getFacing();

        }
    }

    public static class getPosition extends Block {
        public getPosition() {
            super( "getPosition", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerDigging);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerDigging) event).getPosition();

        }
    }

    public static class SendPlayerDigging extends Block {
        public SendPlayerDigging() {
            super( "SendPlayerDigging", MainBlockType.Default, Tabs.Sub, Headers.CPacketPlayerDigging, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send PlayerDigging Action:";
            this.words[1] = "BlockPos:";
            this.words[2] = "Facing:";

            this.typesAccepted.add(typesCollapse(MainBlockType.PlayerDigging));
            this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
            this.typesAccepted.add(typesCollapse(MainBlockType.Facing));

        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // CPacketPlayerDigging.Action action, BlockPos blockPos, EnumFacing enumFacing
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayerDigging((CPacketPlayerDigging.Action) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event), (BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event), (EnumFacing) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
