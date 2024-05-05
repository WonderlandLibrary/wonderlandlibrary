package xyz.cucumber.base.module.settings;

import java.util.function.Supplier;

public class NumberSettings extends ModuleSettings{
	
	 public double value;

	    public double getMin()
	    {
	        return min;
	    }

	    public void setMin(double min)
	    {
	        this.min = min;
	    }

	    public double getMax()
	    {
	        return max;
	    }

	    public void setMax(double max)
	    {
	        this.max = max;
	    }

	    public double getIncrement()
	    {
	        return increment;
	    }

	    public void setIncrement(double increment)
	    {
	        this.increment = increment;
	    }

	    public double getValue()
	    {
	        return value;
	    }

	    public NumberSettings(String name, Supplier<Boolean> visibility, double value, double min, double max, double increment)
	    {
	        this.setName(name);
	        this.value = value;
	        this.min = min;
	        this.max = max;
	        this.increment = increment;
	        this.category = null;
	        this.visibility = visibility;
	    }
	    public NumberSettings(String name, double value, double min, double max, double increment)
	    {
	        this.setName(name);
	        this.value = value;
	        this.min = min;
	        this.max = max;
	        this.increment = increment;
	        this.category = null;
	    }
	    public NumberSettings(String category, String name, double value, double min, double max, double increment)
	    {
	        this.setName(name);
	        this.value = value;
	        this.min = min;
	        this.max = max;
	        this.increment = increment;
	        this.category = category;
	    }

	    public double min;
	    public double max, increment = 0.01;

	    public void setValue(double value)
	    {
	        double precision = 1 / increment;
	        this.value = Math.round(Math.max(min, Math.min(max, value)) * precision) / precision;
	    }
	
}
