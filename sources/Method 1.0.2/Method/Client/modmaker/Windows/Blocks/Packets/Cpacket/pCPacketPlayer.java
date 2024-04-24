package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;

public class pCPacketPlayer extends Block {
    public pCPacketPlayer() {
        super("PacketPlayer", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketPlayer);
        this.packetclasses.add(CPacketPlayer.class);
        this.packetclasses.add(CPacketPlayer.PositionRotation.class);
        this.packetclasses.add(CPacketPlayer.Position.class);
        this.packetclasses.add(CPacketPlayer.Rotation.class);
        this.description = "Sends a player packet";
    }

    public static class onGround extends Block {
        public onGround() {
            super("onGround", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayer);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayer) event).onGround;
        }
    }

    public static class Getx extends Block {
        public Getx() {
            super("Getx", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayer);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayer) event).x;
        }
    }

    public static class Gety extends Block {
        public Gety() {
            super("Gety", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayer);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayer) event).y;
        }
    }

    public static class GetType extends Block {
        public GetType() {
            super("GetType", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayer);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            if (event instanceof CPacketPlayer.PositionRotation)
                return "PositionRotation";
            if (event instanceof CPacketPlayer.Position)
                return "Position";
            if (event instanceof CPacketPlayer.Rotation)
                return "Rotation";
            return "PositionRotation";
        }
    }

    public static class Getz extends Block {
        public Getz() {
            super("Getz", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayer);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayer) event).z;
        }
    }

    public static class pitch extends Block {
        public pitch() {
            super("pitch", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayer);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayer) event).pitch;
        }
    }

    public static class yaw extends Block {
        public yaw() {
            super("yaw", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayer);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayer) event).yaw;
        }
    }

    public static class setOnground extends Block {
        public setOnground() {
            super("setOnground", MainBlockType.Default, Tabs.Sub, Headers.CPacketPlayer, BlockObjects.Name, BlockObjects.BooleanTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((CPacketPlayer) event).onGround = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setx extends Block {
        public setx() {
            super("setx", MainBlockType.Default, Tabs.Sub, Headers.CPacketPlayer, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((CPacketPlayer) event).x = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class sety extends Block {
        public sety() {
            super("sety", MainBlockType.Default, Tabs.Sub, Headers.CPacketPlayer, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((CPacketPlayer) event).y = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setz extends Block {
        public setz() {
            super("setz", MainBlockType.Default, Tabs.Sub, Headers.CPacketPlayer, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((CPacketPlayer) event).z = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


    public static class setpitch extends Block {
        public setpitch() {
            super("setpitch", MainBlockType.Default, Tabs.Sub, Headers.CPacketPlayer, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((CPacketPlayer) event).pitch = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setyaw extends Block {
        public setyaw() {
            super("setyaw", MainBlockType.Default, Tabs.Sub, Headers.CPacketPlayer, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((CPacketPlayer) event).yaw = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class SendOnground extends Block {
        public SendOnground() {
            super("SendOnground", MainBlockType.Default, Tabs.Sub, Headers.CPacketPlayer,
                    BlockObjects.Words, BlockObjects.BooleanTextEnter);
            this.words[0] = "Onground:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer(dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event)));
            super.runCode(dragableBlock, event);
        }
    }

    public static class SendPlayerRotation extends Block {
        public SendPlayerRotation() {
            super("SendPlayerRotation", MainBlockType.Default, Tabs.Sub, Headers.CPacketPlayer, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.BooleanTextEnter);
            this.words[0] = "Send Player Yaw:";
            this.words[1] = "Pitch:";
            this.words[2] = "Onground:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // float yaw, float pitch, boolean onground
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Rotation((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event), (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event)
                    , dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 2, event)));
            super.runCode(dragableBlock, event);
        }
    }

    public static class SendPlayerPosition extends Block {
        public SendPlayerPosition() {
            super("SendPlayerPosition", MainBlockType.Default, Tabs.Sub, Headers.CPacketPlayer, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.BooleanTextEnter);
            this.words[0] = "Send Player X:";
            this.words[1] = "Y:";
            this.words[2] = "Z:";
            this.words[3] = "Onground:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            //    double x, double y, double z, boolean onGround
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event), (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event),
                    (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 2, event), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 3, event)));
            super.runCode(dragableBlock, event);
        }
    }

    public static class SendPlayerPosRot extends Block {
        public SendPlayerPosRot() {
            super("SendPlayerPosRot", MainBlockType.Default, Tabs.Sub, Headers.CPacketPlayer, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.BooleanTextEnter);
            this.words[0] = "Send Player X:";
            this.words[1] = "Y:";
            this.words[2] = "Z:";
            this.words[3] = "Yaw:";
            this.words[4] = "Pitch:";
            this.words[5] = "Onground:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            //    double x, double y, double z, float yaw, float pitch, boolean onGround
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.PositionRotation((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event),
                    (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event),
                    (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 2, event), (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 3, event),
                    (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 4, event), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 5, event)));
            super.runCode(dragableBlock, event);
        }
    }

    public static class SendPlayerBlank extends Block {
        public SendPlayerBlank() {
            super("SendPlayerBlank", MainBlockType.Default, Tabs.Sub, Headers.CPacketPlayer, BlockObjects.Name);

        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer());
            super.runCode(dragableBlock, event);
        }
    }


}
