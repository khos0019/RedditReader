/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import dal.BoardDAL;
import entity.Board;
import entity.Host;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.ObjIntConsumer;
import common.ValidationException;

/**
 *
 * @author Navraj Khosla
 */
public class BoardLogic extends GenericLogic<Board, BoardDAL> {

    public static String ID = "id";
    public static String NAME = "name";
    public static String URL = "url";
    public static String HOST_ID = "hostId";

    BoardLogic() {
        super(new BoardDAL());
    }

    @Override
    public List<Board> getAll() {
        return get(() -> dal().findAll());
    }

    @Override
    public Board getWithId(int id) {
        return get(() -> dal().findById(id));
    }

    public List<Board> getBoardsWithHostID(int hostId) {
        return get(() -> dal().findByHostid(hostId));
    }

    public Board getBoardWithUrl(String url) {
        return get(() -> dal().findByUrl(url));
    }

    public List<Board> getBoardsWithName(String name) {
        return get(() -> dal().findByName(name));
    }

    @Override
    public Board createEntity(Map<String, String[]> parameterMap) {

        Objects.requireNonNull(parameterMap, "parameterMap cannot be null");
        //same as if condition below
        //        if (parameterMap == null) {
        //            throw new NullPointerException("parameterMap cannot be null");
        //        }

        //create a new Entity object
        Board entity = new Board();

        //ID is generated, so if it exists add it to the entity object
        //otherwise it does not matter as mysql will create an if for it.
        //the only time that we will have id is for update behaviour.
        if (parameterMap.containsKey(ID)) {
            try {
                entity.setId(Integer.parseInt(parameterMap.get(ID)[0]));
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }

        //Host ID is genereated, so if it exists add it to the entity object
        //otherwise it does not matter as mysql will create an if for it.
        //the only time that we will have host id is for update behaviour.
        if (parameterMap.containsKey(HOST_ID)) {
            try {
                Host host = new Host(Integer.parseInt(parameterMap.get(HOST_ID)[0]));
                entity.setHostid(host);
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }

        //Method does error checking for us.
        //Simple lambda to validate that the string has the appropriate value and length.
        ObjIntConsumer< String> validator = (value, length) -> {
            if (value == null || value.trim().isEmpty() || value.length() > length) {
                throw new ValidationException("value cannot be null, empty or larger than " + length + " characters");
            }
        };

        String url = parameterMap.get(URL)[0];
        String name = parameterMap.get(NAME)[0];

        //validate the data
        validator.accept(url, 255);
        validator.accept(name, 100);

        //set values on entity
        entity.setUrl(url);
        entity.setName(name);

        return entity;
    }
    
    /**
     * this method is used to send a list of all names to be used form table
     * column headers.
     *
     * @return list of all column names to be displayed.
     */
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Hostid", "Url", "Name");
    }

    /**
     * this method returns a list of column names that match the official column
     * names in the db. 
     *
     * @return list of all column names in DB.
     */
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, HOST_ID, URL, NAME);
    }
    
    /**
     * return the list of values of all columns (variables) in given entity.
     *
     * @param e - given Entity to extract data from.
     *
     * @return list of extracted values
     */
    @Override
    public List<?> extractDataAsList(Board e) {
        return Arrays.asList(e.getId(), e.getHostid(), e.getUrl(), e.getName());
    }

}
