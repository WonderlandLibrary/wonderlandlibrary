package tech.atani.client.feature.module.impl.movement;

import cn.muyang.nativeobfuscator.Native;
import com.google.common.base.Supplier;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.stats.StatList;
import tech.atani.client.listener.event.minecraft.player.movement.StepEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.math.time.TimeHelper;
import tech.atani.client.utility.player.movement.MoveUtil;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;

@Native
@ModuleData(name = "Step", description = "Makes you walk up blocks.", category = Category.MOVEMENT)
public class Step extends Module {
    private final StringBoxValue mode = new StringBoxValue("Mode", "Which mode will the module use?", this, new String[]{"Vanilla", "Intave", "NCP", "Motion", "Spartan", "WatchDog"});
    private final StringBoxValue ncpMode = new StringBoxValue("NCP Mode", "Which mode will the NCP mode use?", this, new String[]{"Normal", "Fast", "Packet", "OldNCP"});
    private final SliderValue<Integer> height = new SliderValue<Integer>("Height", "How high will the step go?", this, 2, 0, 10, 1, new Supplier[]{() -> mode.is("Vanilla") || (mode.is("NCP") && ncpMode.is("OldNCP"))});
    private final SliderValue<Float> timer = new SliderValue<>("Timer", "How fast will the timer be?", this, 0.3F, 0.1F, 1.5F, 1);

    // Intave
    private boolean timered;

    // NCP
    private boolean hasStepped;

    // WatchDog
    private boolean step;

    //Smooth
    TimeHelper timerHelper = new TimeHelper();
    private boolean didStep;

    @Override
    public String getSuffix() {
    	return mode.getValue();
    }
    
    @Listen
    public final void onMotion(UpdateMotionEvent updateMotionEvent) {
        if (timerHelper.hasReached(100, true) && didStep) {
            mc.timer.timerSpeed = 1;
            didStep = false;
        }

        if (updateMotionEvent.getType() == UpdateMotionEvent.Type.MID) {
            switch(mode.getValue()) {
                case "WatchDog":
                    if(Methods.mc.thePlayer.onGround && Methods.mc.thePlayer.isCollidedHorizontally) {
                        Methods.mc.thePlayer.jump();
                        MoveUtil.setMoveSpeed(0.43d);
                        step = true;
                    }

                    if(!Methods.mc.thePlayer.onGround && !Methods.mc.thePlayer.isCollidedHorizontally && step) {
                        Methods.mc.thePlayer.motionY = -0.078;
                        MoveUtil.setMoveSpeed(0.45d);
                        step = false;
                    }
                    break;
                case "Intave":
                    if(timered) {
                        mc.timer.timerSpeed = 1;
                        timered = false;
                    }

                    Methods.mc.thePlayer.stepHeight = 0.6F;

                    if(Methods.mc.thePlayer.onGround) {
                        hasStepped = false;
                    }

                    if(!hasStepped && this.isMoving() &&  Methods.mc.thePlayer.onGround && Methods.mc.thePlayer.isCollidedHorizontally) {
                        mc.thePlayer.jump();
                        hasStepped = true;
                    } else {
                        if (!Methods.mc.thePlayer.isCollidedHorizontally && hasStepped && this.isMoving()) {
                            mc.timer.timerSpeed = 1.4F;
                            mc.thePlayer.motionY -= 0.0035;
                            timered = true;
                            hasStepped = false;
                        }
                    }


                    break;
                case "NCP":
                    if (ncpMode.is("Packet") || ncpMode.is("OldNCP")) break;
                    Methods.mc.thePlayer.stepHeight = 0.6F;

                    if(Methods.mc.thePlayer.onGround) {
                        hasStepped = false;
                    }

                    if(!hasStepped && this.isMoving() &&  Methods.mc.thePlayer.onGround && Methods.mc.thePlayer.isCollidedHorizontally) {
                        Methods.mc.thePlayer.jump();
                        hasStepped = true;
                        if(mc.thePlayer.moveForward > 0 && ncpMode.is("Fast")) {
                            MoveUtil.strafe(MoveUtil.getBaseMoveSpeed() + 0.197);
                        }
                    } else {
                        if (!Methods.mc.thePlayer.isCollidedHorizontally && hasStepped && this.isMoving()) {
                            hasStepped = false;
                            Methods.mc.thePlayer.motionY = 0;
                            if (Methods.mc.thePlayer.moveForward > 0) {
                                MoveUtil.setMoveSpeed(MoveUtil.getBaseMoveSpeed() + (ncpMode.is("Fast") ? 0.2 : 0));
                            }
                        }
                    }
                    break;
                case "Motion":
                    Methods.mc.thePlayer.stepHeight = 0.6F;

                    if(Methods.mc.thePlayer.isCollidedHorizontally && Methods.mc.thePlayer.onGround) {
                        Methods.mc.thePlayer.motionY = .39;
                    }
                    break;
            }
        }
    }

    @Listen
    public final void onStep(StepEvent stepEvent) {
        if (stepEvent.getState() == StepEvent.StepState.POST && stepEvent.getStepHeight() > 0.6f) {
            mc.timer.timerSpeed = timer.getValue();
            timerHelper.reset();
            didStep = true;
        }

        switch (mode.getValue()) {
            case "Vanilla":
                stepEvent.setStepHeight(height.getValue());
                break;
            case "Spartan":
                stepEvent.setStepHeight(1);
                break;
            case "NCP":
                if (!canStep()) break;

                if (stepEvent.getState() == StepEvent.StepState.PRE) {
                    if (ncpMode.is("Packet")) {
                        stepEvent.setStepHeight(1);
                    } else if (ncpMode.is("OldNCP")) {
                        stepEvent.setStepHeight(Math.max(2, height.getValue()));
                    }
                } else {
                    if (stepEvent.getStepHeight() < 1) return;

                    double[] packets = new double[0];
                    if (stepEvent.getStepHeight() == 1) {
                        packets = new double[] {0.41999998688698, 0.7531999805212};
                    } else if (stepEvent.getStepHeight() == 1.5) {
                        packets = new double[] {0.4, 0.75, 0.5, 0.41, 0.83, 1.16, 1.41999998688698};
                    } else if (stepEvent.getStepHeight() == 2) {
                        packets = new double[] {0.4, 0.75, 0.5, 0.41, 0.83, 1.16, 1.41999998688698, 1.57, 1.58, 1.42};
                    }

                    fakeJump();
                    for (double offset : packets) {
                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, true));
                    }

                    if (ncpMode.is("Packet")) {
                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + stepEvent.getStepHeight(), mc.thePlayer.posZ, true));
                    }

                }
                break;
        }
    }


    @Override
    public void onEnable() {
        step = false;
        didStep = false;
    }

    @Override
    public void onDisable() {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null) {
            return;
        }

        Methods.mc.thePlayer.stepHeight = 0.6F;
        step = false;
        Methods.mc.timer.timerSpeed = 1;
    }

    private boolean canStep() {
        return this.mc.thePlayer.isCollidedVertically && this.mc.thePlayer.onGround && this.mc.thePlayer.motionY < 0.0 && !this.mc.thePlayer.movementInput.jump;
    }

    private void fakeJump() {
        mc.thePlayer.isAirBorne = true;
        mc.thePlayer.triggerAchievement(StatList.jumpStat);
    }
}
