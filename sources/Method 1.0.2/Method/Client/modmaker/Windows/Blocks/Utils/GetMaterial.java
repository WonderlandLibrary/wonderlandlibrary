package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.block.state.IBlockState;

public class GetMaterial extends Block {

    public GetMaterial() {
        super("GetMaterial", MainBlockType.Boolean, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.IBlockState));
        this.words[0] = "Equals";
        ddOptions.add("AIR");
        ddOptions.add("GRASS");
        ddOptions.add("GROUND");
        ddOptions.add("WOOD");
        ddOptions.add("ROCK");
        ddOptions.add("IRON");
        ddOptions.add("ANVIL");
        ddOptions.add("WATER");
        ddOptions.add("LAVA");
        ddOptions.add("LEAVES");
        ddOptions.add("PLANTS");
        ddOptions.add("VINE");
        ddOptions.add("SPONGE");
        ddOptions.add("CLOTH");
        ddOptions.add("FIRE");
        ddOptions.add("SAND");
        ddOptions.add("CIRCUITS");
        ddOptions.add("CARPET");
        ddOptions.add("GLASS");
        ddOptions.add("REDSTONE_LIGHT");
        ddOptions.add("TNT");
        ddOptions.add("CORAL");
        ddOptions.add("ICE");
        ddOptions.add("PACKED_ICE");
        ddOptions.add("SNOW");
        ddOptions.add("CRAFTED_SNOW");
        ddOptions.add("CACTUS");
        ddOptions.add("CLAY");
        ddOptions.add("GOURD");
        ddOptions.add("DRAGON_EGG");
        ddOptions.add("PORTAL");
        ddOptions.add("CAKE");
        ddOptions.add("WEB");
        ddOptions.add("PISTON");
        ddOptions.add("BARRIER");
        ddOptions.add("STRUCTURE_VOID");


        this.description = "Gets the material of the block. IBlockState";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "AIR":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.AIR);
            case "GRASS":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.GRASS);
            case "GROUND":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.GROUND);
            case "WOOD":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.WOOD);
            case "ROCK":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.ROCK);
            case "IRON":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.IRON);
            case "ANVIL":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.ANVIL);
            case "WATER":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.WATER);
            case "LAVA":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.LAVA);
            case "LEAVES":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.LEAVES);
            case "PLANTS":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.PLANTS);
            case "VINE":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.VINE);
            case "SPONGE":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.SPONGE);
            case "CLOTH":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.CLOTH);
            case "FIRE":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.FIRE);
            case "SAND":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.SAND);
            case "CIRCUITS":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.CIRCUITS);
            case "CARPET":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.CARPET);
            case "GLASS":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.GLASS);
            case "REDSTONE_LIGHT":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.REDSTONE_LIGHT);
            case "TNT":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.TNT);
            case "CORAL":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.CORAL);
            case "ICE":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.ICE);
            case "PACKED_ICE":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.PACKED_ICE);
            case "SNOW":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.SNOW);
            case "CRAFTED_SNOW":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.CRAFTED_SNOW);
            case "CACTUS":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.CACTUS);
            case "CLAY":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.CLAY);
            case "GOURD":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.GOURD);
            case "DRAGON_EGG":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.DRAGON_EGG);
            case "PORTAL":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.PORTAL);
            case "CAKE":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.CAKE);
            case "WEB":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.WEB);
            case "PISTON":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.PISTON);
            case "BARRIER":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.BARRIER);
            case "STRUCTURE_VOID":
                return ((IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaterial().equals(net.minecraft.block.material.Material.STRUCTURE_VOID);

        }
        return false;
    }


}
