package artemis.mods.Movement;

import artemis.Category;
import artemis.module;
import artemis.util.MoveUtil;

public class CubecraftLongJump extends module{

	public CubecraftLongJump() {
		super("Cubecarft LongJump", "Jumps long", 0, 0, Category.MOVEMENT);
	}
	protected boolean boosted = false;
	protected double startY = 0;
	protected MoveUtil move;
	protected double motionVa = 2.8;
					
	@Override
	public void onUpdate() {
          if(this.isToggled()) {
		this.startY = this.mc.thePlayer.posY;

		// Damage
		this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 4, this.mc.thePlayer.posZ);
          
		
		cubecraft();
		
		super.onUpdate();
	}
	}
				
	@Override
	public void onDisable() {
		this.boosted = false;
		this.mc.timer.timerSpeed = 1.0F;
		this.motionVa = 2.8;
		this.mc.thePlayer.motionY = 0;
		super.onDisable();
	}
				
	protected void cubecraft() {
			if (this.mc.gameSettings.keyBindForward.pressed) {

				

					if (!this.boosted) {

						if (this.mc.thePlayer.onGround) {

							this.mc.thePlayer.jump();
							
						} else {

							double x = this.move.instance.getPosForSetPosX(this.motionVa);
							double z = this.move.instance.getPosForSetPosZ(this.motionVa);

							if (this.mc.thePlayer.posY >= this.startY - 1.7) {

								this.mc.timer.timerSpeed = 0.2F;

								this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY,
										this.mc.thePlayer.posZ + z);

							} else {

								this.mc.timer.timerSpeed = 1.0F;

								this.toggle();

							}
						}

						this.boosted = true;

					}

					if (this.boosted) {

						if (this.motionVa >= 0.50) {
							this.motionVa = this.motionVa - 0.15;
						}

						this.boosted = false;

					}
				}
			}
		}


