package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.proxy.renderers.ModRenderBoat;

public class BoatRender extends Block {

    public BoatRender() {
        super("BoatRender", MainBlockType.Default, Tabs.Render, BlockObjects.Words, BlockObjects.BooleanTextEnter,
                BlockObjects.Words, BlockObjects.BooleanTextEnter, BlockObjects.Words, BlockObjects.BooleanTextEnter,
                BlockObjects.Words, BlockObjects.BooleanTextEnter);
        this.words[0] = "Vanish:";
        this.words[1] = "Rainbow:";
        this.words[2] = "Blend:";
        this.words[3] = "Carpet:";

        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));

        this.description = "Render Boat stuff";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        boolean vanish = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
        boolean Rainbow = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event);
        boolean Blend = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 2, event);
        boolean Carpet = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 3, event);
        ModRenderBoat.Vanish = vanish;
        ModRenderBoat.Rainbow = Rainbow;
        ModRenderBoat.Blend = Blend;
        ModRenderBoat.Carpet = Carpet;

        super.runCode(dragableBlock, event);
    }


}
