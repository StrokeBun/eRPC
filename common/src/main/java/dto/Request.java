package dto;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.Arrays;
import java.util.UUID;

/**
 * @description: RPC request sent by client
 * @author: Stroke
 * @date: 2021/04/18
 */
@Getter
@Setter
public class Request implements Serializable {
    private static final long serialVersionUID = 2L;
    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parametersType;
    private Object[] parametersValue;


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
