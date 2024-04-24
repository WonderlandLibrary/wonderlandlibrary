
package Method.Client.module.misc;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static Method.Client.Main.setmgr;
import static org.lwjgl.opengl.GL11.*;

public class Shulkerspy extends Module {

    /////////////////////

    public Shulkerspy() {
        super("Shulkerspy", Keyboard.KEY_NONE, Category.MISC, "See others last held Shulker");
    }

    public static ConcurrentHashMap<String, TileEntityShulkerBox> shulkerMap = new ConcurrentHashMap<>();

    public Setting Mode = setmgr.add(new Setting(" Mode", this, "Dynamic", "Dynamic", "Static"));
    public Setting X = setmgr.add(new Setting("X", this, 1, 0, 1000, false, Mode, "Static"));
    public Setting Y = setmgr.add(new Setting("Y", this, 1, 0, 1000, false, Mode, "Static"));
    public Setting Scale = setmgr.add(new Setting("Scale", this, 1, 0, 4, false, Mode, "Dynamic"));
    public Setting Background = setmgr.add(new Setting("Background", this, true));
    public Setting BgColor = setmgr.add(new Setting("BgColor", this, .22, .88, .22, .22, Background));

    @Subscribe
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (Mode.getValString().equalsIgnoreCase("Dynamic"))
            for (Entity object : mc.world.loadedEntityList) {
                if (object instanceof EntityOtherPlayerMP)
                    if (shulkerMap.containsKey(object.getName().toLowerCase())) {
                        IInventory inv = Shulkerspy.shulkerMap.get(object.getName().toLowerCase());
                        DrawBox((EntityLivingBase) object, inv);
                    }
            }
    }

    @Subscribe
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (Mode.getValString().equalsIgnoreCase("Static")) {
            int Players = 0;
            for (Entity object : mc.world.loadedEntityList) {
                if (object instanceof EntityOtherPlayerMP)
                    if (shulkerMap.containsKey(object.getName().toLowerCase())) {
                        IInventory inv = Shulkerspy.shulkerMap.get(object.getName().toLowerCase());
                        DrawSbox(inv, Players, (EntityLivingBase) object);
                        Players++;
                    }
            }
        }
    }

    private void DrawSbox(IInventory inv, int players, EntityLivingBase object) {
        RenderHelper.enableGUIStandardItemLighting();
        if (Background.getValBoolean()) {
            Gui.drawRect((int)X.getValDouble(), (int)Y.getValDouble() - (players * 100), (int)X.getValDouble() + 88 + 60, (int)Y.getValDouble() + 50 - (players * 100), BgColor.getcolor()); // background
        }
        mc.fontRenderer.drawStringWithShadow(object.getName() + "'s Shulker", (float) X.getValDouble() + 45, (float) Y.getValDouble() - 10, -1);

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack itemStack = inv.getStackInSlot(i);
            int offsetX = (int) (X.getValDouble() + (i % 9) * 16);
            int offsetY = (int) (Y.getValDouble() + (i / 9) * 16) - (players * 100);
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, offsetX, offsetY);
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, offsetX, offsetY, null);
        }
        RenderHelper.disableStandardItemLighting();
        mc.getRenderItem().zLevel = 0.0F;
    }

    public void DrawBox(EntityLivingBase e, IInventory inv) {
        int morey = ModuleManager.getModuleByName("NameTags").isToggled() ? -6 : 0;

        double scale = Math.max(Scale.getValDouble() * (mc.player.getDistance(e) / 20), 2);
        for (int i = 0; i < inv.getSizeInventory(); i++) {
           drawItem(e.posX, e.posY + e.height + ((0.75) * scale), e.posZ, -2.5 + ((i % 9)), -((i / 9) * 1.2) - morey, scale, inv.getStackInSlot(i));
        }
    }
    public static void drawItem(double x, double y, double z, double offX, double offY, double scale, ItemStack item) {

        glSetup(x, y, z);

        GlStateManager.scale(0.4 * scale, 0.4 * scale, 0);

        GlStateManager.translate(offX, offY, 0);
        mc.itemRenderer.renderItemSide(mc.player, item, ItemCameraTransforms.TransformType.NONE, false);
        GlStateManager.enableTexture2D();

        GlStateManager.disableLighting();

        GlStateManager.scale(-0.05F, -0.05F, 0);

        try {
            if (item.getCount() > 0) {
                int w = mc.fontRenderer.getStringWidth("x" + item.getCount()) / 2;
                mc.fontRenderer.drawStringWithShadow("x" + item.getCount(), 7 - w, 5, 0xffffff);
            }

            GlStateManager.scale(0.85F, 0.85F, 0.85F);

            int c = 0;
            for (Map.Entry<Enchantment, Integer> m : EnchantmentHelper.getEnchantments(item).entrySet()) {
                int w1 = mc.fontRenderer.getStringWidth(I18n.format(m.getKey().getName().substring(0, 2)) + m.getValue() / 2);
                mc.fontRenderer.drawStringWithShadow(
                        I18n.format(m.getKey().getName()).substring(0, 2) + m.getValue(), -4 - w1 + 3, c * 10 - 1,
                        m.getKey() == Enchantments.VANISHING_CURSE || m.getKey() == Enchantments.BINDING_CURSE
                                ? 0xff5050 : 0xffb0e0);
                c--;

            }

            GlStateManager.scale(0.6F, 0.6F, 0.6F);
            String dur = item.getMaxDamage() - item.getItemDamage() + "";
            int color = MathHelper.hsvToRGB(((float) (item.getMaxDamage() - item.getItemDamage()) / item.getMaxDamage()) / 3.0F, 1.0F, 1.0F);
            if (item.isItemStackDamageable())
                mc.fontRenderer.drawStringWithShadow(dur, -8 - dur.length() * 3, 15, new Color(color >> 16 & 255, color >> 8 & 255, color & 255).getRGB());


        } catch (Exception ignored) {
        }
        glCleanup();
    }

    public static void glSetup(double x, double y, double z) {
        GlStateManager.pushMatrix();
        RenderManager renderManager = mc.getRenderManager();
        GlStateManager.translate(x - mc.getRenderManager().viewerPosX, y - mc.getRenderManager().viewerPosY, z - mc.getRenderManager().viewerPosZ);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(mc.gameSettings.thirdPersonView == 2 ? -renderManager.playerViewX : renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.disableLighting();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
    }

    public static void glCleanup() {
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GlStateManager.enableDepth();
        GL11.glTranslatef(-.5f, 0, 0);
        GlStateManager.popMatrix();
    }
    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        List<Entity> valids = mc.world.getLoadedEntityList()
                .stream()
                .filter(en -> en instanceof EntityOtherPlayerMP)
                .filter(mp -> ((EntityOtherPlayerMP) mp).getHeldItemMainhand().getItem() instanceof ItemShulkerBox)
                .collect(Collectors.toList());


        for (Entity valid : valids) {
            EntityOtherPlayerMP mp = (EntityOtherPlayerMP) valid;
            TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
            ItemShulkerBox shulker = (ItemShulkerBox) mp.getHeldItemMainhand().getItem();
            entityBox.blockType = shulker.getBlock();
            entityBox.setWorld(mc.world);
            ItemStackHelper.loadAllItems(Objects.requireNonNull(mp.getHeldItemMainhand().getTagCompound()).getCompoundTag("BlockEntityTag"), entityBox.items);
            entityBox.readFromNBT(mp.getHeldItemMainhand().getTagCompound().getCompoundTag("BlockEntityTag"));
            entityBox.setCustomName(mp.getHeldItemMainhand().hasDisplayName() ? mp.getHeldItemMainhand().getDisplayName() : mp.getName() + "'s current shulker box");
            shulkerMap.put(mp.getName().toLowerCase(), entityBox);
        }

    }

}
