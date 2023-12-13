package me.aidanmees.trivia.hackerdetect;

import java.util.ArrayList;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.modules.Hidden.HackerDetect;
import me.aidanmees.trivia.client.modules.Hidden.Ping;
import me.aidanmees.trivia.gui.Level;
import me.aidanmees.trivia.gui.Notification;
import me.aidanmees.trivia.hackerdetect.checks.AntiKBCheck;
import me.aidanmees.trivia.hackerdetect.checks.Check;
import me.aidanmees.trivia.hackerdetect.checks.CheckState;
import me.aidanmees.trivia.hackerdetect.checks.KillAuraCheck;
import me.aidanmees.trivia.hackerdetect.checks.KillAuraCheck3;
import me.aidanmees.trivia.hackerdetect.checks.KillAuraCheck4;
import net.minecraft.entity.player.EntityPlayer;

public class Hacker {

	public EntityPlayer player;
	public ArrayList<Check> checks = new ArrayList<Check>();
	public int maxAps = 0;
	public double maxYawrate = 0;
	boolean didIntercept = false;

	public Hacker(EntityPlayer player) {
		this.player = player;
		checks.add(new AntiKBCheck());
		checks.add(new KillAuraCheck());
		checks.add(new KillAuraCheck3());
		checks.add(new KillAuraCheck4());
	}

	public void updateEnabledChecks() {
		for (Check check : HackerDetect.checks) {
			Check found = HackerDetect.getCheck(this.player.getName(), check.getName());
			found.setEnabled(check.isEnabled());
		}
	}

	public void doChecks() {
		updateEnabledChecks();
		maxAps = Math.max(maxAps, player.aps);
		maxYawrate = Math.max(maxYawrate, Math.abs(player.rotationYaw - player.prevRotationYaw));
		for (Check check : this.checks) {
			if (!check.isEnabled()) {
				continue;
			}
			if (Ping.timer.getTime() < 200) {
				if (didIntercept) {
					didIntercept = false;
					return;
				}
				CheckState state = check.check(this);
				if (state == CheckState.VIOLATION) {
					check.timer.reset();
					check.tempViolations++;
				} else if(state == CheckState.RESET) {
					if (check.timer.hasTimeElapsed(check.getDecayTime(), false)) {
						check.tempViolations = 0;
					}
				}
				else if(state == CheckState.IDLE) {
					
				}

			} else {
				didIntercept = true;
			}
			if (check.tempViolations >= check.getMaxViolations()) {
				check.violate();
				if (!HackerDetect.muted.contains(player.getName())) {
					if (check.getMentionName()) {
						trivia.getNotificationManager().addNotification(new Notification(Level.WARNING, 
								"Player " + "" + player.getName() + check.getPrefix() + check.getName() + " vl=(" + check.getViolations() + ")"));
					} else {
						trivia.getNotificationManager().addNotification(new Notification(Level.WARNING, 
								"Player " + "" + player.getName() + check.getPrefix() + " vl=(" + check.getViolations() + ")"));
					}

				}
			}
		}
	}

	public int getViolations() {
		int i = 0;
		for (Check check : checks) {
			if (!check.isEnabled()) {
				continue;
			}
			i += check.getViolations();
		}
		return i;
	}

}
