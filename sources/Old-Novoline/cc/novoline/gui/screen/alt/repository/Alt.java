package cc.novoline.gui.screen.alt.repository;

import cc.novoline.Novoline;

import static cc.novoline.Novoline.getLogger;

import cc.novoline.gui.screen.alt.login.AltLoginThread;
import cc.novoline.gui.screen.alt.login.AltType;
import cc.novoline.gui.screen.alt.login.SessionUpdatingAltLoginListener;
import cc.novoline.gui.screen.alt.repository.credential.AltCredential;
import cc.novoline.gui.screen.alt.repository.hypixel.HypixelProfile;
import cc.novoline.gui.screen.alt.repository.hypixel.HypixelProfileFactory;
import cc.novoline.utils.RenderUtils;
import cc.novoline.utils.Timer;

import static cc.novoline.utils.fonts.impl.Fonts.SFBOLD.SFBOLD_12.SFBOLD_12;
import static cc.novoline.utils.fonts.impl.Fonts.SFBOLD.SFBOLD_20.SFBOLD_20;
import static cc.novoline.utils.fonts.impl.Fonts.SFTHIN.SFTHIN_12.SFTHIN_12;

import cc.novoline.utils.java.Checks;
import cc.novoline.utils.minecraft.FakeEntityPlayer;
import cc.novoline.utils.notifications.NotificationType;
import cc.novoline.utils.web.JsonObtainer;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import static com.mojang.authlib.minecraft.MinecraftProfileTexture.Type.SKIN;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.lwjgl.opengl.GL11;

/**
 * @author xDelsy
 */
public class Alt {

    private static final GameProfile FAKE_GAME_PROFILE = new GameProfile(new UUID(0, 0), "Ловушка джокера");
    static final FakeEntityPlayer FAKE_ENTITY_PLAYER = new FakeEntityPlayer(FAKE_GAME_PROFILE, null);

    /* fields */
    private final AltRepositoryGUI repository;

    private final AltCredential credential;
    private FakeEntityPlayer player;
    private HypixelProfile hypixelProfile;
    private long unbanDate;

    private String rank;
    private double networkLevel;
    private boolean invalid;

    /* constructors */
    public Alt(@NotNull AltCredential credential,
               @NotNull FakeEntityPlayer player,
               @Nullable HypixelProfile hypixelProfile,
               @NotNull AltRepositoryGUI repository, Long unbanDate, String rank, double networkLevel, boolean invalid) {
        this.repository = repository;
        this.credential = credential;
        this.player = player;
        this.hypixelProfile = hypixelProfile;
        this.unbanDate = unbanDate;
        this.networkLevel = networkLevel;
        this.rank = rank;
        this.invalid = invalid;
    }

    /* methods */
    protected boolean mouseClicked(float width, float y, int mouseX, int mouseY) {
        if (!isHovered(width, y, mouseX, mouseY)) return false;

        if (Minecraft.getSystemTime() - lastClickTime < 250L) {
            logIn(true);
        } else {
            select();
        }

        this.lastClickTime = Minecraft.getSystemTime();
        return true;
    }

    public void drawAlt(float width, int y, int mouseX, int mouseY) {
        //region Rendering alt box
        RenderUtils.drawRoundedRect(AltRepositoryGUI.HORIZONTAL_MARGIN, y, width, AltRepositoryGUI.PLAYER_BOX_HEIGHT, 5,
                (!isSelected() ? DEFAULT_COLOR : SELECTED_COLOR));

        if (triedAuthorizing() && alpha > 0) {
            RenderUtils.drawRoundedRect(AltRepositoryGUI.HORIZONTAL_MARGIN, y, animationX,
                    AltRepositoryGUI.PLAYER_BOX_HEIGHT, 5, (int) Math.max(0, alpha) << 24 | (isLoginSuccessful() ?
                            SUCCESS_LOGIN_COLOR :
                            FAILED_LOGIN_COLOR));
            renderAltBox(width, mouseX, mouseY);
        }

        //endregion
        //region Rendering skull with name
        // if(this.player != null) { /* rendering skull */
        drawSkull(player, y);
        SFBOLD_20.drawString((invalid ? EnumChatFormatting.STRIKETHROUGH : "") + player.getName(), 53, y + 3, TEXT_SELECTED_COLOR);
		/*} else {
			drawSkull(FAKE_ENTITY_PLAYER, y);
			Fonts.SFBOLD_20.drawString("<Unknown Name>", 53, y + 3, TEXT_SELECTED_COLOR);
		}*/
        //endregion
        //region Rendering player-related info
        SFBOLD_12.drawString("Email: " + credential.getLogin(), 53, y + AltRepositoryGUI.HORIZONTAL_MARGIN,
                TEXT_SELECTED_COLOR);

        String password = credential.getPassword();

        if (StringUtils.isNotBlank(password)) {
            // рендер пароля двумя строками для смещения символа пароля на 2 пикселя вниз (больно уж он высоко иначе находится)
            SFBOLD_12.drawString("Password:", 53, y + 24, TEXT_SELECTED_COLOR);
            SFBOLD_12.drawString(new String(new char[password.length()]).replace('\0', '*'),
                    SFTHIN_12.stringWidth("Password:") + 57, y + 25, TEXT_SELECTED_COLOR);
        } /*else if(hypixelProfile != null) {
			FBOLD.kiona12.drawString("Level: " + hypixelProfile.getLevel(), 53, y + 24, TEXT_SELECTED_COLOR);
		}*/
        //endregion
        //region Выделение залогиненного аккаунта
        if (repository.getCurrentAlt() == this) {
            SFBOLD_20.drawString("Logged", width - 35, y + AltRepositoryGUI.PLAYER_BOX_HEIGHT / 2F - 5,
                    new Color(255, 255, 255, 50).getRGB());
        }

        if (unbanDate != -1) {
            if (unbanDate - System.currentTimeMillis() < 0) {
                unbanDate = 0;
            }
        }

        if (unbanDate != 0) {
            String unbansIn;
            if (unbanDate != -1) {
                int seconds = (int) ((unbanDate - System.currentTimeMillis()) / 1000);
                String days = seconds > 86400 ? (seconds / 86400) + "d " : "";
                seconds = !days.equals("") ? seconds % 86400 : seconds;
                String hours = seconds > 3600 ? seconds / 3600 + "h " : "";
                seconds = !hours.equals("") ? seconds % 3600 : seconds;
                String minutes = seconds > 60 ? seconds / 60 + "m " : "";
                unbansIn = days + hours + minutes;
            } else {
                unbansIn = "Permed";
            }

            SFBOLD_20.drawString(unbansIn, width - SFBOLD_20.stringWidth(unbansIn) - (repository.getCurrentAlt() == this ? 45 : 0) + 5, y + AltRepositoryGUI.PLAYER_BOX_HEIGHT / 2F - 5,
                    TEXT_SELECTED_COLOR);
        }

        //endregion
    }

    private void drawSkull(@NotNull FakeEntityPlayer player, int scrolled) {
        Minecraft mc = Minecraft.getInstance();

        mc.getTextureManager().bindTexture(player.getLocationSkin());
        Gui.drawScaledCustomSizeModalRect(18, scrolled + 2, 8.0F, 8.0F, 8, 8, 32, 32, 64.0F, 64.0F);
    }

    private final Timer timer = new Timer();
    private float alpha = 255;
    private float animationX = 0;

    private void renderAltBox(float width, int mouseX, int mouseY) {
        float altBoxAlphaStep = repository.getAltBoxAlphaStep();

        if (timer.delay(UPDATE_MILLIS_DELAY) && alpha > 0) {
            this.alpha -= altBoxAlphaStep;
            timer.reset();
        }

        if (animationX < width) {
            this.animationX = Math.min(animationX + repository.getAltBoxAnimationStep(), width);
        }
    }

    private long lastTimeAlreadyLogged;

    public CompletableFuture<Session> logIn(boolean trippsol) {
        CompletableFuture<Session> sessionCompletableFuture = CompletableFuture.supplyAsync(() -> {
            Session session = null;

            if (!isLoggingIn() && !isLoginSuccessful()) {
                setLoggingIn(true);

                session = new AltLoginThread(credential, new SessionUpdatingAltLoginListener() {

                    @Override
                    public void onLoginSuccess(AltType type, Session session) {
                        super.onLoginSuccess(type, session);

                        repository.getAlts().forEach(Alt::resetLogged);
                        repository.setCurrentAlt(Alt.this);
                        setGameProfile(session.getProfile());
                        setLoginProperty(true);
                        setInvalid(false);

                        if (hypixelProfile != null) {
                            Novoline.getInstance().getNotificationManager().pop("Logged in! " + Alt.this.toString(), NotificationType.INFO);
                        }

                        try {
                            JSONObject object = JsonObtainer.obtainJson("https://api.sk1er.club/player/" + session.getUsername());
                            if (!object.optBoolean("success")) {
                                if (object.optString("cause").equals("non-player")) {
                                    rank = "Never logged in";
                                } else {
                                    rank = "API DOWN";
                                }
                                networkLevel = 1;
                            } else {
                                JSONObject player = object.optJSONObject("player");
                                networkLevel = player.optDouble("networkLevel");
                                rank = player.optString("rank_for_mod");
                            }
                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onLoginFailed() {
                        setLoginProperty(false);
                        setInvalid(true);
                        Novoline.getInstance().getNotificationManager().pop("Invalid credentials!", NotificationType.ERROR);
                    }
                }).run();

                setLoggingIn(false);

                this.alpha = 255;
                this.animationX = 0;
            } else if (isLoggingIn()) {
                if (System.currentTimeMillis() > lastTimeAlreadyLogged + 150) {
                    Novoline.getInstance().getNotificationManager().pop("Already trying logging in!", NotificationType.ERROR);
                    this.lastTimeAlreadyLogged = System.currentTimeMillis();
                }
            } else if (isLoginSuccessful()) {
                if (System.currentTimeMillis() > lastTimeAlreadyLogged + 150) {
                    Novoline.getInstance().getNotificationManager().pop("Already logged in!", NotificationType.ERROR);
                    this.lastTimeAlreadyLogged = System.currentTimeMillis();
                }
            }

            return session;
        }, ForkJoinPool.commonPool());

        if (/*trippsol*/true) {
            return sessionCompletableFuture.whenCompleteAsync((session, throwable) -> {
                if (throwable != null) {
                    getLogger().warn("An error occurred while logging in!", throwable);
                    return;
                }

                if (isLoginSuccessful() && session != null && session.getProfile() != null && session.getProfile()
                        .getId() != null) {
                    boolean wasNull = hypixelProfile == null;

                    try {
                        Novoline.getInstance().getNotificationManager().pop("Logged in! " + toString(), NotificationType.SUCCESS);

                        repository.saveAlts();
                    } catch (Throwable t) {
                        getLogger().warn("An unexpected error occurred while loading Hypixel profile!", t);
                    }
                }
            });
        } else {
            return sessionCompletableFuture;
        }
    }

    //region State-related stuff
    private static final byte SELECTED_POSITION = 0;
    private static final byte AUTHORIZED_POSITION = 1;
    private static final byte LOGGED_POSITION = 2;
    private static final byte LOGGING_IN_POSITION = 3;

    /**
     * & 0b100: 1 — сейчас в этом аккаунте, 0 — пытался войти, но данные не подошли
     * & 0b010: 1 — пытался авторизоваться, 0 — не пытался авторизоваться
     * & 0b001: 1 — аккаунт выделен, 0 — аккаунт не выделен
     */
    private byte state = 0b000;

    private void modifyState(byte pos, boolean b) {
        byte mask = (byte) (1 << pos);

        if (!b) {
            this.state = (byte) (state & ~mask);
        } else {
            this.state = (byte) (state & ~mask | 1 << pos & mask);
        }
    }

    private boolean state(byte pos) {
        byte mask = (byte) (1 << pos);
        return (state & mask) == mask;
    }

    public void resetLogged() {
        modifyState(AUTHORIZED_POSITION, false);
        modifyState(LOGGED_POSITION, false);
    }

    private void setLoginProperty(boolean b) {
        modifyState(AUTHORIZED_POSITION, true);
        modifyState(LOGGED_POSITION, b);
    }

    public boolean isLoginSuccessful() {
        return triedAuthorizing() && state(LOGGED_POSITION);
    }

    public boolean isLoginUnsuccessful() {
        return triedAuthorizing() && !state(LOGGED_POSITION);
    }

    public boolean triedAuthorizing() {
        return state(AUTHORIZED_POSITION);
    }

    void setSelectedProperty(boolean b) {
        modifyState(SELECTED_POSITION, b);
    }

    public boolean isSelected() {
        return state(SELECTED_POSITION);
    }

    public boolean isLoggingIn() {
        return state(LOGGING_IN_POSITION);
    }

    private void setLoggingIn(boolean b) {
        modifyState(LOGGING_IN_POSITION, b);
    }

    public void setUnbanDate(long unbanDate) {
        this.unbanDate = unbanDate;
    }

    public long getUnbanDate() {
        return unbanDate;
    }

    public boolean isInvalid() {
        return invalid;
    }

    //endregion

    public void setGameProfile(@NotNull GameProfile gameProfile) {
        setupPlayer(gameProfile, null);

        Minecraft mc = Minecraft.getInstance();

        gameProfile.getProperties().clear();
        gameProfile.getProperties().putAll(mc.fillSessionProfileProperties());

        MinecraftProfileTexture profileTexture = mc.getSessionService().getTextures(gameProfile, false).get(SKIN);

        if (profileTexture != null) {
            mc.addScheduledTask(
                    () -> mc.getSkinManager().loadSkin(profileTexture, SKIN, (type, skinLocation, texture) -> {
                        setupPlayer(gameProfile, skinLocation);
                    }));
        }
    }

    void setupPlayer(@NotNull GameProfile gameProfile, @Nullable ResourceLocation skinLocation) {
        Minecraft mc = Minecraft.getInstance();
        this.player = new FakeEntityPlayer(gameProfile, skinLocation);

        mc.getRenderManager().cacheActiveRenderInfo(player.worldObj, mc.fontRendererObj, player, player,
                mc.gameSettings, 0.0F);
    }

    long lastClickTime;

    public void select() {
        Alt selected = repository.getAlts().stream().filter(Alt::isSelected).findAny().orElse(null);
        if (selected != null) selected.setSelectedProperty(false);

        setSelectedProperty(true);
        repository.selectAlt(selected, this, null);
    }

    private static final float MODEL_SCALE_FACTOR = 0.71F;
    private static final int MODEL_BOTTOM_MARGIN = 24;

    public void drawEntity(int mouseX, int mouseY) {
        if (player != null) {
            Minecraft mc = Minecraft.getInstance();

            int width = repository.width;
            int height = repository.height;

            final int distanceToSide = (int) (AltRepositoryGUI.HORIZONTAL_MARGIN + AltRepositoryGUI.PLAYER_BOX_WIDTH / 2F);
            float targetHeight = height / 3f * MODEL_SCALE_FACTOR;

            int posX = width - distanceToSide;
            int posY = AltRepositoryGUI.VERTICAL_MARGIN + height - AltRepositoryGUI.VERTICAL_MARGIN - 6 * 4 - 3 * AltRepositoryGUI.BUTTON_HEIGHT - MODEL_BOTTOM_MARGIN;
            int mouseX1 = width - distanceToSide - mouseX;
            float mouseY1 = height / 2F + player.height * targetHeight - player.height * targetHeight * (player
                    .getEyeHeight() / player.height) - mouseY;

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.enableColorMaterial();
            GlStateManager.pushMatrix();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GlStateManager.translate((float) posX, (float) posY, 50.0F);
            GL11.glScalef(-targetHeight, targetHeight, targetHeight);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
            RenderHelper.enableStandardItemLighting();
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);

            float tanX = (float) Math.atan(mouseX1 / 40.0F);
            float tanY = -((float) Math.atan(mouseY1 / 40.0F));

            GlStateManager.rotate(tanY * 20.0F, 1.0F, 0.0F, 0.0F);
            player.renderYawOffset = tanX * 20.0F;
            player.rotationYaw = tanX * 40.0F;
            player.rotationPitch = tanY * 20.0F;
            player.rotationYawHead = player.rotationYaw;
            player.prevRotationYawHead = player.rotationYaw;
            GlStateManager.translate(0.0F, 0.0F, 0.0F);

            try {
                RenderManager renderManager = mc.getRenderManager();
                renderManager.setPlayerViewY(180.0F);
                renderManager.setRenderShadow(false);
                renderManager.renderEntityWithPosYaw(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
                renderManager.setRenderShadow(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GlStateManager.popMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        }
    }

    static final float FHD_ANIMATION_STEP = 5;
    private static final int UPDATES_PER_SECOND = 100;

    private static final int UPDATE_MILLIS_DELAY = 1_000 / UPDATES_PER_SECOND;

    private static final int DEFAULT_COLOR = new Color(0, 0, 0,75).getRGB();
    private static final int SELECTED_COLOR = new Color(0, 0, 0,100).getRGB();
    private static final int TEXT_DEFAULT_COLOR = 0xFF868386;
    private static final int TEXT_SELECTED_COLOR = new Color(198, 198, 198).getRGB();
    private static final int SUCCESS_LOGIN_COLOR = 0x6E8D3D;
    private static final int FAILED_LOGIN_COLOR = 0x9E3939;

    private boolean isHovered(float width, float y, int mouseX, int mouseY) {
        return mouseX >= AltRepositoryGUI.HORIZONTAL_MARGIN && mouseX <= width + AltRepositoryGUI.HORIZONTAL_MARGIN && mouseY >= y && mouseY <= y + AltRepositoryGUI.PLAYER_BOX_HEIGHT;
    }

    @NotNull
    public static Alt fromNBT(AltRepositoryGUI gui, @NotNull NBTTagCompound tagCompound) {
        String login = Checks.notBlank(tagCompound.getString("login"), "login");
        String password = tagCompound.getString("password", null);
        HypixelProfile hypixelProfile = HypixelProfileFactory.fromNBT(tagCompound.getCompoundTag("hypixel", null));
        hypixelProfile = hypixelProfile != null ? hypixelProfile : HypixelProfile.empty();

        NBTTagCompound profileTag = tagCompound.getCompoundTag("profile", null);
        GameProfile profile = NBTUtil.readGameProfileFromNBT(profileTag);
        FakeEntityPlayer fakeEntityPlayer = new FakeEntityPlayer(Objects.requireNonNull(profile), null);
        Long unbanDate = Long.parseLong(tagCompound.getString("unbanDate", null));
        String rank = "NONE";
        double networkLevel = 1;
        if (tagCompound.hasKey("networkLevel")) {
            networkLevel = Double.parseDouble(tagCompound.getString("networkLevel", null));
        }
        if (tagCompound.hasKey("rank")) {
            rank = tagCompound.getString("rank", null);
        }
        boolean invalid = false;
        if(tagCompound.hasKey("invalid")){
            invalid = tagCompound.getBoolean("invalid");
        }

        AltCredential credential = new AltCredential(login, password);
        return new Alt(credential, fakeEntityPlayer, hypixelProfile, gui, unbanDate, rank, networkLevel,invalid);
    }

    public NBTBase asNBTCompound() {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setString("unbanDate", String.valueOf(unbanDate));
        compound.setString("rank", this.rank);
        compound.setString("networkLevel", String.valueOf(this.networkLevel));
        compound.setString("login", credential.getLogin());
        compound.setBoolean("invalid",invalid);
        if (credential.getPassword() != null) compound.setString("password", credential.getPassword());
        if (hypixelProfile != null) compound.setTag("hypixel", hypixelProfile.asNBTCompound());
        compound.setTag("profile", NBTUtil.writeGameProfile(new NBTTagCompound(), player.getGameProfile()));
        return compound;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Username: " + player.getGameProfile().getName());

        if (hypixelProfile != null) {
            String hypixelRank = hypixelProfile.getRank();
            int hypixelLevel = hypixelProfile.getLevel();

            if (hypixelLevel > 1) builder.append(" | ").append(hypixelLevel).append(" Lvl");
            if (hypixelRank != null && !hypixelRank.equalsIgnoreCase("default"))
                builder.append(" | ").append(hypixelRank);
        }

        return builder.toString();
    }

    //region Lombok-alternative
    public AltCredential getCredential() {
        return credential;
    }

    public double getNetworkLevel() {
        return networkLevel;
    }

    public String getRank() {
        return rank;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public FakeEntityPlayer getPlayer() {
        return player;
    }

    public HypixelProfile getHypixelProfile() {
        return hypixelProfile;
    }
    //endregion

}
