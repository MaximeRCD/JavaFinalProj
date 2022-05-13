package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b>StringToUnixTmstp est la classe utilitaire permettant de convertir une string depuis un certain format vers un timestammp unix.</b>
 * <p>
 * L'objectif est à partir de la string qui représente la date dans le nom du fichier de la transformer en date pour en tirer un timestamp unix.
 * </p>
 *
 * @author mrichaudeau
 */

public class StringToUnixTmstp {
    /**
     * La string représentant la date au format prérequis dont on veut obtenir le timestamp.
     * @see StringToUnixTmstp#convert()
     */
    private String input;

    public StringToUnixTmstp(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
    /**
     * Récupère la string pour la transformer en timestamp.
     * @return un long représentant le timestamp unix.
     */
    public Long convert() throws ParseException {
        SimpleDateFormat formatter;
        Matcher date_format_matcher = Pattern.compile("([0-9]{4})(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])(2[0-3]|[01][0-9])([0-5][0-9])([0-5][0-9])").matcher(this.getInput());
        if (date_format_matcher.find() & this.getInput().length() == 14 ) {
            formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = formatter.parse(this.getInput());
            return date.getTime();
        }else{
            return null;
        }
    }
}
