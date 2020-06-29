package com.bes.lucene;


import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.document.TextField;
import java.io.File;
import java.io.IOException;

/**
 * 索引库维护
 */
public class IndexManage {
    private IndexWriter indexWriter;
    /**
     * 初始化方法
     */
    @Before
    public void init() throws IOException {
        indexWriter = new IndexWriter(FSDirectory.open(new File("F:\\lucene\\indexStore").toPath()),
                new IndexWriterConfig(new IKAnalyzer()));
    }
    /**
     * 向索引库中添加文件
     */
    @Test
    public void addFile() throws IOException {
        // 创建一个indexwriter对象
        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(new File("F:\\lucene\\indexStore").toPath()),
                new IndexWriterConfig(new IKAnalyzer()));
        // 创建一个document对象
        Document document = new Document();
        // 向document对象中添加field字段
        document.add(new TextField("name", "haha", Field.Store.YES));
        // 将document对象写入索引库中
        indexWriter.addDocument(document);
        // 关闭写入流
        indexWriter.close();
    }


    /**
     * 删除全部文档
     */
    @Test
    public void deleteAll() throws IOException {
        indexWriter.deleteAll();
        indexWriter.close();
    }

    /**
     * 按照查询结果删除文档
     */
    @Test
    public void deleteFromQuery() throws IOException {
        indexWriter.deleteDocuments(new Term("context", "apache"));
        indexWriter.close();
    }

    /**
     * 更新文档
     */
    @Test
    public void updataIndex() throws IOException {
        Document document = new Document();
        File file = new File("F:\\input1.txt");
        document.add(new TextField("path",file.getPath(), Field.Store.YES ));
        document.add(new TextField("context", FileUtils.readFileToString(file,"utf-8"), Field.Store.YES));
        indexWriter.updateDocument(new Term("context", "apache"), document);
        indexWriter.close();
    }
}
