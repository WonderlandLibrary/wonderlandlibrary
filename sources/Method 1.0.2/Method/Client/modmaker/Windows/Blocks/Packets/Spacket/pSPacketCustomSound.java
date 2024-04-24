package Method.Client.modmaker.Windows.Blocks.Packets.Spacket;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;

public class pSPacketCustomSound extends Block {
    public  pSPacketCustomSound() {
        super( "CustomSound", MainBlockType.PacketHeader, Tabs.In_Pkt, BlockObjects.Name, Headers.SPacketCustomSound);
        this.packetclasses.add(SPacketCustomSound.class);
    }

    public static class getSoundName extends Block {
        public getSoundName() {
            super( "getSoundName", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SPacketCustomSound);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketCustomSound) event).getSoundName();

        }
    }
    public static class getCategory extends Block {
        public getCategory() {
            super( "getCategory", MainBlockType.SoundCategory, Tabs.Sub, BlockObjects.Name, Headers.SPacketCustomSound);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((SPacketCustomSound) event).getCategory();

        }
    }
    public static class getPitchund extends Block {
        public getPitchund() {
            super( "getPitchund", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketCustomSound);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketCustomSound) event).getPitch();

        }
    }
    public static class getVolume extends Block {
        public getVolume() {
            super( "getVolume", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketCustomSound);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketCustomSound) event).getVolume();

        }
    }
    public static class getXund extends Block {
        public getXund() {
            super( "getXund", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketCustomSound);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketCustomSound) event).getX();

        }
    }
    public static class getYund extends Block {
        public getYund() {
            super( "getYund", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketCustomSound);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketCustomSound) event).getY();

        }
    }
    public static class getZund extends Block {
        public getZund() {
            super( "getZund", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.SPacketCustomSound);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((SPacketCustomSound) event).getZ();

        }
    }

    public static class ConnectionState extends Block {
        public ConnectionState() {
            super( "ConnectionState", MainBlockType.SoundCategory, Tabs.Sub, Headers.SPacketCustomSound, BlockObjects.Name, BlockObjects.DropDown);
            for (SoundCategory value : SoundCategory.values()) {
                this.ddOptions.add(value.toString());
            }

        }
    }

    public static class setSoundName extends Block {
        public setSoundName() {
            super( "setSoundName", MainBlockType.Default, Tabs.Sub,Headers.SPacketCustomSound, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCustomSound) event).soundName = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setCategory extends Block {
        public setCategory() {
            super( "setCategory", MainBlockType.Default, Tabs.Sub,Headers.SPacketCustomSound, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.SoundCategory));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCustomSound) event).category = (SoundCategory) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setPitch extends Block {
        public setPitch() {
            super( "setPitch", MainBlockType.Default, Tabs.Sub,Headers.SPacketCustomSound, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCustomSound) event).pitch = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }
    public static class setVolume extends Block {
        public setVolume() {
            super( "setVolume", MainBlockType.Default, Tabs.Sub,Headers.SPacketCustomSound, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCustomSound) event).volume = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setX extends Block {
        public setX() {
            super( "setX", MainBlockType.Default, Tabs.Sub,Headers.SPacketCustomSound, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCustomSound) event).x = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setY extends Block {
        public setY() {
            super( "setY", MainBlockType.Default, Tabs.Sub,Headers.SPacketCustomSound, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCustomSound) event).y = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }

    public static class setZ extends Block {
        public setZ() {
            super( "setZ", MainBlockType.Default, Tabs.Sub,Headers.SPacketCustomSound, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((SPacketCustomSound) event).z = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
            super.runCode(dragableBlock, event);
        }
    }


}
