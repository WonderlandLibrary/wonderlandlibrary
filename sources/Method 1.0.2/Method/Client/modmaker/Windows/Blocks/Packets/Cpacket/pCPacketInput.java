package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketInput;

public class pCPacketInput extends Block {
    public pCPacketInput() {
        super( "Input", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketInput);
        this.packetclasses.add(CPacketInput.class);
        this.description = "Sends a packet to the server to tell it that the player is moving.";
    }

    public static class getForwardSpeed extends Block {
        public getForwardSpeed() {
            super( "getForwardSpeed", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketInput);
        }
        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((CPacketInput) event).getForwardSpeed();
        }
    }
    public static class getStrafeSpeed extends Block {
        public getStrafeSpeed() {
            super( "getStrafeSpeed", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketInput);
        }
        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event){
            return ((CPacketInput) event).getStrafeSpeed();
        }
    }
    public static class isJumping extends Block {
        public isJumping() {
            super( "isJumping", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.CPacketInput);
        }
        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event){
            return ((CPacketInput) event).isJumping();
        }
    }
    public static class isSneaking extends Block {
        public isSneaking() {
            super( "isSneaking", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.CPacketInput);
        }
        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event){
            return ((CPacketInput) event).isSneaking();
        }
    }
    public static class SendPacketInput extends Block {
        public SendPacketInput() {
            super( "SendPacketInput", MainBlockType.Default, Tabs.Sub, Headers.CPacketInput, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.BooleanTextEnter, BlockObjects.Words, BlockObjects.BooleanTextEnter);
            this.words[0] = "Send Handshake strafeSpeed:";
            this.words[1] = "forwardSpeed:";
            this.words[2] = "jumping:";
            this.words[3] = "sneaking:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // float strafeSpeedIn, float forwardSpeedIn, boolean jumpingIn, boolean sneakingIn
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketInput((float) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event), (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event),  dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 2, event),  dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 3, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
