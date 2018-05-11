import java.awt.EventQueue;
import javax.swing.*;
import java.awt.Container;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.awt.CardLayout;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.sql.Time;


public class Partners extends javax.swing.JFrame{

	private JFrame frame;
	private GregorianCalendar calendar;
    private javax.swing.JButton day1;
    private javax.swing.JButton day2;
    private javax.swing.JButton day3;
    private javax.swing.JButton day4;
    private javax.swing.JButton day5;
	private Date today;
	private String dateActual;
	private String displayMonth;
	private String weekStart;
	private JLabel weekDate;
	private String displayYear;
	private JLabel yearDate;
	private String weekEnd;


	private JLabel lbl_9am;
	private JLabel lbl_10am;
	private JLabel lbl_11am;
	private JLabel lbl_12pm;
	private JLabel lbl_1pm;
	private JLabel lbl_2pm;
	private JLabel lbl_3pm;
	private JLabel lbl_4pm;

	private JLabel currApp;
	private JLabel totCost;
	private JLabel totOwed;

	private float totalCostOfApp;
	private float totalOwedOfApp;

	private int partnerID;
	private int currPatientID;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Partners window = new Partners(0);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private String staffType;

	/**
	 * Create the application.
	 */
	public Partners(int click) {

        if (click == 0){
            staffType = "Dentist";
        }
        else if(click == 1){
            staffType = "Hygienist";
        }
        else{
        	staffType= "Secretary";
		}

        //Fetching the partnerID, so the correct appointments can be found.
		QueryResult partner= Database.performSearch("partners", "role='" +staffType+"'");;

		partnerID = partner.getRow(partner.lastRow()).getValueAsInt("partnerID");
        if(click != 0 && click != 1){
        	partnerID = -1;
		}


        calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        Date now = new Date();
        calendar.setTime(now);

        DateFormat dateAct = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        today = calendar.getTime();
        dateActual = dateAct.format(calendar.getTime());

        initialize();
        updateNav();
        updateDays();

        calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.MONDAY);
        calendar.setTime(today);

        fillCalendar(calendar.getTime());
		setCurrentAppointment();

    }

    public void setCurrentAppointment(){
		DateFormat dateF = new SimpleDateFormat("YYYYMMdd");
		String cond = "date=CURDATE() AND ";
		String time = "startTime<CURTIME() AND endTime>CURTIME()";
		QueryResult res = Database.performSearch("appointments", cond + time);
		if(res.size() > 0) {
			currPatientID = res.get(res.lastRow()).getValueAsInt("patientID");

			QueryResult patRes= Database.performSearch("patients", "patientID="+currPatientID);

			Row patient = patRes.getRow(res.lastRow());
			Row app = res.get(res.lastRow());
			currApp.setText("Current Appointment");
			String pTitle = patient.getValueAsString("title");
			String psn = patient.getValueAsString("surname");
			String treatment = app.getValueAsString("treatment");
			String stStr = ((Time)app.getValue("startTime")).toString();
			String appDetails = treatment + ", " + pTitle + " " + psn;

			currApp.setText(appDetails);
		}else{
			currPatientID = -1;
		}


	}
	
	public void updateNav(){
        DateFormat dFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.MONDAY);
        displayMonth = dFormat.format(calendar.getTime());

        DateFormat dFormat2 = new SimpleDateFormat("dd", Locale.getDefault());
        weekStart = dFormat2.format(calendar.getTime());
        calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.FRIDAY);
        weekEnd = dFormat2.format(calendar.getTime());
        weekDate.setText(displayMonth + "  Date: " + weekStart + " - " + weekEnd);


        DateFormat yFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        displayYear = yFormat.format(calendar.getTime());
        yearDate.setText(displayYear);
    }

    public void updateDays(){
        DateFormat dFormat = new SimpleDateFormat("dd", Locale.getDefault());
        calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.MONDAY);
        day1.setText("Mon - " + dFormat.format(calendar.getTime()));
		calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.TUESDAY);
        day2.setText("Tues - " + dFormat.format(calendar.getTime()));
        calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.WEDNESDAY);
        day3.setText("Wed - " + dFormat.format(calendar.getTime()));
        calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.THURSDAY);
        day4.setText("Thurs - " + dFormat.format(calendar.getTime()));
        calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.FRIDAY);
        day5.setText("Fri - " + dFormat.format(calendar.getTime()));
    }

    public void fillCalendar(Date date){

    	String noAppStr = "";
		lbl_9am.setText(noAppStr);
		lbl_10am.setText(noAppStr);
		lbl_11am.setText(noAppStr);
		lbl_12pm.setText(noAppStr);
		lbl_1pm.setText(noAppStr);
		lbl_2pm.setText(noAppStr);
		lbl_3pm.setText(noAppStr);
		lbl_4pm.setText(noAppStr);

    	DateFormat dFormat = new SimpleDateFormat("YYYYMMdd", Locale.getDefault());
		String dateStr = dFormat.format(calendar.getTime());

		String[] tables = new String[] {"appointments", "patients"};
		String[] columns = new String[]{"patientID"};
    	String cond = "date='" + dateStr + "'" ;
    	if(partnerID >= 0){
    		cond +=  " AND partnerID="+partnerID ;
		}
    	QueryResult bookedApps = Database.performSearch(tables, "LEFT OUTER", columns, cond);
    	for(Row r : bookedApps){
			String pTitle = r.getValueAsString("title");
			String psn = r.getValueAsString("surname");
			String treatment = r.getValueAsString("treatment");
			String stStr = ((Time)r.getValue("startTime")).toString();
			int hr = Integer.parseInt(stStr.substring(0, 2));
			String appDetails;
			if(r.getValueAsString("forename") != null) {
				appDetails = stStr + " : " + treatment + ", " + pTitle + " " + psn;
			}
			else {
				appDetails = "HOLIDAY";
			}

			if(hr == 9){
				lbl_9am.setText(lbl_9am.getText() + appDetails + "; ");
			}else if(hr == 10){
				lbl_10am.setText(lbl_10am.getText() + appDetails + "; ");
			}
			else if(hr == 11){
				lbl_11am.setText(lbl_11am.getText() + appDetails + "; ");
			}
			else if(hr == 12){
				lbl_12pm.setText(lbl_12pm.getText() + appDetails + "; ");
			}
			else if(hr == 13){
				lbl_1pm.setText(lbl_1pm.getText() + appDetails + "; ");
			}
			else if(hr == 14){
				lbl_2pm.setText(lbl_2pm.getText() + appDetails + "; ");
			}
			else if(hr == 15){
				lbl_3pm.setText(lbl_3pm.getText() + appDetails + "; ");
			}
			else if(hr == 16){
				lbl_4pm.setText(lbl_4pm.getText() + appDetails + "; ");
			}
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
    @SuppressWarnings("unchecked")
	private void initialize() {
		frame = this;//new JFrame();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenDimensions = toolkit.getScreenSize();
		setSize(screenDimensions.width, screenDimensions.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		frame.getContentPane().add(panel);
		
		JPanel panel_19 = new JPanel();
		panel_19.setBounds(0, 97, 746, 52);
		panel_19.setBackground(Color.ORANGE);
		
		JButton nextWeek = new JButton("Next Week");
		nextWeek.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.MONDAY);
		        calendar.add(GregorianCalendar.DATE, +7);
		        updateNav();
		        updateDays();
				fillCalendar(calendar.getTime());
			}
		});
		nextWeek.setForeground(Color.BLUE);
		
		JButton lastWeek = new JButton("Last Week");
		lastWeek.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.MONDAY);
		        calendar.add(GregorianCalendar.DATE, -7);
		        updateNav();
		        updateDays();
		        fillCalendar(calendar.getTime());
			}
		});
		lastWeek.setForeground(Color.BLUE);
		
		
		weekDate = new JLabel();
		weekDate.setText("jLabel1");
		weekDate.setHorizontalAlignment(SwingConstants.CENTER);
		weekDate.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		yearDate = new JLabel();
		yearDate.setText("jLabel1");
		yearDate.setHorizontalAlignment(SwingConstants.CENTER);
		yearDate.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GroupLayout gl_panel_19 = new GroupLayout(panel_19);
		gl_panel_19.setHorizontalGroup(
			gl_panel_19.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_19.createSequentialGroup()
					.addContainerGap(68, Short.MAX_VALUE)
					.addComponent(weekDate, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(yearDate, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
					.addGap(35)
					.addComponent(lastWeek)
					.addGap(18)
					.addComponent(nextWeek)
					.addGap(49))
		);
		gl_panel_19.setVerticalGroup(
			gl_panel_19.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_19.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_19.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_19.createSequentialGroup()
							.addGap(6)
							.addComponent(weekDate, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
						.addComponent(yearDate, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_19.createParallelGroup(Alignment.BASELINE)
							.addComponent(lastWeek)
							.addComponent(nextWeek)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_19.setLayout(gl_panel_19);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 200, 746, 539);
		panel_1.setBackground(Color.LIGHT_GRAY);
		
		JLabel label_1 = new JLabel();
		label_1.setBounds(14, 18, 48, 33);
		label_1.setText("9:00");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel label = new JLabel();
		label.setBounds(14, 75, 48, 33);
		label.setText("10:00");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel label_2 = new JLabel();
		label_2.setBounds(14, 361, 48, 33);
		label_2.setText("15:00");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel label_3 = new JLabel();
		label_3.setBounds(14, 133, 48, 33);
		label_3.setText("11:00");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel label_4 = new JLabel();
		label_4.setBounds(14, 192, 48, 33);
		label_4.setText("12:00");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel label_5 = new JLabel();
		label_5.setBounds(14, 309, 48, 33);
		label_5.setText("14:00");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel label_6 = new JLabel();
		label_6.setBounds(14, 251, 48, 33);
		label_6.setText("13:00");
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel label_7 = new JLabel();
		label_7.setBounds(14, 407, 48, 33);
		label_7.setText("16:00");
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		panel.setLayout(null);
		panel.add(panel_19);
		panel.add(panel_1);
		panel_1.setLayout(null);
		panel_1.add(label_1);
		panel_1.add(label_6);
		panel_1.add(label_3);
		panel_1.add(label);
		panel_1.add(label_4);
		panel_1.add(label_5);
		panel_1.add(label_2);
		panel_1.add(label_7);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.WHITE);
		panel_4.setBounds(76, 18, 652, 39);
		panel_1.add(panel_4);
		panel_4.setLayout(null);
		
		lbl_9am = new JLabel("9am");
		lbl_9am.setBounds(14, 13, 652, 18);
		panel_4.add(lbl_9am);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(Color.WHITE);
		panel_5.setLayout(null);
		panel_5.setBounds(76, 75, 652, 39);
		panel_1.add(panel_5);

		lbl_10am = new JLabel("10am");
		lbl_10am .setBounds(14, 13, 652, 18);
		panel_5.add(lbl_10am );
		
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(Color.WHITE);
		panel_6.setLayout(null);
		panel_6.setBounds(76, 127, 652, 39);
		panel_1.add(panel_6);

		lbl_11am  = new JLabel("11am");
		lbl_11am.setBounds(14, 13, 652, 18);
		panel_6.add(lbl_11am);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBackground(Color.WHITE);
		panel_7.setLayout(null);
		panel_7.setBounds(76, 186, 652, 39);
		panel_1.add(panel_7);

		lbl_12pm  = new JLabel("12pm");
		lbl_12pm.setBounds(14, 13, 652, 18);
		panel_7.add(lbl_12pm);
		
		JPanel panel_8 = new JPanel();
		panel_8.setBackground(Color.WHITE);
		panel_8.setLayout(null);
		panel_8.setBounds(76, 245, 652, 39);
		panel_1.add(panel_8);

		lbl_1pm = new JLabel("13pm");
		lbl_1pm.setBounds(14, 13, 652, 18);
		panel_8.add(lbl_1pm);
		
		JPanel panel_9 = new JPanel();
		panel_9.setBackground(Color.WHITE);
		panel_9.setLayout(null);
		panel_9.setBounds(76, 303, 652, 39);
		panel_1.add(panel_9);

		lbl_2pm = new JLabel("14pm");
		lbl_2pm.setBounds(14, 13, 652, 18);
		panel_9.add(lbl_2pm);
		
		JPanel panel_10 = new JPanel();
		panel_10.setBackground(Color.WHITE);
		panel_10.setLayout(null);
		panel_10.setBounds(76, 355, 652, 39);
		panel_1.add(panel_10);

		lbl_3pm = new JLabel("15pm");
		lbl_3pm.setBounds(14, 13, 652, 18);
		panel_10.add(lbl_3pm);
		
		JPanel panel_11 = new JPanel();
		panel_11.setBackground(Color.WHITE);
		panel_11.setLayout(null);
		panel_11.setBounds(76, 407, 652, 39);
		panel_1.add(panel_11);

		lbl_4pm = new JLabel("16pm");
		lbl_4pm.setBounds(14, 13, 652, 18);
		panel_11.add(lbl_4pm);
		
		JLabel staffWelcome = new JLabel("Welcome " + staffType);
		staffWelcome.setForeground(Color.BLACK);
		staffWelcome.setBackground(Color.DARK_GRAY);
		staffWelcome.setFont(new Font("Arial", Font.PLAIN, 40));
		staffWelcome.setBounds(153, 26, 379, 43);
		panel.add(staffWelcome);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(747, 97, 286, 642);
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 1));



		JPanel appInfoPanel = new JPanel();
		appInfoPanel.setLayout(new GridLayout(0, 1));
		currApp = new JLabel("Current Appointment: none");
		totCost = new JLabel("Total Cost: £0.00");
		totOwed = new JLabel("Total Owed: £0.00");
		appInfoPanel.add(currApp);
		appInfoPanel.add(totCost);
		appInfoPanel.add(totOwed);
		panel_2.add(appInfoPanel);
		if(staffType == "Secretary") appInfoPanel.setVisible(false);
		
		JLabel lblNewLabel = new JLabel("Treatment Type");
		lblNewLabel.setBounds(54, 203, 131, 19);
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 17));
		panel_2.add(lblNewLabel);
		if(staffType == "Secretary") lblNewLabel.setVisible(false);
		
		JToggleButton tglbtnNewToggleButton = new JToggleButton("Check-Up/ \u00A345 ");
		tglbtnNewToggleButton.setBounds(10, 235, 195, 34);
		panel_2.add(tglbtnNewToggleButton);

		
		JToggleButton tglbtnNewToggleButton_1 = new JToggleButton("Hygiene Visit/\u00A345 ");
		tglbtnNewToggleButton_1.setBounds(10, 286, 195, 34);
		panel_2.add(tglbtnNewToggleButton_1);
		
		JToggleButton tglbtnNewToggleButton_2 = new JToggleButton("Silver amalgam filling/ \u00A390 ");
		tglbtnNewToggleButton_2.setBounds(10, 331, 195, 34);
		panel_2.add(tglbtnNewToggleButton_2);
		
		JToggleButton tglbtnNewToggleButton_3 = new JToggleButton("White composite resin/\u00A3150");
		tglbtnNewToggleButton_3.setBounds(10, 376, 195, 41);
		panel_2.add(tglbtnNewToggleButton_3);
		
		JToggleButton tglbtnNewToggleButton_4 = new JToggleButton("Fitting a gold crow/\u00A3500 ");
		tglbtnNewToggleButton_4.setBounds(10, 428, 195, 41);
		panel_2.add(tglbtnNewToggleButton_4);

		if(staffType == "Secretary") tglbtnNewToggleButton.setVisible(false);
		if(staffType == "Secretary") tglbtnNewToggleButton_1.setVisible(false);
		if(staffType == "Secretary") tglbtnNewToggleButton_2.setVisible(false);
		if(staffType == "Secretary") tglbtnNewToggleButton_3.setVisible(false);
		if(staffType == "Secretary") tglbtnNewToggleButton_4.setVisible(false);

		JButton btnNewButton_1 = new JButton("Submit");
		btnNewButton_1.setBounds(105, 480, 100, 45);
		btnNewButton_1.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_1);
		if(staffType == "Secretary") btnNewButton_1.setVisible(false);


		//SUBMIT
		btnNewButton_1.addActionListener(new ActionListener() {
			 @Override
			 public void actionPerformed(ActionEvent e) {

			 	if(currPatientID == -1) return;
				Row patient = Database.performSearch("patients", "patientID="+currPatientID).get(0);
				QueryResult planRes = Database.performSearch("healthcareplans", "planName='" + patient.getValueAsString("healthCarePlan")+ "'");
				Row plan = null;
				if(planRes.size() > 0){
					plan = planRes.get(0);
				}

				int checkups = patient.getValueAsInt("checkUpsThisYear");
				int hygienes = patient.getValueAsInt("HygieneVisitsThisYear");
				int repairs = patient.getValueAsInt("repairsThisYear");
				float cost = 0.0f;
				float owed = 0.0f;

				//SUBMIT BUTTON
				if (tglbtnNewToggleButton.isSelected()){
					checkups++;
					QueryResult res = Database.performSearch("SELECT cost FROM treatments WHERE treatmentName='check-up'");
					float c = res.get(0).getValueAsFloat("cost");
					cost += c;
					if(plan != null) {
						if (checkups > plan.getValueAsInt("checkUps")) {
							owed += c;
						}
					}
				}
				if(tglbtnNewToggleButton_1.isSelected()){
					hygienes++;
					QueryResult res = Database.performSearch("SELECT cost FROM treatments WHERE treatmentName='hygiene'");
					float c = res.get(0).getValueAsFloat("cost");
					cost += c;
					if(plan != null) {
						if (hygienes > plan.getValueAsInt("hygieneVisits")) {
							owed += c;
						}
					}
				}
				if(tglbtnNewToggleButton_2.isSelected()){
					repairs++;
					QueryResult res = Database.performSearch("SELECT cost FROM treatments WHERE treatmentName='amalgam filling'");
					float c = res.get(0).getValueAsFloat("cost");
					cost += c;
					if(plan != null) {
						if (repairs > plan.getValueAsInt("repairs")) {
							owed += c;
						}
					}
				}
				if(tglbtnNewToggleButton_3.isSelected()){
					repairs++;
					QueryResult res = Database.performSearch("SELECT cost FROM treatments WHERE treatmentName='white comp resin filling'");
					float c = res.get(0).getValueAsFloat("cost");
					cost += c;
					if(plan != null) {
						if (repairs > plan.getValueAsInt("repairs")) {
							owed += c;
						}
					}
				}
				if(tglbtnNewToggleButton_4.isSelected()){
					repairs++;
					QueryResult res = Database.performSearch("SELECT cost FROM treatments WHERE treatmentName='gold crown fitting'");
					float c = res.get(0).getValueAsFloat("cost");
					cost += c;
					if(plan != null) {
						if (repairs > plan.getValueAsInt("repairs")) {
							owed += c;
						}
					}
				}
				if(plan == null){
					owed = cost;
				}

				totalCostOfApp = cost;
				totalOwedOfApp = owed;

				 totCost.setText("Total Cost: £" + cost);
				 totOwed.setText("Total Owed: £" + owed);


			 }
		 });


		JButton btnNewButton_2 = new JButton("Visit Finish");
		btnNewButton_2.setBounds(105, 557, 100, 39);
		btnNewButton_2.setBackground(Color.RED);
		panel_2.add(btnNewButton_2);
		if(staffType == "Secretary") btnNewButton_2.setVisible(false);

		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currPatientID == -1) return;
				Row patient = Database.performSearch("patients", "patientID="+currPatientID).get(0);
				Patient pObj = new Patient(patient);
				if (tglbtnNewToggleButton.isSelected()){ pObj.setCheckUpsThisYear(pObj.getCheckUpsThisYear()+1); }
				if (tglbtnNewToggleButton_1.isSelected()){ pObj.setHygeineVisitsThisYear(pObj.getHygeineVisitsThisYear()+1); }
				if (tglbtnNewToggleButton_2.isSelected()){ pObj.setRepairsThisYear(pObj.getRepairsThisYear()+1); }
				if (tglbtnNewToggleButton_3.isSelected()){ pObj.setRepairsThisYear(pObj.getRepairsThisYear()+1); }
				if (tglbtnNewToggleButton_4.isSelected()){ pObj.setRepairsThisYear(pObj.getRepairsThisYear()+1); }
				pObj.setTotalOwed(totalOwedOfApp);

				Database.performUpdate(pObj);

				totalOwedOfApp = 0.0f;
				totalCostOfApp = 0.0f;

			}
		});

		JButton backBtn = new JButton("Back");
		btnNewButton_2.setBounds(105, 557, 100, 39);
		panel_2.add(backBtn);
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				JFrame menu = new StaffUI();
				menu.setVisible(true);
			}
		});


		JPanel panel_3 = new JPanel();
		panel_3.setBounds(0, 60, 278, 130);
		panel_2.add(panel_3);
		panel_3.setLayout(null);

		/*JLabel lblNewLabel_1 = new JLabel("Health Plan Paid");
		lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 17));
		lblNewLabel_1.setBounds(14, 49, 144, 30);
		panel_3.add(lblNewLabel_1);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"None", "NHS Free Plan/ U18 Free", "Maintenance Plan/ \u00A315 ", "Oral Health Plan/ \u00A321", "Dental Repair Plan/ \u00A336"}));
		comboBox.setToolTipText("");
		comboBox.setBounds(14, 81, 229, 36);
		panel_3.add(comboBox);*/



		JPanel panel_12 = new JPanel();
		panel_12.setBackground(Color.ORANGE);
		panel_12.setBounds(0, 149, 746, 52);
		panel.add(panel_12);
		
		day1 = new JButton("jButton1");
		day1.setForeground(Color.BLACK);
		day1.setBackground(Color.YELLOW);
		
		day2 = new JButton("jButton1");
		day2.setForeground(Color.BLACK);
		day2.setBackground(Color.YELLOW);
		
		day3 = new JButton("jButton1");
		day3.setForeground(Color.BLACK);
		day3.setBackground(Color.YELLOW);
		
		day4 = new JButton("jButton1");
		day4.setForeground(Color.BLACK);
		day4.setBackground(Color.YELLOW);
		
		day5 = new JButton("jButton1");
		day5.setForeground(Color.BLACK);
		day5.setBackground(Color.YELLOW);
		GroupLayout gl_panel_12 = new GroupLayout(panel_12);
		gl_panel_12.setHorizontalGroup(
			gl_panel_12.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_12.createSequentialGroup()
					.addContainerGap()
					.addComponent(day1, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
					.addGap(40)
					.addComponent(day2, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
					.addGap(36)
					.addComponent(day3, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
					.addGap(30)
					.addComponent(day4, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(day5, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(25, Short.MAX_VALUE))
		);
		gl_panel_12.setVerticalGroup(
			gl_panel_12.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_12.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_12.createParallelGroup(Alignment.BASELINE)
						.addComponent(day1)
						.addComponent(day2)
						.addComponent(day3)
						.addComponent(day4)
						.addComponent(day5))
					.addContainerGap(51, Short.MAX_VALUE))
		);
		panel_12.setLayout(gl_panel_12);


		//Selecting Days
		day1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.MONDAY);
				fillCalendar(calendar.getTime());
			}
		});

		day2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.TUESDAY);
				fillCalendar(calendar.getTime());
			}
		});

		day3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.WEDNESDAY);
				fillCalendar(calendar.getTime());
			}
		});
		day4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.THURSDAY);
				fillCalendar(calendar.getTime());
			}
		});

		day5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calendar.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.FRIDAY);
				fillCalendar(calendar.getTime());
			}
		});



	}
}
