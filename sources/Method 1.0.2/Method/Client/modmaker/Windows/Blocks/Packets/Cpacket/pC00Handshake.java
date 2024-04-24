package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.handshake.client.C00Handshake;

public class pC00Handshake extends Block {
    public pC00Handshake() {
        super("C00Handshake", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.C00Handshake);
        this.packetclasses.add(C00Handshake.class);
        this.description = "Sends a handshake packet to the server." + "\n" + "This packet is sent when the client connects to the server." + "\n" + "This packet is sent before the client sends any other packets.";
    }

    public static class protocolVersion extends Block {
        public protocolVersion() {
            super("protocolVersion", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.C00Handshake);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((C00Handshake) event).getProtocolVersion();
        }
    }

    public static class setprotocolVersion extends Block {
        public setprotocolVersion() {
            super("setprotocolVersion", MainBlockType.Default, Tabs.Sub, Headers.C00Handshake, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((C00Handshake) event).protocolVersion = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class FMLMarker extends Block {
        public FMLMarker() {
            super("FMLMarker", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.C00Handshake);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((C00Handshake) event).hasFMLMarker();
        }
    }


    public static class SendpC00Handshake extends Block {
        public SendpC00Handshake() {
            super("SendpC00Handshake", MainBlockType.Default, Tabs.Sub, Headers.C00Handshake, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.BooleanTextEnter);
            this.words[0] = "Send Handshake Address:";
            this.words[1] = "Port:";
            this.words[2] = "ConnectionState:";
            this.words[3] = "FmlMarker:";
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.ConnectionState));
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // String address, int port, EnumConnectionState state, boolean addFMLMarker
            Minecraft.getMinecraft().player.connection.sendPacket(new C00Handshake((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event), (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event), (EnumConnectionState) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 3, event)));
            super.runCode(dragableBlock, event);
        }
    }

}
