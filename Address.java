public class Address {
    private int houseNumber;
    private String streetName;
    private String districtName;
    private String city;
    private String postCode;

    public Address(int h, String s, String d, String c, String p) {
        houseNumber = h;
        streetName = s;
        districtName = d;
        city = c;
        postCode = p;
    }

    public int getHouseNumber(){ return houseNumber;}
    public String getStreetName(){ return streetName;}
    public String getDistrictName(){ return districtName;}
    public String getCity(){return city;}
    public String getPostCode(){ return postCode;}
}
