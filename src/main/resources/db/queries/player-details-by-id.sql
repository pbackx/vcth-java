SELECT p.id, p.name, p.HANDLE, p.FIRST_NAME, p.LAST_NAME, t.name as team_name, t.REGION, count(*) as wins
FROM player p,
     selected_agent sa,
     game g,
     team t,
     PLAYER_TEAM pt
WHERE p.id = ?
  and p.id = sa.player_id
  and sa.game_id = g.id
  and g.winning_team_id = t.id
  and t.id = pt.team_id
  and pt.player_id = p.id
  and g.GAME_YEAR = ?
group by p.id, t.id;