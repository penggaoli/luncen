package com.bes.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * 索引库查询
 */
public class IndexSearch {
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;

    @Before
    public void init() throws IOException {
        indexReader = DirectoryReader.open(FSDirectory.open(new File("F:\\lucene\\indexStore").toPath()));
        indexSearcher = new IndexSearcher(indexReader);
    }


    /**
     * 范围查询
     */
    @Test
    public void RangeSearch() throws IOException {
        Query query = LongPoint.newRangeQuery("size",1,10000);
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
     * 使用QueryParser进行查询
     */
    public void test() {
       
    }

}
