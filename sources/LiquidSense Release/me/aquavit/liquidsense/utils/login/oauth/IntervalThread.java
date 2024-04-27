package me.aquavit.liquidsense.utils.login.oauth;

import com.google.gson.JsonObject;
import me.aquavit.liquidsense.ui.client.gui.GuiAltManager;
import me.aquavit.liquidsense.utils.login.oauth.jsons.DeviceCodeJson;
import me.aquavit.liquidsense.utils.login.oauth.jsons.TokenJson;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;

public class IntervalThread extends Thread {
    public final String appID;

    public boolean isFinished = false;
    public boolean isFailed = false;

    public String user_code = "";
    public String verification_uri = "";

    public ErrorType type;
    public String msg = null;

    public String accessToken = null;
    public String refreshToken = null;

    public int intervalTime = 0;

    public IntervalThread(String appID) {
        this.appID = appID;
    }

    public void run() {
        type = ErrorType.QUERYING;
        try {
            HashMap<String, String> querys = new HashMap<>();
            querys.put("client_id", appID);
            querys.put("scope", "XboxLive.signin offline_access");
            String resultS = OAuthService.sendPost("https://login.microsoftonline.com/consumers/oauth2/v2.0/devicecode", querys);
            System.out.println(querys);
            System.out.println(resultS);
            DeviceCodeJson code = OAuthService.gson.fromJson(resultS, DeviceCodeJson.class);
            user_code = code.user_code;
            intervalTime = code.interval;
            String device_code = code.device_code;
            verification_uri = code.verification_uri;

            this.setToClipboard(user_code);
            msg = "The code has been copied to the clipboard! If not, use " + user_code + " as verify code!";
            OAuthService.openUrl(verification_uri);
            type = ErrorType.WAIT_FOR_USER;

            querys.clear();
            querys.put("grant_type", "urn:ietf:params:oauth:grant-type:device_code");
            querys.put("client_id", appID);
            querys.put("device_code", device_code);

            while (type != ErrorType.SUCCESS) {
                Thread.sleep(1000);
                if (!(Minecraft.getMinecraft().currentScreen instanceof GuiAltManager) && !this.isFinished) {
                    type = ErrorType.QUERY_FAILED;
                    break;
                }
                String resultStr = OAuthService.sendPost("https://login.microsoftonline.com/consumers/oauth2/v2.0/token", querys);
                JsonObject result = (JsonObject) OAuthService.parser.parse(resultStr);
                if (result.has("error")) {
                    switch (result.get("error").getAsString()) {
                        case "authorization_pending":
                            type = ErrorType.WAIT_FOR_USER;
                            break;

                        case "authorization_declined":
                            type = ErrorType.USER_DECLINED;
                            break;

                        case "bad_verification_code":
                            type = ErrorType.WRONG_DEVICE_CODE;
                            break;

                        case "expired_token":
                            type = ErrorType.TOKEN_EXPIRED;
                    }
                } else {
                    TokenJson token = OAuthService.gson.fromJson(resultStr, TokenJson.class);
                    accessToken = token.access_token;
                    refreshToken = token.refresh_token;
                    type = ErrorType.SUCCESS;
                }

                if (type == ErrorType.USER_DECLINED || type == ErrorType.WRONG_DEVICE_CODE || type == ErrorType.TOKEN_EXPIRED) {
                    break;
                }
            }

            if (type != ErrorType.SUCCESS) {
                isFailed = true;
                isFinished = true;
                msg = "Failed: " + type.name();
                return;
            }

            isFinished = true;
        } catch (Exception ex) {
            isFailed = true;
            msg = ex.getMessage();
        }

    }

    public void setToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}
