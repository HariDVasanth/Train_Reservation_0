import java.sql.*;

class Bank {
    private int accountNumber;
    private String accountHolder;
    private double balance;

    public Connection con(int Ano, String Hname) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "Hariharan9@");
        return con;
    }

    public void deposit(int amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount");
        }
    }

    public static boolean withdraw(int AccNo, int amount) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "Hariharan9@");
        String sql = "select balance from bank where AccNo= ? ;";
        PreparedStatement p = con.prepareStatement(sql);
        p.setInt(1, AccNo);
        boolean b1 = false;
        ResultSet r = p.executeQuery();
        if (r.next()) {
            do {
                int b = r.getInt("balance");
                if (b > amount) {
                    String sql1 = "update bank set balance = balance- ?;";
                    PreparedStatement p1 = con.prepareStatement(sql1);

                    p1.setInt(1, amount);
                    p1.execute();
                    b1 = true;
                    return true;
                } else {
                    System.out.println("Invalid withdrawal amount or insufficient balance");
                }

            }while (r.next());
        }
        else{
            System.out.println("Invalid Account Number");
        }
        return b1;

        }

    }

