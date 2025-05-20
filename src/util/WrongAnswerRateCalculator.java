package util;

import data.entity.Word;
import data.repository.SavedWordRepository;
import data.repository.WrongWordRepository;

public class WrongAnswerRateCalculator {

    public static double getWrongRate(Word word){
        SavedWordRepository swr = SavedWordRepository.getInstance();
        WrongWordRepository wwr = WrongWordRepository.getInstance();
        if(wwr.getWordsList().contains(word)){
            double c_i = wwr.getCount(word);
            double W = swr.getWordsList().size();
            double D =  wwr.getWordsList().size();

            double e_i = (c_i * D) / (W + c_i * D);

            return e_i;
        } else {
            return 0.0D;
        }
    }
}
