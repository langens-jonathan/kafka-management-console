package kafka_manager.kafka;

import kafka_manager.config.Configuration;
import kafka.utils.ShutdownableThread;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Created by langens-jonathan on 5/19/18.
 */
public class GenericKafkaConsumer /*extends ShutdownableThread*/ {
    private final KafkaConsumer<String, String> consumer;
    private String topic = null;
    private static final Logger log = LoggerFactory.getLogger(GenericKafkaConsumer.class);

    public GenericKafkaConsumer() {
        //super("KafkaPolicyConsumer", false);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Configuration.getKafkaURLList());

        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<String, String>(props);
    }

    public void subscribeToTopic(String topic) {
        this.topic = topic;
        consumer.subscribe(Collections.singletonList(this.topic));
    }

    public Map<String, List<PartitionInfo>> getTopics() {
        Map<String, List<PartitionInfo>> topics;
        topics = consumer.listTopics();
        consumer.close();
        return topics;
    }

    public ConsumerRecords<String, String> getAllPreviousMessagesInTopic() {
        if(this.topic == null) return null;
        consumer.poll(0);
        consumer.seekToBeginning(consumer.assignment());
        consumer.seek(consumer.assignment().iterator().next(), 0);

        ConsumerRecords<String, String> records = consumer.poll(1000);
        for (ConsumerRecord<String, String> record : records) {

            log.info("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
        }
        return records;
    }

    public void stopConsumer() {
        this.consumer.close();
    }
}
