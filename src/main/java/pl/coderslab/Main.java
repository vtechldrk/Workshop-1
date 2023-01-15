package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import static pl.coderslab.ConsoleColors.*;

public class Main {
   // private static String[][] tasks = new String[0][];
    public static void main(String[] args) {
        /* wczytanie pliku tasks.csv */
        String[][] tasks = tasks();
       // tasks();

        while (true) {
            String choice = menu();
            switch (choice.toLowerCase()) {
                case "add" -> tasks = add(tasks);
                case "list" -> list(tasks);
                case "remove" -> tasks = remove(tasks);
                //case "modify" -> tasks = modify(tasks);
                case "exit" -> exit(tasks);

                default -> {
                }

            }
        }
    }

    public static void list(String[][] tasks) {

        System.out.println(CYAN + "-----------------------------------------------------------------------------");
        System.out.println("| id | Description                                | Due date   | Importance |");
        System.out.println("-----------------------------------------------------------------------------" + RESET);

        for (int i = 0; i < tasks.length; i++) {

            String s0 = Integer.toString(i);
            String s1 = tasks[i][0];
            String s2 = tasks[i][1];
            String s3 = tasks[i][2];
            System.out.printf( CYAN + "|" + RESET + " %2s " + CYAN + "|" + RESET + " %-42s "
                    + CYAN + "|" + RESET + "%11s " + CYAN + "|" + RESET + "%-11s " + CYAN + "|%n"+RESET, s0, s1, s2, s3);
        }
        System.out.println(CYAN + "-----------------------------------------------------------------------------" + RESET);
    }

    public static String[][] tasks() {
         String[][] tasks;
        //   tasks = new String[1][];
        /*
        // Ładujemy liste tasków z pliku
        */
        // liczymy ilosc linii w pliku;
        long count;
        Path path = Paths.get("tasks.csv");
        boolean isExist = true;
        try {
            count = Files.lines(path).count();
        } catch ( IOException e) {
            isExist = false;
            count = 1;
        }

        int row = (int)count; //konwersja long na int

        // if dile not exists we will try to create it.
        if (!isExist) {

            Scanner sc = new Scanner(System.in);
            System.out.println("There is no task.csv file. First run?");
            System.out.println("Do you want to create one with example values?");
            String answer = "";
            while (!answer.equals("y") && !answer.equals("n")) {
                System.out.print("[y/n]-> ");
                answer = sc.nextLine();
            }
            if (answer.equals("y")) {
                try (PrintWriter printWriter = new PrintWriter("tasks.csv")) {
                    printWriter.print("Simple task - very important, 2020-03-09, true\nSecond task not so important, 2020-05-10, false\nThrow away trash, 2020-03-09, false");
                    isExist=true;
                    row = 3; // to create proper array[3][3] 3 rows and 3 cols
                } catch (IOException ex) {
                    System.out.println("Cannot create a file. Permission problem? Full space?");
                    //isExist=false;
                }
            }
        }
        if (isExist) {
            tasks = new String[row][3]; // creating array based on row calc above
            //trying to load a file. Even it wasn't exist should be there now.
            row = 0;
            File file = new File("tasks.csv");
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    tasks[row] = scanner.nextLine().split(",");
                    row++;
                }
            } catch (FileNotFoundException e) {
                System.out.println("File Not Found");
                System.out.println("Starting with empty list");
            }
        } else {
            tasks = new String[][]{{"null","null","null"}};
            //Arrays.fill(tasks, 1);
        }
        return tasks;
    }

    public static String[][] add(String[][] tasks) {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();

            System.out.println(BLUE + "\nPlease add task description:" + RESET);
            System.out.print(" ->");
            String desc = sc.nextLine();
            while (desc.length()>42) {
                System.out.println(BLUE + "\nPlease add task description\nmax 42 characters:" + RESET);
                System.out.print(" ->");
                desc = sc.nextLine();
            }
            sb.append(desc).append(", ");

            System.out.println(BLUE + "\nPlease add task due date (rrrr-mm-dd)" + RESET);
            System.out.print(" ->");
            //Walidacja formatu daty
            String data = sc.nextLine();
            while (!GenericValidator.isDate(data, "yyyy-MM-dd", true)) {
                System.out.println(BLUE + "\nPlease provide date in proper format" + RESET);
                System.out.print(" ->");
                data = sc.nextLine();
            }
            sb.append(data).append(", ");
            System.out.println(BLUE + "\nIs your task is important: "  + YELLOW + "true/false"  + RESET);
            System.out.print(" ->");
            String c1 = sc.nextLine();
            while ( !c1.equals("true") && !c1.equals("false") ){
                System.out.println(BLUE + "\nIs your task is important: "  + YELLOW + "true/false"  + RESET);
                System.out.print(" ->");
                c1 = sc.nextLine();
            }
            sb.append(c1);

            System.out.print("----------------------------------------\n" + BLUE + "New task is below, should I add it to the list?\n" + GREEN + sb + RESET + "\n[y/n] ->");
                if (sc.nextLine().equals("y")){
                    tasks =  Arrays.copyOf(tasks, tasks.length + 1);
                    tasks[tasks.length-1] = sb.toString().split(",");
                    // a moze zapisac od razu do pliku ?
                    return tasks;
                } else {
                    return tasks;
                }

    }

    public static String[][] remove(String[][] tasks) {
        Scanner sc = new Scanner(System.in);

        System.out.println("-------------------------------");
        System.out.println("| " + RED + "REMOVING TASK FROM THE LIST" + RESET + " |");
        System.out.println("-------------------------------");

        // trzeba sprawdzic czy jest cyfra i czy nie jest mniejsza od zera i czy nie jest wieksza od indexu
        boolean loop = true;
        while (loop) {
            if (tasks.length == 0 ) {
                System.out.println("Nothing to remove. Task list is empty!");
                break;
            }
            System.out.println(BLUE + "Please select number to remove:" + RESET);
            System.out.print(" ->");
            //
            //przeczytalem o tej metodzie NumberUtils.isParsable() na końcu więc już nie mieszam bo działa...
            //
            try {
                int rm = Integer.parseInt(sc.next());

                if ((rm < tasks.length) && (rm >= 0)) {
                    //confirm
                    System.out.println("Are you sure that you want to remove this task?");
                    for (int i = 0; i < 3; i++) {
                        System.out.print(YELLOW + tasks[rm][i] + RESET);
                    }
                    System.out.print("\n[y/n]->");

                    sc = new Scanner(System.in);
                    if (sc.nextLine().equals("y")) {
                       // ten try jest tu nie potrzebny ale dodałem bo w zadaniu było aby był ;)
                       // natomiast wyjatek ten tutaj teoretyczni enigdy nie wystąpi

                        try {
                            tasks = ArrayUtils.remove(tasks, rm);
                            //tasks = ArrayUtils.clone(t1);
                            System.out.println(RED + "Task removed." + RESET);
                            loop = false;
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println(RED + "Index Out Of Bound Exception\nPlease give number of existing task.\n" + RESET);
                           // loop = true;
                        }
                    } else {
                        System.out.println("As you wish, I will not remove it.");
                        loop = false;
                    }

                } else {
                    if (rm > tasks.length) {
                        System.out.println(RED + "Index Out Of Bound Exception\nPlease give number of existing task.\n" + RESET);
                        //loop = true;
                    } else {
                        System.out.println(RED + "\nIncorrect argument passed. Please give number greater or equal 0" + RESET);
                        //loop = true;
                    }
                }

            } catch  (NumberFormatException e) {
                System.out.println(RED + "\nIncorrect argument passed. Please give number greater or equal 0" + RESET);

            }

        }
        return tasks;
    }

    public static void exit(String[][] tasks) {
        Scanner sc = new Scanner(System.in);
        System.out.println(YELLOW + "Save tasks to file?" + RESET);

        String answer = "";
        while (!answer.equals("y") && !answer.equals("n") ) {
            System.out.print("[y/n] -> ");
            answer = sc.nextLine();
        }
        if (answer.equals("y")) {
            try (PrintWriter printWriter = new PrintWriter("tasks.csv")) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < tasks.length; i++) {
                    for (int j = 0; j < 3; j++) {
                        line.append(tasks[i][j]);
                        if ( j < 2 ) {line.append(",");}
                    }
                    if ( i < tasks.length - 1 ) {line.append("\n");}
                }
                printWriter.print(line);
                System.out.println(BLUE + "The data has been written to the file tasks.csv." + RESET);
            } catch (FileNotFoundException ex) {
                System.out.println("File Not Found Exception.");
            }
        } else {
            System.out.println("Exit without saving.");

        }

        System.exit(0);
        System.out.println(RED + "Bye, bye..." + RESET);
    }

    public static String menu() {
        System.out.print("----------------------------\n");
        System.out.println("|" + BLUE + " Please select an option " + RESET + " | ");
        System.out.print("----------------------------\n");
        String[] menu = {"add", "remove", "list", "modify (future)", "exit"};
        for (String s : menu) {
            System.out.printf("| %-24s |%n",s);
        }
        System.out.print("----------------------------\n");
        System.out.print("-->");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
}