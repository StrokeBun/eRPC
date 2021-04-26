package dto;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.Arrays;

/**
 * @description: RPC request sent by client
 * @author: Stroke
 * @date: 2021/04/18
 */
@Getter
@Setter
public class Request implements Serializable {
    private static final long serialVersionUID = 2L;
    private static long CURRENT_REQUEST_ID = 1;

    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parametersType;
    private Object[] parametersValue;

    public Request() {
        requestId = String.valueOf(generateNextId());
    }

    private long generateNextId() {
        return CURRENT_REQUEST_ID++;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestId='" + requestId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parametersType=" + Arrays.toString(parametersType) +
                ", parametersValue=" + Arrays.toString(parametersValue) +
                '}';
    }

}
