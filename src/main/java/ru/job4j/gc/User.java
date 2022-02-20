package ru.job4j.gc;

public class User {
    private final String firstName;
    private final String lastName;
    private final String profession;
    private final int age;
    private final float weight;
    private final float height;

    public User(String firstName, String lastName, String profession, int age, float weight, float height) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profession = profession;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.printf("Removed %s %s %s %d %f %f%n", firstName, lastName, profession, age, weight, height);
    }


    public static void main(String[] args) {
        info();
        User user1 = new User("Alexandr", "Ivanov",
                "Blockchain Developer", 27, 85, 182);
        User user2 = new User("Eduard", "Reznov",
                "Auditor", 26, 65, 175);
        User user3 = new User("Anton", "Ershov",
                "Fitness Instructor", 26, 90, 172);
        User user4 = new User("Vladislav", "Bondar",
                "Java Developer", 26, 95, 192);
        User user5 = new User("Denis", "Kalchenko",
                "Analyst", 27, 65, 175);
        int b = 5 * (24 + 24 + 24 + 4 + 4 + 4);
        float mb = (float) b / 1024 / 1024;
        System.out.printf("Размер пяти объектов = %d байт или %f мегабайт%n%n", b, mb);
        info();
    }

    public static void info() {
        Runtime runtime = Runtime.getRuntime();
        System.out.println("=== Environment state ===");
        System.out.printf("Free memory: %d%n", runtime.freeMemory());
        System.out.printf("Total memory: %d%n", runtime.totalMemory());
        System.out.printf("Max memory: %d%n", runtime.maxMemory());
    }
}
