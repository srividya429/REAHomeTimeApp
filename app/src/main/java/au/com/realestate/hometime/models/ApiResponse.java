package au.com.realestate.hometime.models;
import java.util.List;

public class ApiResponse<T> {
    public String errorMessage;
    public Boolean hasError;
    public Boolean hasResponse;
    public List<T> responseObject;
}


