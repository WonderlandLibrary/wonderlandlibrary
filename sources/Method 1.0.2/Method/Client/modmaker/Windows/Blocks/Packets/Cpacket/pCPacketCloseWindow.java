package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketCloseWindow;

public class pCPacketCloseWindow extends Block {
    public pCPacketCloseWindow() {
        super( "ClientCloseWindow", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketCloseWindow);
        this.packetclasses.add(CPacketCloseWindow.class);
        this.description = "Sends a close window packet";
    }

    public static class SendCPacketCloseWindow extends Block {
        public SendCPacketCloseWindow() {
            super( "SendCPacketCloseWindow", MainBlockType.Default, Tabs.Sub, Headers.CPacketCloseWindow, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send PacketCloseWindow windowId:";

            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            //  int windowId,
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCloseWindow((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
