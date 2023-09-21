public class UserIdsGenerator {
    private int id = 0;
    private static UserIdsGenerator singleton;
    public int generateId() {
        return id++;
    }

    public static UserIdsGenerator getInstance()
    {
        if (singleton == null)
            singleton = new UserIdsGenerator();

        return singleton;
    }
}
