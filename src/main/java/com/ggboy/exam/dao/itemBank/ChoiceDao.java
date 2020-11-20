package com.ggboy.exam.dao.itemBank;

import com.ggboy.exam.beans.itemBank.ChoiceQst;
import com.ggboy.exam.config.GeneralMapper;

public interface ChoiceDao extends GeneralMapper<ChoiceQst> {

    ChoiceQst selectChoiceQstByQuestionId(String choiceQuestion);

}
