package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class SetPitchYaw extends Block {

    public SetPitchYaw() {
        super("SetPitchYaw", MainBlockType.Default, Tabs.Utils,BlockObjects.Name, BlockObjects.DropDown, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
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
        this.description = "Sets the pitch or yaw of the camera";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Pitch":
                Minecraft.getMinecraft().player.rotationPitch = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "prevCameraPitch":
                Minecraft.getMinecraft().player.prevCameraPitch = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "cameraPitch":
                Minecraft.getMinecraft().player.cameraPitch = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "prevRotationPitch":
                Minecraft.getMinecraft().player.prevRotationPitch = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "Yaw":
                Minecraft.getMinecraft().player.rotationYaw = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "rotationYawHead":
                Minecraft.getMinecraft().player.rotationYawHead = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "cameraYaw":
                Minecraft.getMinecraft().player.cameraYaw = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "attackedAtYaw":
                Minecraft.getMinecraft().player.attackedAtYaw = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "prevCameraYaw":
                Minecraft.getMinecraft().player.prevCameraYaw = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "prevRotationYaw":
                Minecraft.getMinecraft().player.prevRotationYaw = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "prevRenderYawOffset":
                Minecraft.getMinecraft().player.prevRenderYawOffset = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "prevRotationYawHead":
                Minecraft.getMinecraft().player.prevRotationYawHead = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "renderYawOffset":
                Minecraft.getMinecraft().player.renderYawOffset = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
        }
        super.runCode(dragableBlock, event);
    }


}
