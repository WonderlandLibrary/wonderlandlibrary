package me.nyan.flush.module.impl.render;

import com.google.gson.JsonParser;
import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event2D;
import me.nyan.flush.module.Module;
import me.nyan.flush.notifications.Notification;
import me.nyan.flush.ui.elements.ImageRenderer;
import me.nyan.flush.utils.other.Utils;
import me.nyan.flush.utils.other.WebUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class NekosLife extends Module {
    private ImageRenderer imageRenderer;

    public NekosLife() {
        super("NekosLife", Category.RENDER);
    }

    @Override
    public void onEnable() {
        if (mc.theWorld == null) {
            return;
        }

        if (imageRenderer != null) {
            imageRenderer.dispose();
            imageRenderer = null;
        }

        try {
            EndPoint endPoint = Utils.getRandom(getEndPoints());
            String name = Utils.getRandom(endPoint.names);
            ArrayList<String> source = WebUtils.getURLResponse("https://api.nekos.dev/api/v3/images/" +
                    (endPoint.nsfw ? "nsfw" : "sfw") + "/" + (endPoint.gif ? "gif" : "img") + "/" + name + "/");

            if (source.size() == 0) {
                return;
            }
            URL url = new URL(new JsonParser().parse(source.get(0)).getAsJsonObject().get("data").getAsJsonObject()
                    .get("response").getAsJsonObject().get("url").getAsString());

            File folder = new File(Flush.getClientPath(), "nekoslife" + File.separator + (endPoint.nsfw ? "nsfw" : "sfw") +
                    File.separator + (endPoint.gif ? "gif" : "img"));
            if (!folder.exists()) {
                folder.mkdirs();
            }

            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", WebUtils.USER_AGENT);
            InputStream stream = connection.getInputStream();

            byte[] data = IOUtils.toByteArray(stream);
            InputStream inputStream = new ByteArrayInputStream(data);

            imageRenderer = new ImageRenderer();
            int status = imageRenderer.load(inputStream);
            if (status == ImageRenderer.STATUS_OK) {
                flush.getNotificationManager().show(Notification.Type.INFO, "NekosLife", "§aDownloaded " + FilenameUtils.getName(url.getPath()) + ".");
            } else {
                throw new IOException();
            }

            inputStream = new ByteArrayInputStream(data);
            FileUtils.copyInputStreamToFile(inputStream, new File(folder, FilenameUtils.getName(url.getPath())));
        } catch (Exception e) {
            flush.getNotificationManager().show(Notification.Type.ERROR, "NekosLife", "Failed to download.");
            toggle();
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRender2D(Event2D e) {
        if (imageRenderer == null) {
            return;
        }
        float width = imageRenderer.getWidth();
        float height = imageRenderer.getHeight();
        float ratio = width / height;

        if (width > e.getWidth()) {
            width = e.getWidth();
        }
        if (height > e.getHeight()) {
            height = e.getHeight();
        }
        float newRatio = width / height;
        if (newRatio != ratio) {
            width *= ratio / newRatio;
        }
        imageRenderer.draw(e.getWidth() / 2F - width / 2, e.getHeight() / 2F - height / 2F, width, height);
    }

    public EndPoint[] getEndPoints() {
        return new EndPoint[]{
                new EndPoint(false, false, "lizard", "cat", "neko", "no_tag_avatar", "neko_avatars_avatar", "wallpaper",
                        "kitsune", "kiminonawa", "waifu", "keta_avatar", "gecg", "shinobu", "holo_avatar", "smug", "holo"),
                new EndPoint(false, true, "baka", "tickle", "feed", "neko", "poke", "pat", "kiss", "hug", "cuddle", "slap", "smug"),

                new EndPoint(true, false, "pantyhose_lewd", "holo_lewd", "anus_lewd", "kemonomimi_lewd", "peeing_lewd", "cosplay_lewd",
                        "futanari_lewd", "blowjob_lewd", "shinobu_ero", "shinobu_lewd", "kitsune_lewd", "all_tags_lewd", "kemonomimi_ero", "wallpaper_lewd",
                        "feet_ero", "anal_lewd", "femdom_lewd", "kitsune_ero", "solo_lewd", "holo_ero", "yuri_lewd", "feet_lewd", "classic_lewd",
                        "keta_lewd", "neko_lewd", "piersing_lewd", "trap_lewd", "pantyhose_ero", "hplay_ero", "smallboobs_lewd", "neko_ero",
                        "pussy_lewd", "cum_lewd", "keta_avatar", "ero_wallpaper_ero", "ahegao_avatar", "piersing_ero", "bdsm_lewd", "holo_avatar",
                        "all_tags_ero", "tits_lewd", "yuri_ero"),
                new EndPoint(true, true, "pussy_wank", "neko", "kuni", "blow_job", "pussy", "girls_solo", "yuri", "anal",
                        "tits", "classic", "feet", "spank", "cum", "all_tags")
        };
    }

    public static class EndPoint {
        private final boolean nsfw;
        private final boolean gif;
        private final String[] names;

        public EndPoint(boolean nsfw, boolean gif, String... names) {
            this.nsfw = nsfw;
            this.gif = gif;
            this.names = names;
        }

        public boolean isNsfw() {
            return nsfw;
        }

        public boolean isGif() {
            return gif;
        }

        public String[] getNames() {
            return names;
        }
    }
}