package com.mojang.authlib;

import com.google.common.collect.Multimap;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.util.UUIDTypeAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BaseUserAuthentication implements UserAuthentication {
  private static final Logger LOGGER = LogManager.getLogger();
  
  protected static final String STORAGE_KEY_PROFILE_NAME = "displayName";
  
  protected static final String STORAGE_KEY_PROFILE_ID = "uuid";
  
  protected static final String STORAGE_KEY_PROFILE_PROPERTIES = "profileProperties";
  
  protected static final String STORAGE_KEY_USER_NAME = "username";
  
  protected static final String STORAGE_KEY_USER_ID = "userid";
  
  protected static final String STORAGE_KEY_USER_PROPERTIES = "userProperties";
  
  private final AuthenticationService authenticationService;
  
  private final PropertyMap userProperties = new PropertyMap();
  
  private String userid;
  
  private String username;
  
  private String password;
  
  private GameProfile selectedProfile;
  
  private UserType userType;
  
  protected BaseUserAuthentication(AuthenticationService authenticationService) {
    Validate.notNull(authenticationService);
    this.authenticationService = authenticationService;
  }
  
  public boolean canLogIn() {
    return (!canPlayOnline() && StringUtils.isNotBlank(getUsername()) && StringUtils.isNotBlank(getPassword()));
  }
  
  public void logOut() {
    this.password = null;
    this.userid = null;
    setSelectedProfile(null);
    getModifiableUserProperties().clear();
    setUserType(null);
  }
  
  public boolean isLoggedIn() {
    return (getSelectedProfile() != null);
  }
  
  public void setUsername(String username) {
    if (isLoggedIn() && canPlayOnline())
      throw new IllegalStateException("Cannot change username whilst logged in & online"); 
    this.username = username;
  }
  
  public void setPassword(String password) {
    if (isLoggedIn() && canPlayOnline() && StringUtils.isNotBlank(password))
      throw new IllegalStateException("Cannot set password whilst logged in & online"); 
    this.password = password;
  }
  
  protected String getUsername() {
    return this.username;
  }
  
  protected String getPassword() {
    return this.password;
  }
  
  public void loadFromStorage(Map<String, Object> credentials) {
    logOut();
    setUsername(String.valueOf(credentials.get("username")));
    if (credentials.containsKey("userid")) {
      this.userid = String.valueOf(credentials.get("userid"));
    } else {
      this.userid = this.username;
    } 
    if (credentials.containsKey("userProperties"))
      try {
        List<Map<String, String>> list = (List<Map<String, String>>)credentials.get("userProperties");
        for (Map<String, String> propertyMap : list) {
          String name = propertyMap.get("name");
          String value = propertyMap.get("value");
          String signature = propertyMap.get("signature");
          if (signature == null) {
            getModifiableUserProperties().put(name, new Property(name, value));
            continue;
          } 
          getModifiableUserProperties().put(name, new Property(name, value, signature));
        } 
      } catch (Throwable t) {
        LOGGER.warn("Couldn't deserialize user properties", t);
      }  
    if (credentials.containsKey("displayName") && credentials.containsKey("uuid")) {
      GameProfile profile = new GameProfile(UUIDTypeAdapter.fromString(String.valueOf(credentials.get("uuid"))), String.valueOf(credentials.get("displayName")));
      if (credentials.containsKey("profileProperties"))
        try {
          List<Map<String, String>> list = (List<Map<String, String>>)credentials.get("profileProperties");
          for (Map<String, String> propertyMap : list) {
            String name = propertyMap.get("name");
            String value = propertyMap.get("value");
            String signature = propertyMap.get("signature");
            if (signature == null) {
              profile.getProperties().put(name, new Property(name, value));
              continue;
            } 
            profile.getProperties().put(name, new Property(name, value, signature));
          } 
        } catch (Throwable t) {
          LOGGER.warn("Couldn't deserialize profile properties", t);
        }  
      setSelectedProfile(profile);
    } 
  }
  
  public Map<String, Object> saveForStorage() {
    Map<String, Object> result = new HashMap<String, Object>();
    if (getUsername() != null)
      result.put("username", getUsername()); 
    if (getUserID() != null) {
      result.put("userid", getUserID());
    } else if (getUsername() != null) {
      result.put("username", getUsername());
    } 
    if (!getUserProperties().isEmpty()) {
      List<Map<String, String>> properties = new ArrayList<Map<String, String>>();
      for (Property userProperty : getUserProperties().values()) {
        Map<String, String> property = new HashMap<String, String>();
        property.put("name", userProperty.getName());
        property.put("value", userProperty.getValue());
        property.put("signature", userProperty.getSignature());
        properties.add(property);
      } 
      result.put("userProperties", properties);
    } 
    GameProfile selectedProfile = getSelectedProfile();
    if (selectedProfile != null) {
      result.put("displayName", selectedProfile.getName());
      result.put("uuid", selectedProfile.getId());
      List<Map<String, String>> properties = new ArrayList<Map<String, String>>();
      for (Property profileProperty : selectedProfile.getProperties().values()) {
        Map<String, String> property = new HashMap<String, String>();
        property.put("name", profileProperty.getName());
        property.put("value", profileProperty.getValue());
        property.put("signature", profileProperty.getSignature());
        properties.add(property);
      } 
      if (!properties.isEmpty())
        result.put("profileProperties", properties); 
    } 
    return result;
  }
  
  protected void setSelectedProfile(GameProfile selectedProfile) {
    this.selectedProfile = selectedProfile;
  }
  
  public GameProfile getSelectedProfile() {
    return this.selectedProfile;
  }
  
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(getClass().getSimpleName());
    result.append("{");
    if (isLoggedIn()) {
      result.append("Logged in as ");
      result.append(getUsername());
      if (getSelectedProfile() != null) {
        result.append(" / ");
        result.append(getSelectedProfile());
        result.append(" - ");
        if (canPlayOnline()) {
          result.append("Online");
        } else {
          result.append("Offline");
        } 
      } 
    } else {
      result.append("Not logged in");
    } 
    result.append("}");
    return result.toString();
  }
  
  public AuthenticationService getAuthenticationService() {
    return this.authenticationService;
  }
  
  public String getUserID() {
    return this.userid;
  }
  
  public PropertyMap getUserProperties() {
    if (isLoggedIn()) {
      PropertyMap result = new PropertyMap();
      result.putAll((Multimap)getModifiableUserProperties());
      return result;
    } 
    return new PropertyMap();
  }
  
  protected PropertyMap getModifiableUserProperties() {
    return this.userProperties;
  }
  
  public UserType getUserType() {
    if (isLoggedIn())
      return (this.userType == null) ? UserType.LEGACY : this.userType; 
    return null;
  }
  
  protected void setUserType(UserType userType) {
    this.userType = userType;
  }
  
  protected void setUserid(String userid) {
    this.userid = userid;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\BaseUserAuthentication.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */