public class HealthcarePlan {
    private String name;
    private int checkups;
    private int hygieneVisits;
    private int repairs;
    private float monthlyCost;

    public HealthcarePlan(String n, int ch, int h, int r, float co) {
        name = n;
        checkups = ch;
        hygieneVisits = h;
        repairs = r;
        monthlyCost = co;
    }

    public HealthcarePlan(Row r) {
        name = r.getValueAsString("planName");
        checkups = r.getValueAsInt("checkups");
        hygieneVisits = r.getValueAsInt("hygieneVisits");;
        repairs = r.getValueAsInt("repairs");;
        monthlyCost = r.getValueAsFloat("monthlyCost");;
    }

    public String getName() {return name;}
    public int getCheckups() {return checkups;}
    public int getHygieneVisits() {return hygieneVisits;}
    public int getRepairs() {return repairs;}
    public float getMonthlyCost() {return monthlyCost;}
}
