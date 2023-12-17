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

public class DamageSourcePredicate
{
    public static DamageSourcePredicate ANY;
    private final Boolean isProjectile;
    private final Boolean isExplosion;
    private final Boolean bypassesArmor;
    private final Boolean bypassesInvulnerability;
    private final Boolean bypassesMagic;
    private final Boolean isFire;
    private final Boolean isMagic;
    private final EntityPredicate directEntity;
    private final EntityPredicate sourceEntity;
    
    public DamageSourcePredicate() {
        this.isProjectile = null;
        this.isExplosion = null;
        this.bypassesArmor = null;
        this.bypassesInvulnerability = null;
        this.bypassesMagic = null;
        this.isFire = null;
        this.isMagic = null;
        this.directEntity = EntityPredicate.ANY;
        this.sourceEntity = EntityPredicate.ANY;
    }
    
    public DamageSourcePredicate(@Nullable final Boolean isProjectile, @Nullable final Boolean isExplosion, @Nullable final Boolean bypassesArmor, @Nullable final Boolean bypassesInvulnerability, @Nullable final Boolean bypassesMagic, @Nullable final Boolean isFire, @Nullable final Boolean isMagic, final EntityPredicate directEntity, final EntityPredicate sourceEntity) {
        this.isProjectile = isProjectile;
        this.isExplosion = isExplosion;
        this.bypassesArmor = bypassesArmor;
        this.bypassesInvulnerability = bypassesInvulnerability;
        this.bypassesMagic = bypassesMagic;
        this.isFire = isFire;
        this.isMagic = isMagic;
        this.directEntity = directEntity;
        this.sourceEntity = sourceEntity;
    }
    
    public boolean test(final EntityPlayerMP player, final DamageSource source) {
        return this == DamageSourcePredicate.ANY || ((this.isProjectile == null || this.isProjectile == source.isProjectile()) && (this.isExplosion == null || this.isExplosion == source.isExplosion()) && (this.bypassesArmor == null || this.bypassesArmor == source.isUnblockable()) && (this.bypassesInvulnerability == null || this.bypassesInvulnerability == source.canHarmInCreative()) && (this.bypassesMagic == null || this.bypassesMagic == source.isDamageAbsolute()) && (this.isFire == null || this.isFire == source.isFireDamage()) && (this.isMagic == null || this.isMagic == source.isMagicDamage()) && this.directEntity.test(player, source.getImmediateSource()) && this.sourceEntity.test(player, source.getTrueSource()));
    }
    
    public static DamageSourcePredicate deserialize(@Nullable final JsonElement element) {
        if (element != null && !element.isJsonNull()) {
            final JsonObject jsonobject = JsonUtils.getJsonObject(element, "damage type");
            final Boolean obool = optionalBoolean(jsonobject, "is_projectile");
            final Boolean obool2 = optionalBoolean(jsonobject, "is_explosion");
            final Boolean obool3 = optionalBoolean(jsonobject, "bypasses_armor");
            final Boolean obool4 = optionalBoolean(jsonobject, "bypasses_invulnerability");
            final Boolean obool5 = optionalBoolean(jsonobject, "bypasses_magic");
            final Boolean obool6 = optionalBoolean(jsonobject, "is_fire");
            final Boolean obool7 = optionalBoolean(jsonobject, "is_magic");
            final EntityPredicate entitypredicate = EntityPredicate.deserialize(jsonobject.get("direct_entity"));
            final EntityPredicate entitypredicate2 = EntityPredicate.deserialize(jsonobject.get("source_entity"));
            return new DamageSourcePredicate(obool, obool2, obool3, obool4, obool5, obool6, obool7, entitypredicate, entitypredicate2);
        }
        return DamageSourcePredicate.ANY;
    }
    
    @Nullable
    private static Boolean optionalBoolean(final JsonObject object, final String memberName) {
        return object.has(memberName) ? Boolean.valueOf(JsonUtils.getBoolean(object, memberName)) : null;
    }
    
    static {
        DamageSourcePredicate.ANY = new DamageSourcePredicate();
    }
}
