package pl.coderslab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // wczytanie pliku tasks.csv
        String[][] tasks = tasks();

        while (true) {
            String choice = menu();
            switch (choice) {
                case "add":
                    tasks = add(tasks);

                    break;
                case "exit":
                    exit();
                    break;
                default:
                    System.out.println(ConsoleColors.BLUE + "Please select an option:" + ConsoleColors.RESET);

            }
        }
    //                case "remove":
    //                    remove();
    //                    break;
    //                case "list":
    //                    list();
    //                    break;
    //                case "exit":
    //                    exit();
    //                    break;

    }

    public static String[][] tasks() {
        // liczymy ilosc linii w pliku;
        long count = 0;
        Path path = Paths.get("tasks.csv");
        try {
            count = Files.lines(path).count();
        } catch ( IOException e) {
            e.printStackTrace();
        }
        int row = (int)count; //konwersja long na int

        // tworzymy odpowiednią tablice z 3 kolumnami
        String[][] tasks = new String[row][3];

        //ładujemy plik - jak go nie ma to tworzymy z przykladowym wierszem / sprawdzic if exist
        row = 0;
        File file = new File("tasks.csv");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                    tasks[row] = scanner.nextLine().split(",");
                    row++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Błąd odczytu pliku: " + e);

        }

        return tasks;
    }

    public static String[][] add(String[][] tasks) {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        boolean r = true;
        while (r) {
            System.out.println("Please add task description");
            sb.append(sc.nextLine()).append(", ");
            System.out.println("Please add task due date (rrrr-mm-dd)");
            sb.append(sc.nextLine()).append(", ");
            System.out.println("Is your task is important: true/false");
            sb.append(sc.nextLine());
            System.out.print("----------------------------------------\n" + ConsoleColors.RED + "Is that ok? (y/n):\n" + ConsoleColors.GREEN + sb + ConsoleColors.RESET + "\n\n->");
                if (sc.nextLine().equals("y")){

                    String[][] tmp = Arrays.copyOf(tasks, tasks.length + 1);
                    tmp[tmp.length-1] = sb.toString().split(",");
                    // a moze zapisac od razu do pliku ?
                    tasks = Arrays.copyOf(tmp,tmp.length);

                    for (String[] task : tasks) {
                        for (int j = 0; j < task.length; j++) {
                            System.out.print(task[j]);
                        }
                        System.out.println();
                    }
                    r=false; //bo krzyczy kompilator
                    return tasks;
                }
        }

        return tasks;
    }

    public static int exit() {
        System.exit(0);
        return 1;
    }

    public static String menu() {
        System.out.println(ConsoleColors.BLUE + "Please select an option:" + ConsoleColors.RESET);
        String[] menu = {"add", "remove", "list", "exit"};
        for (String s : menu) {
            System.out.println(s);
        }
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
}