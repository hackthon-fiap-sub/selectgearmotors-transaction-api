package br.com.selectgearmotors.transaction.application.event;

import br.com.selectgearmotors.transaction.application.event.dto.DocumentDTO;
import br.com.selectgearmotors.transaction.commons.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Slf4j
@Service
public class DocumentSenderAdapter {
    private final SqsClient sqsClient;

    @Value("${aws.queue.document.queueUrl}")
    private String queueUrl;

    public DocumentSenderAdapter(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void sendMessage(DocumentDTO documentDTO) {
        try {
            String messageBody = JsonUtil.getJson(documentDTO);
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .delaySeconds(10)  // Opcional, atraso no envio
                    .build();

            SendMessageResponse sendMsgResponse = sqsClient.sendMessage(sendMsgRequest);
            log.info("Message sent with id: {}", sendMsgResponse.messageId());
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage());
        }
    }
}
