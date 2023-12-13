package me.earth.phobos.features.modules.misc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.Util;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

public class Translator
        extends Module {
    public static final String API_KEY = "trnsl.1.1.20200403T133250Z.c0062863622d7503.ca7fca44b9d2259ba3dadd61ddf7c15a2c9f3876";
    private final Setting<Language> sourceLanguage = this.register(new Setting<Language>("SourceLanguage", Language.English));
    private final Setting<Language> targetLanguage = this.register(new Setting<Language>("TargetLanguage", Language.Spanish));
    public Translate translate;

    public Translator() {
        super("Translator", "Translates text to a different language", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onSendPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            event.setCanceled(true);
            this.translate = new Translate(((CPacketChatMessage) event.getPacket()).getMessage(), this.sourceLanguage.getValue(), this.targetLanguage.getValue());
            this.translate.start();
        }
    }

    private JsonObject request(String URL2) throws IOException {
        URL url = new URL(URL2);
        URLConnection urlConn = url.openConnection();
        urlConn.addRequestProperty("User-Agent", "Mozilla");
        InputStream inStream = urlConn.getInputStream();
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) urlConn.getContent()));
        inStream.close();
        return root.getAsJsonObject();
    }

    public enum Language {
        Azerbaijan("az"),
        Albanian("sq"),
        Amharic("am"),
        English("en"),
        Arabic("ar"),
        Armenian("hy"),
        Afrikaans("af"),
        Basque("eu"),
        Bashkir("ba"),
        Belarusian("be"),
        Bengali("bn"),
        Burmese("my"),
        Bulgarian("bg"),
        Bosnian("bs"),
        Welsh("cy"),
        Hungarian("hu"),
        Vietnamese("vi"),
        Haitian("ht"),
        Galician("gl"),
        Dutch("nl"),
        HillMari("mrj"),
        Greek("el"),
        Georgian("ka"),
        Gujarati("gu"),
        Danish("da"),
        Hebrew("he"),
        Yiddish("yi"),
        Indonesian("id"),
        Irish("ga"),
        Italian("it"),
        Icelandic("is"),
        Spanish("es"),
        Kazakh("kk"),
        Kannada("kn"),
        Catalan("ca"),
        Kyrgyz("ky"),
        Chinese("zh"),
        Korean("ko"),
        Xhosa("xh"),
        Khmer("km"),
        Laotian("lo"),
        Latin("la"),
        Latvian("lv"),
        Lithuanian("lt"),
        Luxembourgish("lb"),
        Malagasy("mg"),
        Malay("ms"),
        Malayalam("ml"),
        Maltese("mt"),
        Macedonian("mk"),
        Maori("mi"),
        Marathi("mr"),
        Mari("mhr"),
        Mongolian("mn"),
        German("de"),
        Nepali("ne"),
        Norwegian("no"),
        Russian("ru"),
        Punjabi("pa"),
        Papiamento("pap"),
        Persian("fa"),
        Polish("pl"),
        Portuguese("pt"),
        Romanian("ro"),
        Cebuano("ceb"),
        Serbian("sr"),
        Sinhala("si"),
        Slovakian("sk"),
        Slovenian("sl"),
        Swahili("sw"),
        Sundanese("su"),
        Tajik("tg"),
        Thai("th"),
        Tagalog("tl"),
        Tamil("ta"),
        Tatar("tt"),
        Telugu("te"),
        Turkish("tr"),
        Udmurt("udm"),
        Uzbek("uz"),
        Ukrainian("uk"),
        Urdu("ur"),
        Finnish("fi"),
        French("fr"),
        Hindi("hi"),
        Croatian("hr"),
        Czech("cs"),
        Swedish("sv"),
        Scottish("gd"),
        Estonian("et"),
        Esperanto("eo"),
        Javanese("jv"),
        Japanese("ja");

        private final String code;

        Language(String code) {
            this.code = code;
        }

        public static Language getByCode(String code) {
            for (Language language : Language.values()) {
                if (!language.code.equals(code)) continue;
                return language;
            }
            return null;
        }

        public String getCode() {
            return this.code;
        }
    }

    public static class Translate
            extends Thread {
        public String message;
        public Language sourceLang;
        public Language lang;
        public String finalMessage = null;
        Thread thread;

        public Translate(String message, Language sourceLang, Language lang) {
            super("Translate");
            this.message = message;
            this.sourceLang = sourceLang;
            this.lang = lang;
        }

        @Override
        public void run() {
            try {
                this.finalMessage = this.request("https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20200403T133250Z.c0062863622d7503.ca7fca44b9d2259ba3dadd61ddf7c15a2c9f3876&text=" + this.message.replace(" ", "%20") + "&lang=" + this.sourceLang.getCode() + "-" + this.lang.getCode()).get("text").getAsString();
                Util.mc.player.connection.sendPacket(new CPacketChatMessage(this.finalMessage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void start() {
            if (this.thread == null) {
                this.thread = new Thread(this, "Translate");
                this.thread.start();
            }
        }

        private JsonObject request(String URL2) throws IOException {
            URL url = new URL(URL2);
            URLConnection urlConn = url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Mozilla");
            InputStream inStream = urlConn.getInputStream();
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) urlConn.getContent()));
            inStream.close();
            return root.getAsJsonObject();
        }
    }
}

