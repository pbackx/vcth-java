package com.peated.valhack.config;

import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Configuration
public class AwsRegionProvider implements software.amazon.awssdk.regions.providers.AwsRegionProvider {
    @Override
    public Region getRegion() {
        return Region.US_WEST_2;
    }
}
