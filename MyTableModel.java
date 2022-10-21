import java.io.DataInputStream;
import java.io.IOException;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel
                            implements DataManager
{
    //======================================DATA MEMBERS ===================================================================
    MyListModel listModel;

    //=====================================CONSTRUCTOR =====================================================================

    MyTableModel(MyListModel listModel)
    {
        this.listModel = listModel;
    }

    //====================================  METHODS ===========================================================================

    @Override
    public int getRowCount() 
    {
        
        return listModel.getSize();
    }

    @Override
    public int getColumnCount() 
    {
        
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        
        TripRecord record;
        record = listModel.getElementAt(rowIndex);

        if(columnIndex == 0)
        {
            return record.date;
        }
        else if(columnIndex == 1)
        {
            return record.name;
        }
        else if(columnIndex ==2)
        {
            return record.serviceCode;
        }
        else if(columnIndex == 3)
        {
            return record.initialMileage;
        }
        else if(columnIndex ==4)
        {
            return record.mileageOnReturn;
        }
        else if(columnIndex == 5)
        {
            return record.billingRate;
        }
        else if(columnIndex == 6)
        {
            return record.comments;
        }
        else
        {
            System.out.println("Unexpected error \n");
            System.exit(1);
        }

        return null;
    }

    @Override
    public Class getColumnClass(int c)
    {

        if(c == 0)
        {
            return Date.class;
        }
        else if(c == 1)
        {
            return String.class;
        }
        else if(c == 2)
        {
            return String.class;
        }
        else if(c == 3)
        {
            return Integer.class;
        }
        else if(c == 4)
        {
            return Integer.class;
        }
        else if(c == 5)
        {
            return Double.class;
        }
        else if(c == 6)
        {
            return String.class;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col)
    {
        TripRecord record;
        record = (TripRecord)listModel.elementAt(row);

        if(col == 0)
        {
            record.date = ((Date)value);
        }
        else if(col == 1)
        {
            record.name = ((String)value);
        }
        else if(col == 2)
        {
            record.serviceCode = ((String)value);
        }
        else if(col == 3)
        {
            record.initialMileage = ((Integer)value);
        }
        else if(col == 4)
        {
            record.mileageOnReturn = ((Integer)value);
        }
        else if(col == 5)
        {
            record.billingRate = ((Double)value);
        }
        else if(col == 6)
        {
            record.comments = ((String)value);
        }
        else
        {
            System.out.println("Unexpected problem! \n");
        }
        fireTableCellUpdated(row, col);
    }

    void loadFromFile(DataInputStream dis)
    {
        try 
        {
            listModel.numberOfTripRecords = dis.readInt();               //tells us how many names are stored
            for(int n = 0; n < listModel.numberOfTripRecords; n++)
            {
                addElement(new TripRecord(dis));               //put the element from a file into the JList
            }
        } 
        catch (IOException e) 
        {
            JOptionPane.showMessageDialog(null, "Could not read file");
        }
        
    }

    void addElement(TripRecord record)
    {
        listModel.addElement(record);
        fireTableDataChanged();
    }

    void deleteElement(int rowsToDelete[],JTable table)
    {
        for(int n  = rowsToDelete.length-1; n >= 0; n--)
        {
            listModel.removeElementAt(table.convertRowIndexToModel(rowsToDelete[n]));
            listModel.numberOfTripRecords -= 1;
            System.out.println("There are " + listModel.numberOfTripRecords + " record in the list");
            fireTableDataChanged();
        }
    }

    void replaceElement(TripRecord rec, int index)
    {
        listModel.setElementAt(rec, index);
        fireTableDataChanged();
    }

    @Override
    public void add(TripRecord rec) 
    {
        addElement(rec);
        
    }

    @Override
    public void replace(TripRecord rec, int index) 
    {
//        listModel.setElementAt(rec, index);
        replaceElement(rec, index);
       // System.out.println("I am trying to replace at index: " + index);
    }
    
}
