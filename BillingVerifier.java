import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class BillingVerifier extends InputVerifier
{

    @Override
    public boolean verify(JComponent input) {
        
        String billingRate;
        billingRate = ((JTextField)(input)).getText().trim(); // billing rate hold the string from the textfield

        if(billingRate.equals(""))  //has to be here incase user backspaces and leaves, the conversions get messed up
        {
            return true;
        }

        double numForBilling;
        double originalBillingNum;

    try{
        originalBillingNum = Double.parseDouble(billingRate);  //originalNum holds the 'double' number of that string

        numForBilling = (int)(originalBillingNum*100);         // numForbilling holds the integer of  originNum *100

        numForBilling /= 100.0;                                //puts the decimal place back two spaces

        if(numForBilling == originalBillingNum)
        {
            return true;
        }
    }
    catch(NumberFormatException e)
    {
        System.out.println("Number format exception DDC \n");
    }

        return false;
    }
    
}
