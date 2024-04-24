package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class GetPitchYaw extends Block {

    public GetPitchYaw() {
        super("GetPitchYaw", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name,BlockObjects.DropDown);
        ddOptions.add("Pitch");
        ddOptions.add("prevCameraPitch");
        ddOptions.add("cameraPitch");
        ddOptions.add("prevRotationPitch");
        ddOptions.add("Yaw");
        ddOptions.add("rotationYawHead");
        ddOptions.add("cameraYaw");
        ddOptions.add("attackedAtYaw");
        ddOptions.add("prevCameraYaw");
        ddOptions.add("prevRotationYaw");
        ddOptions.add("prevRenderYawOffset");
        ddOptions.add("prevRotationYawHead");
        ddOptions.add("renderYawOffset");
        this.description = "Gets the pitch or yaw of the player";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Pitch":
                return Minecraft.getMinecraft().player.rotationPitch;
            case "prevCameraPitch":
                return Minecraft.getMinecraft().player.prevCameraPitch;
            case "cameraPitch":
                return Minecraft.getMinecraft().player.cameraPitch;
            case "prevRotationPitch":
                return Minecraft.getMinecraft().player.prevRotationPitch;
            case "Yaw":
                return Minecraft.getMinecraft().player.rotationYaw;
            case "rotationYawHead":
                return Minecraft.getMinecraft().player.rotationYawHead;
            case "cameraYaw":
                return Minecraft.getMinecraft().player.cameraYaw;
            case "attackedAtYaw":
                return Minecraft.getMinecraft().player.attackedAtYaw;
            case "prevCameraYaw":
                return Minecraft.getMinecraft().player.prevCameraYaw;
            case "prevRotationYaw":
                return Minecraft.getMinecraft().player.prevRotationYaw;
            case "prevRenderYawOffset":
                return Minecraft.getMinecraft().player.prevRenderYawOffset;
            case "prevRotationYawHead":
                return Minecraft.getMinecraft().player.prevRotationYawHead;
            case "renderYawOffset":
                return Minecraft.getMinecraft().player.renderYawOffset;
        }
        return 0;
    }


}
