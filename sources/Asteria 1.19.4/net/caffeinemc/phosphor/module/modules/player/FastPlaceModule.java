package net.caffeinemc.phosphor.module.modules.player;

import net.caffeinemc.phosphor.api.event.events.ItemUseEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.util.MathUtils;
import net.caffeinemc.phosphor.mixin.MinecraftClientAccessor;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.caffeinemc.phosphor.module.setting.settings.NumberSetting;
import net.minecraft.item.*;

public class FastPlaceModule extends Module {
    private final BooleanSetting xpOnly = new BooleanSetting("Only XP", this, false);
    private final BooleanSetting blocks = new BooleanSetting("Blocks", this, true);
    private final BooleanSetting items = new BooleanSetting("Items", this, true);
    private final NumberSetting delay = new NumberSetting("Delay", this, 0d, 0d, 10d, 1d);
    private final NumberSetting useChance = new NumberSetting("Use Chance", this, 100d, 0d, 100d, 1d);

    public FastPlaceModule() {
        super("FastPlace", "Spams use action", Category.PLAYER);
    }

    @EventHandler
    private void onItemUse(ItemUseEvent.Pre event) {
        if (MathUtils.getRandomInt(1, 100) <= useChance.getIValue()) {
            ItemStack mainHandStack = mc.player.getMainHandStack();
            ItemStack offHandStack = mc.player.getOffHandStack();
            Item mainHandItem = mainHandStack.getItem();
            Item offHandItem = mc.player.getOffHandStack().getItem();

            if (!(mainHandStack.isOf(Items.EXPERIENCE_BOTTLE) || offHandStack.isOf(Items.EXPERIENCE_BOTTLE)) && xpOnly.isEnabled())
                return;

            if (!xpOnly.isEnabled()) {
                if (mainHandItem instanceof BlockItem || offHandItem instanceof BlockItem) {
                    if (!blocks.isEnabled()) return;
                } else {
                    if (!items.isEnabled()) return;
                }
            }

            if (mainHandItem.getFoodComponent() != null)
                return;

            if (offHandItem.getFoodComponent() != null)
                return;

            if ((mainHandStack.isOf(Items.RESPAWN_ANCHOR) || mainHandStack.isOf(Items.GLOWSTONE)) ||
                    (offHandStack.isOf(Items.RESPAWN_ANCHOR) || offHandStack.isOf(Items.GLOWSTONE)))
                return;

            if (mainHandItem instanceof RangedWeaponItem || offHandItem instanceof RangedWeaponItem)
                return;

            mc.itemUseCooldown = delay.getIValue();
        } else {
            event.cancel();
        }
    }
}
