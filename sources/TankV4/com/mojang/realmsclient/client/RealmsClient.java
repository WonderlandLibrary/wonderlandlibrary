package com.mojang.realmsclient.client;

import com.google.gson.Gson;
import com.mojang.realmsclient.RealmsVersion;
import com.mojang.realmsclient.dto.BackupList;
import com.mojang.realmsclient.dto.Ops;
import com.mojang.realmsclient.dto.PendingInvitesList;
import com.mojang.realmsclient.dto.PingResult;
import com.mojang.realmsclient.dto.RealmsOptions;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsServerAddress;
import com.mojang.realmsclient.dto.RealmsServerList;
import com.mojang.realmsclient.dto.RealmsState;
import com.mojang.realmsclient.dto.ServerActivityList;
import com.mojang.realmsclient.dto.Subscription;
import com.mojang.realmsclient.dto.UploadInfo;
import com.mojang.realmsclient.dto.WorldTemplateList;
import com.mojang.realmsclient.exception.RealmsHttpException;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.exception.RetryCallException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsSharedConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsClient {
   private static final Logger LOGGER = LogManager.getLogger();
   private final String sessionId;
   private final String username;
   private static String baseUrl = "mcoapi.minecraft.net";
   private static final String WORLDS_RESOURCE_PATH = "worlds";
   private static final String INVITES_RESOURCE_PATH = "invites";
   private static final String MCO_RESOURCE_PATH = "mco";
   private static final String SUBSCRIPTION_RESOURCE = "subscriptions";
   private static final String ACTIVITIES_RESOURCE = "activities";
   private static final String OPS_RESOURCE = "ops";
   private static final String REGIONS_RESOURCE = "regions/ping/stat";
   private static final String PATH_INITIALIZE = "/$WORLD_ID/initialize";
   private static final String PATH_GET_ACTIVTIES = "/$WORLD_ID";
   private static final String PATH_GET_SUBSCRIPTION = "/$WORLD_ID";
   private static final String PATH_GET_MINIGAMES = "/minigames";
   private static final String PATH_GET_OPS = "/$WORLD_ID";
   private static final String PATH_OP = "/$WORLD_ID";
   private static final String PATH_PUT_INTO_MINIGAMES_MODE = "/minigames/$MINIGAME_ID/$WORLD_ID";
   private static final String PATH_PUT_INTO_NORMAL_MODE = "/minigames/$WORLD_ID";
   private static final String PATH_AVAILABLE = "/available";
   private static final String PATH_TEMPLATES = "/templates";
   private static final String PATH_WORLD_JOIN = "/$ID/join";
   private static final String PATH_WORLD_GET = "/$ID";
   private static final String PATH_WORLD_INVITES = "/$WORLD_ID/invite/$USER_NAME";
   private static final String PATH_WORLD_UNINVITE = "/$WORLD_ID/invite/$UUID";
   private static final String PATH_PENDING_INVITES_COUNT = "/count/pending";
   private static final String PATH_PENDING_INVITES = "/pending";
   private static final String PATH_ACCEPT_INVITE = "/accept/$INVITATION_ID";
   private static final String PATH_REJECT_INVITE = "/reject/$INVITATION_ID";
   private static final String PATH_UNINVITE_MYSELF = "/$WORLD_ID";
   private static final String PATH_WORLD_UPDATE = "/$WORLD_ID";
   private static final String PATH_WORLD_OPEN = "/$WORLD_ID/open";
   private static final String PATH_WORLD_CLOSE = "/$WORLD_ID/close";
   private static final String PATH_WORLD_RESET = "/$WORLD_ID/reset";
   private static final String PATH_WORLD_BACKUPS = "/$WORLD_ID/backups";
   private static final String PATH_WORLD_DOWNLOAD = "/$WORLD_ID/backups/download";
   private static final String PATH_WORLD_UPLOAD = "/$WORLD_ID/backups/upload";
   private static final String PATH_WORLD_UPLOAD_FINISHED = "/$WORLD_ID/backups/upload/finished";
   private static final String PATH_CLIENT_OUTDATED = "/client/outdated";
   private static final String PATH_TOS_AGREED = "/tos/agreed";
   private static final String PATH_MCO_BUY = "/buy";
   private static final String PATH_STAGE_AVAILABLE = "/stageAvailable";
   private static Gson gson = new Gson();

   public static RealmsClient createRealmsClient() {
      String var0 = Realms.userName();
      String var1 = Realms.sessionId();
      return var0 != null && var1 != null ? new RealmsClient(var1, var0, Realms.getProxy()) : null;
   }

   public static void switchToStage() {
      baseUrl = "mcoapi-stage.minecraft.net";
   }

   public static void switchToProd() {
      baseUrl = "mcoapi.minecraft.net";
   }

   public RealmsClient(String var1, String var2, Proxy var3) {
      this.sessionId = var1;
      this.username = var2;
      RealmsClientConfig.setProxy(var3);
   }

   public RealmsServerList listWorlds() throws RealmsServiceException, IOException {
      String var1 = this.url("worlds");
      String var2 = this.execute(Request.get(var1));
      return RealmsServerList.parse(var2);
   }

   public RealmsServer getOwnWorld(long var1) throws RealmsServiceException, IOException {
      String var3 = this.url("worlds" + "/$ID".replace("$ID", String.valueOf(var1)));
      String var4 = this.execute(Request.get(var3));
      return RealmsServer.parse(var4);
   }

   public ServerActivityList getActivity(long var1) throws RealmsServiceException {
      String var3 = this.url("activities" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(var1)));
      String var4 = this.execute(Request.get(var3));
      return ServerActivityList.parse(var4);
   }

   public RealmsServerAddress join(long var1) throws RealmsServiceException, IOException {
      String var3 = this.url("worlds" + "/$ID/join".replace("$ID", "" + var1));
      String var4 = this.execute(Request.get(var3, 5000, 30000));
      return RealmsServerAddress.parse(var4);
   }

   public void initializeWorld(long var1, String var3, String var4) throws RealmsServiceException, IOException {
      String var5 = QueryBuilder.of("name", var3).with("template", var4).toQueryString();
      String var6 = this.url("worlds" + "/$WORLD_ID/initialize".replace("$WORLD_ID", String.valueOf(var1)), var5);
      this.execute(Request.put(var6, "", 5000, 10000));
   }

   public Boolean mcoEnabled() throws RealmsServiceException, IOException {
      String var1 = this.url("mco/available");
      String var2 = this.execute(Request.get(var1));
      return Boolean.valueOf(var2);
   }

   public Boolean stageAvailable() throws RealmsServiceException, IOException {
      String var1 = this.url("mco/stageAvailable");
      String var2 = this.execute(Request.get(var1));
      return Boolean.valueOf(var2);
   }

   public Boolean clientOutdated() throws RealmsServiceException, IOException {
      String var1 = this.url("mco/client/outdated");
      String var2 = this.execute(Request.get(var1));
      return Boolean.valueOf(var2);
   }

   public void uninvite(long var1, String var3) throws RealmsServiceException {
      String var4 = this.url("invites" + "/$WORLD_ID/invite/$UUID".replace("$WORLD_ID", String.valueOf(var1)).replace("$UUID", var3));
      this.execute(Request.delete(var4));
   }

   public void uninviteMyselfFrom(long var1) throws RealmsServiceException {
      String var3 = this.url("invites" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(var1)));
      this.execute(Request.delete(var3));
   }

   public RealmsServer invite(long var1, String var3) throws RealmsServiceException, IOException {
      String var4 = this.url("invites" + "/$WORLD_ID/invite/$USER_NAME".replace("$WORLD_ID", String.valueOf(var1)).replace("$USER_NAME", var3));
      String var5 = this.execute(Request.post(var4, ""));
      return RealmsServer.parse(var5);
   }

   public BackupList backupsFor(long var1) throws RealmsServiceException {
      String var3 = this.url("worlds" + "/$WORLD_ID/backups".replace("$WORLD_ID", String.valueOf(var1)));
      String var4 = this.execute(Request.get(var3));
      return BackupList.parse(var4);
   }

   public void update(long var1, String var3, String var4, RealmsOptions var5) throws RealmsServiceException, UnsupportedEncodingException {
      QueryBuilder var6 = QueryBuilder.of("name", var3);
      if (var4 != null) {
         var6 = var6.with("motd", var4);
      }

      String var7 = var6.with("options", var5.toJson()).toQueryString();
      String var8 = this.url("worlds" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(var1)), var7);
      this.execute(Request.put(var8, ""));
   }

   public void restoreWorld(long var1, String var3) throws RealmsServiceException {
      String var4 = this.url("worlds" + "/$WORLD_ID/backups".replace("$WORLD_ID", String.valueOf(var1)), "backupId=" + var3);
      this.execute(Request.put(var4, "", 40000, 40000));
   }

   public WorldTemplateList fetchWorldTemplates() throws RealmsServiceException {
      String var1 = this.url("worlds/templates");
      String var2 = this.execute(Request.get(var1));
      return WorldTemplateList.parse(var2);
   }

   public WorldTemplateList fetchMinigames() throws RealmsServiceException {
      String var1 = this.url("worlds/minigames");
      String var2 = this.execute(Request.get(var1));
      return WorldTemplateList.parse(var2);
   }

   public Boolean putIntoMinigameMode(long var1, String var3) throws RealmsServiceException {
      String var4 = "/minigames/$MINIGAME_ID/$WORLD_ID".replace("$MINIGAME_ID", var3).replace("$WORLD_ID", String.valueOf(var1));
      String var5 = this.url("worlds" + var4);
      return Boolean.valueOf(this.execute(Request.put(var5, "")));
   }

   public Boolean putIntoNormalMode(long var1) throws RealmsServiceException {
      String var3 = "/minigames/$WORLD_ID".replace("$WORLD_ID", String.valueOf(var1));
      String var4 = this.url("worlds" + var3);
      return Boolean.valueOf(this.execute(Request.delete(var4)));
   }

   public Ops getOpsFor(long var1) throws RealmsServiceException {
      String var3 = "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(var1));
      String var4 = this.url("ops" + var3);
      return Ops.parse(this.execute(Request.get(var4)));
   }

   public Ops op(long var1, String var3) throws RealmsServiceException {
      String var4 = QueryBuilder.of("profileName", var3).toQueryString();
      String var5 = "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(var1));
      String var6 = this.url("ops" + var5, var4);
      return Ops.parse(this.execute(Request.post(var6, "")));
   }

   public Ops deop(long var1, String var3) throws RealmsServiceException {
      String var4 = QueryBuilder.of("profileName", var3).toQueryString();
      String var5 = "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(var1));
      String var6 = this.url("ops" + var5, var4);
      return Ops.parse(this.execute(Request.delete(var6)));
   }

   public Boolean open(long var1) throws RealmsServiceException, IOException {
      String var3 = this.url("worlds" + "/$WORLD_ID/open".replace("$WORLD_ID", String.valueOf(var1)));
      String var4 = this.execute(Request.put(var3, ""));
      return Boolean.valueOf(var4);
   }

   public Boolean close(long var1) throws RealmsServiceException, IOException {
      String var3 = this.url("worlds" + "/$WORLD_ID/close".replace("$WORLD_ID", String.valueOf(var1)));
      String var4 = this.execute(Request.put(var3, ""));
      return Boolean.valueOf(var4);
   }

   public Boolean resetWorldWithSeed(long var1, String var3, Integer var4, boolean var5) throws RealmsServiceException, IOException {
      QueryBuilder var6 = QueryBuilder.empty();
      if (var3 != null && var3.length() > 0) {
         var6 = var6.with("seed", var3);
      }

      var6 = var6.with((Object)"levelType", (Object)var4).with((Object)"generateStructures", (Object)var5);
      String var7 = this.url("worlds" + "/$WORLD_ID/reset".replace("$WORLD_ID", String.valueOf(var1)), var6.toQueryString());
      String var8 = this.execute(Request.put(var7, "", 30000, 80000));
      return Boolean.valueOf(var8);
   }

   public Boolean resetWorldWithTemplate(long var1, String var3) throws RealmsServiceException, IOException {
      QueryBuilder var4 = QueryBuilder.empty();
      if (var3 != null) {
         var4 = var4.with("template", var3);
      }

      String var5 = this.url("worlds" + "/$WORLD_ID/reset".replace("$WORLD_ID", String.valueOf(var1)), var4.toQueryString());
      String var6 = this.execute(Request.put(var5, "", 30000, 80000));
      return Boolean.valueOf(var6);
   }

   public Subscription subscriptionFor(long var1) throws RealmsServiceException, IOException {
      String var3 = this.url("subscriptions" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(var1)));
      String var4 = this.execute(Request.get(var3));
      return Subscription.parse(var4);
   }

   public int pendingInvitesCount() throws RealmsServiceException {
      String var1 = this.url("invites/count/pending");
      String var2 = this.execute(Request.get(var1));
      return Integer.parseInt(var2);
   }

   public PendingInvitesList pendingInvites() throws RealmsServiceException {
      String var1 = this.url("invites/pending");
      String var2 = this.execute(Request.get(var1));
      return PendingInvitesList.parse(var2);
   }

   public void acceptInvitation(String var1) throws RealmsServiceException {
      String var2 = this.url("invites" + "/accept/$INVITATION_ID".replace("$INVITATION_ID", var1));
      this.execute(Request.put(var2, ""));
   }

   public RealmsState fetchRealmsState() throws RealmsServiceException {
      String var1 = this.url("mco/buy");
      String var2 = this.execute(Request.get(var1));
      return RealmsState.parse(var2);
   }

   public String download(long var1) throws RealmsServiceException {
      String var3 = this.url("worlds" + "/$WORLD_ID/backups/download".replace("$WORLD_ID", String.valueOf(var1)));
      return this.execute(Request.get(var3));
   }

   public UploadInfo upload(long var1, String var3) throws RealmsServiceException {
      String var4 = this.url("worlds" + "/$WORLD_ID/backups/upload".replace("$WORLD_ID", String.valueOf(var1)));
      UploadInfo var5 = new UploadInfo();
      if (var3 != null) {
         var5.setToken(var3);
      }

      String var6 = gson.toJson((Object)var5);
      return UploadInfo.parse(this.execute(Request.put(var4, var6)));
   }

   public void uploadFinished(long var1) throws RealmsServiceException {
      String var3 = this.url("worlds" + "/$WORLD_ID/backups/upload/finished".replace("$WORLD_ID", String.valueOf(var1)));
      this.execute(Request.put(var3, ""));
   }

   public void rejectInvitation(String var1) throws RealmsServiceException {
      String var2 = this.url("invites" + "/reject/$INVITATION_ID".replace("$INVITATION_ID", var1));
      this.execute(Request.put(var2, ""));
   }

   public void agreeToTos() throws RealmsServiceException {
      String var1 = this.url("mco/tos/agreed");
      this.execute(Request.post(var1, ""));
   }

   public void sendPingResults(PingResult var1) throws RealmsServiceException {
      String var2 = this.url("regions/ping/stat");
      this.execute(Request.post(var2, gson.toJson((Object)var1)));
   }

   private String url(String var1) {
      return this.url(var1, (String)null);
   }

   private String url(String var1, String var2) {
      try {
         URI var3 = new URI("https", baseUrl, "/" + var1, var2, (String)null);
         return var3.toASCIIString();
      } catch (URISyntaxException var4) {
         var4.printStackTrace();
         return null;
      }
   }

   private String execute(Request var1) throws RealmsServiceException {
      var1.cookie("sid", this.sessionId);
      var1.cookie("user", this.username);
      var1.cookie("version", RealmsSharedConstants.VERSION_STRING);
      String var2 = RealmsVersion.getVersion();
      if (var2 != null) {
         var1.cookie("realms_version", var2);
      }

      try {
         int var3 = var1.responseCode();
         if (var3 == 503) {
            int var7 = var1.getRetryAfterHeader();
            throw new RetryCallException(var7);
         } else {
            String var4 = var1.text();
            if (var3 >= 200 && var3 < 300) {
               return var4;
            } else if (var3 == 401) {
               String var5 = var1.getHeader("WWW-Authenticate");
               LOGGER.info("Could not authorize you against Realms server: " + var5);
               throw new RealmsServiceException(var3, var5, -1, var5);
            } else if (var4 != null && var4.length() != 0) {
               throw new RealmsServiceException(var3, var4, new RealmsError(var4));
            } else {
               throw new RealmsServiceException(var3, var4, var3, "");
            }
         }
      } catch (RealmsHttpException var6) {
         throw new RealmsServiceException(500, "Could not connect to Realms: " + var6.getMessage(), -1, "");
      }
   }
}
