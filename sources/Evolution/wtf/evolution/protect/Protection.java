package wtf.evolution.protect;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import wtf.evolution.Main;

import java.io.IOException;
import java.util.Date;

public class Protection {

    public int uid;
    public String nickname;
    public String till;

    public void start() throws IOException {
        String hwid = getHwid();

        Document a = Jsoup.connect("http://89.107.10.34:7777?hwid=" + hwid).get();

        if (a.text().equals(genKey(hwid))) {
            Document b = Jsoup.connect("http://89.107.10.34:7777?get=" + hwid).get();

            nickname = b.text().split(":")[1];
            uid = Integer.parseInt(b.text().split(":")[0]);
            till = b.text().split(":")[2];

            if (uid < 1) {
                Main.protectedd = true;
            }


        } else System.exit(0);
    }

    public static String genKey(String hwid) {
        String secret = "98sdfg8765dfgh574fgh56df8g76dfg8907j52";
        int mins = new Date(System.currentTimeMillis()).getMinutes();
        String out = "";
        for (int i = 0; i < hwid.length(); i++) {
            out += hwid.charAt(i) ^ secret.charAt(i % secret.length());
            if (i % 2 == 0) out += "-";
        }
        return DigestUtils.md5Hex(out + mins);
    }

    public static String getHwid() {
        return DigestUtils.md5Hex(System.getenv("PROCESSOR_ARCHITECTURE") + System.getProperty("os.arch") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL"));
    }

}
