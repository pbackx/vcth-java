package com.peated.valhack.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.peated.valhack.lambda.messages.*;
import com.peated.valhack.model.Role;
import com.peated.valhack.model.Tournament;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LambdaHandler implements RequestHandler<Map<String, Object>, ActionGroupFunctionResponseEvent> {
    private final Connection conn;
    private final HashMap<String, PreparedStatement> preparedStatementCache = new HashMap<>();

    public LambdaHandler() throws Exception {
        Class.forName("org.h2.Driver");
        String url = "jdbc:h2:file:/opt/valhack;ACCESS_MODE_DATA=r";
        conn = DriverManager.getConnection(url);
    }

    private String getParameter(Map<String, Object> event, String parameterName) {
        var p = event.get("parameters");
        if (p == null) {
            return null;
        }
        var parameters = (List<Map<String, Object>>) p;

        for (var parameter : parameters) {
            if (parameter.get("name").equals(parameterName)) {
                if (parameter.get("type").equals("string")) {
                    return parameter.get("value").toString();
                } else {
                    throw new RuntimeException("Parameter type not supported");
                }
            }
        }
        return null;
    }

    private Tournament getTournament(Map<String, Object> event) {
        // The tournament. Options are "Game Changers", "VCT Challengers" and "VCT International"
        var tournament = getParameter(event, "tournament");
        if (tournament == null) {
            return Tournament.VCT_INTERNATIONAL;
        }
        return switch (tournament.toString().toLowerCase()) {
            case "game changers" -> Tournament.GAME_CHANGERS;
            case "vct challengers" -> Tournament.VCT_CHALLENGERS;
            default -> Tournament.VCT_INTERNATIONAL;
        };
    }

    private int getYear(Map<String, Object> event) {
        // The year of the tournament
        var year = getParameter(event, "year");
        if (year == null) {
            return 2024;
        }
        return Integer.parseInt(year);
    }

    private Role getRole(Map<String, Object> event) {
        // The role of the player. Options are "Duelist", "Initiator", "Controller" and "Sentinel"
        var role = getParameter(event, "role");
        if (role == null) {
            return null;
        }
        return switch (role.toLowerCase()) {
            case "duelist" -> Role.Duelist;
            case "initiator" -> Role.Initiator;
            case "controller" -> Role.Controller;
            default -> Role.Sentinel;
        };
    }

    @Override
    public ActionGroupFunctionResponseEvent handleRequest(Map<String, Object> event, Context context) {
        // event reference https://docs.aws.amazon.com/bedrock/latest/userguide/agents-lambda.html
        System.out.println(event);

        var functionName = event.get("function").toString();
        var responseText = "Function not found";
        switch (functionName) {
            case "get_team_composition":
                responseText = getTeamComposition(getTournament(event), getYear(event));
                break;
            case "get_players":
                responseText = getPlayers(getTournament(event), getRole(event));
                break;
            case "get_player_details":
                responseText = getPlayerDetails(event);
                break;
        }

        return new ActionGroupFunctionResponseEvent(
                "1.0",
                new ActionGroupResponse(
                        event.get("actionGroup").toString(),
                        event.get("function").toString(),
                        new FunctionResponse(
                                new FunctionResponseBody(
                                        new FunctionResponseTextBody(
                                                responseText
                                        )
                                )
                        )
                )
        );
    }

    public String getPlayerDetails(Map<String, Object> event) {
        var playerId = getParameter(event, "player_id");
        var playerName = getParameter(event, "player_name");
        if (playerId == null && playerName == null) {
            return "One of player_id or player_name is required";
        }

        var year = getYear(event);

        if (playerId != null) {
            return getPlayerDetailsById(Integer.parseInt(playerId), year);
        } else {
            return getPlayerDetailsByName(playerName, year);
        }
    }

    private String getPlayerDetailsByName(String playerName, int year) {
        var stmt = getQuery("player-details-by-search.sql");
        try {
            stmt.setString(1, playerName);
            stmt.setInt(2, year);

            return createPlayerDetailsResponse(stmt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String createPlayerDetailsResponse(PreparedStatement stmt) {
        StringBuilder playerDetails = new StringBuilder("| Player Id | Player Name | Player Handle | Player First Name | Player Last Name | Team Name | Team Region | Number of Wins |\n");
        playerDetails.append("| --- | --- | --- | --- | --- | --- | --- | --- |\n");
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                playerDetails
                        .append("| ")
                        .append(rs.getString("id"))
                        .append(" | ")
                        .append(rs.getString("name"))
                        .append(" | ")
                        .append(rs.getString("handle"))
                        .append(" | ")
                        .append(rs.getString("first_name"))
                        .append(" | ")
                        .append(rs.getString("last_name"))
                        .append(" | ")
                        .append(rs.getString("team_name"))
                        .append(" | ")
                        .append(rs.getString("region"))
                        .append(" | ")
                        .append(rs.getString("wins"))
                        .append(" |\n");
            }
            return playerDetails.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getPlayerDetailsById(int playerId, int year) {
        var stmt = getQuery("player-details-by-id.sql");
        try {
            stmt.setInt(1, playerId);
            stmt.setInt(2, year);

            return createPlayerDetailsResponse(stmt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getPlayers(Tournament tournament, Role role) {
        if (role == null) {
            return "Role is missing or not found";
        }
        if (tournament == null) {
            return "Tournament is missing or not found";
        }

        System.out.println("Role: " + role.getId());
        System.out.println("Tournament: " + tournament.getId());

        PreparedStatement stmt = getQuery("players-for-role.sql");

        List<String> players = new ArrayList<>();
        try {
            stmt.setInt(1, tournament.getId());
            stmt.setInt(2, role.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    players.add("| " + rs.getString("id") +
                            " | " + rs.getString("name") +
                            " | " + rs.getString("count") +
                            " | " + rs.getString("region") + " |");
                }
            }

            var playerStr = new StringBuilder("| Player ID | Player Name | Times Played | Region |\n");
            for (var player : players) {
                playerStr
                        .append(player)
                        .append("\n");
            }

            return playerStr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTeamComposition(Tournament tournament, int year) {
        PreparedStatement stmt = getQuery("team-composition.sql");
        List<TeamComposition> teamCompositions = new ArrayList<>();
        try {
            stmt.setInt(1, tournament.getId());
            stmt.setInt(2, year);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    var winning = rs.getString("winning_team_composition");
                    var losing = rs.getString("losing_team_composition");
                    teamCompositions.add(new TeamComposition(winning, losing));
                }

                var compositionStr = new StringBuilder("| Winning Team | Losing Team |\n");
                for (var teamComposition : teamCompositions) {
                    compositionStr
                            .append("| ")
                            .append(teamComposition.winningTeam())
                            .append(" | ")
                            .append(teamComposition.losingTeam())
                            .append(" |\n");
                }

                return compositionStr.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private PreparedStatement getQuery(String queryPath) {
        if (preparedStatementCache.containsKey(queryPath)) {
            return preparedStatementCache.get(queryPath);
        }
        try (var stream = this.getClass().getResourceAsStream("/db/queries/" + queryPath);
             var reader = new InputStreamReader(stream);
             var bufferedReader = new BufferedReader(reader)) {
            String query = bufferedReader.lines().collect(Collectors.joining("\n"));
            PreparedStatement stmt = conn.prepareStatement(query);
            preparedStatementCache.put(queryPath, stmt);
            return stmt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
