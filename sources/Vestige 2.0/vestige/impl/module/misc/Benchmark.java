package vestige.impl.module.misc;

import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.BenchmarkEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;

@ModuleInfo(name = "Benchmark", category = Category.MISC)
public class Benchmark extends Module {

    private long time;

    public void onEnable() {
        time = System.currentTimeMillis();

        for(int i = 0; i < 1000000; i++) {
            new BenchmarkEvent().call();
        }
        this.setEnabled(false);
    }

    public void onDisable() {
        Vestige.getInstance().addChatMessage("Delay : " + (System.currentTimeMillis() - time));
    }

    @Listener
    public void onBenchmark(BenchmarkEvent e) {
    	
    }

}
