package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Thread extends Block {


    public Thread() {
        super("Thread", MainBlockType.Default, Tabs.Utils, BlockObjects.Name);
        this.description = "beans";
    }
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        executor.execute(() -> {
            try {
                super.runCode(dragableBlock, event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }


}
