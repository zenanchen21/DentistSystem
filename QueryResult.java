import java.util.ArrayList;

/**
 * A class which represents a result of a database query,
 * it is a list of rows.
 */
public class QueryResult extends ArrayList<Row> {

    /**
     * fetches a row from the results.
     * @param index The row index.
     * @return The request row, or an empty row if index isn't in range.
     */
    public Row getRow(int index){
        if(index < this.size() && index > -1) {
            return this.get(index);
        }
        else{
            return new Row();
        }
    }

    /**
     * Fetches the index of the last row.
     * @return index of the last row, int.
     */
    public int lastRow(){
        return this.size() - 1;
    }

    public String toString(){
        String s = "Results: \n\t";
        for(Row r : this){
            s += r;
            s+= "\n\t";
        }
        return s;
    }
}

