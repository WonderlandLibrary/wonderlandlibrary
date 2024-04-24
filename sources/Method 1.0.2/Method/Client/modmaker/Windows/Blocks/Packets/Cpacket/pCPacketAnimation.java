package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;

public class pCPacketAnimation extends Block {
    public pCPacketAnimation() {
        super("ClientAnimation", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketAnimation);
        this.packetclasses.add(CPacketAnimation.class);
        this.description = "Sends an animation to the server.";
    }

    public static class getHandion extends Block {
        public getHandion() {
            super("getHandion", MainBlockType.EnumHand, Tabs.Sub, BlockObjects.Name, Headers.CPacketAnimation);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketAnimation) event).getHand();
        }
    }


    public static class SendpCPacketAnimation extends Block {
        public SendpCPacketAnimation() {
            super("SendpCPacketAnimation", MainBlockType.Default, Tabs.Sub, Headers.CPacketAnimation, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send PacketAnimation EnumHand:";
            this.typesAccepted.add(typesCollapse(MainBlockType.EnumHand));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketAnimation((EnumHand) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)));
            super.runCode(dragableBlock, event);
        }
    }

}
