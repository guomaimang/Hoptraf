package tech.hirsun.hoptraf.Result;

import lombok.Getter;
import lombok.Setter;


public class ErrorMessage {

    @Getter @Setter
    private int code;
    @Getter @Setter
    private String message;



    // Server Message, like 5000x
    public static ErrorMessage SERVER_ERROR = new ErrorMessage(50001, "Server Error");
    public static ErrorMessage REQUEST_ILLEGAL = new ErrorMessage(50003, "Request Illegal.");
    public static ErrorMessage REFUSE_SERVICE = new ErrorMessage(50004, "Access Limit Reached.");

    // Driver Message, like 5001x
    public static ErrorMessage DRIVER_NOT_EXIST = new ErrorMessage(50011, "No match found. Please try again.");


    //constructor
    private ErrorMessage() {
    }

    public ErrorMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString(){
        return "CodeMessage{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

}
