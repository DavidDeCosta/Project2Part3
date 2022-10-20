import javax.swing.*;                           //for JButton
import javax.swing.event.*;                    //for ChangeListener
import javax.swing.table.*;
import java.awt.event.*;                       // for ActionListener
import java.io.*;
import java.awt.*;                             // for Dimension and Toolkit
import java.util.Date;

public class Myframe extends JFrame
                        implements ActionListener, ListSelectionListener, MouseInputListener
{
    //=======================================DATA MEMBERS =====================================================

    Dimension screenSize;
    Toolkit toolkit;
    MyListModel justAListModel;
    TripRecord record;

    JMenuBar menuBar;
    JMenu theMenuOnTheBar;
    JMenuItem loadFromMenu;
    JMenuItem saveFromMenu;
    JMenuItem addFromMenu;
    JMenuItem deleteFromMenu;
    JMenuItem saveAsFromMenu;
    JMenuItem clear;

    JPanel southPanel;
    JPanel centerPanel;

    JButton load;
    JButton save;
    JButton saveAs;
    JButton add;
    JButton delete;
    JButton add_random;
    JButton exit;
    JButton edit;
    

    JDialog userImputField;

    JTextField date;
    JTextField enterName;
    JTextField enterServiceCode;
    JTextField initialMileage;
    JTextField mileageOnReturn;
    JTextField billingRate;
    JTextField comments;

    Date myDate;

    String userName;

    JFileChooser theFileChooser;                  

    JList<TripRecord> displayList;                           // displays their names
    JScrollPane tripScrollPane;

    MyDialog dialog;

    int index = 0;

    MyTableModel tableModel;
    JTable table;



    //======================================CONSTRUCTOR =======================================================
    Myframe()
    {
        theFileChooser = new JFileChooser(".");   //opens current working directory

        addComponents();
        buildMainFrame();
    }


//=========================================METHODS ==================================================================


    void addComponents()
    {
        
        //======================= setting up the JTable to view //===================================================


        tableModel = new MyTableModel();
        table = new JTable(tableModel);
        table.setFont(new Font("Courtier New", Font.BOLD, 14));
        table.setMinimumSize(new Dimension(400,250));
        table.setColumnModel(getColumnModel());
        tableModel.addTableModelListener(table);
        tripScrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(400,250));
        table.setRowSorter(new TableRowSorter<>(tableModel));
        add(tripScrollPane, BorderLayout.CENTER);

        
        
        //======================= setting up south Panel for buttons //===============================================
        southPanel = new JPanel();
        southPanel.setBackground(Color.GRAY);
        southPanel.setPreferredSize(new Dimension(100,30));
        add(southPanel, BorderLayout.SOUTH);

        load = new JButton("load");
        load.addActionListener(this);
        southPanel.add(load);

        save = new JButton("save");
        save.addActionListener(this);
        southPanel.add(save);

        saveAs = new JButton("saveAs");
        southPanel.add(saveAs);
        saveAs.setToolTipText("alt + s");
        saveAs.setMnemonic('s');             //press alt + d to delete
        saveAs.addActionListener(this);

        add = new JButton("add");
        add.addActionListener(this);
        add.setToolTipText("alt+a , add name");
        add.setMnemonic('A');                //press alt + a to add
        southPanel.add(add);

        edit = new JButton("edit");
        edit.addActionListener(this);
        edit.setEnabled(false);
        southPanel.add(edit);

        delete = new JButton("delete");
        delete.addActionListener(this);
        delete.setToolTipText("alt + d, to delete");
        delete.setMnemonic('d');             //press alt + d to delete
        southPanel.add(delete);

        add_random = new JButton("add_random");
        add_random.addActionListener(this);
        southPanel.add(add_random);

        exit = new JButton("exit");
        exit.addActionListener(this);
        southPanel.add(exit);


        //===================================setting up the JMenu Bar  //============================================
        menuBar = new JMenuBar();
        add(menuBar,BorderLayout.NORTH);

        theMenuOnTheBar = new JMenu("File");
        menuBar.add(theMenuOnTheBar);

        loadFromMenu = new JMenuItem("load");
        loadFromMenu.addActionListener(this);
        theMenuOnTheBar.add(loadFromMenu);

        saveFromMenu = new JMenuItem("save");
        saveFromMenu.addActionListener(this);
        theMenuOnTheBar.add(saveFromMenu);

        deleteFromMenu = new JMenuItem("delete");
        deleteFromMenu.addActionListener(this);
        theMenuOnTheBar.add(deleteFromMenu);

        addFromMenu = new JMenuItem("add");
        theMenuOnTheBar.add(addFromMenu);
        addFromMenu.addActionListener(this);

        saveAsFromMenu = new JMenuItem("saveAs");
        theMenuOnTheBar.add(saveAsFromMenu);
        saveAsFromMenu.addActionListener(this);

        clear = new JMenuItem("clear");
        theMenuOnTheBar.add(clear);
        clear.addActionListener(this);

        table.addMouseListener(this);
 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getActionCommand().equals("exit"))
        {
            this.dispose();
        }
        else if(e.getActionCommand().equals("add"))
        {
            handleAdd();
        }
        else if(e.getActionCommand().equals("edit"))
        {
            handleEdit();
        }
        else if(e.getActionCommand().equals("saveAs"))
        {
            handleSaveAs();
        }
        else if(e.getActionCommand().equals("save"))
        {
            handleSave();
        }
        else if(e.getActionCommand().equals("delete"))
        {
            handleDelete();
        }
        else if(e.getActionCommand().equals("load"))
        {
            handleLoad();
        }
        else if(e.getActionCommand().equals("clear"))
        {
            justAListModel.clear();
            justAListModel.numberOfTripRecords = 0;                    //sets the list to empty
        }
        else if(e.getActionCommand().equals("add_random"))
        {
            handleadd_random();
        }
        
    }

    void handleAdd()
    {
    

        dialog = new MyDialog(justAListModel);

    }

    void handleEdit()
    {


        index = displayList.getSelectedIndex();
        record = justAListModel.elementAt(index);
        dialog = new MyDialog(justAListModel, record, index);
    }

    void handleDelete()
    {
        int index[];
        index = displayList.getSelectedIndices();
        for(int n  = index.length-1; n >= 0; n--)
        {
            justAListModel.removeElementAt(index[n]);
            justAListModel.numberOfTripRecords -= 1;
            System.out.println("There are " + justAListModel.numberOfTripRecords + " record in the list");
        }
    }

    void handleSaveAs()
    {
        int savedOrNot;                                               // did they cancel or save?
        File theFileTheUserChooses;                                   // file for what they typed in or picked?
        File theFileTheUserChooses2;
        DataOutputStream dos;                                          // will pass the File to the dos

        savedOrNot = theFileChooser.showSaveDialog(null);     // if returns 0 they saved file if 1 they exited
        if(savedOrNot == JFileChooser.APPROVE_OPTION)
        {
            theFileTheUserChooses = theFileChooser.getSelectedFile();           //grabs the file the user types or selected
            if(theFileTheUserChooses.exists())
            {
                
                int n = JOptionPane.showConfirmDialog(this, "Do you want to Overwrite the file? ", "Confirm Overwrite", JOptionPane.YES_NO_OPTION);
                if(n == JOptionPane.YES_OPTION)
                {
                try 
                {
                    theFileTheUserChooses = theFileChooser.getSelectedFile();           //grabs the file the user types or selected
                    dos = new DataOutputStream(new FileOutputStream(theFileTheUserChooses));     //form a dos with the file
                    justAListModel.store(dos);                                                   // store it to write to later
                }
                catch (FileNotFoundException e1) 
                {
                    JOptionPane.showMessageDialog(this, "Could not save the file");
                }
                }
                else
                {
                    System.out.println("Do nothing");
                }
            }
            else
            {
                try 
                {
                    theFileTheUserChooses2 = theFileChooser.getSelectedFile();           //grabs the file the user types or selected
                    dos = new DataOutputStream(new FileOutputStream(theFileTheUserChooses));     //form a dos with the file
                    justAListModel.store(dos);                                                   // store it to write to later
                }
                catch (FileNotFoundException e1) 
                {
                    JOptionPane.showMessageDialog(this, "Could not save the file");
                }
            }
        }
    }   


    void handleSave()
    {
        DataOutputStream dos;
        if(theFileChooser.getSelectedFile() == null)
        {
            handleSaveAs();
        }
        else
        {
            try 
            {
                dos = new DataOutputStream(new FileOutputStream(theFileChooser.getSelectedFile()));     //form a dos with the file
                justAListModel.store(dos);                                                   // store it to write to later
            }
            catch (FileNotFoundException e1) 
            {
                JOptionPane.showMessageDialog(this, "Could not save the file");
            }
        }
    }

    void handleLoad()
    {
        DataInputStream dis;
        int fileChooser;
        fileChooser = theFileChooser.showOpenDialog(null);

        if(fileChooser == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                dis = new DataInputStream(new FileInputStream(theFileChooser.getSelectedFile()));
                justAListModel = new MyListModel(dis);
                displayList.setModel(justAListModel);

            }
            catch(FileNotFoundException e)
            {
                JOptionPane.showMessageDialog(this, "Error, could not load");
            }
        }
    }

    void handleadd_random()
    {

        justAListModel.addElement(TripRecord.getRandom()); // adds a random instance of triprecord
    }

    void buildMainFrame()
    {
        toolkit = Toolkit.getDefaultToolkit();                      // used to help get the users screen size
        screenSize = toolkit.getScreenSize();                       //get the users screen size
        setSize(screenSize.width/3, screenSize.height/3);           // makes JFrame 1/3 the users screensize
        setLocationRelativeTo(null);                             // window is placed in the center of screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            //when close frame the program stops
        setTitle("Project 3 Ambulance Trip Record");
        setVisible(true);
    }

    TableColumnModel getColumnModel()
    {

        TableColumn col;
        DefaultTableColumnModel colModel;

        colModel = new DefaultTableColumnModel();

        col = new TableColumn(0);
        col.setPreferredWidth(50);
        col.setMinWidth(50);
        col.setHeaderValue("Date");
        colModel.addColumn(col);

        col = new TableColumn(1);
        col.setPreferredWidth(10);
        col.setMinWidth(20);
        col.setHeaderValue("Name");
        colModel.addColumn(col);

        col = new TableColumn(2);
        col.setPreferredWidth(10);
        col.setMinWidth(20);
        col.setHeaderValue("Service Code");
        colModel.addColumn(col);

        col = new TableColumn(3);
        col.setPreferredWidth(10);
        col.setMinWidth(20);
        col.setHeaderValue("Initial Mileage");
        colModel.addColumn(col);

        col = new TableColumn(4);
        col.setPreferredWidth(10);
        col.setMinWidth(20);
        col.setHeaderValue("Mileage On Return");
        colModel.addColumn(col);

        col = new TableColumn(5);
        col.setPreferredWidth(10);
        col.setMinWidth(20);
        col.setHeaderValue("Billing Rate");
        colModel.addColumn(col);

        col = new TableColumn(6);
        col.setPreferredWidth(10);
        col.setMinWidth(20);
        col.setHeaderValue("Comments");
        colModel.addColumn(col);

        
        return colModel;
        
    }


    @Override
    public void valueChanged(ListSelectionEvent e) 
    {
        
        
    }


    @Override
    public void mouseClicked(MouseEvent e) 
    {
        
        
    }


    @Override
    public void mousePressed(MouseEvent e) 
    {
        if(displayList.getSelectedValue() != null)
        {
            edit.setEnabled(true);
        }
        
    }


    @Override
    public void mouseReleased(MouseEvent e) 
    {
        
        
    }


    @Override
    public void mouseEntered(MouseEvent e) 
    {
        
        
    }


    @Override
    public void mouseExited(MouseEvent e) 
    {
        
        
    }


    @Override
    public void mouseDragged(MouseEvent e) 
    {
        
        
    }


    @Override
    public void mouseMoved(MouseEvent e) 
    {
        
        
    }

}