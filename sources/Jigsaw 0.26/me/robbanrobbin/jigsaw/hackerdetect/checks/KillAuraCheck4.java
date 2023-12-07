package me.robbanrobbin.jigsaw.hackerdetect.checks;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.hackerdetect.Hacker;
import net.minecraft.entity.Entity;

public class KillAuraCheck4 extends Check {
	public KillAuraCheck4() {
		super();
	}
	@Override
	public CheckState check(Hacker hacker) {
		Entity entity = Utils.getClosestEntityToEntity(5f, hacker.player);
		if(entity == null) {
			return CheckState.RESET;
		}
		if(hacker.player.swingProgress < 0.2f && hacker.player.swingProgress != 0f) {
			float[] rots = Utils.getFacePosEntityRemote(hacker.player, entity);
			
			hacker.updateAccuracyList(Math.abs((hacker.player.rotationYaw - rots[0])));
			if(hacker.isAccuracyListUsable()) {
				float sum = 0f;
				for(float num : hacker.accuracyValues) {
					sum += num;
				}
				hacker.accuracyValue = sum / hacker.accuracyValues.size();
			}
			if(Math.abs((hacker.player.rotationYaw - rots[0])) < 30) {
				if(hacker.accuracyValue < 90 && hacker.accuracyValues.size() >= 50) {
					return CheckState.VIOLATION;
				}
			}
			else {
				return CheckState.RESET;
			}
		}
		return CheckState.RESET;
	}
	
	@Override
	public String getPrefix() {
		return " may be using ";
	}

	@Override
	public String getName() {
		return "KillAura/Aimbot (Accuracy)";
	}

	@Override
	public int getMaxViolations() {
		return 40;
	}
	@Override
	public int getDecayTime() {
		return 15000;
	}
}
