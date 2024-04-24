package me.crimp.claudius.mod.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.crimp.claudius.claudius;
import me.crimp.claudius.event.events.ClientEvent;
import me.crimp.claudius.event.events.Render2DEvent;
import me.crimp.claudius.mod.command.Command;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;
import me.crimp.claudius.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUD extends Module {
    private static final ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);

    private final Setting<Boolean> renderingUp = register(new Setting<>("RenderingUp", true, "Orientation of the HUD-Elements."));
    private final Setting<Boolean> arrayList = register(new Setting<>("ArrayList", true, "Lists the active modules."));
    private final Setting<Boolean> totems = register(new Setting<>("Totems", true, "TotemHUD"));
    private final Setting<Boolean> armor = register(new Setting<>("Armor", true, "ArmorHud"));
    private final Setting<Boolean> greeter = register(new Setting<>("Greeter", false, "The time"));
    public Setting<TextUtil.Color> bracketColor = register(new Setting<>("BracketColor", TextUtil.Color.WHITE));
    public Setting<TextUtil.Color> commandColor = register(new Setting<>("NameColor", TextUtil.Color.AQUA));
    public Setting<Boolean> Capes = register(new Setting<>("Capes", true, "Kekw"));
    public Setting<Boolean> CustomBg = register(new Setting<>("CustomBackground", true, "CustomBackground in main menu"));
    public Setting<RenderingMode> renderingMode = register(new Setting<>("Ordering", RenderingMode.Length));
    public static final String command = "Claudius";
    private int color;
    private boolean shouldIncrement;
    private int hitMarkerTimer;

    public HUD() {
        super("HUD", "HUD Elements rendered on your screen", Module.Category.Client, false, false);
        this.enabled.setValue(true);
        this.drawn.setValue(false);
    }
    public Timer timer = new Timer();

    public static HUD INSTANCE = new HUD();

    @Override
    public void onUpdate() {
        if (this.shouldIncrement) this.hitMarkerTimer++;
        if (this.hitMarkerTimer == 10) {
            this.hitMarkerTimer = 0;
            this.shouldIncrement = false;
        }
    }




    public void onRender2D(Render2DEvent event) {
        if (fullNullCheck()) return;
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        color = ColorUtil.toRGBA(ClickGuiModule.INSTANCE.red.getValue(), ClickGuiModule.INSTANCE.green.getValue(), ClickGuiModule.INSTANCE.blue.getValue());
        int j = (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat && !this.renderingUp.getValue()) ? 14 : 0;
        if (this.arrayList.getValue()) if (this.renderingUp.getValue()) {
            if (this.renderingMode.getValue() == RenderingMode.ABC) {
                for (int k = 0; k < claudius.moduleManager.sortedModulesABC.size(); k++) {
                    String str = claudius.moduleManager.sortedModulesABC.get(k);
                    claudius.textManager.drawString(str, (width - 2 - this.renderer.getStringWidth(str)), (2 + j * 10), this.color, true);
                    j++;
                }
            } else {
                for (int k = 0; k < claudius.moduleManager.sortedModules.size(); k++) {
                    Module module = claudius.moduleManager.sortedModules.get(k);
                    String str = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    claudius.textManager.drawString(str, (width - 2 - this.renderer.getStringWidth(str)), (2 + j * 10), this.color, true);
                    j++;
                }
            }
        } else if (this.renderingMode.getValue() == RenderingMode.ABC) {
            for (int k = 0; k < claudius.moduleManager.sortedModulesABC.size(); k++) {
                String str = claudius.moduleManager.sortedModulesABC.get(k);
                j += 10;
                claudius.textManager.drawString(str, (width - 2 - this.renderer.getStringWidth(str)), (height - j), this.color, true);
            }
        } else {
            for (int k = 0; k < claudius.moduleManager.sortedModules.size(); k++) {
                Module module = claudius.moduleManager.sortedModules.get(k);
                String str = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                j += 10;
                claudius.textManager.drawString(str, (width - 2 - this.renderer.getStringWidth(str)), (height - j),  this.color, true);
            }
        }
        String grayString = String.valueOf(ChatFormatting.GRAY);
        int i = (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat && this.renderingUp.getValue()) ? 13 : (this.renderingUp.getValue() ? -2 : 0);
        if (this.renderingUp.getValue()) {
            String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.debugFPS;
            String str1 = grayString + "Ping " + ChatFormatting.WHITE + claudius.serverManager.getPing();
            if (this.renderer.getStringWidth(str1) > this.renderer.getStringWidth(fpsText)) {
            }
        } else {
            String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.debugFPS;
            String str1 = grayString + "Ping " + ChatFormatting.WHITE + claudius.serverManager.getPing();
            if (this.renderer.getStringWidth(str1) > this.renderer.getStringWidth(fpsText)) {
            }
        }
        if (this.totems.getValue()) renderTotemHUD();
        if (this.greeter.getValue()) renderGreeter();
        if (this.armor.getValue()) {
            renderBootHUD();
            renderleggingHUD();
            renderChestplateHUD();
            renderHelmetHUD();
        }
    }

    public void renderGreeter() {
        int width = this.renderer.scaledWidth;
        String text = "";
        if (this.greeter.getValue()) text = text + MathUtil.getTimeOfDay() + mc.player.getDisplayNameString();
            this.renderer.drawString(text, width / 2.0F - this.renderer.getStringWidth(text) / 2.0F + 2.0F, 2.0F, this.color, true);
        }

    public void renderLag() {}

    public void renderTotemHUD() {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        int totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.TOTEM_OF_UNDYING)).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)
            totems += mc.player.getHeldItemOffhand().getCount();
        if (totems > 0) {
            GlStateManager.enableTexture2D();
            int i = width / 2;
            int y = height - 55; //- ((mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            int x = i - 189 + 180 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(totem, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, totem, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(totems + "", (x + 19 - 2 - this.renderer.getStringWidth(totems + "")), (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    private static final ItemStack boot = new ItemStack(Items.DIAMOND_BOOTS);
    public void renderBootHUD() {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        int boots = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.DIAMOND_BOOTS)).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.DIAMOND_BOOTS)
            boots += mc.player.getHeldItemOffhand().getCount();
        if (boots > 0) {
            GlStateManager.enableTexture2D();
            int i = width / 2;
            int y = height - 55;
            if ((mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure())) y = height - 10;
            int x = i - 189 + 180 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(boot, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, boot, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(boots + "", (x + 19 - 2 - this.renderer.getStringWidth(boots + "")), (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    private static final ItemStack legging = new ItemStack(Items.DIAMOND_LEGGINGS);
    public void renderleggingHUD() {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        int leggings = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.DIAMOND_LEGGINGS)).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.DIAMOND_LEGGINGS)
            leggings += mc.player.getHeldItemOffhand().getCount();
        if (leggings > 0) {
            GlStateManager.enableTexture2D();
            int i = width / 2;
            int y = height - 55;
            if ((mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure())) y = height - 10; //- ((mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            int x = i - 189 + 180 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(legging, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, legging, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(leggings + "", (x + 19 - 2 - this.renderer.getStringWidth(leggings + "")), (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    private static final ItemStack chestplate = new ItemStack(Items.DIAMOND_CHESTPLATE);
    public void renderChestplateHUD() {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        int chestplates = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.DIAMOND_CHESTPLATE)).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.DIAMOND_CHESTPLATE)
            chestplates += mc.player.getHeldItemOffhand().getCount();
        if (chestplates > 0) {
            GlStateManager.enableTexture2D();
            int i = width / 2;
            int y = height - 55; //- ((mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            int x = i - 189 + 180 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(chestplate, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, chestplate, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(chestplates + "", (x + 19 - 2 - this.renderer.getStringWidth(chestplates + "")), (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    private static final ItemStack helmet = new ItemStack(Items.DIAMOND_HELMET);
    public void renderHelmetHUD() {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        int helmets = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.DIAMOND_HELMET)).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.DIAMOND_HELMET)
            helmets += mc.player.getHeldItemOffhand().getCount();
        if (helmets > 0) {
            GlStateManager.enableTexture2D();
            int i = width / 2;
            int y = height - 55; //- ((mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            int x = i - 189 + 180 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(helmet, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, helmet, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(helmets + "", (x + 19 - 2 - this.renderer.getStringWidth(helmets + "")), (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(AttackEntityEvent event) {
        this.shouldIncrement = true;
    }

    public void onLoad() {
        claudius.commandManager.setClientMessage(getCommandMessage());
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && equals(event.getSetting().getFeature()))
            claudius.commandManager.setClientMessage(getCommandMessage());
    }

    public String getCommandMessage() {
        return TextUtil.coloredString("[", this.bracketColor.getPlannedValue()) + TextUtil.coloredString(command, this.commandColor.getPlannedValue()) + TextUtil.coloredString("]", this.bracketColor.getPlannedValue());
    }

    public enum RenderingMode {
        Length, ABC
    }
}
