package com.had0uken.sport_bot.bot;

import com.had0uken.sport_bot.config.BotConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {


    final BotConfig config;
    private static final String ERROR_TEXT = "Error occurred: ";

    private static final String BACK_BUTTON = "Back button";
    private static final String HELP_TEXT = """
            Hi there! 

            Type /start to see a welcome message

            Type /help to see this message again
                        
            Type /deletedata to delete all your personal data that we keep
                        
            Type /mydata to view the information that we keep about you

            """;

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/mydata", "get your data stored"));
        listOfCommands.add(new BotCommand("/deletedata", "delete my data"));
        listOfCommands.add(new BotCommand("/help", "info how to use this bot"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }

    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    private void startCommandReceived(long chatID, String name, EditMessageText editMessageText) {

    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

    }




    private void handleCommand(Update update) {
        String messageText = update.getMessage().getText();
        long chatID = update.getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatID);
        switch (messageText) {
            case "/start" -> {
                System.out.println("working!!");
            }
            case "/help" -> {
            }
            case "/mydata" -> {
            }
            case "/deletedata" -> {
            }
            default -> {
                message.setText("Sorry, command was not recognized");
            }
        }
    }



}


