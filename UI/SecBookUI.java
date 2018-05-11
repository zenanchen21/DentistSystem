import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Container;
import java.awt.event.*;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.Date;
import java.util.ArrayList;

public class SecBookUI extends JFrame{

  JButton back;

  public SecBookUI(){

    setTitle("Appointment");
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenDimensions = toolkit.getScreenSize();
    setSize(screenDimensions.width, screenDimensions.height);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel emptyPanel = new JPanel();
    emptyPanel.add(new JLabel(" "));

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

    //Create and fill (from db) combobox for partners
    JLabel partnerLabel = new JLabel("Partner:");
    JComboBox partnerSelector = new JComboBox();
    QueryResult partners = Database.performSearch("partners", "");
    ArrayList<Partner> partnersList = new ArrayList<Partner>();
    for(Row r : partners){
      partnersList.add(new Partner(r));
      partnerSelector.addItem(r.getValueAsString("role") + ", " + r.getValueAsString("fullName"));
    }

    JButton searchButton = new JButton("Search");

    JButton bookButton = new JButton("Book this Appointment");

    JButton delButton = new JButton("Delete this Appointment");

    JTextField dateField = new JTextField(10);
    dateField.setText("01/01/2017");

    JTextField startField = new JTextField(4);
    startField.setText("0900");

    JTextField endField = new JTextField(4);
    endField.setText("0920");

    JPanel dateArea = new JPanel();
    dateArea.setLayout(new FlowLayout());
    dateArea.add(new JLabel("DD/MM/YYYY:"));
    dateArea.add(dateField);

    JPanel startArea = new JPanel();
    startArea.setLayout(new FlowLayout());
    startArea.add(new JLabel("Start Time:"));
    startArea.add(startField);

    JPanel endArea = new JPanel();
    endArea.setLayout(new FlowLayout());
    endArea.add(new JLabel("End Time:"));
    endArea.add(endField);

    JPanel treatmentArea = new JPanel();
    treatmentArea.setLayout(new FlowLayout());
    treatmentArea.add(new JLabel("treatment"));
    JComboBox treatmentSelector = new JComboBox();
    QueryResult treatments = Database.performSearch("treatments", "");
    for(Row r : treatments){
      treatmentSelector.addItem(r.getValueAsString("treatmentName"));
    }
    treatmentArea.add(treatmentSelector);


    back = new JButton("back");
    back.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame exist = new SecExistPUI();
        exist.setVisible(true);
      }
    });

    JPanel datePanel = new JPanel();
    datePanel.setLayout(new FlowLayout());
    datePanel.add(dateArea);
    datePanel.add(startArea);
    datePanel.add(endArea);

    JPanel patientPanel = new JPanel();
    patientPanel.setLayout(new GridLayout(0, 4));
    patientPanel.add(patientfn);
    patientPanel.add(patientfnEnter);
    patientPanel.add(patientsn);
    patientPanel.add(patientsnEnter);
    patientPanel.add(patienthn);
    patientPanel.add(patienthnEnter);
    patientPanel.add(patientpc);
    patientPanel.add(patientpcEnter);
    patientPanel.add(searchButton);
    patientPanel.add(patientSelector);
    patientPanel.add(partnerLabel);
    patientPanel.add(partnerSelector);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    buttonPanel.add(bookButton);
    buttonPanel.add(delButton);
    buttonPanel.add(back);

    JPanel bookPanel = new JPanel();
    bookPanel.setLayout(new GridLayout(0,1));
    bookPanel.add(emptyPanel);
    bookPanel.add(patientPanel);
    bookPanel.add(datePanel);
    bookPanel.add(treatmentArea);
    bookPanel.add(buttonPanel);

    Container contentPane = getContentPane();
    contentPane.setLayout(new GridLayout(2,0));
    contentPane.add(bookPanel);

    //Searching for patients and adding it to a combo box.
    searchButton.addActionListener(new ActionListener() {
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

    bookButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String dateStr = Database.toSQLDate(dateField.getText());

        Partner part = partnersList.get(partnerSelector.getSelectedIndex());
        Patient pat = patientList.get(patientSelector.getSelectedIndex());
        String treatment = (String)treatmentSelector.getSelectedItem();

        Appointment app = new Appointment(startField.getText(), endField.getText(), false, dateStr, pat.getPatientID(), part.getPartnerID(),  treatment);
        Database.performInsert(app);
      }
    });


  }

  public static void main(String[] args) {
      SecBookUI secBookUI = new SecBookUI();
      secBookUI.setVisible(true);
    }
  }
