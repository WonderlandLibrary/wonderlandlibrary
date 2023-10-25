/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.authlib;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.UserType;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.properties.PropertyMap;
import java.util.Map;

public interface UserAuthentication {
    public boolean canLogIn();

    public void logIn() throws AuthenticationException;

    public void logOut();

    public boolean isLoggedIn();

    public boolean canPlayOnline();

    public GameProfile[] getAvailableProfiles();

    public GameProfile getSelectedProfile();

    public void selectGameProfile(GameProfile var1) throws AuthenticationException;

    public void loadFromStorage(Map<String, Object> var1);

    public Map<String, Object> saveForStorage();

    public void setUsername(String var1);

    public void setPassword(String var1);

    public String getAuthenticatedToken();

    public String getUserID();

    public PropertyMap getUserProperties();

    public UserType getUserType();
}

