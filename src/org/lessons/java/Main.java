package org.lessons.java;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserisci lo stato che vuoi ricercare.");
        String choice = "%" + scanner.nextLine() + "%";
        String url = "jdbc:mysql://localhost:3306/db_nations";
        String user= "root";
        String password = "root";

        try(Connection conn = DriverManager.getConnection(url, user, password)){
            String query = "SELECT c.name as country_name,c.country_id,r.name as region_name,co.name as continent_name  \n" +
                    "FROM countries c \n" +
                    "JOIN regions r on c.region_id = r.region_id \n" +
                    "JOIN continents co ON r.continent_id = co.continent_id\n" +
                    "where c.name like ?";

            try(PreparedStatement ps = conn.prepareStatement(query)){
                ps.setString(1, choice);
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
