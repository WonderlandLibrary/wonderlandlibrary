package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.SPacketCooldown;

public class pSPacketCooldown extends Block {
    public pSPacketCooldown() {
        super( "Cooldown", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketCooldown);
        this.packetclasses.add(SPacketCooldown.class);
    }

    public static class getItemown extends Block {
        public getItemown() {
            super( "getItemown", MainBlockType.Items, Tabs.Sub, BlockObjects.Name, Headers.SPacketCooldown);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketCooldown) event).getItem();

        }
    }

    public static class getTicks extends Block {
        public getTicks() {
            super( "getTicks", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketCooldown);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketCooldown) event).getTicks();

        }
    }

    public static class setItem extends Block {
        public setItem() {
            super( "setItem", MainBlockType.Default, Tabs.Sub, Headers.SPacketCooldown, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Items));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCooldown) event).item = (Item) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setTicks extends Block {
        public setTicks() {
            super( "setTicks", MainBlockType.Default, Tabs.Sub, Headers.SPacketCooldown, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCooldown) event).ticks = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
