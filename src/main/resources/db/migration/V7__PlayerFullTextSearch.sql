-- H2 has built-in support for full text search:
-- https://www.h2database.com/html/tutorial.html#fulltext

CREATE ALIAS IF NOT EXISTS FT_INIT FOR "org.h2.fulltext.FullText.init";
CALL FT_INIT();

CALL FT_CREATE_INDEX('PUBLIC', 'PLAYER', 'NAME, HANDLE, FIRST_NAME, LAST_NAME');
