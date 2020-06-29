package com.bes.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Lucene全文检索技术demo
 */
public class LuceneFirst {
    /**
     * 创建索引库
     */
    @Test
    public void createIndex() throws IOException {
        // 创建Directory,即索引库的位置。
        Directory directory = FSDirectory.open(new File("F:\\lucene\\indexStore").toPath());
        // 创建indexWriter对象，向指定索引库中写入document
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());
        // 创建源文件对象
        File sourceFile = new File("F:\\searchsource");
        // 遍历源文件
        File[] files = sourceFile.listFiles();
        for (File file :
                files) {
            // 创建document对象
            Document document = new Document();

            // 向document中添加字段Field文件路径
            document.add(new TextField("path", file.getPath(), Field.Store.YES));
            // 向document中添加字段Field文件名
            document.add(new TextField("name", file.getName(), Field.Store.YES));
            // 向document中添加字段Field文件大小
            document.add(new TextField("size", FileUtils.sizeOf(file) + "", Field.Store.YES));
            // 向document中添加字段Field文件内容
            document.add(new TextField("context", FileUtils.readFileToString(file, "utf-8"), Field.Store.YES));

            // 向索引库中添加document对象
            indexWriter.addDocument(document);
        }
        // 关闭indexWriter
        indexWriter.close();
    }

    /**
     * 查询索引库
     */
    @Test
    public void searchIndex() throws IOException {
        // 创建Directory对象
        Directory directory = FSDirectory.open(new File("F:\\lucene\\indexStore").toPath());
        // 创建indexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        // 创建IndexSearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        // 创建Query对象,TermQuery还有RangeQuery这是范围查询
        Query query = new TermQuery(new Term("context", "mvc"));
        // 查询到的结果
        TopDocs topDocs = indexSearcher.search(query, 20);
        System.out.println("查询到的文件总数：" + topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc:
            scoreDocs ) {
            int docID = scoreDoc.doc;
            Document document = indexSearcher.doc(docID);
            System.out.println(document.get("path"));
            System.out.println(document.get("name"));
            System.out.println(document.get("size"));
            System.out.println(document.get("context"));
            System.out.println("---------------------------------------------------------");
        }
        indexReader.close();
    }

    /**
     * 分析器Analyzer
     */
    @Test
    public void testTokenStream() throws IOException {
        // 创建一个Analyzer对象，StandarAnalyzer
        Analyzer analyzer = new StandardAnalyzer();
        // 通过analyzer获得一个TokenStream对象
        TokenStream tokenStream = analyzer.tokenStream("", "My name is peng-gaoli");
        // 向TokenStream中设置一个指针
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        // 调用reset方法,将指针移至头部
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            // 遍历打印关键字
            System.out.println(charTermAttribute.toString());
        }
        tokenStream.close();
    }

    /**
     * 中文分析器
     */
    @Test
    public void chineseAnalyzer() throws IOException {
        // 创建一个Analyzer对象，StandarAnalyzer
        Analyzer analyzer = new IKAnalyzer();
        // 通过analyzer获得一个TokenStream对象
        TokenStream tokenStream = analyzer.tokenStream("", "中新网郑州8月31日电(记者韩章云)一米九三的身高、眉目俊朗的脸庞、左耳还打了酷酷的耳洞……19岁的郑州准大学生冯雪珂是寻常人眼中的阳光帅小伙。而在即将进入大学前，帅气的冯雪珂做了件自认为很酷的事——毅然捐献造血干细胞，为一对罹患白血病的一岁双胞胎男童送去重生希望。8月31日，在河南省肿瘤医院的造血干细胞采集室，冯雪珂在家人的陪伴下顺利完成了造血干细胞的捐献。至此，他心里的一块石头终于落地。");
        // 向TokenStream中设置一个指针
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        // 调用reset方法,将指针移至头部
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            // 遍历打印关键字
            System.out.println(charTermAttribute.toString());
        }
        tokenStream.close();
    }


    /**
     * 创建中文检索库,使用IKAnalyzer分析器
     */
    public void chineseIndexStore() throws IOException {
        // 创建Directory,即索引库的位置。
        Directory directory = FSDirectory.open(new File("F:\\lucene\\indexStore").toPath());
        // 创建indexWriter对象，向指定索引库中写入document,指定IKAnalyzer为IndexWriterConfig的分析器
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
        // 创建源文件对象
        File sourceFile = new File("F:\\searchsource");
        // 遍历源文件
        File[] files = sourceFile.listFiles();
        for (File file :
                files) {
            // 创建document对象
            Document document = new Document();

            // 向document中添加字段Field文件路径
            document.add(new TextField("path", file.getPath(), Field.Store.YES));
            // 向document中添加字段Field文件名
            document.add(new TextField("name", file.getName(), Field.Store.YES));
            // 向document中添加字段Field文件大小
            document.add(new TextField("size", FileUtils.sizeOf(file) + "", Field.Store.YES));
            // 向document中添加字段Field文件内容
            document.add(new TextField("context", FileUtils.readFileToString(file, "utf-8"), Field.Store.YES));

            // 向索引库中添加document对象
            indexWriter.addDocument(document);
        }
        // 关闭索引库
        indexWriter.close();

    }
}
