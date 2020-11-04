package bookmarket;

import bookmarket.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
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

            List<Payment> paymentList = paymentRepository.findByOrderId(orderCanceled.getId());
            for(Payment payment : paymentList){
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                payment.setStatus("PayCanceled");
                // view 레파지 토리에 save
                paymentRepository.save(payment);
            }
        }
    }

}
