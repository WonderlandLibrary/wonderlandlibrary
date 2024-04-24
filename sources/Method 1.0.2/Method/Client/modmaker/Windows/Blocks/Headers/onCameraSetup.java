package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.client.event.EntityViewRenderEvent;

public class onCameraSetup extends Block {
    public onCameraSetup() {
        super( "onCameraSetup", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.CameraSetup);
        this.description = "Called when the camera is setup";
    }

    public static class getPitch extends Block {
        public getPitch() {
            super( "getPitch", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CameraSetup);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((EntityViewRenderEvent.CameraSetup)event).getPitch();
        }
    }

    public static class getRoll extends Block {
        public getRoll() {
            super( "getRoll", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CameraSetup);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((EntityViewRenderEvent.CameraSetup)event).getRoll();
        }
    }

    public static class getYaw extends Block {
        public getYaw() {
            super( "getYaw", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.CameraSetup);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((EntityViewRenderEvent.CameraSetup)event).getYaw();
        }
    }

    public static class getEntitytup extends Block {
        public getEntitytup() {
            super( "getEntitytup", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.CameraSetup);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((EntityViewRenderEvent.CameraSetup)event).getEntity();
        }
    }

}
