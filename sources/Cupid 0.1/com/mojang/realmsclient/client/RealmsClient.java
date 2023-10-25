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
  
  private static final String TRIALS_RESOURCE = "trial";
  
  private static final String PATH_INITIALIZE = "/$WORLD_ID/initialize";
  
  private static final String PATH_GET_ACTIVTIES = "/$WORLD_ID";
  
  private static final String PATH_GET_SUBSCRIPTION = "/$WORLD_ID";
  
  private static final String PATH_GET_MINIGAMES = "/minigames";
  
  private static final String PATH_OP = "/$WORLD_ID";
  
  private static final String PATH_PUT_INTO_MINIGAMES_MODE = "/minigames/$MINIGAME_ID/$WORLD_ID";
  
  private static final String PATH_AVAILABLE = "/available";
  
  private static final String PATH_TEMPLATES = "/templates";
  
  private static final String PATH_WORLD_JOIN = "/$ID/join";
  
  private static final String PATH_WORLD_GET = "/$ID";
  
  private static final String PATH_WORLD_INVITES = "/$WORLD_ID/invite";
  
  private static final String PATH_WORLD_UNINVITE = "/$WORLD_ID/invite/$UUID";
  
  private static final String PATH_PENDING_INVITES_COUNT = "/count/pending";
  
  private static final String PATH_PENDING_INVITES = "/pending";
  
  private static final String PATH_ACCEPT_INVITE = "/accept/$INVITATION_ID";
  
  private static final String PATH_REJECT_INVITE = "/reject/$INVITATION_ID";
  
  private static final String PATH_UNINVITE_MYSELF = "/$WORLD_ID";
  
  private static final String PATH_WORLD_UPDATE = "/$WORLD_ID";
  
  private static final String PATH_SLOT_UPDATE = "/$WORLD_ID/slot";
  
  private static final String PATH_SLOT_SWITCH = "/$WORLD_ID/slot/$SLOT_ID";
  
  private static final String PATH_WORLD_OPEN = "/$WORLD_ID/open";
  
  private static final String PATH_WORLD_CLOSE = "/$WORLD_ID/close";
  
  private static final String PATH_WORLD_RESET = "/$WORLD_ID/reset";
  
  private static final String PATH_DELETE_WORLD = "/$WORLD_ID";
  
  private static final String PATH_WORLD_BACKUPS = "/$WORLD_ID/backups";
  
  private static final String PATH_WORLD_DOWNLOAD = "/$WORLD_ID/backups/download";
  
  private static final String PATH_WORLD_UPLOAD = "/$WORLD_ID/backups/upload";
  
  private static final String PATH_WORLD_UPLOAD_FINISHED = "/$WORLD_ID/backups/upload/finished";
  
  private static final String PATH_WORLD_UPLOAD_CANCELLED = "/$WORLD_ID/backups/upload/cancelled";
  
  private static final String PATH_CLIENT_COMPATIBLE = "/client/compatible";
  
  private static final String PATH_TOS_AGREED = "/tos/agreed";
  
  private static final String PATH_MCO_BUY = "/buy";
  
  private static final String PATH_STAGE_AVAILABLE = "/stageAvailable";
  
  private static Gson gson = new Gson();
  
  public static RealmsClient createRealmsClient() {
    String username = Realms.userName();
    String sessionId = Realms.sessionId();
    if (username == null || sessionId == null)
      return null; 
    return new RealmsClient(sessionId, username, Realms.getProxy());
  }
  
  public static void switchToStage() {
    baseUrl = "mcoapi-stage.minecraft.net";
  }
  
  public static void switchToProd() {
    baseUrl = "mcoapi.minecraft.net";
  }
  
  public RealmsClient(String sessionId, String username, Proxy proxy) {
    this.sessionId = sessionId;
    this.username = username;
    RealmsClientConfig.setProxy(proxy);
  }
  
  public RealmsServerList listWorlds() throws RealmsServiceException, IOException {
    String asciiUrl = url("worlds");
    String json = execute(Request.get(asciiUrl));
    return RealmsServerList.parse(json);
  }
  
  public RealmsServer getOwnWorld(long worldId) throws RealmsServiceException, IOException {
    String asciiUrl = url("worlds" + "/$ID".replace("$ID", String.valueOf(worldId)));
    String json = execute(Request.get(asciiUrl));
    return RealmsServer.parse(json);
  }
  
  public ServerActivityList getActivity(long worldId) throws RealmsServiceException {
    String asciiUrl = url("activities" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(worldId)));
    String json = execute(Request.get(asciiUrl));
    return ServerActivityList.parse(json);
  }
  
  public RealmsServerAddress join(long worldId) throws RealmsServiceException, IOException {
    String asciiUrl = url("worlds" + "/$ID/join".replace("$ID", "" + worldId));
    String json = execute(Request.get(asciiUrl, 5000, 30000));
    return RealmsServerAddress.parse(json);
  }
  
  public void initializeWorld(long worldId, String name, String motd) throws RealmsServiceException, IOException {
    String queryString = QueryBuilder.of("name", name).with("motd", motd).toQueryString();
    String asciiUrl = url("worlds" + "/$WORLD_ID/initialize".replace("$WORLD_ID", String.valueOf(worldId)), queryString);
    execute(Request.put(asciiUrl, "", 5000, 10000));
  }
  
  public Boolean mcoEnabled() throws RealmsServiceException, IOException {
    String asciiUrl = url("mco/available");
    String json = execute(Request.get(asciiUrl));
    return Boolean.valueOf(json);
  }
  
  public Boolean stageAvailable() throws RealmsServiceException, IOException {
    String asciiUrl = url("mco/stageAvailable");
    String json = execute(Request.get(asciiUrl));
    return Boolean.valueOf(json);
  }
  
  public CompatibleVersionResponse clientCompatible() throws RealmsServiceException, IOException {
    CompatibleVersionResponse result;
    String asciiUrl = url("mco/client/compatible");
    String response = execute(Request.get(asciiUrl));
    try {
      result = CompatibleVersionResponse.valueOf(response);
    } catch (IllegalArgumentException e) {
      throw new RealmsServiceException(500, "Could not check compatible version, got response: " + response, -1, "");
    } 
    return result;
  }
  
  public void uninvite(long worldId, String profileUuid) throws RealmsServiceException {
    String asciiUrl = url("invites" + "/$WORLD_ID/invite/$UUID".replace("$WORLD_ID", String.valueOf(worldId)).replace("$UUID", profileUuid));
    execute(Request.delete(asciiUrl));
  }
  
  public void uninviteMyselfFrom(long worldId) throws RealmsServiceException {
    String asciiUrl = url("invites" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(worldId)));
    execute(Request.delete(asciiUrl));
  }
  
  public RealmsServer invite(long worldId, String profileName) throws RealmsServiceException, IOException {
    String queryString = QueryBuilder.of("profileName", profileName).toQueryString();
    String asciiUrl = url("invites" + "/$WORLD_ID/invite".replace("$WORLD_ID", String.valueOf(worldId)), queryString);
    String json = execute(Request.put(asciiUrl, ""));
    return RealmsServer.parse(json);
  }
  
  public BackupList backupsFor(long worldId) throws RealmsServiceException {
    String asciiUrl = url("worlds" + "/$WORLD_ID/backups".replace("$WORLD_ID", String.valueOf(worldId)));
    String json = execute(Request.get(asciiUrl));
    return BackupList.parse(json);
  }
  
  public void update(long worldId, String name, String motd) throws RealmsServiceException, UnsupportedEncodingException {
    QueryBuilder qb = QueryBuilder.of("name", name);
    if (motd != null)
      qb = qb.with("motd", motd); 
    String queryString = qb.toQueryString();
    String asciiUrl = url("worlds" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(worldId)), queryString);
    execute(Request.put(asciiUrl, ""));
  }
  
  public void updateSlot(long worldId, RealmsOptions options) throws RealmsServiceException, UnsupportedEncodingException {
    QueryBuilder qb = QueryBuilder.of("options", options.toJson());
    String queryString = qb.toQueryString();
    String asciiUrl = url("worlds" + "/$WORLD_ID/slot".replace("$WORLD_ID", String.valueOf(worldId)), queryString);
    execute(Request.put(asciiUrl, ""));
  }
  
  public boolean switchSlot(long worldId, int slot) throws RealmsServiceException {
    String asciiUrl = url("worlds" + "/$WORLD_ID/slot/$SLOT_ID".replace("$WORLD_ID", String.valueOf(worldId)).replace("$SLOT_ID", String.valueOf(slot)));
    String json = execute(Request.put(asciiUrl, ""));
    return Boolean.valueOf(json).booleanValue();
  }
  
  public void restoreWorld(long worldId, String backupId) throws RealmsServiceException {
    String asciiUrl = url("worlds" + "/$WORLD_ID/backups".replace("$WORLD_ID", String.valueOf(worldId)), "backupId=" + backupId);
    execute(Request.put(asciiUrl, "", 40000, 40000));
  }
  
  public WorldTemplateList fetchWorldTemplates() throws RealmsServiceException {
    String asciiUrl = url("worlds/templates");
    String json = execute(Request.get(asciiUrl));
    return WorldTemplateList.parse(json);
  }
  
  public WorldTemplateList fetchMinigames() throws RealmsServiceException {
    String asciiUrl = url("worlds/minigames");
    String json = execute(Request.get(asciiUrl));
    return WorldTemplateList.parse(json);
  }
  
  public Boolean putIntoMinigameMode(long worldId, String minigameId) throws RealmsServiceException {
    String path = "/minigames/$MINIGAME_ID/$WORLD_ID".replace("$MINIGAME_ID", minigameId).replace("$WORLD_ID", String.valueOf(worldId));
    String asciiUrl = url("worlds" + path);
    return Boolean.valueOf(execute(Request.put(asciiUrl, "")));
  }
  
  public Ops op(long worldId, String profileName) throws RealmsServiceException {
    String queryString = QueryBuilder.of("profileName", profileName).toQueryString();
    String path = "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(worldId));
    String asciiUrl = url("ops" + path, queryString);
    return Ops.parse(execute(Request.post(asciiUrl, "")));
  }
  
  public Ops deop(long worldId, String profileName) throws RealmsServiceException {
    String queryString = QueryBuilder.of("profileName", profileName).toQueryString();
    String path = "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(worldId));
    String asciiUrl = url("ops" + path, queryString);
    return Ops.parse(execute(Request.delete(asciiUrl)));
  }
  
  public Boolean open(long worldId) throws RealmsServiceException, IOException {
    String asciiUrl = url("worlds" + "/$WORLD_ID/open".replace("$WORLD_ID", String.valueOf(worldId)));
    String json = execute(Request.put(asciiUrl, ""));
    return Boolean.valueOf(json);
  }
  
  public Boolean close(long worldId) throws RealmsServiceException, IOException {
    String asciiUrl = url("worlds" + "/$WORLD_ID/close".replace("$WORLD_ID", String.valueOf(worldId)));
    String json = execute(Request.put(asciiUrl, ""));
    return Boolean.valueOf(json);
  }
  
  public Boolean resetWorldWithSeed(long worldId, String seed, Integer levelType, boolean generateStructures) throws RealmsServiceException, IOException {
    QueryBuilder qb = QueryBuilder.empty();
    if (seed != null && seed.length() > 0)
      qb = qb.with("seed", seed); 
    qb = qb.with("levelType", levelType).with("generateStructures", Boolean.valueOf(generateStructures));
    String asciiUrl = url("worlds" + "/$WORLD_ID/reset".replace("$WORLD_ID", String.valueOf(worldId)), qb.toQueryString());
    String json = execute(Request.put(asciiUrl, "", 30000, 80000));
    return Boolean.valueOf(json);
  }
  
  public Boolean resetWorldWithTemplate(long worldId, String worldTemplateId) throws RealmsServiceException, IOException {
    QueryBuilder qb = QueryBuilder.empty();
    if (worldTemplateId != null)
      qb = qb.with("template", worldTemplateId); 
    String asciiUrl = url("worlds" + "/$WORLD_ID/reset".replace("$WORLD_ID", String.valueOf(worldId)), qb.toQueryString());
    String json = execute(Request.put(asciiUrl, "", 30000, 80000));
    return Boolean.valueOf(json);
  }
  
  public Subscription subscriptionFor(long worldId) throws RealmsServiceException, IOException {
    String asciiUrl = url("subscriptions" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(worldId)));
    String json = execute(Request.get(asciiUrl));
    return Subscription.parse(json);
  }
  
  public int pendingInvitesCount() throws RealmsServiceException {
    String asciiUrl = url("invites/count/pending");
    String json = execute(Request.get(asciiUrl));
    return Integer.parseInt(json);
  }
  
  public PendingInvitesList pendingInvites() throws RealmsServiceException {
    String asciiUrl = url("invites/pending");
    String json = execute(Request.get(asciiUrl));
    return PendingInvitesList.parse(json);
  }
  
  public void acceptInvitation(String invitationId) throws RealmsServiceException {
    String asciiUrl = url("invites" + "/accept/$INVITATION_ID".replace("$INVITATION_ID", invitationId));
    execute(Request.put(asciiUrl, ""));
  }
  
  public RealmsState fetchRealmsState() throws RealmsServiceException {
    String asciiUrl = url("mco/buy");
    String json = execute(Request.get(asciiUrl));
    return RealmsState.parse(json);
  }
  
  public String download(long worldId) throws RealmsServiceException {
    String asciiUrl = url("worlds" + "/$WORLD_ID/backups/download".replace("$WORLD_ID", String.valueOf(worldId)));
    return execute(Request.get(asciiUrl));
  }
  
  public UploadInfo upload(long worldId, String uploadToken) throws RealmsServiceException {
    String asciiUrl = url("worlds" + "/$WORLD_ID/backups/upload".replace("$WORLD_ID", String.valueOf(worldId)));
    UploadInfo oldUploadInfo = new UploadInfo();
    if (uploadToken != null)
      oldUploadInfo.setToken(uploadToken); 
    String content = gson.toJson(oldUploadInfo);
    return UploadInfo.parse(execute(Request.put(asciiUrl, content)));
  }
  
  public void uploadCancelled(long worldId, String uploadToken) throws RealmsServiceException {
    String asciiUrl = url("worlds" + "/$WORLD_ID/backups/upload/cancelled".replace("$WORLD_ID", String.valueOf(worldId)));
    UploadInfo oldUploadInfo = new UploadInfo();
    oldUploadInfo.setToken(uploadToken);
    String content = gson.toJson(oldUploadInfo);
    execute(Request.put(asciiUrl, content));
  }
  
  public void uploadFinished(long worldId) throws RealmsServiceException {
    String asciiUrl = url("worlds" + "/$WORLD_ID/backups/upload/finished".replace("$WORLD_ID", String.valueOf(worldId)));
    execute(Request.put(asciiUrl, ""));
  }
  
  public void rejectInvitation(String invitationId) throws RealmsServiceException {
    String asciiUrl = url("invites" + "/reject/$INVITATION_ID".replace("$INVITATION_ID", invitationId));
    execute(Request.put(asciiUrl, ""));
  }
  
  public void agreeToTos() throws RealmsServiceException {
    String asciiUrl = url("mco/tos/agreed");
    execute(Request.post(asciiUrl, ""));
  }
  
  public void sendPingResults(PingResult pingResult) throws RealmsServiceException {
    String asciiUrl = url("regions/ping/stat");
    execute(Request.post(asciiUrl, gson.toJson(pingResult)));
  }
  
  public Boolean trialAvailable() throws RealmsServiceException, IOException {
    String asciiUrl = url("trial");
    String json = execute(Request.get(asciiUrl));
    return Boolean.valueOf(json);
  }
  
  public Boolean createTrial() throws RealmsServiceException, IOException {
    String asciiUrl = url("trial");
    String json = execute(Request.put(asciiUrl, ""));
    return Boolean.valueOf(json);
  }
  
  public void deleteWorld(long worldId) throws RealmsServiceException, IOException {
    String asciiUrl = url("worlds" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(worldId)));
    execute(Request.delete(asciiUrl));
  }
  
  private String url(String path) {
    return url(path, null);
  }
  
  private String url(String path, String queryString) {
    try {
      URI uri = new URI("https", baseUrl, "/" + path, queryString, null);
      return uri.toASCIIString();
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  private String execute(Request<?> r) throws RealmsServiceException {
    r.cookie("sid", this.sessionId);
    r.cookie("user", this.username);
    r.cookie("version", RealmsSharedConstants.VERSION_STRING);
    String realmsVersion = RealmsVersion.getVersion();
    if (realmsVersion != null)
      r.cookie("realms_version", realmsVersion); 
    try {
      int responseCode = r.responseCode();
      if (responseCode == 503) {
        int pauseTime = r.getRetryAfterHeader();
        throw new RetryCallException(pauseTime);
      } 
      String responseText = r.text();
      if (responseCode < 200 || responseCode >= 300) {
        if (responseCode == 401) {
          String authenticationHeader = r.getHeader("WWW-Authenticate");
          LOGGER.info("Could not authorize you against Realms server: " + authenticationHeader);
          throw new RealmsServiceException(responseCode, authenticationHeader, -1, authenticationHeader);
        } 
        if (responseText == null || responseText.length() == 0) {
          LOGGER.error("Realms error code: " + responseCode + " message: " + responseText);
          throw new RealmsServiceException(responseCode, responseText, responseCode, "");
        } 
        RealmsError error = new RealmsError(responseText);
        LOGGER.error("Realms http code: " + responseCode + " -  error code: " + error.getErrorCode() + " -  message: " + error.getErrorMessage());
        throw new RealmsServiceException(responseCode, responseText, error);
      } 
      return responseText;
    } catch (RealmsHttpException e) {
      throw new RealmsServiceException(500, "Could not connect to Realms: " + e.getMessage(), -1, "");
    } 
  }
  
  public enum CompatibleVersionResponse {
    COMPATIBLE, OUTDATED, OTHER;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\client\RealmsClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */