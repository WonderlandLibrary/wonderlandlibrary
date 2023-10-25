/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.crash.CrashReport;

public class ReportedException
extends RuntimeException {
    private final CrashReport theReportedExceptionCrashReport;

    public CrashReport getCrashReport() {
        return this.theReportedExceptionCrashReport;
    }

    @Override
    public String getMessage() {
        return this.theReportedExceptionCrashReport.getDescription();
    }

    public ReportedException(CrashReport crashReport) {
        this.theReportedExceptionCrashReport = crashReport;
    }

    @Override
    public Throwable getCause() {
        return this.theReportedExceptionCrashReport.getCrashCause();
    }
}

