import java.awt.GridLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class SecMenuUI extends JFrame{

  JButton back;
  JButton calendar;
  JButton newPatient;
  JButton existPatient;
  JButton holidayBook;

  public SecMenuUI(){
    setTitle("Menu");
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenDimensions = toolkit.getScreenSize();
    setSize(screenDimensions.width/2, screenDimensions.height/2);
    setLocation(new Point(screenDimensions.width/4, screenDimensions.height/4));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    calendar = new JButton("Calendar");
     calendar.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         dispose();
         Partners calendar = new Partners(3);
         calendar.setVisible(true);

       }
     });

    newPatient = new JButton("New Patient");
    newPatient.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame registerFrame = new SecRegisterUI();
        registerFrame.setVisible(true);
      }
    });
    existPatient = new JButton("Exist Patient");
    existPatient.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame existFrame = new SecExistPUI();
        existFrame.setVisible(true);
      }
    });

    holidayBook = new JButton("Holiday Book");
    holidayBook.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame holiFrame = new SecHolidayUI();
        holiFrame.setVisible(true);
      }
    });

    back = new JButton("back");
    back.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame staff = new StaffUI();
        staff.setVisible(true);
      }
    });

    Container contentPane = getContentPane();
    contentPane.setLayout(new GridLayout(5,0));
    contentPane.add(calendar);
    contentPane.add(newPatient);
    contentPane.add(existPatient);
    contentPane.add(holidayBook);
    contentPane.add(back);
  }

public static void main(String[] args) {
    SecMenuUI secMenuUI = new SecMenuUI();
    secMenuUI.setVisible(true);
  }
}
