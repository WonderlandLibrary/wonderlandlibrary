package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketPlayerAbilities;

public class pSPacketPlayerAbilities extends Block {
    public pSPacketPlayerAbilities() {
        super( "ServerPlayerAbilities", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketPlayerAbilities);
        this.packetclasses.add(SPacketPlayerAbilities.class);
    }

    public static class isAllowFlyingies extends Block {
        public isAllowFlyingies() {
            super( "isAllowFlyingies", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketPlayerAbilities);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketPlayerAbilities) event).isAllowFlying();
        }
    }
    public static class isFlyingies extends Block {
        public isFlyingies() {
            super( "isFlyingies", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketPlayerAbilities);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketPlayerAbilities) event).isFlying();
        }
    }
    public static class isCreativeModeies extends Block {
        public isCreativeModeies() {
            super( "isCreativeModeies", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketPlayerAbilities);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketPlayerAbilities) event).isCreativeMode();
        }
    }
    public static class isInvulnerableies extends Block {
        public isInvulnerableies() {
            super( "isInvulnerableies", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.SPacketPlayerAbilities);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((SPacketPlayerAbilities) event).isInvulnerable();
        }
    }
    public static class getFlySpeed extends Block {
        public getFlySpeed() {
            super( "getFlySpeed", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketPlayerAbilities);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketPlayerAbilities) event).getFlySpeed();
        }
    }
    public static class getWalkSpeed extends Block {
        public getWalkSpeed() {
            super( "getWalkSpeed", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketPlayerAbilities);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketPlayerAbilities) event).getWalkSpeed();
        }
    }


    public static class setFlying extends Block {
        public setFlying() {
            super( "setFlying", MainBlockType.Default, Tabs.Sub,Headers.SPacketPlayerAbilities, BlockObjects.Name,BlockObjects.BooleanTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketPlayerAbilities) event).flying = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setAllowFlying extends Block {
        public setAllowFlying() {
            super( "setAllowFlying", MainBlockType.Default, Tabs.Sub,Headers.SPacketPlayerAbilities, BlockObjects.Name,BlockObjects.BooleanTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketPlayerAbilities) event).allowFlying = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setInvulnerable extends Block {
        public setInvulnerable() {
            super( "setInvulnerable", MainBlockType.Default, Tabs.Sub,Headers.SPacketPlayerAbilities, BlockObjects.Name,BlockObjects.BooleanTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketPlayerAbilities) event).invulnerable = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setFlySpeed extends Block {
        public setFlySpeed() {
            super( "setFlySpeed", MainBlockType.Default, Tabs.Sub,Headers.SPacketPlayerAbilities, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketPlayerAbilities) event).flySpeed = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setWalkSpeed extends Block {
        public setWalkSpeed() {
            super( "setWalkSpeed", MainBlockType.Default, Tabs.Sub,Headers.SPacketPlayerAbilities, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketPlayerAbilities) event).walkSpeed = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setcreativeMode extends Block {
        public setcreativeMode() {
            super( "setcreativeMode", MainBlockType.Default, Tabs.Sub,Headers.SPacketPlayerAbilities, BlockObjects.Name,BlockObjects.BooleanTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketPlayerAbilities) event).creativeMode = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}
