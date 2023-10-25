package tech.drainwalk.client.module.modules.combat;


import net.minecraft.entity.Entity;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.module.category.Type;
import tech.drainwalk.client.option.Option;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.client.option.options.FloatOption;

public class HitBoxModule extends Module {

    public final FloatOption size = new FloatOption("Size", 1f, 1f,10f)
            .addSettingDescription("Увеличивает шанс попадания по противнику");

    public HitBoxModule() {
        super("HitBox", Category.COMBAT);
        addType(Type.SECONDARY);
        register(
            size
        );



    }
}
