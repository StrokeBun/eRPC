import entity.User;
import service.UserService;
import stub.ClientStub;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/21
 */
public class Client {
    public static void main(String[] args) {
        UserService service = (UserService) ClientStub.getInstance(UserService.class);
        User user = service.getUser(1, "bzzb");
        if (user != null) {
            System.out.println(user.toString());
        }
    }
}
