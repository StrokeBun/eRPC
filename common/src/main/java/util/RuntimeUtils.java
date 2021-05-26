package util;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/23
 */
public class RuntimeUtils {
    /**
     * get cpu core number
     * @return cpu core number
     */
    public static int cpus() {
        return Runtime.getRuntime().availableProcessors();
    }
}