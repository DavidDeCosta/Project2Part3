import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class DateVerifier extends InputVerifier
{

    @Override
    public boolean verify(JComponent input) {

        String dateStr;
        Date myDate;
        dateStr = ((JTextField)(input)).getText().trim();
        
        if(dateStr.equals(""))
        {
            return true;
        }
        
        try
        {
        String numberDelimited = dateStr;                                     // holds the original date string user typed in
        String []numbersSplit = numberDelimited.split("/");            // split the string up by '/' into an array
        String lastPart = numbersSplit[2]; //gets the year of the date        //make the last part equal to the 3rd section of array 'the year'
        if(lastPart.length() != 4)                                            //if they made the year to big or small return false
        {
            return false;
        }
        }
        catch(Exception e )
        {
            System.out.println("wrong format DDC \n");
        }



        
        SimpleDateFormat format;
        format = new SimpleDateFormat("M/d/y");
        format.setLenient(false);

        ParsePosition parsePos;
        parsePos = new ParsePosition(0); //start at the beggining

        myDate =  format.parse(dateStr,parsePos);

        if(parsePos.getIndex() == dateStr.length())
        {
            return true;
        }

        return false;
    }

}