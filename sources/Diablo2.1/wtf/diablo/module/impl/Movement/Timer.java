package wtf.diablo.module.impl.Movement;

import lombok.Getter;
import lombok.Setter;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.NumberSetting;

@Getter@Setter
public class Timer extends Module {
    public NumberSetting timer = new NumberSetting("Timer",1,0.05,0.1,5);
    public Timer(){
        super("Timer","Increase game speed", Category.MOVEMENT, ServerType.All);
        this.addSettings(timer);
    }
}
