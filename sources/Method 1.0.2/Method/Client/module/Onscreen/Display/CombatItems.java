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
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Objects;

import static Method.Client.Main.setmgr;

public final class CombatItems extends Module {
    public CombatItems() {
        super("CombatItems", Keyboard.KEY_NONE, Category.ONSCREEN, "CombatItems");
    }

    static Setting BgColor;
    static Setting background;
    static Setting xpos;
    static Setting ypos;
    static PinableFrame pin;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(BgColor = new Setting("BgColor", this, .22, .88, .22, .22));
        setmgr.add(background = new Setting("background", this, false));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, mc.displayWidth + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 110, -20, (mc.displayHeight) + 40, true));
        pin = new CombatItemsRUN();
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

    public static class CombatItemsRUN extends PinableFrame {

        public CombatItemsRUN() {
            super("CombatItemsSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());

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

        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        ArrayList<Item> Itemslist = new ArrayList<>();

        public void setupitems() {
            itemStacks.clear();
            Itemslist.clear();
            itemStacks.add(new ItemStack(Items.ARROW, 1));
            itemStacks.add(new ItemStack(Items.END_CRYSTAL, 1));
            itemStacks.add(new ItemStack(Items.GOLDEN_APPLE, 1, 1));
            itemStacks.add(new ItemStack(Items.TOTEM_OF_UNDYING, 1));
            itemStacks.add(new ItemStack(Items.EXPERIENCE_BOTTLE, 1));
            itemStacks.add(new ItemStack(Items.ENDER_PEARL, 1));
            itemStacks.add(new ItemStack(Items.CHORUS_FRUIT, 1));
            itemStacks.add(new ItemStack(Item.getItemById(49), 1));
            Itemslist.add(Items.END_CRYSTAL);
            Itemslist.add(Items.ARROW);
            Itemslist.add(Items.GOLDEN_APPLE);
            Itemslist.add(Items.TOTEM_OF_UNDYING);
            Itemslist.add(Items.EXPERIENCE_BOTTLE);
            Itemslist.add(Items.CHORUS_FRUIT);
            Itemslist.add(Item.getItemById(49));
            Itemslist.add(Items.TIPPED_ARROW);
            Itemslist.add(Items.SPECTRAL_ARROW);
        }


        @Subscribe
        public void onRenderGameOverlay(Text event) {
            if (mc.player == null)
                return;
            setupitems();

            for (ItemStack itemStack : mc.player.inventory.mainInventory) {
                if (Itemslist.contains(itemStack.getItem())) {
                    for (ItemStack stack : itemStacks) {
                        if (itemStack.getItem().equals(Items.TIPPED_ARROW) || itemStack.getItem().equals(Items.SPECTRAL_ARROW) && stack.getItem().equals(Items.ARROW))
                            stack.setCount(stack.getCount() + itemStack.getCount());
                        if (Objects.equals(stack.getItem().getRegistryName(), itemStack.getItem().getRegistryName()))
                            stack.setCount(stack.getCount() + itemStack.getCount());
                    }
                }
            }
            int offset = 0;
            int posx = (int) (this.getX() * RenderUtils.simpleScale(false));
            int posy = (int) (this.getY() * RenderUtils.simpleScale(true));

            RenderHelper.enableGUIStandardItemLighting();
            for (ItemStack itemStack : itemStacks) {
                itemStack.setCount(itemStack.getCount() - 1);
                if (itemStack.getCount() >= 1) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, posx + offset, posy - 3);
                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, posx + offset, posy - 3, null);
                    offset += 19;
                }
            }
            if (background.getValBoolean())
                Gui.drawRect(posx, posy, posx + offset + 10, posy + 20, BgColor.getcolor());

            RenderHelper.disableStandardItemLighting();
            mc.getRenderItem().zLevel = 0.0F;
        }

    }

}

