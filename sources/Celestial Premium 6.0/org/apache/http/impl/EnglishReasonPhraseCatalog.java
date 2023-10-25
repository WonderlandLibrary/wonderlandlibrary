/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl;

import java.util.Locale;
import org.apache.http.ReasonPhraseCatalog;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class EnglishReasonPhraseCatalog
implements ReasonPhraseCatalog {
    public static final EnglishReasonPhraseCatalog INSTANCE = new EnglishReasonPhraseCatalog();
    private static final String[][] REASON_PHRASES = new String[][]{null, new String[3], new String[8], new String[8], new String[25], new String[8]};

    protected EnglishReasonPhraseCatalog() {
    }

    public String getReason(int status, Locale loc) {
        Args.check(status >= 100 && status < 600, "Unknown category for status code " + status);
        int category = status / 100;
        int subcode = status - 100 * category;
        String reason = null;
        if (REASON_PHRASES[category].length > subcode) {
            reason = REASON_PHRASES[category][subcode];
        }
        return reason;
    }

    private static void setReason(int status, String reason) {
        int category = status / 100;
        int subcode = status - 100 * category;
        EnglishReasonPhraseCatalog.REASON_PHRASES[category][subcode] = reason;
    }

    static {
        EnglishReasonPhraseCatalog.setReason(200, "OK");
        EnglishReasonPhraseCatalog.setReason(201, "Created");
        EnglishReasonPhraseCatalog.setReason(202, "Accepted");
        EnglishReasonPhraseCatalog.setReason(204, "No Content");
        EnglishReasonPhraseCatalog.setReason(301, "Moved Permanently");
        EnglishReasonPhraseCatalog.setReason(302, "Moved Temporarily");
        EnglishReasonPhraseCatalog.setReason(304, "Not Modified");
        EnglishReasonPhraseCatalog.setReason(400, "Bad Request");
        EnglishReasonPhraseCatalog.setReason(401, "Unauthorized");
        EnglishReasonPhraseCatalog.setReason(403, "Forbidden");
        EnglishReasonPhraseCatalog.setReason(404, "Not Found");
        EnglishReasonPhraseCatalog.setReason(500, "Internal Server Error");
        EnglishReasonPhraseCatalog.setReason(501, "Not Implemented");
        EnglishReasonPhraseCatalog.setReason(502, "Bad Gateway");
        EnglishReasonPhraseCatalog.setReason(503, "Service Unavailable");
        EnglishReasonPhraseCatalog.setReason(100, "Continue");
        EnglishReasonPhraseCatalog.setReason(307, "Temporary Redirect");
        EnglishReasonPhraseCatalog.setReason(405, "Method Not Allowed");
        EnglishReasonPhraseCatalog.setReason(409, "Conflict");
        EnglishReasonPhraseCatalog.setReason(412, "Precondition Failed");
        EnglishReasonPhraseCatalog.setReason(413, "Request Too Long");
        EnglishReasonPhraseCatalog.setReason(414, "Request-URI Too Long");
        EnglishReasonPhraseCatalog.setReason(415, "Unsupported Media Type");
        EnglishReasonPhraseCatalog.setReason(300, "Multiple Choices");
        EnglishReasonPhraseCatalog.setReason(303, "See Other");
        EnglishReasonPhraseCatalog.setReason(305, "Use Proxy");
        EnglishReasonPhraseCatalog.setReason(402, "Payment Required");
        EnglishReasonPhraseCatalog.setReason(406, "Not Acceptable");
        EnglishReasonPhraseCatalog.setReason(407, "Proxy Authentication Required");
        EnglishReasonPhraseCatalog.setReason(408, "Request Timeout");
        EnglishReasonPhraseCatalog.setReason(101, "Switching Protocols");
        EnglishReasonPhraseCatalog.setReason(203, "Non Authoritative Information");
        EnglishReasonPhraseCatalog.setReason(205, "Reset Content");
        EnglishReasonPhraseCatalog.setReason(206, "Partial Content");
        EnglishReasonPhraseCatalog.setReason(504, "Gateway Timeout");
        EnglishReasonPhraseCatalog.setReason(505, "Http Version Not Supported");
        EnglishReasonPhraseCatalog.setReason(410, "Gone");
        EnglishReasonPhraseCatalog.setReason(411, "Length Required");
        EnglishReasonPhraseCatalog.setReason(416, "Requested Range Not Satisfiable");
        EnglishReasonPhraseCatalog.setReason(417, "Expectation Failed");
        EnglishReasonPhraseCatalog.setReason(102, "Processing");
        EnglishReasonPhraseCatalog.setReason(207, "Multi-Status");
        EnglishReasonPhraseCatalog.setReason(422, "Unprocessable Entity");
        EnglishReasonPhraseCatalog.setReason(419, "Insufficient Space On Resource");
        EnglishReasonPhraseCatalog.setReason(420, "Method Failure");
        EnglishReasonPhraseCatalog.setReason(423, "Locked");
        EnglishReasonPhraseCatalog.setReason(507, "Insufficient Storage");
        EnglishReasonPhraseCatalog.setReason(424, "Failed Dependency");
    }
}

