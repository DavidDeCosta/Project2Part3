import java.io.*;
import javax.swing.JOptionPane;
import java.util.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class TripRecord {

//========================================Data Members //=================================================
    
    Date date;                    //Date of the trip   long?
    String stringDate;
    String name;                  // Name of the patient
    String serviceCode;           // service code
    int initialMileage;           // initial mileage int?
    int mileageOnReturn;          // mileage on return int?
    double billingRate;           //billing rate   double?
    String comments;

    SimpleDateFormat sdf;
    String myDate;
    String anotherDate;

    Calendar cal;
    ParsePosition pos;

    

    String datee;


//========================================Constructors //===================================================
    TripRecord()
    {

    }

    TripRecord(String name, long date, String serviceCode, int initialMileage, int mileageOnReturn,
                double billingRate, String comments)
    {
        
    }

    TripRecord(DataInputStream dis)
    {
        try 
        {
            this.date = new Date(dis.readLong());
            this.name = dis.readUTF();
            this.serviceCode = dis.readUTF();
            this.initialMileage = dis.readInt();
            this.mileageOnReturn = dis.readInt();
            this.billingRate = dis.readDouble();
            this.comments = dis.readUTF();
        } 
        catch (IOException e) 
        {
            JOptionPane.showMessageDialog(null, "Could not read name");
        }

    }

//=========================================Methods //===================================================

    void store(DataOutputStream dos)
    {
        try 
        {
            dos.writeLong(date.getTime());
            dos.writeUTF(name);
            dos.writeUTF(serviceCode);
            dos.writeInt(initialMileage);
            dos.writeInt(mileageOnReturn);
            dos.writeDouble(billingRate);
            dos.writeUTF(comments);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    static TripRecord getRandom()
    {
        int randomNumber;
        int randomNumber2;
        
        Random ran;
        ran = new Random();
        
        TripRecord rec;
        rec = new TripRecord();

        String [] ranName = {"David", "Emily","Vincent", "Nathan"};
        String [] ranCode = {"A0428", "A0429", "A0427", "A0434"};
        String [] dates = {"10/05/1990", "08/19/1999", "04/25/2000", "11/03/2018"};
        String [] ranComment = {"Patient was crazy", "Patient was calm", "Patient was in pain", "No comment"};

        SimpleDateFormat sdf;
        ParsePosition pos;
        String stringOfDate = rec.stringDate = (dates[ran.nextInt(4)]);
        sdf = new SimpleDateFormat("M/d/y");
        pos = new ParsePosition(0);

        rec.date = (Date)sdf.parse(stringOfDate,pos);


        rec.name = (ranName[ran.nextInt(4)]);
        rec.serviceCode = (ranCode[ran.nextInt(4)]);

        randomNumber = (ran.nextInt(30000)+100);   // random number for initial mileage
        rec.initialMileage = (randomNumber);

        randomNumber2 = randomNumber + ran.nextInt(1000);  // random number for mileage on return
        rec.mileageOnReturn = (randomNumber2);

        rec.billingRate = (ran.nextDouble(0,2000));

        rec.comments = (ranComment[ran.nextInt(4)]);


        return rec;
    }

    void longToString(long date)
    {
        
    }


    @Override 
    public String toString() 
    {

        billingRate = Math.floor(billingRate * 100) /100;   //makes the decimal show to 2 places

        stringDate = (String.format("%tm/%te/%tY",date,date,date));

        

        return stringDate + "   " + name + "   " + serviceCode + "   " + initialMileage + "   " + mileageOnReturn + "   "
               +  billingRate + "     " + comments + "     " ;
    }


    
}
