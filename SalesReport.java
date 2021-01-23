public class SalesReport extends MilkTeaOrderSystem{
    static int[] salesReport = new int[15]; //0-4=normalMT, 5-9=LargeMT, 10-14=Addon

    public static void addRecord(int index, int num){
        salesReport[index] += num;
    }

    public static int[] getSalesReport() {
        return salesReport;
    }
    
    public static int getSalesReport(int index) {
        return salesReport[index];
    }

    @Override
    public String toString() {
        String msg = line(5) + line(0) + centerText("SALE REPORT") + line(0) + String.format("%-22s %8s %8s", "Item", "Quantity", "RM") + line(1);
        int total = 0, totalTemp = 0, totalSub = 0, totalAll = 0;
        for (int i = 0; i < 15; i++) {
            if (i < 5) msg += LMRText("\nNormal " + stock.getMilkTea(i), salesReport[i], salesReport[i] * stock.getPrice(0));
            else if (i < 10)msg += LMRText("\nLarge " + stock.getMilkTea(i-5), salesReport[i], salesReport[i] * (stock.getPrice(0) + stock.getPrice(2)));
            else if (i < 15)msg += LMRText("\nAddon - " + stock.getAddon(i-10), salesReport[i], salesReport[i] * stock.getPrice(1));
            total += salesReport[i];
            if((i+1)%5 == 0) {
                totalTemp += total;
                if(i < 5) totalAll += totalSub += total * stock.getPrice(0);
                else if(i < 10) totalAll += totalSub += total * (stock.getPrice(0) + stock.getPrice(2));
                else if(i < 15) totalAll += totalSub += total * stock.getPrice(1);
                msg += "\n" + String.format("%22s %8s %8s", " ", "-----", "-----") + "\n" + LMRText(" ", total, totalSub) + "\n";
                if(i == 14)msg += line(2) + LMRText("TOTAL", totalTemp, totalAll) + line(2) + line(0);
                total = 0; totalSub = 0;
            }
        } return msg;
    }
}
