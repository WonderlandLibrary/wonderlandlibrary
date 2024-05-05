package rosegoldclient.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import rosegoldclient.Main;
import rosegoldclient.events.MillisecondEvent;
import rosegoldclient.utils.Utils;

import java.lang.reflect.Method;

public class AutoClicker {

    private boolean toggled = false;
    private int count = 0;
    private long startedAt = 0;
    private long lastClickTime = 0;

    @SubscribeEvent
    public void onMillisecond(MillisecondEvent event) {
        if(!toggled) return;
        if(System.currentTimeMillis() - lastClickTime < (long) Main.configFile.autoClickerDelay - 1) return;
        if(Main.mc.currentScreen != null) {
            Slot currentSlot = ((GuiContainer) Main.mc.currentScreen).getSlotUnderMouse();
            if(currentSlot == null) return;
            Main.mc.playerController.windowClick(
                    Main.mc.player.openContainer.windowId,
                    currentSlot.slotNumber,
                    0,
                    ClickType.PICKUP,
                    Main.mc.player
            );
            count++;
        } else {
            switch (Main.configFile.autoClickerMode) {
                case 1:
                    RayTraceResult rayTraceResult = Main.mc.objectMouseOver;
                    if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.ENTITY) {
                        Main.mc.playerController.attackEntity(Main.mc.player, rayTraceResult.entityHit);
                        Main.mc.player.swingArm(EnumHand.MAIN_HAND);
                        count++;
                    } else if (rayTraceResult != null) {
                        Main.mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                    break;
                case 0:
                    rightClick();
                    count++;
                    break;
            }
        }
        lastClickTime = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onKeyInput(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        if(!Main.configFile.autoClicker) {
            toggled = false;
            return;
        }
        int eventKey = Keyboard.getEventKey();
        if(eventKey != Main.keybinds.get(8).getKeyCode()) return;
        if(Keyboard.isKeyDown(eventKey)) {
            if(!toggled) {
                toggled = true;
                count = 0;
                startedAt = System.currentTimeMillis();
            }
        } else {
            toggled = false;
            Utils.sendModMessage(String.format("%s Clicks in %s milliseconds", count, System.currentTimeMillis() - startedAt));
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(!Main.configFile.autoClicker) {
            toggled = false;
            return;
        }
        int eventKey = Keyboard.getEventKey();
        if(eventKey != Main.keybinds.get(8).getKeyCode()) return;
        if(Keyboard.isKeyDown(eventKey)) {
            if(!toggled) {
                toggled = true;
                count = 0;
                startedAt = System.currentTimeMillis();
            }
        } else {
            toggled = false;
            Utils.sendModMessage(String.format("%s Clicks in %s milliseconds", count, System.currentTimeMillis() - startedAt));
        }
    }

    public static void rightClick() {
        try {
            Method rightClickMouse;
            try {
                rightClickMouse = Minecraft.class.getDeclaredMethod("func_147121_ag");
            } catch (NoSuchMethodException e) {
                rightClickMouse = Minecraft.class.getDeclaredMethod("rightClickMouse");
            }
            rightClickMouse.setAccessible(true);
            rightClickMouse.invoke(Main.mc);
        } catch (Exception ignored) {}
    }

}
