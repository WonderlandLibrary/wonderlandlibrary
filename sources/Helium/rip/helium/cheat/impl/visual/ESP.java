package rip.helium.cheat.impl.visual;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import rip.helium.ChatUtil;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.cheat.FriendManager;
import rip.helium.cheat.impl.combat.aura.Aura;
import rip.helium.cheat.impl.misc.cheststealer.ChestStealer;
import rip.helium.event.minecraft.EntityRenderEvent;
import rip.helium.event.minecraft.RenderOverlayEvent;
import rip.helium.utils.ColorCreator;
import rip.helium.utils.Draw;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.font.Fonts;
import rip.helium.utils.property.impl.ColorProperty;
import rip.helium.utils.property.impl.StringsProperty;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author antja03
 */
public class ESP extends Cheat {

    public static Color colorr;

//    private BooleanProperty prop_chests = new BooleanProperty("Chests", "Enables chests for ESP", () -> false, false);
    private static final int brightness = 130;
    private static boolean ascending;
    private static final float[] hsb = new float[3];
    private final StringsProperty prop_elements = new StringsProperty("Elements",
            "The ESP elements that will be drawn.", null, true, false,
            new String[]{"Box", "Health", "Name", "Chests"}, new Boolean[]{false, false, false, false});
    private final ColorProperty prop_chestColor = new ColorProperty("Chest", "The color of normal chests.", null, 0.1f,
            1f, .64f, 90);
    private final StringsProperty prop_healthStyle = new StringsProperty("Health Style",
            "How the health element will be drawn.", () -> prop_elements.getValue().get("Health"), true, true,
            new String[]{"Number", "Bar"}, new Boolean[]{false, true});
    private final StringsProperty prop_entites = new StringsProperty("Entities", "The entites ESP will be drawn on.",
            () -> !prop_elements.getValue().isEmpty(), true, false,
            new String[]{"Players", "Monsters", "Animals", "Villagers", "Golems"},
            new Boolean[]{false, false, false, false, false});
    private final StringsProperty prop_avoid = new StringsProperty("Avoid", "Flags that ESP will avoid.",
            () -> !prop_elements.getValue().isEmpty(), true, false, new String[]{"Invisible", "Teammate", "Bots"},
            new Boolean[]{false, false, false});
    private final ColorProperty prop_boxColor = new ColorProperty("Box (default)", "The inner color of the box.",
            () -> prop_elements.getValue().get("Box"), 1f, 0f, 1f, 255);
    private final ColorProperty prop_boxTargetColor = new ColorProperty("Box (target)",
            "The inner color of the box on your current Kill aura target.", () -> prop_elements.getValue().get("Box"),
            0f, 1f, .64f, 255);
    private final ColorProperty prop_boxOutlineColor = new ColorProperty("Box outline", "The outer color of the box.",
            () -> prop_elements.getValue().get("Box"), 0f, 0f, 0f, 255);
    private final ColorProperty prop_nameColor = new ColorProperty("Name", "The color of the entities name.",
            () -> prop_elements.getValue().get("Name"), 1f, 0f, 1f, 255);
    private final ColorProperty prop_healthColor = new ColorProperty("Health Number",
            "The color of the entities health number.",
            () -> prop_elements.getValue().get("Health") && prop_healthStyle.getValue().get("Number"), 1f, 0f, 1f, 255);
    private final double[][] currentEntityPoints = new double[8][3];
    Stopwatch stopwatch = new Stopwatch();
    private FloatBuffer modelView = GLAllocation.createDirectFloatBuffer(16);
    private FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private FloatBuffer screenCords = GLAllocation.createDirectFloatBuffer(3);
    private double renderPosX1 = Double.MAX_VALUE;
    private double renderPosY1 = Double.MAX_VALUE;
    private double renderPosX2 = -1;
    private double renderPosY2 = -1;
    private float hue;
    private Hud hud;

    private Aura cheat_killAura;
    private ChestStealer cheat_chestStealer;

    public ESP() {
        super("ESP", "Highlights the position of entites.", CheatCategory.VISUAL);
        registerProperties(prop_chestColor, prop_elements, prop_healthStyle, prop_entites, prop_avoid, prop_boxColor,
                prop_boxTargetColor, prop_boxOutlineColor, prop_chestColor, prop_nameColor, prop_healthColor);
    }

    @Override
    public void onEnable() {
        if (cheat_killAura == null) {
            cheat_killAura = (Aura) Helium.instance.cheatManager.getCheatRegistry().get("aura");
        }
        if (prop_elements.getValue().get("Chests")) {
            if (cheat_chestStealer == null) {
                cheat_chestStealer = (ChestStealer) Helium.instance.cheatManager.getCheatRegistry()
                        .get("ChestStealer");
            }
        }
    }

    @Collect
    public void onEntityRender(EntityRenderEvent event) {

        for (TileEntity tileEntity : mc.theWorld.loadedTileEntityList) {
            if (tileEntity instanceof TileEntityChest) {
                Color color = prop_chestColor.getValue();

                double renderX = tileEntity.getPos().getX() - getMc().getRenderManager().renderPosX;
                double renderY = tileEntity.getPos().getY() - getMc().getRenderManager().renderPosY;
                double renderZ = tileEntity.getPos().getZ() - getMc().getRenderManager().renderPosZ;
                GL11.glTranslated(renderX, renderY, renderZ);
                drawChestEsp(tileEntity, 0.0D, 0.0D, 0.0D, color.getRed(), color.getGreen(), color.getBlue(),
                        color.getAlpha());
                GL11.glTranslated(-renderX, -renderY, -renderZ);
            }
        }
    }

    public int getPlayerColor(EntityLivingBase e) {
        int color;
        if (FriendManager.friends.contains(e.getName())) {
            color = new Color(55, 255, 0).getRGB();
        } else if (e == Aura.getCurrentTarget()) {
            color = new Color(255, 0, 0).getRGB();
        } else {
            color = new Color(0, 0, 0).getRGB();
        }
        return color;
    }

    public void drawChestEsp(TileEntity tileEntity, double x, double y, double z, int r, int g, int b, int a) {
        if (prop_elements.getValue().get("Chests")) {
            double xOff = 0;
            double zOff = 0;
            double xOff2 = 0;
            double zOff2 = 0;

            if (tileEntity instanceof TileEntityChest) {
                TileEntityChest tileEntityChest = (TileEntityChest) tileEntity;
                if (tileEntityChest.adjacentChestXPos != null) {
                    xOff = -1;
                    xOff2 = -1;
                } else if (tileEntityChest.adjacentChestXNeg != null) {
                    xOff = -1 - 0.002;
                    xOff2 = 0.0125;
                } else if (tileEntityChest.adjacentChestZPos != null) {
                    zOff = -1;
                    zOff2 = -1;
                } else if (tileEntityChest.adjacentChestZNeg != null) {
                    zOff = -1 - 0.002;
                    zOff2 = 0.0125;
                }
            }

            if (xOff == -1 || xOff2 == -1 || zOff == -1 || zOff2 == -1)
                return;

            GlStateManager.pushAttrib();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GL11.glBlendFunc(770, 771);

            GlStateManager.disableLighting();
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glColor4f(r / 255f, g / 255f, b / 255f, a / 255f);
            GL11.glLineWidth(1.5F);

            GL11.glBegin(7);
            GL11.glVertex3d(x + 0.0625 + xOff, y, z + 0.0625 + zOff);
            GL11.glVertex3d(x + 0.0625 + xOff, y - (0.062f * 2) + 1.0D, z + 0.0625 + zOff);
            GL11.glVertex3d(x - 0.0625 + xOff2 + 1.0D, y - (0.062f * 2) + 1.0D, z + 0.0625 + zOff);
            GL11.glVertex3d(x - 0.0625 + xOff2 + 1.0D, y, z + 0.0625 + zOff);
            GL11.glVertex3d(x - 0.0625 + xOff2 + 1.0D, y, z + 0.0625 + zOff);
            GL11.glVertex3d(x - 0.0625 + xOff2 + 1.0D, y - (0.062f * 2) + 1.0D, z + 0.0625 + zOff);
            GL11.glVertex3d(x - 0.0625 + xOff2 + 1.0D, y - (0.062f * 2) + 1.0D, z - 0.0625 + zOff2 + 1.0D);
            GL11.glVertex3d(x - 0.0625 + xOff2 + 1.0D, y, z - 0.0625 + zOff2 + 1.0D);
            GL11.glVertex3d(x - 0.0625 + xOff2 + 1.0D, y, z - 0.0625 + zOff2 + 1.0D);
            GL11.glVertex3d(x - 0.0625 + xOff2 + 1.0D, y - (0.062f * 2) + 1.0D, z - 0.0625 + zOff2 + 1.0D);
            GL11.glVertex3d(x + 0.0625 + xOff, y - (0.062f * 2) + 1.0D, z - 0.0625 + zOff2 + 1.0D);
            GL11.glVertex3d(x + 0.0625 + xOff, y, z - 0.0625 + zOff2 + 1.0D);
            GL11.glVertex3d(x + 0.0625 + xOff, y, z - 0.0625 + zOff2 + 1.0D);
            GL11.glVertex3d(x + 0.0625 + xOff, y - (0.062f * 2) + 1.0D, z - 0.0625 + zOff2 + 1.0D);
            GL11.glVertex3d(x + 0.0625 + xOff, y - (0.062f * 2) + 1.0D, z + 0.0625 + zOff);
            GL11.glVertex3d(x + 0.0625 + xOff, y, z + 0.0625 + zOff);
            GL11.glVertex3d(x + 0.0625 + xOff, y, z + 0.0625 + zOff);
            GL11.glVertex3d(x - 0.0625 + xOff2 + 1.0D, y, z + 0.0625 + zOff);
            GL11.glVertex3d(x - 0.0625 + xOff2 + 1.0D, y, z - 0.0625 + zOff2 + 1.0D);
            GL11.glVertex3d(x + 0.0625 + xOff, y, z - 0.0625 + zOff2 + 1.0D);
            GL11.glVertex3d(x + 0.0625 + xOff, y - (0.062f * 2) + 1.0D, z + 0.0625 + zOff);
            GL11.glVertex3d(x - 0.0625 + xOff2 + 1.0D, y - (0.062f * 2) + 1.0D, z + 0.0625 + zOff);
            GL11.glVertex3d(x - 0.0625 + xOff2 + 1.0D, y - (0.062f * 2) + 1.0D, z - 0.0625 + zOff2 + 1.0D);
            GL11.glVertex3d(x + 0.0625 + xOff, y - (0.062f * 2) + 1.0D, z - 0.0625 + zOff2 + 1.0D);
            GL11.glEnd();

            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            GlStateManager.popAttrib();
        }
    }


    @SuppressWarnings("unused")
    @Collect
    public void onRenderOverlay(RenderOverlayEvent renderOverlayEvent) {
        GL11.glPushMatrix();

        for (Object e : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (e instanceof EntityItem) {
                //e.
                //RenderUtil.entityESPBox((Entity)e, 2);
                //getPlayer().getDistanceSqToCenter(pos)

            }
        }

        entityLoop:
        for (EntityLivingBase entity : collectEntities()) {
            Minecraft.getMinecraft().entityRenderer
                    .setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);

            AxisAlignedBB boundingBox = entity.getEntityBoundingBox().expand(0.05f, 0.05f, 0.05f).offset(0, 0.05, 0);
            currentEntityPoints[0] = project2D(
                    boundingBox.minX - RenderManager.renderPosX,
                    boundingBox.minY - RenderManager.renderPosY,
                    boundingBox.minZ - RenderManager.renderPosZ);
            currentEntityPoints[1] = project2D(
                    boundingBox.minX - RenderManager.renderPosX,
                    boundingBox.minY - RenderManager.renderPosY,
                    boundingBox.maxZ - RenderManager.renderPosZ);
            currentEntityPoints[2] = project2D(
                    boundingBox.maxX - RenderManager.renderPosX,
                    boundingBox.minY - RenderManager.renderPosY,
                    boundingBox.minZ - RenderManager.renderPosZ);
            currentEntityPoints[3] = project2D(
                    boundingBox.maxX - RenderManager.renderPosX,
                    boundingBox.minY - RenderManager.renderPosY,
                    boundingBox.maxZ - RenderManager.renderPosZ);
            currentEntityPoints[4] = project2D(
                    boundingBox.minX - RenderManager.renderPosX,
                    boundingBox.maxY - RenderManager.renderPosY,
                    boundingBox.minZ - RenderManager.renderPosZ);
            currentEntityPoints[5] = project2D(
                    boundingBox.minX - RenderManager.renderPosX,
                    boundingBox.maxY - RenderManager.renderPosY,
                    boundingBox.maxZ - RenderManager.renderPosZ);
            currentEntityPoints[6] = project2D(
                    boundingBox.maxX - RenderManager.renderPosX,
                    boundingBox.maxY - RenderManager.renderPosY,
                    boundingBox.minZ - RenderManager.renderPosZ);
            currentEntityPoints[7] = project2D(
                    boundingBox.maxX - RenderManager.renderPosX,
                    boundingBox.maxY - RenderManager.renderPosY,
                    boundingBox.maxZ - RenderManager.renderPosZ);

            renderPosX1 = Double.MAX_VALUE;
            renderPosY1 = Double.MAX_VALUE;
            renderPosX2 = -1;
            renderPosY2 = -1;

            pointChecks:
            for (double[] point : currentEntityPoints) {
                if (point == null)
                    continue entityLoop;

                if (point[2] < 0.0f || point[2] > 1.0f)
                    continue entityLoop;

                point[1] = (Display.getHeight() / 2 - point[1]);

                if (point[0] < renderPosX1)
                    renderPosX1 = point[0];

                if (point[0] > renderPosX2)
                    renderPosX2 = point[0];

                if (point[1] < renderPosY1)
                    renderPosY1 = point[1];

                if (point[1] > renderPosY2)
                    renderPosY2 = point[1];
            }

            int guiScale = getMc().gameSettings.guiScale;
            getMc().gameSettings.guiScale = 2;
            Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
            getMc().gameSettings.guiScale = guiScale;
            if (cheat_killAura == null) {
                cheat_killAura = (Aura) Helium.instance.cheatManager.getCheatRegistry().get("KillAura");
            }
            if (prop_elements.getValue().get("Box")) {
                // LEFT
                if (cheat_killAura != null && Aura.getCurrentTarget() != null) {
					/*/Draw.drawRectangle(renderPosX1, renderPosY1, renderPosX1 + 1.5, renderPosY2,
							cheat_killAura.getCurrentTarget() != null && cheat_killAura.getCurrentTarget() == entity
									|| entity.getName().toLowerCase().contains("bobicraft")
											? prop_boxTargetColor.getValue().getRGB()
											: prop_boxColor.getValue().getRGB());
					// TOP
					Draw.drawRectangle(renderPosX1, renderPosY1, renderPosX2, renderPosY1 + 1.5,
							cheat_killAura.getCurrentTarget() != null && cheat_killAura.getCurrentTarget() == entity
									|| entity.getName().toLowerCase().contains("bobicraft")
											? prop_boxTargetColor.getValue().getRGB()
											: prop_boxColor.getValue().getRGB());
					// RIGHT
					Draw.drawRectangle(renderPosX2 - 1.5, renderPosY1, renderPosX2, renderPosY2,
							cheat_killAura.getCurrentTarget() != null && cheat_killAura.getCurrentTarget() == entity
									|| entity.getName().toLowerCase().contains("bobicraft")
											? prop_boxTargetColor.getValue().getRGB()
											: prop_boxColor.getValue().getRGB());
					// BOTTOM
					Draw.drawRectangle(renderPosX1, renderPosY2 - 1.5, renderPosX2, renderPosY2,
							cheat_killAura.getCurrentTarget() != null && cheat_killAura.getCurrentTarget() == entity
									|| entity.getName().toLowerCase().contains("bobicraft")
											? prop_boxTargetColor.getValue().getRGB()
											: prop_boxColor.getValue().getRGB());
											/*/

                }


                // LEFT
				/*/Draw.drawRectangle(renderPosX1 + 1.5, renderPosY1 + 1.5, renderPosX1 + 1.0, renderPosY2 - 1.5,
						prop_boxOutlineColor.getValue().getRGB());
				// TOP
				Draw.drawRectangle(renderPosX1 + 1.5, renderPosY1 + 1.5, renderPosX2 - 1.5, renderPosY1 + 1.0,
						prop_boxOutlineColor.getValue().getRGB());
				// RIGHT
				Draw.drawRectangle(renderPosX2 - 1.0, renderPosY1 + 1.5, renderPosX2 - 1.5, renderPosY2 - 1.5,
						prop_boxOutlineColor.getValue().getRGB());
				// BOTTOM
				Draw.drawRectangle(renderPosX1 + 1.5, renderPosY2 - 1.0, renderPosX2 - 1.5, renderPosY2 - 1.5,
						prop_boxOutlineColor.getValue().getRGB());/*/

                // LEFT
                Draw.drawRectangle(renderPosX1, renderPosY1 - 1.5, renderPosX1 - 1.5, renderPosY2 + 1.5,
                        getPlayerColor(entity));
                // TOP
                Draw.drawRectangle(renderPosX1 - 1.5, renderPosY1, renderPosX2 + 1.5, renderPosY1 - 1.5,
                        getPlayerColor(entity));
                // RIGHT
                Draw.drawRectangle(renderPosX2 + 1.5, renderPosY1 - 1.5, renderPosX2, renderPosY2 + 1.5,
                        getPlayerColor(entity));
                // BOTTOM
                Draw.drawRectangle(renderPosX1 - 1.5, renderPosY2 + 1.5, renderPosX2 + 1.5, renderPosY2,
                        getPlayerColor(entity));


            }

            if (prop_elements.getValue().get("Name")) {
                //ChatUtil.chat("Please enable NameTags for this feature");
                if (stopwatch.hasPassed(2000)) {
                    ChatUtil.chat("§cThis option is not in use anymore. Please use nametags or MemeESP instead.");
                    stopwatch.reset();
                }
            }

            if (prop_elements.getValue().get("Health")) {
                if (prop_healthStyle.getValue().get("Bar")) {

                    double hpPercentage = entity.getHealth() / 20;
                    if (hpPercentage > 1)
                        hpPercentage = 1;
                    else if (hpPercentage < 0)
                        hpPercentage = 0;

                    double height = (renderPosY2 - renderPosY1) * hpPercentage;

                    int r = (int) (230 + (50 - 230) * hpPercentage);
                    int g = (int) (50 + (230 - 50) * hpPercentage);
                    int b = 50;

                    Draw.drawBorderedRectangle(renderPosX1 - 3, renderPosY2 - height, renderPosX1 - 1.5, renderPosY2,
                            1.5F, ColorCreator.create(r, g, b, 255), ColorCreator.create(0, 0, 0), true);
                }

                if (prop_healthStyle.getValue().get("Number")) {
                    GlStateManager.pushMatrix();
                    float scale = 1.0f / (getPlayer().getDistanceToEntity(entity) / 10f);
                    if (scale < 0.9f)
                        scale = 0.9f;
                    if (scale > 1.0f)
                        scale = 1.0f;

                    GlStateManager.scale(scale, scale, scale);

                    if (prop_healthStyle.getValue().get("Bar")) {
                        double hpPercentage = entity.getHealth() / 20;
                        if (hpPercentage > 1)
                            hpPercentage = 1;
                        else if (hpPercentage < 0)
                            hpPercentage = 0;

                        double height = (renderPosY2 - renderPosY1) * hpPercentage;

                        Fonts.f12.drawStringWithShadow((int) (entity.getHealth() / 20 * 100) + "%",
                                renderPosX1 / scale - 4
                                        - Fonts.f12.getStringWidth(
                                        (int) (entity.getHealth() / 20 * 100) + "%"),
                                renderPosY2 / scale - height - 3, prop_healthColor.getValue().getRGB());
                    } else {
                        Fonts.f12.drawStringWithShadow((int) (entity.getHealth() / 20 * 100) + "%",
                                renderPosX1 / scale - 4
                                        - Fonts.f12.getStringWidth(
                                        (int) (entity.getHealth() / 20 * 100) + "%"),
                                (renderPosY1 + 2) / scale, prop_healthColor.getValue().getRGB());
                    }

                    GlStateManager.popMatrix();
                }
            }
        }
        GL11.glPopMatrix();
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
    }

    private boolean isValidEntity(Entity entity) {
        if (cheat_killAura == null) {
            cheat_killAura = (Aura) Helium.instance.cheatManager.getCheatRegistry().get("KillAura");
        }
        if (entity == getPlayer()
                || (prop_avoid.getValue().get("Bots") && cheat_killAura.ignoredEntities.contains(entity))
                || (prop_avoid.getValue().get("Invisible") && entity.isInvisible()))
            return false;

        boolean selectedEntity = (entity instanceof EntityPlayer && prop_entites.getValue().get("Players"))
                || (entity instanceof EntityVillager && prop_entites.getValue().get("Villagers"))
                || (entity instanceof EntityGolem && prop_entites.getValue().get("Golems"))
                || (entity instanceof EntityAnimal && prop_entites.getValue().get("Animals"))
                || (entity instanceof EntityMob && prop_entites.getValue().get("Monsters"));

        boolean invis = !prop_avoid.getValue().get("Invisible")
                || (!entity.isInvisible() && !entity.isInvisibleToPlayer(getPlayer()));
        boolean npcCheck = true;

        if (getMc().getCurrentServerData() != null && getMc().getCurrentServerData().serverIP.contains("hypixel")) {
            if (entity.getDisplayName().toString().contains("§8[NPC]")) {
                npcCheck = false;
            }
        }

        return selectedEntity && invis && npcCheck;
    }

    public List<EntityLivingBase> collectEntities() {
        return getWorld().loadedEntityList.stream().filter(EntityLivingBase.class::isInstance)
                .map(EntityLivingBase.class::cast).filter(entity -> isValidEntity(entity)).collect(Collectors.toList());
    }

    private double[] project2D(double posX, double posY, double posZ) {
        modelView = GLAllocation.createDirectFloatBuffer(16);
        projection = GLAllocation.createDirectFloatBuffer(16);
        viewport = GLAllocation.createDirectIntBuffer(16);
        screenCords = GLAllocation.createDirectFloatBuffer(3);

        modelView.clear();
        projection.clear();
        viewport.clear();
        screenCords.clear();

        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);

        if (GLU.gluProject((float) posX, (float) posY, (float) posZ, modelView, projection, viewport, screenCords)) {
            return new double[]{screenCords.get(0) / 2.0f, screenCords.get(1) / 2.0f, screenCords.get(2)};
        }

        return null;
    }

}
