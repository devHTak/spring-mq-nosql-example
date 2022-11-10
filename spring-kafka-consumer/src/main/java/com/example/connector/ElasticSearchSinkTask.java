package com.example.connector;

import com.example.consumer.ElasticSearchSinkConnectorConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class ElasticSearchSinkTask extends SinkTask {
    private ElasticSearchSinkConnectorConfig config;
    private RestHighLevelClient esClient;
    private ObjectMapper objectMapper;

    @Override
    public String version() {
        return "1.0.0";
    }

    @Override
    public void start(Map<String, String> props) {
        try {
            config = new ElasticSearchSinkConnectorConfig(props);
        } catch (ConfigException e) {
            throw new ConnectException(e.getMessage(), e);
        }

        esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost(config.getString(config.ES_CLUSTER_HOST), config.getInt(config.ES_CLUSTER_PORT))));
    }

    @Override
    public void put(Collection<SinkRecord> records) {
        if(records.size() > 0) {
            BulkRequest bulkRequest = new BulkRequest();
            for(SinkRecord record: records) {
                try {
                    Map<String, String> map = objectMapper.readValue((String) record.value(), Map.class);
                    bulkRequest.add(new IndexRequest(config.getString(config.ES_INDEX)).source(map, XContentType.JSON));
                    log.info("record: {}", record.value());
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }

            esClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkItemResponses) {
                    if(bulkItemResponses.hasFailures()) {
                        log.error(bulkItemResponses.buildFailureMessage());
                    } else {
                        log.info("bulk save success");
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
    }

    @Override
    public void flush(Map<TopicPartition, OffsetAndMetadata> currentOffsets) {
        log.info("flush");
    }

    @Override
    public void stop() {
        try {
            esClient.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
