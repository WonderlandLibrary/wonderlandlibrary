package tech.drainwalk.client.module.modules.combat;

import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.option.options.*;

public class AuraModule extends Module {
    BooleanOption booleanOption = new BooleanOption("CheckBox",true)
            .addSettingDescription("CheckBox setting");
    public AuraModule() {
        super("Aura", Category.COMBAT);
        register(
                booleanOption ,

                new SelectOption("SelectBox", 2,
                        new SelectOptionValue("Value 0"),
                        new SelectOptionValue("Value 1"),
                        new SelectOptionValue("Value 2"),
                        new SelectOptionValue("Value 3")
                ).addSettingDescription("SelectBox setting").addVisibleCondition(booleanOption::getValue),

                new MultiOption("MultiSelectBox",
                        new MultiOptionValue("Value 0",false),
                        new MultiOptionValue("Value 1",true),
                        new MultiOptionValue("Value 2",false),
                        new MultiOptionValue("Value 3",true),
                        new MultiOptionValue("Value 4",false)
                ).addSettingDescription("MultiSelectBox setting").addVisibleCondition(booleanOption::getValue),

                new FloatOption("Slider",25,0,100).addIncrementValue(0.1f)
                        .addSettingDescription("Slider setting").addVisibleCondition(booleanOption::getValue),
                new ColorOption("Color", -1)
        );
    }
}
