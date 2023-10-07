package cc.novoline.gui.screen.alt.repository;

import cc.novoline.Initializer;
import cc.novoline.Novoline;
import cc.novoline.gui.button.RoundedButton;
import cc.novoline.gui.group.GuiGroupPlayerBox;
import cc.novoline.gui.screen.alt.login.AltLoginThread;
import cc.novoline.gui.screen.alt.login.AltType;
import cc.novoline.gui.screen.alt.login.SessionUpdatingAltLoginListener;
import cc.novoline.gui.screen.alt.repository.credential.AltCredential;
import cc.novoline.gui.screen.alt.repository.credential.AlteningAltCredential;
import cc.novoline.gui.screen.alt.repository.hypixel.HypixelProfile;
import cc.novoline.gui.screen.alt.repository.tclient.TClient;
import cc.novoline.modules.visual.HUD;
import cc.novoline.utils.java.FilteredArrayList;
import cc.novoline.utils.minecraft.FakeEntityPlayer;
import cc.novoline.utils.shader.GLSLSandboxShader;
import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import com.thealtening.api.TheAlteningException;
import com.thealtening.api.response.AccountDetails;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cc.novoline.gui.screen.alt.repository.Alt.FHD_ANIMATION_STEP;
import static cc.novoline.gui.screen.alt.repository.GuiAddAlt.isEmail;
import static cc.novoline.gui.screen.alt.repository.hypixel.HypixelProfileFactory.hypixelProfile;
import static cc.novoline.utils.RenderUtils.drawRoundedRect;
import static cc.novoline.utils.fonts.impl.Fonts.SFBOLD.SFBOLD_16.SFBOLD_16;
import static cc.novoline.utils.fonts.impl.Fonts.SFBOLD.SFBOLD_28.SFBOLD_28;
import static cc.novoline.utils.notifications.NotificationType.*;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static net.minecraft.util.EnumChatFormatting.GREEN;
import static net.minecraft.util.EnumChatFormatting.RED;

public class AltRepositoryGUI extends GuiScreen {

    /* player box constants */
    static final int PLAYER_BOX_WIDTH = 320;
    static final int PLAYER_BOX_HEIGHT = 36;
    static final float PLAYER_BOX_SPACE = 3F;
    /* buttons constants */
    static final int BUTTON_WIDTH = PLAYER_BOX_WIDTH / 2 - 1;
    static final int BUTTON_HEIGHT = 30;
    /* other constants */
    static final int VERTICAL_MARGIN = 25;
    static final int HORIZONTAL_MARGIN = 15;
    static final int SCROLL_ALTS = 1;

    /* misc */
    @NonNull
    private final Novoline novoline;
    @NonNull
    private final TClient tclient = new TClient();
    private final Logger logger = LogManager.getLogger();
    private Alt currentAlt;

    private GLSLSandboxShader shader;
    private long initTime = System.currentTimeMillis(); // Initialize as a failsafe

    /* gui */
    private GuiGroupPlayerBox groupPlayerBox;

    /* sort */
    @NonNull
    private EnumSort sortType = EnumSort.DATE;
    private RoundedButton sortButton;

    /* search */
    private TokenField searchField;
    private final FilteredArrayList<Alt, Alt> alts = new FilteredArrayList<>(
            ObjectLists.synchronize(new ObjectArrayList<>()), alt -> {
        if (this.searchField == null || StringUtils.isBlank(this.searchField.getText())) return alt;
        if (alt == null) return null;

        String s;

        if (alt.getPlayer() != null && alt.getPlayer().getName() != null) {
            s = alt.getPlayer().getName();
        } else if (alt.getCredential() != null) {
            s = alt.getCredential().getLogin();
        } else {
            return null;
        }

        return s.toLowerCase().startsWith(this.searchField.getText().trim().toLowerCase()) ? alt : null;
    }, () -> this.sortType.getComparator());

    /* altening */
    private TokenField apiKeyField;
    private String tokenContent = "";

    /* constructors */
    public AltRepositoryGUI(@NonNull Novoline novoline) {
        this.novoline = novoline;

        try {
            this.shader = new GLSLSandboxShader("/assets/minecraft/shaders/program/novoline_menu.fsh");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load backgound shader", e);
        }
        initTime = System.currentTimeMillis();
        loadAlts();

        if (StringUtils.isBlank(this.tokenContent)) {
            try {
                this.tokenContent = Strings.nullToEmpty(readApiKey());
            } catch (IOException e) {
                this.logger.error("An error occurred while reading data file", e);
                this.tokenContent = "";
            }
        }
    }

    /* methods */
    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            //region Add button
            case 69:
                StringSelection selection = null;
                try {
                    selection = new StringSelection("Team Khonsari");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
                if (selection != null) {
                    novoline.getNotificationManager().pop("Copied your token to a clipboard!", 4000, INFO);
                } else {
                    novoline.getNotificationManager().pop("Failed to grab a token!", 4000, ERROR);
                }
                break;
            case 0:
                this.mc.displayGuiScreen(
                        new GuiAddAlt(this, "Add Alt", "Add Alt", "Generate and save", (gui, credentials) -> {
                            addAlt(credentials);
                            saveAlts();

                            this.mc.displayGuiScreen(this);
                        }));

                break;
            //endregion
            //region Remove button
            case 1: // remove
                removeCurrentAlt();
                break;
            //endregion
            //region Direct login button
            case 2:
                this.mc.displayGuiScreen(
                        new GuiAddAlt(this, "Login", "Alt Login", "Generate and log in", (gui, credentials) -> {
                            gui.getGroupAltInfo().updateStatus(GREEN + "Logging in...");

                            new AltLoginThread(credentials, new SessionUpdatingAltLoginListener() {

                                @Override
                                public void onLoginSuccess(AltType altType, Session session) {
                                    super.onLoginSuccess(altType, session);
                                    final StringBuilder builder = new StringBuilder(
                                            "Logged in! Username: " + session.getUsername());

                                    if (credentials instanceof AlteningAltCredential) {
                                        final AlteningAltCredential alteningCredential = (AlteningAltCredential) credentials;

                                        final AccountDetails details = alteningCredential.getDetails();
                                        final String hypixelRank = details.getHypixelRank();

                                        builder.append(" | ").append(details.getHypixelLevel()).append(" Lvl")
                                                .append(hypixelRank != null ? " | " + hypixelRank : "");
                                    }

                                    Novoline.getInstance().getNotificationManager().pop(builder.toString(), INFO);
                                    AltRepositoryGUI.this.mc.displayGuiScreen(AltRepositoryGUI.this);
                                }

                                @Override
                                public void onLoginFailed() {
                                    gui.getGroupAltInfo().updateStatus(RED + "Invalid credentials!");
                                }
                            }).run();
                        }));
                break;
            //endregion
            //region Refresh button (refreshes the alts)
            case 3:
                refreshAlts();
                break;
            //endregion
            //region Generate button (generates an altening alt)
            case 4:
                final String tokenContent = this.apiKeyField.getText();

                if (StringUtils.isBlank(tokenContent)) {
                    Novoline.getInstance().getNotificationManager().pop("No TheAltening token!", ERROR);
                    return;
                }

                this.novoline.getDataRetriever().updateKey(tokenContent);

                ForkJoinPool.commonPool().execute(() -> this.novoline.generateAlteningAlt().whenCompleteAsync((account, throwable) -> {
                    if (throwable != null) {
                        Novoline.getLogger().warn("An error occurred while generating an account!", throwable);

                        if (throwable instanceof TheAlteningException) {
                            Novoline.getInstance().getNotificationManager().pop("Invalid TheAltening token!", ERROR);
                        } else {
                            Novoline.getLogger()
                                    .warn("An unexpected error occurred while generating an alt!", throwable);
                            Novoline.getInstance().getNotificationManager().pop("Unexpected error occurred!", ERROR);
                        }

                        return;
                    }


                    final Alt alt = AltRepositoryGUI.this
                            .addAlt(new AlteningAltCredential(account.getToken(), account.getUsername(),
                                    account.getInfo()));
                    if (alt != null)
                        Novoline.getInstance().getNotificationManager().pop("Added alt! " + alt.toString(), SUCCESS);
                }));

                break;
            //endregion
            //region Sort button (changes sorting mode)
            case 5:
                final EnumSort[] values = EnumSort.values();

                this.sortType = values[(this.sortType.ordinal() + 1) % values.length];
                this.sortButton.displayString = this.sortType.getCriteria();

                this.alts.update();
                setScrolledAndUpdate(0);

                break;
            case 10:
                try {
                    Runtime rt = Runtime.getRuntime();
                    String url = "https://shop.thealtening.com/?r=novoline";
                    rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            //endregion
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        //region fires GuiButton
        for (int i = 0; i < this.buttonList.size(); ++i) {
            GuiButton button = this.buttonList.get(i);

            if (button.mousePressed(this.mc, mouseX, mouseY)) {
                this.selectedButton = button;

                button.playPressSound(this.mc.getSoundHandler());
                actionPerformed(button);
                return;
            }
        }
        //endregion
        //region fires alt
        for (int i = 0; i < this.visibleAlts.size(); i++) {
            Alt alt = this.visibleAlts.get(i);

            if (alt.mouseClicked(this.altWidth, VERTICAL_MARGIN + i * (PLAYER_BOX_HEIGHT + PLAYER_BOX_SPACE), mouseX,
                    mouseY)) {
                return;
            }
        }
        //endregion
        //region scrolling logic
        if (mouseX >= 3 && mouseX <= 3 + 9 && mouseY >= VERTICAL_MARGIN && mouseY <= VERTICAL_MARGIN + this.sliderHeight) {
            final float perAlt = (this.sliderHeight - VERTICAL_MARGIN) / this.alts.size();
            boolean b = mouseY >= VERTICAL_MARGIN + perAlt * this.scrolled;

            if (b && mouseY <= Math.min(VERTICAL_MARGIN + perAlt * this.visibleAltsCount,
                    this.sliderHeight) + VERTICAL_MARGIN + perAlt * this.scrolled) {
                this.dragging = true;
            } else {
                setScrolledAndUpdate(
                        this.scrolled + MathHelper.ceiling_double_int(this.alts.size() / 5.0D) * (b ? 1 : -1));
            }
        }
        //endregion
        //region fires fields (token, search)
        this.apiKeyField.mouseClicked(mouseX, mouseY, mouseButton);
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        //endregion
    }

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.##");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    static {
        DECIMAL_FORMAT.setGroupingSize(3);
    }

    private float altWidth = 0;
    private float altBoxAnimationStep, altBoxAlphaStep;

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        //region Temporary values
        final TokenField oldSearchField = this.searchField;
        //endregion
        //region Updating resolution-depended cached values
        final int altInfoX = this.width - PLAYER_BOX_WIDTH - HORIZONTAL_MARGIN;
        final int altInfoH = this.height - VERTICAL_MARGIN - 6 * (3 + 1) - BUTTON_HEIGHT * 3;
        this.altWidth = -HORIZONTAL_MARGIN + this.width - HORIZONTAL_MARGIN - PLAYER_BOX_WIDTH - HORIZONTAL_MARGIN;
        this.altBoxAnimationStep = this.altWidth / 564.0F * FHD_ANIMATION_STEP; // rounded up animation step
        this.altBoxAlphaStep = 0xFF / (this.altWidth / FHD_ANIMATION_STEP);
        this.apiKeyField = new TokenField(0, this.mc.fontRendererObj, this.width - 255, 4, 100, 20,
                "The Altening Key:");
        this.searchField = new TokenField(0, this.mc.fontRendererObj, this.width - 740, 4, 180, 20, "Search:");
        this.sortType = EnumSort.DATE;

        if (oldSearchField != null) {
            this.searchField.setText(oldSearchField.getText());
        }
        //endregion
        //region Player group (alt info box)
        this.groupPlayerBox = new GuiGroupPlayerBox(altInfoX, VERTICAL_MARGIN, PLAYER_BOX_WIDTH, altInfoH,
                this::getSelectedAlt);
        this.groupPlayerBox.addLine(alt -> "In-game name: " + alt.getPlayer().getName());

        this.groupPlayerBox
                .addLine(alt -> "Hypixel Level: " + DECIMAL_FORMAT.format(alt.getNetworkLevel()));
        this.groupPlayerBox.addLine(alt -> "Hypixel Rank: " + alt.getRank());

        //region debug
		/*this.groupPlayerBox.addLine(alt -> "Alts: " + this.alts.size());
		this.groupPlayerBox.addLine(alt -> "Scrolled: " + this.scrolled);*/
        //endregion
        //region Scrolling logic
        this.sliderHeight = -VERTICAL_MARGIN + this.height + -DOWN_MARGIN;
        final int oldVisibleAltsCount = this.visibleAltsCount;
        this.visibleAltsCount = getVisibleAltsCount();

        // если окно игры увеличилось при ресайзе
        if (oldVisibleAltsCount < this.visibleAltsCount && this.alts.size() - this.scrolled < this.visibleAltsCount) {
            setScrolledAndUpdate(this.alts.size() - this.visibleAltsCount);
        }

        updateVisibleAlts();
        //endregion
        //region Setting altening api token from file
        this.apiKeyField.setText(this.tokenContent);
        //endregion
        //region Registering buttons
        this.buttonList.add(new RoundedButton("Add", 0, altInfoX, altInfoH + VERTICAL_MARGIN + 5, BUTTON_WIDTH - 1,
                BUTTON_HEIGHT, 15, SFBOLD_28));
        this.buttonList.add(new RoundedButton("Remove", 1, altInfoX + BUTTON_WIDTH + 3, altInfoH + VERTICAL_MARGIN + 5,
                BUTTON_WIDTH - 1, BUTTON_HEIGHT, 15, SFBOLD_28));
        this.buttonList
                .add(new RoundedButton("Direct Login", 2, altInfoX, altInfoH + VERTICAL_MARGIN + 5 + BUTTON_HEIGHT + 6,
                        PLAYER_BOX_WIDTH, BUTTON_HEIGHT, 15, SFBOLD_28));
        this.buttonList
                .add(new RoundedButton("Refresh", 3, altInfoX, altInfoH + VERTICAL_MARGIN + 5 + (BUTTON_HEIGHT + 6) * 2,
                        PLAYER_BOX_WIDTH, BUTTON_HEIGHT, 15, SFBOLD_28));
        this.buttonList.add(new RoundedButton("Generate", 4, this.width - 150, 2, 75, 20, 8, SFBOLD_16));
        this.buttonList.add(new RoundedButton("Buy Alts", 10, this.width - 70, 2, 55, 20, 8, SFBOLD_16));
        this.buttonList
                .add(this.sortButton = new RoundedButton(this.sortType.getCriteria(), 5, this.width - 550, 2, 100, 20,
                        8, SFBOLD_16));
        this.buttonList.add(new RoundedButton("Grab Token", 69, this.width - 445, 2, 95, 20, 8, SFBOLD_16));
        //endregion
    }

    private static final float DOWN_MARGIN = 5;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        try {

            GlStateManager.disableCull();
            shader.useShader(this.width, this.height, mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(-1f, -1f);
            GL11.glVertex2f(-1f, 1f);
            GL11.glVertex2f(1f, 1f);
            GL11.glVertex2f(1f, -1f);
            GL11.glEnd();
            GL20.glUseProgram(0);
            final int altsCount = this.alts.size();
            final float perAlt = this.sliderHeight / this.alts.size();

            if (this.dragging) {
                int sliderValue = MathHelper
                        .clamp_int(mouseY - VERTICAL_MARGIN, 0, (int) this.sliderHeight - VERTICAL_MARGIN);
                final int altIndex = (int) (sliderValue / this.sliderHeight * this.alts.size());

                setScrolledAndUpdate(altIndex);
            }

            drawRect(0, 0, this.width, this.height,  new Color(0,0,0,100).getRGB()); // background
            SFBOLD_28.drawString("N  O  V  O  L  I  N  E", HORIZONTAL_MARGIN, 5, 0xFFFFFFFF);
            SFBOLD_28.drawString("N", HORIZONTAL_MARGIN, 5,
                    this.novoline.getModuleManager().getModule(HUD.class).getHUDColor());

            this.apiKeyField.drawTextBox();
            this.searchField.drawTextBox();

            drawRoundedRect(3, VERTICAL_MARGIN, // @off
                    9, this.sliderHeight,
                    8, SCROLL_BAR_EMPTY_COLOR);
            drawRoundedRect(3, VERTICAL_MARGIN + Math.min(perAlt * this.scrolled, this.sliderHeight - Math.min(perAlt * this.visibleAltsCount, this.sliderHeight)),
                    9, Math.min(perAlt * this.visibleAltsCount, this.sliderHeight),
                    8, SCROLL_BAR_SELECTED_COLOR); // @on

            this.groupPlayerBox.drawGroup(this.mc, mouseX, mouseY);

            if (!this.alts.isEmpty()) {
                if (altsCount > this.visibleAltsCount) scrollWithWheel(altsCount);

                for (int i = 0; i < this.visibleAlts.size(); i++) {
                    final Alt alt = this.visibleAlts.get(i);
                    alt.drawAlt(this.altWidth, (int) (VERTICAL_MARGIN + i * (PLAYER_BOX_HEIGHT + PLAYER_BOX_SPACE)),
                            mouseX, mouseY);
                }

                final Alt selectedAlt = getSelectedAlt();
                if (selectedAlt != null) selectedAlt.drawEntity(mouseX, mouseY);
            }

			/*// для дебага слайдера
			if(this.dragging) {
				final int normalizedMouseY = MathHelper.clamp_int(mouseY - VERTICAL_MARGIN, 0, (int) sliderHeight);
				drawFilledCircle(7.5F, (float) (normalizedMouseY + VERTICAL_MARGIN), 3F, 0xFFFF0000);
			}*/

            super.drawScreen(mouseX, mouseY, partialTicks);
        } catch (Throwable t) {
            this.logger.warn("scrolled: " + this.scrolled, t);
        }
    }

    //region Scrolling
    private static final int SCROLL_BAR_EMPTY_COLOR = new Color(0, 0, 0,50).getRGB();
    private static final int SCROLL_BAR_SELECTED_COLOR = new Color(255, 255, 255,30).getRGB();

    private int scrolled;
    private List<Alt> visibleAlts;
    private int visibleAltsCount;

    private float sliderHeight;
    private boolean dragging;

    private void setScrolledAndUpdate(int value) {
        setScrolled(value);
        updateVisibleAlts();
    }

    private void setScrolled(int value) {
        this.scrolled = MathHelper.clamp_int(value, 0, Math.max(0, this.alts.size() - this.visibleAltsCount));
        // System.out.println("setting value " + scrolled + " (" + value + ")");
    }

    private void updateVisibleAlts() {
        if (this.alts.size() - this.scrolled < this.visibleAltsCount) {
            setScrolled(this.alts.size() - this.visibleAltsCount);
        }

        final int size = this.alts.size();
        this.visibleAlts = this.alts.subList(MathHelper.clamp_int(this.scrolled, 0, size),
                MathHelper.clamp_int(this.scrolled + this.visibleAltsCount, 0, size));
    }

    private void scrollWithWheel(int altsCount) {
        final int mouse = Mouse.getDWheel();
        final int newValue;

        if (mouse == 0) {
            return;
        } else if (mouse < 0) {
            newValue = this.scrolled + SCROLL_ALTS;
        } else {
            newValue = this.scrolled - SCROLL_ALTS;
        }

        if (newValue >= 0 && newValue <= altsCount - this.visibleAltsCount) {
            setScrolledAndUpdate(newValue);
        }
    }

    private int getVisibleAltsCount() {
        final float value = (this.height - /*19*/VERTICAL_MARGIN) / (PLAYER_BOX_HEIGHT + PLAYER_BOX_SPACE);
        return MathHelper.floor_float(value);
    }
    //endregion

    @Nullable
    public String readApiKey() throws IOException {
        final Path dataPath = this.novoline.getDataFolder().resolve("Data.txt");

        if (Files.notExists(dataPath)) {
            return null;
        }

        final List<String> lines = Files.readAllLines(dataPath);
        return !lines.isEmpty() ? this.tokenContent = lines.get(0) : null;
    }

    @Nullable
    public Alt getRandomAlt() {
        List<Alt> alts = this.alts.stream().filter(alt1 -> alt1.getUnbanDate() == 0L && !alt1.isInvalid()).collect(Collectors.toList());

        if (alts.isEmpty()) return null;

        final Alt alt = alts.get(alts.size() == 1 ? 0 : ThreadLocalRandom.current().nextInt(0, alts.size() - 1));
        alt.select();

        return alt;
    }

    public Alt getSelectedAlt() {
        return this.alts.stream().filter(Alt::isSelected).findAny().orElse(null);
    }

	/*private Alt getLoggedAlt() {
		return this.alts.stream().filter(Alt::isLoginSuccessful).findAny().orElse(null);
	}*/

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);

        saveAlts();
        this.tokenContent = this.apiKeyField.getText();

        try {
            Files.write(this.novoline.getDataFolder().resolve("Data.txt"),
                    this.tokenContent.getBytes(StandardCharsets.UTF_8), CREATE, TRUNCATE_EXISTING);
        } catch (Throwable t) {
            this.logger.error("Unable to reach clients folder", t);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.dragging = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (isKeyComboCtrlV(keyCode)) {
            pasteAltsFromClipboard();
        } else if (isKeyComboCtrlC(keyCode)) {
            Alt selectedAlt = getSelectedAlt();

            if (selectedAlt == null) return;

            final String credential = selectedAlt.getCredential().toString();
            final StringSelection selection = new StringSelection(credential);

            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
        } else {
            try {
                switch (keyCode) {
                    case Keyboard.KEY_NUMPAD8:
                    case Keyboard.KEY_UP: {
                        Alt previous = null;

                        for (int i = 0; i < this.alts.size(); i++) {
                            final Alt alt = this.alts.get(i);

                            if (alt.isSelected()) {
                                if (previous != null) selectAlt(alt, previous, i - 1);
                                return;
                            } else {
                                previous = alt;
                            }
                        }

                        break;
                    }

                    case Keyboard.KEY_NUMPAD2:
                    case Keyboard.KEY_DOWN:
                        final int size = this.alts.size();

                        for (int i = 0; i < size; i++) {
                            final Alt alt = this.alts.get(i);

                            if (alt.isSelected()) {
                                if (i + 1 < size) selectAlt(alt, this.alts.get(i + 1), i + 1);
                                return;
                            }
                        }

                        break;

                    case Keyboard.KEY_F5:
                        refreshAlts();
                        break;

                    case Keyboard.KEY_NUMPADENTER:
                    case Keyboard.KEY_RETURN: {
                        final Alt alt = getSelectedAlt();
                        if (alt != null) alt.logIn(true);

                        break;
                    }

                    case Keyboard.KEY_DELETE: {
                        removeCurrentAlt();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.apiKeyField.textboxKeyTyped(typedChar, keyCode);

        if (this.searchField.textboxKeyTyped(typedChar, keyCode) && this.searchField.isEnabled()) {
            this.alts.update();
            setScrolledAndUpdate(0);
        }

        super.keyTyped(typedChar, keyCode);
    }

    void selectAlt(@Nullable Alt oldValue, @NonNull Alt newValue, Integer newValueIndex) {
        if (newValueIndex == null) {
            if ((newValueIndex = this.alts.indexOf(newValue)) == -1) {
                return;
            }
        }

        if (oldValue != null) {
            oldValue.setSelectedProperty(false);
        }

        newValue.setSelectedProperty(true);

        if (newValueIndex < this.scrolled) { // если выделенный аккаунт выше view
            setScrolledAndUpdate(newValueIndex);
        } else if (newValueIndex >= this.scrolled + this.visibleAltsCount) { // если выделенный аккаунт ниже view
            setScrolledAndUpdate(newValueIndex - this.visibleAltsCount + 1);
        }
    }

    private void refreshAlts() {
        loadAlts();
        setScrolledAndUpdate(0);
    }

    @SuppressWarnings("unchecked")
    private void pasteAltsFromClipboard() {
        try {
            final Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (contents == null) return;

            final Stream<String> stream;

            if (contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                stream = Arrays.stream(((String) contents.getTransferData(DataFlavor.stringFlavor)).split("\n"));
            } else if (contents.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                stream = ((List<File>) contents.getTransferData(DataFlavor.javaFileListFlavor)).stream().map(file -> {
                    try {
                        return Files.lines(file.toPath());
                    } catch (Exception e) {
                        return null;
                    }
                }).filter(Objects::nonNull).flatMap(s -> s);
            } else {
                return;
            }

            final Set<Object> seen = ConcurrentHashMap.newKeySet();

            stream.map(s -> {
                if (!s.endsWith("@alt.com")) {
                    final int index = s.indexOf(':');
                    return index == -1 ? null : new String[]{s.substring(0, index), s.substring(index + 1)};
                } else {
                    return new String[]{s, new String(new char[ThreadLocalRandom.current().nextInt(7) + 1]).replace(
                            '\0', 'a')};
                }
            }).filter(Objects::nonNull).filter(alt -> !alt[0].trim().isEmpty() && !alt[1].trim().isEmpty()) //
                    .filter(t -> seen.add(t[0])) // distinct
                    .sorted(Comparator.comparing(o -> o[0])).forEach(alt -> addAlt(new AltCredential(alt[0], alt[1])));
        } catch (Exception e) { // not gonna happen
            e.printStackTrace();
        }
    }

    private void removeCurrentAlt() {
        Alt previous = null;

        for (Alt alt : this.alts) {
            if (alt.isSelected()) {
                removeAlt(alt);

                if (previous != null) previous.setSelectedProperty(true);
                return;
            } else {
                previous = alt;
            }
        }

        saveAlts();
    }

    public boolean hasAlt(@NonNull AltCredential credential) {
        return this.alts.getUnfiltered().stream()
                .anyMatch(alt -> alt.getCredential().getLogin().equals(credential.getLogin()));
    }

    public Alt addAlt(@NonNull AltCredential credential) {
        if (!hasAlt(credential)) {
            final Alt alt;

            if (credential instanceof AlteningAltCredential) {
                final AlteningAltCredential alteningCredential = (AlteningAltCredential) credential;

                final AccountDetails details = alteningCredential.getDetails();
                alt = new Alt(credential,
                        new FakeEntityPlayer(new GameProfile(UUID.randomUUID(), alteningCredential.getName()), null),
                        hypixelProfile(details.getHypixelRank(), Math.max(1, details.getHypixelLevel())), this,0L,"Default",1,false);
            } else {
                final String login = credential.getLogin();
                final String name = isEmail(login) ? "<Unknown Name>" : login;

                alt = new Alt(credential, new FakeEntityPlayer(new GameProfile(UUID.randomUUID(), name), null),
                        HypixelProfile.empty(), this,0L,"Default",1,false);
            }

            this.alts.add(alt);
            updateVisibleAlts();

            alt.select();
            return alt;
        } else {
            Novoline.getInstance().getNotificationManager().pop("Account is already added!", ERROR);
            return null;
        }
    }

    public void removeAlt(@NonNull Alt alt) {
        if (this.alts.remove(alt)) {
            updateVisibleAlts();
        }
    }

    public void loadAlts() {
        this.alts.clear();

        try {
            final NBTTagCompound tagCompound = CompressedStreamTools
                    .read(new File(Novoline.getInstance().getPathString(), "alts.novo"));
            if (tagCompound == null) return;

            final NBTTagList altListTag = tagCompound.getTagList("alts", 10);

            for (int i = 0; i < altListTag.tagCount(); ++i) {
                final Alt alt;

                try {
                    alt = Alt.fromNBT(this, altListTag.getCompoundTagAt(i));
                } catch (Throwable t) {
                    this.logger.error("Failed to parse account: " + altListTag.getCompoundTagAt(i).toString(), t);
                    continue;
                }

                this.alts.add(alt);
            }
        } catch (Exception e) {
            this.logger.error("Couldn't load alt list", e);
        }

        updateVisibleAlts();
    }

    public void saveAlts() {
        try {
            final NBTTagList tagList = new NBTTagList();

            for (Alt alt : this.alts.getUnfiltered()) {
                tagList.appendTag(alt.asNBTCompound());
            }

            final NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setTag("alts", tagList);

            CompressedStreamTools.safeWrite(tagCompound, new File(Novoline.getInstance().getPathString(), "alts.novo"));
        } catch (Exception e) {
            this.logger.error("Couldn't save alt list", e);
        }
    }

    private enum EnumSort {

        DATE("Date", (o1, o2) -> 0),
        LEVEL("Level", (o1, o2) -> {
            if (o1.getHypixelProfile() == null) {
                if (o2.getHypixelProfile() == null) {
                    return 0;
                } else {
                    return 1;
                }
            } else if (o2.getHypixelProfile() == null) {
                return -1;
            }

            return Integer.compare(o2.getHypixelProfile().getLevel(), o1.getHypixelProfile().getLevel());
        }),
        NAME("Name", (o1, o2) -> o1.getPlayer().getGameProfile().getName()
                .compareTo(o2.getPlayer().getGameProfile().getName())),
        EMAIL("Email", (o1, o2) -> o1.getCredential().getLogin().compareTo(o2.getCredential().getLogin())),
        ;

        private final String criteria;
        private final Comparator<Alt> comparator;

        EnumSort(String criteria, Comparator<Alt> comparator) {
            this.criteria = criteria;
            this.comparator = comparator;
        }

        @NonNull
        public String getCriteria() {
            return "By " + this.criteria;
        }

        public Comparator<Alt> getComparator() {
            return this.comparator;
        }
    }

    //region Lombok-alternative
    public List<Alt> getAlts() {
        return (List<Alt>) this.alts.getUnfiltered();
    }

    public TClient getTClient() {
        return this.tclient;
    }

    public String getTokenContent() {
        return this.tokenContent;
    }

    public Alt getCurrentAlt() {
        return this.currentAlt;
    }

    public void setCurrentAlt(Alt currentAlt) {
        this.currentAlt = currentAlt;
    }

    public float getAltBoxAlphaStep() {
        return this.altBoxAlphaStep;
    }

    public float getAltBoxAnimationStep() {
        return this.altBoxAnimationStep;
    }

    public TokenField getApiKeyField() {
        return this.apiKeyField;
    }
    //endregion

}
