package tech.atani.client.feature.module.impl.combat;

import cn.muyang.nativeobfuscator.Native;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.MovingObjectPosition;
import tech.atani.client.listener.event.minecraft.input.ClickingEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.utility.player.combat.FightUtil;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.math.random.RandomUtil;
import tech.atani.client.utility.math.time.TimeHelper;
import tech.atani.client.utility.player.PlayerHandler;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;

@Native
@ModuleData(name = "KeepKB", description = "Helps you get people in combos", category = Category.COMBAT)
public class KeepKB extends Module {

    public StringBoxValue mode = new StringBoxValue("Mode", "Which mode will the module use?", this, new String[]{"Sprint Reset", "Legit", "Legit Fast", "Single Packet", "Normal Packet", "Double Packet"});
    public CheckBoxValue auraOnly = new CheckBoxValue("KillAura Only", "Operate only if the target is attacked by KillAura?", this, true);
    public SliderValue<Long> minDelay = new SliderValue<>("Minimum Delay", "What'll be the minimum delay between unsprinting?", this, 50L, 0L, 100L, 0);
    public SliderValue<Long> maxDelay = new SliderValue<>("Maximum Delay", "What'll be the maximum delay between unsprinting?", this, 60L, 0L, 100L, 0);

    private EntityLivingBase target;

    private boolean isHit = false;
    private TimeHelper attackTimer = new TimeHelper();

    private long delay = 0L;

    @Override
    public String getSuffix() {
    	return mode.getValue();
    }
    
    @Listen
    public final void onMotion(UpdateMotionEvent updateMotionEvent) {
        switch (mode.getValue()) {
            case "Legit":
                getGameSettings().keyBindSprint.pressed = true;

                if (isHit && attackTimer.hasReached(delay / 2)) {
                    isHit = false;
                    Methods.mc.thePlayer.setSprinting(false);
                }
                break;
            case "Legit Fast":
                if (isHit) {
                    Methods.mc.thePlayer.sprintingTicksLeft = 0;
                }
                break;
        }
    }

    @Listen
    public final void onAttack(ClickingEvent clickingEvent) {
        KillAura killAura = ModuleStorage.getInstance().getByClass(KillAura.class);
        boolean setTarget = false;
        if(!auraOnly.getValue() && Methods.mc.objectMouseOver != null && Methods.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && Methods.mc.objectMouseOver.entityHit != null && Methods.mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
            target = (EntityLivingBase) Methods.mc.objectMouseOver.entityHit;
            setTarget = true;
        }
        if(killAura.isEnabled() && KillAura.curEntity != null && FightUtil.getRange(KillAura.curEntity) <= ModuleStorage.getInstance().getByClass(KillAura.class).attackRange.getValue().floatValue()) {
            target = killAura.curEntity;
            setTarget = true;
        }
        if(!setTarget)
            target = null;
        if(target != null) {
            switch (mode.getValue()) {
                case "Legit":
                case "Legit Fast":
                    if (!isHit) {
                        isHit = true;
                        attackTimer.reset();
                        delay = (long) RandomUtil.randomBetween(minDelay.getValue().longValue(), maxDelay.getValue().longValue());
                    }
                    break;
                case "Sprint Reset":
                    PlayerHandler.shouldSprintReset = true;
                    break;
                case "Single Packet":
                    if (Methods.mc.thePlayer.isSprinting()) {
                        Methods.mc.thePlayer.setSprinting(false);
                    }
                    Methods.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(Methods.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    Methods.mc.thePlayer.serverSprintState = true;
                    break;
                case "Normal Packet":
                    Methods.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Methods.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    Methods.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Methods.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    Methods.mc.thePlayer.serverSprintState = true;
                    break;
                case "Double Packet":
                    Methods.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Methods.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    Methods.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Methods.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    Methods.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Methods.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    Methods.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Methods.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    Methods.mc.thePlayer.serverSprintState = true;
                    break;
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
