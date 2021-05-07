package loadbalance;

import java.util.List;
import java.util.Random;

/**
 * @description: random load balance
 * @author: Stroke
 * @date: 2021/05/06
 */
public class RandomLoadBalance extends BaseLoadBalance {

    @Override
    protected String doSelect(List<String> addressList) {
        int index = new Random().nextInt(addressList.size());
        return addressList.get(index);
    }
}
