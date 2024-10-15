select player.id, name, count(*) as count
from PLAYER
         join selected_agent on PLAYER.ID = selected_agent.PLAYER_ID
         join GAME on (selected_agent.GAME_ID = GAME.ID and GAME.TOURNAMENT_ID = ?)
         join agent on (selected_agent.AGENT_ID = AGENT.ID and AGENT.ROLE_ID = ?)
group by PLAYER.ID
order by count(*) desc
limit 50;