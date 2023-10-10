package today.flux.module.implement.Render;

import java.awt.Color;

import today.flux.Flux;
import today.flux.event.DrawTextEvent;
import today.flux.event.NameTagRenderEvent;
import today.flux.event.WorldRenderEvent;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.irc.IRCUser;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Combat.AntiBots;
import today.flux.module.implement.World.HackerDetector;
import today.flux.utility.MathUtils;
import today.flux.utility.WorldRenderUtils;
import today.flux.utility.WorldUtil;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumChatFormatting;

public class NameTags extends Module {
    public static FloatValue scale = new FloatValue("NameTags", "Scale", 1.0f, 0.1f, 1.0f, 0.1f);
    public static BooleanValue invisible = new BooleanValue("NameTags", "Bot & Invis", false);
    public static BooleanValue health = new BooleanValue("NameTags", "Health", true);
    public static BooleanValue distance = new BooleanValue("NameTags", "Distance", true);
    public static BooleanValue renderArmor = new BooleanValue("NameTags", "Armor", true);
    public static BooleanValue background = new BooleanValue("NameTags", "Background", true);

    public NameTags() {
        super("NameTags", Category.Render, false);
    }

    @EventTarget
    private void onRenderNameTag(NameTagRenderEvent event) {
        if (event.getEntity() instanceof EntityPlayer && this.isValid((EntityPlayer) event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventTarget(Priority.LOWEST)
    private void onRender3DEvent(WorldRenderEvent event) {
        if (this.mc.theWorld == null)
            return;

        for (EntityPlayer entity : WorldUtil.getLivingPlayers()) {
            if (this.isValid(entity)) {
                final double yOffset = entity.isSneaking() ? -0.25 : 0.0;

                final double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - this.mc.getRenderManager().getRenderPosX();
                final double posY = (entity.lastTickPosY + yOffset) + ((entity.posY + yOffset) - (entity.lastTickPosY + yOffset)) * mc.timer.renderPartialTicks - this.mc.getRenderManager().getRenderPosY();
                final double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - this.mc.getRenderManager().getRenderPosZ();

                mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);

                this.renderNameTag(entity, posX, posY, posZ, event.getPartialTicks());
            }
        }
    }

    public boolean isValid(EntityLivingBase entity) {
    	if (ModuleManager.noRenderMod.isEnabled() && NoRender.players.getValueState() && entity instanceof EntityPlayer) return false;
    	
        if (entity.isInvisible() && !invisible.getValue())
            return false;

        if (!AntiBots.isInTablist(entity) && !invisible.getValue())
            return false;

        return true;
    }

    private int getDisplayColour(EntityPlayer player) {
        int colour = new Color(0xFFFFFF).getRGB();

        if (Flux.INSTANCE.getFriendManager().isFriend(player.getName())) {
            return -11157267;
        } else {
            if (player.isInvisible()) {
                colour = -1113785;
            }

            return colour;
        }
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double) delta;
    }

    private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        double tempY = y + 0.7D;

        Entity camera = this.mc.getRenderViewEntity();
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);


        double distance = camera.getDistance(x + this.mc.getRenderManager().viewerPosX, y + this.mc.getRenderManager().viewerPosY, z + this.mc.getRenderManager().viewerPosZ);

        float width = FontManager.tahomaArrayList.getStringWidth(this.getDisplayName(player)) / 2;

        double scale = (double) (0.004F * NameTags.scale.getValue()) * distance;

        if (scale < 0.01)
            scale = 0.01;

        GlStateManager.pushMatrix();

        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0F, -1500000.0F);

        GlStateManager.disableLighting();

        GlStateManager.translate((float) x, (float) tempY + 1.4F, (float) z);
        GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        float var10001 = this.mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;
        GlStateManager.rotate(this.mc.getRenderManager().playerViewX, var10001, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
        if(NameTags.background.getValue()) {
            WorldRenderUtils.drawBorderedRectReliant(-width - 2, -(FontManager.tahomaArrayList.FONT_HEIGHT + 3), (float) width + 2.0F, 2.0F, (float) scale, new Color(HackerDetector.isHacker(player) ? 255 : 25, 25, 25, 160).getRGB(), new Color(0, 0, 0, 180).getRGB());
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        //Fix Color Glitch
        GL11.glDepthMask(false);
        FontManager.tahomaArrayList.drawStringWithSuperShadow(this.getDisplayName(player), -width, -(FontManager.tahomaArrayList.FONT_HEIGHT), this.getDisplayColour(player));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDepthMask(true);

        if (renderArmor.getValue()) {
            this.renderArmor(player);

            //No Paper item
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
        }

        GlStateManager.disableLighting();

        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;

        GlStateManager.doPolygonOffset(1.0F, 1500000.0F);
        GlStateManager.disablePolygonOffset();

        GlStateManager.popMatrix();
    }

    private void renderArmor(EntityPlayer player) {
        int xOffset = 0;

        int index;
        ItemStack stack;
        for (index = 3; index >= 0; --index) {
            stack = player.inventory.armorInventory[index];
            if (stack != null) {
                xOffset -= 8;
            }
        }

        if (player.getCurrentEquippedItem() != null) {
            xOffset -= 8;
            ItemStack var27 = player.getCurrentEquippedItem().copy();
            if (var27.hasEffect() && (var27.getItem() instanceof ItemTool || var27.getItem() instanceof ItemArmor)) {
                var27.stackSize = 1;
            }

            this.renderItemStack(var27, xOffset, -26);
            xOffset += 16;
        }

        for (index = 3; index >= 0; --index) {
            stack = player.inventory.armorInventory[index];
            if (stack != null) {
                ItemStack armourStack = stack.copy();
                if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
                    armourStack.stackSize = 1;
                }

                this.renderItemStack(armourStack, xOffset, -26);
                xOffset += 16;
            }
        }
    }

    private void renderItemStack(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();

        GlStateManager.disableAlpha();
        this.mc.getRenderItem().zLevel = -150.0F;

        GlStateManager.disableCull();

        this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, stack, x, y);

        GlStateManager.enableCull();

        this.mc.getRenderItem().zLevel = 0;

        GlStateManager.disableBlend();

        GlStateManager.scale(0.5F, 0.5F, 0.5F);

        GlStateManager.disableDepth();
        GlStateManager.disableLighting();

        this.renderEnchantmentText(stack, x, y);

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();

        GlStateManager.scale(2.0F, 2.0F, 2.0F);

        GlStateManager.enableAlpha();

        GlStateManager.popMatrix();
    }

    private static final String pro = "pro";
    private static final String sha = "sha";

    private void renderEnchantmentText(ItemStack stack, int x, int y) {
        try {
            int enchantmentY = y - 24;
            int color = new Color(0xFFFFFF).getRGB();
            if (stack.getItem() instanceof ItemArmor) {
                int protection = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
                int projectileProtection = EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack);
                int blastProtection = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack);
                int fireProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack);
                int thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
                int featherFallingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack);
                int unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);

                if (protection > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(pro + protection, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }

                if (unbreakingLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.unbreaking.getName().substring(0, 3) + unbreakingLevel, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }

                if (projectileProtection > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.projectileProtection.getName().substring(0, 3) + projectileProtection, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }

                if (blastProtection > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.blastProtection.getName().substring(0, 3) + blastProtection, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }

                if (fireProtectionLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.fireAspect.getName().substring(0, 3) + fireProtectionLevel, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }

                if (thornsLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.thorns.getName().substring(0, 3) + thornsLevel, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }

                if (featherFallingLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.featherFalling.getName().substring(0, 3) + featherFallingLevel, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }

                /*
                boolean dura = false;
                if(dura && stack.getMaxDamage() - stack.getItemDamage() < stack.getMaxDamage()) {
                    this.mc.fontRendererObj.drawStringWithShadow(stack.getMaxDamage() - stack.getItemDamage() + "", x * 2, enchantmentY + 2, -26215);
                    enchantmentY += 8;
                }*/
            }

            if (stack.getItem() instanceof ItemBow) {
                int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
                int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
                int flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);

                if (powerLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.power.getName().substring(0, 3) + powerLevel, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }

                if (punchLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.punch.getName().substring(0, 3) + punchLevel, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }

                if (flameLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.flame.getName().substring(0, 3) + flameLevel, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }
            }

            if (stack.getItem() instanceof ItemPickaxe) {
                int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
                int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);

                if (efficiencyLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.efficiency.getName().substring(0, 3) + efficiencyLevel, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }

                if (fortuneLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.fortune.getName().substring(0, 3) + fortuneLevel, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }
            }

            if (stack.getItem() instanceof ItemAxe) {
                int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
                int fireAspect = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
                int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
                if (sharpnessLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.sharpness.getName().substring(0, 3) + sharpnessLevel, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }

                if (fireAspect > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.fireAspect.getName().substring(0, 3) + fireAspect, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }

                if (efficiencyLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.efficiency.getName().substring(0, 3) + efficiencyLevel, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }
            }

            if (stack.getItem() instanceof ItemSword) {
                int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
                int knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
                int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
                if (sharpnessLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(sha + sharpnessLevel, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }

                if (knockbackLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.knockback.getName().substring(0, 3) + knockbackLevel, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }

                if (fireAspectLevel > 0) {
                    this.mc.fontRendererObj.drawStringWithShadow(Enchantment.fireAspect.getName().substring(0, 3) + fireAspectLevel, x * 2, enchantmentY, color);
                    enchantmentY += 8;
                }
            }

           /* if(stack.getItem() == Items.golden_apple && stack.hasEffect()) {
                this.mc.fontRendererObj.drawStringWithShadowWithShadow("god", (float)(x * 2), (float)enchantmentY, -3977919);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDisplayName(EntityLivingBase entity) {
        String drawTag = entity.getDisplayName().getFormattedText();
        if (Flux.INSTANCE.getFriendManager().isFriend(entity.getName())) {
            drawTag = drawTag.replace(entity.getDisplayName().getFormattedText(), EnumChatFormatting.AQUA + entity.getName());
        }

        EnumChatFormatting color;

        final double health = MathUtils.roundToPlace(entity.getHealth() / 2.00, 2);

            if (health >= 6.0) {
                color = EnumChatFormatting.GREEN;
            } else if (health >= 2.0) {
                color = EnumChatFormatting.YELLOW;
            } else {
                color = EnumChatFormatting.RED;
            }

        String clientTag = "";

        IRCUser user = IRCUser.getIRCUserByIGN(entity.getName());

        if (user != null) {
            clientTag = "\247" + user.rank.charAt(0) + "[" + user.rank.substring(1) + "|" + user.username + "] ";
        }

        drawTag = (NameTags.distance.getValue() ?  EnumChatFormatting.GRAY + "[" + (int)entity.getDistanceToEntity(this.mc.thePlayer) + "m] " : "") + EnumChatFormatting.RESET + clientTag + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + drawTag + " " + (NameTags.health.getValue() ? "" + color + health : "");

        DrawTextEvent drawTextEvent = new DrawTextEvent(drawTag);
        EventManager.call(drawTextEvent);
        drawTag = drawTextEvent.text;

        return drawTag;
    }
}
