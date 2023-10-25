/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.ServerActivity;
import com.mojang.realmsclient.dto.ServerActivityList;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.util.RealmsTextureManager;
import com.mojang.realmsclient.util.RealmsUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsDefaultVertexFormat;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsScrolledSelectionList;
import net.minecraft.realms.Tezzelator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class RealmsActivityScreen
extends RealmsScreen {
    private static final Logger LOGGER = LogManager.getLogger();
    private final RealmsScreen lastScreen;
    private final RealmsServer serverData;
    private volatile List<ActivityRow> activityMap = new ArrayList<ActivityRow>();
    private DetailsList list;
    private int matrixWidth;
    private String toolTip;
    private volatile List<Day> dayList = new ArrayList<Day>();
    private final List<Color> colors = Arrays.asList(new Color(79, 243, 29), new Color(243, 175, 29), new Color(243, 29, 190), new Color(29, 165, 243), new Color(29, 243, 130), new Color(243, 29, 64), new Color(29, 74, 243));
    private int colorIndex;
    private long periodInMillis;
    private int maxKeyWidth;
    private Boolean noActivity = false;
    private int activityPoint;
    private int dayWidth;
    private double hourWidth;
    private double minuteWidth;
    private final int BUTTON_BACK_ID = 0;
    private static final String USER_ICON_LOCATION = "realms:textures/gui/realms/user_icon.png";

    public RealmsActivityScreen(RealmsScreen lastScreen, RealmsServer serverData) {
        this.lastScreen = lastScreen;
        this.serverData = serverData;
        this.getActivities();
    }

    @Override
    public void mouseEvent() {
        super.mouseEvent();
        this.list.mouseEvent();
    }

    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.matrixWidth = this.width();
        this.list = new DetailsList();
        this.buttonsAdd(RealmsActivityScreen.newButton(0, this.width() / 2 - 100, this.height() - 30, 200, 20, RealmsActivityScreen.getLocalizedString("gui.back")));
    }

    private Color getColor() {
        if (this.colorIndex > this.colors.size() - 1) {
            this.colorIndex = 0;
        }
        return this.colors.get(this.colorIndex++);
    }

    private void getActivities() {
        new Thread(){

            @Override
            public void run() {
                RealmsClient client = RealmsClient.createRealmsClient();
                try {
                    Day theDay;
                    String day;
                    ServerActivityList activities = client.getActivity(((RealmsActivityScreen)RealmsActivityScreen.this).serverData.id);
                    RealmsActivityScreen.this.activityMap = RealmsActivityScreen.this.convertToActivityMatrix(activities);
                    ArrayList<Day> tempDayList = new ArrayList<Day>();
                    for (ActivityRow row : RealmsActivityScreen.this.activityMap) {
                        for (Activity activity : row.activities) {
                            day = new SimpleDateFormat("dd/MM").format(new Date(activity.start));
                            theDay = new Day(day, activity.start);
                            if (tempDayList.contains(theDay)) continue;
                            tempDayList.add(theDay);
                        }
                    }
                    Collections.sort(tempDayList);
                    for (ActivityRow row : RealmsActivityScreen.this.activityMap) {
                        for (Activity activity : row.activities) {
                            day = new SimpleDateFormat("dd/MM").format(new Date(activity.start));
                            theDay = new Day(day, activity.start);
                            activity.dayIndex = tempDayList.indexOf(theDay) + 1;
                        }
                    }
                    RealmsActivityScreen.this.dayList = tempDayList;
                }
                catch (RealmsServiceException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private List<ActivityRow> convertToActivityMatrix(ServerActivityList serverActivityList) {
        ArrayList<ActivityRow> activityRows = Lists.newArrayList();
        this.periodInMillis = serverActivityList.periodInMillis;
        long base = System.currentTimeMillis() - serverActivityList.periodInMillis;
        for (ServerActivity sa : serverActivityList.serverActivities) {
            ActivityRow activityRow = this.find(sa.profileUuid, activityRows);
            Calendar joinTime = Calendar.getInstance(TimeZone.getDefault());
            joinTime.setTimeInMillis(sa.joinTime);
            Calendar leaveTime = Calendar.getInstance(TimeZone.getDefault());
            leaveTime.setTimeInMillis(sa.leaveTime);
            Activity e = new Activity(base, joinTime.getTimeInMillis(), leaveTime.getTimeInMillis());
            if (activityRow == null) {
                String name = "";
                try {
                    name = RealmsUtil.uuidToName(sa.profileUuid);
                }
                catch (Exception exception) {
                    LOGGER.error("Could not get name for " + sa.profileUuid, (Throwable)exception);
                    continue;
                }
                activityRow = new ActivityRow(sa.profileUuid, new ArrayList<Activity>(), name, sa.profileUuid);
                activityRow.activities.add(e);
                activityRows.add(activityRow);
                continue;
            }
            activityRow.activities.add(e);
        }
        Collections.sort(activityRows);
        for (ActivityRow row : activityRows) {
            row.color = this.getColor();
            Collections.sort(row.activities);
        }
        this.noActivity = activityRows.size() == 0;
        return activityRows;
    }

    private ActivityRow find(String key, List<ActivityRow> rows) {
        for (ActivityRow row : rows) {
            if (!row.key.equals(key)) continue;
            return row;
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void buttonClicked(RealmsButton button) {
        if (button.id() == 0) {
            Realms.setScreen(this.lastScreen);
        }
    }

    @Override
    public void keyPressed(char ch, int eventKey) {
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }

    @Override
    public void render(int xm, int ym, float a) {
        this.toolTip = null;
        this.renderBackground();
        for (ActivityRow row : this.activityMap) {
            int keyWidth = this.fontWidth(row.name) + 2;
            if (keyWidth <= this.maxKeyWidth) continue;
            this.maxKeyWidth = keyWidth + 10;
        }
        int keyRightPadding = 25;
        this.activityPoint = this.maxKeyWidth + 25;
        int spaceLeft = this.matrixWidth - this.activityPoint - 10;
        int days = this.dayList.size() < 1 ? 1 : this.dayList.size();
        this.dayWidth = spaceLeft / days;
        this.hourWidth = (double)this.dayWidth / 24.0;
        this.minuteWidth = this.hourWidth / 60.0;
        if (this.list != null) {
            this.list.render(xm, ym, a);
        }
        if (this.activityMap != null && this.activityMap.size() > 0) {
            Tezzelator t = Tezzelator.instance;
            GL11.glDisable(3553);
            t.begin(7, RealmsDefaultVertexFormat.POSITION_COLOR);
            t.vertex(this.activityPoint, this.height() - 40, 0.0).color(128, 128, 128, 255).endVertex();
            t.vertex(this.activityPoint + 1, this.height() - 40, 0.0).color(128, 128, 128, 255).endVertex();
            t.vertex(this.activityPoint + 1, 30.0, 0.0).color(128, 128, 128, 255).endVertex();
            t.vertex(this.activityPoint, 30.0, 0.0).color(128, 128, 128, 255).endVertex();
            t.end();
            GL11.glEnable(3553);
            for (Day day : this.dayList) {
                int daysIndex = this.dayList.indexOf(day) + 1;
                this.drawString(day.day, this.activityPoint + (daysIndex - 1) * this.dayWidth + (this.dayWidth - this.fontWidth(day.day)) / 2 + 2, this.height() - 52, 0xFFFFFF);
                GL11.glDisable(3553);
                t.begin(7, RealmsDefaultVertexFormat.POSITION_COLOR);
                t.vertex(this.activityPoint + daysIndex * this.dayWidth, this.height() - 40, 0.0).color(128, 128, 128, 255).endVertex();
                t.vertex(this.activityPoint + daysIndex * this.dayWidth + 1, this.height() - 40, 0.0).color(128, 128, 128, 255).endVertex();
                t.vertex(this.activityPoint + daysIndex * this.dayWidth + 1, 30.0, 0.0).color(128, 128, 128, 255).endVertex();
                t.vertex(this.activityPoint + daysIndex * this.dayWidth, 30.0, 0.0).color(128, 128, 128, 255).endVertex();
                t.end();
                GL11.glEnable(3553);
            }
        }
        super.render(xm, ym, a);
        this.drawCenteredString(RealmsActivityScreen.getLocalizedString("mco.activity.title"), this.width() / 2, 10, 0xFFFFFF);
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(this.toolTip, xm, ym);
        }
        if (this.noActivity.booleanValue()) {
            this.drawCenteredString(RealmsActivityScreen.getLocalizedString("mco.activity.noactivity", TimeUnit.DAYS.convert(this.periodInMillis, TimeUnit.MILLISECONDS)), this.width() / 2, this.height() / 2 - 20, 0xFFFFFF);
        }
    }

    protected void renderMousehoverTooltip(String msg, int x, int y) {
        if (msg == null) {
            return;
        }
        int index = 0;
        int width = 0;
        for (String s : msg.split("\n")) {
            int theWidth = this.fontWidth(s);
            if (theWidth <= width) continue;
            width = theWidth;
        }
        int rx = x - width - 5;
        int ry = y;
        if (rx < 0) {
            rx = x + 12;
        }
        for (String s : msg.split("\n")) {
            this.fillGradient(rx - 3, ry - (index == 0 ? 3 : 0) + index, rx + width + 3, ry + 8 + 3 + index, -1073741824, -1073741824);
            this.fontDrawShadow(s, rx, ry + index, -1);
            index += 10;
        }
    }

    class DetailsList
    extends RealmsScrolledSelectionList {
        public DetailsList() {
            super(RealmsActivityScreen.this.width(), RealmsActivityScreen.this.height(), 30, RealmsActivityScreen.this.height() - 40, RealmsActivityScreen.this.fontLineHeight() + 1);
        }

        @Override
        public int getItemCount() {
            return RealmsActivityScreen.this.activityMap.size();
        }

        @Override
        public void selectItem(int item, boolean doubleClick, int xMouse, int yMouse) {
        }

        @Override
        public boolean isSelectedItem(int item) {
            return false;
        }

        @Override
        public int getMaxPosition() {
            return this.getItemCount() * (RealmsActivityScreen.this.fontLineHeight() + 1) + 15;
        }

        @Override
        protected void renderItem(int i, int x, int y, int h, Tezzelator t, int mouseX, int mouseY) {
            if (RealmsActivityScreen.this.activityMap != null && RealmsActivityScreen.this.activityMap.size() > i) {
                ActivityRow row = (ActivityRow)RealmsActivityScreen.this.activityMap.get(i);
                RealmsActivityScreen.this.drawString(row.name, 20, y, ((ActivityRow)((RealmsActivityScreen)RealmsActivityScreen.this).activityMap.get((int)i)).uuid.equals(Realms.getUUID()) ? 0x7FFF7F : 0xFFFFFF);
                int r = row.color.r;
                int g = row.color.g;
                int b = row.color.b;
                GL11.glDisable(3553);
                t.begin(7, RealmsDefaultVertexFormat.POSITION_COLOR);
                t.vertex(RealmsActivityScreen.this.activityPoint - 8, (double)y + 6.5, 0.0).color(r, g, b, 255).endVertex();
                t.vertex(RealmsActivityScreen.this.activityPoint - 3, (double)y + 6.5, 0.0).color(r, g, b, 255).endVertex();
                t.vertex(RealmsActivityScreen.this.activityPoint - 3, (double)y + 1.5, 0.0).color(r, g, b, 255).endVertex();
                t.vertex(RealmsActivityScreen.this.activityPoint - 8, (double)y + 1.5, 0.0).color(r, g, b, 255).endVertex();
                t.end();
                GL11.glEnable(3553);
                ArrayList<ActivityRender> toRender = new ArrayList<ActivityRender>();
                for (Activity activity : row.activities) {
                    int minute = activity.minuteIndice();
                    int hour = activity.hourIndice();
                    double itemWidth = RealmsActivityScreen.this.minuteWidth * (double)TimeUnit.MINUTES.convert(activity.end - activity.start, TimeUnit.MILLISECONDS);
                    if (itemWidth < 3.0) {
                        itemWidth = 3.0;
                    }
                    double pos = (double)(RealmsActivityScreen.this.activityPoint + (RealmsActivityScreen.this.dayWidth * activity.dayIndex - RealmsActivityScreen.this.dayWidth)) + (double)hour * RealmsActivityScreen.this.hourWidth + (double)minute * RealmsActivityScreen.this.minuteWidth;
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    Date startDate = new Date(activity.start);
                    Date endDate = new Date(activity.end);
                    int length = (int)Math.ceil((double)TimeUnit.SECONDS.convert(activity.end - activity.start, TimeUnit.MILLISECONDS) / 60.0);
                    if (length < 1) {
                        length = 1;
                    }
                    String tooltip = "[" + format.format(startDate) + " - " + format.format(endDate) + "] " + length + (length > 1 ? " minutes" : " minute");
                    boolean exists = false;
                    for (ActivityRender render : toRender) {
                        if (!(render.start + render.width >= pos - 0.5)) continue;
                        double overlap = render.start + render.width - pos;
                        double padding = Math.max(0.0, pos - (render.start + render.width));
                        render.width = render.width - Math.max(0.0, overlap) + itemWidth + padding;
                        render.tooltip = render.tooltip + "\n" + tooltip;
                        exists = true;
                        break;
                    }
                    if (exists) continue;
                    toRender.add(new ActivityRender(pos, itemWidth, tooltip));
                }
                for (ActivityRender render : toRender) {
                    GL11.glDisable(3553);
                    t.begin(7, RealmsDefaultVertexFormat.POSITION_COLOR);
                    t.vertex(render.start, (double)y + 6.5, 0.0).color(r, g, b, 255).endVertex();
                    t.vertex(render.start + render.width, (double)y + 6.5, 0.0).color(r, g, b, 255).endVertex();
                    t.vertex(render.start + render.width, (double)y + 1.5, 0.0).color(r, g, b, 255).endVertex();
                    t.vertex(render.start, (double)y + 1.5, 0.0).color(r, g, b, 255).endVertex();
                    t.end();
                    GL11.glEnable(3553);
                    if (!((double)this.xm() >= render.start) || !((double)this.xm() <= render.start + render.width) || !((double)this.ym() >= (double)y + 1.5) || !((double)this.ym() <= (double)y + 6.5)) continue;
                    RealmsActivityScreen.this.toolTip = render.tooltip.trim();
                }
                RealmsScreen.bind(RealmsActivityScreen.USER_ICON_LOCATION);
                RealmsTextureManager.bindFace(((ActivityRow)((RealmsActivityScreen)RealmsActivityScreen.this).activityMap.get((int)i)).uuid);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                RealmsScreen.blit(10, y, 8.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
                RealmsScreen.blit(10, y, 40.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
            }
        }

        @Override
        public int getScrollbarPosition() {
            return this.width() - 7;
        }
    }

    static class ActivityRender {
        double start;
        double width;
        String tooltip;

        private ActivityRender(double start, double width, String tooltip) {
            this.start = start;
            this.width = width;
            this.tooltip = tooltip;
        }
    }

    static class Activity
    implements Comparable<Activity> {
        long base;
        long start;
        long end;
        int dayIndex;

        private Activity(long base, long start, long end) {
            this.base = base;
            this.start = start;
            this.end = end;
        }

        @Override
        public int compareTo(Activity o) {
            return (int)(this.start - o.start);
        }

        public int hourIndice() {
            String hour = new SimpleDateFormat("HH").format(new Date(this.start));
            return Integer.parseInt(hour);
        }

        public int minuteIndice() {
            String minute = new SimpleDateFormat("mm").format(new Date(this.start));
            return Integer.parseInt(minute);
        }
    }

    static class ActivityRow
    implements Comparable<ActivityRow> {
        String key;
        List<Activity> activities;
        Color color;
        String name;
        String uuid;

        @Override
        public int compareTo(ActivityRow o) {
            return this.name.compareTo(o.name);
        }

        ActivityRow(String key, List<Activity> activities, String name, String uuid) {
            this.key = key;
            this.activities = activities;
            this.name = name;
            this.uuid = uuid;
        }
    }

    static class Day
    implements Comparable<Day> {
        String day;
        Long timestamp;

        @Override
        public int compareTo(Day o) {
            return this.timestamp.compareTo(o.timestamp);
        }

        Day(String day, Long timestamp) {
            this.day = day;
            this.timestamp = timestamp;
        }

        public boolean equals(Object d) {
            if (!(d instanceof Day)) {
                return false;
            }
            Day that = (Day)d;
            return this.day.equals(that.day);
        }
    }

    static class Color {
        int r;
        int g;
        int b;

        Color(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
}

