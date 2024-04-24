package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketSetPassengers;

import java.util.ArrayList;
import java.util.Collections;

public class pSPacketSetPassengers extends Block {
    public pSPacketSetPassengers() {
        super( "SetPassengers", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketSetPassengers);
        this.packetclasses.add(SPacketSetPassengers.class);
    }

    public static class getEntityIders extends Block {
        public getEntityIders() {
            super( "getEntityIders", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketSetPassengers);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketSetPassengers) event).getEntityId();

        }
    }

    public static class getPassengerIds extends Block {
        public getPassengerIds() {
            super( "getPassengerIds", MainBlockType.Array, Tabs.Sub, BlockObjects.Name, Headers.SPacketSetPassengers);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return new ArrayList<>(Collections.singletonList(((SPacketSetPassengers) event).getPassengerIds()));
        }
    }


    public static class addPassengerIds extends Block {
        public addPassengerIds() {
            super( "addPassengerIds", MainBlockType.Default, Tabs.Sub, Headers.SPacketSetPassengers, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSetPassengers) event).passengerIds = arrayAdd(((SPacketSetPassengers) event).passengerIds, dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }

    }

    public static class removePassengerIds extends Block {
        public removePassengerIds() {
            super( "removePassengerIds", MainBlockType.Default, Tabs.Sub, Headers.SPacketSetPassengers, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSetPassengers) event).passengerIds = arrayRemove(((SPacketSetPassengers) event).passengerIds, dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }

    public static class setEntityIders extends Block {
        public setEntityIders() {
            super( "setEntityIders", MainBlockType.Default, Tabs.Sub, Headers.SPacketSetPassengers, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketSetPassengers) event).entityId = ((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }


}
