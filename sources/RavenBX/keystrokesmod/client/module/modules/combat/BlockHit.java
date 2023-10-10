package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.modules.world.AntiBot;
import keystrokesmod.client.module.setting.impl.DescriptionSetting;
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

public class BlockHit extends Module {
    public static SliderSetting range;
    public static DescriptionSetting eventTypeDesc;
    public static TickSetting onlyPlayers, onRightMBHold;
    private CoolDown actionTimer = new CoolDown(0);
    private boolean waitingForAction;
    private int hitCount;
    private boolean isBlocking;

    public BlockHit() {
        super("BlockHit", ModuleCategory.combat);

        this.registerSetting(onlyPlayers = new TickSetting("Only combo players", true));
        this.registerSetting(onRightMBHold = new TickSetting("When holding down rmb", true));
        this.registerSetting(range = new SliderSetting("Range: ", 3, 1, 6, 0.05));
        this.registerSetting(eventTypeDesc = new DescriptionSetting("Mode: POST"));
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent e) {
        if (!Utils.Player.isPlayerInGame())
            return;

        if (onRightMBHold.isToggled() && !Utils.Player.tryingToCombo()) {
            if (Utils.Player.isPlayerHoldingWeapon() && Mouse.isButtonDown(0)) {
                finishCombo();
            }
            return;
        }

        if (waitingForAction) {
            if (actionTimer.hasFinished()) {
                startCombo();
                waitingForAction = false;
                actionTimer.start();
            }
            return;
        }

        if (Utils.Player.tryingToCombo()) {
            if (mc.objectMouseOver == null || mc.objectMouseOver.entityHit == null) {
                if (Utils.Player.isPlayerHoldingWeapon() && Mouse.isButtonDown(0)) {
                    finishCombo();
                }
                return;
            } else {
                Entity target = mc.objectMouseOver.entityHit;
                if (target.isDead) {
                    if (Utils.Player.isPlayerHoldingWeapon() && Mouse.isButtonDown(0)) {
                        finishCombo();
                    }
                    return;
                }
            }
        }

        if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof Entity && Mouse.isButtonDown(0)) {
            Entity target = mc.objectMouseOver.entityHit;
            if (target.isDead) {
                if (onRightMBHold.isToggled() && Mouse.isButtonDown(1) && Mouse.isButtonDown(0)) {
                    if (Utils.Player.isPlayerHoldingWeapon() && Mouse.isButtonDown(0)) {
                        finishCombo();
                    }
                }
                return;
            }

            if (mc.thePlayer.getDistanceToEntity(target) <= range.getInput()) {
                if ((target.hurtResistantTime >= 10 && Utils.Modes.SprintResetTimings.values()[1] == Utils.Modes.SprintResetTimings.POST) || (target.hurtResistantTime <= 10 && Utils.Modes.SprintResetTimings.values()[1] == Utils.Modes.SprintResetTimings.PRE)) {

                    if (onlyPlayers.isToggled()) {
                        if (!(target instanceof EntityPlayer)) {
                            return;
                        }
                    }

                    if (AntiBot.bot(target)) {
                        return;
                    }

                    if (hitCount < 2) {
                        hitCount++;
                        guiUpdate();

                        actionTimer.setCooldown(20L);
                        waitingForAction = true;
                    } else {
                        finishCombo();
                    }
                }
            }
        }
    }

    private void finishCombo() {
        int key = mc.gameSettings.keyBindUseItem.getKeyCode();
        KeyBinding.setKeyBindState(key, false);
        Utils.Client.setMouseButtonState(1, false);
        isBlocking = false;
        hitCount = 0;
    }

    private void startCombo() {
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
            int key = mc.gameSettings.keyBindUseItem.getKeyCode();
            KeyBinding.setKeyBindState(key, true);
            KeyBinding.onTick(key);
            Utils.Client.setMouseButtonState(1, true);
            isBlocking = true;
            new Thread(() -> {
                try {
                    Thread.sleep(60); // Adjust this value to control holding time
                    finishCombo();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void guiUpdate() {
        eventTypeDesc.setDesc(Utils.md + Utils.Modes.SprintResetTimings.values()[1]);
    }

    @SubscribeEvent
    public void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
        if (isBlocking && !Mouse.isButtonDown(1)) {
            finishCombo();
        }
    }
}
