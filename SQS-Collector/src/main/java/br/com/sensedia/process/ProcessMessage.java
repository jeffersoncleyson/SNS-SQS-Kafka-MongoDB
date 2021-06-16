package br.com.sensedia.process;

import br.com.sensedia.model.Message;
import br.com.sensedia.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;

@Service
public class ProcessMessage {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.producer.topic}")
    private String TOPIC;

    public void startProcess(String message, Long sentTimestamp) throws Exception {
        saveMessageOnMongoDB(buildMessageObject(message, sentTimestamp));
        sendMessageToKafka(message);
    }

    private Message buildMessageObject(String message, Long sentTimestamp){

        Timestamp ts = new Timestamp(sentTimestamp);
        Date date = new Date(ts.getTime());

        Message m = new Message();
        m.setMessage(message);
        m.setTimestamp(date);

        return m;
    }

    private void saveMessageOnMongoDB(Message message) throws Exception {
        Message mSave = messageRepository.save(message);
        if (!(mSave instanceof Message)) throw new Exception("Erro ao salvar mensagem no MongoDB");
    }

    private void sendMessageToKafka(String message) {
        this.kafkaTemplate.send(TOPIC, message);
    }
}
