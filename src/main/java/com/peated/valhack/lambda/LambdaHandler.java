package com.peated.valhack.lambda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaHandler implements RequestHandler<Map<String, String>, String> {
    @Override
    public String handleRequest(Map<String, String> event, Context context) {
        System.out.println(getTournamentCounts());

        return switch (event.getOrDefault("action", "default")) {
            case "greet" -> "Hello from Java 21 Lambda!";
            case "farewell" -> "Goodbye from Java 21 Lambda!";
            default -> "Unknown action";
        };
    }

    public Map<Integer, Integer> getTournamentCounts() {
        try {
            Map<Integer, Integer> tournamentCounts = new HashMap<>();
            String url = "jdbc:h2:file:/opt/valhack;ACCESS_MODE_DATA=r";
            String query = "select tournament_id, count(*) as count from GAME group by tournament_id";

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    int tournamentId = rs.getInt("tournament_id");
                    int count = rs.getInt("count");
                    tournamentCounts.put(tournamentId, count);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return tournamentCounts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
