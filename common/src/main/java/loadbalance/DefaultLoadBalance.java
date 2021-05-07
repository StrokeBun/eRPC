package loadbalance;

import java.util.List;

/**
 * @description: default load balance, always return first address
 * @author: Stroke
 * @date: 2021/05/06
 */
public class DefaultLoadBalance extends BaseLoadBalance {

    @Override
    protected String doSelect(List<String> addressList) {
        return addressList.get(0);
    }
}
