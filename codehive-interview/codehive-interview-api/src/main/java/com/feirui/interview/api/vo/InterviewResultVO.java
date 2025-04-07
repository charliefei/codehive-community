package com.feirui.interview.api.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class InterviewResultVO implements Serializable {

    private Double avgScore;

    private String tips;

    private String avgTips;

    private List<InterviewResult> resp;

    @Data
    public static class InterviewResult {
        private Integer userScore;
        private String feeling;
        private String subjectAnswer;
        private String suggestion;
    }

}
