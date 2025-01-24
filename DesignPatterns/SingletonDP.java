class Singleton {
    // Declare the instance as volatile to ensure visibility across threads
    private static volatile Singleton obj = null;

    // Private constructor to prevent instantiation
    private Singleton() {}

    // Public method to provide the global point of access
    public static Singleton getInstance() {
        if (obj == null) { // First check (no locking)
            synchronized (Singleton.class) { // Lock the class for thread safety
                if (obj == null) { // Second check (after acquiring the lock)
                    obj = new Singleton();
                }
            }
        }
        return obj;
    }
}
