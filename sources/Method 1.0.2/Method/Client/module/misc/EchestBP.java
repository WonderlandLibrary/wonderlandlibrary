
package Method.Client.module.misc;

import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.ChatUtils;
import com.google.common.eventbus.Subscribe;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static net.minecraft.network.play.client.CPacketEntityAction.Action.OPEN_INVENTORY;

public class EchestBP extends Module {
    public EchestBP() {
        super("EchestBP", Keyboard.KEY_NONE, Category.MISC, "EchestBP");
    }

    private GuiScreen echestScreen = null;
    boolean EchestSet = false;
    boolean Tryrightclick = false;

    @Override
    public void onEnable() {
        EchestSet = false;
        Tryrightclick = false;
        ChatUtils.message(ChatFormatting.AQUA + " Open an Echest to start!");
    }

    @Override
    public void onDisable() {
        if (this.echestScreen != null)
            mc.displayGuiScreen(this.echestScreen);
        this.echestScreen = null;
    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (packet instanceof CPacketEntityAction) {
            CPacketEntityAction pac = (CPacketEntityAction) packet;
            if (pac.getAction().equals(OPEN_INVENTORY))
                return false;
        }
        return !(packet instanceof CPacketCloseWindow);
    }

    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc.currentScreen instanceof GuiInventory)
            mc.playerController.updateController();
        if (mc.currentScreen instanceof GuiContainer) {
            if (((GuiContainer) mc.currentScreen).inventorySlots instanceof ContainerChest) {
                Container inventorySlots = ((GuiContainer) mc.currentScreen).inventorySlots;
                if (((ContainerChest) inventorySlots).getLowerChestInventory() instanceof InventoryBasic)
                    if (((ContainerChest) inventorySlots).getLowerChestInventory().getName().equalsIgnoreCase("Ender Chest")) {
                        if (!EchestSet) {
                            EchestSet = true;
                            mc.player.closeScreen();
                        } else {
                            this.echestScreen = mc.currentScreen;
                            mc.currentScreen = null;
                            ChatUtils.message(ChatFormatting.AQUA + "Done! To open please disable EchestBP");
                            Mouse.setGrabbed(true);
                        }
                    }
            }
        } else if (EchestSet && !Tryrightclick) {
            Tryrightclick = true;
            mc.rightClickMouse();
        }
    }
}
