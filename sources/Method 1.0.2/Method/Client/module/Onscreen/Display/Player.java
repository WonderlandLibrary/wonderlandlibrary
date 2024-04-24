package Method.Client.module.Onscreen.Display;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

import static Method.Client.Main.setmgr;

public final class Player extends Module {
    public Player() {
        super("Player", Keyboard.KEY_NONE, Category.ONSCREEN, "Player");
    }

    static Setting xpos;
    static Setting ypos;
    static Setting Scale;
    static Setting Nolook;
    static PinableFrame pin;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 20, -20, (mc.displayHeight) + 40, true));
        setmgr.add(Scale = new Setting("Scale", this, 1, -0, 5, false));
        ArrayList<String> options = new ArrayList<>();
        options.add("Free");
        options.add("Mouse");
        options.add("None");

        setmgr.add(Nolook = new Setting("Mode", this, "Free", options));
        pin = new PlayerRUN();
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

    public static class PlayerRUN extends PinableFrame {

        public PlayerRUN() {
            super("PlayerSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());

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
            if (mc.player == null) return;
            if (mc.gameSettings.thirdPersonView != 0) return;

            int posx = (int) (this.getX() * RenderUtils.simpleScale(false));
            int posy = (int) (this.getY() * RenderUtils.simpleScale(true));

            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (Nolook.getValString().equalsIgnoreCase("Free")) {
                drawPlayer(posx, posy, mc.player);
            } else {
                GuiInventory.drawEntityOnScreen(posx + 17, posy + 60, (int) (Scale.getValDouble() * 30), (Nolook.getValString().equalsIgnoreCase("None") ? 0.0f : (float) (posx) - Mouse.getX()), (Nolook.getValString().equalsIgnoreCase("None") ? 0.0f : (float) (-mc.displayHeight) + Mouse.getY()), mc.player);
            }

            GlStateManager.popMatrix();
        }


        private void drawPlayer(int x, int y, EntityLivingBase ent) {
            GlStateManager.translate((float) x + 30, (float) y + 50, 50.0F);
            GlStateManager.scale((float) -Scale.getValDouble() * 24, (float) Scale.getValDouble() * 24, (float) Scale.getValDouble() * 24);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.disableBlend();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            RenderHelper.enableStandardItemLighting();
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-((float) Math.atan(((float) 0 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(0.0F, 0.0F, 0.0F);
            RenderManager renderManager = mc.getRenderManager();
            renderManager.setPlayerViewY(180.0F);
            renderManager.setRenderShadow(false);
            renderManager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
            renderManager.setRenderShadow(true);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
        }


    }
}

