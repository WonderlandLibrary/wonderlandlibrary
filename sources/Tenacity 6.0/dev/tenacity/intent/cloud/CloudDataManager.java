// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.intent.cloud;

import com.google.gson.JsonObject;
import java.util.Iterator;
import com.google.gson.JsonArray;
import dev.tenacity.ui.sidegui.utils.CloudDataUtils;
import dev.tenacity.utils.client.ReleaseType;
import com.google.gson.JsonElement;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.Tenacity;
import java.util.HashMap;
import java.util.ArrayList;
import dev.tenacity.intent.cloud.data.Votes;
import java.util.Map;
import dev.tenacity.intent.cloud.data.CloudScript;
import dev.tenacity.intent.cloud.data.CloudConfig;
import java.util.List;

public class CloudDataManager
{
    private final List<CloudConfig> cloudConfigs;
    private final List<CloudScript> cloudScripts;
    private boolean refreshing;
    private final Map<String, Votes> voteMap;
    private final Map<String, Boolean> userVoteMap;
    private boolean refreshedVotes;
    
    public CloudDataManager() {
        this.cloudConfigs = new ArrayList<CloudConfig>();
        this.cloudScripts = new ArrayList<CloudScript>();
        this.refreshing = false;
        this.voteMap = new HashMap<String, Votes>();
        this.userVoteMap = new HashMap<String, Boolean>();
        this.refreshedVotes = false;
    }
    
    public void refreshData() {
        this.refreshing = true;
        Tenacity.INSTANCE.getSideGui().getTooltips().clear();
        this.cloudConfigs.clear();
        this.cloudScripts.clear();
        this.refreshVotes();
        final JsonArray dataArray = CloudUtils.listAllData();
        if (dataArray == null || dataArray.size() == 0) {
            System.err.println("Null or no data found in cloud");
            NotificationManager.post(NotificationType.DISABLE, "Cloud Configs", "Failed to retrieve config data");
            return;
        }
        for (final JsonElement element : dataArray) {
            final JsonObject data = element.getAsJsonObject();
            final String name = data.get("name").getAsString();
            final String description = data.get("description").getAsString();
            final String shareCode = data.get("share_code").getAsString();
            final String author = data.get("author_username").getAsString();
            final String[] meta = data.get("meta").getAsString().split(":");
            final String lastUpdated = data.get("edit_epoch").getAsString();
            final String server = data.get("server").getAsString();
            final boolean ownership = data.get("mine").getAsBoolean() || Tenacity.RELEASE.equals(ReleaseType.DEV);
            final boolean script = Boolean.parseBoolean(meta[1]);
            final String version = meta[3];
            if (script) {
                this.cloudScripts.add(new CloudScript(name, description, shareCode, author, version, lastUpdated, ownership));
            }
            else {
                this.cloudConfigs.add(new CloudConfig(name, description, shareCode, author, version, lastUpdated, server, ownership));
            }
        }
        this.applyVotes();
        CloudDataUtils.refreshCloud();
        this.refreshing = false;
    }
    
    public void refreshVotes() {
        final JsonObject voteData = CloudUtils.getVoteData();
        if (voteData == null) {
            System.err.println("Failed to retrieve voting data");
            NotificationManager.post(NotificationType.DISABLE, "Cloud Configs", "Failed to retrieve voting data");
            this.refreshedVotes = false;
            return;
        }
        this.voteMap.clear();
        this.userVoteMap.clear();
        if (voteData.has("configs") && !voteData.get("configs").toString().equals("[]")) {
            final JsonObject obj;
            voteData.getAsJsonObject("configs").entrySet().forEach(entry -> {
                obj = entry.getValue().getAsJsonObject();
                this.voteMap.put((String)entry.getKey(), new Votes(obj.get("upvotes").getAsInt(), obj.get("downvotes").getAsInt()));
                return;
            });
            if (voteData.has("voted")) {
                for (final JsonElement el : voteData.getAsJsonArray("voted")) {
                    final JsonObject obj2 = el.getAsJsonObject();
                    this.userVoteMap.put(obj2.get("share_code").getAsString(), obj2.get("upvote").getAsBoolean());
                }
            }
        }
        this.refreshedVotes = true;
    }
    
    public void applyVotes() {
        if (this.refreshedVotes) {
            final String shareCode;
            final Votes v;
            this.cloudConfigs.forEach(c -> {
                shareCode = c.getShareCode();
                v = this.voteMap.get(shareCode);
                if (v != null) {
                    c.setVotes(v);
                    if (this.userVoteMap.containsKey(shareCode)) {
                        c.forceSet(this.userVoteMap.get(shareCode));
                    }
                }
                else {
                    c.setVotes(new Votes(0, 0));
                }
                return;
            });
            final String shareCode2;
            final Votes v2;
            this.cloudScripts.forEach(c -> {
                shareCode2 = c.getShareCode();
                v2 = this.voteMap.get(shareCode2);
                if (v2 != null) {
                    c.setVotes(v2);
                    if (this.userVoteMap.containsKey(shareCode2)) {
                        c.forceSet(this.userVoteMap.get(shareCode2));
                    }
                }
                else {
                    c.setVotes(new Votes(0, 0));
                }
                return;
            });
            this.refreshedVotes = false;
        }
    }
    
    public List<CloudConfig> getCloudConfigs() {
        return this.cloudConfigs;
    }
    
    public List<CloudScript> getCloudScripts() {
        return this.cloudScripts;
    }
    
    public boolean isRefreshing() {
        return this.refreshing;
    }
    
    public Map<String, Votes> getVoteMap() {
        return this.voteMap;
    }
    
    public Map<String, Boolean> getUserVoteMap() {
        return this.userVoteMap;
    }
    
    public boolean isRefreshedVotes() {
        return this.refreshedVotes;
    }
}
