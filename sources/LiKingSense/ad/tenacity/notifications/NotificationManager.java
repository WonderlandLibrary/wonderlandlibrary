/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 */
package ad.tenacity.notifications;

import ad.tenacity.notifications.Notification;
import ad.tenacity.notifications.NotificationType;
import java.util.concurrent.CopyOnWriteArrayList;
import net.ccbluex.liquidbounce.utils.render.miku.animations.Animation;
import net.ccbluex.liquidbounce.utils.render.miku.animations.Direction;
import net.ccbluex.liquidbounce.utils.render.miku.animations.impl.DecelerateAnimation;
import net.minecraft.client.gui.ScaledResolution;

public class NotificationManager {
    private final float spacing = 10.0f;
    private final float widthSpacing = 25.0f;
    private static final CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList();
    Animation downAnimation = null;

    public void drawNotifications(ScaledResolution sr) {
        int count = 0;
        for (Notification notification2 : notifications) {
            if (notification2.timerUtil.hasTimeElapsed((long)notification2.getMaxTime(), false)) {
                if (notification2.getAnimation() != null) {
                    if (notification2.getAnimation().isDone()) {
                        notifications.remove(notification2);
                        this.downAnimation = new DecelerateAnimation(225, 1.0, Direction.FORWARDS);
                        continue;
                    }
                } else {
                    NotificationManager.vanish(notification2);
                }
            } else if (notification2.getAnimation() != null && notification2.getAnimation().isDone()) {
                notification2.stopAnimation();
            }
            float notifWidth = notification2.getWidth() + 25.0f;
            float notifX = (float)sr.func_78326_a() - (notifWidth + 5.0f);
            if (count == 0) {
                notification2.notificationY = sr.func_78328_b();
            }
            notification2.notificationY = NotificationManager.notifications.get((int)Math.max((int)(count - 1), (int)0)).notificationY - 10.0f - notification2.getHeight();
            if (notification2.isAnimating()) {
                notifX = (float)((double)notifX + (double)notifWidth * notification2.getAnimation().getOutput());
            }
            if (this.downAnimation != null) {
                if (this.downAnimation.isDone()) {
                    this.downAnimation = null;
                    return;
                }
                float newY = (float)sr.func_78328_b() - (10.0f + notification2.getHeight()) * (float)(count + 2);
                notification2.notificationY = (float)((double)newY + (double)(notification2.getHeight() + 10.0f) * this.downAnimation.getOutput());
            }
            this.notificationDraw(notifX, notification2.notificationY, notifWidth, notification2.getHeight(), notification2);
            ++count;
        }
    }

    /*
     * Exception decompiling
     */
    public void notificationDraw(float x, float y, float width, float height, Notification notification) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl18 : INVOKESPECIAL - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public void blurNotifs(ScaledResolution sr) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl6 : ALOAD_3 - null : trying to set 2 previously set to 0
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public static void post(NotificationType type, String title, String description) {
        NotificationManager.post(new Notification(type, title, description));
    }

    public static void post(NotificationType type, String title, String description, float time) {
        NotificationManager.post(new Notification(type, title, description, time));
    }

    private static void post(Notification notification2) {
        notifications.add(notification2);
        notification2.startAnimation(new DecelerateAnimation(225, 1.0, Direction.BACKWARDS));
    }

    public static void vanish(Notification notification2) {
        notification2.startAnimation(new DecelerateAnimation(225, 1.0, Direction.FORWARDS));
    }
}

