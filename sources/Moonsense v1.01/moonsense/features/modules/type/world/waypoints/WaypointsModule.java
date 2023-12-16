// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world.waypoints;

import moonsense.enums.ModuleCategory;
import moonsense.ui.utils.GuiUtils;
import moonsense.features.modules.type.mechanic.FreelookModule;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import moonsense.utils.MathUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import moonsense.MoonsenseClient;
import moonsense.event.impl.SCWorldLoadedEvent;
import moonsense.event.SubscribeEvent;
import net.minecraft.client.gui.GuiScreen;
import moonsense.ui.screen.AbstractGuiScreen;
import moonsense.ui.screen.GuiWaypointAdd;
import org.lwjgl.input.Keyboard;
import moonsense.event.impl.SCKeyEvent;
import moonsense.utils.KeyBinding;
import java.awt.Color;
import moonsense.settings.Setting;
import net.minecraft.util.ResourceLocation;
import moonsense.features.SCModule;

public class WaypointsModule extends SCModule
{
    public final ResourceLocation BEAM_LOCATION;
    public static WaypointsModule INSTANCE;
    private final Setting showBeam;
    private final Setting boxBorders;
    private final Setting textShadow;
    private final Setting boxPadding;
    private final Setting borderWidth;
    private final Setting dynamicBackgroundColor;
    private final Setting staticBackgroundColor;
    private final Setting fillWaypointWithWaypointColor;
    public final Setting defaultColor;
    private final Setting createWaypoint;
    public final WaypointRenderer waypointRenderer;
    
    public WaypointsModule() {
        super("Waypoints", "Add waypoints to a world.");
        this.BEAM_LOCATION = new ResourceLocation("textures/entity/beacon_beam.png");
        this.waypointRenderer = new WaypointRenderer();
        new Setting(this, "General Options");
        this.defaultColor = new Setting(this, "Default Color").setDefault(new Color(123, 234, 12, 190).getRGB(), 0).hide();
        this.createWaypoint = new Setting(this, "Create Waypoint").setDefault(new KeyBinding(13));
        new Setting(this, "Waypoint Options");
        this.showBeam = new Setting(this, "Show Beam").setDefault(true);
        new Setting(this, "Waypoint Style Options");
        this.boxBorders = new Setting(this, "Box Borders").setDefault(true);
        this.boxPadding = new Setting(this, "Box Padding").setDefault(4.0f).setRange(1.0f, 8.0f, 0.1f);
        this.borderWidth = new Setting(this, "Border Width").setDefault(1.0f).setRange(1.0f, 4.0f, 0.1f);
        this.textShadow = new Setting(this, "Text Shadow").setDefault(true);
        this.dynamicBackgroundColor = new Setting(this, "Dynamic (Fading) Waypoints").setDefault(true);
        this.staticBackgroundColor = new Setting(this, "Static Background Color").setDefault(new Color(0, 0, 0, 75).getRGB(), 0);
        this.fillWaypointWithWaypointColor = new Setting(this, "Fill Waypoint Background").setDefault(false);
        WaypointsModule.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onKeyPress(final SCKeyEvent event) {
        if (Keyboard.isKeyDown(this.createWaypoint.getValue().get(0).getKeyCode()) && Keyboard.isKeyDown(this.createWaypoint.getValue().get(0).getKeyCode())) {
            this.mc.displayGuiScreen(new GuiWaypointAdd(null, false));
        }
    }
    
    @SubscribeEvent
    public void onWorldLoaded(final SCWorldLoadedEvent event) {
        WaypointManager.currentWorldName = event.worldName;
    }
    
    @SubscribeEvent
    public void onRenderWorldPass(final SCRenderWorldPassEvent event) {
        if (this.mc.func_175606_aa() == null) {
            return;
        }
        for (final Waypoint waypoint : MoonsenseClient.INSTANCE.getWaypointManager()) {
            if (waypoint.isVisible()) {
                if (!waypoint.canRenderWaypoint()) {
                    continue;
                }
                final double distToWp = this.getPlayerDistanceToWaypoint(waypoint, this.mc.func_175606_aa());
                final double d2 = this.mc.gameSettings.renderDistanceChunks * 16.0f * 0.75;
                if (this.showBeam.getBoolean()) {
                    this.renderWaypointBeam(waypoint, event.partialTicks, distToWp, d2);
                }
                this.renderWaypoint(waypoint, distToWp, d2);
            }
        }
    }
    
    public void renderWaypointBeam(final Waypoint waypoint, final float f, final double d, final double d2) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        final RenderManager renderManager = minecraft.getRenderManager();
        final double d3 = renderManager.getRenderPosX();
        final double d4 = renderManager.getRenderPosY();
        final double d5 = renderManager.getRenderPosZ();
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final double d6 = (float)waypoint.getLocation().xCoord - d3;
        final double d7 = (float)waypoint.getLocation().zCoord - d5;
        GlStateManager.depthMask(false);
        GlStateManager.translate((float)d6, (float)(-d4), (float)d7);
        GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
        final float f2 = 0.75f;
        final float f3 = 0.0f;
        final float f4 = (float)MathUtil.lIlIlIlIlIIlIIlIIllIIIIIl(minecraft.thePlayer.posY + 50.0, waypoint.getLocation().yCoord, 256.0);
        final int n = 20;
        final int n2 = 1;
        final float f5 = 1.0f * ((float)Math.max(0.0, Math.min(60.0, this.getPlayerDistanceToWaypoint(waypoint, this.mc.thePlayer) - 4.0)) / 60.0f);
        final float f6 = 0.6f * f5;
        final int n3 = SCModule.getColor(waypoint.getColor());
        final float f7 = (n3 >> 16 & 0xFF) / 255.0f;
        final float f8 = (n3 >> 8 & 0xFF) / 255.0f;
        final float f9 = (n3 & 0xFF) / 255.0f;
        final float f10 = 6.2831855f / n;
        final float f11 = (f3 - f2) / n2;
        final float f12 = f4 / n2;
        float f13 = 0.0f;
        float f14 = f2;
        final Tessellator ts = Tessellator.getInstance();
        final WorldRenderer wr = ts.getWorldRenderer();
        for (int i = 0; i < n2; ++i) {
            wr.startDrawing(8);
            for (int j = 0; j <= n; ++j) {
                float f15;
                float f16;
                if (j == n) {
                    f15 = this.llIlllIIIllllIIlllIllIIIl(0.0f);
                    f16 = this.lIllIlIIIlIIIIIIIlllIlIll(0.0f);
                }
                else {
                    f15 = this.llIlllIIIllllIIlllIllIIIl(j * f10);
                    f16 = this.lIllIlIIIlIIIIIIIlllIlIll(j * f10);
                }
                wr.color(f7, f8, f9, f6);
                wr.addVertex(f15 * f14, f16 * f14, f13);
                wr.color(f7, f8, f9, f6);
                wr.addVertex(f15 * (f14 + f11), f16 * (f14 + f11), f13 + f12);
            }
            ts.draw();
            f14 += f11;
            f13 += f12;
        }
        GlStateManager.depthMask(true);
        GlStateManager.translate(0.0f, 0.0f, (float)(waypoint.getLocation().yCoord + 0.0010000000474974513));
        GlStateManager.color(f7, f8, f9, 0.85f);
        this.waypointRenderer.lIlIlIlIlIIlIIlIIllIIIIIl(0.85f, 0.78f, 0.0f, 26, 1);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
    
    public float lIllIlIIIlIIIIIIIlllIlIll(final float f) {
        return (float)Math.cos(f);
    }
    
    public float llIlllIIIllllIIlllIllIIIl(final float f) {
        return (float)Math.sin(f);
    }
    
    public void lIlIlIlIlIIlIIlIIllIIIIIl(float f, float f2, float f3) {
        final float f4 = (float)Math.sqrt(f * f + f2 * f2 + f3 * f3);
        if (f4 > 1.0E-5f) {
            f /= f4;
            f2 /= f4;
            f3 /= f4;
        }
        GL11.glNormal3f(f, f2, f3);
    }
    
    public void renderWaypoint(final Waypoint waypoint, double d, final double d2) {
        if (Minecraft.getMinecraft().func_175606_aa() == null) {
            return;
        }
        double xPosDifference = waypoint.getLocation().xCoord - Minecraft.getMinecraft().getRenderManager().getRenderPosX();
        double yPosDifference = 2.0 + waypoint.getLocation().yCoord - Minecraft.getMinecraft().getRenderManager().getRenderPosY();
        double zPosDifference = waypoint.getLocation().zCoord - Minecraft.getMinecraft().getRenderManager().getRenderPosZ();
        String name = waypoint.getName();
        if (name.contains("&")) {
            name = name.split("&")[0];
        }
        final String waypointText = String.valueOf(name) + " [" + (int)d + "m]";
        if (d > d2) {
            xPosDifference = xPosDifference / d * d2;
            yPosDifference = yPosDifference / d * d2;
            zPosDifference = zPosDifference / d * d2;
            d = d2;
        }
        final int distFromCenterOfWP = (int)(Minecraft.getMinecraft().fontRendererObj.getStringWidth(waypointText) / 2.0f);
        final float f = 0.016666668f * (float)(1.0 + d * 0.15000000596046448);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)xPosDifference, (float)yPosDifference, (float)zPosDifference);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-FreelookModule.getCameraYaw(), 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(FreelookModule.getCameraPitch(), 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-f, -f, f);
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        final float boxPadding = this.boxPadding.getFloat();
        final float outsideEdgeLeft = -distFromCenterOfWP - boxPadding;
        final float f2 = -boxPadding;
        final float waypointWidth = distFromCenterOfWP * 2 + boxPadding * 2.0f;
        final float waypointHeight = 8.0f + boxPadding * 2.0f;
        final float f3 = 1.0f * ((float)Math.max(0.0, Math.min(10.0, this.getPlayerDistanceToWaypoint(waypoint, Minecraft.getMinecraft().thePlayer) - 4.0)) / 10.0f);
        if (this.dynamicBackgroundColor.getBoolean()) {
            GuiUtils.drawRect(outsideEdgeLeft, f2, outsideEdgeLeft + waypointWidth, waypointHeight, (int)(0.4f * f3 * 255.0f) << 24);
        }
        else {
            GuiUtils.drawRect(outsideEdgeLeft, f2, outsideEdgeLeft + waypointWidth, waypointHeight, this.staticBackgroundColor.getColor());
        }
        if (this.boxBorders.getBoolean()) {
            final int n2 = (int)(f3 * 255.0f) << 24 | (SCModule.getColor(waypoint.getColor()) & 0xFFFFFF);
            final float f4 = 0.5f;
            if (this.dynamicBackgroundColor.getBoolean()) {
                GuiUtils.drawRoundedOutline(outsideEdgeLeft, f2, outsideEdgeLeft + waypointWidth, waypointHeight, 0.0f, this.borderWidth.getFloat(), n2);
            }
            else {
                GuiUtils.drawRoundedOutline(outsideEdgeLeft, f2, outsideEdgeLeft + waypointWidth, waypointHeight, 0.0f, this.borderWidth.getFloat(), waypoint.getColor().getColor());
            }
        }
        if (this.fillWaypointWithWaypointColor.getBoolean()) {
            final int n2 = (int)(f3 * 255.0f) << 24 | (SCModule.getColor(waypoint.getColor()) & 0xFFFFFF);
            final float f4 = 0.5f;
            if (this.dynamicBackgroundColor.getBoolean()) {
                GuiUtils.drawRect(outsideEdgeLeft, f2, outsideEdgeLeft + waypointWidth, waypointHeight, n2);
            }
            else {
                GuiUtils.drawRect(outsideEdgeLeft, f2, outsideEdgeLeft + waypointWidth, waypointHeight, waypoint.getColor().getColor());
            }
        }
        GlStateManager.enableBlend();
        if (this.dynamicBackgroundColor.getBoolean()) {
            Minecraft.getMinecraft().fontRendererObj.drawString(waypointText, (float)(-distFromCenterOfWP), 0.0f + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2 - 2.0f, (int)Math.max(4.0f, f3 * 255.0f) << 24 | 0xFFFFFF, this.textShadow.getBoolean());
        }
        else {
            Minecraft.getMinecraft().fontRendererObj.drawString(waypointText, (float)(-distFromCenterOfWP), 0.0f + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2 - 2.0f, -1, this.textShadow.getBoolean());
        }
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
    
    private void drawTriangle(final float var1, final float var2, final float var3, final float var4, final float var5, final float var6, final float var7, final int var8) {
        try {
            final Tessellator t = Tessellator.getInstance();
            final WorldRenderer wr = t.getWorldRenderer();
            wr.startDrawing(4);
            wr.addVertex(var1, var4, 0.0);
            wr.addVertex(var2, var5, 0.0);
            wr.addVertex(var3, var6, 0.0);
            t.draw();
        }
        catch (Throwable var9) {
            throw var9;
        }
    }
    
    private void setGLWaypointColor(final int n) {
        final float f = (n >> 24 & 0xFF) / 255.0f;
        final float f2 = (n >> 16 & 0xFF) / 255.0f;
        final float f3 = (n >> 8 & 0xFF) / 255.0f;
        final float f4 = (n & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.color(f2, f3, f4, f);
    }
    
    private void postWaypointRender() {
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public double getPlayerDistanceToWaypoint(final Waypoint waypoint, final Entity entity) {
        final double d = waypoint.getLocation().xCoord - entity.posX;
        final double d2 = waypoint.getLocation().yCoord - entity.posY;
        final double d3 = waypoint.getLocation().zCoord - entity.posZ;
        return Math.sqrt(d * d + d2 * d2 + d3 * d3);
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.MECHANIC;
    }
}
