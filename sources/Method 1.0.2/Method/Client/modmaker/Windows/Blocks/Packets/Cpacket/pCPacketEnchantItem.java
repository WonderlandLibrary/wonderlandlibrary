package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketEnchantItem;

public class pCPacketEnchantItem extends Block {
    public pCPacketEnchantItem() {
        super( "EnchantItem", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketEnchantItem);
        this.packetclasses.add(CPacketEnchantItem.class);
        this.description = "Sends a packet to the server to enchant an item";
    }

    public static class getWindowIdtem extends Block {
        public getWindowIdtem() {
            super( "getWindowIdtem", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketEnchantItem);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketEnchantItem) event).getWindowId();

        }
    }

    public static class getButton extends Block {
        public getButton() {
            super( "getButton", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketEnchantItem);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketEnchantItem) event).getButton();

        }
    }

    public static class SendEnchantItem extends Block {
        public SendEnchantItem() {
            super( "SendEnchantItem", MainBlockType.Default, Tabs.Sub, Headers.CPacketEnchantItem, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send EnchantItem windowId:";
            this.words[1] = "buttonIn:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // int windowIdIn, int buttonIn
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketEnchantItem((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event), (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
