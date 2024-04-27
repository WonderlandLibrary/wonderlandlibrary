package me.aquavit.liquidsense.module.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.client.RenderChanger;
import me.aquavit.liquidsense.module.modules.misc.Teams;
import me.aquavit.liquidsense.module.modules.world.EventLogger;
import me.aquavit.liquidsense.utils.entity.EntityUtils;
import me.aquavit.liquidsense.utils.render.GLUtils;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render2DEvent;
import me.aquavit.liquidsense.event.events.Render3DEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.module.modules.misc.AntiBot;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ModuleInfo(name = "NameTags", description = "Changes the scale of the nametags so you can always read them.", category = ModuleCategory.RENDER)
public class NameTags extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"2D", "3D"}, "2D");
    private final MultiBoolValue elements = new MultiBoolValue("Elements",
            new String[]{"Health","Distance","Armor","ClearNames"},
            new boolean[]{true,true,false,false,false});
    private final IntegerValue red = (IntegerValue) new IntegerValue("Red", 255,0,255).displayable(
            () -> elements.getMultiBool("ClearNames"));
    private final IntegerValue green = (IntegerValue) new IntegerValue("Green", 255,0,255).displayable(
            () -> elements.getMultiBool("ClearNames"));
    private final IntegerValue blue = (IntegerValue) new IntegerValue("Blue", 255,0,255).displayable(
            () -> elements.getMultiBool("ClearNames"));
    public final BoolValue showself = new BoolValue("ShowSelf", false);

    private final ListValue bgMode = (ListValue) new ListValue("GroundMode", new String[] {"Both", "Name", "Armor", "None"}, "None").displayable(
            () -> modeValue.get().equalsIgnoreCase("2D"));
    private final IntegerValue bgAlpha = (IntegerValue) new IntegerValue("GroundAlpha", 120,0,255).displayable(
            () -> !bgMode.get().equals("None") && modeValue.get().equalsIgnoreCase("2D"));

    private final FloatValue fontScale = (FloatValue) new FloatValue("FontScale", 1.0f, 1.0f, 4.0f).displayable(
            () -> !(modeValue.get().equalsIgnoreCase("2D") && !font.get().equals(Fonts.minecraftFont)));
    private final BoolValue shadow = new BoolValue("Shadow", false);
    public static final FontValue font = new FontValue("Font", Fonts.minecraftFont);

    final RenderChanger rc = (RenderChanger) LiquidSense.moduleManager.getModule(RenderChanger.class);

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (modeValue.get().equalsIgnoreCase("3D")) return;
        mc.theWorld.loadedEntityList.stream().filter(entity ->
                EntityUtils.isSelected(entity, false, showself.get()) && RenderUtils.isInViewFrustrum(entity)).forEach(entity -> {
                    Tag2D(event.getPartialTicks(), (EntityLivingBase) entity, elements.getMultiBool("ClearNames") ?
                            StringUtils.stripControlCodes(entity.getName()) : entity.getDisplayName().getUnformattedText());

        });
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (modeValue.get().equalsIgnoreCase("2D")) return;
        mc.theWorld.loadedEntityList.stream().filter(entity ->
                EntityUtils.isSelected(entity, false, showself.get())).forEach(entity -> {
                    Tag3D(event, (EntityLivingBase) entity, elements.getMultiBool("ClearNames") ?
                            StringUtils.stripControlCodes(entity.getName()) : entity.getDisplayName().getUnformattedText());

        });
    }

    public void Tag2D(float partialTicks, EntityLivingBase entity, String name) {
        double posX = RenderUtils.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
        double posY = RenderUtils.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
        double posZ = RenderUtils.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);

        double width = entity.width / 1.5;
        double height = (entity.height + (entity.isSneaking() ? -0.4 : 0.1) +
                ((rc.getState() && RenderChanger.bigHeadsValue.get()) ? 0.25 : 0.0)) *
                ((rc.getState() && RenderChanger.littleEntitiesValue.get()) ? 0.5 : 1.0);


        AxisAlignedBB aabb = new AxisAlignedBB(posX - width, posY, posZ - width, posX + width, posY + height + 0.05, posZ + width);
        List<Vector3d> vectors = Arrays.asList(
                new Vector3d(aabb.minX, aabb.minY, aabb.minZ),
                new Vector3d(aabb.minX, aabb.maxY, aabb.minZ),
                new Vector3d(aabb.maxX, aabb.minY, aabb.minZ),
                new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ),
                new Vector3d(aabb.minX, aabb.minY, aabb.maxZ),
                new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ),
                new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ),
                new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ)
        );
        mc.entityRenderer.setupCameraTransform(partialTicks, 0);
        Vector4d position = null;

        for (Vector3d vector : vectors) {
            vector = RenderUtils.project(vector.x - mc.getRenderManager().viewerPosX, vector.y - mc.getRenderManager().viewerPosY, vector.z - mc.getRenderManager().viewerPosZ);
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

        mc.entityRenderer.setupOverlayRendering();
        if (position != null) {
            GL11.glPushMatrix();
            if (elements.getMultiBool("Armor") && entity instanceof EntityPlayer) {
                GL11.glScalef(0.5f, 0.5f, 0.5f);
                if (!font.get().equals(Fonts.minecraftFont)) {
                    drawArmor((EntityPlayer) entity, (position.x + ((position.z - position.x) / 2)) * 2, (position.y - 8 - font.get().FONT_HEIGHT * 2) * 2);
                } else
                    drawArmor((EntityPlayer) entity, (position.x + 1.5 + ((position.z - position.x) / 2)) * 2, (position.y - 1 - Fonts.minecraftFont.FONT_HEIGHT * 2) * 2);
                GL11.glScalef(2.0f, 2.0f, 2.0f);
            }

            final float x = (float) position.x;
            final float x2 = (float) position.z;
            final float y = (float) position.y - 1;

            final String healthText = (entity.getHealth() >= 16 ? ChatFormatting.GREEN : entity.getHealth() >= 12 ? ChatFormatting.YELLOW : entity.getHealth() >= 8 ? ChatFormatting.RED : ChatFormatting.DARK_RED) + " " + (int) entity.getHealth();

            final String nameText = (
                    elements.getMultiBool("Distance") ? "(" + Math.round(mc.thePlayer.getDistance(entity.posX, entity.posY, entity.posZ)) + "m) " : "")
                    + (isHacker(entity) ? entity.getName() : name)
                    + (elements.getMultiBool("Health") ? healthText : "");

            if (bgMode.get().equalsIgnoreCase("Both") || bgMode.get().equalsIgnoreCase("Name"))
                RenderUtils.SmoothRect(
                        (x + (x2 - x) / 2) - (!font.get().equals(Fonts.minecraftFont) ? font.get().getStringWidth(nameText) : mc.fontRendererObj.getStringWidth(nameText) / 2f) / 2f - 1,
                        y - (!font.get().equals(Fonts.minecraftFont) ? font.get().FONT_HEIGHT + 5 : mc.fontRendererObj.FONT_HEIGHT - 2),
                        (!font.get().equals(Fonts.minecraftFont) ? font.get().getStringWidth(nameText) : mc.fontRendererObj.getStringWidth(nameText) / 2f) + 2,
                        (!font.get().equals(Fonts.minecraftFont) ? font.get().FONT_HEIGHT + 6 : mc.fontRendererObj.FONT_HEIGHT - 4 ),
                        (isHacker(entity) ? new Color(255, 50, 50, bgAlpha.getMaximum()).getRGB() : new Color(0, 0, 0, bgAlpha.get()).getRGB()));


            if (!font.get().equals(Fonts.minecraftFont)) {
                font.get().drawString(nameText, (int) ((x + ((x2 - x) / 2)) - (font.get().getStringWidth(nameText) / 2f)), (int) (y - font.get().FONT_HEIGHT - 2), getNameColor(entity), shadow.get());
            } else {
                GL11.glPushMatrix();
                GL11.glScalef(fontScale.get() * 0.5f, fontScale.get() * 0.5f, fontScale.get() * 0.5f);
                float offset = 2f / fontScale.get();
                font.get().drawString(nameText, ((x + ((x2 - x) / 2)) - (font.get().getStringWidth(nameText) / 4f)) * offset, (y - font.get().FONT_HEIGHT / 2f - 2) * offset, getNameColor(entity), shadow.get());
                GL11.glPopMatrix();
                GL11.glScalef(1.0f, 1.0f, 1.0f);
            }
            GL11.glPopMatrix();
        }
    }

    private void Tag3D(Render3DEvent event, EntityLivingBase entity, String name) {
        RenderManager renderManager = mc.getRenderManager();
        double pX = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks()
                - renderManager.renderPosX);
        double pY = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks()
                - renderManager.renderPosY + (double) entity.getEyeHeight() + 0.55);
        double pZ = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks()
                - renderManager.renderPosZ);
        //pY += entity.isSneaking() ? 0.5 : 0.7;
        String bot = AntiBot.isBot(entity) && LiquidSense.moduleManager.getModule(AntiBot.class).getState() ? "§9[BOT]" : "";
        String team = Teams.isInYourTeam(entity) && LiquidSense.moduleManager.getModule(Teams.class).getState() ? "§b[TEAM]" : "";
        String hack = isHacker(entity) ? "§c[HACK]" : "";
        String lol = !bot.equals("") ? bot + name : (!team.equals("") ? team + name : (!hack.equals("") ? hack + name : "§a" + name));
        String hp = elements.getMultiBool("Health") ? "§7" + (int)entity.getHealth() + "hp" : "";
        String dis = elements.getMultiBool("Distance") ? "§7"+ (int)mc.thePlayer.getDistanceToEntity(entity)+"m " : "";
        String element = dis +hp;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) pX, (float)pY, (float)pZ);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        float distance = mc.thePlayer.getDistanceToEntity(entity) / 4F;
        if (distance < 1F) distance = 1F;
        float scale = distance / 100F * fontScale.get();
        GL11.glScalef(-scale, -scale, scale);
        GLUtils.setGLCap(2896, false);
        GLUtils.setGLCap(2929, false);
        int width = font.get().getStringWidth(lol) / 2;
        int elementwidth = font.get().getStringWidth(element) / 2;
        GLUtils.setGLCap(3042, true);
        GL11.glBlendFunc(770, 771);
        int borderwidth = Math.max(width + 2, elementwidth + 2);
        drawBorderedRectNameTag(-borderwidth - 2, -(font.get().FONT_HEIGHT + 9), borderwidth + 2, 2.0f, 1.0f,
                RenderUtils.reAlpha(Color.BLACK.getRGB(), 0.3f), RenderUtils.reAlpha(Color.BLACK.getRGB(), 0.3f));
        GL11.glColor3f(1f, 1f, 1f);
        font.get().drawString(lol, -width, -(font.get().FONT_HEIGHT + 8), -1, shadow.get());
        font.get().drawString(element, (float)-font.get().getStringWidth(element) / 2, -(font.get().FONT_HEIGHT - 2), -1, shadow.get());
        int COLOR = getNameColor(entity);
        float nowhealth = (float) Math.ceil(entity.getHealth() + entity.getAbsorptionAmount());
        float health = nowhealth / (entity.getMaxHealth() + entity.getAbsorptionAmount());
        RenderUtils.drawRect(borderwidth + health * borderwidth * 2 - borderwidth * 2 + 2, 2f, -borderwidth - 2, 0.9f, COLOR);
        GL11.glPushMatrix();
        int xOffset = 0;
        if (elements.getMultiBool("Armor") && entity instanceof EntityPlayer) {
            for (ItemStack armourStack : ((EntityPlayer) entity).inventory.armorInventory) {
                if (armourStack != null) xOffset -= 11;
            }
            ItemStack renderStack;
            if (entity.getHeldItem() != null) {
                xOffset -= 8;
                renderStack = entity.getHeldItem().copy();
                if (renderStack.hasEffect() && (renderStack.getItem() instanceof ItemTool || renderStack.getItem() instanceof ItemArmor)) renderStack.stackSize = 1;
                renderItemStack(renderStack, xOffset, -35);
                xOffset += 20;
            }
            for (ItemStack armourStack : ((EntityPlayer)entity).inventory.armorInventory) {
                if (armourStack == null) continue;
                ItemStack renderStack1 = armourStack.copy();
                if (renderStack1.hasEffect() && (renderStack1.getItem() instanceof ItemTool || renderStack1.getItem() instanceof ItemArmor)) renderStack1.stackSize = 1;
                this.renderItemStack(renderStack1, xOffset, -35);
                xOffset += 20;
            }
        }

        GL11.glPopMatrix();
        GLUtils.revertAllCaps();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    public void drawArmor(EntityPlayer player, double posX, double posY) {
        final ArrayList<ItemStack> stacks = new ArrayList<>();
        if (player.getHeldItem() != null) stacks.add(player.getHeldItem());
        stacks.addAll(Arrays.stream(player.inventory.armorInventory).filter(Objects::nonNull).collect(Collectors.toList()));
        double offset = (posX - (stacks.size() * 9.5));
        for (ItemStack stack : stacks) {
            if (bgMode.get().equalsIgnoreCase("Both") || bgMode.get().equalsIgnoreCase("Armor"))
                RenderUtils.SmoothRect(offset - 1, posY - 1, 18, 18, new Color(30, 30, 30,  bgAlpha.get()).getRGB());
            GlStateManager.enableLighting();
            mc.getRenderItem().renderItemIntoGUI(stack, (int)offset, (int)posY);
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, (int)offset, (int)posY, "");
            GlStateManager.disableLighting();
            NBTTagList enchants = stack.getEnchantmentTagList();
            GlStateManager.pushMatrix();
            GlStateManager.disableDepth();
            if (stack.isStackable()) {
                if (!font.get().equals(Fonts.minecraftFont))
                    font.get().drawString(String.valueOf(stack.stackSize), (float)offset, (float)posY + 11, 0xDDD1E6, shadow.get());
                else {
                    font.get().drawString(String.valueOf(stack.stackSize), (float) (offset), (float) (posY + 11), 0xDDD1E6, shadow.get());
                }
            }
            if (stack.getItem() == Items.golden_apple && stack.getMetadata() == 1) {
                if (!font.get().equals(Fonts.minecraftFont))
                    font.get().drawString("op", (float)offset, (float)posY + 2, 0xFFFF0000, shadow.get());
                else {
                    font.get().drawString("op", (float) (offset), (float) ((posY + 2)), 0xFFFF0000, shadow.get());
                }
            }
            Enchantment[] goodEnchants = new Enchantment[]{Enchantment.protection, Enchantment.unbreaking, Enchantment.sharpness, Enchantment.fireAspect, Enchantment.efficiency, Enchantment.featherFalling, Enchantment.power, Enchantment.flame, Enchantment.punch, Enchantment.fortune, Enchantment.infinity, Enchantment.thorns};
            if (enchants != null) {
                double enchantmentY = posY + 11;
                if (enchants.tagCount() > 3) {
                    if (!font.get().equals(Fonts.minecraftFont))
                        font.get().drawString("god", (float)offset, (float)enchantmentY, new Color(0xF0311D).getRGB(), shadow.get());
                    else {
                        font.get().drawString("god", (float) (offset), (float) (enchantmentY), new Color(0xF0311D).getRGB(), shadow.get());
                    }
                } else {
                    for (int index = 0; index < enchants.tagCount(); ++index) {
                        short id = enchants.getCompoundTagAt(index).getShort("id");
                        short level = enchants.getCompoundTagAt(index).getShort("lvl");
                        Enchantment enc = Enchantment.getEnchantmentById(id);
                        for (Enchantment goodEnchant : goodEnchants) {
                            if (enc == goodEnchant) {
                                String encName = Objects.requireNonNull(enc).getTranslatedName(level).substring(0, 1).toLowerCase();
                                if (level > 99) encName = encName + "99+";
                                else encName = encName + level;
                                if (!font.get().equals(Fonts.minecraftFont))
                                    font.get().drawString(encName, (float)offset, (float)enchantmentY, 0xDDD1E6, shadow.get());
                                else {
                                    font.get().drawString(encName, (float) (offset), (float) (enchantmentY), 0xDDD1E6, shadow.get());
                                }
                                enchantmentY -= 5;
                                break;
                            }
                        }
                    }
                }
            }
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
            offset += 19;
        }
    }

    private void renderItemStack(ItemStack stack, int x, int y) {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().zLevel = -150.0F;
        whatTheFuckOpenGLThisFixesItemGlint();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, y);
        mc.getRenderItem().zLevel = 0.0F;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5D, 0.5D, 0.5D);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GL11.glPopMatrix();
    }

    private void whatTheFuckOpenGLThisFixesItemGlint() {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    private void drawBorderedRectNameTag(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        RenderUtils.drawRect(x, y, x2, y2, col2);
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f2 = (col1 >> 16 & 0xFF) / 255.0F;
        float f3 = (col1 >> 8 & 0xFF) / 255.0F;
        float f4 = (col1 & 0xFF) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    private int getNameColor(EntityLivingBase ent) {
        if (EntityUtils.isFriend(ent)) {
            return new Color(100, 150, 255).getRGB();
        } else if (ent.getName().equals(mc.thePlayer.getName())) {
            return new Color(0xFF99ff99).getRGB();
        } else if (isHacker(ent)) {
            return ColorUtils.rainbow().getRGB();
        } else
            return new Color(red.get(), green.get(), blue.get()).getRGB();
    }

    private boolean isHacker(EntityLivingBase entity) {
        EventLogger eventLogger = (EventLogger) LiquidSense.moduleManager.getModule(EventLogger.class);
        return (eventLogger.getState() && eventLogger.hackerDetect.get() && EventLogger.isHacker((EntityPlayer) entity));
    }
}
