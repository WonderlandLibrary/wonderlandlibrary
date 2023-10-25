package tech.drainwalk.client.module.modules.combat;

import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.client.option.options.MultiOption;
import tech.drainwalk.client.option.options.MultiOptionValue;

public class AntiBotModule extends Module {
    private final BooleanOption setting = new BooleanOption("Delifan", true);
    private final MultiOption setting3 = new MultiOption("Delikates", new MultiOptionValue("Deus", false), new MultiOptionValue("De1us", true));
    private final BooleanOption setting1 = new BooleanOption("Delifan1", true);

    public AntiBotModule() {
        super("AntiBot", Category.COMBAT);
        register(setting, setting3, setting1);
    }
}