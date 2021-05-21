package TFIndustrial.modules;



import arc.Core;
import arc.func.Boolf;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.g2d.TextureAtlas.AtlasRegion;

import arc.math.*;
import arc.scene.ui.Button;
import arc.scene.ui.layout.Table;
import arc.struct.EnumSet;
import arc.util.Log;
import arc.util.Log.*;
import arc.util.io.Reads;
import arc.util.io.Writes;

import mindustry.Vars;
import mindustry.content.Items;
import mindustry.gen.Icon;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.ui.Fonts;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.PlanetDialog.Mode;
import mindustry.world.meta.BlockFlag;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.production.GenericCrafter;

import TFIndustrial.modules.*;

public class ConfigurableDrill extends FlexibleGenericCrafter{
    
    public TextureRegion[] regions = new TextureRegion[2];
    public Item[][] lootTable;
    public ItemChance[][] specialChances;
    public Item[] allPossible;
    public Color[] DepthColors;
    public String[] ModeNames = {"Adjusting", "Mining"};
    public Color[] ModeColors = {Color.valueOf("FF0000"), Color.valueOf("00FF00")};

    public ConfigurableDrill(String name){
        super(name);
        
    }
    
    
    
    public void load(){
        super.load();
        
        regions[0] = Core.atlas.find(name);
        regions[1] = Core.atlas.find(name + "-rotator");
        Log.info(name);
        arc.util.Log.info(regions[0]);
        arc.util.Log.info(regions[1]);
    }

    public class ConfigurableDrillBuild extends FlexibleGenericCrafterBuild{
        

        int DrillDepth = 0;
        int TargetDepth = 0;
        
        
        int Mode = 0;
        
        
        
        
        
        
        public void created(){
            super.created();
            Mode = 0;
            fct.SwitchTable(0);
        }

        
        public void draw(){
            Draw.rect(regions[0], x, y);
            Draw.rect(regions[1], x, y, totalProgress * 2);
            Fonts.outline.draw(String.valueOf(DrillDepth), x, y + 2, DepthColors[DrillDepth], 0.4f, true, 1);
            Fonts.outline.draw(ModeNames[Mode], x, y - 6, ModeColors[Mode], 0.2f, false, 1);
            
            if (TargetDepth != DrillDepth){
                Fonts.outline.draw("(" + String.valueOf(TargetDepth) + ")", x, y + 8, DepthColors[TargetDepth], 0.2f, false, 1);
            }
        }

        
        
        public void buildConfiguration(Table parent){
            final Table table = parent.fill();

            // turn from mining to adjusting
            table.button(Icon.pause, Styles.clearTransi, () -> {
                configure(1);
            }).size(40).disabled(Mode == 0).get();

            // turn from adjusting to mining
            table.button(Icon.play, Styles.clearTransi, () -> {
                configure(2);
            }).size(40).disabled(Mode == 1).get();

            // increase target layer
            table.button(Icon.download, Styles.clearTransi, () -> {
                configure(3);
            }).size(40).disabled(Mode == 1 || DrillDepth == lootTable.length).get();

            // go to top
            table.button(Icon.upOpen, Styles.clearTransi, () -> {
                configure(4);
            }).size(40).disabled(Mode == 1 || DrillDepth == 0).get();
        }

        
        public void configured(Unit player, Object value){
            if (value.equals(1)) {
                Mode = 0;
                fct.SwitchTable(0);
            }
            if (value.equals(2)) {
                Mode = 1;
                fct.SwitchTable(1);
                //this.cons.update()
            }
            if (value.equals(3)) {
                TargetDepth++;
    
            }
            if (value.equals(4)) {
                TargetDepth = 0;
            }
        }

        @Override
        public void updateTile(){
            if (fct.CurrentConsValid() && ((Mode == 0 && TargetDepth != DrillDepth) || Mode == 1)){
                progress += this.getProgressIncrease(craftTime);
                totalProgress += delta();
                warmup = Mathf.lerpDelta(warmup, 1, 0.02f);

                if (Mathf.chanceDelta(updateEffectChance)){
                    updateEffect.at(x + Mathf.range(block.size * 4), y + Mathf.range(block.size * 4));
                }
                else {
                    warmup = Mathf.lerp(warmup, 0, 0.02f);
                }
            }

            if (progress >= 1){
                if (Mode == 0){
                    fct.consume();

                    if (DrillDepth < TargetDepth){
                        DrillDepth++;
                    }
                    if (DrillDepth > TargetDepth){
                        DrillDepth--;
                    }

                    craftEffect.at(x, y);
                    progress %= 1;
                }

                int outsum = 0;

                for (Item item : allPossible){
                    outsum += items.get(item);
                }
                
                if (Mode == 1 && outsum < block.itemCapacity) {
                    fct.consume();

                    Item chosenItem = lootTable[DrillDepth][ Mathf.random(0, lootTable[DrillDepth].length - 1)];
                    ItemChance chosenItemChance = new ItemChance(chosenItem, 1.0);
                    
                    for (int i = 0; i < specialChances[DrillDepth].length; i++){
                        if (specialChances[DrillDepth][i].item == chosenItem){
                            chosenItemChance = specialChances[DrillDepth][i];
                            
                            break;
                        }
                    }

                    
                    if (Mathf.chance(chosenItemChance.chance)) {
                        offload(chosenItem);
                    }
                    craftEffect.at(x, y);
                    progress %= 1;

                } 



            }



            // loads of shite


            if (outputItem != null && timer(timerDump, dumpTime)){
                for (Item i : allPossible){
                    if (dump(i)) {break;}
                }
                
                
            }
        } 
       
        public void read(Reads reads, Byte revision){
            super.read(reads, revision);
            Mode = reads.i();

            DrillDepth = reads.i();
            TargetDepth = reads.i();
        }
        // finish read + write code
        // yeet
        public void write(Writes writes){
            super.write(writes);
            writes.i(Mode);
            writes.i(DrillDepth);
            writes.i(TargetDepth);
        }
    }
}


