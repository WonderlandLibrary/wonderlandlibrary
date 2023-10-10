package tech.atani.client.feature.module.impl.combat;

import cn.muyang.nativeobfuscator.Native;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.listener.event.minecraft.game.TimerManipulationEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.player.combat.FightUtil;
import tech.atani.client.utility.math.time.TimeHelper;
import tech.atani.client.feature.value.impl.SliderValue;

@Native
@ModuleData(name = "TickBase", description = "Tick Base Manipulation", category = Category.COMBAT)
public class TickBase extends Module {

	public final SliderValue<Long> maxBalance = new SliderValue<>("Max Balance", "What will be the maximum balance?", this, 100L, 0L, 5000L, 0);
	public final SliderValue<Long> delay = new SliderValue<>("Delay", "What will be the delay between shifting?", this, 300L, 0L, 1000L, 0);
	public final SliderValue<Float> range = new SliderValue<>("Range", "At what range will the module operate?", this, 3f, 0.1f,7f, 1);

	private KillAura killAura;
	private long shifted, previousTime;
	private final TimeHelper timeHelper = new TimeHelper();

	@Listen
	public void onTime(TimerManipulationEvent timerManipulationEvent) {
		if(killAura == null)
			killAura = ModuleStorage.getInstance().getByClass(KillAura.class);

		if(shouldCharge() && this.timeHelper.hasReached(delay.getValue())) {
			shifted += timerManipulationEvent.getTime() - previousTime;
		}

		if(shouldDischarge()) {
			shifted = 0;
			this.timeHelper.reset();
		}

		previousTime = timerManipulationEvent.getTime();
		timerManipulationEvent.setTime(timerManipulationEvent.getTime() - shifted);
	}

	private boolean shouldCharge() {
		return killAura.isEnabled() && KillAura.curEntity != null && this.shifted < maxBalance.getValue();
	}

	private boolean shouldDischarge() {
		return this.shifted >= this.maxBalance.getValue() && killAura.isEnabled() && killAura.curEntity != null && FightUtil.getRange(KillAura.curEntity) > range.getValue();
	}

	@Override
	public void onDisable() {
		this.shifted = 0;
	}

	@Override
	public void onEnable() {
		this.shifted = 0;
		this.previousTime = (System.nanoTime() / 1000000L) / 1000L;
	}

}
