package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityEquipment;

public class pSPacketEntityEquipment extends Block {
    public pSPacketEntityEquipment() {
        super( "EntityEquipment", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketEntityEquipment);
        this.packetclasses.add(SPacketEntityEquipment.class);
    }

    public static class getEquipmentSlot extends Block {
        public getEquipmentSlot() {
            super( "getEquipmentSlot", MainBlockType.EquipmentSlot, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityEquipment);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityEquipment) event).getEquipmentSlot();
        }
    }

    public static class getItemStackent extends Block {
        public getItemStackent() {
            super( "getItemStackent", MainBlockType.Items, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityEquipment);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityEquipment) event).getItemStack();
        }
    }

    public static class getEntityIDent extends Block {
        public getEntityIDent() {
            super( "getEntityIDent", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketEntityEquipment);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketEntityEquipment) event).getEntityID();
        }
    }

    public static class setEntityIDent extends Block {
        public setEntityIDent() {
            super( "setEntityIDent", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityEquipment, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityEquipment) event).entityID = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setItemStack extends Block {
        public setItemStack() {
            super( "setItemStack", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityEquipment, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.ItemStack));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityEquipment) event).itemStack = (ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class EquipmentSlot extends Block {
        public EquipmentSlot() {
            super( "EquipmentSlot", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityEquipment, BlockObjects.Name, BlockObjects.DropDown);
            for (EntityEquipmentSlot value : EntityEquipmentSlot.values()) {
                this.ddOptions.add(value.toString());
            }

        }
    }

    public static class setEquipmentSlot extends Block {
        public setEquipmentSlot() {
            super( "setEquipmentSlot", MainBlockType.Default, Tabs.Sub, Headers.SPacketEntityEquipment, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.EquipmentSlot));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketEntityEquipment) event).equipmentSlot = (EntityEquipmentSlot) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

}
