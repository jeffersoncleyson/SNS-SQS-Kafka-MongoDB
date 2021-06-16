package br.com.sensedia.collector;

import br.com.sensedia.process.ProcessMessage;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class MessageReceiver {

    @Autowired
    private ProcessMessage processMessage;

    @SqsListener(value = "${sensedia.localstack.sqs-queue-name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveMessage(String message, @Header("SentTimestamp") Long sentTimestamp) {

        try {
            processMessage.startProcess(message, sentTimestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
