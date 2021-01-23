import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Order {
    static int numOrder = 0;
    String orderID, date, time;
    int totalPrice;
    ArrayList orderList = new ArrayList();

    public Order(){
        orderID = "OD" + String.format("%4s",++numOrder).replaceAll(" ", "0");
        DateTimeFormatter dates = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter times = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.date = dates.format(now);
        this.time = times.format(now);
    }

    public String getOrderID(){
        return orderID;
    }
    
    public String getDate(){
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setOrderList(int index, int stockIndex) {//0=MilkTea, 1=size, 2=sugar, 3=ice, 4-8=addon(0=false,1=true), 9=quantity
        this.orderList.add(index, stockIndex);
    }
    
    public ArrayList getOrderList(){
        return orderList;
    }
    
    public Object getOrderList(int index){
        return orderList.get(index);
    }
   
    public void addPrice(int price){
        totalPrice += price;
    }
    
    public int getPrice(){
        return totalPrice;
    }
}
