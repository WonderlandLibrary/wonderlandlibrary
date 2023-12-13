package best.azura.client.impl.module.impl.render;

import best.azura.client.impl.events.EventEmitParticle;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.NumberValue;
import net.minecraft.util.EnumParticleTypes;

@ModuleInfo(name = "Particles", description = "Do particle multiplication", category = Category.RENDER)
public class Particles extends Module {

    private final NumberValue<Integer> multiplier = new NumberValue<>("Multiplier", "Multiplier for particles", 1, 1, 1, 10);
    private final BooleanValue doCritical = new BooleanValue("Critical particles", "Do critical particles", true);
    private final BooleanValue doMagic = new BooleanValue("Magic particles", "Do magic particles", true);

    @EventHandler
    public final Listener<EventEmitParticle> eventEmitParticleListener = e -> {
        if (!((e.particleTypes == EnumParticleTypes.CRIT && doCritical.getObject()) || (e.particleTypes == EnumParticleTypes.CRIT_MAGIC && doMagic.getObject()))) return;
        e.multiplier = multiplier.getObject();
    };

}