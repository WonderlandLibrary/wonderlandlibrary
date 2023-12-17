// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model.anim;

import net.optifine.expr.ParseException;
import net.optifine.expr.IExpressionResolver;
import net.optifine.expr.ExpressionParser;
import net.minecraft.src.Config;
import net.optifine.expr.IExpressionFloat;

public class ModelVariableUpdater
{
    private String modelVariableName;
    private String expressionText;
    private ModelVariableFloat modelVariable;
    private IExpressionFloat expression;
    
    public boolean initialize(final IModelResolver mr) {
        this.modelVariable = mr.getModelVariable(this.modelVariableName);
        if (this.modelVariable == null) {
            Config.warn("Model variable not found: " + this.modelVariableName);
            return false;
        }
        try {
            final ExpressionParser expressionparser = new ExpressionParser(mr);
            this.expression = expressionparser.parseFloat(this.expressionText);
            return true;
        }
        catch (ParseException parseexception) {
            Config.warn("Error parsing expression: " + this.expressionText);
            Config.warn(parseexception.getClass().getName() + ": " + parseexception.getMessage());
            return false;
        }
    }
    
    public ModelVariableUpdater(final String modelVariableName, final String expressionText) {
        this.modelVariableName = modelVariableName;
        this.expressionText = expressionText;
    }
    
    public void update() {
        final float f = this.expression.eval();
        this.modelVariable.setValue(f);
    }
}
