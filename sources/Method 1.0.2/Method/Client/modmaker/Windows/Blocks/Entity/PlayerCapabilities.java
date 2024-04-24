package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerCapabilities extends Block {

    public PlayerCapabilities() {
        super("PlayerCapabilities", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        ddOptions.add("isCreativeMode");
        ddOptions.add("isSpecatorMode");
        ddOptions.add("isAdventureMode");
        ddOptions.add("isSurvivalMode");
        ddOptions.add("disableDamage");
        ddOptions.add("isFlying");
        ddOptions.add("allowFlying");
        ddOptions.add("allowEdit");
        this.description = "Returns Player Capabilities   " + "\n" + "mc.player.capabilities.isCreativeMode" + "\n" + "mc.player.capabilities.disableDamage"
                + "\n" + "mc.player.capabilities.isFlying" + "\n" + "mc.player.capabilities.allowFlying" + "\n" + "mc.player.capabilities.allowEdit";


    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "isCreativeMode":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).capabilities.isCreativeMode;
            case "isSpecatorMode":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isSpectator();
            case "disableDamage":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).capabilities.disableDamage;
            case "isFlying":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).capabilities.isFlying;
            case "allowFlying":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).capabilities.allowFlying;
            case "allowEdit":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).capabilities.allowEdit;
            case "isAdventureMode":
                return !((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isAllowEdit() && !((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isSpectator();
            case "isSurvivalMode":
                return !((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isCreative() && !((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isSpectator() && ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isAllowEdit();
                
        }
        return false;
    }

}
