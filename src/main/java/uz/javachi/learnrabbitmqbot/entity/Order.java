package uz.javachi.learnrabbitmqbot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long chatId;
    private String optionalMessage;

    public Order() {
    }

    //    @Builder(builderMethodName = "childBuilder")
    public Order(Long chatId, String optionalMessage) {
        this.chatId = chatId;
        this.optionalMessage = optionalMessage;
    }

    public String getOptionalMessage() {
        return optionalMessage;
    }

    public void setOptionalMessage(String optionalMessage) {
        this.optionalMessage = optionalMessage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", optionalMessage='" + optionalMessage + '\'' +
                '}';
    }
}
