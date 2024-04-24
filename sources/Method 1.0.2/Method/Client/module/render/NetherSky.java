package Method.Client.module.render;


import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;
import static Method.Client.utils.visual.ColorUtils.colorcalc;

public class NetherSky extends Module {

    Setting mode = setmgr.add( new Setting("Mode", this, "Glint", "Glint", "Method"));
     Setting OverlayColor=  setmgr.add( new Setting("OverlayColor", this, 0, 1, 1, .62));


    @Override
    public void setup() {
        skyboxSpaceRenderer = new NetherSky.SkyboxSpaceRenderer();
    }


    public NetherSky() {
        super("NetherSky", Keyboard.KEY_NONE, Category.RENDER, "NetherSky");
    }

    private static NetherSky.ISpaceRenderer skyboxSpaceRenderer;
    private boolean wasChanged;

    @Override
    public void onEnable() {
        this.wasChanged = false;
    }

    @Override
    public void onDisable() {
        this.disableBackgroundRenderer(mc.player.world);
    }

    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!this.wasChanged) {
            this.enableBackgroundRenderer(mc.player.world);
            this.wasChanged = true;
        }
    }

    @Subscribe
    public void onWorldLoad(WorldEvent.Load event) {
        this.wasChanged = false;
    }

    @Subscribe
    public void onWorldUnload(WorldEvent.Unload event) {
        this.wasChanged = false;
    }

    private void enableBackgroundRenderer(World world) {
        if (world.provider.getDimensionType() == DimensionType.NETHER) {
            world.provider.setSkyRenderer(new IRenderHandler() {
                public void render(float partialTicks, WorldClient world, Minecraft mc) {
                    NetherSky.skyboxSpaceRenderer.render(NetherSky.this.mode);
                }
            });
        }

    }

    private void disableBackgroundRenderer(World world) {
        if (world.provider.getDimensionType() == DimensionType.NETHER) {
            world.provider.setSkyRenderer(new IRenderHandler() {
                public void render(float partialTicks, WorldClient world, Minecraft mc) {
                }
            });
        }

    }

    public class SkyboxSpaceRenderer implements NetherSky.ISpaceRenderer {

        public void render(Setting mode) {
            GlStateManager.disableFog();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.depthMask(false);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();

            for (int i = 0; i < 6; ++i) {
                if (mode.getValString().equalsIgnoreCase("Glint")) {

                    mc.getTextureManager().bindTexture(new ResourceLocation(Main.MODID, "none.png"));
                }

                if (mode.getValString().equalsIgnoreCase("Method")) {
                    mc.getTextureManager().bindTexture(new ResourceLocation(Main.MODID, "method.png"));
                }


                GlStateManager.pushMatrix();
                if (i == 1) {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 2) {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (i == 3) {
                    GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 4) {
                    GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (i == 5) {
                    GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                double size = 100.0D;
                float a = colorcalc(OverlayColor.getcolor(), 24);
                float r = colorcalc(OverlayColor.getcolor(), 16);
                float g = colorcalc(OverlayColor.getcolor(), 8);
                float b = colorcalc(OverlayColor.getcolor(), 0);
                bufferbuilder.pos(-size, -size, -size).tex(0.0D, 0.0D).color(r, g, b, a).endVertex();
                bufferbuilder.pos(-size, -size, size).tex(0.0D, 1.0D).color(r, g, b, a).endVertex();
                bufferbuilder.pos(size, -size, size).tex(1.0D, 1.0D).color(r, g, b, a).endVertex();
                bufferbuilder.pos(size, -size, -size).tex(1.0D, 0.0D).color(r, g, b, a).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            GlStateManager.enableAlpha();
        }
    }

    public interface ISpaceRenderer {
        void render(Setting var1);
    }

}
