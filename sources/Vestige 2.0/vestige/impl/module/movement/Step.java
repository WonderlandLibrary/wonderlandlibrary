package vestige.impl.module.movement;

import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import vestige.api.event.Listener;
import vestige.api.event.impl.PreStepEvent;
import vestige.api.event.impl.StepEvent;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.BooleanSetting;
import vestige.api.setting.impl.ModeSetting;
import vestige.api.setting.impl.NumberSetting;
import vestige.util.network.PacketUtil;

@ModuleInfo(name = "Step", category = Category.MOVEMENT)
public class Step extends Module {
	
	private final ModeSetting mode = new ModeSetting("Mode", this, "NCP", "Vanilla", "NCP", "Hypixel", "LegitOffsets");
	
	private final NumberSetting vanillaHeight = new NumberSetting("vanilla-height", this, 1, 1, 9, 0.5, false) {
		@Override
		public boolean isShown() {
			return mode.is("Vanilla");
		}
		
		@Override
		public String getDisplayName() {
			return "Height";
		}
	};
	
	private final NumberSetting ncpHeight = new NumberSetting("vanilla-height", this, 1, 1, 2.5, 0.5, false) {
		@Override
		public boolean isShown() {
			return mode.is("NCP");
		}
		
		@Override
		public String getDisplayName() {
			return "Height";
		}
	};
	
	private final NumberSetting timer = new NumberSetting("Timer", this, 1, 0.1, 1, 0.1, false) {
		@Override
		public boolean isShown() {
			return !mode.is("NCP") || !timerAdjustment.isEnabled();
		}
	};
	
	private final BooleanSetting timerAdjustment = new BooleanSetting("Timer adjustment", this, false) {
		@Override
		public boolean isShown() {
			return mode.is("NCP");
		}
	};
	
	private boolean doneTimer;
	private boolean prevOffGround;
	
	public Step() {
		this.registerSettings(mode, vanillaHeight, ncpHeight, timerAdjustment, timer);
	}
	
	public void onEnable() {
		doneTimer = true;
	}

	public void onDisable() {
		mc.thePlayer.stepHeight = 0.6F;
	}

	@Listener
	public void onUpdate(UpdateEvent event) {
		this.setSuffix(mode.getMode());

		switch(mode.getMode()) {
			case "Vanilla":
				mc.thePlayer.stepHeight = (float) vanillaHeight.getCurrentValue();
				break;
			case "NCP":
				mc.thePlayer.stepHeight = (float) ncpHeight.getCurrentValue();
				break;
			case "Hypixel":
				mc.thePlayer.stepHeight = 1.01F;
				break;
			case "LegitOffsets":
				mc.thePlayer.stepHeight = 1.01F;
				break;
		}

		if(!doneTimer) {
			mc.timer.timerSpeed = 1F;
			doneTimer = true;
		}

		if(!mc.thePlayer.onGround) {
			prevOffGround = true;
		}
	}

	@Listener
	public void onPreStep(PreStepEvent e) {
		if(mc.thePlayer.onGround && prevOffGround) {
			if(e.getHeight() > 0.6) {
				e.setHeight(0.6F);
			}
			prevOffGround = false;
		}
	}

	@Listener
	public void onStep(StepEvent e) {
		double[] values = new double[0];
		double height = e.getHeight();

		boolean randomisedValues = false;

		if(height > 0.6) {
			mc.timer.timerSpeed = (float) timer.getCurrentValue();
			doneTimer = false;
		}

		switch(mode.getMode()) {
			case "NCP":
				if (height > 2.019) {
                    values = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.919};
                } else if (height > 1.869) {
                    values = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869};
                } else if (height > 1.5) {
                    values = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652};
                } else if(height > 1.015) {
                	values = new double[] {0.41999998688698, 0.7531999805212, 1.00133597911215, 1.166109260938214, 1.24918707874468, 1.170787077218804};
				} else if(height > 0.875) {
					values = new double[] {0.41999998688698, 0.7531999805212};
				} else if(height > 0.6) {
					values = new double[] {0.39, 0.6938};
				}
				
				if(timerAdjustment.isEnabled()) {
					if (height > 2.019) {
	                    mc.timer.timerSpeed = (float) Math.min(timer.getCurrentValue(), 0.1);
	                } else if (height > 1.869) {
	                    mc.timer.timerSpeed = (float) Math.min(timer.getCurrentValue(), 0.12);
	                } else if(height > 1.5) {
						mc.timer.timerSpeed = (float) Math.min(timer.getCurrentValue(), 0.14);
					} else if(height > 1.015) {
						mc.timer.timerSpeed = (float) Math.min(timer.getCurrentValue(), 0.2);
					} else if(height > 0.6) {
						mc.timer.timerSpeed = (float) Math.min(timer.getCurrentValue(), 0.5);
					}
				}
				break;
			case "Hypixel":
				if(height > 0.875) {
					values = new double[]{0.41999998688698, 0.7531999805212, 1.00133597911215};
				} else if(height > 0.6) {
					values = new double[]{0.39, 0.6938};
				}
				break;
			case "LegitOffsets":
				if(height > 0.6) {
					values = new double[] {0.41999998688698, 0.7531999805212, 1.00133597911215, 1.166109260938214, 1.24918707874468, 1.170787077218804, 1.015555072702206};
				}
				break;
		}

		if(values.length > 0) {
			if(randomisedValues) {
				for (final double y : values) {
					PacketUtil.sendPacket(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + (y + Math.random() / 500), mc.thePlayer.posZ, false));
				}
			} else {
				for(final double y : values) {
					PacketUtil.sendPacket(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + y, mc.thePlayer.posZ, false));
				}
			}
		}
	}

}
