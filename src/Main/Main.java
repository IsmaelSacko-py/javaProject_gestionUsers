package Main;

import dao.DB;

public class Main {
    public static void main(String[] args)
    {
        DB db = new DB();
        db.getConnection();
    }
}