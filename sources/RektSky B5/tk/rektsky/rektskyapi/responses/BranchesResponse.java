/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import tk.rektsky.rektskyapi.Branch;

public class BranchesResponse {
    @Expose
    @SerializedName(value="branches")
    @JsonProperty(value="branches")
    public List<Branch> branches = new ArrayList<Branch>();
}

