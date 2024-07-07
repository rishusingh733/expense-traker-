
import java.io.*;
import java.util.*;

class User {
    private String username;
    private String password;
    private static Map<String, String> users = new HashMap<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // Username already exists
        }
        users.put(username, password);
        return true;
    }

    public static boolean authenticateUser(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}

class Expense implements Serializable {
    private Date date;
    private String category;
    private double amount;

    public Expense(Date date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Date: " + date + ", Category: " + category + ", Amount: " + amount;
    }
}

class ExpenseTracker {
    private List<Expense> expenses = new ArrayList<>();

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void listExpenses() {
        for (Expense expense : expenses) {
            System.out.println(expense);
        }
    }

    public Map<String, Double> getCategoryWiseSummation() {
        Map<String, Double> categorySum = new HashMap<>();
        for (Expense expense : expenses) {
            categorySum.put(expense.getCategory(), categorySum.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
        }
        return categorySum;
    }

    public void displayCategoryWiseSummation() {
        Map<String, Double> categorySum = getCategoryWiseSummation();
        for (String category : categorySum.keySet()) {
            System.out.println("Category: " + category + ", Total: " + categorySum.get(category));
        }
    }

    public void saveExpenses(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(expenses);
        }
    }

    public void loadExpenses(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            expenses = (List<Expense>) ois.readObject();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenseTracker tracker = new ExpenseTracker();

        System.out.println("Welcome to the Expense Tracker!");
        System.out.println("1. Register");
        System.out.println("2. Login");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            if (User.registerUser(username, password)) {
                System.out.println("Registration successful!");
            } else {
                System.out.println("Username already exists.");
                return;
            }
        } else if (choice == 2) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            if (!User.authenticateUser(username, password)) {
                System.out.println("Invalid username or password.");
                return;
            }
        }

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Add Expense");
            System.out.println("2. List Expenses");
            System.out.println("3. Display Category-wise Summation");
            System.out.println("4. Save Expenses");
            System.out.println("5. Load Expenses");
            System.out.println("6. Exit");
            int menuChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (menuChoice) {
                case 1:
                    System.out.print("Enter date (yyyy-mm-dd): ");
                    Date date = java.sql.Date.valueOf(scanner.nextLine());
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    tracker.addExpense(new Expense(date, category, amount));
                    break;
                case 2:
                    tracker.listExpenses();
                    break;
                case 3:
                    tracker.displayCategoryWiseSummation();
                    break;
                case 4:
                    System.out.print("Enter filename to save: ");
                    String saveFile = scanner.nextLine();
                    try {
                        tracker.saveExpenses(saveFile);
                    } catch (IOException e) {
                        System.out.println("Error saving expenses: " + e.getMessage());
                    }
                    break;
                case 5:
                    System.out.print("Enter filename to load: ");
                    String loadFile = scanner.nextLine();
                    try {
                        tracker.loadExpenses(loadFile);
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Error loading expenses: " + e.getMessage());
                    }
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}