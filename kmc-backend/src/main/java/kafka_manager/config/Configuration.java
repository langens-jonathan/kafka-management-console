package kafka_manager.config;

/**
 * Created by langens-jonathan on 4/10/18.
 */
public class Configuration {
    private static boolean instantiated = false;

    // everything kafka
    private static String kafkaURLList;
    private static String kafkaClientID;

    // the names of the environmnet variables
    private static String KAFKA_URL_LIST = "KAFKA_URL_LIST";
    private static String KAFKA_CLIENT_ID = "KAFKA_CLIENT_ID";

    // instantiates the variables
    // should include validity checking
    private static void instantiate() {
        if(Configuration.instantiated) return;
        Configuration.instantiateKafkaURLList();
        Configuration.instantiateKafkaClientID();
        Configuration.instantiated = true;
    }

    private static void instantiateKafkaURLList() {
        if(System.getenv().containsKey(Configuration.KAFKA_URL_LIST)) {
            Configuration.kafkaURLList = System.getenv(Configuration.KAFKA_URL_LIST);
        } else {
            Configuration.kafkaURLList = "";
        }
    }

    private static void instantiateKafkaClientID() {
        if(System.getenv().containsKey(Configuration.KAFKA_CLIENT_ID)) {
            Configuration.kafkaClientID = System.getenv(Configuration.KAFKA_CLIENT_ID);
        } else {
            Configuration.kafkaClientID = "";
        }
    }

    // getters, all lazy
    public static String getKafkaURLList() {
        Configuration.instantiate();
        return Configuration.kafkaURLList;
    }

    public static String getKafkaClientID() {
        Configuration.instantiate();
        return Configuration.kafkaClientID;
    }
}
