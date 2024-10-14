package com.peated.valhack.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.peated.valhack.lambda.messages.*;
import com.peated.valhack.model.Tournament;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LambdaHandler implements RequestHandler<Map<String, Object>, ActionGroupFunctionResponseEvent> {
    private final Connection conn;

    public LambdaHandler() throws Exception {
        Class.forName("org.h2.Driver");
        String url = "jdbc:h2:file:/opt/valhack;ACCESS_MODE_DATA=r";
        conn = DriverManager.getConnection(url);
    }

    @Override
    public ActionGroupFunctionResponseEvent handleRequest(Map<String, Object> event, Context context) {
        // event reference https://docs.aws.amazon.com/bedrock/latest/userguide/agents-lambda.html
        System.out.println(event);

        var composition = getTeamComposition(Tournament.VCT_CHALLENGERS, 2024);

        var compositionStr = new StringBuilder("| Winning Team | Losing Team |\n");
        for (var teamComposition : composition) {
            compositionStr
                    .append("| ")
                    .append(teamComposition.winningTeam())
                    .append(" | ")
                    .append(teamComposition.losingTeam())
                    .append(" |\n");
        }

        return new ActionGroupFunctionResponseEvent(
                "1.0",
                new ActionGroupResponse(
                        event.get("actionGroup").toString(),
                        event.get("function").toString(),
                        new FunctionResponse(
                                new FunctionResponseBody(
                                        new FunctionResponseTextBody(
                                                compositionStr.toString()
                                        )
                                )
                        )
                )
        );
    }

    public List<TeamComposition> getTeamComposition(Tournament tournament, int year) {
        try (var stream = this.getClass().getResourceAsStream("/db/queries/team-composition.sql");
             var reader = new InputStreamReader(stream);
             var bufferedReader = new BufferedReader(reader)) {
            String query = bufferedReader.lines().collect(Collectors.joining("\n"));

            List<TeamComposition> teamComposition = new ArrayList<>();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    var winning = rs.getString("winning_team_composition");
                    var losing = rs.getString("losing_team_composition");
                    teamComposition.add(new TeamComposition(winning, losing));
                }

                return teamComposition;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
//            String query = "select tournament_id, count(*) as count from GAME group by tournament_id";
//
//                 Statement stmt = conn.createStatement();
//                 ResultSet rs = stmt.executeQuery(query)) {
//
//                while (rs.next()) {
//                    int tournamentId = rs.getInt("tournament_id");
//                    int count = rs.getInt("count");
//                    tournamentCounts.put(tournamentId, count);
//                }
//
//            return tournamentCounts;
//    }
}
