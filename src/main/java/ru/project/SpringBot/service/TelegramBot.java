package ru.project.SpringBot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.project.SpringBot.config.BotConfig;

@Component
public class TelegramBot extends TelegramLongPollingBot /*Webhook*/ {

    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageTest = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageTest) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                break;
                    default: sendMessage(chatId, "Sorry, не знаю такой команды братанчик");
            }
        }
    }

    private void startCommandReceived(Long chatId, String name) {

        String answer = "Hi, " + name + ", nice to meet you";

        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException();
        }
    }
}
