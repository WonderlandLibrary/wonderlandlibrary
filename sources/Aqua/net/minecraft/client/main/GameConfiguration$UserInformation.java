package net.minecraft.client.main;

import com.mojang.authlib.properties.PropertyMap;
import java.net.Proxy;
import net.minecraft.util.Session;

public static class GameConfiguration.UserInformation {
    public final Session session;
    public final PropertyMap userProperties;
    public final PropertyMap profileProperties;
    public final Proxy proxy;

    public GameConfiguration.UserInformation(Session sessionIn, PropertyMap userPropertiesIn, PropertyMap profilePropertiesIn, Proxy proxyIn) {
        this.session = sessionIn;
        this.userProperties = userPropertiesIn;
        this.profileProperties = profilePropertiesIn;
        this.proxy = proxyIn;
    }
}
