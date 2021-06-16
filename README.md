# Informações do Projeto

#
## Introdução
#

Este projeto é uma prova de conceito da utilização de produtos da AWS como: SNS - *Simple Notification Service* e SQS - *Simple Queue Service* como serviço de troca de mensagens. Também foi utilizado o Apache Kafka como produtor de mensagens extraidas da fila do SQS e o MongoDB como forma de armazenamento de mensagens enviadas pelo SNS e recebidas no SQS onde são salvas no banco de dados.</br>
A utilização de uma API Rest foi para facilitar a prova de conceito e demonstrar o uso de envio de mensagens pelo SNS e também para visualização dos dados gravdos no banco de dados recebidos pelo SQS-Collector.</br></br>


#
## Técnologias utilizadas neste projeto
#

* #### Spring Boot

* #### Apache Kafka

* #### Lombok

* #### OpenAPI

* #### API Rest

* #### Localstack - AWS Local

Os arquivos README de cada projeto contém conteúdos conceituais e links para outros sites mostrando mais informações. Abaixo os projetos:

* Projeto **[Kafka-API](https://github.com/jeffersoncleyson/kafka-api)**
* Projeto **[Security-API](https://github.com/jeffersoncleyson/Security-API)**
* Projeto **[Mongo-API](https://github.com/jeffersoncleyson/Security-API)**
* Projeto **[Env-DEV](https://github.com/jeffersoncleyson/env_dev)**

#
### Postman </br></br>

Os testes da API podem ser feitos utilizando a documentação especificada no tópico abaixo ou utilizando o software *Postman*. Junto ao projeto foi disponibilizado uma *collection* para ser importado no software e assim poder realizar os testes com os endpoints especificiados.

Para importar seguir os seguintes passos:

0) Fazer o [download](https://www.postman.com/downloads/) e instalar o Postman
1) Abrir o Postman
2) Clicar em *file*
3) Clicar em *import*
4) Arrastar e soltar o arquivo presenta na pasta *"Postman/AWS-API.postman_collection.json"*

</br></br>

#
# Preparando o ambiente
#

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

* Para verificar se o ambiente do LocalStack está correto verificar se o estado é running através do link abaixo:

```
http://localhost:4566/health
```

```
{
    "services": {
        "sqs": "running",
        "sns": "running"
    },
    "features": {
        "persistence": "disabled",
        "initScripts": "initialized"
    }
}
```

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

#
## Iniciando os Projetos
#


Para que tudo funcione como o esperado é necessário seguir os passos abaixo:

* Iniciado o ambiente de desenvolvimento.
* Iniciado o Código do SNS-Sender
* Iniciado o Código do SQS-Collector
* Iniciado o Código do Kafka-API. **[Disponível Aqui](https://www.postman.com/downloads/)**
* Importado no **Postman** as collections disponíveis neste projeto e no Kafka-API, ambos se encontram na pasta *Postman* de cada projeto.</br></br>

Se todos os projetos iniciaram sem erro, acessar no **Postman** a collection **AWS-API** e clicar em Producer no qual é um endpoint com método **POST**

```
localhost:9095/aws-sns/v1/producer
```

Neste endpoint o corpo da mensagem deve esta da seguinte forma (A mensagem pode ser alterado se quiser):

```
{
    "message": "testeMessage"
}
```

Após realizar este procedimento a mensagem é enviada para o SNS que em seguida é consumida pelo serviço do SQS-Collector e salva no Banco de dados.</br>
Para verificar se a mensagem foi salva no Banco de Dados consultar o segundo endpoint.

```
localhost:9095/aws-sns/v1/consumer
```

```
[
    {
        "message": "{\"Type\": \"Notification\", \"MessageId\": \"c2dbce6f-0966-4a88-a993-6b0264d90ba5\", \"TopicArn\": \"arn:aws:sns:us-east-1:000000000000:onexlab-sns\", \"Message\": \"testeMessage 111\", \"Timestamp\": \"2021-06-16T15:19:31.972Z\", \"SignatureVersion\": \"1\", \"Signature\": \"EXAMPLEpH+..\", \"SigningCertURL\": \"https://sns.us-east-1.amazonaws.com/SimpleNotificationService-0000000000000000000000.pem\", \"Subject\": \"subject\"}",
        "timestamp": "2021-06-16T15:19:31.996+00:00"
    }
]
```

Verificamos então que a mensagem foi salva no banco de dados com informações que o SQS trás. Dentro do JSON salvo se encontra uma chave com o nome de **Message** no qual foi a mensagem enviada.</br></br>
Vimos que a mensagem foi processada de forma correta, porém ainda temos mais um passo a ser seguido neste tutorial.</br>
Um resumo:
* Mensagem enviada através da API para o SNS
* Mensagem consumida pelo SQS-Collector
* Mensagem salva no banco de dados 
* Mensagem enviada para um tópico no Kafka.

Agora a mensagem está na fila do Apache Kafka, para consumi-la é necessário acessar a collection **Kafka-API** e o método **GET** chamado de **Consumer**. A mesma mensagem deve ser reproduzida no Kafka como mostra os resultados abaixo.

```
localhost:9000/kafka/v1/consumer
```
```
[
    {
        "value": "{\"Type\": \"Notification\", \"MessageId\": \"c2dbce6f-0966-4a88-a993-6b0264d90ba5\", \"TopicArn\": \"arn:aws:sns:us-east-1:000000000000:onexlab-sns\", \"Message\": \"testeMessage 111\", \"Timestamp\": \"2021-06-16T15:19:31.972Z\", \"SignatureVersion\": \"1\", \"Signature\": \"EXAMPLEpH+..\", \"SigningCertURL\": \"https://sns.us-east-1.amazonaws.com/SimpleNotificationService-0000000000000000000000.pem\", \"Subject\": \"subject\"}"
    }
]
```

#
### Visualizando os dados No MongoDB
#
Também é possível ver os dados gravados no Banco de Dados do MongoDB. Para isso abrir o **Mongo Compass**

* Clicar em "Fill in connection fields individually"
* inserir Hostname
```
localhost
```
* Clicar em Authentication -> "Username / Password"
* Inserir username
```
admin
```
* Inserir password
```
qwe123
```
* Clicar em Connect
* Acessar no canto esquerdo o DB: "aws-message"
* Clicar na collection "message"
* Então irá visualizar a mensagem gravada no banco de dados.
</br></br>



#
### Links e informações utilizadas para solução de problemas encontrados durante o desenvolviemento.
#

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