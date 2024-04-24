package Method.Client.module.Onscreen.Display;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;

public final class Inventory extends Module {
    public Inventory() {
        super("Inventory", Keyboard.KEY_NONE, Category.ONSCREEN, "Inventory");
    }

    static Setting BgColor;
    static Setting background;
    static Setting Hotbar;
    static Setting Xcarry;
    static Setting xpos;
    static Setting ypos;
    static PinableFrame pin;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(BgColor = new Setting("BgColor", this, .22, .88, .22, .22));
        setmgr.add(background = new Setting("background", this, false));
        setmgr.add(Hotbar = new Setting("Hotbar", this, false));
        setmgr.add(Xcarry = new Setting("Xcarry", this, false));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, mc.displayWidth + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 110, -20, (mc.displayHeight) + 40, true));
        pin = new InventoryRUN();
        OnscreenGUI.pinableFrames.add(pin);
    }

    @Override
    public void onEnable() {
        PinableFrame.Toggle(pin, true);
    }

    @Override
    public void onDisable() {
        PinableFrame.Toggle(pin, false);
    }

    public static class InventoryRUN extends PinableFrame {

        public InventoryRUN() {
            super("InventorySET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());
        }

        @Override
        public void setup() {
            this.x = (int) xpos.getValDouble();
            this.y = (int) ypos.getValDouble();
        }

        @Override
        public void Ongui() {
            if (!this.getDrag()) {
                this.x = (int) xpos.getValDouble();
                this.y = (int) ypos.getValDouble();
            } else {
                xpos.setValDouble(this.x);
                ypos.setValDouble(this.y);
            }
        }

        @Subscribe
        public void onRenderGameOverlay(Text event) {
            if (mc.player == null)
                return;

            RenderHelper.enableGUIStandardItemLighting();

            int posx = (int) (this.getX() * RenderUtils.simpleScale(false));
            int posy = (int) (this.getY() * RenderUtils.simpleScale(true));

            if (background.getValBoolean())
                Gui.drawRect(posx, posy, posx + this.getWidth() + 60, posy + 50 + (Hotbar.getValBoolean() ? 25 : 0), BgColor.getcolor()); // background

            for (int i = 0; i < 27; i++) {
                ItemStack itemStack = mc.player.inventory.mainInventory.get(i + 9);
                int offsetX = posx + (i % 9) * 16;
                int offsetY = posy + (i / 9) * 16;
                mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, offsetX, offsetY);
                mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, offsetX, offsetY, null);
            }

            if (Hotbar.getValBoolean()) {
                for (int i = 0; i < 9; i++) {
                    ItemStack itemStack = mc.player.inventory.mainInventory.get(i);
                    int offsetX = posx + (i % 9) * 16;
                    int offsetY = posy + 48;
                    mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, offsetX, offsetY);
                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, offsetX, offsetY, null);
                }
            }

            if (Xcarry.getValBoolean()) {
                for (int i = 0; i < 5; i++) {
                    ItemStack itemStack = mc.player.inventoryContainer.getInventory().get(i);
                    int offsetX = posx + i * 16;
                    int offsetY = posy + 60;
                    mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, offsetX, offsetY);
                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, offsetX, offsetY, null);
                }
            }

            RenderHelper.disableStandardItemLighting();
            mc.getRenderItem().zLevel = 0.0F;
        }

    }

}

