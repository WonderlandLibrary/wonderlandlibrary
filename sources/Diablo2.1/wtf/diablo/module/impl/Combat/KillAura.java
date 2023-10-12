package wtf.diablo.module.impl.Combat;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import wtf.diablo.Diablo;
import wtf.diablo.commands.impl.FriendCommand;
import wtf.diablo.events.EventType;
import wtf.diablo.events.impl.OverlayEvent;
import wtf.diablo.events.impl.Render3DEvent;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.gui.guiElement.GuiElement;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.BooleanSetting;
import wtf.diablo.settings.impl.ModeSetting;
import wtf.diablo.settings.impl.NumberSetting;
import wtf.diablo.utils.animations.Animation;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.math.MathUtil;
import wtf.diablo.utils.math.Stopwatch;
import wtf.diablo.utils.packet.PacketUtil;
import wtf.diablo.utils.render.ColorUtil;
import wtf.diablo.utils.render.RenderUtil;
import wtf.diablo.utils.world.EntityUtil;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

@Getter
@Setter
public class KillAura extends Module {
    private final Stopwatch timer = new Stopwatch();
    private final Stopwatch hurtTimer = new Stopwatch();
    public ModeSetting rotate = new ModeSetting("Rotate", "Pre", "Post");
    public ModeSetting autoblock = new ModeSetting("AutoBlock", "None", "Watchdog");
    public NumberSetting range = new NumberSetting("Range", 4.2, 0.1, 3, 6);
    public NumberSetting minCps = new NumberSetting("Min CPS", 9, 1, 1, 20);
    public NumberSetting maxCps = new NumberSetting("Max CPS", 14, 1, 1, 20);
    public BooleanSetting visualiseTarget = new BooleanSetting("Visualise", true);

    public ModeSetting targetHud = new ModeSetting("TargetHud", "None", "Diablo", "Material");

    private EntityLivingBase lastTarget;
    private double lastHealth;

    public static boolean blocking;

    public static ArrayList<EntityLivingBase> totalTargets = new ArrayList<EntityLivingBase>();

    public KillAura() {
        super("KillAura", "Automatically hit entities", Category.COMBAT, ServerType.All);
        this.setKey(Keyboard.KEY_R);
        addSettings(autoblock, range, minCps, maxCps,rotate, visualiseTarget, targetHud);
    }

    @Override
    public void onDisable() {
        blocking = false;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    public static EntityLivingBase target = null;

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        if (Diablo.getInstance().getModuleManager().getModuleByName("Scaffold").isToggled())
            return;
        this.setSuffix(minCps.getValue() + " | " + maxCps.getValue());
        double range = this.range.getValue();
        target = EntityUtil.getClosestEntity(range);
        if (FriendCommand.isFriend(target))
            target = null;

        if (target != null) {

            if(mc.getNetHandler().doneLoadingTerrain) {
                if (!totalTargets.contains(target)) {
                    totalTargets.add(target);
                }
            } else {
                totalTargets.clear();
            }

            float[] rotations = EntityUtil.getAngles(target);
            float rot0 = (float) (rotations[0] + MathUtil.randomNumber(-5, 5));
            float rot1 = (float) (rotations[1] + + MathUtil.randomNumber(-8, 4));
            
            switch (rotate.getMode()){
                case "Pre":
                    if(e.getType() == EventType.Pre) {
                        EntityUtil.setRotations(e, rot0, rot1);
                    }
                    break;
                case "Post":
                    if(e.getType() == EventType.Post) {
                        EntityUtil.setRotations(e, rot0, rot1);
                    }
                    break;
            }

            if (e.getType() == EventType.Pre) {
                if (timer.hasReached(1000 / MathUtil.getRandInt((int) minCps.getValue(), (int) maxCps.getValue()))) {
                    attack(target);
                    timer.reset();
                }
            }


            if (e.getType() == EventType.Post && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) { //no cap this is straight for diablo and a hypixel autoblock
                blocking = true;
                switch (autoblock.getMode()) {
                    case "Watchdog":
                        /*
                        if (mc.thePlayer.swingProgressInt == -1) {
                            PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                        } else if (mc.thePlayer.swingProgressInt == 0) {
                            PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                        }

                         */

                        if (mc.thePlayer.swingProgressInt == -1) {
                            PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));

                        } else if (mc.thePlayer.swingProgressInt < 0.5 && mc.thePlayer.swingProgressInt != -1) {
                            PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                            //mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, new Vec3((double)MathUtil.getRandomFloat(-50, 50)/100, (double)MathUtil.getRandomFloat(0, 200)/100, (double)MathUtil.getRandomFloat(-50, 50)/100)));
                            //mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.INTERACT)); mc.playerController.syncCurrentPlayItem();
                            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));

                        }


                        break;
                }
            }
            blocking = true;
        } else {
            blocking = false;
        }
    }

    ScaledResolution sr = new ScaledResolution(mc);
    public GuiElement guiElement = new GuiElement("TargetHUD", (sr.getScaledWidth() / 2) + 5, (sr.getScaledHeight() / 2), 150, 45,0,0);
    Animation visualise = new Animation();
    Boolean visualiseReverse = false;

    @Subscribe
    public void onOverlay(OverlayEvent e) {
        if (target == null && !(mc.currentScreen instanceof GuiChat)) return;
        EntityLivingBase target = KillAura.target;
        if (target == null)
            target = mc.thePlayer;
        guiElement.renderStart();


        double percentageHealth = (target.getHealth() * 100) / target.getMaxHealth();

        int width, height;
        switch (targetHud.getMode()) {
            case "Material":
                Color main = new Color(38, 38, 38);
                Color boarder = new Color(28, 28, 28);
                Color backdrop = new Color(54, 54, 54);

                int themeColor = ColorUtil.getColor(0);

                float transparency = 1;

                width = 156;
                height = 45;
                guiElement.setWidth(width);
                guiElement.setHeight(height);
                RenderUtil.drawRectAlpha((float) 0, (float) -3, (float) width + 3, (float) height + 3, backdrop.getRGB(), transparency);
                RenderUtil.drawRectAlpha((float) -1, (float) -1, (float) width + 1, (float) height + 1, boarder.getRGB(), transparency);
                RenderUtil.drawRectAlpha((float) 0, (float) 0, (float) width, (float) height, main.getRGB(), transparency);
                RenderUtil.drawRectAlpha((float) 0, (float) 0, (float) width, (float) 2, themeColor, transparency);
                RenderUtil.drawRoundedRect(0, 0, width, height, 2, 0x901c1c1c);
                if (mc.getNetHandler() != null && target.getUniqueID() != null) {
                    NetworkPlayerInfo i = mc.getNetHandler().getPlayerInfo(target.getUniqueID());
                    if (i != null) {
                        mc.getTextureManager().bindTexture(i.getLocationSkin());
                        GlStateManager.color(1, 1, 1);
                        GL11.glEnable(GL11.GL_BLEND);
                        Gui.drawModalRectWithCustomSizedTexture(5, 5, (float) (35), (float) (35), 35, 35, (float) 280, (float) 280);
                        glDisable(GL11.GL_BLEND);
                    }
                }

                break;
            case "Diablo":
                width = 150;
                height = 45;
                guiElement.setWidth(width);
                guiElement.setHeight(height);
                RenderUtil.drawRoundedRect(0, 0, width, height, 2, 0x901c1c1c);
                if (mc.getNetHandler() != null && target.getUniqueID() != null) {
                    NetworkPlayerInfo i = mc.getNetHandler().getPlayerInfo(target.getUniqueID());
                    if (i != null) {
                        mc.getTextureManager().bindTexture(i.getLocationSkin());
                        GlStateManager.color(1, 1, 1);
                        GL11.glEnable(GL11.GL_BLEND);
                        Gui.drawModalRectWithCustomSizedTexture(5, 5, (float) (35), (float) (35), 35, 35, (float) 280, (float) 280);
                        glDisable(GL11.GL_BLEND);
                    }
                }
                Fonts.SFReg24.drawStringWithShadow(target.getName(), 50, 10, -1);
                Fonts.SFReg18.drawStringWithShadow("" + Math.floor(mc.thePlayer.getDistanceToEntity(target) * 10) / 10 + " Blocks", 50, 21, 0xffbababa);

                Gui.drawRect(50, 30, 145, height - 5, 0xFF292929);
                Gui.drawRect(50, 30, ((percentageHealth * 95) / 100) + 50, height - 5, ColorUtil.getColor(0));

                Fonts.SFReg18.drawStringWithShadow((int) percentageHealth + "%", 50 + (95 / 2f) - (Fonts.SFReg18.getStringWidth((int) percentageHealth + "%") / 2f), 31 + (10 / 2f) - (Fonts.SFReg18.getHeight() / 2f), -1);
                break;
        }

        guiElement.renderEnd();
    }

    @Subscribe
    public void onRender(Render3DEvent event) {
        if (target != null && visualiseTarget.getValue()) {

            visualise.setAmount((int) target.height * 1000);
            visualise.setSpeed(2);
            if (visualise.hasFinished()) {
                visualise.setReverse(!visualise.isReversed());
                visualise.start();
            }
            visualise.updateAnimation();
            glPushMatrix();
            float partialTicks = event.getPartialTicks();
            glEnable(GL_BLEND);
            glEnable(GL_LINE_SMOOTH);
            glDisable(GL_TEXTURE_2D);
            mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
            double x = RenderUtil.interpolate(target.posX, target.lastTickPosX, partialTicks) - mc.getRenderManager().viewerPosX;
            double y = RenderUtil.interpolate(target.posY, target.lastTickPosY, partialTicks) - mc.getRenderManager().viewerPosY;
            double z = RenderUtil.interpolate(target.posZ, target.lastTickPosZ, partialTicks) - mc.getRenderManager().viewerPosZ;
            glTranslated(x, y, z);
            glLineWidth(12.75f);
            glPointSize(1.0f);
            glDepthMask(false);
            glDisable(GL_DEPTH_TEST);
            glBegin(GL_LINES);

            for (int i = 0; i < 45; i++) {
                double sin = 2 * Math.sin((2 * Math.PI) * i / 45);
                double cos = 2 * Math.cos((2 * Math.PI) * i / 45);
                Color color = new Color(ColorUtil.getColor((int) ((i / 45f) * 1000)));
                glColor4d(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
                glVertex3d(x + sin, y + (visualise.getValue() / 1000) * 3 - 1, z + cos);
            }

            glEnd();
            glDisable(GL_BLEND);
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_DEPTH_TEST);
            glDisable(GL_LINE_SMOOTH);
            glTranslated(-x, -y, -z);
            glPopMatrix();
        }
    }


    public void attack(Entity entity) {
        mc.thePlayer.swingItem();
        mc.playerController.attackEntity(mc.thePlayer, entity);
        //mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
    }
}
