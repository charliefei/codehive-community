package com.feirui.ai.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    private String model;
    private List<Message> messages;
    private ResponseFormat response_format;
    private Object stop;
    private Double temperature;
    private Integer max_tokens;
    private Boolean stream;
    private StreamOptions stream_options;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseFormat {
        /**
         * text | json_object
         */
        private String type;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StreamOptions {
        private Boolean include_usage;
    }

}

