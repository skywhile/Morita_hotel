package com.morita.hotel.config;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

@Configuration
public class RagConfig {

    // 文本向量化模型：负责把文字转换成计算机能理解的数字向量
    // 比如把"苹果"转换成[0.1, 0.5, -0.3, ...]这样的数字序列
    @Resource
    private EmbeddingModel aliEmbeddingModel;

    // 向量数据库：用来存储和搜索这些文本向量
    // 想象成一个专门存放文字"指纹"的特殊仓库
    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;

    /**
     * 创建文档检索控制器
     *
     * 这个系统的工作流程：
     * 1. 读取文档 → 2. 切分文档 → 3. 转换成向量 → 4. 存入向量库 → 5. 搭建检索器
     *
     * 当用户提问时，系统会把问题也转换成向量，然后在向量库中寻找最相似的文档片段来回答问题
     */
    @Bean
    public ContentRetriever contentRetriever() throws IOException {
        // 🗂️ 第一步：加载知识库文档
        // 从resources/docs文件夹读取所有文档文件
        // 这些文档就是AI的"知识教科书"
        String docsPath = new ClassPathResource("docs").getFile().getAbsolutePath();
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(docsPath);

        // ✂️ 第二步：准备文档切割器
        // 为什么要切割文档？
        // - 长文档不易处理：就像读书时我们不会整本书背，而是分章节学习
        // - 提高检索精度：只返回最相关的段落，而不是整篇文档
        DocumentByParagraphSplitter paragraphSplitter = new DocumentByParagraphSplitter(
                1000,  // 每个文本块最多1000个字符（约200-300字）
                200    // 块之间重叠200字符，避免把完整的意思拦腰截断
        );

        // 🏭 第三步：创建文档处理流水线
        // 这个流水线负责：切割文档 → 添加标签 → 转换成向量 → 存入数据库
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(paragraphSplitter)  // 使用段落切割器
                // 给每段文本加上"出处标签"，这样回答时会显示信息来源
                // 比如："员工手册.pdf\n员工请假需提前3天申请..."
                .textSegmentTransformer(textSegment -> TextSegment.from(
                        "📄 " + textSegment.metadata().getString("file_name") + "\n" + textSegment.text(),
                        textSegment.metadata()
                ))
                .embeddingModel(aliEmbeddingModel)  // 文本转向量模型
                .embeddingStore(embeddingStore)     // 向量存储位置
                .build();

        // 📥 第四步：处理并存储所有文档
        // 这步相当于让AI"学习"所有文档内容
        // 文档被切割成小块，转换成向量后存入向量数据库
        ingestor.ingest(documents);

        // 🔍 第五步：创建智能检索器
        // 当用户提问时，这个检索器负责：
        // 1. 把用户问题转换成向量
        // 2. 在向量库中搜索最相似的文档片段
        // 3. 返回质量最高的结果给AI参考
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)     // 指定搜索的向量库
                .embeddingModel(aliEmbeddingModel)  // 问题转向量用的模型
                .maxResults(3)      // 最多返回3个相关结果（兼顾准确性和信息量）
                .minScore(0.65)     // 相似度门槛：65%（过滤掉不相关的内容）
                .build();

        return contentRetriever;
    }
}
