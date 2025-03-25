package com.feirui.ai.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ChatResponse implements Serializable {

    private String id;

    private String object;

    private Integer created;

    private String model;

    private List<Choices> choices;

    private Usage usage;

    private String system_fingerprint;

    @Data
    public static class Choices {

        private Integer index;

        /**
         * 流式
         */
        private Delta delta;

        /**
         * 非流式
         */
        private Message message;

        private String logprobs;

        // stop, length, content_filter, tool_calls, insufficient_system_resource
        private String finish_reason;

        @Data
        public static class Message {
            private String role;
            private String content;
            private String reasoning_content;
        }

        @Data
        public static class Delta {
            private String role;
            private String content;
            private String reasoning_content;
        }
    }

    @Data
    public static class Usage {
        private Integer prompt_tokens;

        private Integer completion_tokens;

        private Integer total_tokens;

        private Prompt_tokens_details prompt_tokens_details;

        private Integer prompt_cache_hit_tokens;

        private Integer prompt_cache_miss_tokens;

        @Data
        public static class Prompt_tokens_details {
            private Integer cached_tokens;
        }
    }
}
