package loadbalance;

import java.util.List;

public interface LoadBalance {

    String selectServiceAddress(List<String> addressList);
}
