/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.game.entity.attribute;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.ModifierOperation;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.ModifierType;
import com.github.steveice10.mc.protocol.util.ObjectUtil;
import java.util.Objects;
import java.util.UUID;

public class AttributeModifier {
    private ModifierType type;
    private UUID uuid;
    private double amount;
    private ModifierOperation operation;

    public AttributeModifier(ModifierType type, double amount, ModifierOperation operation) {
        if (type == ModifierType.DYNAMIC) {
            throw new IllegalArgumentException("Cannot create a dynamic-typed modifier without a UUID.");
        }
        this.type = type;
        this.uuid = MagicValues.value(UUID.class, (Object)type);
        this.amount = amount;
        this.operation = operation;
    }

    public AttributeModifier(UUID uuid, double amount, ModifierOperation operation) {
        try {
            this.type = MagicValues.key(ModifierType.class, uuid);
        }
        catch (IllegalArgumentException e) {
            this.type = ModifierType.DYNAMIC;
        }
        this.uuid = uuid;
        this.amount = amount;
        this.operation = operation;
    }

    public ModifierType getType() {
        return this.type;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public double getAmount() {
        return this.amount;
    }

    public ModifierOperation getOperation() {
        return this.operation;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttributeModifier)) {
            return false;
        }
        AttributeModifier that = (AttributeModifier)o;
        return this.type == that.type && Objects.equals(this.uuid, that.uuid) && this.amount == that.amount && this.operation == that.operation;
    }

    public int hashCode() {
        return ObjectUtil.hashCode(new Object[]{this.type, this.uuid, this.amount, this.operation});
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}

