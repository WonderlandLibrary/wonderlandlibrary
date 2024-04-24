package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

public class EntityAttributes extends Block {

    public EntityAttributes() {
        super("EntityAttributes", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns attributes about an Entity"
                +"Take care with some that require a living entity"
                + "\n" + "Examples:" +
                " OnGround " +
                " OnLadder " +
                " InWater " +
                " OverWater " +"\n" +
                " BreatheUnderwater " +
                " isPushedByWater " + "\n" +
                " isInLava " +
                " isChild " +
                " isRiding " +"\n" +
                " isAirBorne " +
                " isDead " +
                " isInWeb " +"\n" +
                " isSwingInProgress " +
                " isAIDisabled " +
                " isLeftHanded " +"\n" +
                " isNoDespawnRequired " +
                " isNotColliding " +
                " isServerWorld " +"\n" +
                " isActiveItemStackBlocking " +
                " isAddedToWorld " +
                " isBeingRidden " +
                " isBurning " +"\n" +
                " isElytraFlying " +
                " isEntityAlive " +
                " isEntityInsideOpaqueBlock " +
                " isEntityUndead " +"\n" +
                " isGlowing " +
                " isHandActive " +"\n" +
                " isImmuneToExplosions " +
                " isImmuneToFire " +
                " isInvisible " +"\n" +
                " isNonBoss " +
                " isOutsideBorder " +
                " isPlayerSleeping " +"\n" +
                " isSilent " +
                " isSneaking " +
                " isSprinting " +"\n" +
                " isWet " +
                " inPortal " +
                " isPositionDirty " +"\n" +
                " isLeashed " +
                " velocityChanged " +
                " noClip " +
                " canPickUpLoot " +
                " persistenceRequired ";
        ddOptions.add("OnGround");
        ddOptions.add("OnLadder");
        ddOptions.add("InWater");
        ddOptions.add("OverWater");
        ddOptions.add("BreatheUnderwater");
        ddOptions.add("isPushedByWater");
        ddOptions.add("isInLava");
        ddOptions.add("isChild");
        ddOptions.add("isRiding");
        ddOptions.add("isAirBorne");
        ddOptions.add("isDead");
        ddOptions.add("isInWeb");
        ddOptions.add("isSwingInProgress");
        ddOptions.add("isAIDisabled");
        ddOptions.add("isLeftHanded");
        ddOptions.add("isNoDespawnRequired");
        ddOptions.add("isNotColliding");
        ddOptions.add("isServerWorld");
        ddOptions.add("isActiveItemStackBlocking");
        ddOptions.add("isAddedToWorld");
        ddOptions.add("isBeingRidden");
        ddOptions.add("isBurning");
        ddOptions.add("isElytraFlying");
        ddOptions.add("isEntityAlive");
        ddOptions.add("isEntityInsideOpaqueBlock");
        ddOptions.add("isEntityUndead");
        ddOptions.add("isGlowing");
        ddOptions.add("isHandActive");
        ddOptions.add("isImmuneToExplosions");
        ddOptions.add("isImmuneToFire");
        ddOptions.add("isInvisible");
        ddOptions.add("isNonBoss");
        ddOptions.add("isOutsideBorder");
        ddOptions.add("isPlayerSleeping");
        ddOptions.add("isSilent");
        ddOptions.add("isSneaking");
        ddOptions.add("isSprinting");
        ddOptions.add("isWet");
        ddOptions.add("inPortal");
        ddOptions.add("isPositionDirty");
        ddOptions.add("isLeashed");
        ddOptions.add("velocityChanged");
        ddOptions.add("noClip");
        ddOptions.add("canPickUpLoot");
        ddOptions.add("persistenceRequired");
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "OnLadder":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isOnLadder();
            case "OnGround":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).onGround;
            case "InWater":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isInWater();
            case "OverWater":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isOverWater();
            case "BreatheUnderwater":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).canBreatheUnderwater();
            case "isPushedByWater":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isPushedByWater();
            case "isInLava":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isInLava();
            case "isChild":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isChild();
            case "isRiding":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isRiding();
            case "isAirBorne":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isAirBorne;
            case "isDead":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isDead;
            case "isInWeb":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isInWeb;
            case "isSwingInProgress":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isSwingInProgress;

            case "isServerWorld":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isServerWorld();
            case "isActiveItemStackBlocking":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isActiveItemStackBlocking();
            case "isAddedToWorld":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isAddedToWorld();
            case "isBeingRidden":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isBeingRidden();
            case "isBurning":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isBurning();
            case "isElytraFlying":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isElytraFlying();
            case "isEntityAlive":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isEntityAlive();
            case "isEntityInsideOpaqueBlock":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isEntityInsideOpaqueBlock();
            case "isEntityUndead":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isEntityUndead();
            case "isGlowing":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isGlowing();
            
                    
                    
            case "isHandActive":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isHandActive();
            case "isImmuneToExplosions":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isImmuneToExplosions();
            case "isImmuneToFire":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isImmuneToFire();
            case "isInvisible":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isInvisible();
            case "isNonBoss":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isNonBoss();
            case "isOutsideBorder":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isOutsideBorder();
            case "isPlayerSleeping":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isPlayerSleeping();
            case "isSilent":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isSilent();
            case "isSneaking":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isSneaking();
            case "isSprinting":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isSprinting();
            case "isWet":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isWet();
            case "inPortal":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).inPortal;
            case "isPositionDirty":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isPositionDirty;
            case "velocityChanged":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).velocityChanged;
                case "noClip":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).noClip;
            case "canPickUpLoot":
                return ((EntityLiving) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).canPickUpLoot;
            case "persistenceRequired":
                return ((EntityLiving) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).persistenceRequired;
            case "isLeashed":
                return ((EntityLiving) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isLeashed;
            case "isAIDisabled":
                return ((EntityLiving) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isAIDisabled();
            case "isLeftHanded":
                return ((EntityLiving) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isLeftHanded();
            case "isNoDespawnRequired":
                return ((EntityLiving) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isNoDespawnRequired();
            case "isNotColliding":
                return ((EntityLiving) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isNotColliding();
        }
        return false;
    }

}
