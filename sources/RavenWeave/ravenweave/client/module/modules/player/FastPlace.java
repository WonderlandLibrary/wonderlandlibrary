package ravenweave.client.module.modules.player;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.SliderSetting;
import ravenweave.client.module.setting.impl.TickSetting;
import ravenweave.client.utils.ReflectionUtils;
import ravenweave.client.utils.Utils;

import java.lang.reflect.Field;

public class FastPlace extends Module {
    public static SliderSetting delaySlider, projSlider;
    public static TickSetting blockOnly, projSeparate;

    public static final Field rightClickDelayTimerField;

    static {
        rightClickDelayTimerField = ReflectionUtils.findField(Minecraft.class, "rightClickDelayTimer");

        if (rightClickDelayTimerField != null) {
            rightClickDelayTimerField.setAccessible(true);
        }
    }

    public FastPlace() {
        super("FastPlace", ModuleCategory.player);

        this.registerSetting(delaySlider = new SliderSetting("Delay", 0.0D, 0.0D, 4.0D, 1.0D));
        this.registerSetting(blockOnly = new TickSetting("Blocks only", true));
        this.registerSetting(projSeparate = new TickSetting("Separate Projectile Delay", true));
        this.registerSetting(projSlider = new SliderSetting("Projectile Delay", 2.0D, 0.0D, 4.0D, 1.0D));

    }

    @Override
    public boolean canBeEnabled() {
        return rightClickDelayTimerField != null;
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        if (Utils.Player.isPlayerInGame() && mc.inGameHasFocus && rightClickDelayTimerField != null) {
            if (blockOnly.isToggled()) {
                ItemStack item = mc.thePlayer.getHeldItem();
                if (item != null && (item.getItem() instanceof ItemBlock)) {
                    try {
                        int c = (int) delaySlider.getInput();
                        if (c == 0) {
                            rightClickDelayTimerField.set(mc, 0);
                        } else {
                            if (c == 4) {
                                return;
                            }

                            int d = rightClickDelayTimerField.getInt(mc);
                            if (d == 4) {
                                rightClickDelayTimerField.set(mc, c);
                            }
                        }
                    } catch (IllegalAccessException | IndexOutOfBoundsException ignored) {
                    }
                } else if (item != null && (item.getItem() instanceof ItemSnowball || item.getItem() instanceof ItemEgg)
                        && projSeparate.isToggled()) {
                    try {
                        int c = (int) projSlider.getInput();
                        if (c == 0) {
                            rightClickDelayTimerField.set(mc, 0);
                        } else {
                            if (c == 4) {
                                return;
                            }

                            int d = rightClickDelayTimerField.getInt(mc);
                            if (d == 4) {
                                rightClickDelayTimerField.set(mc, c);
                            }
                        }
                    } catch (IllegalAccessException | IndexOutOfBoundsException ignored) {
                    }
                }
            } else {
                try {
                    int c = (int) delaySlider.getInput();
                    if (c == 0) {
                        rightClickDelayTimerField.set(mc, 0);
                    } else {
                        if (c == 4) {
                            return;
                        }

                        int d = rightClickDelayTimerField.getInt(mc);
                        if (d == 4) {
                            rightClickDelayTimerField.set(mc, c);
                        }
                    }
                } catch (IllegalAccessException | IndexOutOfBoundsException ignored) {
                }
            }
        }
    }
}
