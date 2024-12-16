package uz.javachi.learnrabbitmqbot.map;

import lombok.Getter;
import org.springframework.stereotype.Component;
import uz.javachi.learnrabbitmqbot.entity.Order;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class OrderMapTemporary {
    private final Map<Long, Order> list = new HashMap<>();

    public void setOrder(Long chatId, Order entity) {
        list.put(chatId, entity);
    }

    public Order getOrder(Long chatId) {
        return list.get(chatId);
    }

    public Boolean orderStatus(Long chatId) {
        return list.containsKey(chatId);
    }

    public void deletedOrder(Long chatId) {
        list.remove(chatId);
    }
}
