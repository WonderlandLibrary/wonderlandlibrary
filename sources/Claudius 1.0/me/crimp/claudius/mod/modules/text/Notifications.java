package me.crimp.claudius.mod.modules.text;

import me.crimp.claudius.mod.command.Command;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;
import me.crimp.claudius.utils.InventoryUtil;
import me.crimp.claudius.utils.Timer;
import net.minecraft.init.Items;


public class Notifications extends Module {
    public Setting<Boolean> notifyToggles = register(new Setting<Boolean>("ToggleNotis", false));
    public Setting<Boolean> totemwarning = register(new Setting<Boolean>("TotemWarning", false));
    public Notifications() {
        super("Notifications", "Notification for client shit", Category.Text, false, false);
    }
    public static Notifications INSTANCE = new Notifications();
    Timer timer = new Timer();

    @Override
    public void onUpdate() {

        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING && InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING, true) != 0 && totemwarning.getValue()) { timer.reset();}
        if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING, true) != 0 && totemwarning.getValue()) {
            if (timer.passedMs(5L)) {
                Command.sendOverwriteClientMessage("There is no totem in your offhand!");
                timer.reset();
            }
        }
    }
}



