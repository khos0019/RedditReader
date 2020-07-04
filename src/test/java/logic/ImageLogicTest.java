/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import entity.Image;
import entity.Board;
import entity.Host;
import common.TomcatStartUp;
import common.ValidationException;
import dal.EMFactory;
import java.text.ParseException;
import java.util.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.persistence.EntityManager;
import static logic.ImageLogic.FORMATTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 * @author Navraj Khosla
 */
public class ImageLogicTest {

    private ImageLogic imageLogic;
    private BoardLogic boardLogic;
    private HostLogic hostLogic;
    private Image expectedImage;
    private Board expectedBoard;
    private Host expectedHost;

    /**
     * Starts up the tomcat at the very beginning.
     *
     * @throws Exception
     */
    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat("/RedditReader", "common.ServletListener");
    }

    /**
     * Stops/Destroys the tomcat at the very end.
     *
     * @throws Exception
     */
    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }

    /**
     * Before each test setup/create the entities.
     *
     * @throws Exception
     */
    @BeforeEach
    final void setUp() throws Exception {

        EntityManager em = EMFactory.getEMF().createEntityManager();

        //Create a new host.
        Host host = new Host();

        //Set specific information
        host.setName("Junit Test For Host");
        host.setUrl("www.JunitTestHost.com");
        host.setExtractionType("json");

        //Begin transaction
        em.getTransaction().begin();

        //Merge the host information
        expectedHost = em.merge(host);

        //Commit the transaction
        em.getTransaction().commit();

        //Create a new board.
        Board board = new Board();

        //Set specific information
        board.setUrl("www.JunitTest.com");
        board.setName("Junit Test For Board");
        board.setHostid(expectedHost);

        //Begin the transaction
        em.getTransaction().begin();

        //Merge the board information
        expectedBoard = em.merge(board);

        //Commit the transaction
        em.getTransaction().commit();

        //Create a new image
        Image image = new Image();

        String localPath = System.getProperty("user.home");
        String dateString = FORMATTER.format(new Date());

        //Set specific information
        image.setTitle("Junit Image");
        image.setUrl("www.JunitTest.com");
        image.setLocalPath(localPath + "/image");
        image.setDate(FORMATTER.parse(dateString));
        image.setBoard(expectedBoard);

        //Begin the transaction
        em.getTransaction().begin();

        //Merge the host information
        expectedImage = em.merge(image);

        //Commit the transaction
        em.getTransaction().commit();

        //Close the EntityManager
        em.close();

        hostLogic = LogicFactory.getFor("Host");
        boardLogic = LogicFactory.getFor("Board");
        imageLogic = LogicFactory.getFor("Image");

    }

    /**
     * After each test, delete the created entities from the db.
     *
     * @throws Exception
     */
    @AfterEach
    final void tearDown() throws Exception {

        if (expectedImage != null) {
            imageLogic.delete(expectedImage);
        }

        if (expectedBoard != null) {
            boardLogic.delete(expectedBoard);
        }

        if (expectedHost != null) {
            hostLogic.delete(expectedHost);
        }

    }

    @Test
    final void testGetAll() {
        //get all the accounts from the DB
        List<Image> list = imageLogic.getAll();
        //store the size of list, this way we know how many accounts exits in DB
        int originalSize = list.size();

        //make sure account was created successfully
        assertNotNull(expectedImage);
        //delete the new account
        imageLogic.delete(expectedImage);

        //get all accounts again
        list = imageLogic.getAll();
        //the new size of accounts must be one less
        assertEquals(originalSize - 1, list.size());
    }

    /**
     * helper method for testing all image fields
     *
     * @param expected
     * @param actual
     */
    private void assertImageEquals(Image expected, Image actual) {
        //assert all field to guarantee they are the same
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUrl(), actual.getUrl());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getBoard(), actual.getBoard());
        assertEquals(expected.getLocalPath(), actual.getLocalPath());
        assertEquals(expected.getDate(), actual.getDate());
    }

    //Tests the ID of the expected and returned.
    @Test
    final void testGetWithId() {

        Image returnedImage = imageLogic.getWithId(expectedImage.getId());

        assertImageEquals(expectedImage, returnedImage);
    }

    //Test the Board ID of the expected and returned.
    //if returned ID is equals to expected ID, check if the images are equal.
    @Test
    final void testGetImagesWithBoardId() {

        int foundFull = 0;
        List<Image> returnedImages = imageLogic.getImagesWithBoardId(expectedBoard.getId());

        for (Image image : returnedImages) {
            //all accounts must have the same password
            assertEquals(expectedImage.getBoard(), image.getBoard());
            //exactly one account must be the same
            if (image.getId().equals(expectedImage.getId())) {
                assertImageEquals(expectedImage, image);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }

    //Test the Title of the expected and returned.
    //if returned ID is equals to expected ID, check if the images are equal.
    @Test
    final void testGetImagesWithTitle() {
        int foundFull = 0;
        List<Image> returnedImages = imageLogic.getImagesWithTitle(expectedImage.getTitle());

        for (Image image : returnedImages) {
            //all accounts must have the same password
            assertEquals(expectedImage.getTitle(), image.getTitle());
            //exactly one account must be the same
            if (image.getId().equals(expectedImage.getId())) {
                assertImageEquals(expectedImage, image);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");

    }

    //Tests the URL of the expected and returned.
    @Test
    final void testGetImageWithUrl() {

        Image returnedImage = imageLogic.getImageWithUrl(expectedImage.getUrl());
        
        assertImageEquals(expectedImage, returnedImage);

    }
    
    //Tests the Local Path of the expected and returned.
    @Test
    final void testGetImageWithLocalPath() {

        Image returnedImage = imageLogic.getImageWithLocalPath(expectedImage.getLocalPath());

        assertImageEquals(expectedImage, returnedImage);

    }

    //Test the Date of the expected and returned.
    //if returned ID is equals to expected ID, check if the images are equal.
    @Test
    final void testGetImagesWithDate() {
        int foundFull = 0;
        List<Image> returnedImages = imageLogic.getImagesWithDate(expectedImage.getDate());
        for (Image image : returnedImages) {
            //all accounts must have the same password
            assertEquals(expectedImage.getDate(), image.getDate());
            //exactly one account must be the same
            if (image.getId().equals(expectedImage.getId())) {
                assertImageEquals(expectedImage, image);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");

    }

    //Tests if the entity was created and if the information added is equal to the expected.
    @Test
    final void testCreateEntityAndAdd() {
        String localPath = System.getProperty("user.home");
        Date date = null;

        try {
            date = FORMATTER.parse("2020-07-03 12:00:00");
        } catch (ParseException ex) {
            Logger.getLogger(ImageLogicTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(ImageLogic.BOARD_ID, new String[]{expectedBoard.getId().toString()});
        sampleMap.put(ImageLogic.TITLE, new String[]{"Test Entity"});
        sampleMap.put(ImageLogic.URL, new String[]{"www.red.com"});
        sampleMap.put(ImageLogic.LOCAL_PATH, new String[]{localPath + "/image01"});
        sampleMap.put(ImageLogic.DATE, new String[]{imageLogic.convertDate(date)});

        Image returnedImage = imageLogic.createEntity(sampleMap);
        imageLogic.add(returnedImage);

        returnedImage = imageLogic.getImageWithUrl(returnedImage.getUrl());

        assertEquals(sampleMap.get(ImageLogic.BOARD_ID)[0], returnedImage.getBoard().getId().toString());
        assertEquals(sampleMap.get(ImageLogic.TITLE)[0], returnedImage.getTitle());
        assertEquals(sampleMap.get(ImageLogic.URL)[0], returnedImage.getUrl());
        assertEquals(sampleMap.get(ImageLogic.LOCAL_PATH)[0], returnedImage.getLocalPath());
        assertEquals(sampleMap.get(ImageLogic.DATE)[0], imageLogic.convertDate(returnedImage.getDate()));

        imageLogic.delete(returnedImage);
    }

    //Tests if the returned entity created is equal to the expected entity.
    @Test
    final void testCreateEntity() {

        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(ImageLogic.ID, new String[]{expectedImage.getId().toString()});
        sampleMap.put(ImageLogic.BOARD_ID, new String[]{expectedBoard.getId().toString()});
        sampleMap.put(ImageLogic.TITLE, new String[]{expectedImage.getTitle()});
        sampleMap.put(ImageLogic.URL, new String[]{expectedImage.getUrl()});
        sampleMap.put(ImageLogic.LOCAL_PATH, new String[]{expectedImage.getLocalPath()});
        sampleMap.put(ImageLogic.DATE, new String[]{imageLogic.convertDate(expectedImage.getDate())});

        Image returnedImage = imageLogic.createEntity(sampleMap);

        assertImageEquals(expectedImage, returnedImage);

    }
    
    //Tests if any data within the entity is null or empty.
    @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(ImageLogic.ID, new String[]{expectedImage.getId().toString()});
            map.put(ImageLogic.BOARD_ID, new String[]{expectedBoard.getId().toString()});
            map.put(ImageLogic.TITLE, new String[]{expectedImage.getTitle()});
            map.put(ImageLogic.URL, new String[]{expectedImage.getUrl()});
            map.put(ImageLogic.LOCAL_PATH, new String[]{expectedImage.getLocalPath()});
            map.put(ImageLogic.DATE, new String[]{imageLogic.convertDate(expectedImage.getDate())});
        };

        //idealy every test should be in its own method
        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.ID, null);
        assertThrows(NullPointerException.class, () -> imageLogic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.ID, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> imageLogic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.BOARD_ID, null);
        assertThrows(NullPointerException.class, () -> imageLogic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.BOARD_ID, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> imageLogic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.TITLE, null);
        assertThrows(NullPointerException.class, () -> imageLogic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.TITLE, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> imageLogic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.URL, null);
        assertThrows(NullPointerException.class, () -> imageLogic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.URL, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> imageLogic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.LOCAL_PATH, null);
        assertThrows(NullPointerException.class, () -> imageLogic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.LOCAL_PATH, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> imageLogic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.DATE, null);
        assertThrows(NullPointerException.class, () -> imageLogic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.DATE, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> imageLogic.createEntity(sampleMap));
    }

    //Tests if the entity created has any inappropriate lengths.
    @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(ImageLogic.ID, new String[]{expectedImage.getId().toString()});
            map.put(ImageLogic.BOARD_ID, new String[]{expectedBoard.getId().toString()});
            map.put(ImageLogic.TITLE, new String[]{expectedImage.getTitle()});
            map.put(ImageLogic.URL, new String[]{expectedImage.getUrl()});
            map.put(ImageLogic.LOCAL_PATH, new String[]{expectedImage.getLocalPath()});
            map.put(ImageLogic.DATE, new String[]{imageLogic.convertDate(expectedImage.getDate())});
        };

        IntFunction<String> generateString = (int length) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints('a', 'z' + 1).limit(length)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        };

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.ID, new String[]{""});
        assertThrows(ValidationException.class, () -> imageLogic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.ID, new String[]{"15d"});
        assertThrows(ValidationException.class, () -> imageLogic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.BOARD_ID, new String[]{""});
        assertThrows(ValidationException.class, () -> imageLogic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.BOARD_ID, new String[]{"30cc"});
        assertThrows(ValidationException.class, () -> imageLogic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.TITLE, new String[]{""});
        assertThrows(ValidationException.class, () -> imageLogic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.TITLE, new String[]{generateString.apply(300)});
        assertThrows(ValidationException.class, () -> imageLogic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.URL, new String[]{""});
        assertThrows(ValidationException.class, () -> imageLogic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.URL, new String[]{generateString.apply(200)});
        assertThrows(ValidationException.class, () -> imageLogic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.LOCAL_PATH, new String[]{""});
        assertThrows(ValidationException.class, () -> imageLogic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.LOCAL_PATH, new String[]{generateString.apply(200)});
        assertThrows(ValidationException.class, () -> imageLogic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.DATE, new String[]{""});
        assertThrows(ValidationException.class, () -> imageLogic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.DATE, new String[]{generateString.apply(30)});
        assertThrows(ValidationException.class, () -> imageLogic.createEntity(sampleMap));
    }

    //Tests the minimum and maxium length of each data in the entity.
    @Test
    final void testCreateEntityEdgeValues() {
        IntFunction<String> generateString = (int length) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints('a', 'z' + 1).limit(length)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        };

        Date minDate = null;
        Date maxDate = null;

        try {
            minDate = FORMATTER.parse("1970-01-01 12:00:00");
            maxDate = FORMATTER.parse("2120-01-01 12:00:00");
        } catch (ParseException ex) {
            Logger.getLogger(ImageLogicTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(ImageLogic.ID, new String[]{Integer.toString(1)});
        sampleMap.put(ImageLogic.BOARD_ID, new String[]{Integer.toString(1)});
        sampleMap.put(ImageLogic.TITLE, new String[]{generateString.apply(1)});
        sampleMap.put(ImageLogic.URL, new String[]{generateString.apply(1)});
        sampleMap.put(ImageLogic.LOCAL_PATH, new String[]{generateString.apply(1)});
        sampleMap.put(ImageLogic.DATE, new String[]{imageLogic.convertDate(minDate)});

        //idealy every test should be in its own method
        Image returnedImage = imageLogic.createEntity(sampleMap);
        assertEquals(Integer.parseInt(sampleMap.get(ImageLogic.ID)[0]), returnedImage.getId());
        assertEquals(Integer.parseInt(sampleMap.get(ImageLogic.BOARD_ID)[0]), returnedImage.getBoard().getId());
        assertEquals(sampleMap.get(ImageLogic.TITLE)[0], returnedImage.getTitle());
        assertEquals(sampleMap.get(ImageLogic.URL)[0], returnedImage.getUrl());
        assertEquals(sampleMap.get(ImageLogic.LOCAL_PATH)[0], returnedImage.getLocalPath());
        assertEquals(sampleMap.get(ImageLogic.DATE)[0], imageLogic.convertDate(returnedImage.getDate()));

        sampleMap = new HashMap<>();
        sampleMap.put(ImageLogic.ID, new String[]{Integer.toString(1)});
        sampleMap.put(ImageLogic.BOARD_ID, new String[]{Integer.toString(1)});
        sampleMap.put(ImageLogic.TITLE, new String[]{generateString.apply(255)});
        sampleMap.put(ImageLogic.URL, new String[]{generateString.apply(100)});
        sampleMap.put(ImageLogic.LOCAL_PATH, new String[]{generateString.apply(100)});
        sampleMap.put(ImageLogic.DATE, new String[]{imageLogic.convertDate(maxDate)});

        //idealy every test should be in its own method
        returnedImage = imageLogic.createEntity(sampleMap);
        assertEquals(Integer.parseInt(sampleMap.get(ImageLogic.ID)[0]), returnedImage.getId());
        assertEquals(Integer.parseInt(sampleMap.get(ImageLogic.BOARD_ID)[0]), returnedImage.getBoard().getId());
        assertEquals(sampleMap.get(ImageLogic.TITLE)[0], returnedImage.getTitle());
        assertEquals(sampleMap.get(ImageLogic.URL)[0], returnedImage.getUrl());
        assertEquals(sampleMap.get(ImageLogic.LOCAL_PATH)[0], returnedImage.getLocalPath());
        assertEquals(sampleMap.get(ImageLogic.DATE)[0], imageLogic.convertDate(returnedImage.getDate()));
    }

    //Tests if the column names in ImageLogic match.
    @Test
    final void testGetColumnNames() {
        List<String> list = imageLogic.getColumnNames();
        assertEquals(Arrays.asList("ID", "BoardID", "Title", "Url", "LocalPath", "Date"), list);
    }
    
    //Tests if the column codes in ImageLogic match.
    @Test
    final void testGetColumnCodes() {
        List<String> list = imageLogic.getColumnCodes();
        assertEquals(Arrays.asList(ImageLogic.ID, ImageLogic.BOARD_ID, ImageLogic.TITLE, ImageLogic.URL, ImageLogic.LOCAL_PATH, ImageLogic.DATE), list);
    }

    //Tests if the extracted data in ImageLogic matches the expected.
    @Test
    final void testExtractDataAsList() {
        List<?> list = imageLogic.extractDataAsList(expectedImage);
        assertEquals(expectedImage.getId(), list.get(0));
        assertEquals(expectedImage.getBoard(), list.get(1));
        assertEquals(expectedImage.getTitle(), list.get(2));
        assertEquals(expectedImage.getUrl(), list.get(3));
        assertEquals(expectedImage.getLocalPath(), list.get(4));
        assertEquals(expectedImage.getDate(), list.get(5));
    }

}
