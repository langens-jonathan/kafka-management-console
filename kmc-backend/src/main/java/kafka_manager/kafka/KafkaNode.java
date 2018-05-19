package kafka_manager.kafka;

import kafka.Kafka;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;

/**
 * Created by langens-jonathan on 5/19/18.
 */
public class KafkaNode {
    private int id;
    private String host;
    private int port;

//    public KafkaNode(Node leader, List<Node> replicas, List<Node> inSyncReplicas, List<Node> offlineReplicas) {
//    }

    public KafkaNode(Node node) {
        this.id = node.id();
        this.host = node.host();
        this.port = node.port();
    }

    public KafkaNode() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
