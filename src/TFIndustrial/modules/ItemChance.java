package TFIndustrial.modules;

import mindustry.type.Item;

public class ItemChance {
    public Item item;
    public double chance;

    public ItemChance(Item i, double c){
        chance = c;
        item = i;
    }
}
