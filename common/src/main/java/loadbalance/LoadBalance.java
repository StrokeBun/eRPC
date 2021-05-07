package loadbalance;

import java.util.List;

public interface LoadBalance {

    /**
     *
     * @param addressList
     * @return
     */
    String selectServiceAddress(List<String> addressList);
}
