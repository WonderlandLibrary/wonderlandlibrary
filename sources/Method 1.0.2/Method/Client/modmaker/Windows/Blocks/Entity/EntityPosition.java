package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EntityPosition extends Block {

    public EntityPosition() {
        super("EntityPosition", MainBlockType.Numbers, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns Data about an Entity" + "\n" + "(Entity).posx , posy , posz , "+"\n" +"prevposx , prevposy , prevposz , serverposx , serverposy " + "\n"
                + "serverposz , movevertical , movestrafing ,"+"\n" +" moveforward , motionx ,deathtime , motiony , motionz ,"+"\n" +" jumpmovementfactor , randomyawvelocity";
        ddOptions.add("X");
        ddOptions.add("Y");
        ddOptions.add("Z");
        ddOptions.add("PrevX");
        ddOptions.add("PrevY");
        ddOptions.add("PrevZ");
        ddOptions.add("ServerX");
        ddOptions.add("ServerY");
        ddOptions.add("ServerZ");
        ddOptions.add("LastTickPosX");
        ddOptions.add("LastTickPosY");
        ddOptions.add("LastTickPosZ");
        ddOptions.add("moveVertical");
        ddOptions.add("moveStrafing");
        ddOptions.add("moveForward");
        ddOptions.add("MotionX");
        ddOptions.add("MotionY");
        ddOptions.add("MotionZ");
        ddOptions.add("jumpFactor");
        ddOptions.add("YawVelocity");
        ddOptions.add("rotationPitch");
        ddOptions.add("rotationYaw");
        ddOptions.add("rotationYawHead");
        ddOptions.add("deathTime");

    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "X":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).posX;
            case "Y":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).posY;
            case "Z":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).posZ;
            case "PrevX":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).prevPosX;
            case "PrevY":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).prevPosY;
            case "PrevZ":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).prevPosZ;
            case "ServerX":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).serverPosX;
            case "ServerY":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).serverPosY;
            case "ServerZ":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).serverPosZ;
            case "LastTickPosX":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).lastTickPosX;
            case "LastTickPosY":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).lastTickPosY;
            case "LastTickPosZ":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).lastTickPosZ;
            case "moveVertical":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).moveVertical;
            case "moveStrafing":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).moveStrafing;
            case "moveForward":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).moveForward;
            case "MotionX":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).motionX;
            case "MotionY":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).motionY;
            case "MotionZ":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).motionZ;
            case "jumpFactor":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).jumpMovementFactor;
            case "YawVelocity":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).randomYawVelocity;
            case "rotationPitch":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).rotationPitch;
            case "rotationYaw":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).rotationYaw;
            case "rotationYawHead":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).rotationYawHead;
            case "deathTime":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).deathTime;
        }
        return 0;
    }

}
