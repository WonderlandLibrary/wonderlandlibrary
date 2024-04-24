package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.CPacketPlayerAbilities;

public class pCPacketPlayerAbilities extends Block {
    public pCPacketPlayerAbilities() {
        super( "ClientPlayerAbilities", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketPlayerAbilities);
        this.packetclasses.add(CPacketPlayerAbilities.class);
        this.description = "Sends the player's abilities to the server.";
    }

    public static class isInvulnerable extends Block {
        public isInvulnerable() {
            super( "isInvulnerable", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerAbilities);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerAbilities) event).isInvulnerable();

        }
    }

    public static class isFlying extends Block {
        public isFlying() {
            super( "isFlying", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerAbilities);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerAbilities) event).isFlying();

        }
    }

    public static class isAllowFlying extends Block {
        public isAllowFlying() {
            super( "isAllowFlying", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerAbilities);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerAbilities) event).isAllowFlying();

        }
    }

    public static class isCreativeMode extends Block {
        public isCreativeMode() {
            super( "isCreativeMode", MainBlockType.Boolean, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerAbilities);
        }

        @Override
        public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerAbilities) event).isCreativeMode();

        }
    }

    public static class flySpeed extends Block {
        public flySpeed() {
            super( "flySpeed", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerAbilities);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerAbilities) event).flySpeed;
        }
    }

    public static class walkSpeed extends Block {
        public walkSpeed() {
            super( "walkSpeed", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CPacketPlayerAbilities);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((CPacketPlayerAbilities) event).walkSpeed;
        }
    }

    public static class SendPlayerAbilities extends Block {
        public SendPlayerAbilities() {
            super( "SendPlayerAbilities", MainBlockType.Default, Tabs.Sub, Headers.CPacketPlayerAbilities, BlockObjects.Words, BlockObjects.BooleanTextEnter,
                    BlockObjects.Words, BlockObjects.BooleanTextEnter, BlockObjects.Words, BlockObjects.BooleanTextEnter, BlockObjects.Words, BlockObjects.BooleanTextEnter, BlockObjects.Words,
                    BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send PlayerAbilities disableDamage:";
            this.words[1] = "isFlying:";
            this.words[2] = "allowFlying:";
            this.words[3] = "isCreativeMode:";
            this.words[4] = "FlySpeed:";
            this.words[5] = "WalkSpeed:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
            this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // PlayerCapabilities capabilities
            PlayerCapabilities capabilities = new PlayerCapabilities();
            capabilities.disableDamage = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
            capabilities.isFlying = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
            capabilities.allowFlying = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 2, event);
            capabilities.isCreativeMode = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 3, event);
            capabilities.setFlySpeed((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 4, event));
            capabilities.setPlayerWalkSpeed((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 5, event));
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayerAbilities(capabilities));
            super.runCode(dragableBlock, event);
        }
    }

}
