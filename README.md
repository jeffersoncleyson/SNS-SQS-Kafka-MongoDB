## Instalação do AWS CLI

```
sudo snap install aws-cli --classic
```

## Configuração do CLI

```
aws configure --profile default
```
```
access_key_aws_local: access_key_aws_local
```
```
AWS Secret Access Key: secret_key_aws_local
```
```
Default region name: us-east-1
```

#
## Iniciar o ambiente do SNS, SQS, Kafka e MongoDB
#

O docker-compose do ambiente se encontra disponível no pasta **Environment**

#
## Sequência de comandos e respostas esperadas.
#

Para testes e verificar se o ambiente está funcionando como o esperado.</br>

* Criar uma fila no SQS

```
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name onexlab
```

```
{
    "QueueUrl": "http://localhost:4566/000000000000/onexlab"
}
```

* Listar filas do SQS

```
aws --endpoint-url=http://localhost:4566 sqs list-queues
```
```
{
    "QueueUrls": [
        "http://localhost:4566/000000000000/onexlab"
    ]
}
```

* Receber mensagem da fila do SQS

```
aws --endpoint-url=http://localhost:4566 sqs receive-message --queue-url http://localhost:4566/000000000000/onexlab
```

* Criar uma Topico no SNS

```
aws --endpoint-url=http://localhost:4566 sns create-topic --name onexlab-sns
```
```
{
    "TopicArn": "arn:aws:sns:us-east-1:000000000000:onexlab-sns"
}
```
* Listar os Topicos do SNS

```
aws --endpoint-url=http://localhost:4566 sns list-subscriptions
{
    "Subscriptions": []
}
```

* Subscrever o Topico do SNS na fila do SQS

```
aws --endpoint-url=http://localhost:4566 sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:onexlab-sns --protocol sqs --notification-endpoint http://localhost:4566/000000000000/onexlab
```
```
{
    "SubscriptionArn": "arn:aws:sns:us-east-1:000000000000:onexlab-sns:5b8d4015-8ba1-41a9-9cb2-0d903a05a6da"
}
```

* Enviar uma mensagem no topico do SNS

```
aws --endpoint-url=http://localhost:4566 sns publish  --topic-arn arn:aws:sns:us-east-1:000000000000:onexlab-sns --message 'Welcome to Onexlab!'
```
```
{
    "MessageId": "6d8d0ec3-5b1f-4824-a4ec-b1e6ecd821f2"
}
```

* Ler a mensagem criada no SQS

```
aws --endpoint-url=http://localhost:4566 sqs receive-message --queue-url http://localhost:4566/000000000000/onexlab
```
```
{
    "Messages": [
        {
            "MD5OfBody": "6c989b820f4f02dc6e9913615d77f019",
            "MessageId": "01c41da2-a548-775e-fcba-6587dae0ac06",
            "ReceiptHandle": "chpurzlhdcnseymshvzgvmslstnamfkxciwqsxpsirdftmvnzmblhhgnhxjtaqbzkodpvpsiowtkprqbkoveveebuotkjzfyhgpqnkvtzvmyzvhdcfvshlsvmfgrqqsujciefkwbatvemqbvlewhkcwyfvkpwkqqalrcffwiuqkuewswxlvqussny",
            "Body": "{\"Type\": \"Notification\", \"MessageId\": \"6d8d0ec3-5b1f-4824-a4ec-b1e6ecd821f2\", \"TopicArn\": \"arn:aws:sns:us-east-1:000000000000:onexlab-sns\", \"Message\": \"Welcome to Onexlab!\", \"Timestamp\": \"2021-06-14T18:47:45.044Z\", \"SignatureVersion\": \"1\", \"Signature\": \"EXAMPLEpH+..\", \"SigningCertURL\": \"https://sns.us-east-1.amazonaws.com/SimpleNotificationService-0000000000000000000000.pem\"}"
        }
    ]
}
```






* https://stackoverflow.com/questions/54879212/unable-to-use-spring-cloud-to-connect-with-aws-ses
* https://blog.jcore.com/2020/04/running-aws-locally-with-localstack/
* https://stackoverflow.com/questions/59517989/spring-cloud-aws-sqs-fails-to-connect-to-service-endpoint-locally
* https://gabrielfeitosa.com/simulando-aws-com-localstack
* https://lobster1234.github.io/2017/04/05/working-with-localstack-command-line/
* https://www.netsurfingzone.com/aws/spring-boot-aws-sqs-listener-example/
* https://onexlab-io.medium.com/localstack-sns-to-sqs-47a38f33b8f4
* https://github.com/MatthewEdge/boot-sqs-test/blob/main/docker-compose.yml
* https://levelup.gitconnected.com/receiving-messages-from-amazon-sqs-in-a-spring-boot-application-6e8a2d7583be
* https://reflectoring.io/spring-cloud-aws-sqs/