package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.entity.Entity;

public class EntBoundingBox extends Block {

    public EntBoundingBox() {
        super("EntBoundingBox", MainBlockType.BoundingBox, Tabs.Render, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter );


        this.words[0] = "Entity:";
        this.words[1] = "X:";
        this.words[2] = "Y:";
        this.words[3] = "Z:";
        this.words[4] = "X2:";
        this.words[5] = "Y2:";
        this.words[6] = "Z2:";

        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Creates a Entity bounding box";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return RenderUtils.Boundingbb((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event),
                dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event),
                dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event),
                dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,3, event),
                dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,4, event),
                dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,5, event),
                 dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,6, event));
    }


}
