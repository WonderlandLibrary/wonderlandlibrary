package today.flux.module.implement.Combat;

import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;
import today.flux.Flux;
import today.flux.event.RespawnEvent;
import today.flux.gui.clickgui.classic.BlurBuffer;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.hud.notification.Notification;
import today.flux.gui.hud.notification.NotificationManager;
import today.flux.irc.IRCUser;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Combat.killaura.AAC;
import today.flux.module.implement.Combat.killaura.NCP;
import today.flux.module.implement.World.AutoL;
import today.flux.module.value.ColorValue;
import today.flux.utility.*;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.module.value.ModeValue;

import java.awt.*;
import java.util.*;
import java.util.List;

public class KillAura extends Module {
    public static EntityLivingBase target;

    public static ModeValue priority = new ModeValue("KillAura", "Priority", "Angle", "Angle", "Health", "Fov", "Range", "Armor");
    public static ModeValue targetHUDMode = new ModeValue("KillAura", "TargetHUD Mode", "Flux", "Flux", "Flux (Old)", "Astolfo", "Innominate");
    //todo: finish colour mode thingy
    public static ModeValue thColourMode = new ModeValue("KillAura", "TargetHUD Color Mode", "Health", "Static");

    public static ColorValue targetHudColours = new ColorValue("KillAura", "TargetHUD Color", Color.RED);

    public static BooleanValue autoBlock = new BooleanValue("KillAura", "Autoblock", false);
    public static FloatValue range = new FloatValue("KillAura", "Range", 4.2f, 1.0f, 8.0f, 0.1f);
    public static FloatValue blockRange = new FloatValue("KillAura", "Block Range", 2.0f, 0.0f, 4.0f, 0.1f);
    public static FloatValue rotationSpeed = new FloatValue("KillAura", "Rotation Speed", 120.0f, 30.0f, 180.0f, 1f);
    public static FloatValue aps = new FloatValue("KillAura", "APS", 10f, 1f, 20f, 1f);
    public static FloatValue switchDelay = new FloatValue("KillAura", "Switch Delay", 1000f, 1f, 2000f, 100f);
    public static FloatValue switchSize = new FloatValue("KillAura", "Switch Size", 3f, 1f, 8f, 1f);
    public static FloatValue yawdiff = new FloatValue("KillAura", "Yaw Offset", 0f, -1f, 1f, 0.1f);
    public static FloatValue pitchdiff = new FloatValue("KillAura", "Pitch Offset", 0f, -1f, 1f, 0.1f);
    public static FloatValue fov = new FloatValue("KillAura", "FoV", 360f, 10f, 360f, 10f, "°");
    public static BooleanValue attackMob = new BooleanValue("KillAura", "Mob", false);
    public static BooleanValue attackPlayer = new BooleanValue("KillAura", "Player", true);
    public static BooleanValue blockThoughWall = new BooleanValue("KillAura", "Block Through Wall", false);
    public static BooleanValue attackInvisible = new BooleanValue("KillAura", "Invisible", true);
    public static BooleanValue autodisable = new BooleanValue("KillAura", "Auto Disable", true);
    public static BooleanValue targetHUD = new BooleanValue("KillAura", "Target HUD", true);

    public static BooleanValue instantRotate = new BooleanValue("KillAura", "Rotate Instantly", true);

    public static ArrayList<EntityLivingBase> attacked = new ArrayList<>();
    public DelayTimer disableHelper = new DelayTimer();
    public static int killed;

    public KillAura() {
        super("KillAura", Category.Combat, true, new NCP(true), new NCP(false), new AAC());
    }

    public void doRenderTargetHUD() {
        if (this.isEnabled() && targetHUD.getValueState() && ModuleManager.killAuraMod.disableHelper.isDelayComplete(50)) {
            ScaledResolution sr = new ScaledResolution(mc);
            int renderIndex = 0;
            for (Entity base : mc.theWorld.loadedEntityList) {
                if (base instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) base;
                    List<EntityLivingBase> targets;

                    if (getMode().isCurrentMode("Switch")) {
                        targets = NCP.targets;
                    } else {
                        targets = new ArrayList<>();
                        targets.add(target);
                    }

                    if (targets.contains(base)) {
                        if (player.targetHUD == null) {
                            player.targetHUD = new TargetHUD(player);
                        }
                        int size = 33;
                        if (targetHUDMode.isCurrentMode("Flux (Old)")) {
                            size = 43;
                        } else if (targetHUDMode.isCurrentMode("Astolfo")) {
                            size = 60;
                        }
                        player.targetHUD.render(sr.getScaledWidth() / 2f + 14, sr.getScaledHeight() / 2f - 14 + (renderIndex * size));
                        renderIndex++;
                    } else if (player.targetHUD != null) {
                        player.targetHUD.animation = 0;
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
        attacked.clear();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        target = null;
        super.onDisable();
    }

    @EventTarget
    @SoterObfuscator.Obfuscation(flags = "+native")
    public void onRespawn(RespawnEvent respawnEvent) {
        if (autodisable.getValue()) {
            this.setEnabled(false);
            NotificationManager.show("Module", this.getName() + " Disabled (Auto)", Notification.Type.INFO);
        }
    }

    public static boolean isValid(EntityLivingBase entity, double maxRange) {
        if (entity.isDead || entity.getHealth() <= 0 || entity == mc.thePlayer) {

            if (attacked.contains(entity)) {
                killed += 1;

                //DO AutoL
                if (ModuleManager.autoLMod.isEnabled()) {
                    ChatUtils.SendMessage(AutoL.getAutoLMessage(entity.getName()));
                    if (AutoL.autowdr.getValueState()) ChatUtils.ReportPlayer(entity.getName());
                }

                attacked.remove(entity);
            }

            return false;
        }


        // not armor standdddddd
        if (entity instanceof EntityArmorStand) {
            return false;
        }

        if (entity.isInvisible() && !attackInvisible.getValue())
            return false;

        if (entity instanceof EntityPlayer && ModuleManager.antiBotsMod.isEnabled() && ModuleManager.antiBotsMod.getMode().isCurrentMode("Hypixel") && AntiBots.isHypixelNPC(entity))
            return false;

        if (entity instanceof EntityPlayer && !attackPlayer.getValue())
            return false;

        if ((entity instanceof EntityCreature || entity instanceof EntityBat || entity instanceof EntitySlime || entity instanceof EntitySquid) && !attackMob.getValue())
            return false;

        // Range check
        if (entity.getDistanceToEntity(mc.thePlayer) > maxRange)
            return false;

        // Fov check
        if (!RotationUtils.isVisibleFOV(entity, fov.getValue())) {
            return false;
        }

        // Team check
        if (entity instanceof EntityPlayer && FriendManager.isTeam((EntityPlayer) entity) && Flux.teams.getValue())
            return false;

        // Friend check
        if (entity instanceof EntityPlayer && Flux.INSTANCE.getFriendManager().isFriend(entity.getName()) && Flux.friends.getValueState())
            return false;

        // Bot check
        if (ModuleManager.antiBotsMod.isEnabled() && ModuleManager.antiBotsMod.isNPC(entity))
            return false;

        return true;
    }

    public static boolean blockedStatusForRender;
    public static boolean blocked;

    public static boolean isRenderBlocked() {
        return blockedStatusForRender;
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    public void attackEntity(final EntityLivingBase entity) {
        if (entity == null)
            return;

        if (!attacked.contains(entity) && entity instanceof EntityPlayer) {
            attacked.add(entity);
        }

        mc.thePlayer.swingItem();
        mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));

        float sharpLevel = EnchantmentHelper.getModifierForCreature(mc.thePlayer.inventory.getCurrentItem(), entity.getCreatureAttribute());
        if (sharpLevel > 0.0F) {
            mc.thePlayer.onEnchantmentCritical(entity);
        }
    }

    public static void unblock() {
        if (KillAura.blocked) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
            KillAura.blocked = false;
        }
    }


    public static double getRandomDoubleInRange(double minDouble, double maxDouble) {
        return minDouble >= maxDouble ? minDouble : new Random().nextDouble() * (maxDouble - minDouble) + minDouble;
    }

    public static boolean isValidAttack(EntityLivingBase entityLivingBase) {
        return isValid(entityLivingBase, range.getValue());
    }

    public static boolean isValidBlock(EntityLivingBase entityLivingBase) {
        return isValid(entityLivingBase, range.getValue() + blockRange.getValue());
    }

    public class TargetHUD {
        public final EntityPlayer ent;
        public float animation = 0;

        public TargetHUD(EntityPlayer player) {
            this.ent = player;
        }

        private void renderArmor(EntityPlayer player) {
            int xOffset = 60;

            int index;
            ItemStack stack;
            for (index = 3; index >= 0; --index) {
                stack = player.inventory.armorInventory[index];
                if (stack != null) {
                    xOffset -= 8;
                }
            }

            for (index = 3; index >= 0; --index) {
                stack = player.inventory.armorInventory[index];
                if (stack != null) {
                    ItemStack armourStack = stack.copy();
                    if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
                        armourStack.stackSize = 1;
                    }

                    this.renderItemStack(armourStack, xOffset, 12);
                    xOffset += 16;
                }
            }
        }

        private void renderItemStack(ItemStack stack, int x, int y) {
            GlStateManager.pushMatrix();

            GlStateManager.disableAlpha();
            mc.getRenderItem().zLevel = -150.0F;

            GlStateManager.disableCull();

            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, y);

            GlStateManager.enableCull();

            mc.getRenderItem().zLevel = 0;

            GlStateManager.disableBlend();

            GlStateManager.scale(0.5F, 0.5F, 0.5F);

            GlStateManager.disableDepth();
            GlStateManager.disableLighting();

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();

            GlStateManager.scale(2.0F, 2.0F, 2.0F);

            GlStateManager.enableAlpha();

            GlStateManager.popMatrix();
        }

        public void render(float x, float y) {

            Color color;

            GL11.glPushMatrix();
            String playerName = ent.getName();

            String clientTag = "";

            IRCUser user = IRCUser.getIRCUserByIGN(playerName);

            if (user != null) {
                clientTag = "\247" + user.rank.charAt(0) + "[" + user.rank.substring(1) + "|" + user.username + "] \247f";
            }

            String healthStr = Math.round(ent.getHealth() * 10) / 10d + " hp";
            float width = Math.max(75, FontManager.wqy18.getStringWidth(clientTag + playerName) + 25);

            //更改TargetHUD在屏幕坐标的初始位置
            if (targetHUDMode.isCurrentMode("Flux")) {
                if (BlurBuffer.blurEnabled()) {
                    BlurBuffer.blurRoundArea(x + .5f, y + .5f, 28 + width - 1f, 28 - 1f, 2f, true);
                }

                //更改TargetHUD在屏幕坐标的初始位置
                GL11.glTranslatef(x, y, 0);
                GuiRenderUtils.drawRoundedRect(0, 0, 28 + width, 28, 2, RenderUtil.reAlpha(0xff000000, BlurBuffer.blurEnabled() ? 0.4f : 0.6f), 1, RenderUtil.reAlpha(0xff000000, BlurBuffer.blurEnabled() ? 0.3f : 0.5f));

                FontManager.wqy15.drawString(clientTag + playerName, 30f, 3f, ColorUtils.WHITE.c);
                FontManager.roboto12.drawString(healthStr, 26 + width - FontManager.roboto12.getStringWidth(healthStr) - 2, 4f, 0xffcccccc);

                boolean isNaN = Float.isNaN(ent.getHealth());
                float health = isNaN ? 20 : ent.getHealth();
                float maxHealth = isNaN ? 20 : ent.getMaxHealth();
                float healthPercent = MathUtils.clampValue(health / maxHealth, 0, 1);

                int hue = (int) (healthPercent * 120);
                color = Color.getHSBColor(hue / 360f, 0.7f, 1f);

                RenderUtil.drawRect(37, 14.5f, 26 + width - 2, 17.5f, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.35f));

                float barWidth = (26 + width - 2) - 37;
                float drawPercent = 37 + (barWidth / 100) * (healthPercent * 100);

                if (this.animation <= 0) {
                    this.animation = drawPercent;
                }

                if (ent.hurtTime <= 6) {
                    this.animation = AnimationUtils.getAnimationState(this.animation, drawPercent, (float) Math.max(10, (Math.abs(this.animation - drawPercent) * 30) * 0.4));
                }

                RenderUtil.drawRect(37, 14.5f, this.animation, 17.5f, color.darker().getRGB());
                RenderUtil.drawRect(37, 14.5f, drawPercent, 17.5f, color.getRGB());

                FontManager.icon10.drawString("s", 30f, 13, ColorUtils.WHITE.c);
                FontManager.icon10.drawString("r", 30f, 20, ColorUtils.WHITE.c);

                float f3 = 37 + (barWidth / 100f) * (ent.getTotalArmorValue() * 5);
                RenderUtil.drawRect(37, 21.5f, 26 + width - 2, 24.5f, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.35f));
                RenderUtil.drawRect(37, 21.5f, f3, 24.5f, 0xff4286f5);

                ESP2D.INSTANCE.rectangleBordered(1.5f, 1.5f, 26.5f, 26.5f, 0.5f, 0x00000000, Color.GREEN.getRGB());
                GlStateManager.resetColor();
                for (NetworkPlayerInfo info : GuiPlayerTabOverlay.field_175252_a.sortedCopy(mc.getNetHandler().getPlayerInfoMap())) {
                    if (mc.theWorld.getPlayerEntityByUUID(info.getGameProfile().getId()) == ent) {
                        mc.getTextureManager().bindTexture(info.getLocationSkin());
                        drawScaledCustomSizeModalRect(2f, 2f, 8.0f, 8.0f, 8, 8, 24, 24, 64.0f, 64.0f);
                        if (ent.isWearing(EnumPlayerModelParts.HAT)) {
                            drawScaledCustomSizeModalRect(2f, 2f, 40.0f, 8.0f, 8, 8, 24, 24, 64.0f, 64.0f);
                        }
                        GlStateManager.bindTexture(0);
                        break;
                    }
                }
                GL11.glPopMatrix();
            } else if (targetHUDMode.isCurrentMode("Flux (Old)")) {
                GL11.glTranslatef(x, y, 0);
                String playerName2 = ent.getName();
                String healthStr2 = "Health: " + Math.round(ent.getHealth() * 10) / 10d;
                float namewidth = FontManager.wqy18.getStringWidth(playerName2) + 4;
                float healthwidth = FontManager.roboto16.getStringWidth(healthStr2) + 4;
                float width2 = Math.max(namewidth, healthwidth);

                GuiRenderUtils.drawRoundedRect(0, 0, 26 + width2, 40, 2, RenderUtil.reAlpha(0xff22252b, 0.85f), 1, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0f));
                FontManager.wqy18.drawString(playerName2, 26f, 2f, ColorUtils.WHITE.c);
                FontManager.roboto16.drawString(healthStr2, 26f, 14f, ColorUtils.WHITE.c);

                float health = ent.getHealth();
                float maxHealth = ent.getMaxHealth();
                float healthPercent = MathUtils.clampValue(health / maxHealth, 0, 1);
                float drawPercent = ((16.5f + width2 - 2) / 100) * (healthPercent * 100);

                if (this.animation <= 0) {
                    this.animation = drawPercent;
                }

                if (this.animation > 25.5f + width2 - 2) {
                    this.animation = drawPercent;
                }

                if (ent.hurtTime <= 6) {
                    this.animation = AnimationUtils.getAnimationState(this.animation, drawPercent, (float) Math.max(10, (Math.abs(this.animation - drawPercent) * 30) * 0.4));
                }

                RenderUtil.drawRect(10f, 27.5f, 25.5f + width2 - 2, 29.5f, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.35f));

                if (drawPercent > 0) {
                    float f1 = Math.max((10f + this.animation - 1), 10f);
                    float f2 = Math.max((10f + drawPercent - 1), 10f);
                    RenderUtil.drawRect(10f, 27.5f, f1, 29.5f, RenderUtil.reAlpha(0xfffeb647, 0.95f));
                    RenderUtil.drawRect(10f, 27.5f, f2, 29.5f, RenderUtil.reAlpha(0xff00d17f, 0.95f));
                }

                FontManager.icon10.drawString("s", 2.5f, 26, ColorUtils.WHITE.c);
                FontManager.icon10.drawString("r", 2.5f, 33, ColorUtils.WHITE.c);

                float f3 = Math.max((10f + ((16.5f + width2 - 2) / 100) * (ent.getTotalArmorValue() * 5) - 1), 10f);
                RenderUtil.drawRect(10f, 34.5f, 25.5f + width2 - 2, 36.5f, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.35f));
                RenderUtil.drawRect(10f, 34.5f, f3, 36.5f, 0xff4286f5);

                ESP2D.INSTANCE.rectangleBordered(1.5f, 1.5f, 24.5f, 24.5f, 0.5f, 0x00000000, KillAura.target == ent ? ColorUtils.GREEN.c : 0xffffffff);
                GlStateManager.resetColor();
                for (NetworkPlayerInfo info : GuiPlayerTabOverlay.field_175252_a.sortedCopy(mc.getNetHandler().getPlayerInfoMap())) {
                    if (mc.theWorld.getPlayerEntityByUUID(info.getGameProfile().getId()) == ent) {
                        mc.getTextureManager().bindTexture(info.getLocationSkin());
                        drawScaledCustomSizeModalRect(2f, 2f, 8.0f, 8.0f, 8, 8, 22, 22, 64.0f, 64.0f);
                        if (ent.isWearing(EnumPlayerModelParts.HAT)) {
                            drawScaledCustomSizeModalRect(2f, 2f, 40.0f, 8.0f, 8, 8, 22, 22, 64.0f, 64.0f);
                        }
                        GlStateManager.bindTexture(0);
                        break;
                    }
                }
                GL11.glPopMatrix();
            } else if(targetHUDMode.isCurrentMode("Astolfo")) {
                float width2 = Math.max(75, mc.fontRendererObj.getStringWidth(clientTag + playerName) + 20);
                String healthStr2 = Math.round(ent.getHealth() * 10) / 10d + " ❤";
                GL11.glTranslatef(x, y, 0);
                GuiRenderUtils.drawBorderedRect(0, 0, 55 + width2, 47, 0.5f, new Color(0, 0, 0, 140), new Color(0, 0, 0));

                mc.fontRendererObj.drawStringWithShadow(clientTag + playerName, 35, 3f, ColorUtils.WHITE.c);

                boolean isNaN = Float.isNaN(ent.getHealth());
                float health = isNaN ? 20 : ent.getHealth();
                float maxHealth = isNaN ? 20 : ent.getMaxHealth();
                float healthPercent = MathUtils.clampValue(health / maxHealth, 0, 1);

                int hue = (int) (healthPercent * 120);
                color = Color.getHSBColor(hue / 360f, 0.7f, 1f);

                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0, 2.0, 2.0);
                mc.fontRendererObj.drawStringWithShadow(healthStr2, 18, 7.5f, color.getRGB());
                GlStateManager.popMatrix();

                RenderUtil.drawRect(36, 36.5f, 45 + width2, 44.5f, RenderUtil.reAlpha(color.darker().darker().getRGB(), 0.35f));

                float barWidth = (43 + width2 - 2) - 37;
                float drawPercent = 43 + (barWidth / 100) * (healthPercent * 100);

                if (this.animation <= 0) {
                    this.animation = drawPercent;
                }

                if (ent.hurtTime <= 6) {
                    this.animation = AnimationUtils.getAnimationState(this.animation, drawPercent, (float) Math.max(10, (Math.abs(this.animation - drawPercent) * 30) * 0.4));
                }
                RenderUtil.drawRect(36, 36.5f, this.animation + 6, 44.5f, color.darker().darker().getRGB());
                RenderUtil.drawRect(36, 36.5f, this.animation, 44.5f, color.getRGB());
                RenderUtil.drawRect(36, 36.5f, drawPercent, 44.5f, color.getRGB());

                GlStateManager.disableBlend();
                GlStateManager.enableAlpha();

                GlStateManager.resetColor();
                // 3D model of the target
                GlStateManager.disableBlend();
                GlStateManager.color(1, 1, 1, 1);
                GuiInventory.drawEntityOnScreen(17, 46, (int) (42 / target.height), 0, 0, ent, 165);
                GL11.glPopMatrix();
            } else if (targetHUDMode.isCurrentMode("Innominate")) {
                float width2 = Math.max(75, FontManager.tahoma13.getStringWidth(clientTag + playerName) + 25);
                String healthStr2 = Math.round(ent.getHealth() * 10) / 10d + "";
                GL11.glTranslatef(x, y, 0);
                GuiRenderUtils.drawRect(0, 0, 45 + width2, 37, new Color(0, 0, 0, 90));
                GuiRenderUtils.drawRect(4, 4, 37 + width2, 29, new Color(0xff1E1E1C));

                FontManager.tahoma13.drawOutlinedString(clientTag + playerName, 8, 5, ColorUtils.WHITE.c, ColorUtils.BLACK.c);

                boolean isNaN = Float.isNaN(ent.getHealth());
                float health = isNaN ? 20 : ent.getHealth();
                float maxHealth = isNaN ? 20 : ent.getMaxHealth();
                float healthPercent = MathUtils.clampValue(health / maxHealth, 0, 1);

                int hue = (int) (healthPercent * 120);
                color = Color.getHSBColor(hue / 360f, 0.7f, 1f);

                RenderUtil.drawRect(7, 14, 27.5f + width2, 21, RenderUtil.reAlpha(0xff1D1D1D, 1f));

                float barWidth = (34.5f + width2 - 2) - 37;
                float drawPercent = 34.5f + (barWidth / 100) * (healthPercent * 100);

                if (this.animation <= 0) {
                    this.animation = drawPercent;
                }

                if (ent.hurtTime <= 6) {
                    this.animation = AnimationUtils.getAnimationState(this.animation, drawPercent, (float) Math.max(10, (Math.abs(this.animation - drawPercent) * 30) * 0.4));
                }

                RenderUtil.drawRect(7, 14, this.animation, 21, color.getRGB());
                RenderUtil.drawRect(7, 14, drawPercent, 21, color.getRGB());

                FontManager.tahoma13.drawOutlinedString(healthStr2, 55.5f, 13, ColorUtils.WHITE.c, ColorUtils.BLACK.c);
                FontManager.tahoma13.drawOutlinedString("Distance: " + Math.round(target.getDistanceToEntity(mc.thePlayer) * 10) / 10d + " - Target HurtTime: " + Math.round(target.hurtTime), 9, 23, ColorUtils.WHITE.c, ColorUtils.BLACK.c);

                GlStateManager.disableBlend();
                GlStateManager.enableAlpha();

                GL11.glPopMatrix();
            }
        }

        public void drawScaledCustomSizeModalRect(float x, float y, float u, float v, float uWidth, float vHeight, float width, float height, float tileWidth, float tileHeight) {
            float f = 1.0F / tileWidth;
            float f1 = 1.0F / tileHeight;
            GL11.glColor4f(1, 1, 1, 1);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer bufferbuilder = tessellator.getWorldRenderer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos((double) x, (double) (y + height), 0.0D).tex((double) (u * f), (double) ((v + (float) vHeight) * f1)).endVertex();
            bufferbuilder.pos((double) (x + width), (double) (y + height), 0.0D).tex((double) ((u + (float) uWidth) * f), (double) ((v + (float) vHeight) * f1)).endVertex();
            bufferbuilder.pos((double) (x + width), (double) y, 0.0D).tex((double) ((u + (float) uWidth) * f), (double) (v * f1)).endVertex();
            bufferbuilder.pos((double) x, (double) y, 0.0D).tex((double) (u * f), (double) (v * f1)).endVertex();
            tessellator.draw();
        }
    }
}
