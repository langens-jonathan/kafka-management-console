package kafka_manager.kafka;

import kafka_manager.config.Configuration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by langens-jonathan on 4/25/18.
 */
public class GenericKafkaProducer /*extends Thread*/ {
    private KafkaProducer<String, String> producer;

    public GenericKafkaProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Configuration.getKafkaURLList());
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, Configuration.getKafkaClientID());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(properties);
    }

    public void producerMessage(String topic, String message) {
        try {
            producer.send(new ProducerRecord<String, String>(topic, null, message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopProducer() {
        this.producer.close();
    }
}
