package utils;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * <b>StringToUnixTmstpTest est la classe test de la classe utilitaire permettant de convertir une string depuis un certain format vers un timestammp unix.</b>
 * <p>
 * L'objectif est à partir de la string qui représente la date dans le nom du fichier de la transformer en date pour en tirer un timestamp unix.
 * </p>
 *
 * @author mrichaudeau
 */
public class StringToUnixTmstpTest {
    String good = "20210606220136";
    String mal_formatted = "2021a201125435";
    String mal_formatted2 = "20210101112545000";
    StringToUnixTmstp stut = new StringToUnixTmstp(good);
    @Test
    /**
     * Test d'une string provenant d'un des fichiers.
     */
    public void findTestBasic() throws ParseException{
        assertEquals(1623009696000L, stut.convert());
    }
    @Test
    /**
     * Test d'une string vide.
     */
    public void findTestEmpty() throws ParseException{
        stut.setInput("");
        assertNull(stut.convert());
    }
    @Test
    /**
     * Test d'une string malformattée.
     */
    public void findTestBadFormatted() throws ParseException{
        stut.setInput(mal_formatted);
        assertNull(stut.convert());
    }
    @Test
    /**
     * Test d'une string provenant d'un des fichiers.
     */
    public void findTestBadFormatted2() throws ParseException{
        stut.setInput(mal_formatted2);
        assertNull(stut.convert());
    }
}
