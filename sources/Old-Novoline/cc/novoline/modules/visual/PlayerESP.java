package cc.novoline.modules.visual;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.Render2DEvent;
import cc.novoline.events.events.Render3DEvent;
import cc.novoline.events.events.RenderNameTagEvent;
import cc.novoline.events.events.UpdateModelEvent;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.PlayerManager;
import cc.novoline.modules.combat.KillAura;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.*;
import cc.novoline.utils.PlayerUtils;
import cc.novoline.utils.RenderUtils;
import cc.novoline.utils.ScaleUtils;
import cc.novoline.utils.Timer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.*;

import static cc.novoline.gui.screen.setting.Manager.put;
import static cc.novoline.gui.screen.setting.SettingType.CHECKBOX;
import static cc.novoline.gui.screen.setting.SettingType.COLOR_PICKER;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createColor;

public final class PlayerESP extends AbstractModule {


    private final Color WHITE = new Color(255, 255, 255);
    private final Color TEAM = new Color(0, 231, 255, 255);
    private final Color TARGET = new Color(255, 59, 59, 255);
    private final Color FRIEND = new Color(169, 255, 43, 255);
    private final Color BLACK = new Color(0, 0, 0, 100);

    private int percent = 0;

    private Timer tpTimer = new Timer();

    /* properties @off */
    private final List<EntityPlayer> collectedEntities = new ArrayList<>();
    private final Map<EntityPlayer, float[][]> playerRotationMap = new WeakHashMap();

    private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private final FloatBuffer modelView = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);

    @Property("pESP-filter")
    private final ListProperty<String> filter = PropertyFactory.createList("Health").acceptableValues("Health", "Armor", "Box", "Name", "Skeleton","Held Item");
    @Property("pESP-hurtTime")
    private final BooleanProperty hurtTimeCheck = PropertyFactory.booleanFalse();
    @Property("pESP-boxColor")
    private final ColorProperty color = createColor(0xFF8A8AFF);
    @Property("pESP-nameColor")
    private final ColorProperty nameColor = createColor(new Color(245, 255, 94).getRGB());
    @Property("pESP-targetcolor")
    private final ColorProperty targetColor = createColor(TARGET.getRGB());
    @Property("pESP-friendColor")
    private final ColorProperty friendColor = createColor(FRIEND.getRGB());
    @Property("pESP-box_mode")
    private final StringProperty boxMode = PropertyFactory.createString("Bordered").acceptableValues("Basic", "Bordered");
    @Property("pESP-box_color_mode")
    private final StringProperty boxColorMode = PropertyFactory.createString("Static").acceptableValues("Static", "Rainbow", "Astolfo");
    @Property("pESP-box_style")
    private final StringProperty boxStyle = PropertyFactory.createString("Full").acceptableValues("Full", "Corner");
    @Property("pESP-health-mode")
    private final StringProperty healthMode = PropertyFactory.createString("Hearts").acceptableValues("Hearts", "HP", "Percentage");
    @Property("pESP-name-color-type")
    private final StringProperty nameColorType = PropertyFactory.createString("Team").acceptableValues("Team", "Custom");
    @Property("pESP-onlyTargets")
    private final BooleanProperty onlyTargets = PropertyFactory.booleanFalse();
    @Property("bg-transparency")
    private final IntProperty transparency = PropertyFactory.createInt(100).minimum(0).maximum(255);

    /* constructors @on */
    public PlayerESP(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "PlayerESP", "Player ESP", Keyboard.KEY_NONE, EnumModuleType.VISUALS, "fucking cs:go shit");
        put(new Setting("PESP_FILTER", "Filter", SettingType.SELECTBOX, this, this.filter));
        put(new Setting("BOX_MODE", "Health", SettingType.COMBOBOX, this, this.healthMode, () -> filter.get().contains("Health")));
        put(new Setting("BOX_MODE", "Box mode", SettingType.COMBOBOX, this, this.boxMode, () -> filter.get().contains("Box")));
        put(new Setting("BOX_MODE", "Box Style", SettingType.COMBOBOX, this, this.boxStyle, () -> filter.get().contains("Box")));
        put(new Setting("BOX_COLOR_MODE", "Box Color", SettingType.COMBOBOX, this, this.boxColorMode, () -> filter.get().contains("Box")));
        put(new Setting("BOX_COLOR", "Color", COLOR_PICKER, this, this.color, null, () -> filter.get().contains("Box")));
        put(new Setting("PESP-ONLYTARGETS", "Targets Only", CHECKBOX, this, onlyTargets));
        put(new Setting("PESP-HT", "HurtTime Check", SettingType.CHECKBOX, this, this.hurtTimeCheck, () -> filter.get().contains("Box")));
        put(new Setting("PEST-TRANSPARENCY", "Transparency", SettingType.SLIDER, this, transparency, 5, () -> filter.get().contains("Name")));
        put(new Setting("NAME_COLOR_MODE", "Name Color", SettingType.COMBOBOX, this, this.nameColorType, () -> filter.get().contains("Name")));
        put(new Setting("NAME_COLOR", "Color", COLOR_PICKER, this, this.nameColor, null, () -> filter.get().contains("Name") && nameColorType.get().equals("Custom")));
        put(new Setting("NAME_COLOR_TARGET", "Target Color", COLOR_PICKER, this, this.targetColor, null, () -> filter.get().contains("Name") && nameColorType.get().equals("Custom")));
        put(new Setting("NAME_COLOR_FRIEND", "Friend Color", COLOR_PICKER, this, this.friendColor, null, () -> filter.get().contains("Name") && nameColorType.get().equals("Custom")));
    }


    @EventTarget
    public void onRenderTag(RenderNameTagEvent event) {
        event.setCancelled(filter.contains("Name") && (!onlyTargets.get() || novoline.getPlayerManager().hasType(event.getEntity().getName(),
                PlayerManager.EnumPlayerType.TARGET)) || getModule(Nametags.class).isEnabled());
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        GL11.glPushMatrix();
        this.collectedEntities.clear();
        collectEntities();

        final double scaling = event.getResolution().getScaleFactor() / Math
                .pow(event.getResolution().getScaleFactor(), 2.0);
        GlStateManager.scale(scaling, scaling, scaling);

        for (EntityPlayer entity : this.collectedEntities) {
            if (onlyTargets.get() && !novoline.getPlayerManager().hasType(entity.getName(), PlayerManager.EnumPlayerType.TARGET))
                continue;
            if (entity == mc.player && mc.gameSettings.thirdPersonView == 0)
                continue;
            if (isValid(entity) && RenderUtils.isInViewFrustrum(entity)) {
                final double x = RenderUtils.interpolate(entity.posX, entity.lastTickPosX, event.getPartialTicks()), // @off
                        y = RenderUtils.interpolate(entity.posY, entity.lastTickPosY, event.getPartialTicks()),
                        z = RenderUtils.interpolate(entity.posZ, entity.lastTickPosZ, event.getPartialTicks()),
                        width = entity.width / 1.4,
                        height = entity.height + 0.2; // @on

                AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ),
                        new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ),
                        new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ),
                        new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ),
                        new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));

                this.mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);

                Vector4d position = null;

                for (Vector3d vector : vectors) {
                    vector = project2D(event.getResolution(), vector.x - this.mc.getRenderManager().viewerPosX,
                            vector.y - this.mc.getRenderManager().viewerPosY,
                            vector.z - this.mc.getRenderManager().viewerPosZ);

                    if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                        if (position == null) {
                            position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                        }

                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }

                this.mc.entityRenderer.setupOverlayRendering();

                if (position != null) {
                    double posX = position.x, // @off
                            posY = position.y,
                            endPosX = position.z,
                            endPosY = position.w,
                            length = Math.abs(endPosY - posY),
                            difference = posY - endPosY + 0.5; // @on


                    float amp = 1;
                    switch (mc.gameSettings.guiScale) {
                        case 0:
                            amp = 0.5F;
                            break;
                        case 1:
                            amp = 2.0F;
                            break;
                        case 3:
                            amp = 0.6666666666666667F;
                    }

                    double[] positions = ScaleUtils.getScaledMouseCoordinates(mc, posX, posY);
                    double[] positionsEnd = ScaleUtils.getScaledMouseCoordinates(mc, endPosX, endPosY);
                    double[] scaledPositions = new double[]{positions[0] * 2, positions[1] * 2, positionsEnd[0] * 2, positionsEnd[1] * 2};

                    final ScaledResolution resolution = new ScaledResolution(mc);

                    float width1 = resolution.getScaledWidth() / 2F;
                    float height1 = resolution.getScaledHeight() / 2F;
                    double centreX = posX + (endPosX - posX) / 2;
                    double centreY = posY + (endPosY - posY) / 2;

                    if (centreX <= width1 + (endPosX - posX) / 2 && centreX >= width1 - (endPosX - posX) / 2 &&
                            centreY <= height1 + (endPosY - posY) / 2 && centreY >= height1 - (endPosY - posY) / 2 && mc.gameSettings.thirdPersonView == 0) {
                        GL11.glPushMatrix();
                        GL11.glScalef(0.5f * amp, 0.5f * amp, 0.5f * amp);

                        double _width = Math.abs(scaledPositions[2] - scaledPositions[0]);
                        float v = (float) (mc.fontRendererCrack.getHeight() * 2) - mc.fontRendererCrack.getHeight() / 2;

                        mc.fontRendererCrack.drawStringWithShadow("Target", (float) (scaledPositions[0] + _width / 2 -
                                mc.fontRendererCrack.getStringWidth("Target") / 2), (float) scaledPositions[3] + mc.fontRendererCrack.getHeight() / 2, 0xffffffff);

                        if (Mouse.isButtonDown(2)) {
                            if (tpTimer.delay(150)) {
                                mc.player.sendChatMessage(".tar " + entity.getName().toLowerCase());
                                tpTimer.reset();
                            }
                        }

                        GL11.glPopMatrix();

                    }

                    if(filter.contains("Held Item")){
                        GL11.glPushMatrix();
                        GL11.glScalef(0.5f * amp, 0.5f * amp, 0.5f * amp);
                        float v = (float) (mc.fontRendererCrack.getHeight() * 2) - mc.fontRendererCrack.getHeight() / 2;
                        double _width = Math.abs(scaledPositions[2] - scaledPositions[0]);

                        if(entity.getHeldItem() != null) {
                            Gui.drawRect(
                                    (float) (scaledPositions[0] + _width / 2 -
                                            mc.fontRendererCrack.getStringWidth(entity.getHeldItem().getDisplayName()) / 2) - 2,
                                    (float) scaledPositions[3] + v / 3f - 2,
                                    (float) (scaledPositions[0] + _width / 2 -
                                            mc.fontRendererCrack.getStringWidth(entity.getHeldItem().getDisplayName()) / 2) + mc.fontRendererCrack.getStringWidth(entity.getHeldItem().getDisplayName()) + 1,
                                    scaledPositions[3] + v / 3f + mc.fontRendererCrack.getHeight() + 1,
                                    new Color(0, 0, 0, 255 - transparency.get()).getRGB());
                            mc.fontRendererCrack.drawStringWithShadow(entity.getHeldItem().getDisplayName(), (float) (scaledPositions[0] + _width / 2 -
                                    mc.fontRendererCrack.getStringWidth(entity.getHeldItem().getDisplayName()) / 2), (float) scaledPositions[3] + v / 3f, 0xffffffff);
                            double newY = scaledPositions[3] + v / 3f + v;
                            if(entity.getHeldItem().getTagCompound() != null){
                                if(entity.getHeldItem().getTagCompound().toString().contains("Lore:")) {
                                    String[] split = entity.getHeldItem().getTagCompound().toString().split("\"");
                                    for (String s : split) {
                                        if(s.startsWith("§9") && !s.contains("+") && !s.contains("Unbreakable")){
                                            s = s.replace("§9","");
                                            Gui.drawRect(
                                                    (float) (scaledPositions[0] + _width / 2 -
                                                            mc.fontRendererCrack.getStringWidth(s) / 2) - 2,
                                                    (float) newY - 2,
                                                    (float) (scaledPositions[0] + _width / 2 -
                                                            mc.fontRendererCrack.getStringWidth(s) / 2) + mc.fontRendererCrack.getStringWidth(s) + 1,
                                                    newY + mc.fontRendererCrack.getHeight() + 1,
                                                    new Color(0, 0, 0, 255 - transparency.get()).getRGB());
                                            mc.fontRendererCrack.drawStringWithShadow(EnumChatFormatting.AQUA + s, (float) (scaledPositions[0] + _width / 2 -
                                                    mc.fontRendererCrack.getStringWidth(s) / 2), (float) newY, 0xffffffff);
                                            newY+=v;
                                        }
                                    }
                                }
                            }
                            if(entity.inventory.armorInventory[1] != null){
                                if(entity.inventory.armorInventory[1].getTagCompound() != null){
                                    if(entity.inventory.armorInventory[1].getTagCompound().toString().contains("Lore:")){
                                        String[] split = entity.inventory.armorInventory[1].getTagCompound().toString().split("\"");
                                        for (String s : split) {
                                            if (s.length() != 1 && s.startsWith("§9") && !s.contains("Pants") && !s.contains("Also, a fashion") && !s.contains("Used in the") && !s.contains("As strong")) {
                                                s = s.replace("§9","");
                                                Gui.drawRect(
                                                        (float) (scaledPositions[0] + _width / 2 -
                                                                mc.fontRendererCrack.getStringWidth(s) / 2) - 2,
                                                        (float) newY - 2,
                                                        (float) (scaledPositions[0] + _width / 2 -
                                                                mc.fontRendererCrack.getStringWidth(s) / 2) + mc.fontRendererCrack.getStringWidth(s) + 1,
                                                        newY + mc.fontRendererCrack.getHeight() + 1,
                                                        new Color(0, 0, 0, 255 - transparency.get()).getRGB());
                                                mc.fontRendererCrack.drawStringWithShadow(EnumChatFormatting.GOLD + s, (float) (scaledPositions[0] + _width / 2 -
                                                        mc.fontRendererCrack.getStringWidth(s) / 2), (float) newY, 0xffffffff);
                                                newY+=v;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        GL11.glPopMatrix();
                    }


                    if (filter.contains("Name")) {
                        GL11.glPushMatrix();
                        GL11.glScalef(0.5f * amp, 0.5f * amp, 0.5f * amp);
                        double _width = Math.abs(scaledPositions[2] - scaledPositions[0]);

                        int color;

                        if (nameColorType.get().equals("Team")) {
                            color = new Color(255, 99, 99).getRGB();
                            if (onlyTargets.get()) {
                                color = WHITE.getRGB();
                            } else {
                                if (novoline.playerManager.hasType(entity.getName().toLowerCase(), PlayerManager.EnumPlayerType.FRIEND)) {
                                    color = FRIEND.getRGB();
                                } else if (novoline.playerManager.hasType(entity.getName().toLowerCase(), PlayerManager.EnumPlayerType.TARGET)) {
                                    color = TARGET.getRGB();
                                } else if (PlayerUtils.inTeam(mc.player, entity) && getModule(KillAura.class).getFilters().get().contains("Teams")) {
                                    color = TEAM.getRGB();
                                }
                            }
                        } else {
                            color = nameColor.getAwtColor().getRGB();
                            if (novoline.playerManager.hasType(entity.getName().toLowerCase(), PlayerManager.EnumPlayerType.FRIEND)) {
                                color = friendColor.getAwtColor().getRGB();
                            } else if (novoline.playerManager.hasType(entity.getName().toLowerCase(), PlayerManager.EnumPlayerType.TARGET)) {
                                color = targetColor.getAwtColor().getRGB();
                            }
                        }

                        String name = entity.getName();
//                        UserEntity user = novoline.getIRCUser(name);
//
//                        if (!name.isEmpty() && user != null) {
//                            name += " \u00A77(\u00A7b" + user.getUsername() + "\u00A77)\u00A7r";
//                        }
//                        if (entity.getCustomNameTag().startsWith("Desynced - ")) {
//                            name = EnumChatFormatting.YELLOW + "Your position for " + name;
//                        }

                        float v = (float) (mc.fontRendererCrack.getHeight() * 2) - mc.fontRendererCrack.getHeight() / 2;
                        Gui.drawRect(
                                (float) (scaledPositions[0] + _width / 2 -
                                        mc.fontRendererCrack.getStringWidth(name) / 2) - 2,
                                (float) scaledPositions[1] - v - 2,
                                (float) (scaledPositions[0] + _width / 2 -
                                        mc.fontRendererCrack.getStringWidth(name) / 2) + mc.fontRendererCrack.getStringWidth(name) + 1,
                                scaledPositions[1] - v + mc.fontRendererCrack.getHeight() + 1,
                                new Color(0, 0, 0, 255 - transparency.get()).getRGB());
                        mc.fontRendererCrack.drawStringWithShadow(name, (float) (scaledPositions[0] + _width / 2 -
                                mc.fontRendererCrack.getStringWidth(name) / 2), (float) scaledPositions[1] - v, color);
                        GL11.glPopMatrix();
                    }


                    if (this.filter.contains("Health")) {
                        RenderUtils.start2D();
                        GL11.glPushMatrix();
                        double maxHealth = entity.getMaxHealth(), amplifier = 100 / maxHealth, space = length / 100;
                        float health = entity.getHealth();

                        if (health > maxHealth) {
                            health *= maxHealth / health;
                        }

                        int percent = (int) (health * amplifier);
                        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
                        RenderUtils.setColor(new Color(RenderUtils.getHealthColor(entity)));
                        GL11.glLineWidth(2);
                        GL11.glBegin(GL11.GL_LINE_STRIP);
                        GL11.glVertex2d(posX - 2, endPosY - percent * space);
                        GL11.glVertex2d(posX - 2, endPosY);
                        GL11.glEnd();
                        RenderUtils.setColor(new Color(40, 40, 40));
                        GL11.glLineWidth(1);
                        GL11.glBegin(GL11.GL_LINE_STRIP);
                        GL11.glVertex2d(posX - 2.3, endPosY - percent * space - 0.3);
                        GL11.glVertex2d(posX - 2.3, endPosY + 0.3);
                        GL11.glVertex2d(posX - 1.3, endPosY + 0.3);
                        GL11.glVertex2d(posX - 1.3, endPosY - percent * space - 0.3);
                        GL11.glVertex2d(posX - 2.3, endPosY - percent * space - 0.3);
                        GL11.glEnd();
                        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
                        GL11.glPopMatrix();
                        RenderUtils.stop2D();
                        GL11.glPushMatrix();

                        double[] healthPos = ScaleUtils.getScaledMouseCoordinates(mc, posX, endPosY - percent * space);
                        double[] newPos = {healthPos[0] * 1 / 0.48, healthPos[1] * 1 / 0.48};

                        String hp = "";

                        switch (healthMode.get()) {
                            case "Hearts":
                                hp = String.format("%.1f", entity.getHealth() / 2) + "" + EnumChatFormatting.RED + "❤";
                                break;
                            case "HP":
                                hp = (int) entity.getHealth() + " HP";
                                break;
                            case "Percentage":
                                hp = percent + "%";
                        }


                        GL11.glScalef(0.48f * amp, 0.48f * amp, 0.48f * amp);
                        if (entity.getHealth() != entity.getMaxHealth())
                            mc.fontRendererCrack.drawString("" + hp,
                                    (float) newPos[0] - mc.fontRendererCrack.getStringWidth("a" + hp),
                                    (float) newPos[1],
                                    0xffffffff, true);
                        GL11.glPopMatrix();
                    }

                    if (this.filter.contains("Box")) {
                        boolean bordered = this.boxMode.get().equalsIgnoreCase("Bordered");
                        if (boxColorMode.get().equals("Rainbow")
                                || boxColorMode.get().equals("Astolfo")) {
                            RenderUtils.drawRainbowBox(posX, posY, endPosX, endPosY, 2,
                                    boxStyle.get().equals("Corner"),
                                    boxColorMode.get().equals("Astolfo"), bordered);
                        } else {
                            if (boxStyle.get().equals("Full")) {
                                RenderUtils.drawBorderedBox(posX, posY, endPosX, endPosY, this.color.getAwtColor(), bordered);
                            } else {
                                if (bordered) {
                                    RenderUtils.drawCornerBox(posX, posY, endPosX, endPosY, 3, BLACK);
                                }

                                RenderUtils.drawCornerBox(posX, posY, endPosX, endPosY, 1, this.hurtTimeCheck.get() &&
                                        entity.hurtResistantTime >= 10 ? Color.RED : this.color.getAwtColor());
                            }
                        }

                    }


                    if (this.filter.contains("Armor")) {
                        int i = 0;
                        float posy = 0;

                        List<ItemStack> stuff = new ArrayList<>();

                        for (ItemStack stack : entity.inventory.armorInventory) {
                            if (stack != null) {
                                stuff.add(stack);
                            }
                        }

                        Collections.reverse(stuff);
                        GlStateManager.disableAlpha();
                        GlStateManager.clear(256);
                        GlStateManager.enableBlend();
                        mc.getRenderItem().zLevel = -150.0F;
                        GL11.glScalef(0.5f * amp, 0.5f * amp, 0.5f * amp);
                        for (ItemStack item : stuff) {
                            if (mc.world != null) {
                                RenderHelper.enableGUIStandardItemLighting();
                            }
                            mc.getRenderItem().renderItemAndEffectIntoGUI(item, (float) scaledPositions[2] + 2, (float) scaledPositions[1] + posy - mc.player.getDistanceToEntity(entity) * 0.053f);
                            posy += Math.abs(scaledPositions[3] - scaledPositions[1]) / 4;
                        }
                        GlStateManager.disableDepth();
                        GlStateManager.disableLighting();
                        GlStateManager.enableDepth();
                        GlStateManager.scale(1 / (0.5f * amp), 1 / (0.5f * amp), 1 / (0.5f * amp));
                        mc.getRenderItem().zLevel = 0.0F;
                        GlStateManager.enableBlend();
                        GlStateManager.enableAlpha();

                    }
                }
            }
        }

        GL11.glPopMatrix();
        this.mc.entityRenderer.setupOverlayRendering();
    }

    @EventTarget
    public void onUpdateModel(UpdateModelEvent event) {
        ModelPlayer model = event.getModelPlayer();
        this.playerRotationMap.put((EntityPlayer) event.getEntity(), new float[][]{{model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ}, {model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ}, {model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ}, {model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ}, {model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ}});
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (!filter.get().contains("Skeleton")) {
            return;
        }

        this.setupRender(true);
        GL11.glEnable(2903);
        GL11.glDisable(2848);
        this.playerRotationMap.keySet().removeIf(this::contain);
        Map<EntityPlayer, float[][]> playerRotationMap = this.playerRotationMap;
        List worldPlayers = mc.world.getPlayerEntities();
        Object[] players = playerRotationMap.keySet().toArray();
        int playersLength = players.length;


        for (Object o : players) {
            EntityPlayer player = (EntityPlayer) o;
            float[][] entPos = playerRotationMap.get(player);
            if (entPos == null || player.getEntityID() == -1488 || !player.isEntityAlive() || !RenderUtils.isInViewFrustrum(player) || player.isDead || player == mc.player || player.isPlayerSleeping() || player.isInvisible())
                continue;
            GL11.glPushMatrix();
            float[][] modelRotations = playerRotationMap.get(player);
            GL11.glLineWidth(1.2f);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            double x = interpolate(player.posX, player.lastTickPosX, event.getPartialTicks()) - mc.getRenderManager().renderPosX;
            double y = interpolate(player.posY, player.lastTickPosY, event.getPartialTicks()) - mc.getRenderManager().renderPosY;
            double z = interpolate(player.posZ, player.lastTickPosZ, event.getPartialTicks()) - mc.getRenderManager().renderPosZ;
            GL11.glTranslated(x, y, z);
            float bodyYawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * mc.timer.renderPartialTicks;
            GL11.glRotatef(-bodyYawOffset, 0.0f, 1.0f, 0.0f);
            GL11.glTranslated(0.0, 0.0, player.isSneaking() ? -0.235 : 0.0);
            float legHeight = player.isSneaking() ? 0.6f : 0.75f;
            float rad = 57.29578f;
            GL11.glPushMatrix();
            GL11.glTranslated(-0.125, legHeight, 0.0);

            if (modelRotations[3][0] != 0.0f) {
                GL11.glRotatef(modelRotations[3][0] * 57.29578f, 1.0f, 0.0f, 0.0f);
            }

            if (modelRotations[3][1] != 0.0f) {
                GL11.glRotatef(modelRotations[3][1] * 57.29578f, 0.0f, 1.0f, 0.0f);
            }

            if (modelRotations[3][2] != 0.0f) {
                GL11.glRotatef(modelRotations[3][2] * 57.29578f, 0.0f, 0.0f, 1.0f);
            }

            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, -legHeight, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.125, legHeight, 0.0);

            if (modelRotations[4][0] != 0.0f) {
                GL11.glRotatef(modelRotations[4][0] * 57.29578f, 1.0f, 0.0f, 0.0f);
            }

            if (modelRotations[4][1] != 0.0f) {
                GL11.glRotatef(modelRotations[4][1] * 57.29578f, 0.0f, 1.0f, 0.0f);
            }

            if (modelRotations[4][2] != 0.0f) {
                GL11.glRotatef(modelRotations[4][2] * 57.29578f, 0.0f, 0.0f, 1.0f);
            }

            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, -legHeight, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glTranslated(0.0, 0.0, player.isSneaking() ? 0.25 : 0.0);
            GL11.glPushMatrix();
            GL11.glTranslated(0.0, player.isSneaking() ? -0.05 : 0.0, player.isSneaking() ? -0.01725 : 0.0);
            GL11.glPushMatrix();
            GL11.glTranslated(-0.375, (double) legHeight + 0.55, 0.0);
            if (modelRotations[1][0] != 0.0f) {
                GL11.glRotatef(modelRotations[1][0] * 57.29578f, 1.0f, 0.0f, 0.0f);
            }
            if (modelRotations[1][1] != 0.0f) {
                GL11.glRotatef(modelRotations[1][1] * 57.29578f, 0.0f, 1.0f, 0.0f);
            }
            if (modelRotations[1][2] != 0.0f) {
                GL11.glRotatef(-modelRotations[1][2] * 57.29578f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, -0.5, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.375, (double) legHeight + 0.55, 0.0);

            if (modelRotations[2][0] != 0.0f) {
                GL11.glRotatef(modelRotations[2][0] * 57.29578f, 1.0f, 0.0f, 0.0f);
            }

            if (modelRotations[2][1] != 0.0f) {
                GL11.glRotatef(modelRotations[2][1] * 57.29578f, 0.0f, 1.0f, 0.0f);
            }

            if (modelRotations[2][2] != 0.0f) {
                GL11.glRotatef(-modelRotations[2][2] * 57.29578f, 0.0f, 0.0f, 1.0f);
            }

            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, -0.5, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glRotatef(bodyYawOffset - player.rotationYawHead, 0.0f, 1.0f, 0.0f);
            GL11.glPushMatrix();
            GL11.glTranslated(0.0, (double) legHeight + 0.55, 0.0);

            if (modelRotations[0][0] != 0.0f) {
                GL11.glRotatef(modelRotations[0][0] * 57.29578f, 1.0f, 0.0f, 0.0f);
            }

            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, 0.3, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glRotatef(player.isSneaking() ? 25.0f : 0.0f, 1.0f, 0.0f, 0.0f);
            GL11.glTranslated(0.0, player.isSneaking() ? -0.16175 : 0.0, player.isSneaking() ? -0.48025 : 0.0);
            GL11.glPushMatrix();
            GL11.glTranslated(0.0, legHeight, 0.0);
            GL11.glBegin(3);
            GL11.glVertex3d(-0.125, 0.0, 0.0);
            GL11.glVertex3d(0.125, 0.0, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.0, legHeight, 0.0);
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, 0.55, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.0, (double) legHeight + 0.55, 0.0);
            GL11.glBegin(3);
            GL11.glVertex3d(-0.375, 0.0, 0.0);
            GL11.glVertex3d(0.375, 0.0, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }

        this.setupRender(false);
    }

    private void setupRender(boolean start) {
        if (start) {
            GL11.glDisable(2848);
            GL11.glDisable(2929);
            GL11.glDisable(3553);
        } else {
            GL11.glEnable(3553);
            GL11.glEnable(2929);
        }

        GL11.glDepthMask((!start ? 1 : 0) != 0);
    }

    public ListProperty<String> getFilter() {
        return filter;
    }

    private void collectEntities() {
        for (EntityPlayer entity : this.mc.world.getPlayerEntities()) {
            if (isValid(entity)) this.collectedEntities.add(entity);
        }
    }

    private boolean contain(EntityPlayer var0) {
        return !mc.world.getPlayerEntities().contains(var0);
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    private Vector3d project2D(ScaledResolution scaledResolution, double x, double y, double z) {
        GL11.glGetFloat(2982, this.modelView);
        GL11.glGetFloat(2983, this.projection);
        GL11.glGetInteger(2978, this.viewport);

        if (GLU.gluProject((float) x, (float) y, (float) z, this.modelView, this.projection, this.viewport,
                this.vector)) {
            return new Vector3d(this.vector.get(0) / scaledResolution.getScaleFactor(),
                    (Display.getHeight() - this.vector.get(1)) / scaledResolution.getScaleFactor(), this.vector.get(2));
        }

        return null;
    }

    private boolean isValid(EntityPlayer entityLivingBase) {
        return !entityLivingBase.isDead && !entityLivingBase.isInvisible();
    }

    public ColorProperty getColor() {
        return color;
    }
}