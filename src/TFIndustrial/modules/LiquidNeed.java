package TFIndustrial.modules;

import TFIndustrial.modules.FlexibleGenericCrafter.FlexibleGenericCrafterBuild;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.type.Liquid;

public class LiquidNeed {
    public Liquid lq = Vars.content.getByName(ContentType.liquid, "water");
    public float amount = 0;

    public boolean valid(FlexibleGenericCrafterBuild fgcb){
        if (fgcb.liquids.get(lq) < amount){
            return false;
        }
        else {
            return true;
        }
    }
}
