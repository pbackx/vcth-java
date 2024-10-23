package com.peated.valhack.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentRequest;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentResponseHandler;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Service
public class BedrockAgentService {
    private final BedrockAgentRuntimeAsyncClient bedrockAgentRuntimeClient;

    public BedrockAgentService() {
        var config = ClientOverrideConfiguration.builder()
                .retryStrategy(r -> r.maxAttempts(1))
                .build();
        var httpClient = NettyNioAsyncHttpClient.builder()
                .readTimeout(Duration.ofSeconds(900))
                .writeTimeout(Duration.ofSeconds(900))
                .connectionAcquisitionTimeout(Duration.ofSeconds(900))
                .build();
        this.bedrockAgentRuntimeClient = BedrockAgentRuntimeAsyncClient.builder()
                .overrideConfiguration(config)
                .httpClient(httpClient)
                .region(Region.US_EAST_1)
                .build();
    }

    public String getResponse(String message, String session) {
        var finalCompletion = new AtomicReference<>("");

        Consumer<InvokeAgentRequest.Builder> request = (InvokeAgentRequest.Builder builder) -> builder
                .agentId("XE4ZTBU4MR")
                .agentAliasId("UG5IN91VIJ") // can also use TSTALIASID for testing https://docs.aws.amazon.com/bedrock/latest/userguide/agents-test.html
                .inputText(message)
                .sessionId(session);

        var visitor = InvokeAgentResponseHandler.Visitor.builder()
                .onChunk(chunk -> finalCompletion.set(finalCompletion.get() + chunk.bytes().asUtf8String()))
                .build();

        var handler = InvokeAgentResponseHandler.builder()
                .subscriber(visitor)
                .onError(e -> finalCompletion.set(
                        "I am sorry, but I can not answer your question at the moment.\n" +
                                "The agent returned the following error:\n" +
                                e.getMessage())
                )
                .build();

        try {
            bedrockAgentRuntimeClient.invokeAgent(
                    request,
                    handler
            ).join();
        } catch (Exception e) {
            finalCompletion.set(
                    "I am sorry, but I can not answer your question at the moment.\n" +
                            "The agent returned the following error:\n" +
                            e.getMessage());
        }

        return finalCompletion.get();
    }
}
