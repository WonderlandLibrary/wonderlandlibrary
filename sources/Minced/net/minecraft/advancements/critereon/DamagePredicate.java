// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayerMP;
import javax.annotation.Nullable;

public class DamagePredicate
{
    public static DamagePredicate ANY;
    private final MinMaxBounds dealt;
    private final MinMaxBounds taken;
    private final EntityPredicate sourceEntity;
    private final Boolean blocked;
    private final DamageSourcePredicate type;
    
    public DamagePredicate() {
        this.dealt = MinMaxBounds.UNBOUNDED;
        this.taken = MinMaxBounds.UNBOUNDED;
        this.sourceEntity = EntityPredicate.ANY;
        this.blocked = null;
        this.type = DamageSourcePredicate.ANY;
    }
    
    public DamagePredicate(final MinMaxBounds dealt, final MinMaxBounds taken, final EntityPredicate sourceEntity, @Nullable final Boolean blocked, final DamageSourcePredicate type) {
        this.dealt = dealt;
        this.taken = taken;
        this.sourceEntity = sourceEntity;
        this.blocked = blocked;
        this.type = type;
    }
    
    public boolean test(final EntityPlayerMP player, final DamageSource source, final float dealt, final float taken, final boolean blocked) {
        return this == DamagePredicate.ANY || (this.dealt.test(dealt) && this.taken.test(taken) && this.sourceEntity.test(player, source.getTrueSource()) && (this.blocked == null || this.blocked == blocked) && this.type.test(player, source));
    }
    
    public static DamagePredicate deserialize(@Nullable final JsonElement element) {
        if (element != null && !element.isJsonNull()) {
            final JsonObject jsonobject = JsonUtils.getJsonObject(element, "damage");
            final MinMaxBounds minmaxbounds = MinMaxBounds.deserialize(jsonobject.get("dealt"));
            final MinMaxBounds minmaxbounds2 = MinMaxBounds.deserialize(jsonobject.get("taken"));
            final Boolean obool = jsonobject.has("blocked") ? Boolean.valueOf(JsonUtils.getBoolean(jsonobject, "blocked")) : null;
            final EntityPredicate entitypredicate = EntityPredicate.deserialize(jsonobject.get("source_entity"));
            final DamageSourcePredicate damagesourcepredicate = DamageSourcePredicate.deserialize(jsonobject.get("type"));
            return new DamagePredicate(minmaxbounds, minmaxbounds2, entitypredicate, obool, damagesourcepredicate);
        }
        return DamagePredicate.ANY;
    }
    
    static {
        DamagePredicate.ANY = new DamagePredicate();
    }
}
