import java.awt.GridLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.event.*;

public class SecExistPUI extends JFrame{
  JButton healthCareB;
  JButton appointmentB;
  JButton treatmentB;
  JButton back;

  public SecExistPUI(){
    setTitle("Exist Patient");
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenDimensions = toolkit.getScreenSize();
    setSize(screenDimensions.width/2, screenDimensions.height/2);
    setLocation(new Point(screenDimensions.width/4, screenDimensions.height/4));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    healthCareB = new JButton("Health Care Plan");
    healthCareB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame plan = new SecPlanUI();
        plan.setVisible(true);
      }
    });
    appointmentB = new JButton("Appointment");
    appointmentB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame book = new SecBookUI();
        book.setVisible(true);
      }
    });
    treatmentB = new JButton("Treatment");
    treatmentB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame treat = new SecTreatUI();
        treat.setVisible(true);
      }
    });

    back = new JButton("back");
    back.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame menu = new SecMenuUI();
        menu.setVisible(true);
      }
    });

    Container contentPane = getContentPane();
    contentPane.setLayout(new GridLayout(4,0));
    contentPane.add(healthCareB);
    contentPane.add(appointmentB);
    contentPane.add(treatmentB);
    contentPane.add(back);
  }

public static void main(String[] args) {
    SecExistPUI secExistPUI = new SecExistPUI();
    secExistPUI.setVisible(true);
  }
}
