public class LibraryMember {
    private String memberType;
    private int ID;
    private static int counter = 0;
    public LibraryMember(String Type) {
         this.memberType = Type.equals("S") ? "Student" : "Academic";

        this.memberType = memberType;
        counter++;
        if (counter == 1000000)throw new Error();
        this.ID = counter;
    }

    public int getID() {
        return ID;
    }

    public String getMemberType() {
        return memberType;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberType='" + memberType + '\'' +
                ", ID=" + ID +
                '}';
    }
}
