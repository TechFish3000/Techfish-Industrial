package TFIndustrial.modules;

import javax.jws.WebParam.Mode;

import TFIndustrial.modules.*;
import arc.Core;
import arc.func.Boolf;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.g2d.TextureAtlas.AtlasRegion;
import arc.scene.ui.Button;
import arc.scene.ui.layout.Table;
import arc.struct.EnumSet;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.gen.Icon;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.type.ItemStack;
import mindustry.ui.Fonts;
import mindustry.ui.Styles;
import mindustry.world.meta.BlockFlag;



public class ConfigurableDrill extends FlexibleGenericCrafter{
    
    public AtlasRegion[] regions;
    public ItemStack[][] lootTable;
    public Items[] allPossible;
    public Color[] DepthColors;
    public String[] ModeNames = {"Adjusting", "Mining"};
    public Color[] ModeColors = {Color.valueOf("FF0000"), Color.valueOf("00FF00")};

    public ConfigurableDrill(String name){
        super(name);
        
    }
    
    
    @Override
    public void load(){
        super.load();
        TextureRegion[] regions = new TextureRegion[2];
        regions[0] = Core.atlas.find(this.name);
        regions[3] = Core.atlas.find(this.name + "-rotator");
    }

    public class ConfigurableDrillBuild extends FlexibleGenericCrafterBuild{
        

        int DrillDepth = 0;
        int TargetDepth = 0;
        int MiningTarget = 0;
        int SpecialOreCounter = 0;
        int Mode = 0;
        
        
        
        
        
        @Override
        public void created(){
            super.created();
        }

        @Override
        public void draw(){
            Draw.rect(regions[0], x, y);
            Draw.rect(regions[3], x, y, totalProgress * 2);
            Fonts.outline.draw(String.valueOf(DrillDepth), x, y + 2, DepthColors[DrillDepth], 0.4f, true, 1);
            Fonts.outline.draw(ModeNames[Mode], x, y - 6, ModeColors[Mode], 0.2f, false, 1);
            
            if (TargetDepth != DrillDepth){
                Fonts.outline.draw("(" + String.valueOf(TargetDepth) + ")", x, y + 8, DepthColors[TargetDepth], 0.2f, false, 1);
            }
        }

        
        
        public void buildConfiguration(Table parent){
            final Table table = parent.fill();

            final Button adjust = table.button(Icon.pause, Styles.clearTransi, () -> {
                configure(1);
            }).size(40).disabled(Mode == 0).get();
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
            if (fct.CurrentConsValid() && Mode == 0 && TargetDepth != DrillDepth){
                progress += this.getProgressIncrease(craftTime);
            }
        }


    }
}


