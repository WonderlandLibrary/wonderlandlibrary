package wtf.expensive.ui.alt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public class Account {

    public String accountName = "";
    public String accountPassword = "";

    public long dateAdded;

    public ResourceLocation skin;
    public float x,y;

    public Account(String accountName) {
        this.accountName = accountName;
        this.dateAdded = System.currentTimeMillis();
        UUID uuid = null;
        try {
            uuid = resolveUUID(accountName);
        } catch (IOException e) {
            uuid = UUID.randomUUID();
        }
        this.skin = DefaultPlayerSkin.getDefaultSkin(uuid);
        Minecraft.getInstance().getSkinManager().loadProfileTextures(new GameProfile(uuid, accountName), (type, loc, tex) -> {
            if (type == MinecraftProfileTexture.Type.SKIN) {
                skin = loc;
            }
        }, true);
    }


    public Account(String accountName, long dateAdded) {
        this.accountName = accountName;
        this.dateAdded = dateAdded;
        UUID uuid = null;
        try {
            uuid = resolveUUID(accountName);
        } catch (IOException e) {
            uuid = UUID.randomUUID();
        }
        this.skin = DefaultPlayerSkin.getDefaultSkin(uuid);
        Minecraft.getInstance().getSkinManager().loadProfileTextures(new GameProfile(uuid, accountName), (type, loc, tex) -> {
            if (type == MinecraftProfileTexture.Type.SKIN) {
                skin = loc;
            }
        }, true);
    }
    public static UUID resolveUUID(String name) throws IOException {
        UUID uUID;
        InputStreamReader in = new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream(), StandardCharsets.UTF_8);
        try {
            uUID = UUID.fromString(new Gson().fromJson(in, JsonObject.class).get("id").getAsString().replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        }
        catch (Throwable uuid) {
            try {
                try {
                    in.close();
                }
                catch (Throwable throwable) {
                    uuid.addSuppressed(throwable);
                }
                throw uuid;
            }
            catch (Throwable ignored) {
                return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
            }
        }
        in.close();
        return uUID;
    }

}
