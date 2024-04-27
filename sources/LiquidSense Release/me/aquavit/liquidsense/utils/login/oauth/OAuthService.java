package me.aquavit.liquidsense.utils.login.oauth;

import com.google.gson.*;
import me.aquavit.liquidsense.ui.client.gui.GuiAltManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OAuthService {
    private static final CloseableHttpClient httpclient = HttpClients.createDefault();
    public static final Gson gson = new Gson();
    public static final JsonParser parser = new JsonParser();

    private final String appID = "1427ac50-eb75-42b3-9536-48d263dfeef2";

    public boolean authenticating = false;
    public Session session;

    public IntervalThread thread = null;

    /**
     * Login with RefreshToken
     *
     * @param refreshToken provide if you have one, otherwise null
     * @return RefreshToken
     */
    public String authenticate(String refreshToken) {
        authenticating = true;
        
        GuiAltManager guiAltManager = null;
        
        if (Minecraft.getMinecraft().currentScreen instanceof GuiAltManager) {
            guiAltManager = (GuiAltManager) Minecraft.getMinecraft().currentScreen;
        } else {
            throw new RuntimeException("unreachable");
        }

        //打开授权链接
        guiAltManager.status = "Starting to authenticate";

        System.out.println(refreshToken);
        if (refreshToken == null) {
            if (thread != null) {
                try {
                    thread.join();
                } catch (Exception ignored) {
                }
                thread = null;
            }

            thread = new IntervalThread(appID);
            //等待网站授权
            guiAltManager.status = "Waiting for user to authorize...";
            thread.start();

            while (thread.type != ErrorType.SUCCESS) {
                if (!(Minecraft.getMinecraft().currentScreen instanceof GuiAltManager) && !thread.isFinished) {
                    thread.isFailed = true;
                    break;
                }

                if (thread.isFinished || thread.isFailed) {
                    break;
                }

                if (thread.msg != null) {
                    guiAltManager.status = thread.msg;
                }

                try {
                    Thread.sleep(100);
                } catch (Exception ignored) {
                }
            }

            if (thread.isFailed) {
                authenticating = false;
                return "NO_REFRESH_TOKEN";
            }
        }

        //授权码 -> 授权令牌
        String[] live = null;
        if (refreshToken != null) {
            guiAltManager.status = "Authorization Code -> Authorization Token";
            live = this.liveAuth(refreshToken);
        }

        if ((live != null && !live[0].equals("FAILED")) || refreshToken == null) {
            guiAltManager.status = "§7Authenticating with XBox live...";

            //XBL身份验证
            String[] xbox = this.xBoxAuth(refreshToken == null ? thread.accessToken : live[0]);
            if (xbox[0].equals("FAILED")) {
                guiAltManager.status = "§cAuthentication with XBox live failed! Please try a higher version of Java8";
            } else {
                guiAltManager.status = "§7Authenticating with XSTS...";

                //XSTS身份验证
                String[] xsts = this.xstsAuth(xbox[0]);
                if (xsts[0].equals("FAILED")) {
                    guiAltManager.status = "§cAuthentication with XSTS failed!";
                } else {
                    guiAltManager.status = "§7Authenticating with Minecraft API...";

                    //Minecraft身份验证
                    String[] mc_accessToken = this.minecraftAuth(xsts[0], xsts[1]);
                    if (mc_accessToken[0].equals("FAILED")) {
                        guiAltManager.status = "§cAuthentication with Minecraft API failed!";
                    } else {
                        guiAltManager.status = "§7Obtaining user profile with Minecraft API...";

                        //获取玩家档案
                        String[] mc_userInfo = this.obtainUUID(mc_accessToken[1]);
                        if (mc_userInfo[0].equals("FAILED")) {
                            guiAltManager.status = "§cFailed to obtain user profile!";
                        } else {
                            this.session = new Session(mc_userInfo[1], mc_userInfo[0], mc_accessToken[1], "mojang");
                            guiAltManager.status = "§aSuccessfully logged in to Microsoft account: " + this.session.getUsername();
                            Minecraft.getMinecraft().session = this.session;

                            authenticating = false;
                            return refreshToken == null ? thread.refreshToken : live[1];
                        }
                    }
                }
            }
        } else {
            guiAltManager.status = "§cAuthentication with live failed!";
        }

        authenticating = false;
        return "NO_REFRESH_TOKEN";
    }

    public static void openUrl(String url) {
        try {
            Desktop d = Desktop.getDesktop();
            d.browse(new URI(url));
        } catch (Exception ex) {
            System.out.println("Failed to open url: " + url);
        }
    }

    public String[] obtainUUID(String mc_accessToken) {
        String resultJson = "UNKNOWN";
        try {
            resultJson = sendGet("https://api.minecraftservices.com/minecraft/profile", mc_accessToken);
            JsonObject result = (JsonObject) parser.parse(resultJson);
            return new String[]{result.get("id").getAsString(), result.get("name").getAsString()};
        } catch (Exception ex) {
            return new String[]{"FAILED", resultJson};
        }
    }

    public String[] minecraftAuth(String xbl_token, String uhs) {
        JsonObject obj = new JsonObject();
        obj.addProperty("identityToken", "XBL3.0 x=" + uhs + ";" + xbl_token);

        String resultJson = "UNKNOWN";
        try {
            resultJson = sendPost("https://api.minecraftservices.com/authentication/login_with_xbox", obj.toString());
            System.out.println(resultJson);
            JsonObject result = (JsonObject) parser.parse(resultJson);
            return new String[]{"SUCCESS", result.get("access_token").getAsString()};
        } catch (Exception ex) {
            ex.printStackTrace();
            return new String[]{"FAILED", resultJson};
        }

    }

    public String[] xstsAuth(String xbl_token) {
        JsonObject obj = new JsonObject();
        JsonObject properties = new JsonObject();
        JsonArray arr = new JsonArray();

        properties.addProperty("SandboxId", "RETAIL");
        arr.add(new JsonPrimitive(xbl_token));
        properties.add("UserTokens", arr);

        obj.add("Properties", properties);
        obj.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
        obj.addProperty("TokenType", "JWT");

        String resultJson = "UNKNOWN";
        try {
            resultJson = sendPost("https://xsts.auth.xboxlive.com/xsts/authorize", obj.toString());
            JsonObject result = (JsonObject) parser.parse(resultJson);
            return new String[]{result.get("Token").getAsString(), result.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString()};
        } catch (Exception ex) {
            return new String[]{"FAILED", resultJson};
        }
    }

    public String[] xBoxAuth(String accessToken) {
        JsonObject obj = new JsonObject();
        JsonObject properties = new JsonObject();
        properties.addProperty("AuthMethod", "RPS");
        properties.addProperty("SiteName", "user.auth.xboxlive.com");
        properties.addProperty("RpsTicket", "d=" + accessToken);
        obj.add("Properties", properties);
        obj.addProperty("RelyingParty", "http://auth.xboxlive.com");
        obj.addProperty("TokenType", "JWT");

        String resultJson = "UNKNOWN";
        try {
            resultJson = sendPost("https://user.auth.xboxlive.com/user/authenticate", obj.toString());
            JsonObject result = (JsonObject) parser.parse(resultJson);
            return new String[]{result.get("Token").getAsString(), result.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString()};
        } catch (Exception ex) {
            return new String[]{"FAILED", resultJson};
        }
    }

    public String[] liveAuth(String authCode) {
        HashMap<String, String> map = new HashMap<>();
        map.put("client_id", appID);
        map.put("refresh_token", authCode);
        map.put("grant_type", "refresh_token");
        map.put("scope", "XboxLive.signin offline_access");

        String resultJson = "UNKNOWN";
        try {
            resultJson = sendPost("https://login.live.com/oauth20_token.srf", map);
            JsonObject obj = (JsonObject) parser.parse(resultJson);
            return new String[]{obj.get("access_token").getAsString(), obj.get("refresh_token").getAsString()};
        } catch (Exception ex) {
            return new String[]{"FAILED", resultJson};
        }
    }

    public static String sendPost(String url, Map<String, String> map) throws Exception {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        HttpPost httppost = new HttpPost(url);

        httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httppost.setEntity(entity);

        CloseableHttpResponse response = httpclient.execute(httppost);
        return EntityUtils.toString(response.getEntity());
    }

    public static String sendPost(String url, String value) throws Exception {
        StringEntity entity = new StringEntity(value, "UTF-8");
        HttpPost httppost = new HttpPost(url);

        httppost.setHeader("Content-Type", "application/json");
        httppost.setHeader("Accept", "application/json");
        httppost.setEntity(entity);

        CloseableHttpResponse response = httpclient.execute(httppost);
        return EntityUtils.toString(response.getEntity());
    }

    public static String sendGet(String url, String header) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Bearer " + header);

        CloseableHttpResponse response = httpclient.execute(httpGet);
        return EntityUtils.toString(response.getEntity());
    }

}
