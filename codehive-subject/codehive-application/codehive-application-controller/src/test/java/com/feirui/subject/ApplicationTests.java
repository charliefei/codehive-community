package com.feirui.subject;

import com.feirui.subject.domain.bo.SubjectLabelBO;
import com.feirui.subject.domain.convert.SubjectLabelConverter;
import com.feirui.subject.infra.basic.entity.SubjectLabel;

import java.util.Arrays;
import java.util.List;

public class ApplicationTests {

    public static void main(String[] args) {
        SubjectLabel label1 = new SubjectLabel();
        label1.setId(1L);
        label1.setLabelName("a");
        label1.setCategoryId(1L);
        SubjectLabel label2 = new SubjectLabel();
        label2.setId(2L);
        label2.setLabelName("b");
        label2.setCategoryId(2L);
        List<SubjectLabel> labelList = Arrays.asList(label1, label2);
        List<SubjectLabelBO> labelBOList = SubjectLabelConverter.INSTANCE.convert(labelList);
        System.out.println(labelBOList);
    }

}
