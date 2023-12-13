package me.robbanrobbin.jigsaw.hackerdetect.checks;

import me.robbanrobbin.jigsaw.hackerdetect.Hacker;

public class HighAPS extends Check {

	@Override
	public CheckState check(Hacker en) {

		if (en.player.aps >= 10) {
			return CheckState.VIOLATION;
		}
		return CheckState.RESET;
	}

	@Override
	public String getName() {
		return "KillAura (Clicking too fast)";
	}

	@Override
	public int getMaxViolations() {
		return 80;
	}
	
	@Override
	public int getDecayTime() {
		return 10000;
	}
}
