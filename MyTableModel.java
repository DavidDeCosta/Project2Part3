import java.util.Date;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel
{
    //======================================DATA MEMBERS ===================================================================
    MyListModel listmodel;

    //=====================================CONSTRUCTOR =====================================================================

    MyTableModel()
    {
        listmodel = new MyListModel();
    }

    //====================================  METHODS ===========================================================================

    @Override
    public int getRowCount() 
    {
        
        return listmodel.getSize();
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
        record = listmodel.getElementAt(rowIndex);

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

   //     return getValueAt(0, c).getClass();
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
        record = (TripRecord)listmodel.elementAt(row);

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
    
}
