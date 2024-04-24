package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;

import java.util.Objects;

public class SpawnParticle extends Block {

    public SpawnParticle() {
        super("SpawnParticle", MainBlockType.Default, Tabs.Render, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter);

        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.words[0] = "X:";
        this.words[1] = "Y:";
        this.words[2] = "Z:";

        this.words[3] = "Spd X:";
        this.words[4] = "Spd Y:";
        this.words[5] = "Spd Z:";
        this.description = "Spawns a particle";
        for (EnumParticleTypes value : EnumParticleTypes.values()) {
            ddOptions.add(value.getParticleName());
        }
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {

            Minecraft.getMinecraft().world.spawnParticle(Objects.requireNonNull(EnumParticleTypes.getByName(dragableBlock.dropDowns.getSelected())),
                    dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event), dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event)
                    , dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,3, event), dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,4, event), dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,5, event));


        super.runCode(dragableBlock, event);
    }



}
