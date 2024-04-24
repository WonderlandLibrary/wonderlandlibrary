package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.block.BlockJukebox;
import net.minecraft.tileentity.*;

public class TileEntityInstance extends Block {

    public TileEntityInstance() {
        super("TileEntityInstance", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.TileEntity));
        this.description = "Returns true if input Entity is Instanceof Entity " + "\n" + "Entity instanceof EntityLiving";

        ddOptions.add("Sign");
        ddOptions.add("Banner");
        ddOptions.add("Chest");

        ddOptions.add("Dispenser");
        ddOptions.add("Furnace");
        ddOptions.add("Brewing Stand");
        ddOptions.add("Hopper");
        ddOptions.add("Dropper");
        ddOptions.add("Shulker");
        ddOptions.add("Beacon");
        ddOptions.add("Spawner");
        ddOptions.add("Piston");
        ddOptions.add("Jukebox");
        ddOptions.add("Enchantment Table");
        ddOptions.add("End Portal");
        ddOptions.add("Ender Chest");
        ddOptions.add("Mob head");
        ddOptions.add("Command Block");
        ddOptions.add("Daylight Detector");
        ddOptions.add("Comparator");
        ddOptions.add("Bed");
        ddOptions.add("Flower Pot");
        ddOptions.add("Structure");


    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Sign":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntitySign;
            case "Banner":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityBanner;
            case "Chest":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityChest;
            case "Dispenser":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityDispenser;
            case "Furnace":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityFurnace;
            case "Brewing Stand":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityBrewingStand;
            case "Hopper":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityHopper;
            case "Dropper":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityDropper;
            case "Shulker":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityShulkerBox;
            case "Beacon":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityBeacon;
            case "Spawner":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityMobSpawner;
            case "Piston":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityPiston;
            case "Jukebox":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof BlockJukebox.TileEntityJukebox;
            case "Enchantment Table":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityEnchantmentTable;
            case "End Portal":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityEndPortal;
            case "Ender Chest":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityEnderChest;
            case "Mob head":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntitySkull;
            case "Command Block":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityCommandBlock;
            case "Daylight Detector":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityDaylightDetector;
            case "Comparator":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityComparator;
            case "Bed":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityBed;

            case "Flower Pot":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityFlowerPot;
            case "Structure":
                return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof TileEntityStructure;


        }
        return false;

    }

}
