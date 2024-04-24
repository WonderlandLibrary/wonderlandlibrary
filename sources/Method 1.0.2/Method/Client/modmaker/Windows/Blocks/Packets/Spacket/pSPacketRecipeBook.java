package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketRecipeBook;

public class pSPacketRecipeBook extends Block {
    public pSPacketRecipeBook() {
        super( "RecipeBook", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketRecipeBook);
        this.packetclasses.add(SPacketRecipeBook.class);
    }

    public static class isGuiOpenook extends Block {
        public isGuiOpenook() {
            super( "isGuiOpenook", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketRecipeBook);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketRecipeBook) event).isGuiOpen();

        }
    }

    public static class getStateook extends Block {
        public getStateook() {
            super( "getStateook", MainBlockType.RecipeBook, Tabs.Sub, BlockObjects.Name, Headers.SPacketRecipeBook);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketRecipeBook) event).getState();

        }
    }

    public static class isFilteringCraftableook extends Block {
        public isFilteringCraftableook() {
            super( "isFilteringCraftableook", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketRecipeBook);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketRecipeBook) event).isFilteringCraftable();

        }
    }

    public static class setGuiOpen extends Block {
        public setGuiOpen() {
            super( "setGuiOpen", MainBlockType.Default, Tabs.Sub, Headers.SPacketRecipeBook, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketRecipeBook) event).guiOpen = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setFilteringCraftable extends Block {
        public setFilteringCraftable() {
            super( "setFilteringCraftable", MainBlockType.Default, Tabs.Sub, Headers.SPacketRecipeBook, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketRecipeBook) event).filteringCraftable = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setstateook extends Block {
        public setstateook() {
            super( "setstateook", MainBlockType.Default, Tabs.Sub, Headers.SPacketRecipeBook, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.RecipeBook));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketRecipeBook) event).state = (SPacketRecipeBook.State) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
