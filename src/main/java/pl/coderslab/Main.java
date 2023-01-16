package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

import static pl.coderslab.ConsoleColors.*;

public class Main {

    private static String[][] tasks = new String[0][];
    private static final String TASKS_FILE_NAME = "tasks.csv";

    public static void main(String[] args) {
        /* wczytanie pliku tasks.csv */
        tasks();

        while (true) {
            String choice = menu();
            switch (choice.toLowerCase()) {
                //case "add" -> tasks = add(tasks);
                case "add" -> add();
                case "list" -> list();
                case "remove" -> remove();
                case "exit" -> exit();
                default -> {
                }

            }
        }
    }

    public static void list() {
        System.out.println("tasks.length = " + tasks.length);
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

    //public static String[][] tasks() {

        public static void tasks() {
        // Ładujemy liste tasków z pliku

        File file = new File(TASKS_FILE_NAME);

        // if file not exists try to create it.
        if (!file.exists()) {

            System.out.println(BLUE + "There is no task.csv file. First run?" + RESET);
            System.out.println(BLUE + "I am creating new one with example values.\n"+ RESET);
                    try (PrintWriter printWriter = new PrintWriter(TASKS_FILE_NAME)) {
                    printWriter.print("Simple task - very important, 2020-03-09, true\nSecond task not so important, 2020-05-10, false\nThrow away trash, 2020-03-09, false");


                } catch (IOException ex) {
                    System.out.println("Cannot create a file. Permission problem? Full space?");
                    exit();
                }
            }
        if (file.exists()) {
            StringBuilder sb = new StringBuilder();
            try (Scanner scanner = new Scanner(new File(TASKS_FILE_NAME))) {
                while (scanner.hasNextLine()) {
                    sb.append(scanner.nextLine()).append((";"));
                }
            } catch (FileNotFoundException e) {
                System.out.println("File Not Found");
                System.out.println("Starting with an empty list");
            }

            if (sb.length()>0) {
                String[] taskLines = sb.toString().split(";");
                for (String taskLine : taskLines) {
                    String[] task = taskLine.split(",");

                    tasks = ArrayUtils.add(tasks, task);
                }
            }
        } else {
            System.out.println(RED + "File tasks.csv does not exit and I cannot create it. Check your file system." + RESET);
            exit();
        }

       // return tasks;
    }

    public static void add() {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();

            System.out.print(BLUE + "\nPlease add task description ->" + RESET);
            String desc = sc.nextLine().trim();
            while (desc.length()>42) {
                System.out.print(BLUE + "\nPlease add task description\nmax 42 chr ->" + RESET);
                desc = sc.nextLine().trim();
            }
            sb.append(desc).append(", ");

            System.out.print(BLUE + "\nPlease add task due date (rrrr-mm-dd) ->" + RESET);
            //Walidacja formatu daty
            String data = sc.nextLine().trim();
            while (!GenericValidator.isDate(data, "yyyy-MM-dd", true)) {
                System.out.print(BLUE + "\nPlease provide date in proper format (yyyy-MM-dd) ->" + RESET);
                data = sc.nextLine().trim();
            }
            sb.append(data).append(", ");
            System.out.print(BLUE + "\nIs your task is important "  + YELLOW + "true/false" + BLUE + " ->"  + RESET);
            String c1 = sc.nextLine().trim().toLowerCase();
            while ( !c1.equals("true") && !c1.equals("false") ){
                System.out.print(BLUE + "\nIs your task is important: "  + YELLOW + "true/false" + BLUE + " ->" + RESET);
                c1 = sc.nextLine().trim().toLowerCase();
            }
            sb.append(c1);

            System.out.print( CYAN + "-------------------------------------------\n" + BLUE + "New task is below, should I add it to the list?\n" + GREEN + sb + RESET + "\n[y/n] ->");
                if (sc.nextLine().toLowerCase().trim().equals("y")){
                    tasks =  Arrays.copyOf(tasks, tasks.length + 1);
                    tasks[tasks.length-1] = sb.toString().split(",");
                } else {
                    System.out.println(RED + "Task will not be added." + RESET);
                }

    }

    public static void remove() {
        Scanner sc = new Scanner(System.in);

        System.out.println( CYAN + "-------------------------------" + RESET );
        System.out.println(CYAN + "| " + RED + "REMOVING TASK FROM THE LIST" + RESET + " |" + RESET);
        System.out.println( CYAN + "-------------------------------" + RESET);

        // trzeba sprawdzic czy jest cyfra i czy nie jest mniejsza od zera i czy nie jest wieksza od indexu
        boolean loop = true;
        while (loop) {
            if (tasks.length == 0 ) {
                System.out.println( BLUE + "Nothing to remove. Task list is empty!" + RESET);
                break;
            }
            System.out.print(BLUE + "Please select number to remove ->" + RESET);

            try {
                int rm = Integer.parseInt(sc.next().trim());
                if ((rm < tasks.length) && (rm >= 0)) {
                    //confirm
                    for (int i = 0; i < 3; i++) {
                        System.out.print(YELLOW + tasks[rm][i] + RESET);
                    }
                    System.out.print(BLUE + "\nAre you sure that you want to remove this task? [y/n] ->"+RESET);

                    sc = new Scanner(System.in);
                    if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                       // ten try jest tu nie potrzebny ale dodałem bo w zadaniu było aby był ;)
                       // natomiast wyjatek ten tutaj teoretyczni enigdy nie wystąpi

                        try {
                            tasks = ArrayUtils.remove(tasks, rm);
                            //tasks = ArrayUtils.clone(t1);
                            System.out.println(RED + "Task removed." + RESET);
                            loop = false;
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println(RED + "Please enter an existing task number.\n" + RESET);
                           // loop = true;
                        }
                    } else {
                        System.out.println("As you wish, I will not remove it.");
                        loop = false;
                    }

                } else {
                    System.out.println(RED + "\nIncorrect argument passed. Please give number greater or equal 0 and lower than " + tasks.length + RESET);
                }

            } catch  (NumberFormatException e) {
                System.out.println(RED + "\nIncorrect argument passed. Please give number greater or equal 0 and lower than " + tasks.length + RESET);
            }
        }
    }

    public static void exit() {
       if (tasks.length > 0 ) {


           Scanner sc = new Scanner(System.in);
           System.out.println(YELLOW + "Save tasks to file?" + RESET);

           String answer = "";
           while (!answer.equals("y") && !answer.equals("n")) {
               System.out.print("[y/n] -> ");
               answer = sc.nextLine();
           }
           if (answer.equals("y")) {
               try (PrintWriter printWriter = new PrintWriter("tasks.csv")) {
                   StringBuilder line = new StringBuilder();
                   for (int i = 0; i < tasks.length; i++) {
                       for (int j = 0; j < 3; j++) {
                           line.append(tasks[i][j]);
                           if (j < 2) {
                               line.append(",");
                           }
                       }
                       if (i < tasks.length - 1) {
                           line.append("\n");
                       }
                   }
                   printWriter.print(line);
                   System.out.println(BLUE + "The data has been written to the file tasks.csv." + RESET);
               } catch (FileNotFoundException ex) {
                   System.out.println("File Not Found Exception.");
               }
           } else {
               System.out.println("Exit without saving.");

           }
       }
        System.exit(0);
        System.out.println(RED + "Bye, bye..." + RESET);
    }

    public static String menu() {
        System.out.print(CYAN + "----------------------------\n" + RESET);
        System.out.println(CYAN + "|" + BLUE + " Please select an option " + CYAN + " | " + RESET);
        System.out.print(CYAN + "----------------------------\n"+ RESET);
        String[] menu = {"add", "remove", "list", "exit"};
        for (String s : menu) {
            System.out.printf(CYAN + "| "+ RESET + "%-24s" + CYAN + " |%n",s);
        }
        System.out.print(CYAN + "----------------------------\n" + RESET);
        System.out.print("-->");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().trim().toLowerCase();
    }
}