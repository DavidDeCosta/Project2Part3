import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import java.awt.event.*;
import java.util.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.awt.*;

public class MyDialog extends JDialog
                        implements ActionListener
{
//==================================DATA MEMBERS ====================================================
    JComponent panel;
    GroupLayout groupLayout;

    JPanel panelForSubmitCancel;

    JLabel dateLabel;
    JLabel nameLabel;
    JLabel serviceCodeLabel;
    JLabel initalMileageLabel;
    JLabel mileageOnReturnLabel;
    JLabel billingRateLabel;
    JLabel commentsLabel;

    JTextField dateTF;
    JTextField nameTF;
    JTextField initialMileageTF;
    JTextField mileageOnReturnTF;
    JTextField billingRateTF;
    JTextField commentsTF;

    JButton submitButton;
    JButton cancelButton;

    BillingVerifier billingVerifier;

    String []serviceCodeOptions = { "", "A0428", "A0429", "A0427", "A0434"};
    JComboBox comboBox;

    TripRecord record;
    String name;
    long date;
    int initialMileage;
    int mileageOnReturn;
    String serviceCode;
    String comments;
    double billingRate;

    DataManager dataManager;
    MyListModel justAListModel;

    
    SimpleDateFormat sdf;
    ParsePosition pos;

    int index;
    String stringDate;

    MyTableModel tabelModel;

//==================================CONSTRUCTORS ===================================================

    MyDialog(DataManager manager)
    {
        this.dataManager = manager;
        buildGUI("Submit");

        add(panel);
        setLocationRelativeTo(null);   //centers the JDialog
        setSize(400,400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setVisible(true);
    }

    MyDialog(DataManager manager, TripRecord record, int index)
    {
        this.dataManager = manager;
        this.index = index;
        this.record = record;
        buildGUI("Edit");
        populateFields(record);

        add(panel);
        setLocationRelativeTo(null);   //centers the JDialog
        setSize(400,400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setVisible(true);

    }

//====================================METHODS ===========================================================

    void buildGUI(String actionCommand)
    {

//============================= Instantiating Labels and TextFields ========================================

        dateTF = new JTextField(30);
        dateLabel = new JLabel("Enter the Date: ");
        dateTF.setInputVerifier(new DateVerifier()); // sets the verifier

        nameLabel = new JLabel("Enter name: ");
        nameTF = new JTextField(30);

        serviceCodeLabel = new JLabel("Enter seriveCode: ");
        comboBox = new JComboBox<>(serviceCodeOptions);

        initalMileageLabel = new JLabel("Enter initial Mileage: ");
        initialMileageTF= new JTextField(30);
        initialMileageTF.setInputVerifier(new MileageVerifier());

        mileageOnReturnLabel = new JLabel("Enter return mileage ");
        mileageOnReturnTF = new JTextField(30);
        mileageOnReturnTF.setInputVerifier(new MileageVerifier());

        billingRateLabel = new JLabel("Enter billing rate: ");
        billingRateTF = new JTextField(30);
        billingRateTF.setInputVerifier(new BillingVerifier()); //sets the verifier

        commentsLabel = new JLabel("Enter additional comments: ");
        commentsTF = new JTextField(30);

//===================================Adding buttons ===========================================================
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        submitButton.setActionCommand(actionCommand);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);


        panel = new JPanel();
        groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        panelForSubmitCancel = new JPanel();
        add(panelForSubmitCancel,BorderLayout.SOUTH);


        panelForSubmitCancel.add(submitButton);
        panelForSubmitCancel.add(cancelButton);
        
//=================================Creating horizonal and vertical groups ==========================================

        GroupLayout.SequentialGroup hGroup = groupLayout.createSequentialGroup();
        hGroup.addGroup(groupLayout.createParallelGroup().
        addComponent(dateLabel).addComponent(nameLabel).addComponent(serviceCodeLabel).addComponent(initalMileageLabel)
        .addComponent(mileageOnReturnLabel).addComponent(billingRateLabel).addComponent(commentsLabel));
        hGroup.addGroup(groupLayout.createParallelGroup().
        addComponent(dateTF).addComponent(nameTF).addComponent(comboBox).addComponent(initialMileageTF)
        .addComponent(mileageOnReturnTF).addComponent(billingRateTF).addComponent(commentsTF));
        groupLayout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = groupLayout.createSequentialGroup();
        vGroup.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).
        addComponent(dateLabel).addComponent(dateTF));
        vGroup.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).
        addComponent(nameLabel).addComponent(nameTF));
        vGroup.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).
        addComponent(serviceCodeLabel).addComponent(comboBox));
        vGroup.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).
        addComponent(initalMileageLabel).addComponent(initialMileageTF));
        vGroup.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).
        addComponent(mileageOnReturnLabel).addComponent(mileageOnReturnTF));
        vGroup.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).
        addComponent(billingRateLabel).addComponent(billingRateTF));
        vGroup.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).
        addComponent(commentsLabel).addComponent(commentsTF));
        groupLayout.setVerticalGroup(vGroup);



    }


    void populateFields(TripRecord record)
    {

        dateTF.setText(String.format("%tm/%te/%tY",record.date,record.date,record.date));
        nameTF.setText(record.name);
        comboBox.setSelectedItem(record.serviceCode);
        initialMileageTF.setText(Integer.toString(record.initialMileage));
        mileageOnReturnTF.setText(Integer.toString(record.mileageOnReturn));
        billingRateTF.setText(Double.toString(record.billingRate));
        commentsTF.setText(record.comments);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        
        if(e.getActionCommand().equals("Cancel"))
        {
            this.dispose();
        }
        else if(e.getActionCommand().equals("Submit"))
        {
            handAdd();
        }
        else if(e.getActionCommand().equals("Edit"))
        {
            handleReplace();
        }
    }

    void handleReplace()
    {
        String tempForMileage;
        String tempforBilling;
        String tempDate;

        String tempForInitial = initialMileageTF.getText();
        String tempForReturn = mileageOnReturnTF.getText();

        if(dateTF.getText().trim().equals(""))
        {
            dateTF.requestFocus();
        }
        else if(nameTF.getText().trim().equals(""))
        {
            nameTF.requestFocus();
        }
        else if(comboBox.getSelectedItem().equals(""))
        {
            comboBox.requestFocus();
        }
        else if(initialMileageTF.getText().trim().equals(""))
        {
            initialMileageTF.requestFocus();
        }
        else if(mileageOnReturnTF.getText().trim().equals(""))
        {
            mileageOnReturnTF.requestFocus();
        }
        else if(billingRateTF.getText().trim().equals(""))
        {
            billingRateTF.requestFocus();
        }
        else if(commentsTF.getText().trim().equals(""))
        {
            commentsTF.requestFocus();
        }
        else if(Integer.parseInt(tempForInitial) > Integer.parseInt(tempForReturn))
        {
            initialMileageTF.requestFocus();
        }
        else{

        record = new TripRecord(name, date,serviceCode,initialMileage,mileageOnReturn,billingRate, comments );
        record.name = nameTF.getText();
        record.serviceCode = (String)comboBox.getSelectedItem();
        record.comments = commentsTF.getText();
        tempForMileage = initialMileageTF.getText();
        record.initialMileage = Integer.parseInt(tempForMileage);
        tempForMileage = mileageOnReturnTF.getText();
        record.mileageOnReturn = Integer.parseInt(tempForMileage);
        tempforBilling = billingRateTF.getText();
        record.billingRate = Double.parseDouble(tempforBilling);

        tempDate = dateTF.getText();
        record.date = convertStringToDate(tempDate);
        dataManager.replace(record,index);
        
        dispose();

    }
}

    void handAdd()
    {
        String tempForMileage;
        String tempforBilling;
        String tempDate;

        String tempForInitial = initialMileageTF.getText();
        String tempForReturn = mileageOnReturnTF.getText();

        if(dateTF.getText().trim().equals(""))
        {
            dateTF.requestFocus();
        }
        else if(nameTF.getText().trim().equals(""))
        {
            nameTF.requestFocus();
        }
        else if(comboBox.getSelectedItem().equals(""))
        {
            comboBox.requestFocus();
        }
        else if(initialMileageTF.getText().trim().equals(""))
        {
            initialMileageTF.requestFocus();
        }
        else if(mileageOnReturnTF.getText().trim().equals(""))
        {
            mileageOnReturnTF.requestFocus();
        }
        else if(billingRateTF.getText().trim().equals(""))
        {
            billingRateTF.requestFocus();
        }
        else if(commentsTF.getText().trim().equals(""))
        {
            commentsTF.requestFocus();
        }
        else if(Integer.parseInt(tempForInitial) > Integer.parseInt(tempForReturn))
        {
            initialMileageTF.setBackground(Color.RED);
            mileageOnReturnTF.setBackground(Color.RED);
            initialMileageTF.requestFocus();
            JOptionPane.showMessageDialog(this, "Inital mileage was less than mileage return!");
        }
        else{

        record = new TripRecord(name, date,serviceCode,initialMileage,mileageOnReturn,billingRate, comments );
        record.name = nameTF.getText();
        record.serviceCode = (String)comboBox.getSelectedItem();
        record.comments = commentsTF.getText();
        tempForMileage = initialMileageTF.getText();
        record.initialMileage = Integer.parseInt(tempForMileage);
        tempForMileage = mileageOnReturnTF.getText();
        record.mileageOnReturn = Integer.parseInt(tempForMileage);
        tempforBilling = billingRateTF.getText();
        record.billingRate = Double.parseDouble(tempforBilling);

        tempDate = dateTF.getText();
        record.date = convertStringToDate(tempDate);
        dataManager.add(record);
        }
        dispose();
    }

    Date convertStringToDate(String stringOfDate)
    {
        
        Date date = null;
        sdf = new SimpleDateFormat("M/d/y");
        pos = new ParsePosition(0);


        date = (Date)sdf.parse(stringOfDate,pos);
        if(stringOfDate.length() != pos.getIndex())
        {
            dateTF.requestFocus();
        }


        return date;
    }
}
