package TFIndustrial.modules;



import TFIndustrial.modules.FlexibleGenericCrafter.FlexibleGenericCrafterBuild;
import javafx.scene.control.Tab;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;

public class FlexibleConsumeTable{

    FlexibleGenericCrafterBuild crafter;
    ItemStack[][] ItemConsTable;
    float[] PowerConsTable;
    LiquidNeed[][] LiquidConsTable;

    int SelectedTable = 0;
    int TableSize = 0;

    public FlexibleConsumeTable(int size){
        TableSize = size;
        ItemConsTable = new ItemStack[size][];
        PowerConsTable = new float[size];
        LiquidConsTable = new LiquidNeed[size][];
    }

    // attach entity
    public void attachEntity(FlexibleGenericCrafterBuild FGC){
        crafter = FGC;
    }

    // handles item adding
    public void addItems(ItemStack[] items){
        addItems(SelectedTable, items);
    }
    public void addItems(int TableIndex, ItemStack[] items){
        ItemConsTable[TableIndex] = items;
    }
    
    
    // handles liquid adding
    public void addLiquids(LiquidNeed[] liquids){
        addLiquids(SelectedTable, liquids);
    }
    public void addLiquids(int TableIndex, LiquidNeed[] liquids){
        LiquidConsTable[TableIndex] = liquids;
    }


    // handles power
    public void addPower(float amt){
        addPower(SelectedTable, amt);
    }
    public void addPower(int TableIndex, float amt){
        PowerConsTable[TableIndex] = amt;
    }

    // Handles valid checks & table switching
    public int SwitchTable(){
        int OldTable = SelectedTable;
        SelectedTable++;
        SelectedTable %= TableSize;
        return OldTable;
    }

    public int SwitchTable(int TableIndex){
        int OldTable = SelectedTable;

        if (TableIndex < 0 || TableIndex > TableSize - 1){
            return -1;
        }


        return OldTable;
    }

    public boolean CurrentConsValid(){
        boolean valid = crafter.items.has(ItemConsTable[SelectedTable]) && LiquidConsTable[SelectedTable][0].valid(crafter) && crafter.power.status != 0;
        return valid;
        
        
    }

    public float getPowerNeeded(){
        if (isPowerNeeded()){
            return PowerConsTable[SelectedTable];
        }
        else{
            return 0;
        }
        
    }
    public boolean isPowerNeeded(){
        return PowerConsTable[SelectedTable] != 0f;
    }

    public void consume(){
        if (ItemConsTable[SelectedTable].length != 0){
            crafter.items.remove(ItemConsTable[SelectedTable]);
        }
        
        if (LiquidConsTable[SelectedTable].length != 0){
            crafter.liquids.remove(LiquidConsTable[SelectedTable][0].lq, LiquidConsTable[SelectedTable][0].amount);
        }

        
    }
}