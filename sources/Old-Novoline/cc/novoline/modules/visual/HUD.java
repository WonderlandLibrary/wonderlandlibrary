package cc.novoline.modules.visual;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.Render2DEvent;
import cc.novoline.events.events.SettingEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.*;
import cc.novoline.utils.ScaleUtils;
import cc.novoline.utils.fonts.impl.Fonts;
import cc.novoline.utils.notifications.NotificationRenderer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cc.novoline.gui.screen.setting.SettingType.*;
import static cc.novoline.modules.PlayerManager.EnumPlayerType.TARGET;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.*;
import static java.lang.Integer.compare;
import static net.minecraft.block.material.Material.water;
import static net.minecraft.client.renderer.GlStateManager.*;
import static net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting;
import static net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting;
import static net.minecraft.util.EnumChatFormatting.*;
import static org.lwjgl.opengl.GL11.*;

public final class HUD extends AbstractModule {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int colorDelay = 11;
    private int colorLength = 110;
    private int displayWidth = 0, displayHeight = 0;
    private List<String> lastMessages = new CopyOnWriteArrayList<>();
    private cc.novoline.utils.fonts.api.FontRenderer font = Fonts.SF.SF_21.SF_21;
    private boolean setFont;

    /* properties @off */
    @Property("client-name")
    private final StringProperty clientName = createString("Novoline");
    @Property("scoreboard")
    private final IntProperty scoreboard = createInt(0).minimum(-150).maximum(200);
    @Property("scale")
    private final FloatProperty fontScale = createFloat(1f).minimum(0.4f).maximum(3f);
    @Property("hud-elements")
    private final ListProperty<String> hudElements = createList("ModuleList", "UserInfo", "ArmorHUD", "Scoreboard", "Bossbar", "PotionHUD", "Coords", "Name", "Time", "FPS")
            .acceptableValues("ModuleList", "UserInfo", "HotBar", "TargetsList", "ArmorHUD", "Scoreboard", "Bossbar", "PotionHUD", "Coords", "Speed", "Name", "Time", "FPS", "Inventory");
    @Property("hud-font")
    private final StringProperty hudFont = createString("Client").acceptableValues("Client", "Vanilla");
    @Property("suffix-type")
    private final StringProperty suffixType = createString("Simple").acceptableValues("Simple", "Dash", "Bracket", "None");
    @Property("background-alpha")
    private final IntProperty backgroundAlpha = createInt(50).minimum(1).maximum(255);
    @Property("background-mode")
    private final StringProperty backgroundMode = createString("Bar").acceptableValues("Outline", "Simple", "Split", "Bar");
    @Property("hud-mode")
    private final StringProperty hudMode = createString("Static").acceptableValues("Rainbow", "Astolfo", "Static", "Dynamic", "Type");
    @Property("color")
    private final ColorProperty color = createColor(0xFF8A8AFF);
    @Property("inv-x")
    private final IntProperty invx = PropertyFactory.createInt((int) screenSize.getWidth() / 4).minimum(1).maximum((int) screenSize.getWidth() / 2);
    @Property("inv-y")
    private final IntProperty invy = PropertyFactory.createInt((int) screenSize.getHeight() / 4).minimum(1).maximum((int) screenSize.getHeight() / 2);
    @Property("tlist-x")
    private final IntProperty tlx = PropertyFactory.createInt((int) screenSize.getWidth() / 4).minimum(1).maximum((int) screenSize.getWidth() / 2);
    @Property("tlist-y")
    private final IntProperty tly = PropertyFactory.createInt((int) screenSize.getHeight() / 4).minimum(1).maximum((int) screenSize.getHeight() / 2);
    @Property("notifications-sounds")
    private final BooleanProperty nSounds = PropertyFactory.booleanFalse();

    /* constructors @on */
    public HUD(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "HUD", "HUD", Keyboard.KEY_NONE, EnumModuleType.VISUALS, "Literally overlay", null);
        Manager.put(new Setting("CLIENT_NAME", "Client Name", TEXTBOX, this, "Client name", clientName, () -> hudElements.get().contains("Name")));
        Manager.put(new Setting("HUDELEMENTS", "Elements", SELECTBOX, this, hudElements));
        Manager.put(new Setting("HUD_FONT", "Font", COMBOBOX, this, hudFont));
        Manager.put(new Setting("SUFFIX_TYPE", "Suffix", COMBOBOX, this, suffixType, () -> hudElements.get().contains("ModuleList")));
        Manager.put(new Setting("BACKGROUND_MODE", "Background", COMBOBOX, this, backgroundMode));
        Manager.put(new Setting("POTION_MODE", "ModuleList", COMBOBOX, this, hudMode, () -> hudElements.get().contains("ModuleList")));
        Manager.put(new Setting("HUD_COLOR", "Color", COLOR_PICKER, this, color, null));
        Manager.put(new Setting("FONT_SCALE", "HUD Scale", SLIDER, this, fontScale, 0.1, () -> hudElements.get().contains("ModuleList") && hudFont.equals("Client")));
        Manager.put(new Setting("SCOREBOARD_POS", "Scoreboard Height", SLIDER, this, scoreboard, 5, () -> hudElements.get().contains("Scoreboard")));
        Manager.put(new Setting("BACKGROUND_ALPHA", "Background Alpha", SLIDER, this, backgroundAlpha, 5, () -> hudElements.get().contains("ModuleList")));
        Manager.put(new Setting("NOTIFICATION-SOUNDS", "Notification sounds", CHECKBOX, this, nSounds));
    }

    @Override
    public void onEnable() {
    }

    public List<String> getLastMessages() {
        return lastMessages;
    }

    public int getArrayStatic(int counter, int alpha) {
        final float[] hudHSB = color.getHSB();
        Color color = Color.getHSBColor(hudHSB[0], hudHSB[1], hudHSB[2]);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }

    public FloatProperty getFontScale() {
        return fontScale;
    }

    public float getScale(){
        float s = 1/getFontScale().get();
        switch (Minecraft.getInstance().gameSettings.guiScale) {
            case 0:
                return s / 2;
            case 1:
                return s / 0.5f;
            case 3:
                return s / 1.5f;
            default:
                return s;
        }
    }

    public int getArrayDynamic(float counter, int alpha) {
        float brightness = 1.0F - MathHelper.abs(MathHelper.sin(counter % 6000F / 6000F * Math.PI * 2.0F) * 0.6F);
        final float[] hudHSB = color.getHSB();
        Color color = Color.getHSBColor(hudHSB[0], hudHSB[1], brightness);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }

    public int getArrayRainbow(int counter, int alpha) {
        final int width = colorLength;

        double rainbowState = Math.ceil(System.currentTimeMillis() - (long) counter * width) / colorDelay;
        rainbowState %= 360;

        final float[] colors = color.getHSB();
        Color color = Color.getHSBColor((float) (rainbowState / 360), colors[1], colors[2]);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }

    public int getArrayType(int counter, float hue, int alpha) {
        final float[] hsb = color.getHSB();
        Color color = Color.getHSBColor(hue, hsb[1], hsb[2]);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }

    public int getArrayAstolfo(int counter, int alpha) {
        final int width = colorLength;
        double rainbowState = Math.ceil(System.currentTimeMillis() - (long) counter * width) / colorDelay;
        rainbowState %= 360;
        final float hue = (float) (rainbowState / 360) < 0.5 ? -((float) (rainbowState / 360)) : (float) (rainbowState / 360);
        final float[] colors = color.getHSB();
        Color color = Color.getHSBColor(hue, colors[1], colors[2]);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }

    public int getHUDColor() {
        final float[] hsb = color.getHSB();
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]).getRGB();
    }

    @EventTarget
    public void drawHUD(Render2DEvent event) {

        glPushMatrix();
        ScaleUtils.scale(mc);
        if (mc.currentScreen == null) {
            NotificationRenderer.draw(event.getResolution());
        }
        glPopMatrix();
        glPushMatrix();
        GL11.glScaled(fontScale.get(),fontScale.get(),fontScale.get());

        if (!isExhibition()) {
            if (hudElements.contains("FPS")) drawFPS(event);
            if (hudElements.contains("Time") && !mc.gameSettings.showDebugInfo) drawTime(event);
        }

        if (hudElements.contains("ArmorHUD")) drawArmorHUD(event);
        if (hudElements.contains("PotionHUD")) drawPotionHUD(event);


        if (hudElements.contains("UserInfo")) drawUserInfo(event);
        if (hudElements.contains("Name") && !mc.gameSettings.showDebugInfo) drawName(event);
        if (hudElements.contains("Coords")) drawCoordinates(event);
        if (hudElements.contains("Speed")) drawSpeed(event);

        if (hudElements.contains("TargetsList")) drawTargetsList(event);

        if (hudElements.contains("ModuleList") && !mc.gameSettings.showDebugInfo) {

            drawArrayList(event);

        }
        if (hudElements.contains("Inventory")) drawInventory();

        glColor4f(1, 1, 1, 1);
        glPopMatrix();
    }



    public IntProperty getInvx() {
        return invx;
    }

    public IntProperty getInvy() {
        return invy;
    }


    public void drawInventory() {
        if (!(mc.currentScreen instanceof GuiChat)) {
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableDepth();
            int x = invx.get();
            int y = invy.get();
            Gui.drawRect(x, y, x + 167, y + 73, new Color(29, 29, 29, 255).getRGB());
            Gui.drawRect(x + 1, y + 13, x + 166, y + 72, new Color(40, 40, 40, 255).getRGB());
            mc.fontRendererCrack.drawString("Your Inventory", x + 3, y + 3, 0xffffffff, true);


            boolean hasStacks = false;
            for (int i1 = 9; i1 < mc.player.inventoryContainer.inventorySlots.size() - 9; ++i1) {
                Slot slot = mc.player.inventoryContainer.inventorySlots.get(i1);
                if (slot.getHasStack()) hasStacks = true;
                int i = slot.xDisplayPosition;
                int j = slot.yDisplayPosition;
                mc.getRenderItem().renderItemAndEffectIntoGUI(slot.getStack(), x + i - 4, y + j - 68);
                mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererCrack, slot.getStack(), x + i - 4, y + j - 68, null);
            }
            if (mc.currentScreen instanceof GuiInventory) {
                mc.fontRendererCrack.drawString("Already in inventory",
                        x + 167 / 2 - mc.fontRendererCrack.getStringWidth("Already in inventory") / 2,
                        y + 72 / 2,
                        0xffffffff,
                        true);
            } else if (!hasStacks) {
                mc.fontRendererCrack.drawString("Empty...",
                        x + 167 / 2 - mc.fontRendererCrack.getStringWidth("Empty...") / 2,
                        y + 72 / 2,
                        0xffffffff,
                        true);
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableDepth();
        }
    }


    private int getArrayListColor(int counter, int alpha, float hue, float brightnesscounter) {
        int color = getArrayRainbow(counter, alpha);

        switch (hudMode.get().toLowerCase()) {
            case "type":
                color = getArrayType(counter, hue, alpha);
                break;

            case "astolfo":
                color = getArrayAstolfo(counter, alpha);
                break;

            case "static":
                color = getArrayStatic(counter, alpha);
                break;

            case "dynamic":
                color = getArrayDynamic(brightnesscounter, alpha);
        }

        return color;
    }

    public List<AbstractModule> getModules() {
        final FontRenderer font = mc.fontRendererObj;
        final String hudFont = this.hudFont.get();
        Stream<AbstractModule> stream = novoline.getModuleManager().getAbstractModules().stream();

        if (hudFont.equalsIgnoreCase("Vanilla")) {
            stream = stream.sorted((mod1, mod2) -> compare(font.getStringWidth(mod2.getFinalDisplayName()),
                    font.getStringWidth(mod1.getFinalDisplayName())));
        } else {
            stream = stream.sorted((mod1, mod2) -> compare(this.font.stringWidth(mod2.getFinalDisplayName()),
                    this.font.stringWidth(mod1.getFinalDisplayName())));
        }
        return stream.filter(m -> !m.isHidden()).collect(Collectors.toList());
    }

    public float getHeight(AbstractModule module) {
        float y = 2;
        for (AbstractModule abstractModule : getModules()) {
            if (abstractModule == module) {
                return y;
            }

            if (hudFont.equalsIgnoreCase("Vanilla")) {
                y += mc.fontRendererObj.getHeight() + 2;
            } else {
                y += font.getHeight() + 1;
            }
        }

        return 2;
    }

    @EventTarget
    public void onUpdate(Render2DEvent eventUpdate) {
        float y = hudFont.equals("Vanilla") ? -mc.fontRendererCrack.getHeight() + 1 : -font.getHeight() + 1;

        for (AbstractModule m : getModules()) {
            double clamp = MathHelper.clamp_double(mc.getDebugFPS() / 30, 1, 9999);

            if (m.isEnabled()) {
                m.offsetX = (float) (m.offsetX + (hudFont.equalsIgnoreCase("Vanilla") ?
                        mc.fontRendererObj.getStringWidth(m.getFinalDisplayName()) : font.stringWidth(m.getFinalDisplayName()) - m.offsetX) * (0.2 / clamp));
            } else if (m.offsetX < 0) {
                novoline.getModuleManager().getAbstractModules().remove(m);
            } else if (m.offsetX > -2) {
                m.offsetX = (float) (m.offsetX + (-2 - m.offsetX) * (0.2 / clamp));
            }
            m.offsetX = MathHelper.clamp_float(m.offsetX, -2, hudFont.equalsIgnoreCase("Vanilla") ?
                    mc.fontRendererObj.getStringWidth(m.getFinalDisplayName()) : font.stringWidth(m.getFinalDisplayName()));

            if (hudFont.equalsIgnoreCase("Vanilla")) {
                y += mc.fontRendererObj.getHeight() + 1.0F;
            } else {
                y += font.getHeight() + 1F;
            }

            m.offsetY = (float) (m.offsetY + (y - m.offsetY) * (0.2 / clamp));
        }
    }

    private void drawArrayList(Render2DEvent event) {
        int counter = 1;
        float j = Minecraft.getSystemTime();
        float y;
        final cc.novoline.utils.fonts.api.FontRenderer cFont = font;
        final String hudFont = this.hudFont.get();

        final List<AbstractModule> modules = getModules();

        if (getModule(HUD.class).isEnabled() && hudElements.get().contains("ModuleList")) {
            for (AbstractModule module : modules) {
                y = module.offsetY;
                /* Позиция для рисования шрифта */
                float x = (event.getResolution().getScaledWidthStatic(mc) * getScale()) - module.offsetX - 2.0F;
                /* Цвет бг */
                int bgColor = new Color(0, 0, 0, backgroundAlpha.get()).getRGB();
                /* Цвет шрифта */
                int modColor = getArrayListColor(counter, 255, module.categoryColor(), j);
                /* Ширина окна майна */
                float width = event.getResolution().getScaledWidthStatic(mc) * getScale();
                /* Высота Шрифта */
                float fontHeight = hudFont.equalsIgnoreCase("Vanilla") ? mc.fontRendererObj.getHeight() : cFont.getHeight();
                /* Отступы */
                float bgMargin = backgroundMode.get().equalsIgnoreCase("bar") ? 2.5f : 1.5f;

                if (backgroundMode.get().equalsIgnoreCase("outline") || backgroundMode.get().equalsIgnoreCase("split")) {
                    Gui.drawRect(
                            width - (module.offsetX + 6) - bgMargin,
                            y - (modules.indexOf(module) == 0 ? 2 : 0),
                            width - (module.offsetX + 5) - bgMargin,
                            y + fontHeight + 2, modColor);
                }
                if (backgroundMode.get().equalsIgnoreCase("outline")) {
                    float difference;

                    if (modules.indexOf(module) == modules.size() - 1) {
                        // нижняя часть
                        Gui.drawRect(
                                width - (module.offsetX + 6) - bgMargin,
                                y + fontHeight + 2,
                                width,
                                y + fontHeight + 3, modColor);
                    } else {
                        if (hudFont.equalsIgnoreCase("Vanilla")) {
                            difference = Math.abs(mc.fontRendererObj.getStringWidth(modules.get(modules.indexOf(module)).getFinalDisplayName()) -
                                    mc.fontRendererObj.getStringWidth(modules.get(modules.indexOf(module) + 1).getFinalDisplayName()));
                        } else {
                            difference = Math.abs(cFont.stringWidth(modules.get(modules.indexOf(module)).getFinalDisplayName()) -
                                    cFont.stringWidth(modules.get(modules.indexOf(module) + 1).getFinalDisplayName()));
                        }

                        // нижняя часть
                        Gui.drawRect(
                                width - (module.offsetX + 5) - bgMargin,
                                y + fontHeight + 1,
                                width - (module.offsetX + 5) - bgMargin + difference,
                                y + fontHeight + 2, modColor);
                    }
                }

                // сам бг
                Gui.drawRect(
                        width - (module.offsetX + 4) - bgMargin - (backgroundMode.equalsIgnoreCase("outline") || backgroundMode.equalsIgnoreCase("split") ? 1 : 0),
                        y - (modules.indexOf(module) == 0 ? 3 : 0),
                        width,
                        y + fontHeight + (modules.indexOf(module) == modules.size() - 1 ? 2 : 1), bgColor);

                // Текст

                if (hudFont.equalsIgnoreCase("Vanilla")) {
                    mc.fontRendererObj.drawStringWithShadow(module.getFinalDisplayName(), x - bgMargin, y + 1.0F, modColor);
                } else {
                    cFont.drawString(module.getFinalDisplayName(), x - bgMargin, y + 1.0F, modColor, true);
                }

                if (backgroundMode.get().equalsIgnoreCase("Bar")) {
                    Gui.drawRect(width - 1.0F, y - 1, width, y + fontHeight + (modules.indexOf(module) == modules.size() - 1 ? 2 : 1), modColor);
                }
                j -= 300;
                counter++;
            }
        }
    }


    private void drawArmorHUD(Render2DEvent event) {
        final int color = getHUDColor();
        glPushMatrix();

        final List<ItemStack> stuff = new ObjectArrayList<>();
        final boolean onWater = mc.player.isEntityAlive() && mc.player.isInsideOfMaterial(water);

        for (int index = 3; index >= 0; --index) {
            final ItemStack armor = mc.player.inventory.armorInventory[index];

            if (armor != null) stuff.add(armor);
        }

        if (mc.player.getCurrentEquippedItem() != null) stuff.add(mc.player.getCurrentEquippedItem());

        int split = -3;
        int split2 = -3;

        for (ItemStack item : stuff) {
            if (mc.world != null) {
                enableGUIStandardItemLighting();
                split += 16;
                split2 += 32;
            }

            final int damage = item.getMaxDamage() - item.getItemDamage();
            final String hudFont = this.hudFont.get();

            final ScaledResolution resolution = event.getResolution();
            final int scaledWidth = (int) (resolution.getScaledWidthStatic(mc) * getScale());
            final int scaledHeight = (int) (resolution.getScaledHeightStatic(mc)* getScale());

            if (!item.isStackable()) {
                disableStandardItemLighting();
                if (!(item.getItem() instanceof ItemPotion)) {
                    if (hudFont.equalsIgnoreCase("Client")) {
                        float v1 = (float) (scaledHeight - (onWater ? 68 : 58));
                        Fonts.SFTHIN.SFTHIN_10.SFTHIN_10.drawString(String.valueOf(damage), split + scaledWidth / 2F - 1.5F, v1, color, true);
                    } else {
                        glPushMatrix();
                        glScalef(0.5f, 0.5f, 0.5f);
                        mc.fontRendererObj.drawStringWithShadow(String.valueOf(damage), split2 + scaledWidth - 4F,
                                (float) (scaledHeight * 2 - (onWater ? 139 : 119)), color);
                        glScalef(2.0f, 2.0f, 2.0f);
                        glPopMatrix();
                    }
                }
            }

            pushMatrix();
            disableAlpha();
            clear(256);
            enableBlend();

            mc.getRenderItem().zLevel = -150.0F;
            mc.getRenderItem().renderItemAndEffectIntoGUI(item, split + (int) (scaledWidth / 2F) - 4F,
                    scaledHeight - (onWater ? 66.5F : 56.5F));

            if (item.isStackable()) {
                disableStandardItemLighting();
                if (hudFont.equalsIgnoreCase("Client")) {
                    float v1 = (float) (scaledHeight - (onWater ? 68 : 58));
                    Fonts.SFTHIN.SFTHIN_10.SFTHIN_10.drawString(String.valueOf(item.stackSize), split + scaledWidth / 2F - 1.5F, v1, color, true);
                } else {
                    glPushMatrix();
                    glScalef(0.5f, 0.5f, 0.5f);
                    mc.fontRendererObj
                            .drawStringWithShadow(String.valueOf(item.stackSize), split2 + scaledWidth - 4F,
                                    (float) (scaledHeight * 2 - (onWater ? 139 : 119)), color);
                    glScalef(2.0f, 2.0f, 2.0f);
                    glPopMatrix();
                }
            }

            mc.getRenderItem().zLevel = 0.0F;

            enableBlend();

            final float z = 0.5F;

            scale(z, z, z);
            disableDepth();
            disableLighting();
            enableDepth();
            scale(2.0f, 2.0f, 2.0f);
            enableAlpha();
            popMatrix();
            item.getEnchantmentTagList();
        }

        glPopMatrix();
    }

    private void drawUserInfo(Render2DEvent render2DEvent) {
        String build = "Build - " + RESET + novoline.getVersion();
        final String info = GRAY + build + GRAY + " | " + "UID - " + WHITE + "TeamKhonsari";

        if (hudFont.equalsIgnoreCase("Client")) {
            font.drawString(info, (float) ((render2DEvent.getResolution().getScaledWidthStatic(mc) * getScale()) - font.stringWidth(info) - 2),
                    (float) ((render2DEvent.getResolution().getScaledHeightStatic(mc) * getScale()) - font.getHeight() - 2), 0xFFFFFF, true);
        } else {
            mc.fontRendererObj.drawStringWithShadow(info, (render2DEvent.getResolution().getScaledWidthStatic(mc) * getScale()) - mc.fontRendererObj.getStringWidth(info) - 1,
                    (render2DEvent.getResolution().getScaledHeightStatic(mc) * getScale()) - mc.fontRendererObj.getHeight() - 1, 0xFFFFFF);
        }
    }

    private void drawPotionHUD(Render2DEvent event) {
        float y = 0;
        int counter = 1;
        float j = Minecraft.getSystemTime();

        for (PotionEffect effect : mc.player.getActivePotionEffects()) {
            final Potion potion = Potion.potionTypes[effect.getPotionID()];
            final StringBuilder pTypeBuilder = new StringBuilder(I18n.format(potion.getName()));

            switch (effect.getAmplifier()) {
                case 1:
                    pTypeBuilder.append(" II");
                    break;
                case 2:
                    pTypeBuilder.append(" III");
                    break;
                case 3:
                    pTypeBuilder.append(" IV");
                    break;
                case 4:
                    pTypeBuilder.append(" V");
                    break;
            }

            if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                pTypeBuilder.append("\2477\2476 ").append(Potion.getDurationString(effect));
            } else if (effect.getDuration() < 300) {
                pTypeBuilder.append("\2477\247c ").append(Potion.getDurationString(effect));
            } else if (effect.getDuration() > 600) {
                pTypeBuilder.append("\2477\2477 ").append(Potion.getDurationString(effect));
            }

            final int yChat = mc.ingameGUI.getChatGUI().getChatOpen() && !hudElements
                    .contains("HotBar") && !hudElements.contains("UserInfo") ? 5 : -10;
            final String pType = String.valueOf(pTypeBuilder);
            final Color color = new Color(potion.getLiquidColor());
            final float[] newColor = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);

            int rgbColor = getArrayListColor(counter, 255, newColor[0], j);
            final String potionMode = hudMode.get();

            final ScaledResolution resolution = event.getResolution();
            final int scaledWidth = (int) (resolution.getScaledWidthStatic(mc) * getScale()), scaledHeight = (int) (resolution.getScaledHeightStatic(mc) * getScale());

            if (hudFont.equalsIgnoreCase("Client")) {
                float v1 = scaledHeight - font.getHeight() + y - (hudElements.contains("UserInfo") ? font.getHeight() + 14 : 12.5F) - yChat;
                font.drawString(pType, (float) (scaledWidth - font.stringWidth(pType) - 2), v1, rgbColor, true);
            } else {
                mc.fontRendererObj.drawStringWithShadow(pType, scaledWidth - mc.fontRendererObj.getStringWidth(pType) - 1,
                        scaledHeight - mc.fontRendererObj.getHeight() + y - (hudElements.contains("HotBar") ? 35 :
                                hudElements.contains("UserInfo") ? 23.5F : 13F) - yChat, rgbColor);
            }

            counter++;
            j -= 300;
            y -= hudFont.equals("Vanilla") ? mc.fontRendererObj.getHeight() + 1 : font.getHeight() + 1;
        }
    }

    private void drawCoordinates(Render2DEvent event) {
        String xyz = String.format("\u00A7rXYZ:\u00A7f %.0f %.0f %.0f", mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ);

        if (hudFont.equalsIgnoreCase("Client")) {
            font.drawString(xyz, (float) 1, (event.getResolution().getScaledHeightStatic(mc) * getScale()) - (hudFont.equals("Vanilla") ? mc.fontRendererObj.getHeight() + 1 : font.getHeight() * 1.2f), getHUDColor(), true);
        } else {
            mc.fontRendererObj.drawStringWithShadow(xyz, 2, (event.getResolution().getScaledHeightStatic(mc) * getScale()) - 10F, getHUDColor());
        }
    }

    private void drawFPS(@NonNull Render2DEvent event) {
        final float y = (event.getResolution().getScaledHeightStatic(mc) * getScale()) - (hudElements.get().contains("Coords") ? hudElements.get().contains("Speed") ? hudFont.equals("Vanilla") ? mc.fontRendererObj.getHeight() * 3 + 2 : font.getHeight() * 3.2f + 3 : hudFont.equals("Vanilla") ? mc.fontRendererObj.getHeight() * 2 + 2 : font.getHeight() * 2.2f + 1 : hudElements.contains("Speed") ? hudFont.equals("Vanilla") ? mc.fontRendererObj.getHeight() * 2 + 2 : font.getHeight() * 2.2f + 2 : hudFont.equals("Vanilla") ? mc.fontRendererObj.getHeight() + 2 : font.getHeight() * 1.2f + 1);
        final String fpsFont = RESET + "FPS: " + WHITE + mc
                .getDebugFPS(), fpsVanilla = RESET + "FPS: " + WHITE + mc.getDebugFPS();

        if (hudFont.equalsIgnoreCase("Client")) {
            font.drawString(fpsFont, (float) 1, y, getHUDColor(), true);
        } else {
            mc.fontRendererObj.drawStringWithShadow(fpsVanilla, 2, y, getHUDColor());
        }
    }

    private void drawSpeed(@NonNull Render2DEvent event) {
        final float y = (event.getResolution().getScaledHeightStatic(mc) * getScale()) - (hudElements.get().contains("Coords") ? hudFont.equals("Vanilla") ? mc.fontRendererObj.getHeight() * 2 + 2 : font.getHeight() * 2.2f + 2 : hudFont.equals("Vanilla") ? mc.fontRendererObj.getHeight() * 2 + 2 : font.getHeight() * 1.2f + 1);
        String speed = String.format("%.2f", mc.player.getLastTickDistance() * 20 * (mc.timer.timerSpeed < 1 ? 1 : mc.timer.timerSpeed));

        if (hudFont.equalsIgnoreCase("Client")) {
            font.drawString("Speed: " + EnumChatFormatting.WHITE + speed + " b/s", (float) 1, y, getHUDColor(), true);
        } else {
            mc.fontRendererObj.drawStringWithShadow("Speed: " + EnumChatFormatting.WHITE + speed + " b/s", 2, y, getHUDColor());
        }
    }

    private void drawName(Render2DEvent event) {
        long j = Minecraft.getSystemTime();
        final String clientName = this.clientName.get();
        String name = !clientName.isEmpty() ? clientName.charAt(0) + clientName.substring(1) : "N" + "ovoline";

        int x = 1;


        if (hudFont.equalsIgnoreCase("Client")) {
            for (int i = 0; i < name.length(); i++) {
                Fonts.SF.SF_20.SF_20.drawString(String.valueOf(name.charAt(i)), x, 4.5F, getArrayDynamic(j, 255), true);
                x += Fonts.SF.SF_20.SF_20.charWidth(name.charAt(i));
                j -= 300;
            }

        } else {
            for (int i = 0; i < name.length(); i++) {
                mc.fontRendererObj.drawString(String.valueOf(name.charAt(i)), x, 4.5F, getArrayDynamic(j, 255), true);
                x += mc.fontRendererObj.getCharWidth(name.charAt(i));
                j -= 300;
            }
        }
    }

    private void drawTime(Render2DEvent event) {
        int x = 3, pos = 1;

        final String clientName_ = this.clientName.get();
        String name = !clientName_.isEmpty() ? clientName_.charAt(0) + clientName_.substring(1) : "N" + "ovoline";

        if (hudFont.equalsIgnoreCase("Client")) {
            for (int i = 0; i < name.length(); i++) {
                pos += Fonts.SF.SF_20.SF_20.charWidth(name.charAt(i));
            }

            if (hudElements.get().contains("Name")) {
                final String clientName = this.clientName.get();
                x = !clientName.isEmpty() ? pos + 2 : 40;
            }
            Fonts.SF.SF_20.SF_20.drawString(GRAY + "(" + WHITE + dateFormat.format(new Date()) + GRAY + ")", (float) x, 4.0F, getHUDColor(), true);
        } else {
            for (int i = 0; i < name.length(); i++) {
                pos += mc.fontRendererObj.getCharWidth(name.charAt(i));
            }

            if (hudElements.get().contains("Name")) {
                final String clientName = this.clientName.get();
                x = !clientName.isEmpty() ? pos + 2 : 44;
            }
            mc.fontRendererObj
                    .drawStringWithShadow(GRAY + "(" + WHITE + dateFormat.format(new Date()) + GRAY + ")", x, 4.5F,
                            getHUDColor());
        }
    }


    public void renderTargetsList() {
        int yHeight = 13;
        int x = tlx.get();
        int y = tly.get();
        final List<Entity> targets = getLoadedTargets();
        int width = 0;

        for (Entity target : targets) {
            yHeight += mc.fontRendererCrack.getHeight() + 1;
            int stringWidth = mc.fontRendererCrack.getStringWidth(target.getName());

            if (stringWidth > width) {
                width = stringWidth;
            }
        }

        width = Math.max(100, width + 25);
        width += width == 100 ? 0 : 30;
        yHeight -= mc.fontRendererCrack.getHeight() + 1;

        if (targets.isEmpty()) {
            yHeight -= 2;
        }

        Gui.drawRect(x, y - 13, x + width, y + yHeight, new Color(29, 29, 29, 255).getRGB());
        Gui.drawRect(x + 1, y, x + width - 1, y + yHeight - 1, new Color(40, 40, 40, 255).getRGB());
        mc.fontRendererCrack.drawString("Your Targets", x + 3, y - 10, 0xffffffff, true);
        int yOffset = y;

        for (Entity target : targets) {
            mc.fontRendererCrack.drawString(target.getName(), x + 3, yOffset + 2, new Color(200, 200, 200, 255).getRGB());
            String s = (int) mc.player.getDistanceToEntity(target) + "m";
            mc.fontRendererCrack.drawString(s, x + width - 1 - mc.fontRendererCrack.getStringWidth(s) - 1, yOffset + 2, new Color(200, 200, 200, 255).getRGB());
            yOffset += mc.fontRendererCrack.getHeight() + 1;
        }
    }

    public double[] getTargetListWidthAndHeight() {
        final List<Entity> targets = getLoadedTargets();
        int yHeight = 13;
        int width = 0;

        for (Entity target : targets) {
            yHeight += mc.fontRendererCrack.getHeight() + 1;
            int stringWidth = mc.fontRendererCrack.getStringWidth(target.getName());

            if (stringWidth > width) {
                width = stringWidth;
            }
        }

        width = Math.max(100, width + 25);
        width += width == 100 ? 0 : 30;
        yHeight -= mc.fontRendererCrack.getHeight() + 1;
        return new double[]{width, yHeight};
    }


    private void drawTargetsList(Render2DEvent event) {
        if (!(mc.currentScreen instanceof GuiChat)) {
            renderTargetsList();
        }
    }

    public IntProperty getTlx() {
        return tlx;
    }

    public IntProperty getTly() {
        return tly;
    }

    private List<Entity> getLoadedTargets() {
        final List<String> players = novoline.getPlayerManager().whoHas(TARGET);
        return mc.world.getLoadedEntityList().stream().filter(e -> players.contains(e.getName().toLowerCase()))
                .collect(Collectors.toCollection(ObjectArrayList::new));
    }

    private int getLongestName() {
        final List<Entity> loadedTargets = getLoadedTargets();
        int lenght = 0;

        for (Entity entity : loadedTargets) {
            if (Fonts.SF.SF_16.SF_16.stringWidth(entity.getName()) > lenght) {
                lenght = Fonts.SF.SF_16.SF_16.stringWidth(entity.getName());
            }
        }

        return lenght + 35;
    }

    private boolean isExhibition() {
        return clientName.get().equalsIgnoreCase("Exhibition");
    }

    public StringProperty getClientName() {
        return clientName;
    }

    public IntProperty getScoreboard() {
        return scoreboard;
    }

    public ListProperty<String> getHudElements() {
        return hudElements;
    }

    public StringProperty getHudFont() {
        return hudFont;
    }

    public StringProperty getSuffixType() {
        return suffixType;
    }

    public IntProperty getBackgroundAlpha() {
        return backgroundAlpha;
    }

    public StringProperty getBackgroundMode() {
        return backgroundMode;
    }

    public BooleanProperty getnSounds() {
        return nSounds;
    }

    public StringProperty getHudMode() {
        return hudMode;
    }

    public Color getColor() {
        float[] hsb = color.getHSB();
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }

    public boolean getChatStyle() {
        return false;
    }

    public boolean getScoreBoardStyle() {
        return false;
    }
}
