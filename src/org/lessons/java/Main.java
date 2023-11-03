package org.lessons.java;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserisci lo stato che vuoi ricercare.");
        String choice = "%" + scanner.nextLine() + "%";

        //dati per connessione db
        String url = "jdbc:mysql://localhost:3306/db_nations";
        String user= "root";
        String password = "root";

        //Connessione a db
        try(Connection conn = DriverManager.getConnection(url, user, password)){

            //Prima query per selezione tramite ricerca parola inserita
            String query = "SELECT c.name as country_name,c.country_id,r.name as region_name,co.name as continent_name  \n" +
                    "FROM countries c \n" +
                    "JOIN regions r on c.region_id = r.region_id \n" +
                    "JOIN continents co ON r.continent_id = co.continent_id\n" +
                    "where c.name like ?";

            //PreparedStatement per ricerca parola
            try(PreparedStatement ps = conn.prepareStatement(query)){
                ps.setString(1, choice);

                //Esecuzione query
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

            //Seconda richiesta, inserire il codice nazione
            System.out.println("Seleziona il codice nazione che vuoi visualizzare.");
            int choice2 = Integer.parseInt(scanner.nextLine());

            String query2 = "SELECT c.name, GROUP_CONCAT(l.`language`, \" \") as lingue \n" +
                    "FROM countries c \n" +
                    "join country_languages cl on cl.country_id = c.country_id \n" +
                    "join languages l on cl.language_id = l.language_id \n" +
                    "where c.country_id = ? \n" +
                    "group by c.name";

            //PreparedStatement per codice inserito
            try(PreparedStatement ps = conn.prepareStatement(query2)){
                ps.setInt(1, choice2);

                //Esecuzione seconda query
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        System.out.printf("Dettagli nazione: %s\nLingue: %s\n",rs.getString("name"),rs.getString("lingue"));
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                    System.out.println("Errore esecuzione query");
                }
            }catch (SQLException e){
                e.printStackTrace();
                System.out.println("Errore PrepareStatement");
            }

            //Terza stringa con dati aggiuntivi
            String query3 = "SELECT cs.`year` ,cs.population ,cs.gdp \n" +
                    "FROM country_stats cs \n" +
                    "where cs.country_id = ?\n" +
                    "order by cs.year desc\n" +
                    "limit 1";

            try(PreparedStatement ps = conn.prepareStatement(query3)){
                ps.setInt(1, choice2);
                //Esecuzione query
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
        scanner.close();
    }
}
