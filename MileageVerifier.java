import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class MileageVerifier extends InputVerifier
{


    @Override
    public boolean verify(JComponent input) 
    {
        String mileage;
        mileage = ((JTextField)(input)).getText().trim();

        if(mileage.equals(""))
        {
            return true;
        }

        double originalmileageValue;
        int numFormileage;
        try{
        originalmileageValue = Double.parseDouble(mileage);     
        numFormileage = (int)originalmileageValue;

        if(numFormileage == originalmileageValue)  //makes sure there isn't any numbers in the decimal place. 'if any'
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
