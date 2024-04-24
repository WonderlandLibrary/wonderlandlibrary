package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

public class pCPacketPlayerTryUseItem extends Block {
    public pCPacketPlayerTryUseItem() {
        super( "PlayerTryUseItem", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketPlayerTryUseItem);
        this.packetclasses.add(CPacketPlayerTryUseItem.class);
        this.description = "Sends a packet to the server to use an item.";
    }

    public static class getHandtem extends Block {
        public getHandtem() {
            super( "getHandtem", MainBlockType.EnumHand, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerTryUseItem);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerTryUseItem) event).getHand();

        }
    }
    public static class SendTryUseItem extends Block {
        public SendTryUseItem() {
            super( "SendTryUseItem", MainBlockType.Default, Tabs.Sub, Headers.CPacketPlayerTryUseItem, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send TryUseItem EnumHand:";
            this.typesAccepted.add(typesCollapse(MainBlockType.EnumHand));

        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // EnumHand enumHand
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayerTryUseItem((EnumHand) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
