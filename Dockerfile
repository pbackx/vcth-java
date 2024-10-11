# Use the Java 21 AWS Lambda runtime
FROM amazon/aws-lambda-java:21

# Copy the built jar into the Lambda container
COPY build/libs/vcth-lambda-0.0.1-SNAPSHOT.jar ${LAMBDA_TASK_ROOT}/lib/
COPY valhack.mv.db /opt/
COPY build/dependency ${LAMBDA_TASK_ROOT}/lib/

# Set the Lambda function handler
CMD ["com.peated.valhack.lambda.LambdaHandler::handleRequest"]
