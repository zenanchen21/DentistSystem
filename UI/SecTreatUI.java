import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Container;
import java.awt.event.*;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;

public class SecTreatUI extends JFrame{

  JButton back;

  public SecTreatUI(){

    setTitle("View Treatment");
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
          patientList.add(new Patient(row));
          patientSelector.addItem(name + ", " + dob);
        }
        if(res.size() == 0){
          patientSelector.addItem("No matching patients found");
        }

      }
    });


    JPanel searchArea = new JPanel();
    searchArea.add(searchB);
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

    back = new JButton("back");
    back.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame exist = new SecExistPUI();
        exist.setVisible(true);
      }
    });

    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new FlowLayout());

    Object columnNames[] = { "Date", "Treatment", "Cost"};
    JTable treatmentHist = new JTable();
    DefaultTableModel dtm = new DefaultTableModel(0, 0);
    dtm.setColumnIdentifiers(columnNames);
    treatmentHist.setModel(dtm);

    JPanel payPanel = new JPanel();
    payPanel.setLayout(new FlowLayout());
    JLabel totCost = new JLabel("Total Owed: ");
    JButton payB = new JButton("Pay total");
    payB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Patient p = patientList.get(patientSelector.getSelectedIndex());
        p.setTotalOwed(0.0f);

        Database.performUpdate(p);
      }
    });

    payPanel.add(totCost);
    payPanel.add(payB);

    infoPanel.add(treatmentHist);
    infoPanel.add(payPanel);

    patientSelector.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        String[] tables = new String[] { "appointments", "treatments" };
        String[] columns1 = new String[] { "treatment" };
        String[] columns2 = new String[] { "treatmentName" };
        String cond = "patientID=" + patientList.get(patientSelector.getSelectedIndex()).getPatientID();
        QueryResult appointments = Database.performSearch(tables, "INNER", columns1, columns2, cond);

        for(Row r : appointments){
          String t = r.getValueAsString("treatmentName");
          String date = ((Date)r.getValue("date")).toString();
          String cost= String.valueOf(r.getValueAsFloat("cost"));
          dtm.addRow(new Object[]{t, date , cost }); //Add row to the db.

        }

        totCost.setText("Total Cost: £" + patientList.get(patientSelector.getSelectedIndex()).getTotalOwed()); //Pretty sure this total will be wrong in the long term...

      }
    });

    JPanel bookPanel = new JPanel();
    bookPanel.setLayout(new GridLayout(0,1));
    bookPanel.add(emptyPanel);
    bookPanel.add(patientSearchArea);
    bookPanel.add(infoPanel);

    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(bookPanel, BorderLayout.NORTH);
    contentPane.add(back, BorderLayout.SOUTH);
  }

  public static void main(String[] args) {
      SecTreatUI secTreatUI = new SecTreatUI();
      secTreatUI.setVisible(true);
    }
  }
