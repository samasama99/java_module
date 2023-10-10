public class UserIdsGenerator {
    private static UserIdsGenerator singleton;
    private int id = 1;

    public static UserIdsGenerator getInstance() {
        if (singleton == null) {
            singleton = new UserIdsGenerator();
        }

        return singleton;
    }

    public int generateId() {
        return id++;
    }
}
