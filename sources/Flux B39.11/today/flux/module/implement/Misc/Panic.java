package today.flux.module.implement.Misc;

import today.flux.Flux;
import today.flux.module.Category;
import today.flux.module.Module;

public class Panic extends Module {
    public Panic() {
        super("Panic", Category.Misc, false);
    }

    @Override
    public void onEnable() {
        for (Module mod : Flux.INSTANCE.getModuleManager().getModList()) {
            if (mod.isEnabled())
                mod.setEnabled(false);
        }
    }
}
