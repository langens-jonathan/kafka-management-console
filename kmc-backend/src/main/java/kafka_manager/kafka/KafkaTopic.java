package kafka_manager.kafka;

import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by langens-jonathan on 5/19/18.
 */
public class KafkaTopic {
    private int id;
    private String name;
    private KafkaNode leader;
    private List<KafkaNode> replicas;
    private List<KafkaNode> inSyncReplicas;
    private List<KafkaNode> offlineReplicas;
    private int partition;

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public KafkaTopic() {

    }

    private List<Node> convertToList(Node [] nodeArray) {
        List<Node> nodes = new ArrayList<Node>();
        for(int i = 0; i < nodeArray.length; ++i) {
            nodes.add(nodeArray[i]);
        }
        return nodes;
    }

    private List<KafkaNode> convertCommonsNodeList(List<Node> nodes) {
        List<KafkaNode> kafkaNodes = new ArrayList<KafkaNode>();
        for(Node node : nodes) {
            kafkaNodes.add(new KafkaNode(node));
        }
        return kafkaNodes;
    }

    public KafkaTopic(int id, PartitionInfo info) {
        this.id = id;
        this.name = info.topic();
        this.partition = info.partition();
        this.leader = new KafkaNode(info.leader());
        this.replicas = convertCommonsNodeList(convertToList(info.replicas()));
        this.inSyncReplicas = convertCommonsNodeList(convertToList(info.inSyncReplicas()));
        this.offlineReplicas = convertCommonsNodeList(convertToList(info.offlineReplicas()));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KafkaNode getLeader() {
        return leader;
    }

    public void setLeader(KafkaNode leader) {
        this.leader = leader;
    }

    public List<KafkaNode> getReplicas() {
        return replicas;
    }

    public void setReplicas(List<KafkaNode> replicas) {
        this.replicas = replicas;
    }

    public List<KafkaNode> getInSyncReplicas() {
        return inSyncReplicas;
    }

    public void setInSyncReplicas(List<KafkaNode> inSyncReplicas) {
        this.inSyncReplicas = inSyncReplicas;
    }

    public List<KafkaNode> getOfflineReplicas() {
        return offlineReplicas;
    }

    public void setOfflineReplicas(List<KafkaNode> offlineReplicas) {
        this.offlineReplicas = offlineReplicas;
    }
}
