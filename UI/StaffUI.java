
import java.awt.Container;

//import com2002.Partners;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JPopupMenu;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Font;

public class StaffUI extends JFrame{
  JButton btnSecretary;
  JButton btnDentist;
  JButton btnHygienist;
  JButton btnExit;

  public StaffUI() {
    setTitle("Menu");
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenDimensions = toolkit.getScreenSize();
    setSize(screenDimensions.width/2, screenDimensions.height/2);
    setLocation(new Point(screenDimensions.width/4, screenDimensions.height/4));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    btnSecretary = new JButton("Secretary");
    btnSecretary.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame secretary = new SecMenuUI();
        secretary.setVisible(true);
      }
    });

    btnDentist = new JButton("Dentist");
    btnDentist.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame registerFrame = new Partners(0);
        registerFrame.setVisible(true);
      }
    });

    btnHygienist = new JButton("Hygienist");
    btnHygienist.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        JFrame registerFrame = new Partners(1);
        registerFrame.setVisible(true);
      }
    });

    btnExit = new JButton("Exit");
    btnExit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });

    JPanel exitPanel = new JPanel();
    exitPanel.add(btnExit);

    JPanel selectOne = new JPanel();
    selectOne.setLayout(new GridLayout(3,0));
    selectOne.add(btnSecretary);
    selectOne.add(btnDentist);
    selectOne.add(btnHygienist);

    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(selectOne, BorderLayout.CENTER);
    contentPane.add(exitPanel, BorderLayout.SOUTH);

  }

  public static void main(String[] args) {
          StaffUI staffUI = new StaffUI();
          staffUI.setVisible(true);
  }
}
