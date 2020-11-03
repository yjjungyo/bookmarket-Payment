package bookmarket;

import bookmarket.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    PaymentRepository paymentRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderCanceled_PayCancel(@Payload OrderCanceled orderCanceled){

        if(orderCanceled.isMe()){
            System.out.println("##### listener PayCancel : " + orderCanceled.toJson());
            /*
            Payment payment = new Payment();
            payment.setOrderId(orderCanceled.getId());
            payment.setCustomerId(orderCanceled.getCustomerId());
            payment.setStatus("PayCanceled");
            paymentRepository.save(payment);
            */
            Payment payment = null;
            Iterable<Payment> paymentIterable = paymentRepository.findAll();

            for (Payment pay : paymentIterable) {
                if(orderCanceled.getId().equals(pay.getOrderId())) {
                    payment = pay;
                    break;
                }
            }
            payment.setStatus(orderCanceled.getStatus());

            paymentRepository.save(payment);
        }
    }

}
