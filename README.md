It has been a while since I've written code in Java, so I wanted to refresh my memory by joining the [VCT Hackathon](https://vcthackathon.devpost.com/).

# Tech 

Currently there is no much here yet. I wanted to test out some new things or refresh my memory on some old things.

- Gradle
- Spring Boot
- Spring Data JDBC
- Spring Batch (maybe)
- DuckDB ❌
- Cursor IDE
- Thymeleaf
- HTMX

## DuckDB

I tried to integrate DuckDB, but for some reason it did not want to cooperate with Spring Data JDBC. For the record, this is what I tried:

- I implemented a custom Dialect, which was just an extention of the `PostgresDialect`
- This seemed to mostly work, but for some reason the DuckDB driver was complaining that prepared statements were not supported.

Anyway, you may be wondering why I didn't import the JSON directly into DuckDB. I tried that, but the resulting structure is so clunky, I
think the resulting SQL would be an unreadable and unmaintainable nightmare.

## Cursor IDE

My regular IDE is IntelliJ IDEA, and I've been using it for a long time for pretty much any programming language or framework.

I totally get what Cursor is trying to do and I like it. With Github Copilot in IntelliJ IDEA, you can get very close to the same experience,
but not completely. AI is just better integrated into Cursor.

Cursor is definitely missing some things that I like about IntelliJ IDEA. Most importantly, configuring Cursor is a nightmare. For instance,
I was unable to increase some of the font sizes and colors. There is libaral use of small gray on black text that my eyes are having a 
really hard time with.

# Goal

Just to get me going, I decided on this first use case:

Extract a list of all matches played and the composition of the teams (the roles).
This is going to be the input for a chatbot that will decide the best team composition.
