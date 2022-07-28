package TFIndustrial.modules;

import arc.*;
import arc.func.Floatp;
import arc.func.Func;
import arc.graphics.Color;
import arc.scene.ui.Dialog;
import arc.scene.ui.layout.Table;
import arc.util.*;

import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.ContentType;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.mod.*;
import mindustry.type.Liquid;
import mindustry.ui.Bar;
import mindustry.ui.dialogs.*;
import mindustry.entities.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.consumers.ConsumePower;
import mindustry.world.consumers.ConsumeType;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.Stat;
import arc.math.Mathf;
import TFIndustrial.modules.*;

public class FlexibleGenericCrafter extends GenericCrafter{
    
    public FlexibleConsumeTable fct;
    
    public FlexibleGenericCrafter(String name){
        super(name);
        Log.info("yeet");
        GenericCrafter.class
    }

    // @Override
    // public void setBars(){
    //     bars.add("health", entity -> new Bar("stat.health", Pal.health, entity::healthf).blink(Color.white));

    //     if(hasLiquids){
    //         Func<Building, Liquid> current;
    //         if(consumes.has(ConsumeType.liquid) && consumes.get(ConsumeType.liquid) instanceof ConsumeLiquid){
    //             Liquid liquid = consumes.<ConsumeLiquid>get(ConsumeType.liquid).liquid;
    //             current = entity -> liquid;
    //         }else{
    //             current = entity -> entity.liquids == null ? Liquids.water : entity.liquids.current();
    //         }
    //         bars.add("liquid", entity -> new Bar(() -> entity.liquids.get(current.get(entity)) <= 0.001f ? Core.bundle.get("bar.liquid") : current.get(entity).localizedName,
    //         () -> current.get(entity).barColor(), () -> entity == null || entity.liquids == null ? 0f : entity.liquids.get(current.get(entity)) / liquidCapacity));
    //     }

    //     if(hasPower && consumes.hasPower()){
    //         ConsumePower cons = consumes.getPower();
    //         boolean buffered = cons.buffered;
    //         float capacity = cons.capacity;

    //         bars.add("power", entity -> new Bar(buffered ? Core.bundle.format("bar.poweramount", Float.isNaN(entity.power.status * capacity) ? "<ERROR>" : (int)(entity.power.status * capacity)) :
    //             Core.bundle.get("bar.power"), () -> Pal.powerBar, () -> Mathf.zero(cons.requestedPower(entity)) && entity.power.graph.getPowerProduced() + entity.power.graph.getBatteryStored() > 0f ? 1f : entity.power.status));
    //     }

    //     if(hasItems && configurable){
    //         bars.add("items", entity -> new Bar(() -> Core.bundle.format("bar.items", entity.items.total()), () -> Pal.items, () -> (float)entity.items.total() / itemCapacity));
    //     }
        
    //     if(flags.contains(BlockFlag.unitModifier)) stats.add(Stat.maxUnits, (unitCapModifier < 0 ? "-" : "+") + Math.abs(unitCapModifier));
    // }


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

        @Override
        public void displayBars(Table table){
            table.add(new Bar("stat.health", Pal.health, /*(Floatp)*/ () -> (health / maxHealth) ).blink(Color.white)).growX();
            table.row();
            bars.add("health", entity -> new Bar("stat.health", Pal.health, entity::healthf).blink(Color.white));
        }
    }
}


