package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketRecipeInfo;

public class pCPacketRecipeInfo extends Block {
    public pCPacketRecipeInfo() {
        super( "RecipeInfo", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketRecipeInfo);
        this.packetclasses.add(CPacketRecipeInfo.class);
        this.description = "Sends a recipe info packet to the server.";
    }


    public static class isFilteringCraftable extends Block {
        public isFilteringCraftable() {
            super( "isFilteringCraftable", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.CPacketRecipeInfo);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((CPacketRecipeInfo) event).isFilteringCraftable();

        }
    }

    public static class isGuiOpen extends Block {
        public isGuiOpen() {
            super( "isGuiOpen", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.CPacketRecipeInfo);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((CPacketRecipeInfo) event).isGuiOpen();

        }
    }

    public static class SendRecipeInfo extends Block {
        public SendRecipeInfo() {
            super( "SendRecipeInfo", MainBlockType.Default, Tabs.Sub, Headers.CPacketRecipeInfo, BlockObjects.Words, BlockObjects.BooleanTextEnter,
                    BlockObjects.Words, BlockObjects.BooleanTextEnter);
            this.words[0] = "Send RecipeInfo isGuiOpen:";
            this.words[1] = "filteringCraftable:";

            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));

        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // boolean isGuiOpen, boolean filteringCraftable
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketRecipeInfo(dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event)));
            super.runCode(dragableBlock, event);
        }
    }
}
