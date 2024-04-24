package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.ArrayList;

public class onRenderGameOverlayText extends Block {
    public onRenderGameOverlayText() {
        super("onRenderGameOverlayText", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.RenderGameOverlayEventText);
        this.description = "Called when the game is rendering the overlay for text (f3)";
    }

    public static class getLeft extends Block {
        public getLeft() {
            super("getLeft", MainBlockType.Array, Tabs.Sub, BlockObjects.Name, Headers.RenderGameOverlayEventText);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((RenderGameOverlayEvent.Text) event).getLeft();
        }
    }

    public static class setLeft extends Block {
        public setLeft() {
            super("setLeft", MainBlockType.Default, Tabs.Sub, Headers.RenderGameOverlayEventText, BlockObjects.Name, BlockObjects.NumericalTextEnter
                    , BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Set to:";
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ArrayList<String> left = ((RenderGameOverlayEvent.Text) event).getLeft();
            for (int i = 0; i < left.size(); i++) {
                String s = left.get(i);
                if (s .contains( (String)dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)) )
                    left.set(i, (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event));
            }
            super.runCode(dragableBlock, event);
        }
    }

    public static class setRight extends Block {
        public setRight() {
            super("setRight", MainBlockType.Default, Tabs.Sub, Headers.RenderGameOverlayEventText, BlockObjects.Name, BlockObjects.NumericalTextEnter
                    , BlockObjects.Words, BlockObjects.NumericalTextEnter);
            this.words[0] = "Set to:";
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
            this.typesAccepted.add(typesCollapse(MainBlockType.String));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ArrayList<String> right = ((RenderGameOverlayEvent.Text) event).getRight();
            for (int i = 0; i < right.size(); i++) {
                String s = right.get(i);
                if (s.contains((String)dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)) )
                    right.set(i, (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event));
            }
            super.runCode(dragableBlock, event);
        }
    }

    public static class getRight extends Block {
        public getRight() {
            super("getRight", MainBlockType.Array, Tabs.Sub, BlockObjects.Name, Headers.RenderGameOverlayEventText);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((RenderGameOverlayEvent.Text) event).getRight();
        }
    }

}
