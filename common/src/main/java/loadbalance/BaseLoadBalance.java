package loadbalance;

import java.util.List;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/06
 */
public abstract class BaseLoadBalance implements LoadBalance {

    @Override
    public String selectServiceAddress(List<String> addressList) {
        if (addressList == null || addressList.isEmpty()) {
            return null;
        }
        if (addressList.size() == 1) {
            return addressList.get(0);
        }
        return doSelect(addressList);
    }

    protected abstract String doSelect(List<String> addressList);
}
