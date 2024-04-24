package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class pCPacketUpdateSign extends Block {
    public pCPacketUpdateSign() {
        super( "UpdateSign", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketUpdateSign);
        this.packetclasses.add(CPacketUpdateSign.class);
        this.description = "Sends a packet to update a sign";
    }

    public static class getPositionign extends Block {
        public getPositionign() {
            super( "getPositionign", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.CPacketUpdateSign);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketUpdateSign) event).getPosition();

        }
    }

    public static class SendSignUpdate extends Block {
        public SendSignUpdate() {
            super( "SendSignUpdate", MainBlockType.Default, Tabs.Sub, Headers.CPacketUpdateSign, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send SignUpdate BlockPos:";
            this.words[1] = "Line1:";
            this.words[2] = "Line2:";
            this.words[3] = "Line3:";
            this.words[4] = "Line4:";

            this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ITextComponent[] lines = new ITextComponent[4];
            lines[0] = new TextComponentString((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event));
            lines[1] = new TextComponentString((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event));
            lines[2] = new TextComponentString((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 3, event));
            lines[3] = new TextComponentString((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 4, event));

            // BlockPos pos, ITextComponent[] Lines
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketUpdateSign(((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)), lines));
            super.runCode(dragableBlock, event);
        }
    }
}
