package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class pSPacketPlayerPosLook extends Block {
    public pSPacketPlayerPosLook() {
        super("PlayerPosLook", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketPlayerPosLook);
        this.packetclasses.add(SPacketPlayerPosLook.class);
    }

    public static class getXook extends Block {
        public getXook() {
            super("getXook", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketPlayerPosLook);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketPlayerPosLook) event).getX();

        }
    }

    public static class getYook extends Block {
        public getYook() {
            super("getYook", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketPlayerPosLook);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketPlayerPosLook) event).getY();

        }
    }

    public static class getZook extends Block {
        public getZook() {
            super("getZook", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketPlayerPosLook);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketPlayerPosLook) event).getZ();

        }
    }

    public static class isFlagook extends Block {
        public isFlagook() {
            super("isFlagook", MainBlockType.Boolean, Tabs.Sub, Headers.SPacketPlayerPosLook, BlockObjects.Name, BlockObjects.DropDown);
            ddOptions.add("X");
            ddOptions.add("Y");
            ddOptions.add("Z");
            ddOptions.add("X_ROT");
            ddOptions.add("Y_ROT");
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            switch (dragableBlock.dropDowns.getSelected()) {
                case "X":
                    return ((SPacketPlayerPosLook) event).getFlags().contains(SPacketPlayerPosLook.EnumFlags.X);
                case "Y":
                    return ((SPacketPlayerPosLook) event).getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y);
                case "Z":
                    return ((SPacketPlayerPosLook) event).getFlags().contains(SPacketPlayerPosLook.EnumFlags.Z);
                case "X_ROT":
                    return ((SPacketPlayerPosLook) event).getFlags().contains(SPacketPlayerPosLook.EnumFlags.X_ROT);
                case "Y_ROT":
                    return ((SPacketPlayerPosLook) event).getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y_ROT);

            }

            return false;
        }
    }

    public static class getPitchook extends Block {
        public getPitchook() {
            super("getPitchook", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketPlayerPosLook);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketPlayerPosLook) event).getPitch();

        }
    }

    public static class pSPgetYaw extends Block {
        public pSPgetYaw() {
            super("pSPgetYaw", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketPlayerPosLook);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketPlayerPosLook) event).getYaw();

        }
    }

    public static class getTeleportIdook extends Block {
        public getTeleportIdook() {
            super("getTeleportIdook", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketPlayerPosLook);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketPlayerPosLook) event).getTeleportId();

        }
    }

    public static class setXook extends Block {
        public setXook() {
            super("setXook", MainBlockType.Default, Tabs.Sub, Headers.SPacketPlayerPosLook, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketPlayerPosLook) event).x = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setYook extends Block {
        public setYook() {
            super("setYook", MainBlockType.Default, Tabs.Sub, Headers.SPacketPlayerPosLook, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketPlayerPosLook) event).y = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setZook extends Block {
        public setZook() {
            super("setZook", MainBlockType.Default, Tabs.Sub, Headers.SPacketPlayerPosLook, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketPlayerPosLook) event).z = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setPitchook extends Block {
        public setPitchook() {
            super("setPitchook", MainBlockType.Default, Tabs.Sub, Headers.SPacketPlayerPosLook, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketPlayerPosLook) event).pitch = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class pSPsetYaw extends Block {
        public pSPsetYaw() {
            super("pSPsetYaw", MainBlockType.Default, Tabs.Sub, Headers.SPacketPlayerPosLook, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketPlayerPosLook) event).yaw = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setTeleportId extends Block {
        public setTeleportId() {
            super("setTeleportId", MainBlockType.Default, Tabs.Sub, Headers.SPacketPlayerPosLook, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketPlayerPosLook) event).teleportId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}
