package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;

import java.util.Objects;

public class SetNumEntityAttributes extends Block {

    public SetNumEntityAttributes() {
        super("SetNumEntityAttr", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

        this.words[0] = "Value";
        this.description = "Set Numerical values of a Entity   " + "\n" + "\n" + " RidingStepHeight " +
                " StepHeight " +
                " entityId " +
                " rideCooldown " +
                " width " +
                " height " +
                " distanceWalkedModified \n" +
                " distanceWalkedOnStepModified \n" +
                " fallDistance " +
                " nextStepDistance " +
                " nextFlap " +
                " entityCollisionReduction " +
                " ticksExisted \n" +
                " fire " +
                " hurtResistantTime " +
                " chunkCoordX " +
                " chunkCoordY " +
                " chunkCoordZ " +
                " dimension " +
                " lifespan ItemOnly" +
                " timeUntilPortal \n" +
                " portalCounter " +
                " maxHurtTime " +
                " hurtTime " +
                " getSleepTimer \n" +
                " getHealth ";

        ddOptions.add("chunkCoordX");
        ddOptions.add("chunkCoordY");
        ddOptions.add("chunkCoordZ");
        ddOptions.add("lifespan");
        ddOptions.add("RidingStepHeight");
        ddOptions.add("StepHeight");
        ddOptions.add("rideCooldown");
        ddOptions.add("width");
        ddOptions.add("height");
        ddOptions.add("distanceWalkedModified");
        ddOptions.add("distanceWalkedOnStepModified");
        ddOptions.add("fallDistance");
        ddOptions.add("nextStepDistance");
        ddOptions.add("nextFlap");
        ddOptions.add("entityCollisionReduction");
        ddOptions.add("ticksExisted");
        ddOptions.add("fire");
        ddOptions.add("hurtResistantTime");
        ddOptions.add("timeUntilPortal");
        ddOptions.add("portalCounter");
        ddOptions.add("dimension");
        ddOptions.add("maxHurtTime");
        ddOptions.add("hurtTime");
        ddOptions.add("setHealth");
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "RidingStepHeight":
                Objects.requireNonNull(Objects.requireNonNull(Minecraft.getMinecraft().player).getRidingEntity()).stepHeight = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "StepHeight":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).stepHeight = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "rideCooldown":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).rideCooldown = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "width":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).width = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "height":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).height = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "distanceWalkedModified":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).distanceWalkedModified = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "distanceWalkedOnStepModified":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).distanceWalkedOnStepModified = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "fallDistance":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).fallDistance = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "nextStepDistance":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).nextStepDistance = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "nextFlap":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).nextFlap = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "entityCollisionReduction":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).entityCollisionReduction = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "ticksExisted":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).ticksExisted = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "fire":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).fire = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "hurtResistantTime":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).hurtResistantTime = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "timeUntilPortal":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).timeUntilPortal = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "portalCounter":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).portalCounter = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "dimension":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).dimension = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "maxHurtTime":
                Objects.requireNonNull((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).maxHurtTime = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "hurtTime":
                Objects.requireNonNull((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).hurtTime = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "setHealth":
                Objects.requireNonNull((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).setHealth((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event));
                break;
            case "chunkCoordX":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).chunkCoordX = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "chunkCoordY":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).chunkCoordY = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "chunkCoordZ":
                Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).chunkCoordZ = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;
            case "lifespan":
                Objects.requireNonNull((EntityItem) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).lifespan = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
                break;

        }
        super.runCode(dragableBlock, event);
    }
}
