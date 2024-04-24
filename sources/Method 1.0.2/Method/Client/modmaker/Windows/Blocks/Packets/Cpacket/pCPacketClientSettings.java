package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.util.EnumHandSide;

public class pCPacketClientSettings extends Block {
    public pCPacketClientSettings() {
        super( "ClientSettings", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketClientSettings);
        this.packetclasses.add(CPacketClientSettings.class);
        this.description = "Sends the client settings to the server.";
    }

    public static class getChatVisibility extends Block {
        public getChatVisibility() {
            super( "getChatVisibility", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.CPacketClientSettings);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketClientSettings) event).getChatVisibility();

        }
    }

    public static class getLang extends Block {
        public getLang() {
            super( "getLang", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.CPacketClientSettings);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketClientSettings) event).getLang();

        }
    }

    public static class getMainHand extends Block {
        public getMainHand() {
            super( "getMainHand", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.CPacketClientSettings);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketClientSettings) event).getMainHand();

        }
    }

    public static class getModelPartFlags extends Block {
        public getModelPartFlags() {
            super( "getModelPartFlags", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.CPacketClientSettings);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketClientSettings) event).getModelPartFlags();

        }
    }

    public static class isColorsEnabled extends Block {
        public isColorsEnabled() {
            super( "isColorsEnabled", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.CPacketClientSettings);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((CPacketClientSettings) event).isColorsEnabled();
        }
    }

    public static class SendCPacketClientSettings extends Block {
        public SendCPacketClientSettings() {
            super( "SendCPacketClientSettings", MainBlockType.Default, Tabs.Sub, Headers.CPacketClientSettings, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.BooleanTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send Handshake lang:";
            this.words[1] = "renderDistance:";
            this.words[2] = "ChatVisibility:";
            this.words[3] = "chatColors:";
            this.words[4] = "modelParts:";
            this.words[5] = "EnumHandSide:";
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.ChatVisibility));
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.EnumHandSide));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // String langIn, int renderDistanceIn, EntityPlayer.EnumChatVisibility chatVisibilityIn, boolean chatColorsIn, int modelPartsIn, EnumHandSide mainHandIn
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketClientSettings((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event),
                    (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event), (EntityPlayer.EnumChatVisibility) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 3, event),
                    (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 4, event), (EnumHandSide) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 5, event)));

            super.runCode(dragableBlock, event);
        }
    }
}
