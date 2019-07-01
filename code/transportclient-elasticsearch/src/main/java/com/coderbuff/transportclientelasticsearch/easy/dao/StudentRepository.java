package com.coderbuff.transportclientelasticsearch.easy.dao;

import com.coderbuff.transportclientelasticsearch.common.AbstractElasticSearchDao;
import com.coderbuff.transportclientelasticsearch.easy.domain.StudentPO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

/**
 * Created by OKevin on 2019-06-30 13:54
 */
@Repository
@Slf4j
public class StudentRepository extends AbstractElasticSearchDao {

    /**
     * 批量插入
     * @param studentPOList StudentPO List
     */
    public void batchInsert(List<StudentPO> studentPOList) {
        BulkRequestBuilder bulkRequest = elasticSearchClient.getClient().prepareBulk();
        try {
            for (StudentPO student : studentPOList) {
                bulkRequest.add(elasticSearchClient.getClient().prepareIndex("user", "student").setSource(MAPPER.writeValueAsString(student)));
            }
        } catch (JsonProcessingException e) {
            log.error("json parse error!", e);
        }
    }

    /**
     * 批量修改
     * @param studentPOList StudentPO List
     */
    public void batchUpdate(List<StudentPO> studentPOList) {
        for (StudentPO student : studentPOList) {
            /*UpdateRequest updateRequest = new UpdateRequestBuilder()
                    .setIndex("user")
                    .setType("student")
                    .setId(student.getId())*/
        }
    }

    /**
     * 批量删除
     * @param studentPOList StudentPO List
     */
    public void batchDelete(List<StudentPO> studentPOList) {

    }

    public List<StudentPO> search(SearchRequestBuilder searchRequestBuilder) {
        SearchResponse searchResponse = searchRequestBuilder.get();
        SearchHits hits = searchResponse.getHits();
        List<StudentPO> studentPOList = Lists.newArrayList();
        try {
            for (SearchHit hit : hits) {
                String studentJson = hit.getSourceAsString();
                StudentPO studentPO = MAPPER.readValue(studentJson, StudentPO.class);
                studentPOList.add(studentPO);
            }
        } catch (IOException e) {
            log.error("parse json error!", e);
        }
        return studentPOList;
    }
}
