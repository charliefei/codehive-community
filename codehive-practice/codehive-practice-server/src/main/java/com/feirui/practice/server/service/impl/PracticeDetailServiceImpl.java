package com.feirui.practice.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.feirui.practice.api.enums.CompleteStatusEnum;
import com.feirui.practice.api.enums.IsDeletedFlagEnum;
import com.feirui.practice.api.enums.SubjectTypeEnum;
import com.feirui.practice.api.req.GetScoreDetailReq;
import com.feirui.practice.api.req.GetSubjectDetailReq;
import com.feirui.practice.api.req.SubmitPracticeDetailReq;
import com.feirui.practice.api.req.SubmitSubjectDetailReq;
import com.feirui.practice.api.vo.PracticeSubjectOptionVO;
import com.feirui.practice.api.vo.ScoreDetailVO;
import com.feirui.practice.api.vo.SubjectDetailVO;
import com.feirui.practice.server.dao.*;
import com.feirui.practice.server.entity.dto.SubjectDTO;
import com.feirui.practice.server.entity.dto.SubjectDetailDTO;
import com.feirui.practice.server.entity.dto.SubjectOptionDTO;
import com.feirui.practice.server.entity.po.*;
import com.feirui.practice.server.service.PracticeDetailService;
import com.feirui.practice.server.utils.DateUtils;
import com.feirui.practice.server.utils.LoginContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PracticeDetailServiceImpl implements PracticeDetailService {

    @Resource
    private PracticeDetailDao practiceDetailDao;
    @Resource
    private PracticeSetDao practiceSetDao;
    @Resource
    private PracticeSetDetailDao practiceSetDetailDao;
    @Resource
    private PracticeDao practiceDao;
    @Resource
    private SubjectDao subjectDao;
    @Resource
    private SubjectRadioDao subjectRadioDao;
    @Resource
    private SubjectMultipleDao subjectMultipleDao;
    @Resource
    private SubjectJudgeDao subjectJudgeDao;
    @Resource
    private SubjectMappingDao subjectMappingDao;
    @Resource
    private SubjectLabelDao subjectLabelDao;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Boolean submit(SubmitPracticeDetailReq req) {
        PracticePO practicePO = new PracticePO();
        Long practiceId = req.getPracticeId();
        Long setId = req.getSetId();
        practicePO.setSetId(setId);
        String timeUse = req.getTimeUse();
        String hour = timeUse.substring(0, 2);
        String minute = timeUse.substring(2, 4);
        String second = timeUse.substring(4, 6);
        practicePO.setTimeUse(hour + ":" + minute + ":" + second);
        practicePO.setSubmitTime(DateUtils.parseStrToDate(req.getSubmitTime()));
        practicePO.setCompleteStatus(CompleteStatusEnum.COMPLETE.getCode());
        practicePO.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        practicePO.setCreatedBy(LoginContextHolder.getLoginId());
        practicePO.setCreatedTime(new Date());
        // 计算正确率
        int correctCount = practiceDetailDao.selectCorrectCount(practiceId);
        List<PracticeSetDetailPO> practiceSetDetailPOS = practiceSetDetailDao.selectBySetId(setId);
        int totalCount = practiceSetDetailPOS.size();
        BigDecimal correctRate = new BigDecimal(correctCount)
                .divide(new BigDecimal(totalCount), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100.00"));
        practicePO.setCorrectRate(correctRate);
        PracticePO po = practiceDao.selectById(practiceId);
        if (Objects.isNull(po)) {
            practiceDao.insert(practicePO);
        } else {
            practicePO.setId(practiceId);
            practiceDao.update(practicePO);
        }
        practiceSetDao.updateHeat(setId);
        // 补充剩余题目的记录
        List<PracticeDetailPO> practiceDetailPOList = practiceDetailDao.selectByPracticeId(practiceId);
        List<PracticeSetDetailPO> minusList = practiceSetDetailPOS.stream()
                .filter(item -> !practiceDetailPOList.stream()
                        .map(PracticeDetailPO::getSubjectId)
                        .collect(Collectors.toList())
                        .contains(item.getSubjectId()))
                .collect(Collectors.toList());
        if (log.isInfoEnabled()) {
            log.info("题目差集{}", JSON.toJSONString(minusList));
        }
        if (CollectionUtils.isNotEmpty(minusList)) {
            minusList.forEach(e -> {
                PracticeDetailPO practiceDetailPO = new PracticeDetailPO();
                practiceDetailPO.setPracticeId(practiceId);
                practiceDetailPO.setSubjectType(e.getSubjectType());
                practiceDetailPO.setSubjectId(e.getSubjectId());
                practiceDetailPO.setAnswerStatus(0);
                practiceDetailPO.setAnswerContent("");
                practiceDetailPO.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
                practiceDetailPO.setCreatedTime(new Date());
                practiceDetailPO.setCreatedBy(LoginContextHolder.getLoginId());
                practiceDetailDao.insertSingle(practiceDetailPO);
            });
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean submitSubject(SubmitSubjectDetailReq req) {
        String timeUse = req.getTimeUse();
        if (timeUse.equals("0")) {
            timeUse = "000000";
        }
        String hour = timeUse.substring(0, 2);
        String minute = timeUse.substring(2, 4);
        String second = timeUse.substring(4, 6);
        PracticePO practicePO = new PracticePO();
        practicePO.setId(req.getPracticeId());
        practicePO.setTimeUse(hour + ":" + minute + ":" + second);
        practicePO.setSubmitTime(new Date());
        practiceDao.update(practicePO);

        PracticeDetailPO practiceDetailPO = new PracticeDetailPO();
        practiceDetailPO.setPracticeId(req.getPracticeId());
        practiceDetailPO.setSubjectId(req.getSubjectId());
        practiceDetailPO.setSubjectType(req.getSubjectType());
        String answerContent = "";
        // 排序答案
        if (CollectionUtils.isNotEmpty(req.getAnswerContents())) {
            List<Integer> answerContents = req.getAnswerContents();
            Collections.sort(answerContents);
            answerContent = StringUtils.join(answerContents, ",");
        }
        practiceDetailPO.setAnswerContent(answerContent);
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setSubjectId(req.getSubjectId());
        subjectDTO.setSubjectType(req.getSubjectType());
        // 获取正确答案，并判断答案是否正确
        SubjectDetailDTO subjectDetail = getSubjectDetail(subjectDTO);
        StringBuffer correctAnswer = new StringBuffer();
        if (req.getSubjectType().equals(SubjectTypeEnum.JUDGE.getCode())) {
            Integer isCorrect = subjectDetail.getIsCorrect();
            correctAnswer.append(isCorrect);
        } else {
            subjectDetail.getOptionList().forEach(e -> {
                if (Objects.equals(e.getIsCorrect(), 1)) {
                    correctAnswer.append(e.getOptionType()).append(",");
                }
            });
            if (correctAnswer.length() > 0) {
                correctAnswer.deleteCharAt(correctAnswer.length() - 1);
            }
        }
        if (Objects.equals(correctAnswer.toString(), answerContent)) {
            practiceDetailPO.setAnswerStatus(1);
        } else {
            practiceDetailPO.setAnswerStatus(0);
        }
        practiceDetailPO.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        practiceDetailPO.setCreatedBy(LoginContextHolder.getLoginId());
        practiceDetailPO.setCreatedTime(new Date());
        PracticeDetailPO existDetail = practiceDetailDao.selectDetail(req.getPracticeId(), req.getSubjectId(), LoginContextHolder.getLoginId());
        if (Objects.isNull(existDetail)) {
            practiceDetailDao.insertSingle(practiceDetailPO);
        } else {
            practiceDetailPO.setId(existDetail.getId());
            practiceDetailDao.update(practiceDetailPO);
        }
        return true;
    }

    public SubjectDetailDTO getSubjectDetail(SubjectDTO dto) {
        SubjectDetailDTO subjectDetailDTO = new SubjectDetailDTO();
        SubjectPO subjectPO = subjectDao.selectById(dto.getSubjectId());
        if (dto.getSubjectType() == SubjectTypeEnum.RADIO.getCode()) {
            List<SubjectOptionDTO> optionList = new LinkedList<>();
            List<SubjectRadioPO> radioSubjectPOS = subjectRadioDao.selectBySubjectId(subjectPO.getId());
            radioSubjectPOS.forEach(e -> {
                SubjectOptionDTO subjectOptionDTO = new SubjectOptionDTO();
                subjectOptionDTO.setOptionContent(e.getOptionContent());
                subjectOptionDTO.setOptionType(e.getOptionType());
                subjectOptionDTO.setIsCorrect(e.getIsCorrect());
                optionList.add(subjectOptionDTO);
            });
            subjectDetailDTO.setOptionList(optionList);
        }
        if (dto.getSubjectType() == SubjectTypeEnum.MULTIPLE.getCode()) {
            List<SubjectOptionDTO> optionList = new LinkedList<>();
            List<SubjectMultiplePO> multipleSubjectPOS = subjectMultipleDao.selectBySubjectId(subjectPO.getId());
            multipleSubjectPOS.forEach(e -> {
                SubjectOptionDTO subjectOptionDTO = new SubjectOptionDTO();
                subjectOptionDTO.setOptionContent(e.getOptionContent());
                subjectOptionDTO.setOptionType(e.getOptionType());
                subjectOptionDTO.setIsCorrect(e.getIsCorrect());
                optionList.add(subjectOptionDTO);
            });
            subjectDetailDTO.setOptionList(optionList);
        }
        if (dto.getSubjectType() == SubjectTypeEnum.JUDGE.getCode()) {
            SubjectJudgePO judgeSubjectPO = subjectJudgeDao.selectBySubjectId(subjectPO.getId());
            subjectDetailDTO.setIsCorrect(judgeSubjectPO.getIsCorrect());
        }
        subjectDetailDTO.setSubjectParse(subjectPO.getSubjectParse());
        subjectDetailDTO.setSubjectName(subjectPO.getSubjectName());
        return subjectDetailDTO;
    }

    @Override
    public List<ScoreDetailVO> getScoreDetail(GetScoreDetailReq req) {
        Long practiceId = req.getPracticeId();
        List<ScoreDetailVO> list = new LinkedList<>();
        List<PracticeDetailPO> practiceDetailPOList = practiceDetailDao.selectByPracticeId(practiceId);
        if (CollectionUtils.isEmpty(practiceDetailPOList)) {
            return Collections.emptyList();
        }
        practiceDetailPOList.forEach(po -> {
            ScoreDetailVO scoreDetailVO = new ScoreDetailVO();
            scoreDetailVO.setSubjectId(po.getSubjectId());
            scoreDetailVO.setSubjectType(po.getSubjectType());
            scoreDetailVO.setIsCorrect(po.getAnswerStatus());
            list.add(scoreDetailVO);
        });
        return list;
    }

    @Override
    public SubjectDetailVO getSubjectDetail(GetSubjectDetailReq req) {
        SubjectDetailVO subjectDetailVO = new SubjectDetailVO();
        Long subjectId = req.getSubjectId();
        Integer subjectType = req.getSubjectType();
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setSubjectId(subjectId);
        subjectDTO.setSubjectType(subjectType);
        SubjectDetailDTO subjectDetail = getSubjectDetail(subjectDTO);
        List<SubjectOptionDTO> optionList = subjectDetail.getOptionList();
        List<PracticeSubjectOptionVO> optionVOList = new LinkedList<>();
        List<Integer> correctAnswer = new LinkedList<>();
        if (CollectionUtils.isNotEmpty(optionList)) {
            optionList.forEach(option -> {
                PracticeSubjectOptionVO optionVO = new PracticeSubjectOptionVO();
                optionVO.setOptionType(option.getOptionType());
                optionVO.setOptionContent(option.getOptionContent());
                optionVO.setIsCorrect(option.getIsCorrect());
                optionVOList.add(optionVO);
                if (option.getIsCorrect() == 1) {
                    correctAnswer.add(option.getOptionType());
                }
            });
        }
        if (subjectType.equals(SubjectTypeEnum.JUDGE.getCode())) {
            Integer isCorrect = subjectDetail.getIsCorrect();
            PracticeSubjectOptionVO correctOption = new PracticeSubjectOptionVO();
            correctOption.setOptionType(1);
            correctOption.setOptionContent("正确");
            correctOption.setIsCorrect(isCorrect == 1 ? 1 : 0);
            PracticeSubjectOptionVO errorOptionVO = new PracticeSubjectOptionVO();
            errorOptionVO.setOptionType(2);
            errorOptionVO.setOptionContent("错误");
            errorOptionVO.setIsCorrect(isCorrect == 0 ? 1 : 0);
            optionVOList.add(correctOption);
            optionVOList.add(errorOptionVO);
            correctAnswer.add(subjectDetail.getIsCorrect());
        }
        subjectDetailVO.setOptionList(optionVOList);
        subjectDetailVO.setSubjectParse(subjectDetail.getSubjectParse());
        subjectDetailVO.setSubjectName(subjectDetail.getSubjectName());
        subjectDetailVO.setCorrectAnswer(correctAnswer);
        // 自己的答题答案
        List<Integer> respondAnswer = new LinkedList<>();
        PracticeDetailPO practiceDetailPO = practiceDetailDao.selectAnswer(req.getPracticeId(), subjectId);
        String answerContent = practiceDetailPO.getAnswerContent();
        if (StringUtils.isNotBlank(answerContent)) {
            String[] split = answerContent.split(",");
            for (String s : split) {
                respondAnswer.add(Integer.valueOf(s));
            }
        }
        subjectDetailVO.setRespondAnswer(respondAnswer);
        List<SubjectMappingPO> subjectMappingPOList = subjectMappingDao.getLabelIdsBySubjectId(subjectId);
        List<Long> labelIdList = new LinkedList<>();
        subjectMappingPOList.forEach(subjectMappingPO -> labelIdList.add(subjectMappingPO.getLabelId()));
        List<String> labelNameList = subjectLabelDao.getLabelNameByIds(labelIdList);
        subjectDetailVO.setLabelNames(labelNameList);
        return subjectDetailVO;
    }

}