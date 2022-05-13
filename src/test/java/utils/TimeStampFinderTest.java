package utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 *<b>TimeStampFinderTest est la classe test de la clase utilitaire permettant de récupérer le timestamps d'un fichier traité par le projet.</b>
 * <p>
 * L'objectif est de tester la récupération la string qui représente la date dans le nom du fichier.
 * On utilise une regex pour matcher un timestamp au format : yyyyMMddHHmmss composé de 14 chiffres consécutifs.
 * </p>
 */

public class TimeStampFinderTest {

    TimeStampFinder tsf = new TimeStampFinder("C:\\Users\\maxim\\TélécomST\\Cours\\DE2\\DEV\\Final_Project\\ressources\\inputData\\user_20210606220136.csv");

    @Test
    /**
     * Test le plus basic sur les fichiers de InputData.
     */
    public void findTestBasic() {
        assertEquals("20210606220136", tsf.finder());
    }

    @Test
    /**
     * Test si la string est vide.
     */
    public void findTestEmpty() {
        tsf.setInput("");
        assertNull(tsf.finder());
    }

    @Test
    /**
     * Test si plusieurs match dans la string. Considère que le dernier match est la date dans le nom du fichier.
     */
    public void findTestMultiple() {
        tsf.setInput("C:\\Users\\maxim\\TélécomST\\Cours\\DE2\\DEV\\Final_Project\\ressources_20201209125536\\inputData\\user_20210606220136.csv");
        assertEquals("20210606220136", tsf.finder());
    }

    @Test
    /**
     * Test du format de la date dans la string
     */
    public void findTestStringFormat() {
        tsf.setInput("C:\\Users\\maxim\\TélécomST\\Cours\\DE2\\DEV\\Final_Project\\ressources\\inputData\\user_2021.csv");
        assertNull(tsf.finder());
    }
}
