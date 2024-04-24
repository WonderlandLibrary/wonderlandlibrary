package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;

public class pCPacketClickWindow extends Block {
    public pCPacketClickWindow() {
        super( "ClickWindow", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketClickWindow);
        this.packetclasses.add(CPacketClickWindow.class);
        this.description = "Sends a click packet to the server";
    }

    public static class getWindowId extends Block {
        public getWindowId() {
            super( "getWindowId", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketClickWindow);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketClickWindow) event).getWindowId();
        }
    }

    public static class getClickType extends Block {
        public getClickType() {
            super( "getClickType", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.CPacketClickWindow);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketClickWindow) event).getClickType();

        }
    }

    public static class getClickedItem extends Block {
        public getClickedItem() {
            super( "getClickedItem", MainBlockType.Items, Tabs.Sub, BlockObjects.Name, Headers.CPacketClickWindow);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketClickWindow) event).getClickedItem();

        }
    }

    public static class getSlotId extends Block {
        public getSlotId() {
            super( "getSlotId", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketClickWindow);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketClickWindow) event).getSlotId();

        }
    }

    public static class getUsedButton extends Block {
        public getUsedButton() {
            super( "getUsedButton", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketClickWindow);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketClickWindow) event).getUsedButton();

        }
    }

    public static class SendCPacketClickWindow extends Block {
        public SendCPacketClickWindow() {
            super( "SendCPacketClickWindow", MainBlockType.Default, Tabs.Sub, Headers.CPacketClickWindow, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send Handshake Windowid:";
            this.words[1] = "slotId:";
            this.words[2] = "usedButton:";
            this.words[3] = "ClickType:";
            this.words[4] = "ItemStack:";
            this.words[5] = "actionNumber:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.ClickType));
            this.typesAccepted.add(typesCollapse(MainBlockType.Items));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // int windowIdIn, int slotIdIn, int usedButtonIn, ClickType modeIn, ItemStack clickedItemIn, short actionNumberIn
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketClickWindow((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event), (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event),
                    (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 2, event), (ClickType) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 3, event), (ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 4, event), (short) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 5, event)));
            super.runCode(dragableBlock, event);
        }
    }

}
