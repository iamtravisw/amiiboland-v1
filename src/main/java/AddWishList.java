import java.sql.*;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;

public class AddWishList {
    // Variables
    public int userID = 1;
    public int amiiboID;

    // Setters and Getters
    public void setAmiiboID(int amiiboID) {
        this.amiiboID = amiiboID;
    }
    public int getAmiiboID() {
        return amiiboID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getUserID() {
        return userID;
    }

    public void main(String[] args) {

        Date addDate = new Date();
        Date modDate = new Date();
        String dbURL = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");
        int amiiboID = this.amiiboID;
        int userID = this.userID;

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (conn != null)
            {

                // Check to see if the row already exists before inserting any new rows
                String checkExistsWishList = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
                PreparedStatement psAddWishList = conn.prepareStatement(checkExistsWishList);
                psAddWishList.setInt(1, amiiboID);                                 // AmiiboID
                psAddWishList.setInt(2, userID);                               // UserID
                ResultSet rsAddWishList = psAddWishList.executeQuery();               // Execute

                System.out.println("AddWishList class has an ID of: " + this.amiiboID);

                // If the row does not exist, insert a new row for this user
                if (!rsAddWishList.isBeforeFirst()) {
                    System.out.println("Data does not exist... Inserting into database...");

                    // sql insert statement
                    String faveAmiibo = "insert into Collection (AmiiboID, UserID, WishList, ModUser, ModDate, AddUser, AddDate)"
                            + "values (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement psWishInsert = conn.prepareStatement(faveAmiibo);

                    // sql insert values
                    psWishInsert.setInt(1, amiiboID);                  // AmiiboID
                    psWishInsert.setInt(2, userID);                 // UserID
                    psWishInsert.setString(3, "Y");             // WishList Y/N
                    psWishInsert.setInt(4, userID);                 // ModUser (ID)
                    psWishInsert.setString(5, modDate.toString()); // ModDate
                    psWishInsert.setInt(6, userID);                 // AddUser (ID)
                    psWishInsert.setString(7, addDate.toString()); // AddDate
                    psWishInsert.execute();                           // Execute

                    // If the row does exist, simply update it
                } else {
                    while (rsAddWishList.next()) {
                        int collectionID = rsAddWishList.getInt("CollectionID");
                        System.out.println("This row already exists as CollectionID: " + collectionID + ". Updating database...");

                        // update the data
                        PreparedStatement psWishUpdate = conn.prepareStatement(
                                "UPDATE Collection SET WishList = ?, ModUser = ?, ModDate = ? WHERE CollectionID = ?");

                        // set the preparedstatement parameters
                        psWishUpdate.setString(1, "Y");             // WishList
                        psWishUpdate.setInt(2, userID);                 // ModUser
                        psWishUpdate.setString(3, modDate.toString()); // ModDate
                        psWishUpdate.setInt(4, collectionID);           // CollectionID
                        psWishUpdate.executeUpdate();                    // Execute

                        System.out.println("AddWishList class has an ID of: " + this.amiiboID);
                    }
                }
                conn.close();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
