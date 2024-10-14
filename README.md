It has been a while since I've written code in Java, so I wanted to refresh my memory by joining the [VCT Hackathon](https://vcthackathon.devpost.com/).

# Tech 

Currently there is no much here yet. I wanted to test out some new things or refresh my memory on some old things.

- Gradle
- Spring Boot
- Spring Data JDBC
- Spring Batch (maybe)
- DuckDB ❌
- Cursor IDE ❌
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
I was unable to increase some of the font sizes and colors. There is liberal use of small gray on black text that my eyes are having a 
really hard time with.

Update: After a week of consistently using Cursor, I gave up and returned to IntelliJ IDEA. There were just so many small
things. For instance, it didn't automatically import classes when it could, it tried to intend in very weird ways, and it
had autocomplete suggestions that would simply not compile (the basics, such as trying to use a method that doesn't exist).

## HTMX

I had heard about HTMX through various people and articles. I understand the attraction. Not having to write JavaScript is a big plus.
However, this comes with limitations and it looks like it may force you into crafting complex code just to get around those limitations.


# Goal

Just to get me going, I decided on this first use case:

Extract a list of all matches played and the composition of the teams (the roles).
This is going to be the input for a chatbot that will decide the best team composition.

# Notes

## Running on EC2

?? I decided to skip running on EC2. The data is not that big and the processing is not that heavy. I can run it fine on my machine.

With regard to permissions, it is easiest to give your EC2 instance a role that has read access to S3.

Adoptium / Temuring on Amazon Linux is a manual process:

    wget https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.4%2B7/OpenJDK21U-jdk_x64_linux_hotspot_21.0.4_7.tar.gz
    tar xzf OpenJDK21U-jdk_x64_linux_hotspot_21.0.4_7.tar.gz
    sudo mv jdk-21.0.4+7/ /opt/jdk-21

Install Gradle:

    wget https://services.gradle.org/distributions/gradle-8.10.2-bin.zip
    sudo mkdir /opt/gradle
    sudo unzip -d /opt/gradle gradle-8.10.2-bin.zip

Add the following to your `.bashrc`:

    export JAVA_HOME=/opt/jdk-21
    export PATH=$JAVA_HOME/bin:/opt/gradle/gradle-8.10.2/bin:$PATH

You may want to update the Java alternatives, but I did not bother for now.
[Here are more instructions](https://techviewleo.com/how-to-install-temurin-openjdk-on-amazon-linux/)

## Building and Deploying the Lambda

This should probably be automated a bit more, but this is how it works right now:

1. Build the jar and zip file: `gradle buildLambda`
2. Upload the zip file to AWS lambda

Note that there are two layers that need to be added to the lambda:

1. one layer with all the dependencies: `gradle buildLambdaDependencyLayer`
2. one with the H2 database file: `gradle buildLambdaDataLayer` (You may need to close any open connections to the database)

Note: For the agent to call the lambda, permission needs to be set on the Lambda. It looks like this is currently not possible
through the console, as the console will ask for an ARN, which is not available for the agent. You can set the permission
via the CLI:
    
    aws lambda add-permission --function-name "lambda-arn" --statement-id "vcth-agent-access" --action "lambda:InvokeFunction" --principal "bedrock.amazonaws.com"

There is a `Dockerfile` to test the changes locally:

1. Build the jar: `gradle buildLambdaJar`
2. Build the image: `docker build -t valhack-lambda .`
3. Run the image: `docker run -p 9000:8080 valhack-lambda:latest`

