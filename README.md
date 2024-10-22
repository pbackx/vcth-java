It has been a while since I've written code in Java, so I wanted to refresh my memory by joining the [VCT Hackathon](https://vcthackathon.devpost.com/).

# Tech 

Currently there is no much here yet. I wanted to test out some new things or refresh my memory on some old things.

- Gradle
- Spring Boot
- Spring Data JDBC
- H2
- DuckDB ❌
- Cursor IDE ❌
- Thymeleaf
- HTMX
- AWS Lambda, Bedrock agents and Elastic Beanstalk

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
However, I do feel moving everything (or at least, most of it) to attributes could also be a slippery slope.

Overall, I think HTMX helped me in quickly whipping up a somewhat functional UI. I was able to pretty much copy paste
free templates and examples from the Internet with very few modifications. This is not something I would have been able
to do with Angular or React.

## H2

Another good choice I would like to mention is the H2 database. It is a very lightweight database that can run in memory
or store its data in one file. Since, after processing, there really isn't that much data (about 20 MB) and it is a read-only
database, this made it possible to simply copy and paste the database wherever it was needed.

Everything just worked and H2 even has built-in support for full text search. It's obviously not a fully fledged vector search,
but for a project that needed to be developed in a month, it was the right balance between power and simplicity.

## AWS Elastic Beanstalk

A final technology I would give a mention is AWS Elastic Beanstalk. It was my first time using it, but apart from some
minor hickups, it was a matter of building the Spring Boot runnable JAR and uploading it to Elastic Beanstalk.

The way I currently understand AWS EB (and I could be very wrong) is that it is a very fancy wrapper around a number of
AWS services. It creates a CloudFormation stack and manages the configuration for you.

One could say that you could do this on your own and make it more flexible, more scalable, cheaper, etc. But for a small
project that only needs to run a month or two, this just saves so much time and effort.

# Goal

While I initially only focused on collecting data for the first basic prompts, I now support all the use cases mentioned
on the [VCT Hackathon submission specifics](https://vcthackathon.devpost.com/details/submissionspecifics) page.

# Improvements

As is always the case with these projects, here are some improvements I would start with, but there are many more:

- Use a vector store to supply the agent with additional Valorant information from the wiki. Right now, I am just counting 
  on the foundational model to have enough knowledge to answer the questions. From my limited testing, it seems to be doing
  just fine, but more info will obviously help.
- Every time I update the data model/database, I basically rerun the full processing. Since the runtime is not very long
  (a few hours) this is not a big deal, but it was holding me back in the end from making some final improvements.
- The agent response is only shown after the full answer is received. Streaming partial answers or maybe even progress
  would make the UI much more user-friendly.

# Notes

## Running on EC2

I decided to skip running on EC2. The data is not that big and the processing is not that heavy. I can run it fine on my machine.

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

