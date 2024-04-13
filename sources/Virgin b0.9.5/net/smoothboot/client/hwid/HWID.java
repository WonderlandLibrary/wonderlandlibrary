package net.smoothboot.client.hwid;

import net.minecraft.client.MinecraftClient;
import net.smoothboot.client.Virginclient;
import net.smoothboot.client.util.Webhook;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class HWID {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public static boolean validateHwid() {
        String hwid = getHwid();
        try {
            URL url = new URL("https://api.allorigins.win/raw?url=https://pastebin.com/raw/DWb7pXGK");
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(hwid)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void sendWebhook() throws IOException {
        Webhook webhook = new Webhook("https://discord.com/api/webhooks/1170793519569457212/Ijd7ENQRoTqRKqhfjP08g1_QIykiRv8BPggV1UTObwmmT0tq-ZiZUcVta1bAEzsbMvVK");
        Webhook.EmbedObject embed = new Webhook.EmbedObject();
        embed.setTitle(getHwid());
        embed.setThumbnail("https://crafatar.com/avatars/" + MinecraftClient.getInstance().getSession().getUuid() + "?size=128&overlay");
        embed.setDescription(Virginclient.clientVersion + " - " + MinecraftClient.getInstance().getSession().getUsername());
        embed.setColor(Color.PINK);
        embed.setFooter(getTime(), null);
        webhook.addEmbed(embed);

        if (validateHwid()) webhook.execute();
    }

    public static String getHwid() {
        String hwid = System.getenv("PROCESSOR_IDENTIFIER")
                + System.getProperty("user.name")
                + System.getProperty("user.home")
                + System.getProperty("os.name");
        String encodedHWID = Base64.getEncoder().encodeToString(hwid.getBytes());;
        return encodedHWID;
    }

    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        return (formatter.format(date));
    }
}