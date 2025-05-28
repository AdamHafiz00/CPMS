
import java.io.IOException;
import java.util.List;

public interface PersonOperations<PersonType> {            // Generic interface for person operations
    void add(PersonType person) throws IOException;         // add a new person to the file
    void update(PersonType person) throws IOException;      // update an existing person in the file
    void delete(String id) throws IOException;              // delete a person by their ID
    PersonType searchById(String id) throws IOException;    // search for a person by their ID
    List<PersonType> getAll() throws IOException;           // retrieve all persons from the file
}
