package wtf.diablo.module.impl.Render;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;

@Getter
@Setter
public class Cape extends Module {
    public Cape() {
        super("Cape", "Give ur player a cape", Category.RENDER, ServerType.All);
    }

    @Override
    public void onEnable(){

        super.onEnable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
        mc.thePlayer.setLocationOfCape(new ResourceLocation("diablo/images/cape.png"));
    }

    @Override
    public void onDisable() {
        mc.thePlayer.setLocationOfCape(null);
        super.onDisable();
    }
}
