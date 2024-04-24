package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class SetEntityPosition extends Block {

    public SetEntityPosition() {
        super("SetEntityPosition", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Set position of Entity   " + "\n" + "Entity.posX = " + "Entity.posY = " + "Entity.posZ = " + "\n"
                + "Entity.prevPosX = " + "Entity.prevPosY = " + "Entity.prevPosZ = " + "\n"
                + "Entity.serverPosX = " + "Entity.serverPosY = " + "\n" + "Entity.serverPosZ = "
                + "\n" + "Entity.moveVertical = " + "\n" + "Entity.moveStrafing = " + "\n" + "Entity.moveForward = " + "\n"
                + "Entity.motionX = " + "\n" + "Entity.motionY = "
                + "Entity.motionZ = " + "Entity.jumpMovementFactor = " + "\n" + "Entity.randomYawVelocity = ";
        ddOptions.add("X");
        ddOptions.add("Y");
        ddOptions.add("Z");
        ddOptions.add("PrevX");
        ddOptions.add("PrevY");
        ddOptions.add("PrevZ");
        ddOptions.add("ServerX");
        ddOptions.add("ServerY");
        ddOptions.add("ServerZ");
        ddOptions.add("moveVertical");
        ddOptions.add("moveStrafing");
        ddOptions.add("moveForward");
        ddOptions.add("MotionX");
        ddOptions.add("MotionY");
        ddOptions.add("MotionZ");
        ddOptions.add("jumpFactor");
        ddOptions.add("YawVelocity");
        ddOptions.add("rotationYaw");
        ddOptions.add("rotationPitch");


    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "X":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).posX = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "Y":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).posY = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "Z":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).posZ = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "PrevX":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).prevPosX = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "PrevY":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).prevPosY = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "PrevZ":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).prevPosZ = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "ServerX":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).serverPosX = (long) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "ServerY":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).serverPosY = (long) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "ServerZ":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).serverPosZ = (long) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "moveVertical":
                ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).moveVertical = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "moveStrafing":
                ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).moveStrafing = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "moveForward":
                ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).moveForward = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "MotionX":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).motionX = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "MotionY":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).motionY = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "MotionZ":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).motionZ = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "jumpFactor":
                ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).jumpMovementFactor = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "YawVelocity":
                ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).randomYawVelocity = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "rotationYaw":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).rotationYaw = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "rotationPitch":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).rotationPitch = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;


        }

        super.runCode(dragableBlock, event);
    }

}
