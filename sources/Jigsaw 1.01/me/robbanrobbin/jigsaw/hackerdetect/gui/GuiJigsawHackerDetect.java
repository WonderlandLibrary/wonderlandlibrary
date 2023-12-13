package me.robbanrobbin.jigsaw.hackerdetect.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.input.Mouse;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.modules.HackerDetect;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.font.Fonts;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import me.robbanrobbin.jigsaw.hackerdetect.Hacker;
import me.robbanrobbin.jigsaw.hackerdetect.checks.Check;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;

public class GuiJigsawHackerDetect extends GuiScreen {
	private GuiScreen before;
	public Hacker selected;
	public int scroll;

	private ArrayList<Hacker> savedSortedList = new ArrayList<Hacker>();

	public GuiJigsawHackerDetect(GuiScreen before) {
		this.before = before;
	}

	public void initGui() {
		int left = 4;
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(1, 205 - 200 - left, height - 21, 70, 20, "Reset All", true));
		left += 4;
//		this.buttonList.add(new GuiButton(3, 280 - 200 - left, height - 21, 70, 20, "Auto Notify", true));
//		left += 4;
//		this.buttonList.add(new GuiButton(4, 355 - 200 - left, height - 21, 80, 20, "Denotify All", true));
//		left += 4;
//		this.buttonList.add(new GuiButton(2, 440 - 200 - left, height - 21, 100, 20, "Notify Selected", true));
//		left += 4;
//		this.buttonList.add(new GuiButton(5, 545 - 200 - left, height - 21, 77, 20, "More Info", true));
//		left += 4;
		this.buttonList.add(new GuiButton(6, width - 51, height - 21, 50, 20, "Back", true));
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		{
			
			GuiUtils.drawBlurBuffer(true);
			
			drawRect(0, 0, width, 27, 0xcf000000);
			
			drawRect(0, 27, 200, height - 22, 0xcf000000); // sidebar with all
															// players
			
			drawRect(200, 27, width, height - 22, 0xdf000000); // big area to
																// the right
			
			drawRect(0, height - 22, width, height, 0xef000000); // bar below
																	// everything
																	// with the
																	// buttons

			String s = HackerDetect.getLoadedPlayers() + " players loaded §8|§r " + HackerDetect.hackers.size()
					+ " players in total";
			Fonts.font18.drawCenteredString("§jJig§8saw§r§7§o Advanced §rHackerDetect", width / 2, 2, 0xffffffff);
			Fonts.font55.drawCenteredString(s, width / 2, 10, 0xffdfdfdf);

			Fonts.font55.drawString("Loaded Players", 2, 11, 0xffffffff);

			drawHorizontalLine(0, width, 27, 0xffffffff);

			ArrayList<Hacker> list = new ArrayList<Hacker>();
			list.addAll(HackerDetect.hackers);
			if (Jigsaw.java8) {
				list.sort(new Comparator<Hacker>() {
					public int compare(Hacker o1, Hacker o2) {
						boolean loaded = o1.loaded;
						if (o1.getViolations() > o2.getViolations()) {
							return -1;
						}
						if (o1.getViolations() < o2.getViolations()) {
							return 1;
						}
						return 0;
					};
				});
			}

			savedSortedList = list;

			int y = 28 - scroll;
			for (Hacker hacker : list) {
				boolean loaded = hacker.loaded;
				boolean hovered = mouseY > y && mouseY <= y + 13 && mouseX < 200;
				boolean mouseDown = Mouse.isButtonDown(0);

				if (hovered && mouseDown) {
					selected = hacker;
				}

				if (loaded) {
					drawRect(0, y, 200, y + 13, hovered ? 0x4aa0a0a0 : 0x2ea0a0a0);
				} else {
					drawRect(0, y, 200, y + 13, hovered ? 0x4a505050 : 0x2e000000);
				}

				String pre = (hacker.getViolations() > 0) ? "§c" : "";
				if (!loaded) {
					pre = (hacker.getViolations() > 0) ? "§4" : "§7";
				}
				Fonts.font18.drawStringWithShadow(pre + hacker.player.getName(), 2, y + 3, 0xffffffff);

				if (hovered && mouseDown) {
					drawRect(0, y, 200, y + 13, 0x3a000000);
				}

				y += 13;
			}
			if (selected != null) {
				int padding = 200;
				int infoY = 160;

				int violationY = 94;

				Fonts.font100.drawStringWithShadow(selected.player.getName(), padding + 10, 35, 0xffffffff);

				drawHorizontalLine(padding + 20, padding + Fonts.font100.getStringWidth(selected.player.getName()) - 5,
						67, 0xffffffff);

				Fonts.font55.drawString("Violations:", padding + 20, violationY - 20, 0xffffffff);

				for (Check check : selected.checks) {
					if (check.getViolations() == 0) {
						continue;
					}
					if (ClientSettings.hackerDetectMoreInfo) {
						Fonts.font18.drawStringWithShadow(
								"§7 - §r" + check.getName() + " §8 -§7 Violated §j" + check.getViolations()
										+ "§7 times! §8- §r" + check.tempViolations + "/" + check.getMaxViolations()
										+ " " + Math.min(check.timer.getTime(), check.getDecayTime()),
								padding + 20, violationY, 0xffffffff);
					} else {
						Fonts.font18.drawStringWithShadow("§7 - §r" + check.getName() + " §8 -§7 Violated §j"
								+ check.getViolations() + "§7 times!", padding + 20, violationY, 0xffffffff);
					}

					violationY += 10;
				}

				if (selected.isAccuracyListUsable()) {
					drawString(Fonts.font18, "Accuracy(0-360)" + ": §j" + selected.accuracyValue, padding + 20,
							infoY - 10, 0xffffffff);
				}

				drawString(Fonts.font18, "Max APS" + ": §j" + (int) Math.floor((selected.maxAps * 0.8)) + " §7- §j"
						+ (int) Math.ceil((selected.maxAps * 2)), padding + 20, infoY, 0xffffffff);

				drawString(Fonts.font18, "Current APS" + ": §j" + (int) Math.floor((selected.player.aps * 0.8))
						+ " §7- §j" + (int) Math.ceil((selected.player.aps * 2)), padding + 20, infoY + 10, 0xffffffff);

				drawString(Fonts.font18, "Max Yawrate" + ": §j" + (float) selected.maxYawrate, padding + 20, infoY + 20,
						0xffffffff);

				drawString(Fonts.font18,
						"Current Yawrate" + ": §j"
								+ (float) Math.abs(selected.player.rotationYaw - selected.player.prevRotationYaw),
						padding + 20, infoY + 30, 0xffffffff);
			}
		}
		for (int i = 0; i < this.buttonList.size(); ++i) {
			((GuiButton) this.buttonList.get(i)).drawButton(this.mc, mouseX, mouseY, mc.timer.renderPartialTicks);
		}

		for (int j = 0; j < this.labelList.size(); ++j) {
			((GuiLabel) this.labelList.get(j)).drawLabel(this.mc, mouseX, mouseY);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 1) {
			HackerDetect.hackers.clear();
			selected = null;
		}
		if (button.id == 2) {
			if (selected == null) {
				return;
			}
			selected.muted = !selected.muted;
		}
		if (button.id == 3) {
			ClientSettings.hackerDetectAutoNotify = !ClientSettings.hackerDetectAutoNotify;
			if (ClientSettings.hackerDetectAutoNotify) {
				HackerDetect.unmuteAll();
			}
		}
		if (button.id == 4) {
			HackerDetect.muteAll();
		}
		if (button.id == 5) {
			ClientSettings.hackerDetectMoreInfo = !ClientSettings.hackerDetectMoreInfo;
		}
		if (button.id == 6) {
			mc.displayGuiScreen(before);
		}
		super.actionPerformed(button);
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		if (i != 0) {
			if (i > 1) {
				i = 1;
			}

			if (i < -1) {
				i = -1;
			}
			i *= 20;
			if (scroll - i < 0) {
				return;
			}
			this.scroll -= i;
			// for (int ii = 0; ii < this.buttonList.size(); ++ii)
			// {
			// ((GuiButton)this.buttonList.get(ii)).yPosition += i;
			// }
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		// ArrayList<Hacker> list = new ArrayList<Hacker>();
		// list.addAll(HackerDetect.players.values());
		// if (Jigsaw.java8) {
		// list.sort(new Comparator<Hacker>() {
		// public int compare(Hacker o1, Hacker o2) {
		// if (o1.getViolations() > o2.getViolations()) {
		// return -1;
		// }
		// if (o1.getViolations() < o2.getViolations()) {
		// return 1;
		// }
		// return 0;
		// };
		// });
		// }
		// int i = 0;
		// for (Hacker hacker : list) {
		// if (mouseX > 0 && mouseX < 200 && mouseY > i - scroll && mouseY < i +
		// 20 - scroll) {
		// selected = hacker;
		// return;
		// }
		// i += 20;
		// if (i - scroll >= height - 30) {
		// break;
		// }
		// }
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		int y = 28;
		// for (Hacker hacker : savedSortedList) {
		// if (mouseX > 0 && mouseX < 200 && mouseY > y - scroll && mouseY < y +
		// 20 - scroll) {
		// selected = hacker;
		// return;
		// }
		// y+=13;
		// if (y - scroll >= height - 30) {
		// break;
		// }
		// }
		super.mouseReleased(mouseX, mouseY, state);
	}
}