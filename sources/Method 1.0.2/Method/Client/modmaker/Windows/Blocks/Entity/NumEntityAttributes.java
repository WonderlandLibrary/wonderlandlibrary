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
import net.minecraft.entity.player.EntityPlayer;

import java.util.Objects;

public class NumEntityAttributes extends Block {

    public NumEntityAttributes() {
        super("NumEntityAttributes", MainBlockType.Numbers, Tabs.Entity, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        ddOptions.add("RidingStepHeight");
        ddOptions.add("StepHeight");
        ddOptions.add("entityId");
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
        ddOptions.add("getSleepTimer");
        ddOptions.add("getHealth");

        ddOptions.add("chunkCoordX");
        ddOptions.add("chunkCoordY");
        ddOptions.add("chunkCoordZ");

        ddOptions.add("lifespan");

        this.description = "Returns Various numerical attributes about Entities " + "\n" + " RidingStepHeight " +
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
                " chunkCoordX " +
                " chunkCoordY " +
                " chunkCoordZ " +
                " lifespan (Only for entity Item)" +
                " hurtResistantTime " +
                " timeUntilPortal \n" +
                " portalCounter " +
                " dimension " +
                " maxHurtTime " +
                " hurtTime " +
                " getSleepTimer \n" +
                " getHealth ";

     }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "RidingStepHeight":
                return Objects.requireNonNull(Objects.requireNonNull(Minecraft.getMinecraft().player).getRidingEntity()).stepHeight;
            case "StepHeight":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).stepHeight;
            case "entityId":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getEntityId();
            case "rideCooldown":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).rideCooldown;
            case "width":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).width;
            case "height":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).height;
            case "distanceWalkedModified":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).distanceWalkedModified;
            case "distanceWalkedOnStepModified":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).distanceWalkedOnStepModified;
            case "fallDistance":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).fallDistance;
            case "nextStepDistance":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).nextStepDistance;
            case "nextFlap":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).nextFlap;
            case "entityCollisionReduction":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).entityCollisionReduction;
            case "ticksExisted":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).ticksExisted;
            case "fire":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).fire;
            case "hurtResistantTime":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).hurtResistantTime;
            case "timeUntilPortal":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).timeUntilPortal;
            case "portalCounter":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).portalCounter;
            case "dimension":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).dimension;
            case "maxHurtTime":
                return Objects.requireNonNull((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).maxHurtTime;
            case "hurtTime":
                return Objects.requireNonNull((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).hurtTime;
            case "getSleepTimer":
                return Objects.requireNonNull((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getSleepTimer();
            case "getHealth":
                return Objects.requireNonNull((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getHealth();
            case "chunkCoordX":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).chunkCoordX;
            case "chunkCoordY":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).chunkCoordY;
            case "chunkCoordZ":
                return Objects.requireNonNull((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).chunkCoordZ;
            case "lifespan":
                return Objects.requireNonNull((EntityItem) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).lifespan;

        }
        return 0;
    }

}
