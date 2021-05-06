package loadbalance;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/06
 */
public class HashLoadBalance extends BaseLoadBalance {

    @Override
    protected String doSelect(List<String> addressList) {
        int hashcode = 0;
        try {
            hashcode = InetAddress.getLocalHost().getHostAddress().hashCode();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return addressList.get(hashcode % addressList.size());
    }
}
