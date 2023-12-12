package com.had0uken.sport_bot.bot;

import com.had0uken.sport_bot.model.League;
import com.had0uken.sport_bot.model.Team;
import com.had0uken.sport_bot.model.User;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardGenerator {

    public InlineKeyboardMarkup getStartButtonsKit(){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardButton button4 = new InlineKeyboardButton();

        button1.setText("Add new team");
        button2.setText("Delete a team");
        button3.setText("View my teams");
        button4.setText("Check results");



        button1.setCallbackData("addTeam");
        button2.setCallbackData("deleteTeam");
        button3.setCallbackData("viewTeam");
        button4.setCallbackData("checkResults");

        rowInLine1.add(button1);
        rowInLine1.add(button2);
        rowInLine1.add(button3);
        rowsInline.add(rowInLine1);

        rowInLine2.add(button4);
        rowsInline.add(rowInLine2);

        markup.setKeyboard(rowsInline);
        return markup;
    }

    public InlineKeyboardMarkup getButtonsLeagues(List<League> leagues, String BACK_BUTTON){
       // List<League> leagues = leagueService.getAllLeagues();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for(League l: leagues)
        {
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(l.getLeague_name());
            button.setCallbackData("league_"+l.getLeague_id());
            rowInLine.add(button);
            rowsInline.add(rowInLine);
        }
        rowsInline.add(getBackButton(BACK_BUTTON));
        markup.setKeyboard(rowsInline);
        return markup;
    }

    public InlineKeyboardMarkup getButtonsTeams(League league, String BACK_BUTTON){
        List<Team> teams = league.getTeams();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for(Team t: teams)
        {
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(t.getTEAM_NAME());
            button.setCallbackData("team_"+t.getTEAM_ID());
            rowInLine.add(button);
            rowsInline.add(rowInLine);
        }
        rowsInline.add(getBackButton(BACK_BUTTON));
        markup.setKeyboard(rowsInline);
        return markup;
    }

    public InlineKeyboardMarkup getButtonsUserTeam(User user, String prefix, String BACK_BUTTON) {
        List<Team> teams = user.getTeams();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for(Team t: teams)
        {
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(t.getTEAM_NAME());
            button.setCallbackData(prefix+t.getTEAM_ID());
            rowInLine.add(button);
            rowsInline.add(rowInLine);
        }
        rowsInline.add(getBackButton(BACK_BUTTON));
        markup.setKeyboard(rowsInline);
        return markup;
    }

    public InlineKeyboardMarkup getBackButtonKit(User user, String prefix, String BACK_BUTTON) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(getBackButton(BACK_BUTTON));
        markup.setKeyboard(rowsInline);
        return markup;
    }



    private List<InlineKeyboardButton> getBackButton(String BACK_BUTTON) {
        List<InlineKeyboardButton> backButtonList = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("<< BACK TO MAIN MENU");
        button.setCallbackData(BACK_BUTTON);
        backButtonList.add(button);
        return backButtonList;
    }



}
