
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Model {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private String DBName = "heroku_be826f701685add";

    public Model() throws Exception {
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager
                    .getConnection("mysql://b363f5860f72e3:94e5fe61@us-cdbr-iron-east-01.cleardb.net/heroku_be826f701685add?reconnect=true");
        } catch (Exception e) {
            throw e;
        }
    }

    public String create_developer(String email, String plang, String lang) throws SQLException {
        //Insert on developers table and retrieve dev_id;
        preparedStatement = this.connect
                .prepareStatement("INSERT INTO  "+this.DBName+".developers VALUES (default, ?, default, default)");
        preparedStatement.setString(1, email);
        preparedStatement.executeUpdate();

        preparedStatement = this.connect
                .prepareStatement("SELECT last_insert_id() AS last_id FROM "+this.DBName+".developers");
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String dev_id = resultSet.getString("last_id");

        //Insert on programming languages table and retrieving plang_id;
        preparedStatement = this.connect
                .prepareStatement("INSERT INTO "+this.DBName+".programming_languages VALUES (default, ?, ?)");
        preparedStatement.setString(1, dev_id);
        preparedStatement.setString(2, plang);
        preparedStatement.executeUpdate();

        preparedStatement = this.connect
                .prepareStatement("SELECT last_insert_id() AS last_id FROM "+this.DBName+".programming_languages");
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String plang_id = resultSet.getString("last_id");

        //Insert on languages table and retrieving lang_id;
        preparedStatement = this.connect
                .prepareStatement("INSERT INTO  "+this.DBName+".languages VALUES (default, ?, ?)");
        preparedStatement.setString(1, dev_id);
        preparedStatement.setString(2, lang);
        preparedStatement.executeUpdate();

        preparedStatement = this.connect
                .prepareStatement("SELECT last_insert_id() AS last_id FROM "+this.DBName+".languages");
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String lang_id = resultSet.getString("last_id");

        //Update dating developers table
        preparedStatement = this.connect
                .prepareStatement("UPDATE  "+this.DBName+".developers SET plang_id=?, lang_id=? WHERE id="+dev_id);
        preparedStatement.setString(1, plang_id);
        preparedStatement.setString(2, lang_id);
        preparedStatement.executeUpdate();

        return "[{\"status\":\"success\",\"developer\":\""+email+"\",\"programming_language\":\""+plang+"\",\"language\":\""+lang+"\"}]";
    }

    public void readDataBase() throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/feedback?"
                            + "user=root&password=");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement
                    .executeQuery("select * from feedback.comments");
            writeResultSet(resultSet);

            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect
                    .prepareStatement("insert into  feedback.comments values (default, ?, ?, ?, ? , ?, ?)");
            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
            // Parameters start with 1
            preparedStatement.setString(1, "Test");
            preparedStatement.setString(2, "TestEmail");
            preparedStatement.setString(3, "TestWebpage");
            preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
            preparedStatement.setString(5, "TestSummary");
            preparedStatement.setString(6, "TestComment");
            preparedStatement.executeUpdate();

            preparedStatement = connect
                    .prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from feedback.comments");
            resultSet = preparedStatement.executeQuery();
            writeResultSet(resultSet);

            // Remove again the insert comment
            preparedStatement = connect
                    .prepareStatement("delete from feedback.comments where myuser= ? ; ");
            preparedStatement.setString(1, "Test");
            preparedStatement.executeUpdate();

            resultSet = statement
                    .executeQuery("select * from feedback.comments");
            writeMetaData(resultSet);

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    private void writeMetaData(ResultSet resultSet) throws SQLException {
        //  Now get some metadata from the database
        // Result set get the result of the SQL query

        System.out.println("The columns in the table are: ");

        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
        for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
            System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
        }
    }

    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            String user = resultSet.getString("myuser");
            String website = resultSet.getString("webpage");
            String summary = resultSet.getString("summary");
            Date date = resultSet.getDate("datum");
            String comment = resultSet.getString("comments");
            System.out.println("User: " + user);
            System.out.println("Website: " + website);
            System.out.println("summary: " + summary);
            System.out.println("Date: " + date);
            System.out.println("Comment: " + comment);
        }
    }

    // You need to close the resultSet
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

}