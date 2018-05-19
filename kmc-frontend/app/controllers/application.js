import Ember from 'ember';
import request from 'ember-ajax/request';
import raw from 'ember-ajax/raw';

export default Ember.Controller.extend({
    topics: [],

    activeTopic: "",

    topic: undefined,

    hasActiveTopic: false,

    activePIPartitionNr: 0,

    activePILeader: {id: 0, host: "", port: 0},

    activePIReplicas: [],

    activePIISReplicas: [],

    acitvePIOfflineReplicas: [],

    messages: [],

    inputMessage: "",

    init() {
        this._super(...arguments);
        console.log("in init");
        this.getTopics();
    },

    getTopics: function() {
        var self = this;
        return request("/topics", {}).then(response => {
            Ember.set(self, "topics", response.data);
            return response;
        }).catch(response => {
            console.log(response);
        });
    },

    rerunMessages() {
        this.getMessages(Ember.get(this, "topic"));
    },

    sendMessage(topic, message) {
        var url = "/topics/" + topic.name;
        var options = {
            method: 'POST',
            data: {"message": message }
        };
        var self = this;
        raw(url, options).then(response => {
            console.log(response);
        }).catch(response => {
            console.log(response);
        });
    },

    getMessages(topic) {
        var url = "/topics/" + topic.name + "/messages";
        var self = this;
        return request(url, {}).then(response => {
            console.log(response);
            Ember.set(self, "messages", response.data);
            Ember.run.later(self, self.rerunMessages, 5000);
        }).catch(response => {
            console.log(response);
        });
    },

    actions: {
        selectTopic(topic) {
            Ember.set(this, "activeTopic", topic.name);
            Ember.set(this, "hasActiveTopic", true);
            Ember.set(this, "activePIPartitionNr", topic.partition);
            Ember.set(this, "activePILeader", topic.leader);
            Ember.set(this, "activePIReplicas", topic.replicas);
            Ember.set(this, "activePIISReplicas", topic.inSyncReplicas);
            Ember.set(this, "activePIOfflineReplicas", topic.offlineReplicas);
            Ember.set(this, "topic", topic);
            this.getMessages(topic);
        },
        sendMessage() {
            var topic = Ember.get(this, "topic");
            var message = Ember.get(this, "inputMessage");
            this.sendMessage(topic, message);
        }
    }
});
