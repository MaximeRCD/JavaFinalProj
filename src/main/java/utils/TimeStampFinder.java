package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * <b>TimeStampFinder est la classe utilitaire permettant de récupérer le timestamps d'un fichier traité par le projet.</b>
 * <p>
 * L'objectif est de récupérer la string qui représente la date dans le nom du fichier.
 * On utilise une regex pour matcher un timestamp au format : yyyyMMddHHmmss composé de 14 chiffres consécutifs.
 * </p>
 *
 * @author mrichaudeau
 */

public class TimeStampFinder {
    /**
     * La string représentant le chemin vers le fichier dont on veut parser le nom.
     * @see TimeStampFinder#finder()
     */
    private String input;
    static Pattern p;
    static Matcher m;

    public TimeStampFinder(String input) {
        this.input = input;
    }

    /**
     * Getter de la classe TimeStampFinder
     * @return input string of file name
     */
    public String getInput() {
        return input;
    }
    /**
     * Setter de la classe TimeStampFinder
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * Récupère la partie timestamp du nom du fichier.
     * @return Une string correspondant au match de la date dans le nom du fichier traité.
     */
    public String finder() throws IllegalStateException {
        String finderResult;
        m = p.compile("[0-9]{14}").matcher(this.getInput());
        if (!m.find()) {
            return null;
        }
        do{
            finderResult = m.group();
        } while(m.find());
        return finderResult;
    }
}
