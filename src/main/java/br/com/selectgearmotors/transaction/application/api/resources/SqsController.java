package br.com.selectgearmotors.transaction.application.api.resources;

import br.com.selectgearmotors.transaction.application.event.DocumentSenderAdapter;
import br.com.selectgearmotors.transaction.application.event.NotificationSenderAdapter;
import br.com.selectgearmotors.transaction.application.event.dto.ContactDTO;
import br.com.selectgearmotors.transaction.application.event.dto.DocumentDTO;
import br.com.selectgearmotors.transaction.application.event.dto.NotificationDTO;
import br.com.selectgearmotors.transaction.application.event.dto.VehicleDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

//TODO = deletar essa classe apos testes
@Slf4j
@RestController
@RequestMapping("/sqs")
@CrossOrigin(origins = "*", allowedHeaders = "Content-Type, Authorization", maxAge = 3600)
public class SqsController {

    private final DocumentSenderAdapter documentSenderAdapter;
    private final NotificationSenderAdapter notificationSenderAdapter;

    @Autowired
    public SqsController(DocumentSenderAdapter documentSenderAdapter, NotificationSenderAdapter notificationSenderAdapter) {
        this.documentSenderAdapter = documentSenderAdapter;
        this.notificationSenderAdapter = notificationSenderAdapter;
    }

    @PostMapping("/send-notification-message")
    public String sendMessageNotification() throws JsonProcessingException {
        NotificationDTO notification = new NotificationDTO(
                "+5534992031938",
                "Compra realizada com sucesso! Veiculo ABC123"
        );

        notificationSenderAdapter.sendMessage(notification);
        return "Message sent!";
    }

    @PostMapping("/send-document-message")
    public String sendMessageDocument() throws JsonProcessingException {

        /*ContactDTO contact = new ContactDTO(
                "Jane Smith",
                "+987654321",
                "jane@contact.com",
                "https://contact.com",
                "5678 Second St",
                "Los Angeles",
                "CA",
                "USA",
                "90001",
                "https://example.com/contact-image.jpg",
                "https://example.com/contact-video.mp4",
                "2024-09-04",
                "14:00",
                "Office",
                "Main contact person for the transaction.",
                "Sales",
                "Vehicle",
                "Honda",
                "Contact",
                "20000",
                "Active",
                "Electric",
                "15000",
                "2023",
                "2HGCM82633A123456",
                "123456789",
                "Blue"
        );*/

        VehicleDTO vehicle = new VehicleDTO(
                2L,
                "8ce50d2a-a955-4be1-ac1c-0125ddd4dbf6",
                "Azul",
                1234,
                "SASA",
                BigDecimal.valueOf(92822.99),
                "Hatch",
                "Escort Hobby 1.0",
                "Ford",
                "AVAILABLE",
                "Uberlândia",
                "GZK-8775",
                "1HGCM82633A123456",
                "76015901475"
        );

        DocumentDTO document = new DocumentDTO(
                "12345",
                "67890",
                "John Doe",
                "Roberto Maria",
                vehicle
        );

        documentSenderAdapter.sendMessage(document);
        return "Message sent!";
    }
}
