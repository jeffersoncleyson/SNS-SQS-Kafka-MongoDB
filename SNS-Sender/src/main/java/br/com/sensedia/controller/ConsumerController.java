package br.com.sensedia.controller;

import br.com.sensedia.model.Message;
import br.com.sensedia.repository.MessageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "consumer")
@Tag(name = "Mongo Consumer", description = "Endpoint for consuming data save from SQS AWS")
public class ConsumerController {

    @Autowired
    private MessageRepository messageRepository;

    @Operation(summary = "Get messages", description = "Get message from MongoDB", tags = { "SQS Messages" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message Received"),
    })
    @GetMapping(produces = { "application/json"})
    public ResponseEntity<?> consumeMessage(){
        List<Message> list = messageRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
}
