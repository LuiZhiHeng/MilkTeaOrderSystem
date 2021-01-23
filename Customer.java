public class Customer {
    private String name, hp;

    public Customer() {
    }

    public Customer(String name, String hp) {
        this.name = name;
        this.hp = hp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

}

class Member extends Customer {
    private static int numMember = 0;
    private int point = 0;
    private String id;

    public Member() {
        setMemberID();
    }
    
    public Member(String name, String hp) {
        super(name, hp);
        setMemberID();
    }

    private void setMemberID() {
        this.id = "M" + String.format("%4s", ++numMember).replace(" ", "0");
    }
    
    public String getMemberID() {
        return id;
    }

    public void addPoint(int point) {
        this.point += point;
    }

    public void minusPoint() {
        this.point -= 100;
    }

    public int getPoint() {
        return point;
    }

    public int getNumMember() {
        return numMember;
    }
    
    public boolean checkMember(String id) {
        return id.equals(getMemberID());
    }
}

