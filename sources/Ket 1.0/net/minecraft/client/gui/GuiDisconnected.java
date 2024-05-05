package net.minecraft.client.gui;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import client.Client;
import client.module.impl.combat.KillAura;
import client.module.impl.other.AutoRegister;
import client.ui.GuiAccountManager;
import client.util.ServerUtil;
import client.util.TimeUtil;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Session;
import org.apache.commons.lang3.RandomStringUtils;

public class GuiDisconnected extends GuiScreen
{
    private String reason;
    private IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp)
    {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey, new Object[0]);
        this.message = chatComp;
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    }

    public void initGui()
    {

        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu", new Object[0])));
        buttonList.add(new GuiButton(1, width / 2 - 100, height / 2 + field_175353_i / 2 + fontRendererObj.FONT_HEIGHT + 24, 98, 20, "Reconnect"));
        buttonList.add(new GuiButton(2, width / 2 + 2, height / 2 + field_175353_i / 2 + fontRendererObj.FONT_HEIGHT + 24, 98, 20, "Account Manager"));
        final AutoRegister autoRegister = Client.INSTANCE.getModuleManager().get(AutoRegister.class);
        if (autoRegister.isEnabled() && autoRegister.getMushGen().getValue()) {

          //  final String username = generateNickname() + RandomStringUtils.random(5, false, true);;
            String altFilePath = "nicks.txt";

            try {
                List<String> alts = readAltsFromFile(altFilePath);
                String randomAlt = getRandomAlt(alts);

            mc.setSession(new Session(randomAlt, randomAlt, "0", "legacy"));
            mc.displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), mc, new ServerData("MushMC", "logic.mush.com.br:25565", false)));
                String filePath = "alts.txt";
                int wordCount = 0;

                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] words = line.split("\\s+");
                        wordCount += words.length;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String filePath1 = "nicks.txt";
                int wordCountshit = 0;

                try (BufferedReader reader1 = new BufferedReader(new FileReader(filePath1))) {
                    String line1;
                    while ((line1 = reader1.readLine()) != null) {
                        String[] jew = line1.split("\\s+");
                        wordCountshit += jew.length;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Using: " + randomAlt.substring(0, 4) + "*****" + " | Alts in alts.txt " + wordCount + " | Nicks in nicks.txt " + wordCountshit);
                removeAltFromFile(altFilePath, randomAlt);

            } catch (IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }

        }
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        if (button.id == 1) mc.displayGuiScreen(new GuiConnecting(parentScreen, mc, ServerUtil.getLastServerData()));
        if (button.id == 2) mc.displayGuiScreen(new GuiAccountManager(parentScreen));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int i = this.height / 2 - this.field_175353_i / 2;

        if (this.multilineMessage != null)
        {
            for (String s : this.multilineMessage)
            {
                this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 16777215);
                i += this.fontRendererObj.FONT_HEIGHT;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private static List<String> readAltsFromFile(String filePath) throws IOException {
        List<String> alts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                alts.add(line);
            }
        }

        return alts;
    }

    private static String getRandomAlt(List<String> alts) {
        Random random = new Random();
        int randomIndex = random.nextInt(alts.size());
        return alts.get(randomIndex);
    }

    public static void sendDiscordWebhook(String webhookUrl, String message) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonPayload = "{\"content\": \"" + message + "\"}";

            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(jsonPayload);
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 204) {
                System.out.println("Сообщение успешно отправлено на вебхук Discord.");
            } else {
                System.out.println("Произошла ошибка при отправке сообщения на вебхук Discord. Код ошибки: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void removeAltFromFile(String filePath, String altToRemove) throws IOException {
        List<String> updatedAlts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals(altToRemove)) {
                    updatedAlts.add(line);
                }
            }
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            for (String alt : updatedAlts) {
                writer.write(alt + "\n");
            }
        }
    }
}