package Method.Client.modmaker.Windows.Blocks.Packets.Cpacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

public class pCPacketUseEntity extends Block {
    public pCPacketUseEntity() {
        super( "PacketUseEntity", MainBlockType.PacketHeader, Tabs.Out_Pkt, BlockObjects.Name, Headers.CPacketUseEntity);
        this.packetclasses.add(CPacketUseEntity.class);
        this.description = "Sends a packet to use an entity.";
    }

    public static class getActionity extends Block {
        public getActionity() {
            super( "getActionity", MainBlockType.UseEntity, Tabs.Sub, BlockObjects.Name, Headers.CPacketUseEntity);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketUseEntity) event).getAction();
        }
    }


    public static class getHandity extends Block {
        public getHandity() {
            super( "getHandity", MainBlockType.EnumHand, Tabs.Sub, BlockObjects.Name, Headers.CPacketUseEntity);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketUseEntity) event).getHand();

        }
    }
    public static class getEntityit extends Block {
        public getEntityit() {
            super( "getEntityit", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.CPacketUseEntity);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return   Minecraft.getMinecraft().world.getEntityByID(((CPacketUseEntity) event).entityId);
        }
    }

    public static class getHitVec extends Block {
        public getHitVec() {
            super( "getHitVec", MainBlockType.Vec3d, Tabs.Sub, BlockObjects.Name, Headers.CPacketUseEntity);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((CPacketUseEntity) event).getHitVec();

        }
    }
    public static class SendUseEntityAttack extends Block {
        public SendUseEntityAttack() {
            super( "SendUseEntityAttack", MainBlockType.Default, Tabs.Sub, Headers.CPacketUseEntity, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send Handshake entity:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // Entity entity
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketUseEntity((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)));
            super.runCode(dragableBlock, event);
        }
    }

    public static class SendUseEntity extends Block {
        public SendUseEntity() {
            super( "SendUseEntity", MainBlockType.Default, Tabs.Sub, Headers.CPacketUseEntity, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send Handshake entity:";
            this.words[1] = "enumHand:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
            this.typesAccepted.add(typesCollapse(MainBlockType.EnumHand));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // Entity entity, EnumHand enumHand
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketUseEntity((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event), (EnumHand) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event)));
            super.runCode(dragableBlock, event);
        }
    }

    public static class SendUseEntityVector extends Block {
        public SendUseEntityVector() {
            super( "SendUseEntityVector", MainBlockType.Default, Tabs.Sub, Headers.CPacketUseEntity, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                    BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Send Handshake entity:";
            this.words[1] = "enumHand:";
            this.words[2] = "Position:";
            this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
            this.typesAccepted.add(typesCollapse(MainBlockType.EnumHand));
            this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            // Entity entity, EnumHand enumHand, Vec3d vec3d
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketUseEntity((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event), (EnumHand) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event), (Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event)));
            super.runCode(dragableBlock, event);
        }
    }

}
