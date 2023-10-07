package dev.rise.notifications;

import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
public enum NotificationType {
    NOTIFICATION,
    WARNING,
    ERROR
}
