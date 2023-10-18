import javax.mail.*;
import java.sql.*;
import java.util.Scanner;

class details{
    private int SleeperAvailable;
    private int AcAvailable;

    private int SleeperFare;
    private int AcFare;


    public Connection trainDetails(String TrainName,int TrainNumber,String StartingPoint,String EndPoint,int TotalSeats,int SleeperSeats,int AcSeats,int SleeperFare,int AcFare) throws SQLException{
        Connection con=DBConnection.getConnection();
        String s="insert into TRAIN_DETAILS (TRAIN_NAME, TRAIN_NUMBER, DEPARTURE, DESTINATION, SEATS_AVAILABLE, SLEEPER_SEATS, AC_SEATS, SLEEPER_FARE, AC_FARE) VALUES(?,?,?,?,?,?,?,?,?)";

        PreparedStatement p= con.prepareCall(s);
        p.setString(1,TrainName);
        p.setInt(2,TrainNumber);
        p.setString(3,StartingPoint);
        p.setString(4,EndPoint);
        p.setInt(5, TotalSeats);
        p.setInt(6,SleeperSeats);
        p.setInt(7,AcSeats);
        p.setInt(8,SleeperFare);
        p.setInt(9,AcFare);
        p.execute();
        p.close();
        System.out.println("Details added");
        return con;
    }
    public Connection search(String Departure) throws SQLException {
        Connection con = DBConnection.getConnection();
        String sql = "select * from TRAIN_DETAILS where DEPARTURE = ? ";
        PreparedStatement p = con.prepareStatement(sql);
        p.setString(1, Departure);
        ResultSet r = p.executeQuery();
        if(r.next()) {
            do{
                String name = r.getString("TRAIN_NAME");
                int num = r.getInt("TRAIN_NUMBER");
                String start = r.getString("DEPARTURE");
                String stop = r.getString("DESTINATION");
                int seats = r.getInt("SEATS_AVAILABLE");
                SleeperAvailable = r.getInt("SLEEPER_SEATS");
                AcAvailable = r.getInt("AC_SEATS");
                SleeperFare = r.getInt("SLEEPER_FARE");
                AcFare = r.getInt("AC_FARE");
                System.out.println(name + " \t " + num + " \t " + start + " \t " + stop + " \t " + seats + " \t " + SleeperAvailable + " \t " + AcAvailable + " \t " + SleeperFare + " \t " + AcFare);
            }while (r.next());
        }
        else{
            System.out.println("Entered Wrong Train Name.");
        }
        r.close();
        ;
        return con;
    }
    public void sleeperBookTicket(String TrainName,int TrainNumber,String StartingPoint,String EndingPoint,int SeatsBooking,String Mail)throws SQLException,MessagingException{
        Scanner sc=new Scanner(System.in);
        Connection con = DBConnection.getConnection();

        String sql="SELECT SLEEPER_SEATS,SLEEPER_FARE FROM TRAIN_DETAILS where TRAIN_NAME = ? ";
        PreparedStatement p= con.prepareStatement(sql);
        p.setString(1,TrainName);

        int Sleeper_avail,fare;
        boolean bool;
        ResultSet r= p.executeQuery();

        if(r.next()) {
            do{
                Sleeper_avail = r.getInt("SLEEPER_SEATS");
                if (Sleeper_avail <= 0) {
                    String sql2 = "INSERT INTO WAITING_LIST (TRAIN_NAME, EMAIL_ID,TRAIN_NUMBER,SEAT_TYPE) VALUES (?,?,?,?)";
                    PreparedStatement p2 = con.prepareStatement(sql2);
                    p2.setString(1, TrainName);
                    p2.setString(2, Mail);
                    p2.setInt(3, TrainNumber);
                    p2.setString(4, "Sleeper");
                    p2.executeUpdate();
                    System.out.println("Seats currently not available. " + "\n" + "We will notify you via mail when tickets are Available.");
                } else if (Sleeper_avail >= SeatsBooking) {
                    fare = r.getInt("SLEEPER_FARE");
                    int SleeperFare = SeatsBooking * fare;
                    System.out.println("Amount to pay: " + SleeperFare);
                    System.out.println("Enter your Account number");
                    int AccNo = sc.nextInt();

                    Bank b = new Bank();
                    bool = Bank.withdraw(AccNo, SleeperFare);
                    if (bool) {
                        String s3 = "UPDATE TRAIN_DETAILS SET SEATS_AVAILABLE= SEATS_AVAILABLE - ? WHERE TRAIN_NAME=?";
                        String s4 = "UPDATE TRAIN_DETAILS SET SLEEPER_SEATS= SLEEPER_SEATS - ? WHERE TRAIN_NAME=?";

                        PreparedStatement p3 = con.prepareStatement(s3);
                        p3.setInt(1, SeatsBooking);
                        p3.setString(2,TrainName);
                        p3.executeUpdate();

                        PreparedStatement p4 = con.prepareStatement(s4);
                        p4.setInt(1, SeatsBooking);
                        p4.setString(2,TrainName);
                        p4.execute();


                        String message = "Train booked Successfully Train Name: " + " " + TrainName + "\n" + "Train Number: " + " " + TrainNumber + "\n" + "Seats Booked: " + SeatsBooking + "\n" + "Fare: " + SleeperFare;
                        Mail m = new Mail(Mail, message);

                        String s5 = "insert into TICKET_BOOKED (TRAIN_NAME, TRAIN_NUMBER, SEAT_TYPE, SEATS_BOOKED, EMAIL_ID, FARE) VALUES(?,?,?,?,?,?)";
                        PreparedStatement p5 = con.prepareStatement(s5);
                        p5.setString(1, TrainName);
                        p5.setInt(2, TrainNumber);
                        p5.setString(3, "SLEEPER");
                        p5.setInt(4, SeatsBooking);
                        p5.setString(5, Mail);
                        p5.setInt(6, fare);

                        p5.executeUpdate();

                        System.out.println("Ticket Booked Successfully information sent to your mail.");

                    }



                } else {
                    System.out.println("Booking failed");
                }
            }while (r.next());
        }
        else {
            System.out.println("Entered Wrong TrainName");
        }
    }

    public void AcBookTicket(String TrainName,int TrainNumber,String StartingPoint,String EndingPoint,int SeatsBooking,String Mail) throws SQLException, MessagingException {
        Scanner sc=new Scanner(System.in);
        Connection con = DBConnection.getConnection();
        int Ac_available,Fare;
        boolean bool;
        int wl=0;

        String sql="select AC_SEATS,AC_FARE from TRAIN_DETAILS where TRAIN_NAME = ?";
        PreparedStatement p= con.prepareStatement(sql);
        p.setString(1,TrainName);

        ResultSet r= p.executeQuery();
        if(r.next()) {
            do {
                Ac_available= r.getInt("AC_SEATS");
                if (Ac_available <= 0) {
                    String sql2 = "INSERT INTO WAITING_LIST (TRAIN_NAME,EMAIL_ID,TRAIN_NUMBER,SEAT_TYPE) VALUES (?,?,?,?)";
                    PreparedStatement p2 = con.prepareStatement(sql2);

                    p2.setString(1,TrainName);
                    p2.setString(2, Mail);
                    p2.setInt(3, TrainNumber);
                    p2.setString(4, "AC");
                    p2.execute();
                    System.out.println("Seats currently not available. " + "\n" + "We will notify you via mail when tickets are Available.");

                } else if (Ac_available >= SeatsBooking) {
                    Fare = r.getInt("AC_FARE");
                    int AcFare = SeatsBooking * Fare;
                    System.out.println("Amount to pay:" + " " + AcFare);

                    System.out.println("Enter your Account number");
                    int AccNo = sc.nextInt();
                    Bank b = new Bank();
                    bool = Bank.withdraw(AccNo, AcFare);

                    if (bool) {
                        String s3 = "UPDATE TRAIN_DETAILS SET SEATS_AVAILABLE= SEATS_AVAILABLE - ? WHERE TRAIN_NAME= ?";
                        String s4 = "UPDATE TRAIN_DETAILS SET AC_SEATS = AC_SEATS - ? WHERE TRAIN_NAME= ?";
                        PreparedStatement p3 = con.prepareStatement(s3);
                        p3.setInt(1, SeatsBooking);
                        p3.setString(2,TrainName);
                        p3.executeUpdate();

                        PreparedStatement p4 = con.prepareStatement(s4);
                        p4.setInt(1, SeatsBooking);
                        p4.setString(2,TrainName);
                        p4.executeUpdate();

                        String message = "Train booked Successfully Train Name: " + " " + TrainName + "\n"   + "Train Number: " + " " + TrainNumber + "\n" + "Seats Booked: " + SeatsBooking + "\n" + "Fare: " + AcFare;
                        Mail m = new Mail(Mail, message);

                        String s5 = "insert into TICKET_BOOKED (TRAIN_NAME, TRAIN_NUMBER, SEAT_TYPE, SEATS_BOOKED, EMAIL_ID, FARE) VALUES(?,?,?,?,?,?)";
                        PreparedStatement p5 = con.prepareStatement(s5);
                        p5.setString(1, TrainName);
                        p5.setInt(2, TrainNumber);
                        p5.setString(3, "AC");
                        p5.setInt(4, SeatsBooking);
                        p5.setString(5, Mail);
                        p5.setInt(6, Fare);

                        p5.execute();

                        System.out.println("ticket Successfully booked info sent to your email");

                    } else {
                        System.out.println("Booking failed");
                    }


                }
            }while (r.next());
        }
        else{
            System.out.println("Entered Wrong Train Name");
        }
    }

    public void CancelledTicket(String Train_Name,int Fare,int SeatsBooked,String Mail_Id,String Type) throws SQLException, MessagingException {
        Connection con = DBConnection.getConnection();
        String sql="DELETE from TICKET_BOOKED where TRAIN_NAME = ?";
        PreparedStatement p= con.prepareStatement(sql);
        p.setString(1,Train_Name);
        p.execute();

        Bank b=new Bank();
        b.deposit(Fare);
        String message = "Your Train ticket Cancellation is Successful "+"\n"+"Train Name: "+Train_Name+"\n"+"Seats Booked: "+SeatsBooked+"\n"+"Amount Refunded: "+Fare;
        Mail m=new Mail(Mail_Id,message);
        System.out.println("Ticket Cancelled Successfully");

        if(Type.equals("AC")){
            String sql2="UPDATE TRAIN_DETAILS SET SEATS_AVAILABLE = SEATS_AVAILABLE + ? WHERE TRAIN_NAME = ?";
            String sql3="UPDATE TRAIN_DETAILS SET AC_SEATS= AC_SEATS + ? WHERE TRAIN_NAME = ?";

            PreparedStatement p1=con.prepareStatement(sql2);
            p1.setInt(1,SeatsBooked);
            p1.setString(2,Train_Name);
            p1.execute();

            PreparedStatement p2=con.prepareStatement(sql3);
            p2.setInt(1,SeatsBooked);
            p2.setString(2,Train_Name);
            p2.execute();

            String sql4="SELECT EMAIL_ID from WAITING_LIST where TRAIN_NAME= ? ";
            PreparedStatement p3=con.prepareStatement(sql4);
            p3.setString(1,Train_Name);
            ResultSet r=p3.executeQuery();
            if(r.next()) {
                do{
                    String Mail = r.getString("EMAIL_ID");
                    String Message1 = "Seats in " + " " + Train_Name + " " + "are now available to book.";
                    m = new Mail(Mail, Message1);
                }while (r.next());
            }
            else {
                System.out.println("The members in the Waiting list to be notified");
                }
        }
        else if(Type.equals("Sleeper")) {
            String sql2 = "UPDATE TRAIN_DETAILS SET SEATS_AVAILABLE= SEATS_AVAILABLE + ? WHERE TRAIN_NAME=?";
            String sql3 = "UPDATE TRAIN_DETAILS SET SLEEPER_SEATS= SLEEPER_SEATS + ? WHERE TRAIN_NAME=?";

            PreparedStatement p1 = con.prepareStatement(sql2);
            p1.setInt(1, SeatsBooked);
            p1.setString(2,Train_Name);
            p1.execute();

            PreparedStatement p2 = con.prepareStatement(sql3);
            p2.setInt(1, SeatsBooked);
            p2.setString(2,Train_Name);
            p2.execute();

            String sql4 = "SELECT EMAIL_ID from WAITING_LIST where TRAIN_NAME= ? ";
            PreparedStatement p3 = con.prepareStatement(sql4);
            p3.setString(1, Train_Name);
            ResultSet r = p3.executeQuery();
            if(r.next()) {
                do {
                    String Mail = r.getString("EMAIL_ID");
                    String Message1 = "Seats in " + " " + Train_Name + " " + "are now available to book.";
                    m = new Mail(Mail, Message1);
                }while (r.next());
            }else{
                System.out.println("The members in the Waiting list to be notified");
            }
        }
    }
}

public class TrainResarvation {
    public static void main(String[] args) throws SQLException, MessagingException {
        while(true) {

            System.out.println("***** Welcome ******");
            System.out.println("1.Employee login");
            System.out.println("2.Login");
            System.out.println("3.Register Account");
            System.out.println("4.Exit");

            System.out.println("Enter the operation: ");

            Scanner sc = new Scanner(System.in);
            int n = sc.nextInt();
            switch (n) {
                case 1:
                    eLogin();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    register();
                    break;
                case 4:
                    System.out.println("Successfully Exited");
                    return;
                default:
                    System.out.println("Enter valid Operation");
                    break;
            }
            System.out.println("\n");
        }

    }

    public static void eLogin() throws SQLException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your Username: ");
        String UserName = sc.next();
        System.out.println("Enter your Password:");
        String PassWord = sc.next();

        Admin a=new Admin();
        if(a.checkUser(UserName,PassWord)){
            System.out.println("Enter Train Name");
            String Tname = sc.next();

            System.out.println("Enter Train number");
            int Tnumber = sc.nextInt();

            System.out.println("Enter train Starting point");
            String Start = sc.next();

            System.out.println("Enter the Destination");
            String destination = sc.next();

            System.out.println("Enter Sleeper seats available");
            int Sleeper = sc.nextInt();

            System.out.println("Enter AC seats Available");
            int ac = sc.nextInt();

            int Total = Sleeper + ac;

            System.out.println("Enter Sleeper class fare");
            int sleeperfare = sc.nextInt();

            System.out.println("Enter AC class fare");
            int Acfare = sc.nextInt();

            details t = new details();
            t.trainDetails(Tname, Tnumber, Start, destination, Total, Sleeper, ac, sleeperfare, Acfare);
        } else {
            System.out.println("Invalid Login Details...");
        }
    }

    public static void login() throws SQLException, MessagingException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your Username: ");
        String UserName = sc.next();
        System.out.println("Enter your Password");
        String PasswordEntered = sc.next();
        Connection con = DBConnection.getConnection();

        String s = "SELECT PASS_WORD FROM REGISTER WHERE USER_NAME = ? ";
        PreparedStatement p = con.prepareStatement(s);
        p.setString(1, UserName);
        ResultSet r = p.executeQuery();
        String Password;
        if(r.next()){

            do{
                Password = r.getString("PASS_WORD");
                if (Password.equals(PasswordEntered)) {
                    System.out.println("Welcome " + UserName);
                    System.out.println("1.Book a ticket");
                    System.out.println("2.Cancel Ticket");
                    System.out.println("3.Exit");

                    int choice = sc.nextInt();
                    switch (choice) {
                        case 1:
                            book();
                            break;
                        case 2:
                            cancel();
                            break;
                        case 3:
                            return;
                    }
                } else {
                    System.out.println("Invalid Login Details");
                }

            }while (r.next());
        }
        else{
            System.out.println("User Credentials Invalid");
        }
    }

    public static void book() throws SQLException, MessagingException {
        while (true) {
            Scanner sc = new Scanner(System.in);

            System.out.println("1.Display all Trains Available");
            System.out.println("2.Search by start point");
            System.out.println("3.book trains");
            System.out.println("4.Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    TrainTable t=new TrainTable();
                    TrainTable.TrainDisplay();
                    break;
                case 2:
                    System.out.println("Enter your Starting point");
                    String Search = sc.next();
                    details d = new details();
                    d.search(Search);
                    break;

                case 3:
                    String Ac = "AC";
                    String Sleeper = "Sleeper";
                    System.out.println("Enter Train name: ");
                    String TrainName = sc.next();

                    System.out.println("Enter Train Number: ");
                    int TrainNumber = sc.nextInt();

                    System.out.println("Enter your Departure: ");
                    String Departure = sc.next();

                    System.out.println("Enter your Destination:");
                    String Destination = sc.next();

                    System.out.println("Enter number of seats: ");
                    int Seats = sc.nextInt();

                    System.out.println("Seat type: AC Or Sleeper");
                    String Type = sc.next();

                    System.out.println("Enter your email");
                    String Email = sc.next();
                    if (Type.equals(Sleeper)) {
                        details dBook = new details();
                        dBook.sleeperBookTicket(TrainName, TrainNumber, Departure, Destination, Seats, Email);
                    } else if (Type.equals(Ac)) {
                        details dBook = new details();
                        dBook.AcBookTicket(TrainName, TrainNumber, Departure, Destination, Seats, Email);
                    } else {
                        System.out.println("PLease enter AC or SLEEPER");
                    }

                    break;
                case 4:
                    System.out.println("Exiting...........");
                    return;
            }
        }
    }
    public static void cancel() throws SQLException, MessagingException {

        Scanner sc=new Scanner(System.in);
        System.out.println("Enter your Train Name");
        String TrainName=sc.next();

        System.out.println("Enter your Seat Type");
        String SeatType=sc.next();

        System.out.println("Enter your E-Mail id: ");
        String Email=sc.next();

        Connection con = DBConnection.getConnection();
        String sql="SELECT FARE,SEATS_BOOKED,TRAIN_NAME from TICKET_BOOKED where EMAIL_ID = ? AND TRAIN_NAME = ?";
        PreparedStatement p= con.prepareStatement(sql);
        p.setString(1,Email);
        p.setString(2,TrainName);
        ResultSet r=p.executeQuery();

        String TRAIN_NAME = null;
        int Ticket=0;
        int FARE=0;

        if(r.next()){
           do{
                TRAIN_NAME= r.getString("TRAIN_NAME");
                FARE=r.getInt("FARE");
                Ticket=r.getInt("SEATS_BOOKED");
                }while (r.next());
            details d=new details();
            d.CancelledTicket(TRAIN_NAME,FARE,Ticket,Email,SeatType);
        }else{
            System.out.println("Invalid Train Name or Email Id");
        }




    }
    public static void register() throws SQLException {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter a Username");
        String Username=sc.next();
        System.out.println("Enter a Password");
        String Password=sc.next();
        if (containsNumber(Password) && containsUppercase(Password) && hasLengthOfEight(Password)) {
            Connection con =DBConnection.getConnection();

            String sql="insert into REGISTER(USER_NAME,PASS_WORD) VALUES (?,?);";
            PreparedStatement p= con.prepareStatement(sql);
            p.setString(1,Username);
            p.setString(2,Password);
            p.execute();
            System.out.println("Account Created Successfully");

            p.close();

        } else {
            System.out.println("Give a strong password");
        }
    }
    public static boolean containsNumber(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
    public static boolean containsUppercase(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }
    public static boolean hasLengthOfEight(String str) {
        return str.length() >= 8;
    }


}


