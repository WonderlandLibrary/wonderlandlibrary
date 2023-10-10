package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.modules.world.AntiBot;
import keystrokesmod.client.module.setting.impl.DescriptionSetting;
import keystrokesmod.client.module.setting.impl.DoubleSliderSetting;
import keystrokesmod.client.module.setting.impl.SliderSetting;
import keystrokesmod.client.module.setting.impl.TickSetting;
import keystrokesmod.client.utils.CoolDown;
import keystrokesmod.client.utils.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.concurrent.ThreadLocalRandom;

public class wTap2 extends Module {
    public static DescriptionSetting eventTypeDesc;
    public static TickSetting onlyPlayers;
    public static SliderSetting eventType;
    public static DoubleSliderSetting actionTicks, onceEvery, postDelay; // Added eventType
    public static boolean comboing, hitCoolDown, alreadyHit, waitingForPostDelay;
    public static int hitTimeout, hitsWaited;
    public static CoolDown actionTimer = new CoolDown(0), postDelayTimer = new CoolDown(0);
    private boolean waitingToMoveForward = false;
    private CoolDown waitCooldown = new CoolDown(0);

    public wTap2() {
        super("WTap", ModuleCategory.combat);
        this.registerSetting(onlyPlayers = new TickSetting("Only combo players", true));
        this.registerSetting(actionTicks = new DoubleSliderSetting("Action Time (MS)", 25, 55, 1, 500, 1));
        this.registerSetting(onceEvery = new DoubleSliderSetting("Once every ... hits", 1, 1, 1, 10, 1));
        this.registerSetting(postDelay = new DoubleSliderSetting("Post delay (MS)", 25, 55, 1, 500, 1));
        this.registerSetting(eventType = new SliderSetting("Value: ", 2, 1, 2, 1)); // Added eventType
        this.registerSetting(eventTypeDesc = new DescriptionSetting("Mode: POST"));
    }

    public void guiUpdate() {
        eventTypeDesc.setDesc(Utils.md + Utils.Modes.SprintResetTimings.values()[(int) eventType.getInput() - 1]);
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent e) {
        if (!Utils.Player.isPlayerInGame() || !Mouse.isButtonDown(0))
            return;

        if (waitingForPostDelay) {
            if (postDelayTimer.hasFinished()) {
                waitingForPostDelay = false;
                comboing = true;
                startCombo();
                actionTimer.start();
            }
            return;
        }

        if (comboing) {
            if (actionTimer.hasFinished()) {
                comboing = false;
                finishCombo();
                return;
            } else {
                return;
            }
        }

        Entity target = mc.objectMouseOver.entityHit;
        if (target == null || target.isDead)
            return;

        double reach = Reach.reach.getInputMin(); // Define your reach distance here

        if (mc.thePlayer.getDistanceToEntity(target) <= reach) {
                if (onlyPlayers.isToggled() && !(target instanceof EntityPlayer))
                    return;

            boolean isStrafing = mc.thePlayer.movementInput.moveStrafe != 0.0F || mc.thePlayer.movementInput.moveForward != 0.0F;

            if (!waitingToMoveForward) {
                if (isStrafing && mc.thePlayer.getDistanceToEntity(target) <= Reach.reach.getInputMax()) {
                    waitingToMoveForward = true;
                    waitCooldown.setCooldown(actionTimer.getElapsedTime());
                    return;
                }
            } else {
                if (waitCooldown.hasFinished()) {
                    waitingToMoveForward = false;
                } else {
                    return; // Wait before moving forward again
                }
            }

                if (AntiBot.bot(target))
                    return;

                if (hitCoolDown && !alreadyHit) {
                    hitsWaited++;
                    if (hitsWaited >= hitTimeout) {
                        hitCoolDown = false;
                        hitsWaited = 0;
                    } else {
                        alreadyHit = true;
                        return;
                    }
                }

                if (!alreadyHit) {
                    guiUpdate();
                    if (onceEvery.getInputMin() == onceEvery.getInputMax()) {
                        hitTimeout = (int) onceEvery.getInputMin();
                    } else {
                        hitTimeout = ThreadLocalRandom.current().nextInt((int) onceEvery.getInputMin(), (int) onceEvery.getInputMax());
                    }
                    hitCoolDown = true;
                    hitsWaited = 0;

                    actionTimer.setCooldown((long) ThreadLocalRandom.current().nextDouble(actionTicks.getInputMin(), actionTicks.getInputMax() + 0.01));

                    if (postDelay.getInputMax() != 0) {
                        postDelayTimer.setCooldown((long) ThreadLocalRandom.current().nextDouble(postDelay.getInputMin(), postDelay.getInputMax() + 0.01));
                        postDelayTimer.start();
                        waitingForPostDelay = true;
                    } else {
                        comboing = true;
                        startCombo();
                        actionTimer.start();
                        alreadyHit = true;
                    }

                    alreadyHit = true;

                }
            } else if (alreadyHit) {
                alreadyHit = false;
            }
        }

    private static void finishCombo() {
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
        }
    }

    private static void startCombo() {
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
            KeyBinding.onTick(mc.gameSettings.keyBindForward.getKeyCode());
        }
    }
}
