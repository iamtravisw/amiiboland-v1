import java.sql.*;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;

public class RemoveAmiibo {

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

        ConnHelper connHelper = new ConnHelper();
        Date modDate = new Date();
        int amiiboID = this.amiiboID;
        int userID = this.userID;

        System.out.println("UserID is: " +userID);

        try {
                // Check to see if the row already exists before inserting any new rows
                String checkForDelete = "SELECT CollectionID FROM Collection WHERE AmiiboID = ? AND UserID = ? LIMIT 1";
                PreparedStatement psCheckDelete = connHelper.db().prepareStatement(checkForDelete);
                psCheckDelete.setInt(1, amiiboID);                               // AmiiboID
                psCheckDelete.setInt(2, userID);                             // UserID
                ResultSet rsDelete = psCheckDelete.executeQuery();              // Execute

                System.out.println("RemoveAmiibo class has an ID of: " + this.amiiboID);


                // If the row does not exist, insert a new row for this user
                if (!rsDelete.isBeforeFirst()) {
                    System.out.println("Data does not exist... Nothing to do.");
                } else {
                    while (rsDelete.next()) {
                        int collectionID = rsDelete.getInt("CollectionID");
                        System.out.println("The row exists as CollectionID: " + collectionID + ". Updating database...");

                        // update the data
                        PreparedStatement psDelete = connHelper.db().prepareStatement(
                                "UPDATE Collection SET Collected = ?, ModUser = ?, ModDate = ? WHERE CollectionID = ?");

                        // set the preparedstatement parameters
                        psDelete.setString(1, "N");              // Collected
                        psDelete.setInt(2, userID);                 // ModUser
                        psDelete.setString(3, modDate.toString());  // ModDate
                        psDelete.setInt(4, collectionID);           // CollectionID
                        psDelete.executeUpdate();                      // Execute

                        System.out.println("RemoveAmiibo class has an ID of: " + this.amiiboID);
                    }
            connHelper.db().close();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}