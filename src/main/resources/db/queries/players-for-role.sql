select player.id,
       name,
       count(*)  as count,
       (select region
        from TEAM
                 join player_team on TEAM.ID = PLAYER_TEAM.TEAM_ID
        where TEAM.ID = PLAYER_TEAM.TEAM_ID
          and PLAYER_TEAM.PLAYER_ID = PLAYER.ID
        limit 1) as region
from PLAYER
         join selected_agent on PLAYER.ID = selected_agent.PLAYER_ID
         join GAME on (selected_agent.GAME_ID = GAME.ID and GAME.TOURNAMENT_ID = ?)
         join agent on (selected_agent.AGENT_ID = AGENT.ID and AGENT.ROLE_ID = ?)
group by PLAYER.ID
order by count(*) desc
limit 50;