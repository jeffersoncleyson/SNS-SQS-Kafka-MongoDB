package br.com.sensedia.controller;

import br.com.sensedia.model.Message;
import br.com.sensedia.service.SNSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "producer")
@Tag(name = "AWS SNS Producer", description = "Endpoint for producing message")
public class SNSProducerController {

    @Autowired
    private SNSService snsService;

    @Operation(summary = "Send message", description = "Send message to kafka broker", tags = { "KafkaMessage" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message delivered"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Message couldn't be delivered")
    })
    @PostMapping(produces = { "application/json"})
    public ResponseEntity<?> produceMessage(@RequestBody Message message){
        try {
            snsService.send(message.getMessage(), "subject");
        }catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
