/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package net.minecraft.entity.ai.attributes;

import io.netty.util.internal.ThreadLocalRandom;
import java.util.UUID;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.Validate;

public class AttributeModifier {
    private final UUID id;
    private final String name;
    private final int operation;
    private final double amount;
    private boolean isSaved = true;

    public AttributeModifier(UUID uUID, String string, double d, int n) {
        this.id = uUID;
        this.name = string;
        this.amount = d;
        this.operation = n;
        Validate.notEmpty((CharSequence)string, (String)"Modifier name cannot be empty", (Object[])new Object[0]);
        Validate.inclusiveBetween((long)0L, (long)2L, (long)n, (String)"Invalid operation");
    }

    public UUID getID() {
        return this.id;
    }

    public int getOperation() {
        return this.operation;
    }

    public double getAmount() {
        return this.amount;
    }

    public boolean isSaved() {
        return this.isSaved;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            AttributeModifier attributeModifier = (AttributeModifier)object;
            return !(this.id != null ? !this.id.equals(attributeModifier.id) : attributeModifier.id != null);
        }
        return false;
    }

    public String getName() {
        return this.name;
    }

    public AttributeModifier setSaved(boolean bl) {
        this.isSaved = bl;
        return this;
    }

    public String toString() {
        return "AttributeModifier{amount=" + this.amount + ", operation=" + this.operation + ", name='" + this.name + '\'' + ", id=" + this.id + ", serialize=" + this.isSaved + '}';
    }

    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }

    public AttributeModifier(String string, double d, int n) {
        this(MathHelper.getRandomUuid(ThreadLocalRandom.current()), string, d, n);
    }
}

