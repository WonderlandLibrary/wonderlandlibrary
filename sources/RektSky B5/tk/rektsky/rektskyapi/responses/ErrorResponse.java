/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import tk.rektsky.rektskyapi.utils.StringUtils;

public class ErrorResponse {
    @SerializedName(value="error")
    @Expose
    public String errorMessage;
    @SerializedName(value="error-id")
    @Expose
    public String errorId;

    public ErrorResponse() {
    }

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorId = StringUtils.generateRandomString(32);
    }
}

