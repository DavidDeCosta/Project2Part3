import javax.swing.DefaultListModel;
import javax.swing.*;
import java.io.*;

public class MyListModel extends DefaultListModel<TripRecord>
{

    //======================================DATA MEMBERS ========================================================
    int numberOfTripRecords = 0;                             // keeping track of the number of names added
    MyTableModel table;

    //=======================================Constructors ==========================================================
    MyListModel()
    {  
        
    }

/*   MyListModel(DataInputStream dis)
    {
        try 
        {
            numberOfTripRecords = dis.readInt();               //tells us how many names are stored
            for(int n = 0; n < numberOfTripRecords; n++)
            {
                addElement(new TripRecord(dis));               //put the element from a file into the JList
            }
        } 
        catch (IOException e) 
        {
            JOptionPane.showMessageDialog(null, "Could not read file");
        }
        
    }
 */ 
//==========================================Methods // ============================================================


    void store(DataOutputStream dos)
    {
        try 
        {
            dos.writeInt(size());
            for(int n = 0; n < size(); n++)
            {
                elementAt(n).store(dos);
            }
        }
        catch (IOException e)
        {
            System.out.println("Error, could not write the name. ");
        }
    }

}

