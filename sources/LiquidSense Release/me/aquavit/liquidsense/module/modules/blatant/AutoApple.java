package me.aquavit.liquidsense.module.modules.blatant;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.utils.client.InventoryUtils;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.module.modules.world.Scaffold;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;

@ModuleInfo(
        name = "AutoApple",
        description = "Makes you automatically eat soup whenever your health is low.",
        category = ModuleCategory.BLATANT
)
public class AutoApple extends Module {

    private final ListValue modeValue = new ListValue("EadMode", new String[]{"Slow", "Fast"}, "Fast");
    private final FloatValue maxHealth = new FloatValue("MaxHealth", 8.0f, 1.0f, 19.0f);
    private final IntegerValue delayValue = new IntegerValue("nextDelay", 150, 0, 2000);
    private final BoolValue regenVaule = new BoolValue("NoRegen", true);
    private final MSTimer timer = new MSTimer();
    private boolean isUse;
    private int eadId = -1;
    private long delay;
    private boolean potifinSave;

    @Override
    public String getTag() {
        return String.valueOf(maxHealth.get());
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer == null) return;
        int GAPPLE = InventoryUtils.findItem(36, 45, Items.golden_apple);
        int SKULL = InventoryUtils.findItem(36, 45, Items.skull);
        int SORG = GAPPLE == -1 && SKULL != -1 ? SKULL : GAPPLE;

        if (timer.hasTimePassed(delay) || !LiquidSense.moduleManager.getModule(Scaffold.class).getState() || !mc.thePlayer.isDead){

            if (mc.thePlayer.getHealth() >= maxHealth.get()) {
                if (isUse) {
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    isUse = false;
                    eadId = -1;
                }
                return;
            }

            if (regenVaule.get() && mc.thePlayer.isPotionActive(Potion.regeneration)) {
                potifinSave = false;
            } else {
                potifinSave = true;
            }

            if (mc.thePlayer.getHealth() <= maxHealth.get() && SORG != -1 && potifinSave) {

                isUse = true;
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(SORG - 36));
                mc.getNetHandler().addToSendQueue(
                        new C08PacketPlayerBlockPlacement(mc.thePlayer.inventoryContainer
                                .getSlot(SORG).getStack())
                );
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(SORG - 36));
                if (modeValue.get().equalsIgnoreCase("Fast")) {

                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    isUse = false;
                }
                timer.reset();
                delay = (long)delayValue.get();
                return;
            }
            if (modeValue.get().equalsIgnoreCase("Slow")) {
                if (isUse || mc.thePlayer.getItemInUseDuration() >= 24) {
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    isUse = false;
                    timer.reset();
                    delay = (long)delayValue.get();
                }
            }

        }
    }

}
