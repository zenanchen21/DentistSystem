import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Container;
import java.awt.event.*;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;

public class SecPlanUI extends JFrame{

  JButton back;

  public SecPlanUI(){

    setTitle("health Care Plan");
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenDimensions = toolkit.getScreenSize();
    setSize(screenDimensions.width, screenDimensions.height);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JComboBox<String> healthPlanC = new JComboBox<String>();
    QueryResult plans = Database.performSearch("healthcareplans" , "");
    for(Row plan : plans){
      healthPlanC.addItem(plan.getValueAsString("planName"));
    }

    JPanel planInfoPanel = new JPanel();
    planInfoPanel.setLayout(new GridLayout(0, 1));
    JLabel checkups = new JLabel("Check Ups: " + plans.get(0).getValueAsInt("checkups"));
    planInfoPanel.add(checkups);
    JLabel hygieneVisits = new JLabel("hygiene Visits: " + plans.get(0).getValueAsInt("hygieneVisits"));
    planInfoPanel.add(hygieneVisits);
    JLabel repairs = new JLabel("Repairs: " + plans.get(0).getValueAsInt("repairs"));
    planInfoPanel.add(repairs);
    JLabel cost = new JLabel("Cost: " + plans.get(0).getValueAsFloat("monthlyCost"));
    planInfoPanel.add(cost);

    healthPlanC.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {

        QueryResult res = Database.performSearch("healthcareplans", "planName='"+(String)e.getItem()+"'");
        Row planInfo = res.getRow(res.lastRow());
        checkups.setText("Check Ups: " + planInfo.getValueAsInt("checkups"));
        hygieneVisits.setText("hygiene Visits: " + planInfo.getValueAsInt("hygieneVisits"));
        repairs.setText("Repairs: " + planInfo.getValueAsInt("repairs"));
        cost.setText("Cost: " + planInfo.getValueAsFloat("monthlyCost"));
      }
    });


    JPanel healthCarePlan = new JPanel();
    healthCarePlan.add(new JLabel("Health Care Plan:"));
    healthCarePlan.add(healthPlanC);

    //Fields needed for patient searching.
    JLabel patientfn = new JLabel("patient forename:");
    JTextField patientfnEnter = new JTextField(10);
    JLabel patientsn = new JLabel("patient surname:");
    JTextField patientsnEnter = new JTextField(10);
    JLabel patienthn = new JLabel("patient house number:");
    JTextField patienthnEnter = new JFormattedTextField(NumberFormat.getIntegerInstance());
    patienthnEnter.setColumns(4);
    JLabel patientpc = new JLabel("patient postcode:");
    JTextField patientpcEnter= new JTextField(8);
    ArrayList<Patient> patientList = new ArrayList<Patient>();
    JComboBox patientSelector = new JComboBox();

    JButton searchB = new JButton("Search");
    //Searching for patients and adding it to a combo box.
    searchB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String forename = patientfnEnter.getText();
        String surname = patientsnEnter.getText();
        String hNumber = patienthnEnter.getText();
        String postCode = patientpcEnter.getText();

        String conditions = "forename='%s' AND surname='%s' AND houseNumber=%s AND postCode='%s'";
        conditions = String.format(conditions, forename, surname, hNumber, postCode);
        QueryResult res = Database.performSearch("patients", conditions);

        patientList.clear();
        patientSelector.removeAllItems();
        for(Row row : res){
          String name = row.getValueAsString("title") + ". " + row.getValueAsString("forename") + " " + row.getValueAsString("surname");
          String dob = ((Date)row.getValue("dateOfBirth")).toString();
          patientSelector.addItem(name + ", " + dob);
          patientList.add(new Patient(row));
        }
        if(res.size() == 0){
          patientSelector.addItem("No matching patients found");
        }

      }
    });


    JPanel searchArea = new JPanel();
    searchArea.add(searchB);

    JButton addB = new JButton("Add this Plan");

    //Update the Database so it reflest the patients new plan.
    addB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Patient patient = patientList.get(patientSelector.getSelectedIndex());
        patient.updateHealthCarePlan((String)healthPlanC.getSelectedItem());
        Database.performUpdate(patient);
      }
    });


    JPanel addArea = new JPanel();
    addArea.add(addB);

    JButton delB = new JButton("Delete this Plan");
    //Update the Database so it reflest the patients new plan.
    delB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Patient patient = patientList.get(patientSelector.getSelectedIndex());
        patient.updateHealthCarePlan(null);
        Database.performUpdate(patient);
      }
    });

    JPanel delArea = new JPanel();
    delArea.add(delB);

    JPanel emptyPanel = new JPanel();
    emptyPanel.add(new JLabel(" "));

    back = new JButton("back");
    back.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame exist = new SecExistPUI();
        exist.setVisible(true);
      }
    });

    JPanel patientSearchArea = new JPanel();
    patientSearchArea.setLayout(new FlowLayout());
    JPanel patientPanel = new JPanel();
    patientPanel.setLayout(new GridLayout(0,2));
    patientPanel.add(patientfn);
    patientPanel.add(patientfnEnter);
    patientPanel.add(patientsn);
    patientPanel.add(patientsnEnter);
    patientPanel.add(patienthn);
    patientPanel.add(patienthnEnter);
    patientPanel.add(patientpc);
    patientPanel.add(patientpcEnter);
    patientPanel.add(patientSelector);
    patientPanel.add(searchArea);
    patientSearchArea.add(patientPanel);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    buttonPanel.add(addArea);
    buttonPanel.add(delArea);
    buttonPanel.add(back);

    JPanel planPanel = new JPanel();
    planPanel.setLayout(new GridLayout(4,1));
    planPanel.add(emptyPanel);
    planPanel.add(patientSearchArea);
    planPanel.add(healthCarePlan);
    planPanel.add(planInfoPanel);
    planPanel.add(buttonPanel);

    Container contentPane = getContentPane();
    contentPane.setLayout(new GridLayout(0,2));
    contentPane.add(planPanel);
  }

  public static void main(String[] args) {
      SecPlanUI secPlanUI = new SecPlanUI();
      secPlanUI.setVisible(true);
    }
  }
