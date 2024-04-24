package Method.Client.modmaker;


import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.module.Module;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("UnstableApiUsage")
public class ThreadedCatchExecuter extends CatchCodeExecuter {

    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public ThreadedCatchExecuter(Module module) {
        super(module);
    }

    @Override
    public void headerRun(Headers headers, net.minecraftforge.fml.common.eventhandler.Event event) {
        if (headers.equals(Headers.RenderWorldLastEvent))
            super.headerRun(headers, event);
        else
            executor.execute(() -> super.headerRun(headers, event));
    }


}
