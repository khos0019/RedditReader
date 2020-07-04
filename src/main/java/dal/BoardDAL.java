/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import entity.Board;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Amarjeet Singh
 */
public class BoardDAL extends GenericDAL<Board> {

    public BoardDAL() {
        super(Board.class);
    }

    @Override
    public List<Board> findAll() {
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        return findResults("Board.findAll", null);
    }

    @Override
    public Board findById(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "id" and value for it is put in map
        return findResult("Board.findById", map);
    }

    public List<Board> findByHostid(int hostId) {
        Map<String, Object> map = new HashMap<>();
        map.put("hostid", hostId);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "hostId" and value for it is put in map
        return findResults("Board.findByHostId", map);
    }

    public Board findByUrl(String url) {
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "url" and value for it is put in map
        return findResult("Board.findByUrl", map);
    }

    public List<Board> findByName(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "name" and value for it is put in map
        return findResults("Board.findByName", map);
    }
}
