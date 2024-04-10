package info.sigmaclient.sigma.modules.movement.flys;

import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.movement.Fly;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class FlyModule extends Module {
    protected Fly parent;
    public FlyModule(String name, String desc, Fly parent) {
        super(name, desc);
        this.parent = parent;
    }
}
