package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

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
                case "list":
                    list(tasks);
                    break;
                case "remove":
                    tasks = remove(tasks);
                    break;
                case "exit":
                    exit();
                    break;
                default:
                    //System.out.println(ConsoleColors.BLUE + "Please select an option:" + ConsoleColors.RESET);
            }
        }
    }

    public static void list(String[][] tasks) {
       // zrobic ładne wyswietlanie
        StringBuilder sb = new StringBuilder();

       for (int i = 0; i < tasks.length; i++) {
            System.out.print( i + " | " );
            for (int j = 0; j < tasks[i].length; j++) {
                System.out.print(tasks[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static String[][] tasks() {
        /*
        // Ładujemy liste tasków z pliku
        */
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
            System.out.println(ConsoleColors.BLUE + "\nPlease add task description below:" + ConsoleColors.RESET);
            System.out.print(" ->");
            sb.append(sc.nextLine()).append(", ");
            System.out.println(ConsoleColors.BLUE + "\nPlease add task due date (rrrr-mm-dd)" + ConsoleColors.RESET);
            System.out.print(" ->");
            sb.append(sc.nextLine()).append(", ");
            System.out.println(ConsoleColors.BLUE + "\nIs your task is important: "  + ConsoleColors.YELLOW + "true/false"  + ConsoleColors.RESET);
            System.out.print(" ->");
            String c1 = sc.nextLine();
            while ( !c1.equals("true") && !c1.equals("false") ){
                System.out.println(ConsoleColors.BLUE + "\nIs your task is important: "  + ConsoleColors.YELLOW + "true/false"  + ConsoleColors.RESET);
                System.out.print(" ->");
                c1 = sc.nextLine();
            }
            sb.append(c1);

            System.out.print("----------------------------------------\n" + ConsoleColors.RED + "Is that ok? :\n" + ConsoleColors.GREEN + sb + ConsoleColors.RESET + "\n[y/n] -> ");
                if (sc.nextLine().equals("y")){

                    tasks =  Arrays.copyOf(tasks, tasks.length + 1);
                    tasks[tasks.length-1] = sb.toString().split(",");

                    // a moze zapisac od razu do pliku ?
                    r=false; //bo krzyczy kompilator
                    return tasks;
                }
        }
        return tasks;
    }

    public static String[][] remove(String[][] tasks) {
        Scanner sc = new Scanner(System.in);

        System.out.println("-------------------------------");
        System.out.println("| " + ConsoleColors.RED + "REMOVING TASK FROM THE LIST" + ConsoleColors.RESET + " |");
        System.out.println("-------------------------------");

        // trzeba sprawdzic czy jest cyfra i czy nie jest mniejsza od zera i czy nie jest wieksza od indexu
        boolean loop = true;
        while (loop) {
            System.out.println(ConsoleColors.BLUE + "Please select number to remove:" + ConsoleColors.RESET);
            try {
                int rm = Integer.parseInt(sc.next());
                if ((rm < tasks.length) && (rm >= 0)) {
                    //confirm
                    System.out.println("Are you sure that you want to remove this task? [y/n]");
                    for (int i = 0; i < 3; i++) {
                        System.out.print(tasks[rm][i]);
                    }

                    System.out.println();
                    sc = new Scanner(System.in);
                    if (sc.nextLine().equals("y")) {
                       // ten try jest tu nie potrzebny ale dodałem bo w zadaniu było aby był ;)
                       // natomiast wyjatek ten tutaj teoretyczni enigdy nie wystąpi

                        try {
                            tasks = ArrayUtils.remove(tasks, rm);
                            //tasks = ArrayUtils.clone(t1);
                            System.out.println(ConsoleColors.RED + "Task removed." + ConsoleColors.RESET);
                            loop = false;
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println(ConsoleColors.RED + "Index Out Of Bound Exception\nPlease give number of existing task.\n" + ConsoleColors.RESET);
                            loop = true;
                        }
                    } else {
                        System.out.println("As you wish, I will not remove it.");
                        loop = false;
                    }

                } else {
                    if (rm > tasks.length) {
                        System.out.println(ConsoleColors.RED + "Index Out Of Bound Exception\nPlease give number of existing task.\n" + ConsoleColors.RESET);
                        loop = true;
                    } else {
                        System.out.println(ConsoleColors.RED + "\nIncorrect argument passed. Please give number greater or equal 0" + ConsoleColors.RESET);
                        loop = true;
                    }
                }

            } catch  (NumberFormatException e) {
                System.out.println(ConsoleColors.RED + "\nIncorrect argument passed. Please give number greater or equal 0" + ConsoleColors.RESET);
                //System.out.println(e);
                loop = true;
            }

        }

        // zapis do pliku aby nie stracić zmian
        //        System.out.println("Do you want save all changes to the file? [y/n] ");
        //        if (sc.nextLine().equals("y")) {
        //            System.out.println("Saving.");
        //       }

        return tasks;
    }

    public static int exit() {
        System.exit(0);
        return 1;
    }

    public static String menu() {
        System.out.printf("----------------------------\n");
        System.out.println("|" + ConsoleColors.BLUE + " Please select an option " + ConsoleColors.RESET + " | ");
        System.out.printf("----------------------------\n");
        String[] menu = {"add", "remove", "list", "exit"};
        for (String s : menu) {
            System.out.printf("| %-24s |%n",s);
        }
        System.out.printf("----------------------------\n");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
}