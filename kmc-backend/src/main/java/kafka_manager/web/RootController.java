package kafka_manager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import kafka_manager.kafka.GenericKafkaConsumer;
import kafka_manager.kafka.GenericKafkaProducer;
import kafka_manager.kafka.KafkaTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RootController {
  // default logger
  private static final Logger log = LoggerFactory.getLogger(RootController.class);

//  @PostConstruct
//  public void startKafkaServices() {
//    GenericKafkaProducer alogProducer = new GenericKafkaProducer();
//    alogProducer.start();
//  }

  @RequestMapping(value = "/topics", method = RequestMethod.GET)
  public ResponseEntity<String> getTopics(){
    GenericKafkaConsumer kafkaConsumer = new GenericKafkaConsumer();
    Map<String, List<PartitionInfo>> topics = kafkaConsumer.getTopics();
    ObjectMapper mapper = new ObjectMapper();
    String topicsJSON = "    {\n" +
            "      \"links\": {\n" +
            "        \"self\": \"http://localhost:8080/topics\"\n" +
            "      },\n" +
            "      \"data\": [";
    int id = 1;
    for(String topic : topics.keySet()) {
      try {
        List<PartitionInfo> lpi = topics.get(topic);
        PartitionInfo pi = lpi.get(0);
        KafkaTopic t = new KafkaTopic(id++, pi);
        topicsJSON += mapper.writeValueAsString(t) + ",\n";
      } catch(Exception e) {
        e.printStackTrace();
      }
    }

    if(topics.size() > 0) {
      topicsJSON = topicsJSON.substring(0, topicsJSON.length()-2);
    }

    topicsJSON += "      ]\n" +
            "    }\n";
    kafkaConsumer.stopConsumer();
    return new ResponseEntity<String>(topicsJSON, HttpStatus.OK);
  }

  @RequestMapping(value = "/topics/{topicId}/messages", method=RequestMethod.GET)
  public ResponseEntity<String> getMessagesForTopic(@PathVariable(value="topicId") String topicId) {
    GenericKafkaConsumer consumer = new GenericKafkaConsumer();
    consumer.subscribeToTopic(topicId);

    ConsumerRecords<String, String> records = consumer.getAllPreviousMessagesInTopic();
    List<ConsumerRecord<String, String>> invertedRecords = new ArrayList<ConsumerRecord<String, String>>();
    for(ConsumerRecord<String, String> record : records) {
      invertedRecords.add(record);
    }
    invertedRecords = Lists.reverse(invertedRecords);
      String outputString = "{\"data\":[\n";
      for(ConsumerRecord<String, String> record : invertedRecords) {
          String value = record.value();
          long offset = record.offset();
          String key = record.key();
          outputString += "{\"offset\":" + offset + ", \"key\": " + key + ", \"value\": \"" + value + "\" },\n";
      }
    if(!records.isEmpty()) {
      outputString = outputString.substring(0, outputString.length() - 2);
    }
    outputString += "]}";
    consumer.stopConsumer();
    return new ResponseEntity<String>(outputString, HttpStatus.OK);
  }

    @RequestMapping(path = "/topics/{topicId}", method=RequestMethod.POST)
    public ResponseEntity<String> setConsent(@PathVariable(value="topicId") String topicId,  @RequestBody String message) {
      message = message.substring(8, message.length());
      try {
        message = URLDecoder.decode(message, "UTF-8");
      } catch(Exception e) {
        e.printStackTrace();
      }
        String responseString = message;

        GenericKafkaProducer producer = new GenericKafkaProducer();
        producer.producerMessage(topicId, message);
      producer.stopProducer();
        return new ResponseEntity<String>(responseString, HttpStatus.OK);
    }

}