package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketUpdateHealth;

public class pSPacketUpdateHealth extends Block {
    public pSPacketUpdateHealth() {
        super( "UpdateHealth", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketUpdateHealth);
        this.packetclasses.add(SPacketUpdateHealth.class);
    }

    public static class getHealth extends Block {
        public getHealth() {
            super( "getHealth", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketUpdateHealth);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketUpdateHealth) event).getHealth();

        }
    }

    public static class getFoodLevel extends Block {
        public getFoodLevel() {
            super( "getFoodLevel", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketUpdateHealth);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketUpdateHealth) event).getFoodLevel();

        }
    }

    public static class getSaturationLevel extends Block {
        public getSaturationLevel() {
            super( "getSaturationLevel", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketUpdateHealth);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketUpdateHealth) event).getSaturationLevel();

        }
    }

    public static class setFoodLevel extends Block {
        public setFoodLevel() {
            super( "setFoodLevel", MainBlockType.Default, Tabs.Sub, Headers.SPacketUpdateHealth, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUpdateHealth) event).foodLevel = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setHealth extends Block {
        public setHealth() {
            super( "setHealth", MainBlockType.Default, Tabs.Sub, Headers.SPacketUpdateHealth, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUpdateHealth) event).health = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setSaturationLevel extends Block {
        public setSaturationLevel() {
            super( "setSaturationLevel", MainBlockType.Default, Tabs.Sub, Headers.SPacketUpdateHealth, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketUpdateHealth) event).saturationLevel = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
