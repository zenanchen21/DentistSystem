import java.sql.Date;

public class Appointment {
    private String startTime;
    private String endTime;
    private boolean isComplete;
    private String date;
    private int patient;
    private int partner;
    private String treatment;

    public Appointment(String s, String e, boolean c, String d, int pc, int pb, String t) {
        startTime = s;
        endTime = e;
        isComplete = c;
        date = d;
        patient = pc;
        partner = pb;
        treatment = t;
    }


    public String getStartTime(){ return startTime;}
    public String getEndTime(){ return endTime;}
    public boolean getIsComplete(){return isComplete;}
    public String getDate(){return date;}
    public int getPatient(){ return patient;}
    public int getPartner(){ return partner;}
    public String getTreatment(){return treatment;}
}
