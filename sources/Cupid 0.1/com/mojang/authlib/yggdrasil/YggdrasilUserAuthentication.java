package com.mojang.authlib.yggdrasil;

import com.google.common.collect.Multimap;
import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.HttpUserAuthentication;
import com.mojang.authlib.UserType;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.yggdrasil.request.AuthenticationRequest;
import com.mojang.authlib.yggdrasil.request.RefreshRequest;
import com.mojang.authlib.yggdrasil.request.ValidateRequest;
import com.mojang.authlib.yggdrasil.response.AuthenticationResponse;
import com.mojang.authlib.yggdrasil.response.RefreshResponse;
import com.mojang.authlib.yggdrasil.response.Response;
import com.mojang.authlib.yggdrasil.response.User;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YggdrasilUserAuthentication extends HttpUserAuthentication {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private static final String BASE_URL = "https://authserver.mojang.com/";
  
  private static final URL ROUTE_AUTHENTICATE = HttpAuthenticationService.constantURL("https://authserver.mojang.com/authenticate");
  
  private static final URL ROUTE_REFRESH = HttpAuthenticationService.constantURL("https://authserver.mojang.com/refresh");
  
  private static final URL ROUTE_VALIDATE = HttpAuthenticationService.constantURL("https://authserver.mojang.com/validate");
  
  private static final URL ROUTE_INVALIDATE = HttpAuthenticationService.constantURL("https://authserver.mojang.com/invalidate");
  
  private static final URL ROUTE_SIGNOUT = HttpAuthenticationService.constantURL("https://authserver.mojang.com/signout");
  
  private static final String STORAGE_KEY_ACCESS_TOKEN = "accessToken";
  
  private final Agent agent;
  
  private GameProfile[] profiles;
  
  private String accessToken;
  
  private boolean isOnline;
  
  public YggdrasilUserAuthentication(YggdrasilAuthenticationService authenticationService, Agent agent) {
    super(authenticationService);
    this.agent = agent;
  }
  
  public boolean canLogIn() {
    return (!canPlayOnline() && StringUtils.isNotBlank(getUsername()) && (StringUtils.isNotBlank(getPassword()) || StringUtils.isNotBlank(getAuthenticatedToken())));
  }
  
  public void logIn() throws AuthenticationException {
    if (StringUtils.isBlank(getUsername()))
      throw new InvalidCredentialsException("Invalid username"); 
    if (StringUtils.isNotBlank(getAuthenticatedToken())) {
      logInWithToken();
    } else if (StringUtils.isNotBlank(getPassword())) {
      logInWithPassword();
    } else {
      throw new InvalidCredentialsException("Invalid password");
    } 
  }
  
  protected void logInWithPassword() throws AuthenticationException {
    if (StringUtils.isBlank(getUsername()))
      throw new InvalidCredentialsException("Invalid username"); 
    if (StringUtils.isBlank(getPassword()))
      throw new InvalidCredentialsException("Invalid password"); 
    LOGGER.info("Logging in with username & password");
    AuthenticationRequest request = new AuthenticationRequest(this, getUsername(), getPassword());
    AuthenticationResponse response = getAuthenticationService().<AuthenticationResponse>makeRequest(ROUTE_AUTHENTICATE, request, AuthenticationResponse.class);
    if (!response.getClientToken().equals(getAuthenticationService().getClientToken()))
      throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!"); 
    if (response.getSelectedProfile() != null) {
      setUserType(response.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG);
    } else if (ArrayUtils.isNotEmpty((Object[])response.getAvailableProfiles())) {
      setUserType(response.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG);
    } 
    User user = response.getUser();
    if (user != null && user.getId() != null) {
      setUserid(user.getId());
    } else {
      setUserid(getUsername());
    } 
    this.isOnline = true;
    this.accessToken = response.getAccessToken();
    this.profiles = response.getAvailableProfiles();
    setSelectedProfile(response.getSelectedProfile());
    getModifiableUserProperties().clear();
    updateUserProperties(user);
  }
  
  protected void updateUserProperties(User user) {
    if (user == null)
      return; 
    if (user.getProperties() != null)
      getModifiableUserProperties().putAll((Multimap)user.getProperties()); 
  }
  
  protected void logInWithToken() throws AuthenticationException {
    if (StringUtils.isBlank(getUserID()))
      if (StringUtils.isBlank(getUsername())) {
        setUserid(getUsername());
      } else {
        throw new InvalidCredentialsException("Invalid uuid & username");
      }  
    if (StringUtils.isBlank(getAuthenticatedToken()))
      throw new InvalidCredentialsException("Invalid access token"); 
    LOGGER.info("Logging in with access token");
    if (checkTokenValidity()) {
      LOGGER.debug("Skipping refresh call as we're safely logged in.");
      this.isOnline = true;
      return;
    } 
    RefreshRequest request = new RefreshRequest(this);
    RefreshResponse response = getAuthenticationService().<RefreshResponse>makeRequest(ROUTE_REFRESH, request, RefreshResponse.class);
    if (!response.getClientToken().equals(getAuthenticationService().getClientToken()))
      throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!"); 
    if (response.getSelectedProfile() != null) {
      setUserType(response.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG);
    } else if (ArrayUtils.isNotEmpty((Object[])response.getAvailableProfiles())) {
      setUserType(response.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG);
    } 
    if (response.getUser() != null && response.getUser().getId() != null) {
      setUserid(response.getUser().getId());
    } else {
      setUserid(getUsername());
    } 
    this.isOnline = true;
    this.accessToken = response.getAccessToken();
    this.profiles = response.getAvailableProfiles();
    setSelectedProfile(response.getSelectedProfile());
    getModifiableUserProperties().clear();
    updateUserProperties(response.getUser());
  }
  
  protected boolean checkTokenValidity() throws AuthenticationException {
    ValidateRequest request = new ValidateRequest(this);
    try {
      getAuthenticationService().makeRequest(ROUTE_VALIDATE, request, Response.class);
      return true;
    } catch (AuthenticationException ex) {
      return false;
    } 
  }
  
  public void logOut() {
    super.logOut();
    this.accessToken = null;
    this.profiles = null;
    this.isOnline = false;
  }
  
  public GameProfile[] getAvailableProfiles() {
    return this.profiles;
  }
  
  public boolean isLoggedIn() {
    return StringUtils.isNotBlank(this.accessToken);
  }
  
  public boolean canPlayOnline() {
    return (isLoggedIn() && getSelectedProfile() != null && this.isOnline);
  }
  
  public void selectGameProfile(GameProfile profile) throws AuthenticationException {
    if (!isLoggedIn())
      throw new AuthenticationException("Cannot change game profile whilst not logged in"); 
    if (getSelectedProfile() != null)
      throw new AuthenticationException("Cannot change game profile. You must log out and back in."); 
    if (profile == null || !ArrayUtils.contains((Object[])this.profiles, profile))
      throw new IllegalArgumentException("Invalid profile '" + profile + "'"); 
    RefreshRequest request = new RefreshRequest(this, profile);
    RefreshResponse response = getAuthenticationService().<RefreshResponse>makeRequest(ROUTE_REFRESH, request, RefreshResponse.class);
    if (!response.getClientToken().equals(getAuthenticationService().getClientToken()))
      throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!"); 
    this.isOnline = true;
    this.accessToken = response.getAccessToken();
    setSelectedProfile(response.getSelectedProfile());
  }
  
  public void loadFromStorage(Map<String, Object> credentials) {
    super.loadFromStorage(credentials);
    this.accessToken = String.valueOf(credentials.get("accessToken"));
  }
  
  public Map<String, Object> saveForStorage() {
    Map<String, Object> result = super.saveForStorage();
    if (StringUtils.isNotBlank(getAuthenticatedToken()))
      result.put("accessToken", getAuthenticatedToken()); 
    return result;
  }
  
  @Deprecated
  public String getSessionToken() {
    if (isLoggedIn() && getSelectedProfile() != null && canPlayOnline())
      return String.format("token:%s:%s", new Object[] { getAuthenticatedToken(), getSelectedProfile().getId() }); 
    return null;
  }
  
  public String getAuthenticatedToken() {
    return this.accessToken;
  }
  
  public Agent getAgent() {
    return this.agent;
  }
  
  public String toString() {
    return "YggdrasilAuthenticationService{agent=" + this.agent + ", profiles=" + Arrays.toString((Object[])this.profiles) + ", selectedProfile=" + getSelectedProfile() + ", username='" + getUsername() + '\'' + ", isLoggedIn=" + isLoggedIn() + ", userType=" + getUserType() + ", canPlayOnline=" + canPlayOnline() + ", accessToken='" + this.accessToken + '\'' + ", clientToken='" + getAuthenticationService().getClientToken() + '\'' + '}';
  }
  
  public YggdrasilAuthenticationService getAuthenticationService() {
    return (YggdrasilAuthenticationService)super.getAuthenticationService();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\yggdrasil\YggdrasilUserAuthentication.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */