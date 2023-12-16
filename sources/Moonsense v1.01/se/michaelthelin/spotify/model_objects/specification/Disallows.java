// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import java.util.Iterator;
import com.google.gson.JsonElement;
import java.util.Map;
import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import se.michaelthelin.spotify.enums.Action;
import java.util.EnumSet;
import se.michaelthelin.spotify.model_objects.special.Actions;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Actions.Builder.class)
public class Disallows extends AbstractModelObject
{
    private final EnumSet<Action> disallowedActions;
    
    public Disallows(final Builder builder) {
        super(builder);
        this.disallowedActions = builder.disallowedActions;
    }
    
    public EnumSet<Action> getDisallowedActions() {
        return this.disallowedActions;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/util/EnumSet;)Ljava/lang/String;, this.disallowedActions);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private EnumSet<Action> disallowedActions;
        
        public Builder setDisallowedActions(final EnumSet<Action> disallowedActions) {
            this.disallowedActions = disallowedActions;
            return this;
        }
        
        @Override
        public Disallows build() {
            return new Disallows(this);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Disallows>
    {
        @Override
        public Disallows createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            final EnumSet<Action> disallowedActions = EnumSet.noneOf(Action.class);
            for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                if (entry.getValue().getAsJsonPrimitive().getAsBoolean()) {
                    disallowedActions.add(Action.keyOf(entry.getKey().toLowerCase()));
                }
            }
            return new Disallows.Builder().setDisallowedActions(disallowedActions).build();
        }
    }
}
