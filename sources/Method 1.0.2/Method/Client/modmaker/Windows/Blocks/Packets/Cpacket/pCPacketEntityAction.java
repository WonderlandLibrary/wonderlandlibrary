package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;

public class pCPacketEntityAction extends Block {
    public pCPacketEntityAction() {
        super( "PacketEntityAction", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketEntityAction);
        this.packetclasses.add(CPacketEntityAction.class);
        this.description = "Sends an action to the server.";
    }

    public static class getAction extends Block {
        public getAction() {
            super( "getAction", MainBlockType.EntityAction, Tabs.Sub, BlockObjects.Name, Headers.CPacketEntityAction);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketEntityAction) event).getAction();

        }
    }

    public static class getAuxData extends Block {
        public getAuxData() {
            super( "getAuxData", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketEntityAction);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketEntityAction) event).getAuxData();

        }
    }

    public static class SendEntityAction extends Block {
        public SendEntityAction() {
            super( "SendEntityAction", MainBlockType.Default, Tabs.Sub, Headers.CPacketEntityAction, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send EntityAction Entity:";
            this.words[1] = "EntityAction:";
            this.words[2] = "auxData(0):";
            this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
            this.typesAccepted.add(typesCollapse(MainBlockType.EntityAction));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // Entity entityIn, CPacketEntityAction.Action actionIn, int auxDataIn
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketEntityAction((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event), (CPacketEntityAction.Action) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event), (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 2, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
