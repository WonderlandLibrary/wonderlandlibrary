package Velo.api.setting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import Velo.api.Module.Module;

public class Setting {
	private Module parent;
	
    @Expose
    @SerializedName("name")
	public String name;
    
	public float optionAnim = 0;// present
	public float optionAnimNow = 0;// present
	public boolean focused;

	
	public final static List settings = new ArrayList();
	
	
	   public final static Setting getSettingByName(String name) {
		      Iterator var2 = settings.iterator();

		      Setting setting;
		      do {
		         if (!var2.hasNext()) {
		            throw new NullPointerException();
		         }

		         setting = (Setting)var2.next();
		      } while(!setting.getName().equals(name));

		      return setting;
		   }
	   
	   
	   public Module getModule() {
			return this.parent;
		}
	   
	   public String getName() {
		   return this.name;
	   }
	   
	   
	   public final List<Setting> getSettings() {
		      return this.settings;
		   }
	   
		public ArrayList<Setting> getSettingsByMod(Module mod){
			ArrayList<Setting> out = new ArrayList<>();
			for(Setting s : getSettings()){
				if(s.getModule().equals(mod)){
					out.add(s);
				}
			}
			return out;
		}
		
		
	    public boolean hasSettings(Module mod) {
	    	  ArrayList<Setting> out = new ArrayList<>();
	          for(Setting s : getSettings()){
	              if(s.getModule().equals(mod)){
	                  out.add(s);
	              }
	          }
	          if(out.isEmpty()){
	              return false;
	          }
	          return true;
	    }

		  public final List<Setting> getSettings2() {
		      return this.settings;
		   }
		  

	   
}
