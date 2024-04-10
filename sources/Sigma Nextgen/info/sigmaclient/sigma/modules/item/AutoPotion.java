
package info.sigmaclient.sigma.modules.item;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.AttackEvent;
import info.sigmaclient.sigma.event.player.ClickEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class AutoPotion extends Module {
    private final NumberValue delay = new NumberValue("Delay", 0, 0, 20, NumberValue.NUMBER_TYPE.INT);
    private final NumberValue health = new NumberValue("Health", 10, 0, 20, NumberValue.NUMBER_TYPE.INT);
    private final BooleanValue instant = new BooleanValue("Instant", false);
    private final BooleanValue speed = new BooleanValue("Speed", false);
    private final BooleanValue regen = new BooleanValue("Regen", false);
    private final BooleanValue inFight = new BooleanValue("InFight", true);
    private final BooleanValue ground = new BooleanValue("Ground", true);
    public AutoPotion() {
        super("AutoPotion", Category.Item, "Automatically throws potion to regen and speed up");
        registerValue(delay);
        registerValue(health);
        registerValue(instant);
        registerValue(speed);
        registerValue(regen);
        registerValue(inFight);
        registerValue(ground);
    }
    int oldSlot = -1, delayT = 0, Fticks = 0;
    boolean reset = false, click = false;
    @Override
    public void onEnable()
    {
        Fticks = 0;
        click = false;
    }
    public void no(){
        if(oldSlot != -1){
            mc.player.inventory.currentItem = oldSlot;
            mc.gameSettings.keyBindUseItem.pressed = false;
            oldSlot = -1;
        }
    }

    @Override
    public void onUpdateEvent(UpdateEvent e) {
        if (e.isPost()) return;
        PlayerInventory inventory = mc.player.inventory;
        int nextTotemSlot = searchForTotems(inventory);
        if (nextTotemSlot == -1) {
            no();
            return;
        }
        if(Fticks > 0) Fticks --;
        if(inFight.isEnable()){
            if(Fticks == 0){
                delayT--;
                no();
                return;
            }
        }
        if (delayT > 0) {
            delayT--;
            no();
            return;
        }
        if (!mc.player.onGround) {
            if(ground.isEnable()) {
                no();
                return;
            }
        }
        if (mc.player.inventory.currentItem != nextTotemSlot) {
            oldSlot = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = nextTotemSlot;
        }
        click = true;
        e.forcePitch = 90;
        e.changeForce = true;
        delayT = delay.getValue().intValue();
    }

    @Override
    public void onAttackEvent(AttackEvent event) {
        if(event.post) return;
        if(inFight.isEnable()){
            Fticks = 60;
        }
        super.onAttackEvent(event);
    }

    @Override
    public void onClickEvent(ClickEvent event) {
        if(click){
            click = false;
            mc.rightClickDelayTimer = 0;
            mc.rightClickMouse();
        }
    }

    private int searchForTotems(PlayerInventory inventory)
    {
        for(int slot = 0; slot < 9; slot++)
        {
            if(!isNeed(inventory.getStackInSlot(slot)))
                continue;
            return slot;
        }
        return -1;
    }

    private boolean isNeed(ItemStack stack) {
        if (stack.getItem() instanceof SplashPotionItem) {
            for (EffectInstance effect : PotionUtils.getEffectsFromStack(stack)) {
                if (instant.isEnable() && effect.getPotion().equals(Effects.INSTANT_HEALTH) && mc.player.getHealth() <= health.getValue().intValue()) {
                    return true;
                }
                if (speed.isEnable() && effect.getPotion().equals(Effects.SPEED) && !mc.player.isPotionActive(Effects.SPEED)) {
                    return true;
                }
                if (regen.isEnable() && effect.getPotion().equals(Effects.REGENERATION) && mc.player.getHealth() <= health.getValue().intValue()) {
                    return true;
                }
            }
        }
        return false;
    }

}
