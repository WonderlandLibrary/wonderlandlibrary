package best.azura.client.api.account;

public class BannedData {
    private String serverIp;
    private String bannedDate;
    private String banDuration;
    private long playTime;
    public BannedData(String serverIp, String bannedDate, String banDuration, long playTime) {
        this.serverIp = serverIp;
        this.bannedDate = bannedDate;
        this.banDuration = banDuration;
        this.playTime = playTime;
    }
    public BannedData(String serverIp, String bannedDate, String banDuration) {
        this(serverIp, bannedDate, banDuration, -1);
    }
    public BannedData(String serverIp, String bannedDate, long playTime) {
        this(serverIp, bannedDate, null, playTime);
    }
    public BannedData(String serverIp, String bannedDate) {
        this(serverIp, bannedDate, null, -1);
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getBannedDate() {
        return bannedDate;
    }

    public void setBannedDate(String bannedDate) {
        this.bannedDate = bannedDate;
    }

    public String getBanDuration() {
        return banDuration;
    }

    public void setBanDuration(String banDuration) {
        this.banDuration = banDuration;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }
}