import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class MilkTeaOrderSystem {
    static ArrayList<Customer> custAL = new ArrayList<Customer>();
    static ArrayList<Order> orderAL = new ArrayList<Order>();
    static Stock stock = new Stock();
    static SalesReport report = new SalesReport();
    static String[] menuText = {" Milk Tea Order System", " Order | MTOS", " Member | MTOS", "", " Member Register | MTOS", " Check Point | MTOS", " Redeem | MTOS", " Customer Info | MTOS"};
    public static void main(String[] args) {
        custAL.add(new Member("Testing Man", "0123456789"));
        Member member = (Member)custAL.get(custAL.size()-1);
        member.addPoint(120);
        menuMain();
    }
    
    public static void menuMain(){
        String[] optBtn = {"Order", "Member", "Sale Report", "Close"};
        int opt = JOptionPane.showOptionDialog(null, "Choose a services:", menuText[0], 0, 1, null, optBtn, custAL);
        switch(opt){
            case 0: menuOrder();
                    menuMain(); break;
            case 1: menuMember(); break;
            case 2: saleReport();
                    menuMain(); break;
            default:exit();
        }
    }
    
    static int count = 0; // 10 per Order(0-9)
    public static void menuOrder(){
        int memberYN = 0, memberIndex, isMember = JOptionPane.showConfirmDialog(null, "Are you a member?", menuText[1], 0);
        String[] custInfo = new String[2];
        Member member = null;
        if(isMember == -1) menuMain();
        else if(isMember == 1){
            custInfo = inputCustomerInfo(1,1);
            memberYN = 0;
        } else {
            memberIndex = isMember(inputMemberId(0, 2));
            if(memberIndex == -1) menuOrder();
            else {
                JOptionPane.showMessageDialog(null, "Member ID found!");
                member = getMember(memberIndex);
                memberYN = 1;
            }
        }
        if (count == 0) {
            orderAL.add(new Order());
            order(count);
        }
        while(orderExtra()) order(count);
        custAL.add(new Customer(custInfo[0], custInfo[1]));
        receipt(memberYN, member);
        count = 0;
    }
    
    public static void order(int count){
        JComboBox cbb_name = new JComboBox(stock.getMilkTea());
        cbb_name.setSelectedIndex(-1);
        JComboBox cbb_size = new JComboBox(stock.getSize());
        cbb_size.setSelectedIndex(-1);
        JTextField tf_quantity = new JTextField(){
          public void processKeyEvent(KeyEvent key){
              if (key.getKeyChar() >= 48 && key.getKeyChar() <= 57 || key.getKeyChar() == 8 || key.getKeyChar() == 127 || key.getKeyCode()== KeyEvent.VK_LEFT || key.getKeyCode()== KeyEvent.VK_RIGHT) super.processKeyEvent(key);
        }};
        JComboBox cbb_sugar = new JComboBox(stock.getSugar());
        cbb_sugar.setSelectedIndex(-1);
        JComboBox cbb_ice = new JComboBox(stock.getIce());
        cbb_ice.setSelectedIndex(-1);
        JCheckBox[] cb_addon = new JCheckBox[stock.getAddon().length];
        Object[] contents = new Object[stock.getAddon().length + 1];
        for (int i = 0; i < stock.getAddon().length; i++) { contents[i + 1] = cb_addon[i] = new JCheckBox(stock.getAddon(i));}
        String[] optBtn = {"Continue", "Back"};
        Object[] content = {"Milk Tea", cbb_name, "Quantity", tf_quantity, "Size", cbb_size, "Sugar Level", cbb_sugar, "Ice Level", cbb_ice, "Addon", cb_addon};
        int opt = JOptionPane.showOptionDialog(null, content, menuText[1], 0, 3, null, optBtn, null);
        if (opt == -1) menuOrder();
        else if (cbb_name.getSelectedIndex() == -1 || cbb_size.getSelectedIndex() == -1 || cbb_sugar.getSelectedIndex() == -1 || cbb_ice.getSelectedIndex() == -1 || tf_quantity.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Please fill in all the data!", null, 0);
            order(count);
        } else if (Integer.parseInt(tf_quantity.getText()) == 0) {
            JOptionPane.showMessageDialog(null, "Please enter positive number only!");
            order(count);
        } else {
            orderAL.get(orderAL.size()-1).setOrderList(count++, cbb_name.getSelectedIndex());
            orderAL.get(orderAL.size()-1).setOrderList(count++, cbb_size.getSelectedIndex());
            orderAL.get(orderAL.size()-1).setOrderList(count++, cbb_sugar.getSelectedIndex());
            orderAL.get(orderAL.size()-1).setOrderList(count++, cbb_ice.getSelectedIndex());
            for (int i = 0; i < cb_addon.length; i++) {//num = 0-4, total 5, arr 4-8
                if (cb_addon[i].isSelected()) orderAL.get(orderAL.size()-1).setOrderList(count++, 1);
                else orderAL.get(orderAL.size()-1).setOrderList(count++, 0);
            } orderAL.get(orderAL.size()-1).setOrderList(count++, Integer.parseInt(tf_quantity.getText()));
        }
    }
    
    public static boolean orderExtra(){
        int opt = JOptionPane.showConfirmDialog(null, "Add more order?", menuText[1], JOptionPane.YES_NO_OPTION);
        return (opt == 0);
    }
    
    public static void receipt(int memberYN, Member member){
        Order order = orderAL.get(orderAL.size()-1);
        String receipt = line(100) + line(2) + centerText("OOP Milk Tea Shop") + line(0) + String.format(" %-30s %9s", order.getDate(), order.getTime())
                + "\n" + centerText("RECEIPT NO: #" + order.getOrderID()) + line(0) + centerText("CUSTOMER INFORMATION") + line(1);//print header
        if (memberYN == 1) receipt += " MemberID: " + member.getMemberID()+ "\n" + " Name\t: " + member.getName() + "\n Phone\t: " + member.getHp(); //print member info
        else receipt += " Name\t: " + custAL.get(custAL.size()-1).getName() + "\n Phone\t: " + custAL.get(custAL.size()-1).getHp(); //get customer info
        receipt += line(0) + String.format("%-30s %10s", " ITEM", "RM  ") + line(1);// print title
        int priceTotalReal = 0, subTotal = 0, total = 0, k = 0;
        for (int j = 1; j <= (order.getOrderList().size()+1)/10; j++) {
            receipt += LRText(String.valueOf(order.getOrderList(k+9)) + ": " + stock.getMilkTea((int)order.getOrderList(k)), String.valueOf(stock.getPrice(0)) + "  "); //print quantity, name, priceMilkTea
            if(order.getOrderList(k+1).equals(1)) { //if size = large
                receipt += LRText("\n    * " + stock.getSize(1), String.valueOf(stock.getPrice(2))); //print size & size price
                subTotal += (stock.getPrice(2) + stock.getPrice(0)) * (int)order.getOrderList(k+9); //cal size(Large) * priceMT * quantity
                report.addRecord(((int)order.getOrderList(k))+5, (int)order.getOrderList(k+9)); //add record MilkTea Large
            } else {
                subTotal += (stock.getPrice(0) * (int)order.getOrderList(k+9));
                report.addRecord((int)order.getOrderList(k), (int)order.getOrderList(k+9)); //add record MilkTea
            } //cal priceMT * quantity
            receipt += LRText("\n    * " + stock.getSugar((int)order.getOrderList(k+2)) , " "); //print sugar
            receipt += LRText("\n    * " + stock.getIce((int)order.getOrderList(k+3)) , " "); //print ice
            for (int i = 4; i <= 8; i++) { //addon
                if (order.getOrderList(k+i).equals(1)) {
                    receipt += LRText("\n    + " + stock.getAddon((i-4)) , String.valueOf(stock.getPrice(1))); //print addon
                    report.addRecord(i + 6, 1); //add record addon
                    subTotal += stock.getPrice(1)*(int)order.getOrderList(k+9); //cal addon
                }
            } receipt += "\n" + LRText(" ", "-----") + "\n" + LRText(" ", String.valueOf(subTotal)) + "\n\n";//print subTotal
            total += subTotal;
            subTotal = 0;
            k += 10;
        }
        order.addPrice(total); // add subTotal into totalPrice
        if(memberYN == 1)member.addPoint(total); //add subTotal to member point
        receipt += line(1) + LRText("TOTAL", String.valueOf(order.getPrice())) + line(2); //print total
        if (memberYN == 1) receipt += centerText("POINT OWNED : " + member.getPoint()) + line(0);//print point owened
        receipt += centerText("THANK FOR VISITING") + line(2);//print footer
        System.out.println(receipt);//print receipt in console
        JOptionPane.showMessageDialog(null, "Receipt has sent to console");//notice
        menuMain();
    }
    
    public static void menuMember(){
        String[] optBtn = {"Register", "Check point", "Redeem", "Back"};
        int opt = JOptionPane.showOptionDialog(null, "Choose a service:", menuText[2], 0, 1, null, optBtn, null);
        switch (opt) {
            case 0: memberRegister();
                    menuMember(); break;
            case 1: memberCheckPoint(getMember(isMember(inputMemberId(1, 5)))); 
                    menuMember(); break;
            case 2: memberRedeem(getMember(isMember(inputMemberId(1, 6))));
                    menuMember(); break;
            default:menuMain();
        }
    }
     
    public static void memberRegister(){
        String[] arrCust = inputCustomerInfo(4,0);
        custAL.add(new Member(arrCust[0],arrCust[1]));
        Member member = (Member)custAL.get(custAL.size()-1);
        JOptionPane.showMessageDialog(null, "Member Registration successfully!\nYour Member ID: " + member.getMemberID(), null, 1);
    }
       
    static Member getMember(int index){
        if(index == -1) menuMember();
        return ((Member)custAL.get(index));
    }
    
    static void memberCheckPoint(Member member){
        JOptionPane.showMessageDialog(null, "Member ID: " + member.getMemberID()+ "\nPoint Earned: " + member.getPoint(), null, 1);
    }
    
    static void memberRedeem(Member member){
        JComboBox cbb_name = new JComboBox(stock.getMilkTea());
        cbb_name.setSelectedIndex(-1);
        String[] optBtn = {"Continue", "Back"};
        Object[] content = {"Milk Tea", cbb_name};
        int opt = JOptionPane.showOptionDialog(null, content, menuText[1], 0, 3, null, optBtn, null);
        if (opt == -1 || opt == 1) menuMember();
        if (cbb_name.getSelectedIndex() == -1) JOptionPane.showMessageDialog(null, "Please select the Milk Tea!");
        else if (member.getPoint() >= 100) {
            member.minusPoint();
            JOptionPane.showMessageDialog(null, "Member ID: " + member.getMemberID()+ "\nRedeem Item: " + cbb_name.getSelectedItem() + "\nPoint Left: " + member.getPoint(), null, 1);
        } else JOptionPane.showMessageDialog(null, "Point not enough!!!\nMember ID: " + member.getMemberID()+ "\nPoint Left: " + member.getPoint() + " (Required 100 points)", null, 0);
    }
 
    public static void saleReport(){
        System.out.println(report);
        JOptionPane.showMessageDialog(null, "Sale Report has sent to console");
    }
    
    public static int isMember(String memberId){
        for (int i = 0; i < custAL.size(); i++) {
            if (custAL.get(i) instanceof Member){
                Member member = (Member)custAL.get(i);
                if (member.checkMember(memberId)) return i;
                else if(custAL.size() == (i + 1)) {
                    JOptionPane.showMessageDialog(null, "Member ID not found", null, 0);
                    return -1;
                }
            }
        } return -1;
    }
    
    public static String inputMemberId(int type, int menuTxt){//type: 0=menuMain 1=menuMember, 2=menuOrder, 3=memberRedeem
        String inputMemberId = JOptionPane.showInputDialog(null, "Enter your member ID:", menuText[menuTxt], 3);
        if(inputMemberId == null) {
            if (type == 0) menuMain();
            else if (type == 1) menuMember();
            else if (type == 2) menuOrder();
        }
        if(!checkInput(inputMemberId, 0)) inputMemberId(type, menuTxt);
        return inputMemberId;
    }
        
    public static String[] inputCustomerInfo(int menuTxt, int type){ // input name, hp; type:0=menuMember(),1=menuOrder
        JTextField custName = new JTextField(),
            custHp = new JTextField(){ public void processKeyEvent(KeyEvent input) {// only positive integer can input
                if (input.getKeyChar() >= 48 && input.getKeyChar() <= 57 || input.getKeyChar() == 8 || input.getKeyChar() == 127 || input.getKeyCode()== KeyEvent.VK_LEFT || input.getKeyCode()== KeyEvent.VK_RIGHT) super.processKeyEvent(input);
            }};
        Object[] content = { "Enter your personal information:\n", "NAME", custName, "PHONE", custHp};
        String[] optBtn = {"Continue", "Cancel"};
        do {
            int opt = JOptionPane.showOptionDialog(null, content, menuText[menuTxt], 0, 3, null, optBtn, null);
            if (opt == -1 || opt == 1) {
                if(type == 0) menuMember();
                else if(type == 1) menuOrder();
            } else if (custName.getText().isEmpty() || custHp.getText().isEmpty()) JOptionPane.showMessageDialog(null, "Please fill in all the details!", null, 2);
        } while(custName.getText().isEmpty() || custHp.getText().isEmpty());
        String[] arr = {custName.getText(), custHp.getText()};
        return arr;
    }
    
    public static void exit(){
        int opt = JOptionPane.showConfirmDialog(null, "Exit?", null, 2);
        if (opt == 0) {
            System.out.println("System Stopped...");
            System.exit(0);
        }
        else menuMain();
    }
    
    public static boolean checkInput(String input, int opt){ // 0 = , 1 = number > 0
        if(input == null) return false;
        else if(input.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Don't leave blank!", null, 2);
            return false;
        } else {
            try {
                if (opt == 1 && Integer.parseInt(input) <= 0) {
                    JOptionPane.showMessageDialog(null, "Enter positive integer only!", null, 2); 
                    return false;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Enter number only!", null, 2); 
                return false;
            }
        } return true;
    }
    public static String centerText(String text){ return String.format("%"+((40 - text.length())/2)+"s %"+text.length()+"s %"+((40 - text.length())/2)+"s", "", text, "");}
    public static String LRText(String left, String right){ return String.format(" %-30s %8s", left, right);}
    public String LMRText(String txt1, int num1, int num2){return String.format("%-22s %8d %8d", txt1, num1, num2);}
    public static String line(int type){ return (type == 0)? "\n******************************************\n": (type == 1)? "\n------------------------------------------\n": (type == 2)? "\n==========================================\n": "\n\n\n\n\n\n\n\n\n\n\n";}
}