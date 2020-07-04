/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;
import entity.Image;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Amarjeet Singh
 */
public class ImageDAL extends GenericDAL<Image>{
    public ImageDAL() {
        super( Image.class);
    }
    
    @Override
    public List<Image> findAll(){
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        return findResults( "Image.findAll", null);
    }
    
    @Override
    public Image findById( int id){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "id" and value for it is put in map
        return findResult( "Image.findById", map);
    }
    
    public List<Image> findByBoardId( int boardId){
        Map<String, Object> map = new HashMap<>();
        map.put("boardid", boardId);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "boardId" and value for it is put in map
        return findResults( "Image.findByBoardId", map);
    }
    
    public List<Image> findByTitle( String title){
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "title" and value for it is put in map
        return findResults( "Image.findByTitle", map);
    }
    
    public Image findByUrl( String url){
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "url" and value for it is put in map
        return findResult( "Image.findByUrl", map);
    }
    
    public List<Image> findByDate(Date date){
        Map<String, Object> map = new HashMap<>();
        map.put("date", date);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "date" and value for it is put in map
        return findResults( "Image.findByDate", map);
    }
    
    public Image findByLocalPath(String localPath){
        Map<String, Object> map = new HashMap<>();
        map.put("localPath", localPath);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "localPath" and value for it is put in map
        return findResult( "Image.findByLocalPath", map);
    }
}
