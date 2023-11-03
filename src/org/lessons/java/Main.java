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
            System.out.println("Seleziona il codice nazione che vuoi visualizzare.");
            int choice2 = Integer.parseInt(scanner.nextLine());

            String query2 = "SELECT c.name, GROUP_CONCAT(l.`language`, \" \") as lingue \n" +
                    "FROM countries c \n" +
                    "JOIN regions r on c.region_id = r.region_id \n" +
                    "JOIN continents co ON r.continent_id = co.continent_id\n" +
                    "join country_languages cl on cl.country_id = c.country_id \n" +
                    "join languages l on cl.language_id = l.language_id \n" +
                    "where c.country_id = ? \n" +
                    "group by c.name";

            try(PreparedStatement ps = conn.prepareStatement(query2)){
                ps.setInt(1, choice2);
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        System.out.printf("Dettagli nazione: %s\nLingue: %s\n",rs.getString("name"),rs.getString("lingue")

                                );
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                    System.out.println("Errore esecuzione query");
                }
            }catch (SQLException e){
                e.printStackTrace();
                System.out.println("Errore PrepareStatement");
            }
            String query3 = "SELECT cs.`year` ,cs.population ,cs.gdp \n" +
                    "FROM countries c\n" +
                    "join country_stats cs on c.country_id = cs.country_id \n" +
                    "where c.country_id = '107'\n" +
                    "order by cs.year desc\n" +
                    "limit 1\n";

            try(PreparedStatement ps = conn.prepareStatement(query3)){
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        System.out.printf("Statistiche recenti:\nYear: %d\nPopulation: %d\nGDP: %d",rs.getInt("year"),rs.getLong("population"),rs.getLong("gdp")
                        );
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
