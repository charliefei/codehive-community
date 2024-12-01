package com.feirui.subject.infra.basic.service.impl;

import com.feirui.subject.common.entity.PageResult;
import com.feirui.subject.common.enums.SubjectTypeEnum;
import com.feirui.subject.infra.basic.entity.EsSubjectFields;
import com.feirui.subject.infra.basic.entity.SubjectInfoEs;
import com.feirui.subject.infra.basic.es.*;
import com.feirui.subject.infra.basic.service.SubjectEsService;
import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class SubjectEsServiceImpl implements SubjectEsService {

    @Resource
    private EsConfigProperties esConfigProperties;

    @Override
    public boolean insert(SubjectInfoEs subjectInfoEs) {
        EsSourceData esSourceData = new EsSourceData();
        Map<String, Object> data = convert2EsSourceData(subjectInfoEs);
        esSourceData.setDocId(subjectInfoEs.getDocId().toString());
        esSourceData.setData(data);
        return EsRestClient.insertDoc(getEsIndexInfo(), esSourceData);
    }

    @Override
    public PageResult<SubjectInfoEs> querySubjectList(SubjectInfoEs req) {
        PageResult<SubjectInfoEs> pageResult = new PageResult<>();
        EsSearchRequest searchRequest = buildEsSearchRequest(req);
        SearchResponse searchResponse = EsRestClient.searchWithTermQuery(getEsIndexInfo(), searchRequest);
        SearchHits searchHits = searchResponse.getHits();

        if (searchHits == null || searchHits.getHits() == null) {
            pageResult.setPageNo(req.getPageNo());
            pageResult.setPageSize(req.getPageSize());
            pageResult.setTotal(0);
            return pageResult;
        }

        List<SubjectInfoEs> list = new LinkedList<>();
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            SubjectInfoEs subjectInfoEs = convert2SubjectInfoEs(hit);
            if (Objects.nonNull(subjectInfoEs)) {
                list.add(subjectInfoEs);
            }
        }

        pageResult.setPageNo(req.getPageNo());
        pageResult.setPageSize(req.getPageSize());
        pageResult.setTotal((int) searchHits.getTotalHits().value);
        pageResult.setRecords(list);
        return pageResult;
    }

    private SubjectInfoEs convert2SubjectInfoEs(SearchHit hit) {
        Map<String, Object> sourceMap = hit.getSourceAsMap();
        if (CollectionUtils.isEmpty(sourceMap)) {
            return null;
        }
        SubjectInfoEs subjectInfoEs = new SubjectInfoEs();
        subjectInfoEs.setDocId(MapUtils.getLong(sourceMap, EsSubjectFields.DOC_ID));
        subjectInfoEs.setSubjectId(MapUtils.getLong(sourceMap, EsSubjectFields.SUBJECT_ID));
        subjectInfoEs.setSubjectName(MapUtils.getString(sourceMap, EsSubjectFields.SUBJECT_NAME));
        subjectInfoEs.setSubjectAnswer(MapUtils.getString(sourceMap, EsSubjectFields.SUBJECT_ANSWER));
        subjectInfoEs.setSubjectType(MapUtils.getInteger(sourceMap, EsSubjectFields.SUBJECT_TYPE));
        subjectInfoEs.setScore(new BigDecimal(String.valueOf(hit.getScore()))
                .multiply(new BigDecimal("100.00").setScale(2, RoundingMode.HALF_UP)));

        // 高亮字段：SUBJECT_NAME & SUBJECT_ANSWER，设置上高亮标签
        String subjectNameHighlight = getHighLightFieldStr(hit, EsSubjectFields.SUBJECT_NAME);
        String subjectAnswerHighlight = getHighLightFieldStr(hit, EsSubjectFields.SUBJECT_ANSWER);
        if (StringUtils.hasLength(subjectNameHighlight)) {
            subjectInfoEs.setSubjectName(subjectNameHighlight);
        }
        if (StringUtils.hasLength(subjectAnswerHighlight)) {
            subjectInfoEs.setSubjectAnswer(subjectAnswerHighlight);
        }
        return subjectInfoEs;
    }

    private String getHighLightFieldStr(SearchHit hit, String fieldName) {
        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        HighlightField highlightField = highlightFields.get(fieldName);
        StringBuilder builder = new StringBuilder();
        if (Objects.nonNull(highlightField)) {
            Text[] fragments = highlightField.getFragments();
            for (Text fragment : fragments) {
                builder.append(fragment);
            }
        }
        return builder.toString();
    }

    private EsSearchRequest buildEsSearchRequest(SubjectInfoEs req) {
        EsSearchRequest esSearchRequest = new EsSearchRequest();

        // 构造bool查询条件
        BoolQueryBuilder bq = new BoolQueryBuilder();
        MatchQueryBuilder subjectTypeMatchQuery = QueryBuilders
                .matchQuery(EsSubjectFields.SUBJECT_TYPE, SubjectTypeEnum.BRIEF.getCode());
        // 必须命中该条件
        bq.must(subjectTypeMatchQuery);
        MatchQueryBuilder subjectNameMatchQuery = QueryBuilders
                .matchQuery(EsSubjectFields.SUBJECT_NAME, req.getKeyWord());
        subjectNameMatchQuery.boost(2);
        MatchQueryBuilder subjectAnswerMatchQuery = QueryBuilders
                .matchQuery(EsSubjectFields.SUBJECT_ANSWER, req.getKeyWord());
        bq.should(subjectNameMatchQuery);
        bq.should(subjectAnswerMatchQuery);
        // 最少命中一个条件
        bq.minimumShouldMatch(1);

        // 构造高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");

        esSearchRequest.setBq(bq);
        esSearchRequest.setHighlightBuilder(highlightBuilder);
        esSearchRequest.setFields(EsSubjectFields.FIELD_QUERY);
        esSearchRequest.setFrom((req.getPageNo() - 1) * req.getPageSize());
        esSearchRequest.setSize(req.getPageSize());
        esSearchRequest.setNeedScroll(false);
        return esSearchRequest;
    }

    private Map<String, Object> convert2EsSourceData(SubjectInfoEs subjectInfoEs) {
        Map<String, Object> data = new HashMap<>();
        data.put(EsSubjectFields.SUBJECT_ID, subjectInfoEs.getSubjectId());
        data.put(EsSubjectFields.DOC_ID, subjectInfoEs.getDocId());
        data.put(EsSubjectFields.SUBJECT_NAME, subjectInfoEs.getSubjectName());
        data.put(EsSubjectFields.SUBJECT_ANSWER, subjectInfoEs.getSubjectAnswer());
        data.put(EsSubjectFields.SUBJECT_TYPE, subjectInfoEs.getSubjectType());
        data.put(EsSubjectFields.CREATE_USER, subjectInfoEs.getCreateUser());
        data.put(EsSubjectFields.CREATE_TIME, subjectInfoEs.getCreateTime());
        return data;
    }

    private EsIndexInfo getEsIndexInfo() {
        EsIndexInfo esIndexInfo = new EsIndexInfo();
        // TODO 仅仅只有条件部署单节点的es
        esIndexInfo.setClusterName(esConfigProperties.getEsConfigs().get(0).getName());
        // TODO 事先在es创建好该索引
        esIndexInfo.setIndexName("subject_index");
        return esIndexInfo;
    }

}
