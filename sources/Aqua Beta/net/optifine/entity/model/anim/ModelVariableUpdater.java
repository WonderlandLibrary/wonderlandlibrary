package net.optifine.entity.model.anim;

import net.minecraft.src.Config;
import net.optifine.entity.model.anim.IModelResolver;
import net.optifine.entity.model.anim.ModelVariableFloat;
import net.optifine.expr.ExpressionParser;
import net.optifine.expr.IExpressionFloat;
import net.optifine.expr.IExpressionResolver;
import net.optifine.expr.ParseException;

public class ModelVariableUpdater {
    private String modelVariableName;
    private String expressionText;
    private ModelVariableFloat modelVariable;
    private IExpressionFloat expression;

    public boolean initialize(IModelResolver mr) {
        this.modelVariable = mr.getModelVariable(this.modelVariableName);
        if (this.modelVariable == null) {
            Config.warn((String)("Model variable not found: " + this.modelVariableName));
            return false;
        }
        try {
            ExpressionParser expressionparser = new ExpressionParser((IExpressionResolver)mr);
            this.expression = expressionparser.parseFloat(this.expressionText);
            return true;
        }
        catch (ParseException parseexception) {
            Config.warn((String)("Error parsing expression: " + this.expressionText));
            Config.warn((String)(parseexception.getClass().getName() + ": " + parseexception.getMessage()));
            return false;
        }
    }

    public ModelVariableUpdater(String modelVariableName, String expressionText) {
        this.modelVariableName = modelVariableName;
        this.expressionText = expressionText;
    }

    public void update() {
        float f = this.expression.eval();
        this.modelVariable.setValue(f);
    }
}
