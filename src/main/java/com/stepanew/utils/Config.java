package com.stepanew.utils;

import lombok.Data;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

@Data
public class Config {

    private GraphConfig graph;

    private AlgorithmConfig algorithm;

    @Data
    public static class GraphConfig {

        private String yaml;

        private PngConfig png;

        @Data
        public static class PngConfig {

            private String path;

            private SuffixConfig suffix;

            @Data
            public static class SuffixConfig {

                private String before;

                private String after;

            }
        }
    }

    @Data
    public static class AlgorithmConfig {

        private int populationSize;

        private int generationSize;

        private double mutationRate;

        private int logRate;

    }

    public static Config load(String configPath) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(configPath)) {
            return yaml.loadAs(inputStream, Config.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
}
