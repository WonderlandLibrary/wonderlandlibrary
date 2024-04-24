package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

public class SetEntityAttributes extends Block {

    public SetEntityAttributes() {
        super("SetEntityAttributes", MainBlockType.Default, Tabs.Entity, BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.DropDown,
                BlockObjects.BooleanTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.words[0] = "Entity";
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));

        this.description = "Sets Entity attributes   " + "\n" + "Glowing" +
                " OnGround " +
                " isAirBorne " +
                " isDead " +
                " isInWeb " +
                " isSwingInProgress \n" +
                " inPortal " +
                " isPositionDirty " +
                " isLeashed " +
                " velocityChanged " +
                " noClip " +
                " canPickUpLoot \n" +
                " persistenceRequired " +
                " forceSpawn " +
                " isOutsideBorder " +
                " inWater " +
                " addedToChunk " +
                " ignoreFrustumCheck \n" +
                " invulnerable \n" +
                " updateBlocked \n" +
                " Sprinting \n" +
                " NoGravity " +
                " jumping " +
                " collided " +
                " collidedHorizontally " +
                " collidedVertically " +
                " isSneaking ";

        ddOptions.add("Glowing");
        ddOptions.add("OnGround");
        ddOptions.add("isAirBorne");
        ddOptions.add("isDead");
        ddOptions.add("isInWeb");
        ddOptions.add("isSwingInProgress");
        ddOptions.add("inPortal");
        ddOptions.add("isPositionDirty");
        ddOptions.add("isLeashed");
        ddOptions.add("velocityChanged");
        ddOptions.add("noClip");
        ddOptions.add("canPickUpLoot");
        ddOptions.add("persistenceRequired");
        ddOptions.add("forceSpawn");
        ddOptions.add("isOutsideBorder");
        ddOptions.add("inWater");
        ddOptions.add("addedToChunk");
        ddOptions.add("ignoreFrustumCheck");
        ddOptions.add("invulnerable");
        ddOptions.add("updateBlocked");
        ddOptions.add("NoGravity");
        ddOptions.add("Sprinting");
        ddOptions.add("Sneaking");
        ddOptions.add("Jumping");

        ddOptions.add("collided");
        ddOptions.add("collidedHorizontally");
        ddOptions.add("collidedVertically");

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Glowing":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).setGlowing(dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event));
                break;
            case "OnGround":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).onGround = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "isAirBorne":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).isAirBorne = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "isDead":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).isDead = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "isInWeb":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).isInWeb = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "isSwingInProgress":
                ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).isSwingInProgress = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "inPortal":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).inPortal = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "isPositionDirty":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).isPositionDirty = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "isLeashed":
                ((EntityLiving) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).isLeashed = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "velocityChanged":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).velocityChanged = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "noClip":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).noClip = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "canPickUpLoot":
                ((EntityLiving) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).canPickUpLoot = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "persistenceRequired":
                ((EntityLiving) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).persistenceRequired = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "forceSpawn":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).forceSpawn = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "isOutsideBorder":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).isOutsideBorder = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "inWater":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).inWater = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "addedToChunk":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).addedToChunk = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "ignoreFrustumCheck":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).ignoreFrustumCheck = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "invulnerable":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).invulnerable = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "updateBlocked":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).updateBlocked = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "NoGravity":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).setNoGravity(dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event));
                break;
            case "Sprinting":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).setSprinting(dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event));
                break;
            case "Sneaking":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).setSneaking(dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event));
                break;
            case "Jumping":
                ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).setJumping(dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event));
                break;
            case "collided":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).collided = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "collidedHorizontally":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).collidedHorizontally = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;
            case "collidedVertically":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).collidedVertically = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
                break;

        }
        super.runCode(dragableBlock, event);
    }

}
