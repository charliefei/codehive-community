package com.feirui.interview.server;

import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.feirui.ai.domain.ChatRequest;
import com.feirui.ai.service.DeepSeekService;
import com.feirui.interview.api.vo.InterviewQuestionVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import static com.feirui.ai.config.PresetPrompts.*;

@SpringBootTest
@Slf4j
public class ApplicationTest {

    private static final ExecutorService executor = Executors.newFixedThreadPool(8);
    private static final Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
    private static final String user_query2 = "面试题：请详细描述你在JVM调优方面的经验，特别是解决fullgc问题的具体步骤和方法论；用户回答：在解决fullgc问题时，我首先会通过jstat、jmap等工具监控GC情况，分析GC日志确定问题类型。然后根据内存使用情况调整堆大小、新生代/老年代比例等参数。对于频繁fullgc，我会检查内存泄漏，使用MAT分析堆转储。最终沉淀的方法论包括：1)建立监控体系 2)定期分析GC日志 3)合理设置内存参数 4)优化对象生命周期 5)选择合适GC算法";
    private static final String user_query3 = "面试题：请详细描述你在JVM调优方面的经验，特别是解决fullgc问题的具体步骤和方法论；用户回答：在解决fullgc问题时，我首先会通过jstat、jmap等工具监控GC情况，分析GC日志确定问题类型。然后根据内存使用情况调整堆大小、新生代/老年代比例等参数。对于频繁fullgc，我会检查内存泄漏，使用MAT分析堆转储。最终沉淀的方法论包括：1)建立监控体系 2)定期分析GC日志 3)合理设置内存参数 4)优化对象生命周期 5)选择合适GC算法\n" +
            "面试题：在鸡翅Club项目中，你如何实现微服务间的鉴权和会话共享？为什么选择Satoken而不是Spring Security; 用户回答：鉴权方案：1)网关层统一拦截校验token 2)Redis存储会话信息实现共享 3)Feign拦截器传递上下文。选择Satoken因为：1)API更简洁 2)学习成本低 3)开箱即用的分布式会话 4)更灵活的权限控制 5)更好的文档和社区支持。具体实现：1)网关校验token有效性 2)将用户信息存入ThreadLocal 3)通过Redis存储会话数据 4)自定义过滤器处理异常情况\n" +
            "面试题：在Ape-Frame脚手架中，你是如何设计common-redis模块的？特别是分布式锁的实现方案; 用户回答：common-redis模块采用分层设计：1)基础层封装RedisTemplate 2)工具层提供分布式锁、本地缓存等 3)扩展层支持Lua脚本和pipeline。分布式锁实现基于Redisson，支持可重入、自动续期和看门狗机制。针对不同场景提供多种锁：1)普通互斥锁 2)读写锁 3)公平锁 4)联锁。同时封装了简洁的API，支持tryLock和leaseTime设置，并处理了锁续期和异常释放问题\n"+
            "面试题：你提到使用工厂+策略模式解耦微信消息处理，能具体说明实现方式吗？; 用户回答：实现方案：1)定义消息类型枚举和消息处理器接口 2)工厂类维护类型与处理器的映射关系 3)每种消息类型实现独立策略类。具体代码：1)MessageHandler接口定义handle方法 2)MessageFactory根据消息类型返回对应处理器 3)TextHandler、ImageHandler等实现具体逻辑。优点：1)新增消息类型只需添加新策略 2)各处理逻辑隔离 3)便于单元测试 4)符合开闭原则\n"+
            "面试题：在性能优化方面，你如何通过CompletableFuture提升分类标签查询性能80%？; 用户回答：优化方案：1)分析原有串行查询瓶颈 2)将无依赖的查询任务拆分为多个子任务 3)使用CompletableFuture.supplyAsync并行执行 4)通过thenCombine合并结果。关键点：1)合理设置线程池大小 2)处理异常情况 3)控制超时时间 4)避免线程饥饿。具体实现：1)标签查询、分类查询等并行执行 2)使用自定义线程工厂区分日志 3)最终通过allOf等待所有任务完成\n"+
            "面试题：请详细说明你封装的ESClient如何支持多集群和多索引切换; 用户回答：ESClient设计：1)抽象集群管理器维护多个RestHighLevelClient实例 2)通过上下文持有当前集群标识 3)模板方法封装常用操作。多集群支持：1)配置文件定义集群连接信息 2)动态切换时更新上下文 3)连接池统一管理。多索引支持：1)定义索引元数据 2)操作时自动路由 3)支持别名机制。扩展功能：1)高亮查询封装 2)聚合结果解析 3)批量操作优化 4)异常统一处理\n"+
            "面试题：在代码生成器组件中，你是如何实现从Controller到Dao的全套代码生成的？; 用户回答：实现方案：1)元数据建模获取表结构 2)FreeMarker模板引擎 3)分层生成策略。具体步骤：1)解析数据库元数据 2)生成领域模型 3)按模板生成各层代码 4)处理关联关系。优化点：1)支持自定义模板 2)增量生成 3)生成单元测试 4)集成Lombok 5)生成Swagger注解。技术栈：1)Spring Boot Starter 2)DatabaseMetaData 3)模板引擎 4)JavaPoet 5)自定义注解\n"+
            "面试题：你如何进行线程池的压测和参数调优？探索出的线程池与CPU关系公式是什么？; 用户回答：压测方法：1)JMeter模拟并发 2)监控CPU使用率 3)分析吞吐量和响应时间曲线。调优公式：最佳线程数 = CPU核心数 * (1 + 等待时间/计算时间)。具体实践：1)I/O密集型应用设置较大线程数 2)CPU密集型接近核心数 3)考虑上下文切换成本。参数确定：1)核心线程数=CPU核心数 2)最大线程数=核心数*2 3)合理队列容量 4)合适拒绝策略。监控指标：1)活跃线程数 2)队列大小 3)拒绝次数 4)任务耗时";
    private static final String html_content = "<p>Devv.ai 是一款面向程序员的新一代 AI 搜索引擎，旨在替代传统的搜索引擎和技术博客社区，专注于解决编程和技术难题。其主要特点包括：</p><ul><li><strong>AI 驱动</strong>：基于 RAG（检索增强生成）技术和大型语言模型，Devv.ai 能够提供快速、准确的编程相关查询结果。</li><li><strong>多语言支持</strong>：支持多种编程语言，如 Python、Go、JavaScript、Java 等。</li><li><strong>多种搜索模式</strong>：提供快速模式、代理模式和</li><li><strong>GitHub 模式</strong>：满足不同开发者的需求。 持续学习：基于用户互动不断学习和改进，提供更个性化的搜索体验。</li></ul>";
    @Resource
    DeepSeekService deepSeekService;

    @Test
    public void generateSubject() {
        long start = System.currentTimeMillis();
        String text;
        try (InputStream in = Files.newInputStream(Paths.get("/Desktop/resume.pdf"))) {
            PDDocument document = PDDocument.load(in);
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            stripper.setStartPage(0);
            stripper.setEndPage(Integer.MAX_VALUE);
            text = stripper.getText(document);
            text = pattern.matcher(text).replaceAll("");
            System.out.println(text);
            System.out.println("----------------------------");
        } catch (Exception e) {
            log.error("提取文本失败：", e);
            return;
        }
        // 构建 deepseek 请求对象
        ChatRequest request = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(Arrays.asList(
                        new ChatRequest.Message("system", INTERVIEW_QUESTION_PROMPT),
                        new ChatRequest.Message("user", text)
                ))
                // .response_format(new ChatRequest.ResponseFormat("json_object"))
                .build();

        String json = deepSeekService.generateResponse(request);
        if (json.contains("```json")) {
            json = json.substring(json.lastIndexOf("```json") + 7, json.lastIndexOf("```"));
        }

        // JSONObject json_obj = JSONUtil.parseObj(json);
        // System.out.println("deepseek subjectName = " + json_obj.get("subjectName"));
        // System.out.println("deepseek labelName = " + json_obj.get("labelName"));
        // System.out.println("deepseek subjectAnswer = " + json_obj.get("subjectAnswer"));
        JSONArray json_arr = JSONUtil.parseArray(json);
        List<InterviewQuestionVO.Interview> list = json_arr.toList(InterviewQuestionVO.Interview.class);
        System.out.println("deepseek result = \n" + json_arr.toStringPretty());
        System.out.println("java result = \n" + list);
        System.out.printf("耗时 %d 秒", (System.currentTimeMillis() - start) / 1000);
    }

    @Test
    public void generateSuggestion() {
        long start = System.currentTimeMillis();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            // 构建 deepseek 请求对象
            ChatRequest request = ChatRequest.builder()
                    .model("deepseek-chat")
                    .messages(Arrays.asList(
                            new ChatRequest.Message("system", INTERVIEW_ANSWER_PROMPT),
                            new ChatRequest.Message("user", user_query2)
                    ))
                    .response_format(new ChatRequest.ResponseFormat("json_object"))
                    .build();

            // 创建异步任务
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> deepSeekService.generateResponse(request), executor)
                    .thenApply(json -> {
                        // 处理JSON字符串
                        if (json.contains("```json")) {
                            json = json.substring(json.lastIndexOf("```json") + 7, json.lastIndexOf("```"));
                        }
                        return JSONUtil.parseObj(json);
                    })
                    .thenAccept(json_obj -> {
                        // 输出结果（异步线程中打印）
                        System.out.println("deepseek userScore = " + json_obj.get("userScore"));
                        System.out.println("deepseek isCorrect = " + json_obj.get("isCorrect"));
                        System.out.println("deepseek subjectAnswer = " + json_obj.get("subjectAnswer"));
                        System.out.println("deepseek suggestion = " + json_obj.get("suggestion"));
                    });

            futures.add(future);
        }
        // 等待所有异步任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        System.out.printf("耗时 %d 秒", (System.currentTimeMillis() - start) / 1000);
        executor.shutdown();
    }

    @Test
    public void generateSuggestion2() {
        long start = System.currentTimeMillis();

        // 构建 deepseek 请求对象
        ChatRequest request = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(Arrays.asList(
                        new ChatRequest.Message("system", INTERVIEW_ANSWER_PROMPT2),
                        new ChatRequest.Message("user", user_query3)
                ))
                // .response_format(new ChatRequest.ResponseFormat("json_object"))
                .build();

        // 创建异步任务
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> deepSeekService.generateResponse(request), executor)
                .thenApply(json -> {
                    // 处理JSON字符串
                    if (json.contains("```json")) {
                        json = json.substring(json.lastIndexOf("```json") + 7, json.lastIndexOf("```"));
                    }
                    return JSONUtil.parseArray(json);
                })
                .thenAccept(json_obj -> {
                    System.out.println(json_obj.toStringPretty());
                    // 输出结果（异步线程中打印）
                    // System.out.println("deepseek userScore = " + json_obj.get("userScore"));
                    // System.out.println("deepseek isCorrect = " + json_obj.get("isCorrect"));
                    // System.out.println("deepseek subjectAnswer = " + json_obj.get("subjectAnswer"));
                    // System.out.println("deepseek suggestion = " + json_obj.get("suggestion"));
                });

        future.join();
        System.out.printf("耗时 %d 秒", (System.currentTimeMillis() - start) / 1000);
        executor.shutdown();
    }

    @Test
    public void generateSummary() {
        long start = System.currentTimeMillis();
        // 构建 deepseek 请求对象
        ChatRequest request = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(Arrays.asList(
                        new ChatRequest.Message("system", CIRCLE_ARTICLE_SUMMARY_PROMPT),
                        new ChatRequest.Message("user", HtmlUtil.cleanHtmlTag(html_content))
                ))
                .build();
        System.out.println(deepSeekService.generateResponse(request));
        System.out.printf("耗时 %d 秒", (System.currentTimeMillis() - start) / 1000);
    }

}
