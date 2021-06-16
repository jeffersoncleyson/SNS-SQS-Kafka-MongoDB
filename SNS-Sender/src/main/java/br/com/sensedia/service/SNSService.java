package br.com.sensedia.service;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SNSService {

    private final Logger logger = LoggerFactory.getLogger(SNSService.class);

    @Autowired
    private AmazonSNSClient amazonSNSClient;

    @Value("${sensedia.localstack.sns-topic-arn}")
    private String ARN_TOPIC;

    public void send(String message, String subject) throws Exception {
        PublishResult publishRequest = amazonSNSClient.publish(new PublishRequest(ARN_TOPIC, message, subject));
        if(publishRequest.getSdkHttpMetadata().getHttpStatusCode() != HttpStatus.OK.value()) throw new Exception("Message not deliivered");
    }
}
