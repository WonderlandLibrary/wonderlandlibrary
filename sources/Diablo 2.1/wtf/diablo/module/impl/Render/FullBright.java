package wtf.diablo.module.impl.Render;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;

@Getter@Setter
public class FullBright extends Module {
    private float lastGam;
    public FullBright(){
        super("FullBright","Full brightness", Category.RENDER, ServerType.All);
    }

    @Override
    public void onEnable() {
        lastGam = mc.gameSettings.gammaSetting;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = lastGam;
        super.onDisable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
        mc.gameSettings.gammaSetting = 999;
    }
}
