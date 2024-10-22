SELECT p.id, p.name, p.HANDLE, p.FIRST_NAME, p.LAST_NAME, t.name as team_name, t.REGION, count(*) as wins
FROM FT_SEARCH_DATA(?, 0, 0) ft,
     player p,
     selected_agent sa,
     game g,
     team t,
     PLAYER_TEAM pt
WHERE ft."TABLE" = 'PLAYER'
  AND p.id = FT."KEYS"[1]
  and p.id = sa.player_id
  and sa.game_id = g.id
  and g.winning_team_id = t.id
  and t.id = pt.team_id
  and pt.player_id = p.id
  and g.GAME_YEAR = ?
group by p.id, t.id;