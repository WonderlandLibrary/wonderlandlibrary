package wtf.diablo.module.impl.Movement;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.input.Keyboard;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.Module;
import wtf.diablo.module.ModuleManager;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.module.impl.Player.Scaffold;
import wtf.diablo.settings.impl.ModeSetting;

@Getter
@Setter
public class Sprint extends Module {
    public ModeSetting mode = new ModeSetting("Mode","Legit","Omni");

    public Sprint() {
        super("Sprint", "Automatically Sprints for you.", Category.MOVEMENT, ServerType.All);
        this.addSettings(mode);
    }

    @Subscribe
    public void onUpdateEvent(UpdateEvent event) {
        this.setSuffix(mode.getMode());
        if ((mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) && !ModuleManager.getModule(Scaffold.class).isToggled()) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
