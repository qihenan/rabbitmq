package org.qhn.rabbitmq;

public interface IMqSender {

    String getExchangeName();

    String getRouterKey();

}
