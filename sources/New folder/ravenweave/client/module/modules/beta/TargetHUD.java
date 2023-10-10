package ravenweave.client.module.modules.beta;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.weavemc.loader.api.event.RenderGameOverlayEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import ravenweave.client.event.impl.AttackEntityEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.modules.client.Targets;
import ravenweave.client.module.setting.impl.DescriptionSetting;
import ravenweave.client.module.setting.impl.RGBSetting;
import ravenweave.client.module.setting.impl.SliderSetting;
import ravenweave.client.utils.RenderUtils;

import java.awt.*;

import static net.minecraft.client.gui.Gui.drawScaledCustomSizeModalRect;

public class TargetHUD extends Module {
    public static final int HEAD_X = 7, HEAD_Y = 7, WIDTH = 200, HEIGHT = 75, NAME_OFFSET_X = 40, NAME_OFFSET_Y = 15;
    public int screenHeight, screenWidth;
    public FontRenderer fr;
    private static EntityOtherPlayerMP target;
    public static RGBSetting borderColor, mainColor;
    public static SliderSetting xSetting, ySetting;
    ScaledResolution sr;

    public TargetHUD() {
        super("Target HUD", ModuleCategory.beta); // Category: Render
        this.registerSetting(new DescriptionSetting("Shows your target."));
        this.registerSetting(borderColor = new RGBSetting("Border",49, 203, 113));
        this.registerSetting(mainColor = new RGBSetting("Main Color",49, 203, 113));
        this.registerSetting(xSetting = new SliderSetting("X", 5, 0, 10, 1));
        this.registerSetting(ySetting = new SliderSetting("Y", 5, 0, 10, 1));

        sr = new ScaledResolution(Minecraft.getMinecraft());
        screenHeight = sr.getScaledHeight();
        screenWidth = sr.getScaledWidth();
        fr = mc.fontRendererObj;
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent e) {
        if (!this.enabled) return;
        if (!(e.target instanceof AbstractClientPlayer)) return;
        EntityPlayer ep = (EntityPlayer) e.target;
    }


    @SubscribeEvent
    public void onRender2d(RenderGameOverlayEvent.Pre e) {
        drawTargetHUD((int) xSetting.getInput(), (int) ySetting.getInput());
    }

    public static void drawTargetHUD(int x, int y) {
        // Get target
        EntityPlayer thatGuy = Targets.getTarget();

        // Filter target
        if (!(thatGuy instanceof EntityOtherPlayerMP)) return;
        target = (EntityOtherPlayerMP) thatGuy;



        // Draw Border
        RenderUtils.drawBorderedRoundedRect(x, y, x + WIDTH, y + HEIGHT, 15, 5, borderColor.getRGB(), mainColor.getRGB());

        // Draw Face
        mc.getTextureManager().bindTexture(target.getLocationSkin());
        drawScaledCustomSizeModalRect(x+HEAD_X, y+HEAD_Y, 8.0f, 8.0f, 8, 8, 24, 24, 64.0f, 64.0f);
        if (target.isWearing(EnumPlayerModelParts.HAT)) {
            drawScaledCustomSizeModalRect(x+HEAD_X, y+HEAD_Y, 40.0f, 8.0f, 8, 8, 24, 24, 64.0f, 64.0f);
        }
        GlStateManager.bindTexture(0);
        GL11.glScalef(1.5f,1.5f,1);

        int currentY = y;

        currentY += (NAME_OFFSET_Y);

        // Draw Name
        mc.fontRendererObj.drawString("Name: " + target.getName(), (int) ((x + NAME_OFFSET_X)/1.5), (int) (currentY/1.5), 0xff00ffff);

        // Draw Distance
        currentY += (NAME_OFFSET_Y + mc.fontRendererObj.FONT_HEIGHT);
        mc.fontRendererObj.drawString("Distance: " + String.format("%.2f", target.getDistanceToEntity(mc.thePlayer)) + " blocks", (int) ((x + HEAD_X)/1.5), (int) (currentY/1.5), 0x00ff00ff);

        // Draw Health Bar

        currentY += mc.fontRendererObj.FONT_HEIGHT + HEAD_Y;
        RenderUtils.drawBorderedRoundedRect((x + HEAD_X)/1.5f, currentY/1.5f, (x + WIDTH - HEAD_X)/1.5f, (y+HEIGHT - HEAD_Y)/1.5f, 5, 7,Color.GRAY.getRGB(), Color.RED.getRGB());

        GL11.glScalef(1/1.5f,1/1.5f,1/1.5f);
    }
}
