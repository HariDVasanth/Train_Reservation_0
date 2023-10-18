public class Admin {
    private String userName = "admin";
    private String password = "admin";

    public  boolean checkUser(String user,String pass_word){
        if(userName.equals(user) && pass_word.equals(password)){
            return true;
        }
        return false;
    }
}
