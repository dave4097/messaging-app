package org.david.messaging.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.david.messaging.domain.Message;
import org.david.messaging.domain.MessagingService;
import org.david.messaging.domain.ViewableMessageIterableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Api(
      value = "Messaging",
      description = "Operations to get and post messages"
)
@RestController
@RequestMapping("/messaging")
public class MessagingController {

   private final MessagingService messagingService;

   @Autowired
   public MessagingController(MessagingService messagingService) {
      this.messagingService = messagingService;
   }

   @ApiOperation(
         value = "Gets all messages that have been posted",
         httpMethod = "GET",
         produces = "application/JSON"
   )
   @GetMapping
   public ViewableMessageIterableWrapper getAllMessages() {
      return messagingService.getAllMessages();
   }

   @ApiOperation(
         value = "Posts a message",
         httpMethod = "POST",
         consumes = "application/JSON"
   )
   @PostMapping
   public void postMessage(@RequestBody @Valid Message message) {
      messagingService.processMessage(message);
   }

   @ResponseStatus(HttpStatus.BAD_REQUEST)
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
      Map<String, String> errors = new HashMap<>();
      ex.getBindingResult().getAllErrors().forEach((error) -> {
         String fieldName = ((FieldError) error).getField();
         String errorMessage = error.getDefaultMessage();
         errors.put(fieldName, errorMessage);
      });
      return errors;
   }
}
