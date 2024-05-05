package rosegoldclient.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditCommandBlockMinecart;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.utils.Utils;

/*
 * Code modified from: https://github.com/PieKing1215/InvMove-Forge
 */

public class InventoryWalk {

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event) {
        if (allowMovementInScreen(Main.mc.currentScreen)) {
            KeyBinding.updateKeyBindState();
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindDrop.getKeyCode(), false);
            manualTickMovement(event.getMovementInput(), Main.mc.player.isSneaking() || Main.mc.player.isInWater(), Main.mc.player.isSpectator());
            Main.mc.player.setSprinting(rawIsKeyDown(Main.mc.gameSettings.keyBindSprint));
            Main.mc.player.setSneaking(rawIsKeyDown(Main.mc.gameSettings.keyBindSneak));
        }
    }

    public static void manualTickMovement(MovementInput input, boolean slow, boolean noDampening) {
        input.forwardKeyDown = rawIsKeyDown(Main.mc.gameSettings.keyBindForward);
        input.backKeyDown = rawIsKeyDown(Main.mc.gameSettings.keyBindBack);
        input.leftKeyDown = rawIsKeyDown(Main.mc.gameSettings.keyBindLeft);
        input.rightKeyDown = rawIsKeyDown(Main.mc.gameSettings.keyBindRight);
        float f = input.forwardKeyDown == input.backKeyDown ? 0.0f : (input.moveForward = (float)(input.forwardKeyDown ? 1 : -1));
        input.moveStrafe = input.leftKeyDown == input.rightKeyDown ? 0.0f : (float)(input.leftKeyDown ? 1 : -1);
        input.jump = rawIsKeyDown(Main.mc.gameSettings.keyBindJump);
        rawIsKeyDown(Main.mc.gameSettings.keyBindSneak);
        input.sneak = false;
        if (!noDampening && slow) {
            input.moveStrafe = (float)((double)input.moveStrafe * 0.3);
            input.moveForward = (float)((double)input.moveForward * 0.3);
        }
    }

    public static boolean rawIsKeyDown(KeyBinding key) {
        try {
            return ObfuscationReflectionHelper.getPrivateValue(KeyBinding.class, key, "field_74513_e");
        } catch (Exception var2) {
            System.out.println("Failed to access KeyBinding.pressed on \"" + key + "\": " + var2.getMessage());
            var2.printStackTrace();
            return false;
        }
    }

    public static boolean allowMovementInScreen(GuiScreen screen) {
        if(!Main.configFile.invWalk) return false;
        if (screen == null) {
            return false;
        }
        if (screen instanceof GuiChat) {
            return false;
        }
        if (screen instanceof GuiCommandBlock) {
            return false;
        }
        if (screen instanceof GuiEditCommandBlockMinecart) {
            return false;
        }
        if (screen instanceof GuiEditSign) {
            return false;
        }
        return true;
    }
}
