package me.iamguus.ovo.handlers;

import me.iamguus.ovo.utils.ConfigUtil;
import org.bukkit.entity.Player;

import java.sql.*;

/**
 * Created by Guus on 3-1-2016.
 */
public class MySQLHandler {

    private static MySQLHandler instance;

    private Connection conn;

    private String host;
    private String port;
    private String database;
    private String username;
    private String password;

    public void connect() {
        host = ConfigUtil.get().getConfig().getString("mysql.host");
        port = ConfigUtil.get().getConfig().getString("mysql.port");
        database = ConfigUtil.get().getConfig().getString("mysql.database");
        username = ConfigUtil.get().getConfig().getString("mysql.username");
        password = ConfigUtil.get().getConfig().getString("mysql.password");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            System.out.println("Successfully connected to database!");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createRowForPlayer(Player player) {
        String sql1 = "INSERT IGNORE INTO player_kit(`uuid`, `kit_item`, `kit_armor`) VALUES('" + player.getUniqueId() + "', 'rO0ABXcEAAAAJHNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw" +
                "dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi" +
                "bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmpl" +
                "Y3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAA" +
                "AAJ0AAI9PXQABHR5cGV1cQB+AAYAAAACdAAeb3JnLmJ1a2tpdC5pbnZlbnRvcnkuSXRlbVN0YWNr" +
                "dAAKSVJPTl9TV09SRHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBw', 'rO0ABXcEAAAABHNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw" +
                "dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi" +
                "bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmpl" +
                "Y3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAA" +
                "AAJ0AAI9PXQABHR5cGV1cQB+AAYAAAACdAAeb3JnLmJ1a2tpdC5pbnZlbnRvcnkuSXRlbVN0YWNr" +
                "dAANRElBTU9ORF9CT09UU3NxAH4AAHNxAH4AA3VxAH4ABgAAAAJxAH4ACHEAfgAJdXEAfgAGAAAA" +
                "AnEAfgALdAAQRElBTU9ORF9MRUdHSU5HU3NxAH4AAHNxAH4AA3VxAH4ABgAAAAJxAH4ACHEAfgAJ" +
                "dXEAfgAGAAAAAnEAfgALdAASRElBTU9ORF9DSEVTVFBMQVRFc3EAfgAAc3EAfgADdXEAfgAGAAAA" +
                "AnEAfgAIcQB+AAl1cQB+AAYAAAACcQB+AAt0AA5ESUFNT05EX0hFTE1FVA==');";

        String sql2 = "INSERT IGNORE INTO players(`uuid`, `kitpref`, `mappref`) VALUES('" + player.getUniqueId() + "', 1, '" + MapHandler.get().getRandomMap().getName() + "');";

        executeUpdate(sql1);
        executeUpdate(sql2);
    }

    public void executeUpdate(String sql) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sql) {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static MySQLHandler get() {
        if (instance == null) {
            instance = new MySQLHandler();
        }
        return instance;
    }
}
