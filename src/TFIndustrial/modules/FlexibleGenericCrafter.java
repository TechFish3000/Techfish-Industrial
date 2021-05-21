package TFIndustrial.modules;

import arc.*;
import arc.scene.ui.Dialog;
import arc.util.*;

import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.ContentType;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;
import mindustry.entities.*;
import mindustry.world.blocks.production.*;
import arc.math.Mathf;

public class FlexibleGenericCrafter extends GenericCrafter{
    
    public FlexibleConsumeTable fct;
    
    public FlexibleGenericCrafter(String name){
        super(name);
        Log.info("yeet");
    }


    public class FlexibleGenericCrafterBuild extends GenericCrafterBuild{
        
        
        
        @Override
        public float efficiency(){
            //disabled -> 0 efficiency
            if(!enabled) return 0;
            return power != null && (fct.isPowerNeeded()) ? (power.status * fct.getPowerNeeded()) : 1f;
        }

        @Override
        public boolean consValid(){
            return fct.CurrentConsValid();
        }

        @Override
        public void consume(){
            fct.consume();
        }

        @Override
        public void updateTile(){
            if(consValid()){

                progress += getProgressIncrease(craftTime);
                totalProgress += delta();
                warmup = Mathf.lerpDelta(warmup, 1f, 0.02f);

                if(Mathf.chanceDelta(updateEffectChance)){
                    updateEffect.at(getX() + Mathf.range(size * 4f), getY() + Mathf.range(size * 4));
                }
            }else{
                warmup = Mathf.lerp(warmup, 0f, 0.02f);
            }

            if(progress >= 1f){
                consume();

                if(outputItem != null){
                    for(int i = 0; i < outputItem.amount; i++){
                        offload(outputItem.item);
                    }
                }

                if(outputLiquid != null){
                    handleLiquid(this, outputLiquid.liquid, outputLiquid.amount);
                }

                craftEffect.at(x, y);
                progress %= 1f;
            }

            if(outputItem != null && timer(timerDump, dumpTime)){
                dump(outputItem.item);
            }

            if(outputLiquid != null){
                dumpLiquid(outputLiquid.liquid);
            }
        }
    }
}


