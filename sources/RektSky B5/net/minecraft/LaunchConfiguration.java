/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft;

import com.mojang.authlib.properties.PropertyMap;
import java.io.File;

public class LaunchConfiguration {
    private String proxyHost = null;
    private String proxyUser = null;
    private String proxyPassword = null;
    private Integer width = 1280;
    private Integer height = 1024;
    private boolean fullscreen = false;
    private boolean checkGlErrors = false;
    private boolean demo = false;
    private String version = "RektSky";
    private PropertyMap userProperties = new PropertyMap();
    private PropertyMap profileProperties = new PropertyMap();
    private File gameDir = new File(".");
    private File assetsDir = new File(this.gameDir, "assets/");
    private File resourcePackDir = new File(this.gameDir, "resourcepacks/");
    private String username = null;
    private String uuid = null;
    private String assetIndex = "1.8";
    private String server = null;
    private Integer port = 25565;
    private String accessToken = "0";
    private String userType = null;
    private Integer proxyPort = null;

    public LaunchConfiguration(String username, String userType) {
        this.username = username;
        this.userType = userType;
    }

    public LaunchConfiguration setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    public LaunchConfiguration setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
        return this;
    }

    public LaunchConfiguration setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
        return this;
    }

    public LaunchConfiguration setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public LaunchConfiguration setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public LaunchConfiguration setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        return this;
    }

    public LaunchConfiguration setCheckGlErrors(boolean checkGlErrors) {
        this.checkGlErrors = checkGlErrors;
        return this;
    }

    public LaunchConfiguration setDemo(boolean demo) {
        this.demo = demo;
        return this;
    }

    public LaunchConfiguration setVersion(String version) {
        this.version = version;
        return this;
    }

    public LaunchConfiguration setUserProperties(PropertyMap userProperties) {
        this.userProperties = userProperties;
        return this;
    }

    public LaunchConfiguration setProfileProperties(PropertyMap profileProperties) {
        this.profileProperties = profileProperties;
        return this;
    }

    public LaunchConfiguration setGameDir(File gameDir) {
        this.gameDir = gameDir;
        return this;
    }

    public LaunchConfiguration setAssetsDir(File assetsDir) {
        this.assetsDir = assetsDir;
        return this;
    }

    public LaunchConfiguration setResourcePackDir(File resourcePackDir) {
        this.resourcePackDir = resourcePackDir;
        return this;
    }

    public LaunchConfiguration setUsername(String username) {
        this.username = username;
        return this;
    }

    public LaunchConfiguration setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public LaunchConfiguration setAssetIndex(String assetIndex) {
        this.assetIndex = assetIndex;
        return this;
    }

    public LaunchConfiguration setServer(String server) {
        this.server = server;
        return this;
    }

    public LaunchConfiguration setPort(Integer port) {
        this.port = port;
        return this;
    }

    public LaunchConfiguration setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public LaunchConfiguration setUserType(String userType) {
        this.userType = userType;
        return this;
    }

    public LaunchConfiguration setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    public Integer getProxyPort() {
        return this.proxyPort;
    }

    public String getProxyHost() {
        return this.proxyHost;
    }

    public String getProxyUser() {
        return this.proxyUser;
    }

    public String getProxyPassword() {
        return this.proxyPassword;
    }

    public Integer getWidth() {
        return this.width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public boolean isFullscreen() {
        return this.fullscreen;
    }

    public boolean isCheckGlErrors() {
        return this.checkGlErrors;
    }

    public boolean isDemo() {
        return this.demo;
    }

    public String getVersion() {
        return this.version;
    }

    public PropertyMap getUserProperties() {
        return this.userProperties;
    }

    public PropertyMap getProfileProperties() {
        return this.profileProperties;
    }

    public File getGameDir() {
        return this.gameDir;
    }

    public File getAssetsDir() {
        return this.assetsDir;
    }

    public File getResourcePackDir() {
        return this.resourcePackDir;
    }

    public String getUsername() {
        return this.username;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getAssetIndex() {
        return this.assetIndex;
    }

    public String getServer() {
        return this.server;
    }

    public Integer getPort() {
        return this.port;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getUserType() {
        return this.userType;
    }
}

