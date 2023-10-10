// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.ConfigurationScheduler;
import java.util.Objects;
import org.apache.logging.log4j.core.config.CronScheduledFuture;
import java.util.Date;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.util.CronExpression;
import org.apache.logging.log4j.core.config.Scheduled;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "CronTriggeringPolicy", category = "Core", printObject = true)
@Scheduled
public final class CronTriggeringPolicy extends AbstractTriggeringPolicy
{
    private static final String defaultSchedule = "0 0 0 * * ?";
    private RollingFileManager manager;
    private final CronExpression cronExpression;
    private final Configuration configuration;
    private final boolean checkOnStartup;
    private volatile Date lastRollDate;
    private CronScheduledFuture<?> future;
    
    private CronTriggeringPolicy(final CronExpression schedule, final boolean checkOnStartup, final Configuration configuration) {
        this.cronExpression = Objects.requireNonNull(schedule, "schedule");
        this.configuration = Objects.requireNonNull(configuration, "configuration");
        this.checkOnStartup = checkOnStartup;
    }
    
    @Override
    public void initialize(final RollingFileManager aManager) {
        this.manager = aManager;
        final Date now = new Date();
        final Date lastRollForFile = this.cronExpression.getPrevFireTime(new Date(this.manager.getFileTime()));
        final Date lastRegularRoll = this.cronExpression.getPrevFireTime(new Date());
        aManager.getPatternProcessor().setCurrentFileTime(lastRegularRoll.getTime());
        CronTriggeringPolicy.LOGGER.debug("LastRollForFile {}, LastRegularRole {}", lastRollForFile, lastRegularRoll);
        aManager.getPatternProcessor().setPrevFileTime(lastRegularRoll.getTime());
        aManager.getPatternProcessor().setTimeBased(true);
        if (this.checkOnStartup && lastRollForFile != null && lastRegularRoll != null && lastRollForFile.before(lastRegularRoll)) {
            this.lastRollDate = lastRollForFile;
            this.rollover();
        }
        final ConfigurationScheduler scheduler = this.configuration.getScheduler();
        if (!scheduler.isExecutorServiceSet()) {
            scheduler.incrementScheduledItems();
        }
        if (!scheduler.isStarted()) {
            scheduler.start();
        }
        this.lastRollDate = lastRegularRoll;
        this.future = scheduler.scheduleWithCron(this.cronExpression, now, new CronTrigger());
        CronTriggeringPolicy.LOGGER.debug(scheduler.toString());
    }
    
    @Override
    public boolean isTriggeringEvent(final LogEvent event) {
        return false;
    }
    
    public CronExpression getCronExpression() {
        return this.cronExpression;
    }
    
    @PluginFactory
    public static CronTriggeringPolicy createPolicy(@PluginConfiguration final Configuration configuration, @PluginAttribute("evaluateOnStartup") final String evaluateOnStartup, @PluginAttribute("schedule") final String schedule) {
        final boolean checkOnStartup = Boolean.parseBoolean(evaluateOnStartup);
        CronExpression cronExpression;
        if (schedule == null) {
            CronTriggeringPolicy.LOGGER.info("No schedule specified, defaulting to Daily");
            cronExpression = getSchedule("0 0 0 * * ?");
        }
        else {
            cronExpression = getSchedule(schedule);
            if (cronExpression == null) {
                CronTriggeringPolicy.LOGGER.error("Invalid expression specified. Defaulting to Daily");
                cronExpression = getSchedule("0 0 0 * * ?");
            }
        }
        return new CronTriggeringPolicy(cronExpression, checkOnStartup, configuration);
    }
    
    private static CronExpression getSchedule(final String expression) {
        try {
            return new CronExpression(expression);
        }
        catch (ParseException pe) {
            CronTriggeringPolicy.LOGGER.error("Invalid cron expression - " + expression, pe);
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
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        final boolean stopped = this.stop(this.future);
        this.setStopped();
        return stopped;
    }
    
    @Override
    public String toString() {
        return "CronTriggeringPolicy(schedule=" + this.cronExpression.getCronExpression() + ")";
    }
    
    private class CronTrigger implements Runnable
    {
        @Override
        public void run() {
            CronTriggeringPolicy.this.rollover();
        }
    }
}
