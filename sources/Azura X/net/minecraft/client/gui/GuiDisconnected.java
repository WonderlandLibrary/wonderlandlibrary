package net.minecraft.client.gui;

import best.azura.client.api.account.AccountData;
import best.azura.client.api.account.AccountType;
import best.azura.client.api.account.BannedData;
import best.azura.client.impl.Client;
import best.azura.irc.impl.packets.client.C4BanNotifierPacket;
import best.azura.client.impl.ui.gui.MainMultiplayer;
import best.azura.client.impl.ui.gui.account.MainAccount;
import best.azura.client.impl.ui.gui.proxy.ProxyManager;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.PlayTimeUtil;
import best.azura.client.util.other.ServerUtil;
import com.google.gson.JsonObject;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

public class GuiDisconnected extends GuiScreen
{
    private final String reason;
    private final IChatComponent message;
    private ArrayList<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;
    private boolean triedToReconnect;

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp)
    {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey);
        this.message = chatComp;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        this.buttonList.clear();
        this.multilineMessage = new ArrayList<>(this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50));
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT * 3 + 3, "Reconnect"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT * 4 + 3 * 5, "Account Manager"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT * 5 + 3 * 9, "Reconnect with random name"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT * 6 + 3 * 13, "Remove alt and reconnect"));
        /*if (ServerUtil.lastIP.contains("hypixel")) {
            buttonList.get(0).yPosition += 3;
            multilineMessage.add("DO NOT EXIT GUI, CALCULATING WATCHDOG BAN STATUS");
            final Thread hypixelCheckingThread = new Thread(() -> {
                boolean watchdog = false;
                final String banInformation = HypixelBanUtil.getHypixelBanInformation(getBanId());
                JsonElement jsonElement = null;

                try {
                    jsonElement = new JsonParser().parse(banInformation);
                } catch (Exception ignore) {}

                if (jsonElement != null && jsonElement.isJsonObject()) {
                    final JsonObject jsonObject = jsonElement.getAsJsonObject();
                    multilineMessage.remove(multilineMessage.get(multilineMessage.size()-1));
                    if (jsonObject.has("errors")) {
                        multilineMessage.add("Error while checking ban type " + jsonObject.get("errors").getAsJsonArray().get(0).getAsJsonObject().get("code").getAsString() + ": " + jsonObject.get("errors").getAsJsonArray().get(0).getAsJsonObject().get("message"));
                    } else if (jsonObject.get("success").getAsBoolean()) {
                        if (jsonObject.get("punishmentCategory").getAsString().equals("hacks")) {
                            watchdog = true;
                        }
                        List<String> list1 = new ArrayList<>();
                        for (String s : multilineMessage) {
                            if (s.contains("Cheating")) list1.add(s + (watchdog ? " [WATCHDOG CHEAT DETECTION]" : " [STAFF BAN]"));
                            else list1.add(s);
                        }
                        multilineMessage.clear();
                        multilineMessage.addAll(list1);
                        buttonList.get(0).yPosition -= 3;
                    }
                }
            });
            hypixelCheckingThread.start();
        }

         */
        this.multilineMessage.add("You played for " + PlayTimeUtil.format(System.currentTimeMillis() - ServerUtil.joinedTime) + ".");
        for (GuiButton button : buttonList) {
            button.yPosition += 9;
        }
        new Thread(() -> {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (((System.currentTimeMillis() - ServerUtil.joinedTime) / 1000 / 60) % 60 > 4 &&
                    this.multilineMessage.stream().noneMatch(m -> EnumChatFormatting.getTextWithoutFormattingCodes(m).toLowerCase().contains("ban")) &&
                    Client.INSTANCE.getAccountManager().getLast() != null &&
                    Client.INSTANCE.getAccountManager().getLast().getBanned().stream().anyMatch(b -> b.getServerIp().equals(ServerUtil.lastIP))) {
                Client.INSTANCE.getAccountManager().getLast().getBanned().removeIf(b -> b.getServerIp().equals(ServerUtil.lastIP));
            }

            if ((System.currentTimeMillis() - ServerUtil.joinedTime >= Duration.ofSeconds(10).toMillis())  &&
                    this.multilineMessage.stream().anyMatch(m -> EnumChatFormatting.getTextWithoutFormattingCodes(m).toLowerCase().contains("ban"))) {
                C4BanNotifierPacket c4BanNotifierPacket = new C4BanNotifierPacket(null);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("server", ServerUtil.lastIP);

                c4BanNotifierPacket.setContent(jsonObject);

                Client.INSTANCE.getIrcConnector().sendPacket(c4BanNotifierPacket);
            }

            if (this.multilineMessage.stream().anyMatch(m -> EnumChatFormatting.getTextWithoutFormattingCodes(m).toLowerCase().contains("ban")) && Client.INSTANCE.getAccountManager().getLast() != null &&
                    Client.INSTANCE.getAccountManager().getLast().getBanned().stream().noneMatch(b -> b.getServerIp().equals(ServerUtil.lastIP)))
                Client.INSTANCE.getAccountManager().getLast().getBanned().add(new BannedData(ServerUtil.lastIP, Client.INSTANCE.getCurrentDate(),
                        System.currentTimeMillis() - ServerUtil.joinedTime));
        }).start();
    }

    private String getBanId() {
        for (String s : multilineMessage) {
            if (s.contains("Ban ID")) return s.split("§r§f")[1].replace("#", "");
        }
        return "";
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        if (button.id == 1)
        {
            this.mc.displayGuiScreen(new GuiConnecting(new MainMultiplayer(new GuiMainMenu()), mc, ServerUtil.lastData));
        }
        if (button.id == 2)
        {
            this.mc.displayGuiScreen(new MainAccount(new MainMultiplayer(new GuiMainMenu())));
        }
        if (button.id == 3)
        {
            final StringBuilder builder = new StringBuilder();
            final String[] letters = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
                    "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                    "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                    "0", "1", "2", "3","4", "5", "6", "7", "8", "9" };
            for (int i = 0; i < MathUtil.getRandom_int(8, 14); i++) builder.append(letters[MathUtil.getRandom_int(0, letters.length)]);
            new AccountData(AccountType.CRACKED, builder.toString()).login();
            this.mc.displayGuiScreen(new GuiConnecting(new MainMultiplayer(new GuiMainMenu()), mc, ServerUtil.lastData));
        }
        if (button.id == 4)
        {
            if (Client.INSTANCE.getAccountManager().getLast() != null &&
                    Client.INSTANCE.getAccountManager().getData().remove(Client.INSTANCE.getAccountManager().getLast()))
                Client.INSTANCE.getAccountManager().saveAccounts();
            if (!Client.INSTANCE.getAccountManager().getData().isEmpty())
                Client.INSTANCE.getAccountManager().getData().get(Client.INSTANCE.getAccountManager().getData().size() - 1).loginSync();
            this.mc.displayGuiScreen(new GuiConnecting(new MainMultiplayer(new GuiMainMenu()), mc, ServerUtil.lastData));
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        try {
            if (this.multilineMessage != null && !triedToReconnect && ProxyManager.getProxy() != null
                    && ServerUtil.lastData != null &&
                    multilineMessage.stream().anyMatch(s -> s.contains("Malformed reply from SOCKS server")
                            || s.contains("java.net.SocketException: Connection reset"))) {
                mc.displayGuiScreen(new GuiConnecting(this.parentScreen, mc, ServerUtil.lastData));
                triedToReconnect = true;
                return;
            }
        } catch (Exception e) {
            triedToReconnect = false;
        }
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int i = this.height / 2 - this.field_175353_i / 2;

        try {
            if (this.multilineMessage != null)
            {
                for (String s : this.multilineMessage)
                {
                    this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 16777215);
                    i += this.fontRendererObj.FONT_HEIGHT;
                }
            }
        } catch (Exception ignored) {}

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
