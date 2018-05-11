import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Container;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.*;
import java.util.ArrayList;

public class SecHolidayUI extends JFrame{

  JButton back;

  public SecHolidayUI(){

    setTitle("Secretary Holiday Bookings");
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenDimensions = toolkit.getScreenSize();
    setSize(screenDimensions.width, screenDimensions.height);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JComboBox<String> selectBox = new JComboBox<String>();
    //Fetching Partners from the DB
    QueryResult partners = Database.performSearch("partners", "");
    ArrayList<Partner> partnerList = new ArrayList<Partner>();
    for(Row partner : partners ){
      partnerList.add(new Partner(partner));
      selectBox.addItem(partner.getValueAsString("role") + ", " + partner.getValueAsString("fullName"));
    }

    JPanel selectPanel = new JPanel();
    selectPanel.add(new JLabel("Choose your position:"));
    selectPanel.add(selectBox);

    JPanel emptyPanel = new JPanel();
    emptyPanel.add(new JLabel(" "));

    JButton addB = new JButton("Book this Holiday");

    JButton delB = new JButton("Delete this Holiday");

    JTextField dateField = new JTextField(10);

    JTextField startField = new JTextField(8);

    JTextField endField = new JTextField(8);

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

    back = new JButton("back");
    back.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame menu = new SecMenuUI();
        menu.setVisible(true);
      }
    });

    JPanel datePanel = new JPanel();
    datePanel.setLayout(new FlowLayout());
    datePanel.add(dateArea);
    datePanel.add(startArea);
    datePanel.add(endArea);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    buttonPanel.add(addB);
    buttonPanel.add(delB);
    buttonPanel.add(back);

    JPanel holidayPanel = new JPanel();
    holidayPanel.setLayout(new GridLayout(4,1));
    holidayPanel.add(emptyPanel);
    holidayPanel.add(selectPanel);
    holidayPanel.add(datePanel);
    holidayPanel.add(buttonPanel);

    Container contentPane = getContentPane();
    contentPane.setLayout(new GridLayout(2,0));
    contentPane.add(holidayPanel);

    addB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String dateStr = Database.toSQLDate(dateField.getText());

        Partner part = partnerList.get(selectBox.getSelectedIndex());

        Appointment app = new Appointment(startField.getText(), endField.getText(), false, dateStr, -1, part.getPartnerID(), "");
        Database.performInsert(app);
      }
    });
  }

  public static void main(String[] args) {
      SecHolidayUI secHolidayUI = new SecHolidayUI();
      secHolidayUI.setVisible(true);
    }
  }
