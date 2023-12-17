// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements;

import java.text.ParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;
import java.util.Date;
import java.text.SimpleDateFormat;

public class CriterionProgress
{
    private static final SimpleDateFormat DATE_TIME_FORMATTER;
    private final AdvancementProgress advancementProgress;
    private Date obtained;
    
    public CriterionProgress(final AdvancementProgress advancementProgressIn) {
        this.advancementProgress = advancementProgressIn;
    }
    
    public boolean isObtained() {
        return this.obtained != null;
    }
    
    public void obtain() {
        this.obtained = new Date();
    }
    
    public void reset() {
        this.obtained = null;
    }
    
    public Date getObtained() {
        return this.obtained;
    }
    
    @Override
    public String toString() {
        return "CriterionProgress{obtained=" + ((this.obtained == null) ? "false" : this.obtained) + '}';
    }
    
    public void write(final PacketBuffer buf) {
        buf.writeBoolean(this.obtained != null);
        if (this.obtained != null) {
            buf.writeTime(this.obtained);
        }
    }
    
    public JsonElement serialize() {
        return (JsonElement)((this.obtained != null) ? new JsonPrimitive(CriterionProgress.DATE_TIME_FORMATTER.format(this.obtained)) : JsonNull.INSTANCE);
    }
    
    public static CriterionProgress read(final PacketBuffer buf, final AdvancementProgress advancementProgressIn) {
        final CriterionProgress criterionprogress = new CriterionProgress(advancementProgressIn);
        if (buf.readBoolean()) {
            criterionprogress.obtained = buf.readTime();
        }
        return criterionprogress;
    }
    
    public static CriterionProgress fromDateTime(final AdvancementProgress advancementProgressIn, final String dateTime) {
        final CriterionProgress criterionprogress = new CriterionProgress(advancementProgressIn);
        try {
            criterionprogress.obtained = CriterionProgress.DATE_TIME_FORMATTER.parse(dateTime);
            return criterionprogress;
        }
        catch (ParseException parseexception) {
            throw new JsonSyntaxException("Invalid datetime: " + dateTime, (Throwable)parseexception);
        }
    }
    
    static {
        DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    }
}
