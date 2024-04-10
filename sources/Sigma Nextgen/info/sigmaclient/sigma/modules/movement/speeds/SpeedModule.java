package info.sigmaclient.sigma.modules.movement.speeds;

import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.movement.Speed;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class SpeedModule extends Module {
    protected Speed parent;
    public SpeedModule(String name, String desc, Speed parent) {
        super(name, desc);
        this.parent = parent;
    }
}
