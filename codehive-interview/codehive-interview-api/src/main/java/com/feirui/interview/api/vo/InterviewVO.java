package com.feirui.interview.api.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class InterviewVO implements Serializable {

    private List<Interview> questionList;

    private String pdfText;

    @Data
    public static class Interview {
        private String keyWord;
        private Long categoryId;
        private Long labelId;
    }

}
