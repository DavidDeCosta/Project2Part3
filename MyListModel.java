import javax.swing.DefaultListModel;
import java.io.*;

public class MyListModel extends DefaultListModel<TripRecord>
{
    //======================================DATA MEMBERS ========================================================
    int numberOfTripRecords = 0;                             // keeping track of the number of names added

    //=======================================Constructors ==========================================================
    MyListModel()
    {  
        
    }
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

