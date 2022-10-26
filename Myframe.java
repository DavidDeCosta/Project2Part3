import javax.swing.*;                           //for JButton
import javax.swing.event.*;                    //for ChangeListener
import javax.swing.plaf.PopupMenuUI;
import javax.swing.table.*;
import java.awt.event.*;                       // for ActionListener
import java.io.*;
import java.awt.*;                             // for Dimension and Toolkit
import java.util.Date;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

public class Myframe extends JFrame
                        implements ActionListener, ListSelectionListener, MouseInputListener, DropTargetListener
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

//    int index = 0;

    MyTableModel tableModel;
    JTable table;

    DropTarget dropTarget;

    JPopupMenu popUpMenu;
    JMenuItem editFromPopUp;
    JMenuItem deleteFromPopUp;



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

        justAListModel = new MyListModel();
        tableModel = new MyTableModel(justAListModel);
        table = new JTable(tableModel);
        table.setFont(new Font("Courtier New", Font.BOLD, 14));
        table.setMinimumSize(new Dimension(400,250));
        table.setColumnModel(getColumnModel());
        tableModel.addTableModelListener(table);
        tripScrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(400,250));
        table.setRowSorter(new TableRowSorter<>(tableModel));
        tripScrollPane.addMouseListener(this);                 //by adding to tripscrollpane instead of table it doesnt affect the JDialog
        add(tripScrollPane, BorderLayout.CENTER);

        
        
        //======================= setting up south Panel for buttons //===============================================
        southPanel = new JPanel();
        southPanel.setBackground(Color.GRAY);
        southPanel.setPreferredSize(new Dimension(100,30));
        add(southPanel, BorderLayout.SOUTH);

        load = new JButton("load");
        load.addActionListener(this);
        load.setMnemonic('o');
        load.setToolTipText("alt + o");
        southPanel.add(load);

        save = new JButton("save");
        save.addActionListener(this);
        save.setToolTipText("alt + s");
        save.setMnemonic('s');             //press alt + d to delete
        southPanel.add(save);

        saveAs = new JButton("saveAs");
        southPanel.add(saveAs);
        saveAs.setToolTipText("alt + a");
        saveAs.setMnemonic('a');             //press alt + d to delete
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


        popUpMenu = new JPopupMenu();
        editFromPopUp = new JMenuItem("edit");
        editFromPopUp.addActionListener(this);
        deleteFromPopUp = new JMenuItem("delete");
        deleteFromMenu.addActionListener(this);

        popUpMenu.add(editFromPopUp);
        popUpMenu.add(deleteFromMenu);


        dropTarget = new DropTarget(tripScrollPane, this);
 
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
            handleReplace();
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
            tableModel.fireTableDataChanged();
            justAListModel.numberOfTripRecords = 0;                    //sets the list to empty
        }
        else if(e.getActionCommand().equals("add_random"))
        {
            handleadd_random();
            tableModel.unSavedChanges = true;
        }
        else if(e.getActionCommand().equals("deleteFromPopUp"))
        {
            handleDelete();
        }
        else if(e.getActionCommand().equals("editFromPopUp"))
        {
            handleReplace();
        }
        
    }

    void handleAdd()
    {
        dialog = new MyDialog(tableModel);
    }

    void handleReplace()
    {
        int index;
        index = table.getSelectedRow();
        System.out.println(index + "       " +  justAListModel.size() + "\n");
        record = justAListModel.elementAt(index);
        dialog = new MyDialog(tableModel, record, table.convertRowIndexToModel(index));
//        dialog = new MyDialog(tableModel, record, index);
    }

    void handleDelete()
    {
        int rowsToDelete[];
        rowsToDelete = table.getSelectedRows();
        int num = table.getSelectedRow();     // to see what row im deleting
        tableModel.deleteElement(rowsToDelete, table);
        System.out.println("Deleted record at index: " + num );
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
//                justAListModel = new MyListModel(dis);
                tableModel.loadFromFile(dis);
              //  displayList.setModel(justAListModel);

            }
            catch(FileNotFoundException e)
            {
                JOptionPane.showMessageDialog(this, "Error, could not load");
            }
        }
    }

    void handleadd_random()
    {

        tableModel.addElement(TripRecord.getRandom()); // adds a random instance of triprecord
        justAListModel.numberOfTripRecords++;
        System.out.println("number of records are: " + justAListModel.numberOfTripRecords + "\n");

    }

    void buildMainFrame()
    {
        toolkit = Toolkit.getDefaultToolkit();                      // used to help get the users screen size
        screenSize = toolkit.getScreenSize();                       //get the users screen size
        setSize(screenSize.width/2, screenSize.height/2);           // makes JFrame 1/2 the users screensize
        setLocationRelativeTo(null);                             // window is placed in the center of screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            //when close frame the program stops
        setTitle("Project 2 part 3 Ambulance Trip Record");
        setVisible(true);
    }

    TableColumnModel getColumnModel()
    {

        TableColumn col;
        DefaultTableColumnModel colModel;

        colModel = new DefaultTableColumnModel();

        col = new TableColumn(0);
        col.setPreferredWidth(20);
        col.setMinWidth(20);
        col.setHeaderValue("Date");
        colModel.addColumn(col);

        col = new TableColumn(1);
        col.setPreferredWidth(10);
        col.setMinWidth(10);
        col.setHeaderValue("Name");
        colModel.addColumn(col);

        col = new TableColumn(2);
        col.setPreferredWidth(10);
        col.setMinWidth(10);
        col.setHeaderValue("Service Code");
        colModel.addColumn(col);

        col = new TableColumn(3);
        col.setPreferredWidth(10);
        col.setMinWidth(10);
        col.setHeaderValue("Initial Mileage");
        colModel.addColumn(col);

        col = new TableColumn(4);
        col.setPreferredWidth(10);
        col.setMinWidth(10);
        col.setHeaderValue("Mileage On Return");
        colModel.addColumn(col);

        col = new TableColumn(5);
        col.setPreferredWidth(10);
        col.setMinWidth(10);
        col.setHeaderValue("Billing Rate");
        colModel.addColumn(col);

        col = new TableColumn(6);
        col.setPreferredWidth(30);
        col.setMinWidth(30);
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
        if(e.getSource().equals(table))
        {
        JTable source = (JTable)e.getSource();
        int row = source.rowAtPoint( e.getPoint() );
        source.setRowSelectionInterval(row, row);

        if(e.getClickCount() == 2)
        {
            handleReplace();
        }

        if(e.getButton() == 3)
        {
            popUpMenu.show(this,e.getX(),e.getY());
        }
    }
    else
    {

    }
        System.out.println("number = " + e.getButton());
        
    }


    @Override
    public void mousePressed(MouseEvent e) 
    {
        if(table.getSelectedRow() != -1)
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


    @Override
    public void dragEnter(DropTargetDragEvent dtde) 
    {
        
    }


    @Override
    public void dragOver(DropTargetDragEvent dtde) 
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) 
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void dragExit(DropTargetEvent dte) 
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void drop(DropTargetDropEvent dtde) 
    {
        
        java.util.List<File> fileList;
        Transferable transferableData;
        int n;
        DataInputStream dis;

        transferableData = dtde.getTransferable();

        try
        {
            if(transferableData.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
            {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);

                fileList = (java.util.List<File>)(transferableData.getTransferData(DataFlavor.javaFileListFlavor));
                if(fileList.size() == 1)
                {
                    dis = new DataInputStream(new FileInputStream(fileList.get(0)));
                    tableModel.loadFromFile(dis);
                }
                else
                {
                    System.out.println("too many files \n");
                }
            }
            else
            {
                System.out.println("Sorry we don't have this flavor \n");
            }
        }
        catch(UnsupportedFlavorException ufe)
        {
            System.out.println("Unsupported favor found! \n");
            ufe.printStackTrace();
        }
        catch(IOException ioe)
        {
            System.out.println("IOexception found getting transferable data!");
            ioe.printStackTrace();
        }
        
    }

}