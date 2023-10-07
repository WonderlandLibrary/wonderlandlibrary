package cc.novoline.modules.visual;

import cc.novoline.Novoline;
import cc.novoline.events.EventTarget;
import cc.novoline.events.events.Render2DEvent;
import cc.novoline.events.events.Render3DEvent;
import cc.novoline.events.events.RenderNameTagEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.PlayerManager;
import cc.novoline.modules.combat.KillAura;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.IntProperty;
import cc.novoline.modules.configurations.property.object.ListProperty;
import cc.novoline.modules.configurations.property.object.StringProperty;
import cc.novoline.modules.player.Freecam;
import cc.novoline.utils.RenderUtils;
import cc.novoline.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static cc.novoline.gui.screen.setting.SettingType.COMBOBOX;
import static cc.novoline.modules.PlayerManager.EnumPlayerType.FRIEND;
import static cc.novoline.modules.PlayerManager.EnumPlayerType.TARGET;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.*;
import static cc.novoline.utils.PlayerUtils.inTeamWithMinecraftPlayer;
import static cc.novoline.utils.fonts.impl.Fonts.SFBOLD.SFBOLD_18.SFBOLD_18;
import static cc.novoline.utils.fonts.impl.Fonts.SFBOLD.SFBOLD_20.SFBOLD_20;
import static net.minecraft.util.EnumChatFormatting.*;

public final class Nametags extends AbstractModule {

    /* fields */
    private final List<Player> validEntities = new CopyOnWriteArrayList<>();

    /* properties @off */
    @Property("render-distance")
    private final IntProperty renderDistance = createInt(192).minimum(4).maximum(256);
    @Property("tag-font")
    private final StringProperty tagFont = createString("Client").acceptableValues("Client", "Vanilla");
    @Property("content")
    private final ListProperty<String> content = createList("Distance").acceptableValues("Distance", "Armor");
    @Property("additions")
    private final ListProperty<String> additions = createList("Background").acceptableValues("Background", "Health");
    @Property("health-type")
    private final StringProperty healthType = createString("Bar").acceptableValues("Bar", "Value");
    @Property("background-alpha")
    private final IntProperty backgroundAlpha = createInt(100).minimum(50).maximum(255);
    @Property("only-targets")
    private final BooleanProperty onlyTargets = booleanFalse();

    /* constructors @on */
    public Nametags(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Nametags", "Nametags", Keyboard.KEY_NONE, EnumModuleType.VISUALS);
        Manager.put(new Setting("NAMETAGS_FONT", "Nametags Font", COMBOBOX, this, tagFont));
        Manager.put(new Setting("NAMETAGS_RENDER_DIST", "Render distance", SettingType.SLIDER, this, this.renderDistance, 4));
        Manager.put(new Setting("NAMETAGS_CONTENT", "Content", SettingType.SELECTBOX, this, this.content));
        Manager.put(new Setting("NAMETAGS_ADDITIONS", "Additions", SettingType.SELECTBOX, this, this.additions));
        Manager.put(new Setting("NAMETAGS_BG_ALPHA", "Background Alpha", SettingType.SLIDER, this, this.backgroundAlpha, 5, () -> additions.get().contains("Background")));
        Manager.put(new Setting("NAMETAGS_HEALTH", "Health Type", SettingType.COMBOBOX, this, this.healthType, () -> additions.get().contains("Health")));
        Manager.put(new Setting("ONLY_TAR", "Only targets", SettingType.CHECKBOX, this, this.onlyTargets));
    }

    /* methods */
    @Override
    public void onDisable() {
        this.validEntities.clear();
    }

    private @Nullable Player getPlayerByEntity(EntityLivingBase entity) {
        return this.validEntities.stream().filter(player -> player.entity.equals(entity)).findFirst().orElse(null);
    }

    /* events */
    @EventTarget
    public void onNameTagRender(RenderNameTagEvent event) {
        if (event.getEntity() == getModule(Freecam.class).getFreecamEntity()) {
            return;
        }

        event.setCancelled(this.novoline.getPlayerManager().getType(event.getEntity().getName()) == TARGET  //
                || this.mc.player.getDistanceToEntity(event.getEntity()) <= this.renderDistance.get() && !this.onlyTargets.get() //
                && !(event.getEntity() instanceof EntityArmorStand) || getModule(PlayerESP.class).isEnabled() && getModule(PlayerESP.class).getFilter().get().contains("Name"));
    }

    @EventTarget
    public void onRender2D(Render2DEvent render2DEvent) {
        this.validEntities.forEach(Player::render);
    }

    @EventTarget
    private void onRender(Render3DEvent event) {
        this.mc.world.getLoadedEntityList().stream() //
                .filter(EntityPlayer.class::isInstance) //
                .filter(entity -> !entity.isInvisible()) //
                .filter(Entity::isEntityAlive) //
                .map(EntityLivingBase.class::cast) //
                .filter(entity -> !this.validEntities.contains(getPlayerByEntity(entity))) //
                .forEach(entity -> this.validEntities.add(new Player(entity))); //

        this.validEntities.forEach(player -> {
            if (!player.entity.isEntityAlive() || this.mc.player.getDistanceToEntity(player.entity) > this.renderDistance.get() && Novoline.getInstance().getPlayerManager().getType(player.entity.getName()) != TARGET) {
                this.validEntities.remove(player);
            }

            if (!this.mc.world.getLoadedEntityList().contains(player.entity) || this.onlyTargets.get() && Novoline.getInstance().getPlayerManager().getType(player.entity.getName()) != TARGET || player.entity.getEntityID() == this.mc.player.getEntityID() || player.entity.getDisplayName().getFormattedText().contains("NPC") || player.entity.getDisplayName().getUnformattedText().equalsIgnoreCase(player.entity.getName()))
                this.validEntities.remove(player);

            final float x = (float) (player.entity.lastTickPosX + (player.entity.posX - player.entity.lastTickPosX) * event.getPartialTicks() - this.mc.getRenderManager().renderPosX), //
                    y = (float) (player.entity.lastTickPosY + 2.3 + (player.entity.posY + 2.3 - (player.entity.lastTickPosY + 2.3)) * event.getPartialTicks() - this.mc.getRenderManager().renderPosY), //
                    z = (float) (player.entity.lastTickPosZ + (player.entity.posZ - player.entity.lastTickPosZ) * event.getPartialTicks() - this.mc.getRenderManager().renderPosZ);
            player.positions = player.convertTo2D(x, y, z);
        });
    }

    private Timer tpTimer = new Timer();

    private class Player {

        private final EntityLivingBase entity;
        private double[] positions = {0, 0, 0};

        public Player(EntityLivingBase entity) {
            this.entity = entity;
        }

        void render() {
            GL11.glPushMatrix();
            final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getInstance());
            final PlayerManager.EnumPlayerType type = Novoline.getInstance().getPlayerManager().getType(this.entity.getName());
            final float x = (float) (this.positions[0] / scaledResolution.getScaleFactor()), //
                    y = (float) (this.positions[1] / scaledResolution.getScaleFactor()), //
                    z = (float) (this.positions[2] / scaledResolution.getScaleFactor());

            final List<String> contentA = getModule(Nametags.class).getContent().get(), //
                    additions = getModule(Nametags.class).getAdditions().get();
            final String health = additions.contains("Health") ? getModule(Nametags.class).getHealthType().equalsIgnoreCase("Value") ? " " + (int) (this.entity.getHealth() + this.entity.getAbsorptionAmount()) : "" : "";//
            final String distance = contentA.contains("Distance") ? " " + (int) Minecraft.getInstance().player.getDistanceToEntity(this.entity) + "m" : "";
            String formattedName = this.entity.getDisplayName().getFormattedText();
//            UserEntity user = novoline.getIRC().getUserManager().findByNickname(this.entity.getName());
//
//            if (!this.entity.getName().isEmpty() && user != null) {
//                formattedName += " \u00A77(\u00A7b" + user.getUsername() + "\u00A77)\u00A7r";
//            }

            GL11.glTranslatef(x, y, z);

            float amp = 1;
            switch (Minecraft.getInstance().gameSettings.guiScale) {
                case 0:
                    amp = 0.5F;
                    break;
                case 1:
                    amp = 2.0F;
                    break;
                case 3:
                    amp = 0.6666666666666667F;
            }


            if (this.positions[2] < 0.0 || this.positions[2] >= 1.0) {
                GlStateManager.popMatrix();
                return;
            }

            ScaledResolution res = new ScaledResolution(mc);
            double scale2 = res.getScaleFactor() / Math.pow(res.getScaleFactor(), 2.0);
            GL11.glScaled(scale2, scale2, scale2);

            GlStateManager.disableDepth();
            String content = (type == FRIEND ? AQUA + "[FRIEND] " : getModule(KillAura.class).isEnabled() && getModule(KillAura.class).getFilters().contains("Teams") && inTeamWithMinecraftPlayer(this.entity) ? GREEN + "[TEAM] " : type == TARGET ? RED + "[TARGET] " : "") + RESET + formattedName + GRAY + distance;
            final float rectLength = Math.abs(-(getStringWidth(content) / 2) - 3 - (getStringWidth(content) / 2 + 4)), maxHealth = (int) (this.entity.getMaxHealth() + this.entity.getAbsorptionAmount()), amplifier = 100 / maxHealth, percent = (int) ((this.entity.getHealth() + this.entity.getAbsorptionAmount()) * amplifier), space = rectLength / 100; // @on
            int n = additions.contains("Health") && getModule(Nametags.class).getHealthType().equalsIgnoreCase("Value") ? 5 : 0;
            final float contentWidth = getStringWidth(content) / 2F;

            final ScaledResolution resolution = new ScaledResolution(Minecraft.getInstance());

            float width = resolution.getScaledWidth() / 2F;
            float height = resolution.getScaledHeight() / 2F;

            float sizePerPixelX = SFBOLD_20.stringWidth(content) / 2F * 0.5F;
            float sizePerPixelY = 15 * 0.45F;

            float xBnd1 = width / amp + sizePerPixelX;
            float xBnd2 = width / amp - sizePerPixelX;
            float yBnd1 = height / amp - sizePerPixelY;
            float yBnd2 = height / amp + sizePerPixelY;

/*            if (mc.gameSettings.thirdPersonView == 0 && this.positions[0] >= xBnd2 * 2 && this.positions[0] <= xBnd1 * 2 && this.positions[1] >= yBnd1 * 2 && this.positions[1] <= yBnd2 * 2) {
                SFBOLD_20.drawString("Middle click to teleport!", -(SFBOLD_20.stringWidth("Middle click to teleport") / 2F), -getYOffset() - 18, 0xffffffff, true);

                if (Mouse.isButtonDown(2)) {
                    if (tpTimer.delay(1000)) {
                        String command = mc.isSingleplayer() ? "/tp" : ".tp";
                        mc.player.sendChatMessage(command + " " + entity.getName());
                        tpTimer.reset();
                    }
                }
            }*/

            if (additions.contains("Background")) {
                RenderUtils.drawRect(-contentWidth - 2 - n, -8.0F - getYOffset(), getStringWidth(content + health) / 2F + //
                                (getModule(Nametags.class).getHealthType().equalsIgnoreCase("Bar") ? 2 : getStringWidth(health) - 3 - n), //
                        (additions.contains("Health") ? getModule(Nametags.class).getHealthType().equalsIgnoreCase("Bar") ? 6 : 5 : 5) - getYOffset(), //
                        new Color(type == TARGET ? 100 : 0, type == FRIEND || getModule(KillAura.class).isEnabled() //
                                && getModule(KillAura.class).getFilters().contains("Teams") && inTeamWithMinecraftPlayer(this.entity) ? 90 : 0, type == FRIEND ? 120 : //
                                inTeamWithMinecraftPlayer(this.entity) ? 15 : 0, getModule(Nametags.class).getBackgroundAlpha().get()).getRGB());
            }

            if (contentA.contains("Armor")) {
                renderArmor((EntityPlayer) this.entity);
            }

            drawString(content, -contentWidth - n, -getYOffset() - 5, 16777215);
            drawString(health, contentWidth - n, -getYOffset() - 5, getHealthColor());

            if (additions.contains("Health") && getModule(Nametags.class).getHealthType().equalsIgnoreCase("Bar")) {
                Gui.drawRect(-contentWidth - 2, 5 - getYOffset(), -contentWidth - 5 + percent * space, 6 - getYOffset(), getHealthColor());
            }

            GlStateManager.enableDepth();
            GL11.glPopMatrix();
        }

        private void drawString(String string, float x, float y, int color) {
            if (tagFont.get().equalsIgnoreCase("Client")) {
                SFBOLD_20.drawString(string, x, y, color);
            } else {
                mc.fontRendererObj.drawString(string, x, y, color);
            }
        }

        private float getStringWidth(String string) {
            if (tagFont.get().equalsIgnoreCase("Client")) {
                return SFBOLD_20.stringWidth(string);
            } else {
                return mc.fontRendererObj.getStringWidth(string);
            }
        }

        private float getYOffset() {
            final float distanceToEntity = Minecraft.getInstance().player.getDistanceToEntity(this.entity);

            if (getModule(Nametags.class).getHealthType().equalsIgnoreCase("Bar")) {
                return (float) Math.max(getDistance() * (distanceToEntity >= 110 ? 0.058 : 0.032 + 4 / distanceToEntity), 1);
            } else {
                return (float) Math.max(getDistance() * (distanceToEntity >= 110 ? 0.046 : 0.02 + 4 / distanceToEntity), 1);
            }
        }

        private int getHealthColor() {
            final float f = this.entity.getHealth(), // @off
                    f1 = this.entity.getMaxHealth(),
                    f2 = Math.max(0.0F, Math.min(f, f1) / f1); // @on
            return Color.HSBtoRGB(f2 / 3.0F, 1, 1) | 0xFF000000;
        }

        private int getDistance() {
            final int diffX = (int) Math.abs(Minecraft.getInstance().player.posX - this.entity.posX), // @off
                    diffY = (int) Math.abs(Minecraft.getInstance().player.posY - this.entity.posY),
                    diffZ = (int) Math.abs(Minecraft.getInstance().player.posZ - this.entity.posZ); // @on
            return (int) Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
        }

        private double[] convertTo2D(double x, double y, double z) {
            final FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
            final IntBuffer viewport = BufferUtils.createIntBuffer(16);
            final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
            final FloatBuffer projection = BufferUtils.createFloatBuffer(16);

            GL11.glGetFloat(2982, modelView);
            GL11.glGetFloat(2983, projection);
            GL11.glGetInteger(2978, viewport);

            final boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);
            return result ? new double[]{(double) screenCoords.get(0), (double) ((float) Display.getHeight() - screenCoords.get(1)), (double) screenCoords.get(2)} : null;
        }

        private void renderArmor(EntityPlayer player) {
            ItemStack[] renderStack = player.inventory.armorInventory;
            ItemStack armourStack;
            int xOffset = 0;

            for (ItemStack aRenderStack : renderStack) {
                armourStack = aRenderStack;

                if (armourStack != null) xOffset -= 8;
            }

            if (player.getCurrentEquippedItem() != null) {
                xOffset -= 8;

                final ItemStack stock = player.getCurrentEquippedItem().copy();

                if (stock.hasEffect() && (stock.getItem() instanceof ItemTool || stock.getItem() instanceof ItemArmor))
                    stock.stackSize = 1;

                renderItemStack(stock, xOffset, -25 - getYOffset() * 1.5f);
                xOffset += 16;
            }

            renderStack = player.inventory.armorInventory;

            for (int index = 3; index >= 0; index--) {
                armourStack = renderStack[index];

                if (armourStack != null) {
                    renderItemStack(armourStack, xOffset, -25 - getYOffset() * 1.5f);
                    xOffset += 16;
                }
            }

            GlStateManager.color(1, 1, 1, 1);
        }

        private void renderItemStack(final ItemStack stack, int x, float y) {
            GlStateManager.pushMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.clear(256);
            RenderHelper.enableStandardItemLighting();

            Minecraft.getInstance().getRenderItem().zLevel = -150.0F;

            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(0.7, 0.7, 0.7);
            Minecraft.getInstance().getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
            Minecraft.getInstance().getRenderItem().renderItemOverlaysCR(SFBOLD_18, stack, x, (int) y);
            Minecraft.getInstance().getRenderItem().zLevel = 0.0f;

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableCull();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            final float s = 0.5F;
            GlStateManager.scale(s, s, s);
            GlStateManager.disableDepth();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            GlStateManager.popMatrix();
        }

    }

    //region Lombok
    public List<Player> getValidEntities() {
        return this.validEntities;
    }

    public IntProperty getRenderDistance() {
        return this.renderDistance;
    }

    public ListProperty<String> getContent() {
        return this.content;
    }

    public ListProperty<String> getAdditions() {
        return this.additions;
    }

    public StringProperty getHealthType() {
        return this.healthType;
    }

    public IntProperty getBackgroundAlpha() {
        return this.backgroundAlpha;
    }

    public BooleanProperty getOnlyTargets() {
        return this.onlyTargets;
    }
    //endregion

}