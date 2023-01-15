package pl.coderslab;

        import org.apache.commons.lang3.ArrayUtils;
        import org.apache.commons.lang3.StringUtils;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.PrintWriter;
        import java.util.Scanner;

        import static pl.coderslab.ConsoleColors.*;

public class Application {

    private static final String[] OPTIONS = {"add", "remove", "list", "exit"};
    private static final String EXIT_OPTION = "exit";
    private static final String TASKS_FILE_NAME = "tasks.csv";

    private static String[][] tasks = new String[0][];

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        printWelcomeMessage();
        loadTasks();
        while (true) {
            printMenu();
            String option = selectOption();
            if (!validOption(option)) {
                printErrorMessage(option);
                continue;
            }
            executeOption(option);
            if (isExitOption(option)) {
                break;
            }
        }
        saveTasks();
        printExitMessage();
    }

    private static void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new File(TASKS_FILE_NAME))) {
            for (String[] task : tasks) {
                String taskLine = StringUtils.join(task, ",");
                writer.println(taskLine);
            }
            System.out.println(PURPLE + tasks.length + " tasks have been written" + RESET);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void loadTasks() {
        StringBuilder dataBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(TASKS_FILE_NAME))) {
            while (scanner.hasNextLine()) {
                dataBuilder.append(scanner.nextLine()).append(";");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] taskLines = dataBuilder.toString().split(";");
        for (String taskLine : taskLines) {
            String[] task = taskLine.split(",");
            tasks = ArrayUtils.add(tasks, task);
        }
        System.out.println(PURPLE + tasks.length + " tasks have bean read" + RESET);
    }

    private static void printExitMessage() {
        System.out.println(BLUE + "Bye, bye" + RESET);
    }

    private static void executeOption(String option) {
        switch (option) {
            case "add": {
                addTask();
                break;
            }
            case "remove": {
                removeTask();
                break;
            }
            case "list": {
                listTasks();
                break;
            }
            default: {
                break;
            }
        }
    }

    private static void listTasks() {
        System.out.printf("%2s | %-40s | %-10s | %-5s |%n", "Lp", "Opis", "Data", "Pr");
        System.out.println("-".repeat(68));
        for (int i = 0; i < tasks.length; i++) {
            String[] task = tasks[i];
            System.out.printf("%2d | %40s | %10s | %5s |%n", i, task[0].substring(0, Math.min(task[0].length(), 40)), task[1], task[2]);
        }
        System.out.println("-".repeat(68));
    }

    private static void removeTask() {
        if (tasks.length == 0) {
            System.out.println(RED + "No tasks to remove" + RESET);
            return;
        }
        System.out.print(BLUE + "Please select index to remove > " + RESET);
        Scanner scanner = new Scanner(System.in);
        int index;
        while (true) {
            while (!scanner.hasNextInt()) {
                scanner.nextLine();
                System.out.print(RED + "Invalid argument passed. Please give number between 0 and " + (tasks.length - 1) + ": " + RESET);
            }
            index = scanner.nextInt();
            if (index >= 0 && index < tasks.length) {
                break;
            } else {
                System.out.print(RED + "Invalid argument passed. Please give number between 0 and " + (tasks.length - 1) + ": " + RESET);
            }
        }
        String[] task = tasks[index];
        System.out.print(BLUE + "Please confirm (Y/y) to remove task '" + task[0] + "' > " + RESET);
        scanner = new Scanner(System.in);
        String confirmed = scanner.nextLine();
        if ("y".equalsIgnoreCase(confirmed)) {
            tasks = ArrayUtils.remove(tasks, index);
            System.out.println("Task was successfully deleted");
        }
    }

    private static void addTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please add task description > ");
        String description = scanner.nextLine().trim();
        System.out.print("Please add task due date > ");
        String dueDate = scanner.nextLine().trim();
        String important = null;
        do {
            System.out.print("Is your task important (true/false)? > ");
            important = scanner.nextLine().trim();
        } while (!("false".equals(important) || "true".equals(important)));
        tasks = ArrayUtils.add(tasks, new String[]{description, dueDate, important});
        System.out.println("Task was successfully added");
    }

    private static boolean isExitOption(String option) {
        return EXIT_OPTION.equalsIgnoreCase(option);
    }

    private static void printErrorMessage(String option) {
        System.out.println(RED + "Invalid menu option: '" + option + "'" + RESET);
    }

    private static boolean validOption(String option) {
        return ArrayUtils.contains(OPTIONS, option);
    }

    private static String selectOption() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().trim().toLowerCase();
    }

    private static void printMenu() {
        System.out.println("\nAvailable options:");
        for (String option : OPTIONS) {
            System.out.println(" " + option);
        }
        System.out.print(BLUE + "Please select an option > " + RESET);
    }

    private static void printWelcomeMessage() {
//        System.getProperties().list(System.out);
        String userName = System.getProperty("user.name");
        System.out.println("Hello " + userName);
    }
}