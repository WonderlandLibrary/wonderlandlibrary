package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.TextComponentString;

public class pSPacketTitle extends Block {
    public pSPacketTitle() {
        super("Title", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketTitle);
        this.packetclasses.add(SPacketTitle.class);
    }

    public static class getDisplayTime extends Block {
        public getDisplayTime() {
            super("getDisplayTime", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketTitle);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketTitle) event).getDisplayTime();

        }
    }

    public static class getFadeInTime extends Block {
        public getFadeInTime() {
            super("getFadeInTime", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketTitle);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketTitle) event).getFadeInTime();

        }
    }

    public static class getFadeOutTime extends Block {
        public getFadeOutTime() {
            super("getFadeOutTime", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketTitle);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketTitle) event).getFadeOutTime();

        }
    }

    public static class getMessagetle extends Block {
        public getMessagetle() {
            super("getMessagetle", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketTitle);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketTitle) event).getMessage().toString();

        }
    }

    public static class getTypetle extends Block {
        public getTypetle() {
            super("getTypetle", MainBlockType.TitleType, Tabs.Sub, BlockObjects.Name, Headers.SPacketTitle);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketTitle) event).getType();

        }
    }

    public static class setDisplayTime extends Block {
        public setDisplayTime() {
            super("setDisplayTime", MainBlockType.Default, Tabs.Sub, Headers.SPacketTitle, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTitle) event).displayTime = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setFadeInTime extends Block {
        public setFadeInTime() {
            super("setFadeInTime", MainBlockType.Default, Tabs.Sub, Headers.SPacketTitle, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTitle) event).fadeInTime = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setFadeOutTime extends Block {
        public setFadeOutTime() {
            super("setFadeOutTime", MainBlockType.Default, Tabs.Sub, Headers.SPacketTitle, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTitle) event).fadeOutTime = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setMessage extends Block {
        public setMessage() {
            super("setMessage", MainBlockType.Default, Tabs.Sub, Headers.SPacketTitle, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTitle) event).message = new TextComponentString((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
            super.runCode(dragableBlock, event);
        }
    }


    public static class setTypetle extends Block {
        public setTypetle() {
            super("setTypetle", MainBlockType.Default, Tabs.Sub, Headers.SPacketTitle, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.TitleType));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTitle) event).type = (SPacketTitle.Type) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event);
            super.runCode(dragableBlock, event);
        }
    }
}
