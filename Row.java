
import java.util.LinkedHashMap;

/**
 * A row from a database, represented as map.
 * Column name to Object.
 */
public class Row extends LinkedHashMap<String, Object>{
    /**
     * Gets the value from the column.
     * @param column The column label to get data from.
     * @return The data in the column, or null if column doesn't exist.
     */
    public Object getValue(String column){
        if(this.containsKey(column)){
            return this.get(column);
        }
        else{
            return null;
        }
    }

    /**
     * Gets the value from the column as an int.
     * @param column The column label to get data from.
     * @return The data in the column, or Max int if column doesn't exist.
     */
    public int getValueAsInt(String column){
        if(this.containsKey(column)){
            return (int)this.get(column);
        }
        else{
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Gets the value from the column as a string.
     * @param column The column label to get data from.
     * @return The data in the column, or "" if column doesn't exist.
     */
    public String getValueAsString(String column){
        if(this.containsKey(column)){
            return (String)this.get(column);
        }
        else{
            return "";
        }
    }

    /**
     * Gets the value from the column as a float.
     * @param column The column label to get data from.
     * @return The data in the column, or +infinity if column doesn't exist.
     */
    public float getValueAsFloat(String column){
        if(this.containsKey(column)){
            return (float)this.get(column);
        }
        else{
            return 0.0f;
        }
    }

}
