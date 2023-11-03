package org.lessons.java;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {

        String url = "jdbc:mysql://localhost:3306/db_nations";
        String user= "root";
        String password = "root";

        try(Connection conn = DriverManager.getConnection(url, user, password)){
            String query = "SELECT c.name as country_name,c.country_id,r.name as region_name,co.name as continent_name  FROM countries c JOIN regions r on c.region_id = r.region_id JOIN continents co ON r.continent_id = co.continent_id";

            try(PreparedStatement ps = conn.prepareStatement(query)){
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        System.out.printf("%s - %d - %s - %s\n", rs.getString("country_name"),rs.getInt("country_id"),rs.getString("region_name"),rs.getString("continent_name"));
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                    System.out.println("Errore esecuzione query");
                }
            }catch (SQLException e){
                e.printStackTrace();
                System.out.println("Errore PrepareStatement");
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Errore connessione db");
        }
    }
}