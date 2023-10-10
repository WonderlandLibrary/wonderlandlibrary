package tech.atani.client.feature.module.impl.render;

import net.minecraft.util.EnumParticleTypes;
import tech.atani.client.listener.event.minecraft.player.combat.AttackEntityEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.feature.value.impl.SliderValue;

@ModuleData(name = "AttackParticles", description = "Spawns particles after attacking someone", category = Category.RENDER)
public class AttackParticles extends Module {

    public SliderValue<Integer> crit = new SliderValue<>("Critical", "How many critical particles to spawn?", this, 0, 0, 20, 0);
    public SliderValue<Integer> critMagic = new SliderValue<>("Critical Magic", "How many critical magic particles to spawn?", this, 0, 0, 20, 0);

    @Listen
    public void onAttack(AttackEntityEvent attackEntityEvent) {
        for (int i = 0; i < this.crit.getValue(); ++i) {
            Methods.mc.effectRenderer.emitParticleAtEntity(attackEntityEvent.getEntity(), EnumParticleTypes.CRIT);
        }
        for (int i = 0; i < this.critMagic.getValue(); ++i) {
            Methods.mc.effectRenderer.emitParticleAtEntity(attackEntityEvent.getEntity(), EnumParticleTypes.CRIT_MAGIC);
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
