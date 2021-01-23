public class Stock {
    String[] nameMilkTea = {"MilkTea", "Green Tea", "Bubble Tea", "Green Milk Tea", "Oolong Tea"};
    static final int priceMilkTea  = 7;
    String[] size = {"Normal", "Large"};
    String[] nameAddon = {"Black Pearl", "White Pearl", "Pudding", "Milk Foam", "Grass Jelly"};
    static final int priceAddon = 1;
    String[] iceLevel = {"No Ice", "Quarter Ice", "Normal Ice"};
    String[] sugarLevel = {"No Sugar", "Quarter Sugar", "Half Sugar", "Less Sugar", "Full Sugar"};
    
    public String[] getMilkTea(){
        return nameMilkTea;
    }
    
    public String getMilkTea(int index){
        return nameMilkTea[index];
    }
    
    public int getPrice(int type){//0=priceMilkTea, 1=priceAddon, 2=Largesize
        return type == 0? priceMilkTea: type == 1? priceAddon: 1;
    }
    
    public String[] getSize(){
        return size;
    }
    
    public String getSize(int index){
        return size[index];
    }
    
    public String[] getAddon(){
        return nameAddon;
    }
    
    public String getAddon(int index){
        return nameAddon[index];
    }

    public String[] getIce(){
        return iceLevel;
    }
    
    public String getIce(int index){
        return iceLevel[index];
    }
    
    public String[] getSugar(){
        return sugarLevel;
    }
    
    public String getSugar(int index){
        return sugarLevel[index];
    }
}