package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketTeams;

public class pSPacketTeams extends Block {
    public pSPacketTeams() {
        super( "Teams", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketTeams);
        this.packetclasses.add(SPacketTeams.class);
    }

    public static class getNameams extends Block {
        public getNameams() {
            super( "getNameams", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketTeams);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketTeams) event).getName();

        }
    }

    public static class getActionams extends Block {
        public getActionams() {
            super( "getActionams", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketTeams);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketTeams) event).getAction();

        }
    }

    public static class getColor extends Block {
        public getColor() {
            super( "getColor", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketTeams);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketTeams) event).getColor();

        }
    }

    public static class getFriendlyFlags extends Block {
        public getFriendlyFlags() {
            super( "getFriendlyFlags", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketTeams);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketTeams) event).getFriendlyFlags();

        }
    }

    public static class getDisplayName extends Block {
        public getDisplayName() {
            super( "getDisplayName", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketTeams);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketTeams) event).getDisplayName();

        }
    }

    public static class getCollisionRule extends Block {
        public getCollisionRule() {
            super( "getCollisionRule", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketTeams);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketTeams) event).getCollisionRule();

        }
    }

    public static class getNameTagVisibility extends Block {
        public getNameTagVisibility() {
            super( "getNameTagVisibility", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketTeams);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketTeams) event).getNameTagVisibility();

        }
    }

    public static class getPrefix extends Block {
        public getPrefix() {
            super( "getPrefix", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketTeams);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketTeams) event).getPrefix();

        }
    }

    public static class getSuffix extends Block {
        public getSuffix() {
            super( "getSuffix", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketTeams);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketTeams) event).getSuffix();

        }
    }

    public static class getPlayers extends Block {
        public getPlayers() {
            super( "getPlayers", MainBlockType.Array, Tabs.Sub, BlockObjects.Name, Headers.SPacketTeams);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketTeams) event).getPlayers();

        }
    }

    public static class setName extends Block {
        public setName() {
            super( "setName", MainBlockType.Default, Tabs.Sub, Headers.SPacketTeams, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTeams) event).name = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setFriendlyFlags extends Block {
        public setFriendlyFlags() {
            super( "setFriendlyFlags", MainBlockType.Default, Tabs.Sub, Headers.SPacketTeams, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTeams) event).friendlyFlags = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


    public static class setNameTagVisibility extends Block {
        public setNameTagVisibility() {
            super( "setNameTagVisibility", MainBlockType.Default, Tabs.Sub, Headers.SPacketTeams, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTeams) event).nameTagVisibility = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


    public static class setCollisionRule extends Block {
        public setCollisionRule() {
            super( "setCollisionRule", MainBlockType.Default, Tabs.Sub, Headers.SPacketTeams, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTeams) event).collisionRule = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setColorams extends Block {
        public setColorams() {
            super( "setColorams", MainBlockType.Default, Tabs.Sub, Headers.SPacketTeams, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTeams) event).color = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setactionams extends Block {
        public setactionams() {
            super( "setactionams", MainBlockType.Default, Tabs.Sub, Headers.SPacketTeams, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTeams) event).action = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setdisplayName extends Block {
        public setdisplayName() {
            super( "setdisplayName", MainBlockType.Default, Tabs.Sub, Headers.SPacketTeams, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTeams) event).displayName = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setprefix extends Block {
        public setprefix() {
            super( "setprefix", MainBlockType.Default, Tabs.Sub, Headers.SPacketTeams, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTeams) event).prefix = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setsuffix extends Block {
        public setsuffix() {
            super( "setsuffix", MainBlockType.Default, Tabs.Sub, Headers.SPacketTeams, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketTeams) event).suffix = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


}
