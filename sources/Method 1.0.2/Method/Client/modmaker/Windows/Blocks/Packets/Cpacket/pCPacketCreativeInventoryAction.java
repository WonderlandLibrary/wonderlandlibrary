package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;

public class pCPacketCreativeInventoryAction extends Block {
    public pCPacketCreativeInventoryAction() {
        super( "CreativeInventoryAction", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketCreativeInventoryAction);
        this.packetclasses.add(CPacketCreativeInventoryAction.class);
        this.description = "Sends a packet to the server to perform an action in the creative inventory.";
    }

    public static class getSlotIdion extends Block {
        public getSlotIdion() {
            super( "getSlotIdion", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketCreativeInventoryAction);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketCreativeInventoryAction) event).getSlotId();

        }
    }

    public static class getStack extends Block {
        public getStack() {
            super( "getStack", MainBlockType.ItemStack, Tabs.Sub, BlockObjects.Name, Headers.CPacketCreativeInventoryAction);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketCreativeInventoryAction) event).getStack();

        }
    }

    public static class SendCreativeInventoryAction extends Block {
        public SendCreativeInventoryAction() {
            super( "SendCreativeInventoryAction", MainBlockType.Default, Tabs.Sub, Headers.CPacketCreativeInventoryAction, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send Handshake slotId:";
            this.words[1] = "ItemStack:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.ItemStack));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // int slotIdIn, ItemStack stackIn
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event), (ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
