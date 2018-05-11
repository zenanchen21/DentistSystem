
import java.sql.Date;

public class Patient {
    private int patientID;
    private String title;
    private String forename;
    private String surname;
    private String dateOfBirth;
    private float totalOwed;
    private int checkUpsThisYear;
    private int hygeineVisitsThisYear;
    private int repairsThisYear;
    private Address address;
    private String healthcarePlan;


    public Patient(int id, String t, String fn, String sn, String dob, float to, int cu, int hv, int r, Address a, String h ){
        patientID = id;
        title = t;
        forename = fn;
        surname = sn;
        dateOfBirth = dob;
        address = a;

        healthcarePlan = h;
        totalOwed = to;
        checkUpsThisYear = cu;
        hygeineVisitsThisYear = hv;
        repairsThisYear = r;
    }

    public Patient(Row r){
        patientID = r.getValueAsInt("patientID");
        title = r.getValueAsString("title");
        forename = r.getValueAsString("forename");
        surname = r.getValueAsString("surname");
        dateOfBirth = ((Date)r.getValue("dateOfBirth")).toString();;
        address = new Address(r.getValueAsInt("houseNumber"), "", "", "", r.getValueAsString("postCode"));

        healthcarePlan = r.getValueAsString("healthCarePlan");
        Object to = r.getValue("totalOwed");
        float amt = 0.0f;
        if(to != null) amt = (float)to;
        totalOwed = amt;

        Object cu = r.getValue("checkUpsThisYear");
        if(cu == null) checkUpsThisYear = 0;
        else checkUpsThisYear = (int)cu;

        Object hv = r.getValue("hygieneVisitsThisYear");
        if(hv == null) hygeineVisitsThisYear = 0;
        else  hygeineVisitsThisYear = (int)hv;

        Object re = r.getValue("repairsThisYear");
        if(re == null) repairsThisYear = 0;
        else  repairsThisYear = (int)re;
    }

    public int getPatientID(){ return patientID; }
    public String getTitle(){ return title; }
    public String getForename(){ return forename; }
    public String getSurname(){ return surname; }
    public Object getDOB(){ return dateOfBirth; }
    public float getTotalOwed(){ return totalOwed; }
    public int getCheckUpsThisYear(){ return checkUpsThisYear; }
    public int getHygeineVisitsThisYear(){ return hygeineVisitsThisYear; }
    public int getRepairsThisYear(){ return repairsThisYear; }
    public Address getAddress(){ return address;}
    public String getHealthcarePlan(){ return healthcarePlan;}

    public void updateHealthCarePlan(String newPlan){ healthcarePlan = newPlan; }
    public void setTotalOwed(float owed){ totalOwed = owed; }
    public void setRepairsThisYear(int r){ repairsThisYear = r; }
    public void setCheckUpsThisYear(int r){ checkUpsThisYear= r; }
    public void setHygeineVisitsThisYear(int r){ hygeineVisitsThisYear= r; }


}
