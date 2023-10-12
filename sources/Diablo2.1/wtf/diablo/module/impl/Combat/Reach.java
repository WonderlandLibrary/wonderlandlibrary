package wtf.diablo.module.impl.Combat;

import lombok.Getter;
import lombok.Setter;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.NumberSetting;

@Getter@Setter
public class Reach extends Module {
    public static NumberSetting reach = new NumberSetting("Reach",3,0.1,3,6);
    public Reach(){
        super("Reach","Extend player reach", Category.COMBAT, ServerType.All);
        this.addSettings(reach);
    }

    public static double getReach(){
        return reach.getValue();

    }
}
