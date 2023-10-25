package tech.drainwalk.client.profile;

public record Profile(String username, int uid, String subscriptionTill) {
    public String getAvatar() {
        return "Avatar";
    }
}
