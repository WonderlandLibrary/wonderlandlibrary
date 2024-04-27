package me.aquavit.liquidsense.ui.client.gui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.AltService;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.ui.client.gui.elements.GuiButtonElement;
import me.aquavit.liquidsense.ui.client.gui.elements.GuiButtonSlot;
import me.aquavit.liquidsense.ui.client.gui.elements.GuiPasswordField;
import me.aquavit.liquidsense.ui.client.gui.elements.GuiUsernameField;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.utils.login.LoginUtils;
import me.aquavit.liquidsense.utils.login.MinecraftAccount;
import me.aquavit.liquidsense.utils.login.UserUtils;
import me.aquavit.liquidsense.utils.login.oauth.OAuthService;
import me.aquavit.liquidsense.utils.mc.TabUtils;
import me.aquavit.liquidsense.utils.misc.HttpUtils;
import me.aquavit.liquidsense.utils.misc.MiscUtils;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GuiAltManager extends GuiScreen {

    public static final AltService altService = new AltService();

    public static final OAuthService oAuthService = new OAuthService();

    private static final Map<String, Boolean> GENERATORS = new HashMap<>();
    private final GuiScreen prevGui;
    public String status = "§7Idle...";
    public static boolean loadcircle = false;
    private GuiButtonElement loginButton;
    private GuiButtonElement randomButton;
    private GuiList altsList;
    public static HashMap<Integer, ResourceLocation> skin = new HashMap<Integer, ResourceLocation>();
    //GuiAdd
    private GuiButtonElement addButton;
    private GuiButtonElement clipboardButton;
    private GuiUsernameField username;
    private GuiPasswordField password;

    public GuiAltManager(final GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    public static void loadGenerators() {
        try {
            // Read versions json from cloud
            final JsonElement jsonElement = new JsonParser().parse(HttpUtils.get(LiquidSense.CLIENT_CLOUD + "/generators.json"));

            // Check json is valid object
            if (jsonElement.isJsonObject()) {
                // Get json object of element
                final JsonObject jsonObject = jsonElement.getAsJsonObject();

                jsonObject.entrySet().forEach(stringJsonElementEntry -> GENERATORS.put(stringJsonElementEntry.getKey(), stringJsonElementEntry.getValue().getAsBoolean()));
            }
        } catch (final Throwable throwable) {
            // Print throwable to console
            ClientUtils.getLogger().error("Failed to load enabled generators.", throwable);
        }
    }

    public static String login(final MinecraftAccount minecraftAccount) {
        if (minecraftAccount == null)
            return "";

        if (altService.getCurrentService() != AltService.EnumAltService.MOJANG) {
            try {
                altService.switchService(AltService.EnumAltService.MOJANG);
            } catch (final NoSuchFieldException | IllegalAccessException e) {
                ClientUtils.getLogger().error("Something went wrong while trying to switch alt service.", e);
            }
        }

        if (minecraftAccount.isCracked()) {
            LoginUtils.loginCracked(minecraftAccount.getName());
            loadcircle = false;
            return "§cYour name is now §8" + minecraftAccount.getName() + "§c.";
        }

        //mojang auth已废弃
//        LoginUtils.LoginResult result = LoginUtils.login(minecraftAccount.getName(), minecraftAccount.getPassword());
//        if (result == LoginUtils.LoginResult.LOGGED) {
//            String userName = Minecraft.getMinecraft().getSession().getUsername();
//            minecraftAccount.setAccountName(userName);
//            LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.accountsConfig);
//            loadcircle = false;
//            return "§cYour name is now §f§l" + userName + "§c.";
//        }
//
//        if (result == LoginUtils.LoginResult.WRONG_PASSWORD)
//            return "§cWrong password.";
//
//        if (result == LoginUtils.LoginResult.NO_CONTACT)
//            return "§cCannot contact authentication server.";
//
//        if (result == LoginUtils.LoginResult.INVALID_ACCOUNT_DATA)
//            return "§cInvaild username or password.";
//
//        if (result == LoginUtils.LoginResult.MIGRATED)
//            return "§cAccount migrated.";

        return "";
    }

    public void initGui() {
        altsList = new GuiList(this);
        altsList.registerScrollButtons(7, 8);

        int index = -1;

        for (int i = 0; i < LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.size(); i++) {
            MinecraftAccount minecraftAccount = LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.get(i);

            if (minecraftAccount != null && (
                    ((
                            // When password is empty, the account is cracked
                            minecraftAccount.getPassword() == null || minecraftAccount.getPassword().isEmpty()) && minecraftAccount.getName() != null && minecraftAccount.getName().equals(mc.session.getUsername()))
                            // When the account is a premium account match the IGN
                            || minecraftAccount.getAccountName() != null && minecraftAccount.getAccountName().equals(mc.session.getUsername())
            )) {
                index = i;
                break;
            }
        }

        altsList.elementClicked(index, false, 0, 0);
        altsList.scrollBy(index * altsList.slotHeight);

        int j = 22;
        this.buttonList.add(new GuiButtonElement(2, 440, j + 24, 70, 20, "Remove"));
        this.buttonList.add(new GuiButtonElement(7, 440, j + 24 * 2, 70, 20, "Import"));
        this.buttonList.add(new GuiButtonElement(8, 440, j + 24 * 3, 70, 20, "Copy"));
        this.buttonList.add(new GuiButtonElement(0, 440, j + 24 * 4, 70, 20, "Back"));


        //GuiAdd
        Keyboard.enableRepeatEvents(true);
//        buttonList.add(addButton = new GuiButtonElement(12, 290, 93, 50, 15,"Add"));
        buttonList.add(addButton = new GuiButtonElement(20, 290, 73, 120, 15,"Microsoft login"));
        buttonList.add(loginButton = new GuiButtonElement(3, 290, 93, 120, 15, "Direct login"));
//        buttonList.add(clipboardButton = new GuiButtonElement(13, 290, 113, 50, 15,"Clip"));
//        buttonList.add(randomButton = new GuiButtonElement(4, 360, 113, 50, 15,"Random"));

        username = new GuiUsernameField(2, Fonts.font20, 290, 51, 120, 15);
//        username.setFocused(true);
        username.setMaxStringLength(Integer.MAX_VALUE);
        password = new GuiPasswordField(3, Fonts.font20, 290, 71, 120, 15);
        password.setMaxStringLength(Integer.MAX_VALUE);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);

        altsList.drawScreen(mouseX, mouseY, partialTicks);

        Fonts.font18.drawStringWithShadow("Status: " + status, 280, 158, Color.WHITE.getRGB());

        //Guiadd
        drawRect(280, 43, 280 + 140, 115, new Color(1,1,1, 80).getRGB());
        drawRect(280, 43, 282, 115, new Color(17, 211,255, 255).getRGB());
        username.drawTextBox();
//        password.drawTextBox();

        if(username.getText().isEmpty() && !username.isFocused())
            drawCenteredString(Fonts.font20, "Username", 337, 55, Color.WHITE.getRGB());

//        if(password.getText().isEmpty() && !password.isFocused())
//            drawCenteredString(Fonts.font20, "Password", 317, 75, Color.WHITE.getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (!button.enabled) return;

        switch (button.id) {
            case 0:
                mc.displayGuiScreen(prevGui);
                break;
            case 2:
                if (altsList.getSelectedSlot() != -1 && altsList.getSelectedSlot() < altsList.getSize()) {
                    LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.remove(altsList.getSelectedSlot());
                    LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.accountsConfig);
                    status = "§aThe account has been removed.";
                } else
                    status = "§cSelect an account.";
                break;
            case 3:
                if (!username.getText().isEmpty()) {
                    loginButton.enabled = randomButton.enabled = false;

                    new Thread(() -> {
                        status = "§aLogging in...";

                        if (password.getText().isEmpty())
                            status = GuiAltManager.login(new MinecraftAccount(ColorUtils.translateAlternateColorCodes(username.getText())));
                        else
                            status = GuiAltManager.login(new MinecraftAccount(username.getText(), password.getText()));

                        loginButton.enabled = randomButton.enabled = true;
                        username.setText("");
                        password.setText("");
                    }).start();
                } else if ((username.getText().isEmpty() || username.getText().equals("")) && altsList.getSelectedSlot() != -1 && altsList.getSelectedSlot() < altsList.getSize()) {
                    loginButton.enabled = randomButton.enabled = false;

                    final Thread thread = new Thread(() -> {
                        final MinecraftAccount minecraftAccount = LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.get(altsList.getSelectedSlot());
                        status = "§aLogging in...";
                        loadcircle = true;
                        status = login(minecraftAccount);

                        loginButton.enabled = randomButton.enabled = true;
                    }, "AltLogin");
                    thread.start();
                } else {
                    status = "§cEnter/Select an account.";
                    loadcircle = false;
                }
                break;
            case 4:
                if (LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.size() <= 0) {
                    status = "§cThe list is empty.";
                    return;
                }

                final int randomInteger = new Random().nextInt(LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.size());

                if (randomInteger < altsList.getSize())
                    altsList.selectedSlot = randomInteger;

                loginButton.enabled = randomButton.enabled = false;

                final Thread thread = new Thread(() -> {
                    final MinecraftAccount minecraftAccount = LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.get(randomInteger);
                    status = "§aLogging in...";
                    loadcircle = true;
                    status = login(minecraftAccount);

                    loginButton.enabled = randomButton.enabled = true;
                }, "AltLogin");
                thread.start();
                break;
            case 7:
                final File file = MiscUtils.openFileChooser();

                if (file == null)
                    return;

                final FileReader fileReader = new FileReader(file);
                final BufferedReader bufferedReader = new BufferedReader(fileReader);

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    final String[] accountData = line.split(":", 2);

                    boolean alreadyAdded = false;

                    for (final MinecraftAccount registeredMinecraftAccount : LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts) {
                        if (registeredMinecraftAccount.getName().equalsIgnoreCase(accountData[0])) {
                            alreadyAdded = true;
                            break;
                        }
                    }

                    if (!alreadyAdded) {
                        if (accountData.length > 1)
                            LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.add(new MinecraftAccount(accountData[0], accountData[1]));
                        else
                            LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.add(new MinecraftAccount(accountData[0]));
                    }
                }

                fileReader.close();
                bufferedReader.close();
                LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.accountsConfig);
                status = "§aThe accounts were imported successfully.";
                break;
            case 8:
                if (altsList.getSelectedSlot() != -1 && altsList.getSelectedSlot() < altsList.getSize()) {
                    final MinecraftAccount minecraftAccount = LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.get(altsList.getSelectedSlot());

                    if (minecraftAccount == null)
                        break;

                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(minecraftAccount.getName() + ":" + minecraftAccount.getPassword()), null);
                    status = "§aCopied account into your clipboard.";
                } else
                    status = "§cSelect an account.";
                break;
            case 12:
                if (LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.stream().anyMatch(account -> account.getName().equals(username.getText()))) {
                    status = "§cThe account has already been added.";
                    break;
                }

                addAccount(username.getText(), password.getText());
                break;
            case 13:
                try{
                    final String clipboardData = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
                            .getData(DataFlavor.stringFlavor);
                    final String[] accountData = clipboardData.split(":", 2);

                    if (!clipboardData.contains(":") || accountData.length != 2) {
                        status = "§cInvalid clipboard data. (Use: E-Mail:Password)";
                        return;
                    }

                    addAccount(accountData[0], accountData[1]);
                }catch(final UnsupportedFlavorException e) {
                    status = "§cClipboard flavor unsupported!";
                    ClientUtils.getLogger().error("Failed to read data from clipboard.", e);
                }
                break;
            case 20:
                new Thread(() -> {
                    status = "§aLogging in...";

                    oAuthService.authenticate(null);
                }).start();
                break;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        switch (keyCode) {
            case Keyboard.KEY_ESCAPE:
                mc.displayGuiScreen(prevGui);
                return;
            case Keyboard.KEY_TAB: //GuiAdd
                TabUtils.tab(username, password);
                return;
            case Keyboard.KEY_UP: {
                int i = altsList.getSelectedSlot() - 1;
                if (i < 0)
                    i = 0;
                altsList.elementClicked(i, false, 0, 0);
                break;
            }
            case Keyboard.KEY_DOWN: {
                int i = altsList.getSelectedSlot() + 1;
                if (i >= altsList.getSize())
                    i = altsList.getSize() - 1;
                altsList.elementClicked(i, false, 0, 0);
                break;
            }
            case Keyboard.KEY_RETURN: {
                if (username.getText().isEmpty()) {
                    altsList.elementClicked(altsList.getSelectedSlot(), true, 0, 0);
                } else {
                    actionPerformed(addButton);
                }
                break;
            }
            case Keyboard.KEY_NEXT: {
                altsList.scrollBy(height - 100);
                break;
            }
            case Keyboard.KEY_PRIOR: {
                altsList.scrollBy(-height + 100);
                return;
            }
        }

        //GuiAdd
        if(username.isFocused())
            username.textboxKeyTyped(typedChar, keyCode);

        if(password.isFocused())
            password.textboxKeyTyped(typedChar, keyCode);

        super.keyTyped(typedChar, keyCode);
    }

    @Override //GuiAdd
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        username.mouseClicked(mouseX, mouseY, mouseButton);
//        password.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override //GuiAdd
    public void updateScreen() {
        username.updateCursorCounter();
//        password.updateCursorCounter();

        super.updateScreen();
    }

    @Override //GuiAdd
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }

    private void addAccount(final String name, final String passwordd) { //GuiAdd
        if (LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.stream()
                .anyMatch(account -> account.getName().equals(name))) {
            status = "§cThe account has already been added.";
            return;
        }

        addButton.enabled = clipboardButton.enabled = false;

        final MinecraftAccount account = new MinecraftAccount(name, passwordd);

        new Thread(() -> {
            if (!account.isCracked()) {
                status = "§aChecking...";

                try {
                    final AltService.EnumAltService oldService = GuiAltManager.altService.getCurrentService();

                    if (oldService != AltService.EnumAltService.MOJANG) {
                        GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
                    }

                    final YggdrasilUserAuthentication userAuthentication = (YggdrasilUserAuthentication)
                            new YggdrasilAuthenticationService(Proxy.NO_PROXY, "")
                                    .createUserAuthentication(Agent.MINECRAFT);

                    userAuthentication.setUsername(account.getName());
                    userAuthentication.setPassword(account.getPassword());

                    userAuthentication.logIn();
                    account.setAccountName(userAuthentication.getSelectedProfile().getName());

                    if (oldService == AltService.EnumAltService.THEALTENING)
                        GuiAltManager.altService.switchService(AltService.EnumAltService.THEALTENING);
                } catch (NullPointerException | AuthenticationException | NoSuchFieldException | IllegalAccessException e) {
                    status = "§cThe account doesn't work.";
                    addButton.enabled = clipboardButton.enabled = true;
                    return;
                }
            }


            LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.add(account);
            LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.accountsConfig);

            status = "§aThe account has been added.";
            addButton.enabled = clipboardButton.enabled = true;
            username.setText("");
            password.setText("");
        }).start();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        altsList.handleMouseInput();
    }

    private class GuiList extends GuiButtonSlot {

        private int selectedSlot;

        GuiList(GuiScreen prevGui) {
            super(260, prevGui.height, 40, prevGui.height - 40, 30);
        }

        @Override
        protected boolean isSelected(int id) {
            return selectedSlot == id;
        }

        int getSelectedSlot() {
            if (selectedSlot > LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.size())
                selectedSlot = -1;
            return selectedSlot;
        }

        public void setSelectedSlot(int selectedSlot) {
            this.selectedSlot = selectedSlot;
        }

        @Override
        protected int getSize() {
            return LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.size();
        }

        @Override
        protected void elementClicked(int var1, boolean doubleClick, int var3, int var4) {
            selectedSlot = var1;

            if (doubleClick) {
                if (altsList.getSelectedSlot() != -1 && altsList.getSelectedSlot() < altsList.getSize() && loginButton.enabled) {
                    loginButton.enabled = randomButton.enabled = false;

                    new Thread(() -> {
                        MinecraftAccount minecraftAccount = LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.get(altsList.getSelectedSlot());
                        status = "§aLogging in...";
                        loadcircle = true;
                        status = "§c" + login(minecraftAccount);

                        loginButton.enabled = randomButton.enabled = true;
                    }, "AltManagerLogin").start();
                } else {
                    status = "§cSelect an account.";
                    loadcircle = false;
                }

            }
        }

        @Override
        protected void drawSlot(int id, int x, int y, int var4, int var5, int var6) {
            MinecraftAccount minecraftAccount = LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.get(id);
            if (loadcircle && minecraftAccount == LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.get(altsList.getSelectedSlot())) {
                int rot = (int) ((System.nanoTime() / 5000000) % 360);
                RenderUtils.drawCircle((float) (width / 2) + 100, y + 4, 3, rot - 180, rot);
            }
            if (!minecraftAccount.isCracked()) {
                if (!skin.containsKey(id)){
                    skin.put(id,UserUtils.getPlayerSkin(UserUtils.getUUID(minecraftAccount.getAccountName())));
                } else {
                    if (skin.get(id) != null) {
                        RenderUtils.drawHead(skin.get(id),(width / 2) - 104, y + 2, 20, 20);
                        RenderUtils.drawFilledCircle((width / 2) - 84,y + 2,2,Color.GREEN);
                    } else {
                        RenderUtils.drawHead(new ResourceLocation("textures/entity/steve.png"), (width / 2) - 104, y + 2, 20, 20);
                        RenderUtils.drawFilledCircle((width / 2) - 84,y + 2,2,Color.GREEN);
                    }
                }
            } else {
                RenderUtils.drawHead(new ResourceLocation("textures/entity/steve.png"), (width / 2) - 104, y + 2, 20, 20);
                RenderUtils.drawFilledCircle((width / 2) - 84,y + 2,2,Color.GRAY);
            }
        }

        @Override
        protected void drawBackground() {
        }
    }

}
