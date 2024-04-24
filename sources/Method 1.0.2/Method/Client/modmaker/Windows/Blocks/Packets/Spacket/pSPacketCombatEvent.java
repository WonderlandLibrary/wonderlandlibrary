package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.util.text.TextComponentString;

public class pSPacketCombatEvent extends Block {
    public pSPacketCombatEvent() {
        super( "PacketCombatEvent", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketCombatEvent);
        this.packetclasses.add(SPacketCombatEvent.class);
    }

    public static class playerId extends Block {
        public playerId() {
            super( "playerId", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketCombatEvent);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketCombatEvent) event).playerId;

        }
    }

    public static class entityIdent extends Block {
        public entityIdent() {
            super( "entityIdent", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketCombatEvent);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketCombatEvent) event).entityId;

        }
    }

    public static class duration extends Block {
        public duration() {
            super( "duration", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketCombatEvent);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketCombatEvent) event).duration;

        }
    }

    public static class deathMessage extends Block {
        public deathMessage() {
            super( "deathMessage", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketCombatEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketCombatEvent) event).deathMessage.toString();

        }
    }

    public static class eventType extends Block {
        public eventType() {
            super( "eventType", MainBlockType.CombatEvent, Tabs.Sub, BlockObjects.Name, Headers.SPacketCombatEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketCombatEvent) event).eventType;
        }
    }


////////////////////////////////////////////////////////////////////////

    public static class seteventType extends Block {
        public seteventType() {
            super( "seteventType", MainBlockType.Default, Tabs.Sub, Headers.SPacketCombatEvent, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.CombatEvent));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCombatEvent) event).eventType = (SPacketCombatEvent.Event) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setPlayerId extends Block {
        public setPlayerId() {
            super( "setPlayerId", MainBlockType.Default, Tabs.Sub, Headers.SPacketCombatEvent, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCombatEvent) event).playerId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setEntityIdcom extends Block {
        public setEntityIdcom() {
            super( "setEntityIdcom", MainBlockType.Default, Tabs.Sub, Headers.SPacketCombatEvent, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCombatEvent) event).entityId = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setDuration extends Block {
        public setDuration() {
            super( "setDuration", MainBlockType.Default, Tabs.Sub, Headers.SPacketCombatEvent, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCombatEvent) event).duration = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setDeathMessage extends Block {
        public setDeathMessage() {
            super( "setDeathMessage", MainBlockType.Default, Tabs.Sub, Headers.SPacketCombatEvent, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCombatEvent) event).deathMessage = new TextComponentString((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }


}
