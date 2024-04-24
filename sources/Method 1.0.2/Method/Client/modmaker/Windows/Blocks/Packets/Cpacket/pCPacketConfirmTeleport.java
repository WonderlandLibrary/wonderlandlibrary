package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketConfirmTeleport;

public class pCPacketConfirmTeleport extends Block {
    public pCPacketConfirmTeleport() {
        super( "ConfirmTeleport", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketConfirmTeleport);
        this.packetclasses.add(CPacketConfirmTeleport.class);
        this.description = "Sends a confirm teleport packet to the server.";
    }

    public static class getTeleportId extends Block {
        public getTeleportId() {
            super( "getTeleportId", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketConfirmTeleport);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event)  {
            return ((CPacketConfirmTeleport) event).getTeleportId();

        }
    }
    public static class SendCPacketConfirmTeleport extends Block {
        public SendCPacketConfirmTeleport() {
            super( "SendCPacketConfirmTeleport", MainBlockType.Default, Tabs.Sub, Headers.CPacketConfirmTeleport, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send ConfirmTeleport teleportId:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // int  teleportIdIn,
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketConfirmTeleport( (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
