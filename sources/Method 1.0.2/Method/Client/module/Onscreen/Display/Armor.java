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


public final class Armor extends Module {
    public Armor() {
        super("Armor", Keyboard.KEY_NONE, Category.ONSCREEN, "Armor");
    }

    static Setting Rotation;
    static Setting xpos;
    static Setting ypos;
    static Setting Background;
    static Setting Numbervals;
    static Setting Reverse;
    static Setting Shadow;
    static Setting Frame;
    static Setting Color;
    static Setting FontSize;
    static PinableFrame pin;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(Color = new Setting("BGColor", this, .01, 0, .30, .22));
        setmgr.add(Rotation = new Setting("Rotation", this, false));
        setmgr.add(Numbervals = new Setting("Numbervals", this, false));
        setmgr.add(Reverse = new Setting("Reverse", this, false));
        setmgr.add(Background = new Setting("Background", this, false));
        setmgr.add(Shadow = new Setting("Shadow", this, true));
        setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));
        setmgr.add(FontSize = new Setting("FontSize", this, 22, 10, 40, true));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth / 2) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 10, -20, (mc.displayHeight / 2) + 40, true));
        pin = new ArmorRUN();
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

    public static class ArmorRUN extends PinableFrame {

        public ArmorRUN() {
            super("ArmorSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());

        }

        @Override
        public void setup() {
            getSetup(this, xpos, ypos, Frame, FontSize);
        }

        @Override
        public void Ongui() {
            getInit(this, xpos, ypos, Frame, FontSize);
        }

        @Subscribe
        public void onRenderGameOverlay(Text event) {
            int posx = (int) (this.x * RenderUtils.simpleScale(false));
            int posy = (int) (this.y * RenderUtils.simpleScale(true));

            int space = 0;
            if (!Reverse.getValBoolean())
                for (int i = 0; i <= 3; i++) {
                    space = Armordisplay(space, posx, posy, i);
                }
            if (Reverse.getValBoolean())
                for (int i = 3; i >= 0; i--) {
                    space = Armordisplay(space, posx, posy, i);
                }
        }

        private int Armordisplay(int space, int posx, int posy, int i) {
            final ItemStack stack = mc.player.inventoryContainer.getSlot(8 - i).getStack();
            if (stack != ItemStack.EMPTY) {
                RenderHelper.enableGUIStandardItemLighting();
                if (Rotation.getValBoolean()) {
                    if (Background.getValBoolean())
                        Gui.drawRect(posx, posy + 10, posx + 72, posy + 25, Color.getcolor());
                    mc.getRenderItem().renderItemAndEffectIntoGUI(stack, posx + space, posy + 10);
                    mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, posx + space, posy + 10);
                    if (Numbervals.getValBoolean()) {
                        
                        fontSelect(Frame, String.valueOf(stack.getMaxDamage() - stack.getItemDamage()), posx + space, posy, (stack.getMaxDamage() - stack.getItemDamage()) < 30 ? 0xFFFF0000 : -1, Shadow.getValBoolean());
                        
                    }
                } else {
                    if (Background.getValBoolean())
                        Gui.drawRect(posx, posy + 10, posx + 15, posy + 80, Color.getcolor());
                    mc.getRenderItem().renderItemAndEffectIntoGUI(stack, posx, posy + space + 10);
                    mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, posx, posy + space + 10);
                    if (Numbervals.getValBoolean()) {
                        
                        fontSelect(Frame, String.valueOf(stack.getItemDamage()), posx + 10, posy + 10 + space, -1, Shadow.getValBoolean());
                        
                    }
                }
                RenderHelper.disableStandardItemLighting();
                space += 18;
            }
            return space;
        }


    }

}

