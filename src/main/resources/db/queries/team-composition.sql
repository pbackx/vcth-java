SELECT ID    AS game_id,
       (
           SELECT STRING_AGG(ROLES.NAME, ',')
           FROM PLAYER_TEAM
                    JOIN SELECTED_AGENT
                         ON PLAYER_TEAM.player_id = SELECTED_AGENT.player_id
                             AND SELECTED_AGENT.GAME_ID = GAME.ID
                    JOIN AGENT
                         ON SELECTED_AGENT.agent_id = AGENT.ID
                    JOIN ROLES
                         ON AGENT.ROLE_ID = ROLES.ID
           WHERE team_id = GAME.WINNING_TEAM_ID
           GROUP BY TEAM_ID
           ) AS winning_team_composition,
       (
           SELECT STRING_AGG(ROLES.NAME, ',')
           FROM PLAYER_TEAM
                    JOIN SELECTED_AGENT
                         ON PLAYER_TEAM.player_id = SELECTED_AGENT.player_id
                             AND SELECTED_AGENT.GAME_ID = GAME.ID
                    JOIN AGENT
                         ON SELECTED_AGENT.agent_id = AGENT.ID
                    JOIN ROLES
                         ON AGENT.ROLE_ID = ROLES.ID
           WHERE team_id = GAME.LOSING_TEAM_ID
           GROUP BY TEAM_ID
           ) AS losing_team_composition
FROM GAME
WHERE TOURNAMENT_ID = 2
LIMIT 50;