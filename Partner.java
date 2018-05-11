public class Partner {
    private int partnerID;
    private String role;
    private String fullName;

    public Partner(int p, String r, String n) {
        partnerID = p;
        role = r;
        fullName = n;
    }

    public Partner(Row r){
        partnerID = r.getValueAsInt("partnerID");
        role = r.getValueAsString("role");
        fullName = r.getValueAsString("fullName");
    }

    public int getPartnerID(){return partnerID;}
    public String getRole(){return role;}
    public String getFullName(){return fullName;}

}
