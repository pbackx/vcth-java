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

        var request = InvokeAgentRequest.builder()
                .agentId("XE4ZTBU4MR")
                .agentAliasId("0KKNAR1ENB") // can also use TSTALIASID for testing https://docs.aws.amazon.com/bedrock/latest/userguide/agents-test.html
                .inputText(message)
                .sessionId(session)
                .build();

        var visitor = InvokeAgentResponseHandler.Visitor.builder()
                .onChunk(chunk -> finalCompletion.set(finalCompletion.get() + chunk.bytes().asUtf8String()))
                .build();

        var handler = InvokeAgentResponseHandler.builder()
                .subscriber(visitor)
                .build();

        bedrockAgentRuntimeClient.invokeAgent(
                request,
                handler
        ).join();

        return finalCompletion.get();
    }
}
