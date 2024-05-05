/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  kotlin.TypeCastException
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.cape;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IThreadDownloadImageData;
import net.ccbluex.liquidbounce.api.minecraft.client.render.WIImageBuffer;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.cape.CapeInfo;
import net.ccbluex.liquidbounce.cape.CapeService;
import net.ccbluex.liquidbounce.cape.ServiceAPI;
import net.ccbluex.liquidbounce.cape.ServiceList;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import org.jetbrains.annotations.Nullable;

public final class CapeAPI
extends MinecraftInstance {
    private static CapeService capeService;
    public static final CapeAPI INSTANCE;

    /*
     * WARNING - void declaration
     */
    public final void registerCapeService() {
        String serviceType;
        JsonObject jsonObject = new JsonParser().parse(HttpUtils.get("https://cloud.liquidbounce.net/LiquidBounce/capes.json")).getAsJsonObject();
        String string = serviceType = jsonObject.get("serviceType").getAsString();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "api": {
                String url = jsonObject.get("api").getAsJsonObject().get("url").getAsString();
                capeService = new ServiceAPI(url);
                ClientUtils.getLogger().info("Registered " + url + " as '" + serviceType + "' service type.");
                break;
            }
            case "list": {
                HashMap users = new HashMap();
                Iterator iterator = jsonObject.get("users").getAsJsonObject().entrySet().iterator();
                while (iterator.hasNext()) {
                    void key;
                    Map.Entry entry;
                    Map.Entry entry2 = entry = (Map.Entry)iterator.next();
                    boolean bl2 = false;
                    String string3 = (String)entry2.getKey();
                    entry2 = entry;
                    bl2 = false;
                    JsonElement value = (JsonElement)entry2.getValue();
                    ((Map)users).put(key, value.getAsString());
                    ClientUtils.getLogger().info("Loaded user cape for '" + (String)key + "'.");
                }
                capeService = new ServiceList(users);
                ClientUtils.getLogger().info("Registered '" + serviceType + "' service type.");
            }
        }
        ClientUtils.getLogger().info("Loaded.");
    }

    public final CapeInfo loadCape(UUID uuid) {
        CapeService capeService = CapeAPI.capeService;
        if (capeService == null) {
            return null;
        }
        String string = capeService.getCape(uuid);
        if (string == null) {
            return null;
        }
        String url = string;
        String string2 = "capes/%s.png";
        Object[] objectArray = new Object[]{uuid.toString()};
        IClassProvider iClassProvider = LiquidBounce.INSTANCE.getWrapper().getClassProvider();
        boolean bl = false;
        String string3 = String.format(string2, Arrays.copyOf(objectArray, objectArray.length));
        IResourceLocation resourceLocation = iClassProvider.createResourceLocation(string3);
        CapeInfo capeInfo = new CapeInfo(resourceLocation, false, 2, null);
        IThreadDownloadImageData threadDownloadImageData2 = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createThreadDownloadImageData(null, url, null, new WIImageBuffer(capeInfo){
            final /* synthetic */ CapeInfo $capeInfo;

            public BufferedImage parseUserSkin(@Nullable BufferedImage image2) {
                return image2;
            }

            public void skinAvailable() {
                this.$capeInfo.setCapeAvailable(true);
            }
            {
                this.$capeInfo = $captured_local_variable$0;
            }
        });
        MinecraftInstance.mc.getTextureManager().loadTexture(resourceLocation, threadDownloadImageData2);
        return capeInfo;
    }

    public final boolean hasCapeService() {
        return capeService != null;
    }

    private CapeAPI() {
    }

    static {
        CapeAPI capeAPI;
        INSTANCE = capeAPI = new CapeAPI();
    }
}

