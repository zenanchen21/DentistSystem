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

public class SecRegisterUI extends JFrame{

  JButton back;
  JButton submit;

  public SecRegisterUI(){

    setTitle("Register");
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenDimensions = toolkit.getScreenSize();
    setSize(screenDimensions.width, screenDimensions.height);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JComboBox<String> titleBox = new JComboBox<String>(
                  new String[] {"Mr", "Mrs", "Ms","Miss","Dr"});

    JPanel titleArea = new JPanel();
    titleArea.add(new JLabel("Title"));
    titleArea.add(titleBox);

    JTextField fNameField = new JTextField(10);

    JPanel fNameArea = new JPanel();
    fNameArea.add(new JLabel("ForeName:"));
    fNameArea.add(fNameField);

    JTextField sNameField = new JTextField(10);

    JPanel sNameArea = new JPanel();
    sNameArea.add(new JLabel("SureName:"));
    sNameArea.add(sNameField);

    JTextField houseField = new JTextField(10);

    JPanel houseArea = new JPanel();
    houseArea.add(new JLabel("House Number:"));
    houseArea.add(houseField);

    JTextField streetField = new JTextField(10);

    JPanel streetArea = new JPanel();
    streetArea.add(new JLabel("Street:"));
    streetArea.add(streetField);

    JTextField districtField = new JTextField(10);

    JPanel districtArea = new JPanel();
    districtArea.add(new JLabel("District:"));
    districtArea.add(districtField);

    JTextField cityField = new JTextField(10);

    JPanel cityArea = new JPanel();
    cityArea.add(new JLabel("City:"));
    cityArea.add(cityField);

    JTextField postCodeField = new JTextField(10);

    JPanel postCodeArea = new JPanel();
    postCodeArea.add(new JLabel("Post Code:"));
    postCodeArea.add(postCodeField);

    JTextField dateField = new JTextField(10);

    JPanel dateArea = new JPanel();
    dateArea.add(new JLabel("DD/MM/YYYY:"));
    dateArea.add(dateField);

    submit = new JButton("Submit");
    submit.addActionListener(new ActionListener() {
                               @Override
                               public void actionPerformed(ActionEvent e) {
                                 //Create the Address and Patient Objects
                                 Address a = new Address(Integer.parseInt(houseField.getText()), streetField.getText(), districtField.getText(), cityField.getText(), postCodeField.getText());
                                 String dob = Database.toSQLDate(dateField.getText());
                                 Patient p = new Patient(0, titleBox.getSelectedItem().toString(), fNameField.getText(), sNameField.getText(), dob, 0.0f, 0, 0, 0, a, null);

                                 //Insert them into the db (note Address is inserted first, as patient relies on it)
                                 Database.performInsert(a);
                                 Database.performInsert(p);
                                 System.out.println(Database.performSearch("patients", ""));
                               }
                             });

            back = new JButton("Back");
    back.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame menu = new SecMenuUI();
        menu.setVisible(true);
      }
    });

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(submit);
    buttonPanel.add(back);

    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new GridLayout(11,0));
    infoPanel.add(titleArea);
    infoPanel.add(fNameArea);
    infoPanel.add(sNameArea);
    infoPanel.add(dateArea);
    infoPanel.add(houseArea);
    infoPanel.add(streetArea);
    infoPanel.add(districtArea);
    infoPanel.add(cityArea);
    infoPanel.add(postCodeArea);
    infoPanel.add(submit);
    infoPanel.add(back);


    Container contentPane = getContentPane();
    contentPane.setLayout(new GridLayout(0,1));
    contentPane.add(infoPanel);
    }

public static void main(String[] args) {
    SecRegisterUI secRegisterUI = new SecRegisterUI();
    secRegisterUI.setVisible(true);
  }
}
