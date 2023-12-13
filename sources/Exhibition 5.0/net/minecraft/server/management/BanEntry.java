// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.management;

import java.text.ParseException;
import com.google.gson.JsonObject;
import java.util.Date;
import java.text.SimpleDateFormat;

public abstract class BanEntry extends UserListEntry
{
    public static final SimpleDateFormat dateFormat;
    protected final Date banStartDate;
    protected final String bannedBy;
    protected final Date banEndDate;
    protected final String reason;
    private static final String __OBFID = "CL_00001395";
    
    public BanEntry(final Object p_i46334_1_, final Date p_i46334_2_, final String p_i46334_3_, final Date p_i46334_4_, final String p_i46334_5_) {
        super(p_i46334_1_);
        this.banStartDate = ((p_i46334_2_ == null) ? new Date() : p_i46334_2_);
        this.bannedBy = ((p_i46334_3_ == null) ? "(Unknown)" : p_i46334_3_);
        this.banEndDate = p_i46334_4_;
        this.reason = ((p_i46334_5_ == null) ? "Banned by an operator." : p_i46334_5_);
    }
    
    protected BanEntry(final Object p_i1174_1_, final JsonObject p_i1174_2_) {
        super(p_i1174_1_, p_i1174_2_);
        Date var3;
        try {
            var3 = (p_i1174_2_.has("created") ? BanEntry.dateFormat.parse(p_i1174_2_.get("created").getAsString()) : new Date());
        }
        catch (ParseException var5) {
            var3 = new Date();
        }
        this.banStartDate = var3;
        this.bannedBy = (p_i1174_2_.has("source") ? p_i1174_2_.get("source").getAsString() : "(Unknown)");
        Date var4;
        try {
            var4 = (p_i1174_2_.has("expires") ? BanEntry.dateFormat.parse(p_i1174_2_.get("expires").getAsString()) : null);
        }
        catch (ParseException var6) {
            var4 = null;
        }
        this.banEndDate = var4;
        this.reason = (p_i1174_2_.has("reason") ? p_i1174_2_.get("reason").getAsString() : "Banned by an operator.");
    }
    
    public Date getBanEndDate() {
        return this.banEndDate;
    }
    
    public String getBanReason() {
        return this.reason;
    }
    
    @Override
    boolean hasBanExpired() {
        return this.banEndDate != null && this.banEndDate.before(new Date());
    }
    
    @Override
    protected void onSerialization(final JsonObject data) {
        data.addProperty("created", BanEntry.dateFormat.format(this.banStartDate));
        data.addProperty("source", this.bannedBy);
        data.addProperty("expires", (this.banEndDate == null) ? "forever" : BanEntry.dateFormat.format(this.banEndDate));
        data.addProperty("reason", this.reason);
    }
    
    static {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    }
}
