package me.yarukon.oauth;

import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonPrimitive;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.yarukon.oauth.web.HTTPServer;

public class OAuthService {
	private final CloseableHttpClient httpclient = HttpClients.createDefault();
	public final JsonParser parser = new JsonParser();
	
	private final String appID = "efb38d0c-09a9-4752-accd-d984962d5fd3";
	private final String appSecret = "Wxd7Q~ml4ExMxw3D44kFBqZ7KdG7NWD2XYVl0";

	public String message = "";
	public Session session;
	public void authWithNoRefreshToken() {
		authenticate(null);
	}

	public void authenticate(String refreshToken) {
		int port = 12220;

		//打开授权链接
		message = "Starting to authenticate";
		HTTPServer srv = null;
		if(refreshToken == null) {
			this.openUrl("https://login.live.com/oauth20_authorize.srf?response_type=code&client_id=" + appID + "&redirect_uri=http://localhost:12220/auth-response&scope=XboxLive.signin+offline_access");
			
			//等待网站授权
			message = "Waiting for user to authorize...";
			srv = new HTTPServer(port);
			srv.await();

			message = "Authorization Code -> Authorization Token";
		}
		
		//授权码 -> 授权令牌
		String[] live = this.liveAuth(refreshToken != null ? refreshToken : srv.token, refreshToken != null);
		if(!live[0].equals("FAILED")) {
			message = "Authenticating with XBox live...";

			//XBL身份验证
			String[] xbox = this.xBoxAuth(live[0]);
			if(!xbox[0].equals("FAILED")) {
				message = "Authenticating with XSTS...";

				//XSTS身份验证
				String[] xsts = this.xstsAuth(xbox[0]);
				if(!xsts[0].equals("FAILED")) {
					message = "Authenticating with Minecraft API...";

					//Minecraft身份验证
					String[] mc_accessToken = this.minecraftAuth(xsts[0], xsts[1]);
					if(!mc_accessToken[0].equals("FAILED")) {
						message = "Obtaining user profile with Minecraft API...";

						//获取玩家档案
						String[] mc_userInfo = this.obtainUUID(mc_accessToken[1]);
						if(!mc_userInfo[0].equals("FAILED")) {
							this.session = new Session(mc_userInfo[1], mc_userInfo[0], mc_accessToken[1], "mojang");
							message = "Successfully login with account " + this.session.getUsername();
							Minecraft.getMinecraft().session = this.session;
						} else {
							message = "Failed to obtain user profile!";
						}
					} else {
						message = "Authentication with Minecraft API failed!";
					}
				} else {
					message = "Authentication with XSTS failed!";
				}
			} else {
				message = "Authentication with XBox live failed!";
			}
		} else {
			message = "Authentication with live failed!";
		}
	}
	
	public void openUrl(String url) {
		try {
			Desktop d = Desktop.getDesktop();
			d.browse(new URI(url));
		} catch (Exception ex) {
			System.out.println("Failed to open url!");
		}
	}
	
	public String[] obtainUUID(String mc_accessToken) {
		String resultJson = "UNKNOWN";
		try {
			resultJson = sendGet("https://api.minecraftservices.com/minecraft/profile", mc_accessToken);
			JsonObject result = (JsonObject) parser.parse(resultJson);
			return new String[] {result.get("id").getAsString(), result.get("name").getAsString()};
		} catch (Exception ex) {
			return new String[] {"FAILED", resultJson};
		}
	}
	
	public String[] minecraftAuth(String xbl_token, String uhs) {
		JsonObject obj = new JsonObject();
		obj.addProperty("identityToken", "XBL3.0 x=" + uhs + ";" + xbl_token);
		
		String resultJson = "UNKNOWN";
		try {
			resultJson = sendPost("https://api.minecraftservices.com/authentication/login_with_xbox", obj.toString());
			JsonObject result = (JsonObject) parser.parse(resultJson);
			return new String[] {"SUCCESS", result.get("access_token").getAsString()};
		} catch (Exception ex) {
			return new String[] {"FAILED", resultJson};
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
			return new String[] {result.get("Token").getAsString(), result.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString()};
		} catch (Exception ex) {
			return new String[] {"FAILED", resultJson};
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
			return new String[] {result.get("Token").getAsString(), result.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString()};
		} catch (Exception ex) {
			return new String[] {"FAILED", resultJson};
		}
	}
	
	public String[] liveAuth(String authCode, boolean isRefresh) {
		HashMap<String, String> map = new HashMap<>();
		map.put("client_id", appID);
		
		if(isRefresh)
			map.put("refresh_token", authCode);
		else
			map.put("code", authCode);
		
		map.put("grant_type", isRefresh ? "refresh_token" : "authorization_code");
		map.put("redirect_uri", "http://localhost:12220/auth-response");
		map.put("scope", "XboxLive.signin offline_access");
		map.put("client_secret", appSecret);
		
		String resultJson = "UNKNOWN";
		try {
			resultJson = sendPost("https://login.live.com/oauth20_token.srf", map);
			JsonObject obj = (JsonObject) parser.parse(resultJson);
			return new String[] {obj.get("access_token").getAsString(), obj.get("refresh_token").getAsString()};
		} catch (Exception ex) {
			return new String[] {"FAILED", resultJson};
		}
	}

	public String sendPost(String url, Map<String, String> map) throws Exception {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		HttpPost httppost = new HttpPost(url);
		
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httppost.setEntity(entity);
		
		CloseableHttpResponse response = null;
		response = httpclient.execute(httppost);
		HttpEntity entity1 = response.getEntity();
		String result = EntityUtils.toString(entity1);
		
		return result;
	}
	
	public String sendPost(String url, String value) throws Exception {
		StringEntity entity = new StringEntity(value, "UTF-8");
		HttpPost httppost = new HttpPost(url);
		
		httppost.setHeader("Content-Type", "application/json");
		httppost.setHeader("Accept", "application/json");
		httppost.setEntity(entity);
		
		CloseableHttpResponse response = null;
		response = httpclient.execute(httppost);
		HttpEntity entity1 = response.getEntity();
		String result = null;
		result = EntityUtils.toString(entity1);
		
		return result;
	}
	
	public String sendGet(String url, String header) throws Exception {
		HttpGet httpGet = new HttpGet(url);
		
		httpGet.setHeader("Authorization", "Bearer " + header);
		
		CloseableHttpResponse response = null;
		response = httpclient.execute(httpGet);
		HttpEntity entity1 = response.getEntity();
		String result = null;
		result = EntityUtils.toString(entity1);

		return result;
	}
	
}
