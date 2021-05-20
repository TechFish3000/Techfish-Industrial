package TFIndustrial.content;

import mindustry.ctype.ContentList;
import mindustry.world.Block;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.campaign.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.experimental.*;
import mindustry.world.blocks.legacy.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.blocks.logic.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.sandbox.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.units.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import static mindustry.type.ItemStack.*;

import TFIndustrial.modules.*;
// End

public class TFIBlocks implements ContentList {
    public static Block

    // production
    siliconCrusher;

    @Override
    public void load(){
    siliconCrusher = new FlexibleGenericCrafter("silicon-crusher"){{
        requirements(Category.crafting, with(Items.titanium, 10));

        size = 2;
        hasPower = true;
        hasLiquids = false;
        hasItems = true;
        craftTime = 90;
        craftEffect = Fx.smeltsmoke;
        outputItem = new ItemStack(Items.scrap, 2);

        consumes.power(1.8f);
        consumes.items(with(Items.silicon, 2));
    }};

    }
}
