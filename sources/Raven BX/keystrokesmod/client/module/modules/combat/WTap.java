package keystrokesmod.client.module.modules.combat;

import com.google.common.eventbus.Subscribe;
import keystrokesmod.client.event.impl.ForgeEvent;
import keystrokesmod.client.event.impl.Render2DEvent;
import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.setting.impl.ComboSetting;
import keystrokesmod.client.module.setting.impl.DoubleSliderSetting;
import keystrokesmod.client.module.setting.impl.SliderSetting;
import keystrokesmod.client.module.setting.impl.TickSetting;
import keystrokesmod.client.utils.CoolDown;
import keystrokesmod.client.utils.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import org.lwjgl.input.Keyboard;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class WTap extends Module {
    public ComboSetting eventType;
    public SliderSetting range, chance, tapMultiplier;
    public TickSetting onlyPlayers;
    public TickSetting onlySword;
    public TickSetting dynamic;
    public DoubleSliderSetting waitMs;
    public DoubleSliderSetting actionMs;
    public DoubleSliderSetting hitPer;
    public int hits, rhit;
    public boolean call, p;
    public long s;
    private WtapState state = WtapState.NONE;
    private final CoolDown timer = new CoolDown(0);
    private Entity target;

    public Random r = new Random();

    // New setting for SpeedCheck
    public TickSetting speedCheck;

    public WTap() {
        super("WTap", ModuleCategory.combat);

        this.registerSetting(eventType = new ComboSetting("Event:", EventType.Attack));
        this.registerSetting(onlyPlayers = new TickSetting("AutoCombo", true));
        this.registerSetting(onlySword = new TickSetting("Only sword", false));

        this.registerSetting(waitMs = new DoubleSliderSetting("ActionTime", 25, 45, 1, 500, 1));
        this.registerSetting(actionMs = new DoubleSliderSetting("Delay", 20, 30, 1, 500, 1));
        //this.registerSetting(hitPer = new DoubleSliderSetting("Once every ... hits", 1, 1, 1, 10, 1));
        //this.registerSetting(chance = new SliderSetting("Chance %", 100, 0, 100, 1));
        //this.registerSetting(range = new SliderSetting("Range: ", 3, 1, 6, 0.05));

        this.registerSetting(dynamic = new TickSetting("Dynamic tap time", false));
        this.registerSetting(tapMultiplier = new SliderSetting("wait time sensitivity", 1F, 0F, 5F, 0.1F));

        // Add the new SpeedCheck setting
        this.registerSetting(speedCheck = new TickSetting("SpeedCheck", false));
    }

    @Subscribe
    public void onRender2D(Render2DEvent e) {
        if (state == WtapState.NONE)
            return;
        if (state == WtapState.WAITINGTOTAP && timer.hasFinished()) {
            startCombo(target);
        } else if (state == WtapState.TAPPING && timer.hasFinished()) {
            finishCombo();
        }
    }

    @Subscribe
    public void onForgeEvent(ForgeEvent fe) {
        if (fe.getEvent() instanceof AttackEntityEvent) {
            AttackEntityEvent e = ((AttackEntityEvent) fe.getEvent());

            target = e.target;

            if (isSecondCall() && eventType.getMode() == EventType.Attack) {
                wTap();
            }
        } else if (fe.getEvent() instanceof LivingUpdateEvent) {
            LivingUpdateEvent e = ((LivingUpdateEvent) fe.getEvent());

            if (eventType.getMode() == EventType.Hurt && e.entityLiving.hurtTime > 0
                    && e.entityLiving.hurtTime == e.entityLiving.maxHurtTime && e.entity == this.target) {
                wTap();
            }
        }
    }
    public static boolean isPlayerHoldingSword() {
        return (mc.thePlayer.getCurrentEquippedItem() != null)
                && (mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword);
    }
    public void wTap() {
        if (state != WtapState.NONE) {
            return;
        }

        if (!(Math.random() <= chance.getInput() / 100)) {
            hits++;
        }

        // New check: Automatically combo players if the "onlyPlayers" setting is enabled.
        if (onlyPlayers.isToggled() && target instanceof EntityPlayer) {
            trystartCombo();
            return;
        }

        double playerDistance = mc.thePlayer.getDistanceToEntity(target);
        if (playerDistance > range.getInput()
                || (onlyPlayers.isToggled() && !(target instanceof EntityPlayer))
                || (onlySword.isToggled() && !isPlayerHoldingSword())
                || !(rhit >= hits)) {
            return;
        }
//this thing works so good i am really good :plague:

        // Check if the player is getting closer than 2.7 blocks
        if (speedCheck.isToggled() && isPlayerStrafing()) {
            if (playerDistance < 2.7) {
                // Check if the player's speed is faster than normal walking speed
                if (mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ > 0.15 * 0.15) {
                    // Increase the wait time by 100 milliseconds (adjust this value as needed)
                    double newMaxWaitMs = waitMs.getMax() + 15;
                    double newMinWaitMs = waitMs.getMin() + 10;
                    double newMaxActionMs = actionMs.getMax() + 25;
                    double newMinActionMs = actionMs.getMin() + 20;
                    //waitMs = new DoubleSliderSetting("ActionTime", newMinWaitMs, newMaxWaitMs, 1, 500, 1);
                    //this.registerSetting(waitMs);
                }

            }


            trystartCombo();
        }
    }
//i was the best among the worst (plague)
    public boolean isPlayerStrafing() {
        return Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())
                || Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode());
    }
        public void finishCombo() {
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
        }
        state = WtapState.NONE;
        hits = 0;
        int easports = (int) (hitPer.getInputMax() - hitPer.getInputMin() + 1);
        rhit = ThreadLocalRandom.current().nextInt((easports));
        rhit += (int) hitPer.getInputMin();
    }

    public void startCombo(Entity target) {

        state = WtapState.TAPPING;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);

        double cd = ThreadLocalRandom.current().nextDouble(waitMs.getInputMin(), waitMs.getInputMax() + 0.01);

        // Check if SpeedCheck is enabled and apply wait time multiplier accordingly
        if (speedCheck.isToggled()) {
            cd *= 2;
        }

        if (dynamic.isToggled() && target != null) {
            double distanceToTarget = mc.thePlayer.getDistanceToEntity(target);

            if (distanceToTarget < 3) {
                cd += (3 - distanceToTarget) * tapMultiplier.getInput() * 10;
            }
        }

        // Set the cooldown timer
        timer.setCooldown((long) cd);
        timer.start();
    }
    private long lastClickTime = 0;
    public void trystartCombo() {
        state = WtapState.WAITINGTOTAP;

        long timeSinceLastClick = System.currentTimeMillis() - lastClickTime;
        double clickSpeedMultiplier = 1.0;
        if (timeSinceLastClick > 0) {
            clickSpeedMultiplier = 1000.0 / timeSinceLastClick;
        }

        // Adjust the combo timer based on the player's click speed and the "ActionMs" setting
        double adjustedActionMs = actionMs.getInputMin() / clickSpeedMultiplier;
        double adjustedActionMsMax = actionMs.getInputMax() / clickSpeedMultiplier;
        double adjustedWaitMs = waitMs.getInputMin();
        double adjustedWaitMsMax = waitMs.getInputMax();

        // Check if SpeedCheck is enabled and apply wait time multiplier accordingly
        if (speedCheck.isToggled()) {
            adjustedActionMs *= 2;
            adjustedActionMsMax *= 2;
            adjustedWaitMs *= 2;
            adjustedWaitMsMax *= 2;
        }

        timer.setCooldown((long) ThreadLocalRandom.current().nextDouble(adjustedWaitMs, adjustedWaitMsMax + 0.01));
        timer.start();
        lastClickTime = System.currentTimeMillis();
    }

    private boolean isSecondCall() {
        if (call) {
            call = false;
            return true;
        } else {
            call = true;
            return false;
        }
    }

    public enum EventType {
        Attack, Hurt,
    }

    public enum WtapState {
        NONE, WAITINGTOTAP, TAPPING
    }
}
