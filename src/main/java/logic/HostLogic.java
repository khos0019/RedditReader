/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import dal.HostDAL;
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
public class HostLogic extends GenericLogic<Host, HostDAL> {

    public static String ID = "id";
    public static String NAME = "name";
    public static String URL = "url";
    public static String EXTRACTION_TYPE = "extractionType";

    HostLogic() {
        super(new HostDAL());
    }

    @Override
    public List<Host> getAll() {
        return get(() -> dal().findAll());
    }

    @Override
    public Host getWithId(int id) {
        return get(() -> dal().findById(id));
    }

    public Host getHostWithName(String name) {
        return get(() -> dal().findByName(name));
    }

    public Host getHostWithUrl(String url) {
        return get(() -> dal().findByUrl(url));
    }

    public List<Host> getHostWithExtractionType(String type) {
        return get(() -> dal().findByExtractionType(type));
    }

    @Override
    public Host createEntity(Map<String, String[]> parameterMap) {

        Objects.requireNonNull(parameterMap, "parameterMap cannot be null");
        //same as if condition below
        //        if (parameterMap == null) {
        //            throw new NullPointerException("parameterMap cannot be null");
        //        }

        //create a new Entity object
        Host entity = new Host();

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

        //Method does error checking for us.
        //Simple lambda to validate that the string has the appropriate value and length.
        ObjIntConsumer< String> validator = (value, length) -> {
            if (value == null || value.trim().isEmpty() || value.length() > length) {
                throw new ValidationException("value cannot be null, empty or larger than " + length + " characters");
            }
        };

        String name = parameterMap.get(NAME)[0];
        String url = parameterMap.get(URL)[0];
        String extraction_type = parameterMap.get(EXTRACTION_TYPE)[0];

        //validate the data
        validator.accept(name, 100);
        validator.accept(url, 255);
        validator.accept(extraction_type, 45);

        //set values on entity
        entity.setName(name);
        entity.setUrl(url);
        entity.setExtractionType(extraction_type);

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
        return Arrays.asList("ID", "Name", "Url", "ExtractionType");
    }
    
    /**
     * this method returns a list of column names that match the official column
     * names in the db. 
     *
     * @return list of all column names in DB.
     */
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, NAME, URL, EXTRACTION_TYPE);
    }

    /**
     * return the list of values of all columns (variables) in given entity.
     *
     * @param e - given Entity to extract data from.
     *
     * @return list of extracted values
     */
    @Override
    public List<?> extractDataAsList(Host e) {
        return Arrays.asList(e.getId(), e.getName(), e.getUrl(), e.getExtractionType());
    }

}
