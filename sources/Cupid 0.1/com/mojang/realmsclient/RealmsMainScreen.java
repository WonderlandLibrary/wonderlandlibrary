package com.mojang.realmsclient;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.realmsclient.client.Ping;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.PingResult;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RegionPingResult;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.mojang.realmsclient.gui.LongRunningTask;
import com.mojang.realmsclient.gui.RealmsDataFetcher;
import com.mojang.realmsclient.gui.screens.RealmsBuyRealmsScreen;
import com.mojang.realmsclient.gui.screens.RealmsClientOutdatedScreen;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsCreateRealmScreen;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongConfirmationScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.gui.screens.RealmsParentalConsentScreen;
import com.mojang.realmsclient.gui.screens.RealmsPendingInvitesScreen;
import com.mojang.realmsclient.util.RealmsTasks;
import com.mojang.realmsclient.util.RealmsUtil;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsMth;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsScrolledSelectionList;
import net.minecraft.realms.RealmsServerStatusPinger;
import net.minecraft.realms.Tezzelator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class RealmsMainScreen extends RealmsScreen {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private static boolean overrideConfigure = false;
  
  private static boolean stageEnabled = false;
  
  private boolean dontSetConnectedToRealms = false;
  
  protected static final int BUTTON_BACK_ID = 0;
  
  protected static final int BUTTON_PLAY_ID = 1;
  
  protected static final int BUTTON_CONFIGURE_ID = 2;
  
  protected static final int BUTTON_LEAVE_ID = 3;
  
  protected static final int BUTTON_BUY_ID = 4;
  
  protected static final int RESOURCEPACK_ID = 100;
  
  private RealmsServer resourcePackServer;
  
  private static final String ON_ICON_LOCATION = "realms:textures/gui/realms/on_icon.png";
  
  private static final String OFF_ICON_LOCATION = "realms:textures/gui/realms/off_icon.png";
  
  private static final String EXPIRED_ICON_LOCATION = "realms:textures/gui/realms/expired_icon.png";
  
  private static final String INVITATION_ICONS_LOCATION = "realms:textures/gui/realms/invitation_icons.png";
  
  private static final String INVITE_ICON_LOCATION = "realms:textures/gui/realms/invite_icon.png";
  
  private static final String WORLDICON_LOCATION = "realms:textures/gui/realms/world_icon.png";
  
  private static final String LOGO_LOCATION = "realms:textures/gui/title/realms.png";
  
  private static RealmsDataFetcher realmsDataFetcher = new RealmsDataFetcher();
  
  private static RealmsServerStatusPinger statusPinger = new RealmsServerStatusPinger();
  
  private static final ThreadPoolExecutor THREAD_POOL = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());
  
  private static int lastScrollYPosition = -1;
  
  private RealmsScreen lastScreen;
  
  private volatile ServerSelectionList serverSelectionList;
  
  private long selectedServerId = -1L;
  
  private RealmsButton configureButton;
  
  private RealmsButton leaveButton;
  
  private RealmsButton playButton;
  
  private RealmsButton buyButton;
  
  private String toolTip;
  
  private List<RealmsServer> realmsServers = Lists.newArrayList();
  
  private static final String mcoInfoUrl = "https://minecraft.net/realms";
  
  private volatile int numberOfPendingInvites = 0;
  
  private int animTick;
  
  private static volatile boolean mcoEnabled;
  
  private static volatile boolean mcoEnabledCheck;
  
  private static boolean checkedMcoAvailability;
  
  private static volatile boolean trialsAvailable;
  
  private static volatile boolean createdTrial = false;
  
  private static final ReentrantLock trialLock = new ReentrantLock();
  
  private static RealmsScreen realmsGenericErrorScreen = null;
  
  private static boolean regionsPinged = false;
  
  private boolean onLink = false;
  
  private int mindex = 0;
  
  private char[] mchars = new char[] { '3', '2', '1', '4', '5', '6' };
  
  private int sindex = 0;
  
  private char[] schars = new char[] { '9', '8', '7', '1', '2', '3' };
  
  static {
    String version = RealmsVersion.getVersion();
    if (version != null)
      LOGGER.info("Realms library version == " + version); 
  }
  
  public RealmsMainScreen(RealmsScreen lastScreen) {
    this.lastScreen = lastScreen;
    checkIfMcoEnabled();
  }
  
  public void mouseEvent() {
    super.mouseEvent();
    this.serverSelectionList.mouseEvent();
  }
  
  public void init() {
    if (!this.dontSetConnectedToRealms)
      Realms.setConnectedToRealms(false); 
    if (realmsGenericErrorScreen != null) {
      Realms.setScreen(realmsGenericErrorScreen);
      return;
    } 
    Keyboard.enableRepeatEvents(true);
    buttonsClear();
    postInit();
    if (isMcoEnabled())
      realmsDataFetcher.init(); 
  }
  
  public void postInit() {
    buttonsAdd(this.playButton = newButton(1, width() / 2 - 154, height() - 52, 154, 20, getLocalizedString("mco.selectServer.play")));
    buttonsAdd(this.configureButton = newButton(2, width() / 2 + 6, height() - 52, 154, 20, getLocalizedString("mco.selectServer.configure")));
    buttonsAdd(this.leaveButton = newButton(3, width() / 2 - 154, height() - 28, 102, 20, getLocalizedString("mco.selectServer.leave")));
    buttonsAdd(this.buyButton = newButton(4, width() / 2 - 48, height() - 28, 102, 20, getLocalizedString("mco.selectServer.buy")));
    buttonsAdd(newButton(0, width() / 2 + 58, height() - 28, 102, 20, getLocalizedString("gui.back")));
    this.serverSelectionList = new ServerSelectionList();
    if (lastScrollYPosition != -1)
      this.serverSelectionList.scroll(lastScrollYPosition); 
    RealmsServer server = findServer(this.selectedServerId);
    this.playButton.active((server != null && server.state == RealmsServer.State.OPEN && !server.expired));
    this.configureButton.active((overrideConfigure || (server != null && server.state != RealmsServer.State.ADMIN_LOCK && server.ownerUUID.equals(Realms.getUUID()))));
    this.leaveButton.active((server != null && !server.ownerUUID.equals(Realms.getUUID())));
  }
  
  public void tick() {
    this.animTick++;
    if (noParentalConsent())
      Realms.setScreen((RealmsScreen)new RealmsParentalConsentScreen(this.lastScreen)); 
    if (isMcoEnabled()) {
      realmsDataFetcher.init();
    } else {
      return;
    } 
    if (realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.SERVER_LIST)) {
      List<RealmsServer> newServers = realmsDataFetcher.getServers();
      boolean ownsNonExpiredRealmServer = false;
      for (RealmsServer retrievedServer : newServers) {
        if (isSelfOwnedNonExpiredServer(retrievedServer))
          ownsNonExpiredRealmServer = true; 
        for (RealmsServer oldServer : this.realmsServers) {
          if (retrievedServer.id == oldServer.id)
            retrievedServer.latestStatFrom(oldServer); 
        } 
      } 
      this.realmsServers = newServers;
      if (!regionsPinged && ownsNonExpiredRealmServer) {
        regionsPinged = true;
        pingRegions();
      } 
    } 
    if (realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.PENDING_INVITE))
      this.numberOfPendingInvites = realmsDataFetcher.getPendingInvitesCount(); 
    if (realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.TRIAL_AVAILABLE) && !createdTrial)
      trialsAvailable = realmsDataFetcher.isTrialAvailable(); 
    realmsDataFetcher.markClean();
  }
  
  private void pingRegions() {
    (new Thread() {
        public void run() {
          List<RegionPingResult> regionPingResultList = Ping.pingAllRegions();
          RealmsClient client = RealmsClient.createRealmsClient();
          PingResult pingResult = new PingResult();
          pingResult.pingResults = regionPingResultList;
          pingResult.worldIds = RealmsMainScreen.this.getOwnedNonExpiredWorldIds();
          try {
            client.sendPingResults(pingResult);
          } catch (Throwable t) {
            RealmsMainScreen.LOGGER.warn("Could not send ping result to Realms: ", t);
          } 
        }
      }).start();
  }
  
  private List<Long> getOwnedNonExpiredWorldIds() {
    List<Long> ids = new ArrayList<Long>();
    for (RealmsServer server : this.realmsServers) {
      if (isSelfOwnedNonExpiredServer(server))
        ids.add(Long.valueOf(server.id)); 
    } 
    return ids;
  }
  
  private boolean isMcoEnabled() {
    return mcoEnabled;
  }
  
  private boolean noParentalConsent() {
    return (mcoEnabledCheck && !mcoEnabled);
  }
  
  public void removed() {
    Keyboard.enableRepeatEvents(false);
  }
  
  public void buttonClicked(RealmsButton button) {
    if (!button.active())
      return; 
    switch (button.id()) {
      case 1:
        play(findServer(this.selectedServerId));
        return;
      case 2:
        configureClicked();
        return;
      case 3:
        leaveClicked();
        return;
      case 4:
        saveListScrollPosition();
        stopRealmsFetcherAndPinger();
        Realms.setScreen((RealmsScreen)new RealmsBuyRealmsScreen(this));
        return;
      case 0:
        stopRealmsFetcherAndPinger();
        Realms.setScreen(this.lastScreen);
        return;
    } 
  }
  
  private void createTrial() {
    if (createdTrial) {
      trialsAvailable = false;
      return;
    } 
    final RealmsScreen mainScreen = this;
    (new Thread("Realms-create-trial") {
        public void run() {
          try {
            if (!RealmsMainScreen.trialLock.tryLock(10L, TimeUnit.MILLISECONDS))
              return; 
            RealmsClient client = RealmsClient.createRealmsClient();
            RealmsMainScreen.trialsAvailable = false;
            if (client.createTrial().booleanValue()) {
              RealmsMainScreen.createdTrial = true;
              RealmsMainScreen.realmsDataFetcher.forceUpdate();
            } else {
              Realms.setScreen((RealmsScreen)new RealmsGenericErrorScreen(RealmsScreen.getLocalizedString("mco.trial.unavailable"), mainScreen));
            } 
          } catch (RealmsServiceException e) {
            RealmsMainScreen.LOGGER.error("Trials wasn't available: " + e.toString());
            Realms.setScreen((RealmsScreen)new RealmsGenericErrorScreen(e, RealmsMainScreen.this));
          } catch (IOException e) {
            RealmsMainScreen.LOGGER.error("Couldn't parse response when trying to create trial: " + e.toString());
            RealmsMainScreen.trialsAvailable = false;
          } catch (InterruptedException e) {
            RealmsMainScreen.LOGGER.error("Trial Interrupted exception: " + e.toString());
          } finally {
            if (RealmsMainScreen.trialLock.isHeldByCurrentThread())
              RealmsMainScreen.trialLock.unlock(); 
          } 
        }
      }).start();
  }
  
  private void checkIfMcoEnabled() {
    if (!checkedMcoAvailability) {
      checkedMcoAvailability = true;
      (new Thread("MCO Availability Checker #1") {
          public void run() {
            RealmsClient client = RealmsClient.createRealmsClient();
            try {
              RealmsClient.CompatibleVersionResponse versionResponse = client.clientCompatible();
              if (versionResponse.equals(RealmsClient.CompatibleVersionResponse.OUTDATED)) {
                Realms.setScreen(RealmsMainScreen.realmsGenericErrorScreen = (RealmsScreen)new RealmsClientOutdatedScreen(RealmsMainScreen.this.lastScreen, true));
                return;
              } 
              if (versionResponse.equals(RealmsClient.CompatibleVersionResponse.OTHER)) {
                Realms.setScreen(RealmsMainScreen.realmsGenericErrorScreen = (RealmsScreen)new RealmsClientOutdatedScreen(RealmsMainScreen.this.lastScreen, false));
                return;
              } 
            } catch (RealmsServiceException e) {
              RealmsMainScreen.checkedMcoAvailability = false;
              RealmsMainScreen.LOGGER.error("Couldn't connect to realms: ", new Object[] { e.toString() });
              if (e.httpResultCode == 401)
                RealmsMainScreen.realmsGenericErrorScreen = (RealmsScreen)new RealmsGenericErrorScreen(e, RealmsMainScreen.this.lastScreen); 
              Realms.setScreen((RealmsScreen)new RealmsGenericErrorScreen(e, RealmsMainScreen.this.lastScreen));
              return;
            } catch (IOException e) {
              RealmsMainScreen.checkedMcoAvailability = false;
              RealmsMainScreen.LOGGER.error("Couldn't connect to realms: ", new Object[] { e.getMessage() });
              Realms.setScreen((RealmsScreen)new RealmsGenericErrorScreen(e.getMessage(), RealmsMainScreen.this.lastScreen));
              return;
            } 
            boolean retry = false;
            for (int i = 0; i < 3; ) {
              try {
                Boolean result = client.mcoEnabled();
                if (result.booleanValue()) {
                  RealmsMainScreen.LOGGER.info("Realms is available for this user");
                  RealmsMainScreen.mcoEnabled = true;
                } else {
                  RealmsMainScreen.LOGGER.info("Realms is not available for this user");
                  RealmsMainScreen.mcoEnabled = false;
                } 
                RealmsMainScreen.mcoEnabledCheck = true;
              } catch (RetryCallException e) {
                retry = true;
              } catch (RealmsServiceException e) {
                RealmsMainScreen.LOGGER.error("Couldn't connect to Realms: " + e.toString());
              } catch (IOException e) {
                RealmsMainScreen.LOGGER.error("Couldn't parse response connecting to Realms: " + e.getMessage());
              } 
              if (retry) {
                try {
                  Thread.sleep(5000L);
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                } 
                i++;
              } 
            } 
          }
        }).start();
    } 
  }
  
  private void switchToStage() {
    if (!stageEnabled)
      (new Thread("MCO Stage Availability Checker #1") {
          public void run() {
            RealmsClient client = RealmsClient.createRealmsClient();
            try {
              Boolean result = client.stageAvailable();
              if (result.booleanValue()) {
                RealmsMainScreen.this.stopRealmsFetcherAndPinger();
                RealmsClient.switchToStage();
                RealmsMainScreen.LOGGER.info("Switched to stage");
                RealmsMainScreen.realmsDataFetcher.init();
                RealmsMainScreen.stageEnabled = true;
              } else {
                RealmsMainScreen.stageEnabled = false;
              } 
            } catch (RealmsServiceException e) {
              RealmsMainScreen.LOGGER.error("Couldn't connect to Realms: " + e.toString());
            } catch (IOException e) {
              RealmsMainScreen.LOGGER.error("Couldn't parse response connecting to Realms: " + e.getMessage());
            } 
          }
        }).start(); 
  }
  
  private void switchToProd() {
    if (stageEnabled) {
      stageEnabled = false;
      stopRealmsFetcherAndPinger();
      RealmsClient.switchToProd();
      realmsDataFetcher.init();
    } 
  }
  
  private void stopRealmsFetcherAndPinger() {
    if (isMcoEnabled()) {
      realmsDataFetcher.stop();
      statusPinger.removeAll();
    } 
  }
  
  private void configureClicked() {
    RealmsServer selectedServer = findServer(this.selectedServerId);
    if (selectedServer != null && (
      Realms.getUUID().equals(selectedServer.ownerUUID) || overrideConfigure)) {
      stopRealmsFetcherAndPinger();
      saveListScrollPosition();
      Realms.setScreen((RealmsScreen)new RealmsConfigureWorldScreen(this, selectedServer.id));
    } 
  }
  
  private void leaveClicked() {
    RealmsServer selectedServer = findServer(this.selectedServerId);
    if (selectedServer != null && 
      !Realms.getUUID().equals(selectedServer.ownerUUID)) {
      saveListScrollPosition();
      String line2 = getLocalizedString("mco.configure.world.leave.question.line1");
      String line3 = getLocalizedString("mco.configure.world.leave.question.line2");
      Realms.setScreen((RealmsScreen)new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Info, line2, line3, true, 3));
    } 
  }
  
  private void saveListScrollPosition() {
    lastScrollYPosition = this.serverSelectionList.getScroll();
  }
  
  private RealmsServer findServer(long id) {
    for (RealmsServer server : this.realmsServers) {
      if (server.id == id)
        return server; 
    } 
    return null;
  }
  
  private int findIndex(long serverId) {
    for (int i = 0; i < this.realmsServers.size(); i++) {
      if (((RealmsServer)this.realmsServers.get(i)).id == serverId)
        return i; 
    } 
    return -1;
  }
  
  public void confirmResult(boolean result, int id) {
    if (id == 3) {
      if (result)
        (new Thread("Realms-leave-server") {
            public void run() {
              try {
                RealmsServer server = RealmsMainScreen.this.findServer(RealmsMainScreen.this.selectedServerId);
                if (server != null) {
                  RealmsClient client = RealmsClient.createRealmsClient();
                  RealmsMainScreen.realmsDataFetcher.removeItem(server);
                  RealmsMainScreen.this.realmsServers.remove(server);
                  client.uninviteMyselfFrom(server.id);
                  RealmsMainScreen.realmsDataFetcher.removeItem(server);
                  RealmsMainScreen.this.realmsServers.remove(server);
                  RealmsMainScreen.this.updateSelectedItemPointer();
                } 
              } catch (RealmsServiceException e) {
                RealmsMainScreen.LOGGER.error("Couldn't configure world");
                Realms.setScreen((RealmsScreen)new RealmsGenericErrorScreen(e, RealmsMainScreen.this));
              } 
            }
          }).start(); 
      Realms.setScreen(this);
    } else if (id == 100) {
      if (!result) {
        Realms.setScreen(this);
      } else {
        connectToServer(this.resourcePackServer);
      } 
    } 
  }
  
  private void updateSelectedItemPointer() {
    int originalIndex = findIndex(this.selectedServerId);
    if (this.realmsServers.size() - 1 == originalIndex)
      originalIndex--; 
    if (this.realmsServers.size() == 0)
      originalIndex = -1; 
    if (originalIndex >= 0 && originalIndex < this.realmsServers.size())
      this.selectedServerId = ((RealmsServer)this.realmsServers.get(originalIndex)).id; 
  }
  
  public void removeSelection() {
    this.selectedServerId = -1L;
  }
  
  public void keyPressed(char ch, int eventKey) {
    switch (eventKey) {
      case 28:
      case 156:
        this.mindex = 0;
        this.sindex = 0;
        buttonClicked(this.playButton);
        return;
      case 1:
        this.mindex = 0;
        this.sindex = 0;
        stopRealmsFetcherAndPinger();
        Realms.setScreen(this.lastScreen);
        return;
    } 
    if (this.mchars[this.mindex] == ch) {
      this.mindex++;
      if (this.mindex == this.mchars.length) {
        this.mindex = 0;
        overrideConfigure = true;
      } 
    } else {
      this.mindex = 0;
    } 
    if (this.schars[this.sindex] == ch) {
      this.sindex++;
      if (this.sindex == this.schars.length) {
        this.sindex = 0;
        if (!stageEnabled) {
          switchToStage();
        } else {
          switchToProd();
        } 
      } 
      return;
    } 
    this.sindex = 0;
  }
  
  public void render(int xm, int ym, float a) {
    this.toolTip = null;
    renderBackground();
    this.serverSelectionList.render(xm, ym, a);
    drawRealmsLogo(width() / 2 - 50, 7);
    renderLink(xm, ym);
    if (this.toolTip != null)
      renderMousehoverTooltip(this.toolTip, xm, ym); 
    drawInvitationPendingIcon(xm, ym);
    if (stageEnabled)
      renderStage(); 
    super.render(xm, ym, a);
  }
  
  private void drawRealmsLogo(int x, int y) {
    RealmsScreen.bind("realms:textures/gui/title/realms.png");
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glPushMatrix();
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    RealmsScreen.blit(x * 2, y * 2 - 5, 0.0F, 0.0F, 200, 50, 200.0F, 50.0F);
    GL11.glPopMatrix();
  }
  
  public void mouseClicked(int x, int y, int buttonNum) {
    if (inPendingInvitationArea(x, y)) {
      stopRealmsFetcherAndPinger();
      RealmsPendingInvitesScreen pendingInvitationScreen = new RealmsPendingInvitesScreen(this.lastScreen);
      Realms.setScreen((RealmsScreen)pendingInvitationScreen);
    } 
    if (this.onLink)
      RealmsUtil.browseTo("https://minecraft.net/realms"); 
  }
  
  private void drawInvitationPendingIcon(int xm, int ym) {
    int pendingInvitesCount = this.numberOfPendingInvites;
    boolean hovering = inPendingInvitationArea(xm, ym);
    int baseX = width() / 2 + 50;
    int baseY = 8;
    if (pendingInvitesCount != 0) {
      float scale = 0.25F + (1.0F + RealmsMth.sin(this.animTick * 0.5F)) * 0.25F;
      int color = 0xFF000000 | (int)(scale * 64.0F) << 16 | (int)(scale * 64.0F) << 8 | (int)(scale * 64.0F) << 0;
      fillGradient(baseX - 2, 6, baseX + 18, 26, color, color);
      color = 0xFF000000 | (int)(scale * 255.0F) << 16 | (int)(scale * 255.0F) << 8 | (int)(scale * 255.0F) << 0;
      fillGradient(baseX - 2, 6, baseX + 18, 7, color, color);
      fillGradient(baseX - 2, 6, baseX - 1, 26, color, color);
      fillGradient(baseX + 17, 6, baseX + 18, 26, color, color);
      fillGradient(baseX - 2, 25, baseX + 18, 26, color, color);
    } 
    RealmsScreen.bind("realms:textures/gui/realms/invite_icon.png");
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glPushMatrix();
    RealmsScreen.blit(baseX, 2, hovering ? 16.0F : 0.0F, 0.0F, 15, 25, 31.0F, 25.0F);
    GL11.glPopMatrix();
    if (pendingInvitesCount != 0) {
      int spritePos = (Math.min(pendingInvitesCount, 6) - 1) * 8;
      int yOff = (int)(Math.max(0.0F, Math.max(RealmsMth.sin((10 + this.animTick) * 0.57F), RealmsMth.cos(this.animTick * 0.35F))) * -6.0F);
      RealmsScreen.bind("realms:textures/gui/realms/invitation_icons.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      RealmsScreen.blit(baseX + 4, 12 + yOff, spritePos, hovering ? 8.0F : 0.0F, 8, 8, 48.0F, 16.0F);
      GL11.glPopMatrix();
    } 
    if (hovering) {
      int rx = xm + 12;
      int ry = ym - 12;
      String message = (pendingInvitesCount == 0) ? getLocalizedString("mco.invites.nopending") : getLocalizedString("mco.invites.pending");
      int width = fontWidth(message);
      fillGradient(rx - 3, ry - 3, rx + width + 3, ry + 8 + 3, -1073741824, -1073741824);
      fontDrawShadow(message, rx, ry, -1);
    } 
  }
  
  private boolean inPendingInvitationArea(int xm, int ym) {
    int x1 = width() / 2 + 50;
    int x2 = width() / 2 + 66;
    int y1 = 13;
    int y2 = 27;
    return (x1 <= xm && xm <= x2 && y1 <= ym && ym <= y2);
  }
  
  public void play(RealmsServer server) {
    if (server != null) {
      stopRealmsFetcherAndPinger();
      this.dontSetConnectedToRealms = true;
      if (server.resourcePackUrl != null && server.resourcePackHash != null) {
        this.resourcePackServer = server;
        saveListScrollPosition();
        String line2 = getLocalizedString("mco.configure.world.resourcepack.question.line1");
        String line3 = getLocalizedString("mco.configure.world.resourcepack.question.line2");
        Realms.setScreen((RealmsScreen)new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Info, line2, line3, true, 100));
      } else {
        connectToServer(server);
      } 
    } 
  }
  
  private void connectToServer(RealmsServer server) {
    RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this, (LongRunningTask)new RealmsTasks.RealmsConnectTask(this, server));
    longRunningMcoTaskScreen.start();
    Realms.setScreen((RealmsScreen)longRunningMcoTaskScreen);
  }
  
  private class ServerSelectionList extends RealmsScrolledSelectionList {
    public ServerSelectionList() {
      super(RealmsMainScreen.this.width(), RealmsMainScreen.this.height(), 32, RealmsMainScreen.this.height() - 64, 36);
    }
    
    public int getItemCount() {
      if (RealmsMainScreen.trialsAvailable)
        return RealmsMainScreen.this.realmsServers.size() + 1; 
      return RealmsMainScreen.this.realmsServers.size();
    }
    
    public void selectItem(int item, boolean doubleClick, int xMouse, int yMouse) {
      if (RealmsMainScreen.trialsAvailable) {
        if (item == 0) {
          RealmsMainScreen.this.createTrial();
          return;
        } 
        item--;
      } 
      if (item >= RealmsMainScreen.this.realmsServers.size())
        return; 
      RealmsServer server = RealmsMainScreen.this.realmsServers.get(item);
      if (server.state == RealmsServer.State.UNINITIALIZED) {
        RealmsMainScreen.this.selectedServerId = -1L;
        RealmsMainScreen.this.stopRealmsFetcherAndPinger();
        Realms.setScreen((RealmsScreen)new RealmsCreateRealmScreen(server, RealmsMainScreen.this));
      } else {
        RealmsMainScreen.this.selectedServerId = server.id;
      } 
      RealmsMainScreen.this.configureButton.active((RealmsMainScreen.overrideConfigure || (RealmsMainScreen.this.isSelfOwnedServer(server) && server.state != RealmsServer.State.ADMIN_LOCK && server.state != RealmsServer.State.UNINITIALIZED)));
      RealmsMainScreen.this.leaveButton.active(!RealmsMainScreen.this.isSelfOwnedServer(server));
      RealmsMainScreen.this.playButton.active((server.state == RealmsServer.State.OPEN && !server.expired));
      if (doubleClick && RealmsMainScreen.this.playButton.active())
        RealmsMainScreen.this.play(RealmsMainScreen.this.findServer(RealmsMainScreen.this.selectedServerId)); 
    }
    
    public boolean isSelectedItem(int item) {
      if (RealmsMainScreen.trialsAvailable) {
        if (item == 0)
          return false; 
        item--;
      } 
      return (item == RealmsMainScreen.this.findIndex(RealmsMainScreen.this.selectedServerId));
    }
    
    public int getMaxPosition() {
      return getItemCount() * 36;
    }
    
    public void renderBackground() {
      RealmsMainScreen.this.renderBackground();
    }
    
    protected void renderItem(int i, int x, int y, int h, Tezzelator t, int mouseX, int mouseY) {
      if (RealmsMainScreen.trialsAvailable) {
        if (i == 0) {
          renderTrialItem(i, x, y);
          return;
        } 
        i--;
      } 
      if (i < RealmsMainScreen.this.realmsServers.size())
        renderMcoServerItem(i, x, y); 
    }
    
    private void renderTrialItem(int i, int x, int y) {
      int textColor, ry = y + 12;
      int index = 0;
      String msg = RealmsScreen.getLocalizedString("mco.trial.message");
      boolean hovered = false;
      if (x <= xm() && xm() <= getScrollbarPosition() && y <= ym() && ym() <= y + 32)
        hovered = true; 
      float scale = 0.5F + (1.0F + RealmsMth.sin(RealmsMainScreen.this.animTick * 0.25F)) * 0.25F;
      if (hovered) {
        textColor = 0xFF | (int)(127.0F * scale) << 16 | (int)(255.0F * scale) << 8 | (int)(127.0F * scale);
      } else {
        textColor = 0xFF000000 | (int)(127.0F * scale) << 16 | (int)(255.0F * scale) << 8 | (int)(127.0F * scale);
      } 
      for (String s : msg.split("\\\\n")) {
        RealmsMainScreen.this.drawCenteredString(s, RealmsMainScreen.this.width() / 2, ry + index, textColor);
        index += 10;
      } 
    }
    
    private void renderMcoServerItem(int i, int x, int y) {
      final RealmsServer serverData = RealmsMainScreen.this.realmsServers.get(i);
      int nameColor = RealmsMainScreen.this.isSelfOwnedServer(serverData) ? 8388479 : 16777215;
      if (serverData.state == RealmsServer.State.UNINITIALIZED) {
        RealmsScreen.bind("realms:textures/gui/realms/world_icon.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(3008);
        GL11.glPushMatrix();
        RealmsScreen.blit(x + 10, y + 6, 0.0F, 0.0F, 40, 20, 40.0F, 20.0F);
        GL11.glPopMatrix();
        float scale = 0.5F + (1.0F + RealmsMth.sin(RealmsMainScreen.this.animTick * 0.25F)) * 0.25F;
        int textColor = 0xFF000000 | (int)(127.0F * scale) << 16 | (int)(255.0F * scale) << 8 | (int)(127.0F * scale);
        RealmsMainScreen.this.drawCenteredString(RealmsScreen.getLocalizedString("mco.selectServer.uninitialized"), x + 10 + 40 + 75, y + 12, textColor);
        return;
      } 
      if (serverData.shouldPing(Realms.currentTimeMillis())) {
        serverData.serverPing.lastPingSnapshot = Realms.currentTimeMillis();
        RealmsMainScreen.THREAD_POOL.submit(new Runnable() {
              public void run() {
                try {
                  RealmsMainScreen.statusPinger.pingServer(serverData.ip, serverData.serverPing);
                } catch (UnknownHostException e) {
                  RealmsMainScreen.LOGGER.error("Pinger: Could not resolve host");
                } 
              }
            });
      } 
      RealmsMainScreen.this.drawString(serverData.getName(), x + 2, y + 1, nameColor);
      int dx = 207;
      int dy = 1;
      if (serverData.expired) {
        RealmsMainScreen.this.drawExpired(x + dx, y + dy, xm(), ym());
      } else if (serverData.state == RealmsServer.State.CLOSED) {
        RealmsMainScreen.this.drawClose(x + dx, y + dy, xm(), ym());
      } else if (RealmsMainScreen.this.isSelfOwnedServer(serverData) && serverData.daysLeft < 7) {
        showStatus(x - 14, y, serverData);
        RealmsMainScreen.this.drawExpiring(x + dx, y + dy, xm(), ym(), serverData.daysLeft);
      } else if (serverData.state == RealmsServer.State.OPEN) {
        RealmsMainScreen.this.drawOpen(x + dx, y + dy, xm(), ym());
        showStatus(x - 14, y, serverData);
      } else if (serverData.state == RealmsServer.State.ADMIN_LOCK) {
        RealmsMainScreen.this.drawLocked(x + dx, y + dy, xm(), ym());
      } 
      String noPlayers = "0";
      if (!serverData.serverPing.nrOfPlayers.equals(noPlayers)) {
        String coloredNumPlayers = ChatFormatting.GRAY + "" + serverData.serverPing.nrOfPlayers;
        RealmsMainScreen.this.drawString(coloredNumPlayers, x + 200 - RealmsMainScreen.this.fontWidth(coloredNumPlayers), y + 1, 8421504);
        if (xm() >= x + 200 - RealmsMainScreen.this.fontWidth(coloredNumPlayers) && xm() <= x + 200 && ym() >= y + 1 && ym() <= y + 9 && ym() < RealmsMainScreen.this.height() - 64 && ym() > 32)
          RealmsMainScreen.this.toolTip = serverData.serverPing.playerList; 
      } 
      if (serverData.worldType.equals(RealmsServer.WorldType.MINIGAME)) {
        int motdColor = 9206892;
        if (RealmsMainScreen.this.animTick % 10 < 5)
          motdColor = 13413468; 
        String miniGameStr = RealmsScreen.getLocalizedString("mco.selectServer.minigame") + " ";
        int mgWidth = RealmsMainScreen.this.fontWidth(miniGameStr);
        RealmsMainScreen.this.drawString(miniGameStr, x + 2, y + 12, motdColor);
        RealmsMainScreen.this.drawString(serverData.getMinigameName(), x + 2 + mgWidth, y + 12, 7105644);
      } else {
        RealmsMainScreen.this.drawString(serverData.getDescription(), x + 2, y + 12, 7105644);
      } 
      RealmsMainScreen.this.drawString(serverData.owner, x + 2, y + 12 + 11, 5000268);
      RealmsScreen.bindFace(serverData.ownerUUID, serverData.owner);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      RealmsScreen.blit(x - 36, y, 8.0F, 8.0F, 8, 8, 32, 32, 64.0F, 64.0F);
      RealmsScreen.blit(x - 36, y, 40.0F, 8.0F, 8, 8, 32, 32, 64.0F, 64.0F);
    }
    
    private void showStatus(int x, int y, RealmsServer serverData) {
      if (serverData.ip == null)
        return; 
      if (serverData.status != null)
        RealmsMainScreen.this.drawString(serverData.status, x + 215 - RealmsMainScreen.this.fontWidth(serverData.status), y + 1, 8421504); 
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      RealmsScreen.bind("textures/gui/icons.png");
    }
  }
  
  private boolean isSelfOwnedServer(RealmsServer serverData) {
    return (serverData.ownerUUID != null && serverData.ownerUUID.equals(Realms.getUUID()));
  }
  
  private boolean isSelfOwnedNonExpiredServer(RealmsServer serverData) {
    return (serverData.ownerUUID != null && serverData.ownerUUID.equals(Realms.getUUID()) && !serverData.expired);
  }
  
  private void drawExpired(int x, int y, int xm, int ym) {
    RealmsScreen.bind("realms:textures/gui/realms/expired_icon.png");
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glPushMatrix();
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    RealmsScreen.blit(x * 2, y * 2, 0.0F, 0.0F, 15, 15, 15.0F, 15.0F);
    GL11.glPopMatrix();
    if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9 && ym < height() - 64 && ym > 32)
      this.toolTip = getLocalizedString("mco.selectServer.expired"); 
  }
  
  private void drawExpiring(int x, int y, int xm, int ym, int daysLeft) {
    if (this.animTick % 20 < 10) {
      RealmsScreen.bind("realms:textures/gui/realms/on_icon.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      RealmsScreen.blit(x * 2, y * 2, 0.0F, 0.0F, 15, 15, 15.0F, 15.0F);
      GL11.glPopMatrix();
    } 
    if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9 && ym < height() - 64 && ym > 32)
      if (daysLeft == 0) {
        this.toolTip = getLocalizedString("mco.selectServer.expires.soon");
      } else if (daysLeft == 1) {
        this.toolTip = getLocalizedString("mco.selectServer.expires.day");
      } else {
        this.toolTip = getLocalizedString("mco.selectServer.expires.days", new Object[] { Integer.valueOf(daysLeft) });
      }  
  }
  
  private void drawOpen(int x, int y, int xm, int ym) {
    RealmsScreen.bind("realms:textures/gui/realms/on_icon.png");
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glPushMatrix();
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    RealmsScreen.blit(x * 2, y * 2, 0.0F, 0.0F, 15, 15, 15.0F, 15.0F);
    GL11.glPopMatrix();
    if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9 && ym < height() - 64 && ym > 32)
      this.toolTip = getLocalizedString("mco.selectServer.open"); 
  }
  
  private void drawClose(int x, int y, int xm, int ym) {
    RealmsScreen.bind("realms:textures/gui/realms/off_icon.png");
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glPushMatrix();
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    RealmsScreen.blit(x * 2, y * 2, 0.0F, 0.0F, 15, 15, 15.0F, 15.0F);
    GL11.glPopMatrix();
    if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9 && ym < height() - 64 && ym > 32)
      this.toolTip = getLocalizedString("mco.selectServer.closed"); 
  }
  
  private void drawLocked(int x, int y, int xm, int ym) {
    RealmsScreen.bind("realms:textures/gui/realms/off_icon.png");
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glPushMatrix();
    GL11.glScalef(0.5F, 0.5F, 0.5F);
    RealmsScreen.blit(x * 2, y * 2, 0.0F, 0.0F, 15, 15, 15.0F, 15.0F);
    GL11.glPopMatrix();
    if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9 && ym < height() - 64 && ym > 32)
      this.toolTip = getLocalizedString("mco.selectServer.locked"); 
  }
  
  protected void renderMousehoverTooltip(String msg, int x, int y) {
    if (msg == null)
      return; 
    int rx = x + 12;
    int ry = y - 12;
    int index = 0;
    int width = 0;
    for (String s : msg.split("\n")) {
      int the_width = fontWidth(s);
      if (the_width > width)
        width = the_width; 
    } 
    for (String s : msg.split("\n")) {
      fillGradient(rx - 3, ry - ((index == 0) ? 3 : 0) + index, rx + width + 3, ry + 8 + 3 + index, -1073741824, -1073741824);
      fontDrawShadow(s, rx, ry + index, 16777215);
      index += 10;
    } 
  }
  
  private void renderLink(int xm, int ym) {
    String text = getLocalizedString("mco.selectServer.whatisrealms");
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glPushMatrix();
    int textWidth = fontWidth(text);
    int leftPadding = 10;
    int topPadding = 12;
    int x1 = leftPadding;
    int x2 = x1 + textWidth + 1;
    int y1 = topPadding;
    int y2 = y1 + fontLineHeight();
    GL11.glTranslatef(x1, y1, 0.0F);
    if (x1 <= xm && xm <= x2 && y1 <= ym && ym <= y2) {
      this.onLink = true;
      drawString(text, 0, 0, 7107012);
    } else {
      this.onLink = false;
      drawString(text, 0, 0, 3368635);
    } 
    GL11.glPopMatrix();
  }
  
  private void renderStage() {
    String text = "STAGE!";
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glPushMatrix();
    GL11.glTranslatef((width() / 2 - 25), 20.0F, 0.0F);
    GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
    GL11.glScalef(1.5F, 1.5F, 1.5F);
    drawString(text, 0, 0, -256);
    GL11.glPopMatrix();
  }
  
  public RealmsScreen newScreen() {
    return new RealmsMainScreen(this.lastScreen);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\RealmsMainScreen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */