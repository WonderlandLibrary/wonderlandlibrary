/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rolling.AbstractTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationScheduler;
import org.apache.logging.log4j.core.config.CronScheduledFuture;
import org.apache.logging.log4j.core.config.Scheduled;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.util.CronExpression;

@Plugin(name="CronTriggeringPolicy", category="Core", printObject=true)
@Scheduled
public final class CronTriggeringPolicy
extends AbstractTriggeringPolicy {
    private static final String defaultSchedule = "0 0 0 * * ?";
    private RollingFileManager manager;
    private final CronExpression cronExpression;
    private final Configuration configuration;
    private final boolean checkOnStartup;
    private volatile Date lastRollDate;
    private CronScheduledFuture<?> future;

    private CronTriggeringPolicy(CronExpression schedule, boolean checkOnStartup, Configuration configuration) {
        this.cronExpression = Objects.requireNonNull(schedule, "schedule");
        this.configuration = Objects.requireNonNull(configuration, "configuration");
        this.checkOnStartup = checkOnStartup;
    }

    @Override
    public void initialize(RollingFileManager aManager) {
        ConfigurationScheduler scheduler;
        this.manager = aManager;
        Date now = new Date();
        Date lastRollForFile = this.cronExpression.getPrevFireTime(new Date(this.manager.getFileTime()));
        Date lastRegularRoll = this.cronExpression.getPrevFireTime(new Date());
        aManager.getPatternProcessor().setCurrentFileTime(lastRegularRoll.getTime());
        LOGGER.debug("LastRollForFile {}, LastRegularRole {}", lastRollForFile, lastRegularRoll);
        aManager.getPatternProcessor().setPrevFileTime(lastRegularRoll.getTime());
        aManager.getPatternProcessor().setTimeBased(true);
        if (this.checkOnStartup && lastRollForFile != null && lastRegularRoll != null && lastRollForFile.before(lastRegularRoll)) {
            this.lastRollDate = lastRollForFile;
            this.rollover();
        }
        if (!(scheduler = this.configuration.getScheduler()).isExecutorServiceSet()) {
            scheduler.incrementScheduledItems();
        }
        if (!scheduler.isStarted()) {
            scheduler.start();
        }
        this.lastRollDate = lastRegularRoll;
        this.future = scheduler.scheduleWithCron(this.cronExpression, now, new CronTrigger());
        LOGGER.debug(scheduler.toString());
    }

    @Override
    public boolean isTriggeringEvent(LogEvent event) {
        return false;
    }

    public CronExpression getCronExpression() {
        return this.cronExpression;
    }

    @PluginFactory
    public static CronTriggeringPolicy createPolicy(@PluginConfiguration Configuration configuration, @PluginAttribute(value="evaluateOnStartup") String evaluateOnStartup, @PluginAttribute(value="schedule") String schedule) {
        CronExpression cronExpression;
        boolean checkOnStartup = Boolean.parseBoolean(evaluateOnStartup);
        if (schedule == null) {
            LOGGER.info("No schedule specified, defaulting to Daily");
            cronExpression = CronTriggeringPolicy.getSchedule(defaultSchedule);
        } else {
            cronExpression = CronTriggeringPolicy.getSchedule(schedule);
            if (cronExpression == null) {
                LOGGER.error("Invalid expression specified. Defaulting to Daily");
                cronExpression = CronTriggeringPolicy.getSchedule(defaultSchedule);
            }
        }
        return new CronTriggeringPolicy(cronExpression, checkOnStartup, configuration);
    }

    private static CronExpression getSchedule(String expression) {
        try {
            return new CronExpression(expression);
        }
        catch (ParseException pe) {
            LOGGER.error("Invalid cron expression - " + expression, (Throwable)pe);
            return null;
        }
    }

    private void rollover() {
        this.manager.rollover(this.cronExpression.getPrevFireTime(new Date()), this.lastRollDate);
        if (this.future != null) {
            this.lastRollDate = this.future.getFireTime();
        }
    }

    @Override
    public boolean stop(long timeout, TimeUnit timeUnit) {
        this.setStopping();
        boolean stopped = this.stop(this.future);
        this.setStopped();
        return stopped;
    }

    public String toString() {
        return "CronTriggeringPolicy(schedule=" + this.cronExpression.getCronExpression() + ")";
    }

    private class CronTrigger
    implements Runnable {
        private CronTrigger() {
        }

        @Override
        public void run() {
            CronTriggeringPolicy.this.rollover();
        }
    }
}

