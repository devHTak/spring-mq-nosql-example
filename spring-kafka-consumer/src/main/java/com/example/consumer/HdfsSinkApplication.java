package com.example.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class HdfsSinkApplication {

    private final Configuration hadoopConfiguration;

    // 개별 리스너에 카프카 컨슈머 옵션값을 부여하고 싶다면 properties={"max.poll.interval.ms:6000", "auto.offset.reset:earlist"} 같은 방식으로 줄 수 있다
    // 병렬처리를 위해서 concurrency="3" 을 줄 수 있다
    // 특정 토픽의 특정 파티션만을 구독하고 싶다면 topicPartitions = { @TopicPartition(topic="test01", partitions={"0", "1"}),
    // @TopicPartition(topic="test02", partitionOffsets=@PartitionOffset(partition="0", initialOffset="3"} 과 같은 방식으로 줄 수 있다
    @KafkaListener(topics = "select-color", groupId = "color-hdfs-consumer-group")
    public void consume(String message) {
        try {
            String fileName = "/data/color-" + LocalDateTime.now().toString() + ".log";
            FileSystem fileSystem = FileSystem.get(hadoopConfiguration);
            FSDataOutputStream outputStream = fileSystem.create(new Path(fileName));
            outputStream.writeBytes(message + "\n");
            outputStream.close();
        } catch (Exception e) {
            log.error("HdfsSinkApplication error: {}", e.getMessage());
        }
    }
}
