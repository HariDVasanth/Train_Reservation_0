import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

    public class TrainTable {

            public static void TrainDisplay(){
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "Hariharan9@");
                Statement statement = connection.createStatement();
                String query = "select * from train_details;";
                ResultSet resultSet = statement.executeQuery(query);

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String ColumnName=String.format(metaData.getColumnName(i));
                    String formatted= String.format("%-20s",ColumnName);
                    System.out.print(formatted);

                }
                System.out.println();

                String query1="Select * from TRAIN_DETAILS";
                Statement st=connection.createStatement();
                ResultSet r=st.executeQuery(query1);

                while (r.next()){
                    String TrainName = r.getString("TRAIN_NAME");
                    int TrainNumber = r.getInt("TRAIN_NUMBER");
                    String StartingPoint = r.getString("DEPARTURE");
                    String EndingPoint = r.getString("DESTINATION");
                    int SeatsAvailable = r.getInt("SEATS_AVAILABLE");
                    int SleeperSeats= r.getInt("SLEEPER_SEATS");
                    int AcSeats= r.getInt("AC_SEATS");
                    int SleeperFare=r.getInt("SLEEPER_FARE");
                    int AcFare=r.getInt("AC_FARE");


                    System.out.printf("%-20s %-20d %-20s %-20s %-20d %-20d %-20d %-20d %-20d%n", TrainName, TrainNumber, StartingPoint, EndingPoint, SeatsAvailable, SleeperSeats, AcSeats,SleeperFare,AcFare);

                }


                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


