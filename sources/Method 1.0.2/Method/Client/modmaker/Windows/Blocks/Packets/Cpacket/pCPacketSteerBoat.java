package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketSteerBoat;

public class pCPacketSteerBoat extends Block {
    public pCPacketSteerBoat() {
        super( "SteerBoat", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketSteerBoat);
        this.packetclasses.add(CPacketSteerBoat.class);
        this.description = "Sends a steer boat packet";
    }

    public static class getLeftoat extends Block {
        public getLeftoat() {
            super( "getLeftoat", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.CPacketSteerBoat);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((CPacketSteerBoat) event).getLeft();
        }
    }

    public static class getRightoat extends Block {
        public getRightoat() {
            super( "getRightoat", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.CPacketSteerBoat);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((CPacketSteerBoat) event).getRight();
        }
    }

    public static class SendSteerBoat extends Block {
        public SendSteerBoat() {
            super( "SendSteerBoat", MainBlockType.Default, Tabs.Sub, Headers.CPacketSteerBoat, BlockObjects.Words, BlockObjects.BooleanTextEnter,
                    BlockObjects.Words, BlockObjects.BooleanTextEnter);
            this.words[0] = "Send SteerBoat left:";
            this.words[1] = "right:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));

        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // boolean left, boolean right
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketSteerBoat(dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event)));
            super.runCode(dragableBlock, event);
        }
    }

}
