// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.main;

import java.net.Proxy;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.util.Session;
import net.minecraft.client.resources.ResourceIndexFolder;
import net.minecraft.client.resources.ResourceIndex;
import javax.annotation.Nullable;
import java.io.File;

public class GameConfiguration
{
    public final UserInformation userInfo;
    public final DisplayInformation displayInfo;
    public final FolderInformation folderInfo;
    public final GameInformation gameInfo;
    public final ServerInformation serverInfo;
    
    public GameConfiguration(final UserInformation userInfoIn, final DisplayInformation displayInfoIn, final FolderInformation folderInfoIn, final GameInformation gameInfoIn, final ServerInformation serverInfoIn) {
        this.userInfo = userInfoIn;
        this.displayInfo = displayInfoIn;
        this.folderInfo = folderInfoIn;
        this.gameInfo = gameInfoIn;
        this.serverInfo = serverInfoIn;
    }
    
    public static class DisplayInformation
    {
        public final int width;
        public final int height;
        public final boolean fullscreen;
        public final boolean checkGlErrors;
        
        public DisplayInformation(final int widthIn, final int heightIn, final boolean fullscreenIn, final boolean checkGlErrorsIn) {
            this.width = widthIn;
            this.height = heightIn;
            this.fullscreen = fullscreenIn;
            this.checkGlErrors = checkGlErrorsIn;
        }
    }
    
    public static class FolderInformation
    {
        public final File gameDir;
        public final File resourcePacksDir;
        public final File assetsDir;
        public final String assetIndex;
        
        public FolderInformation(final File mcDataDirIn, final File resourcePacksDirIn, final File assetsDirIn, @Nullable final String assetIndexIn) {
            this.gameDir = mcDataDirIn;
            this.resourcePacksDir = resourcePacksDirIn;
            this.assetsDir = assetsDirIn;
            this.assetIndex = assetIndexIn;
        }
        
        public ResourceIndex getAssetsIndex() {
            return (this.assetIndex == null) ? new ResourceIndexFolder(this.assetsDir) : new ResourceIndex(this.assetsDir, this.assetIndex);
        }
    }
    
    public static class GameInformation
    {
        public final boolean isDemo;
        public final String version;
        public final String versionType;
        
        public GameInformation(final boolean demo, final String versionIn, final String versionTypeIn) {
            this.isDemo = demo;
            this.version = versionIn;
            this.versionType = versionTypeIn;
        }
    }
    
    public static class ServerInformation
    {
        public final String serverName;
        public final int serverPort;
        
        public ServerInformation(final String serverNameIn, final int serverPortIn) {
            this.serverName = serverNameIn;
            this.serverPort = serverPortIn;
        }
    }
    
    public static class UserInformation
    {
        public final Session session;
        public final PropertyMap userProperties;
        public final PropertyMap profileProperties;
        public final Proxy proxy;
        
        public UserInformation(final Session sessionIn, final PropertyMap userPropertiesIn, final PropertyMap profilePropertiesIn, final Proxy proxyIn) {
            this.session = sessionIn;
            this.userProperties = userPropertiesIn;
            this.profileProperties = profilePropertiesIn;
            this.proxy = proxyIn;
        }
    }
}
