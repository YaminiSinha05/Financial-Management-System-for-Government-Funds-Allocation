import java.util.ArrayList;
import java.util.List;

abstract class FinancialEntity {
    protected Money budget;

    public Money getBudget() {
        return budget;
    }

    public abstract void allocateFunds(String source, double amount);

    @Override
    public abstract String toString();
}

class Money extends FinancialEntity {
    private double amount;

    public Money(double amount) {
        this.amount = amount;
        this.budget = this;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "$" + amount;
    }

    @Override
    public void allocateFunds(String source, double amount) {
        // Money class does not allocate funds
    }
}

class NonTaxRevenue extends FinancialEntity {
    private String source;

    public NonTaxRevenue(String source, double amount) {
        this.source = source;
        this.budget = new Money(amount);
    }

    @Override
    public void allocateFunds(String source, double amount) {
        // NonTaxRevenue class does not allocate funds
    }

    @Override
    public String toString() {
        return source + ": " + budget;
    }
}

class Scheme extends FinancialEntity {
    private String name;
    private List<NonTaxRevenue> revenues;

    public Scheme(String name) {
        this.name = name;
        this.budget = new Money(0);
        this.revenues = new ArrayList<>();
    }

    @Override
    public void allocateFunds(String source, double amount) {
        NonTaxRevenue revenue = new NonTaxRevenue(source, amount);
        revenues.add(revenue);
        budget.setAmount(budget.getAmount() + amount);
    }

    public String displayRevenues() {
        StringBuilder result = new StringBuilder();
        for (NonTaxRevenue revenue : revenues) {
            result.append(revenue).append("\n");
        }
        return result.toString();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Scheme: " + name + ", Allocation: " + budget + ", Revenues:\n" + displayRevenues();
    }
}

class Department extends FinancialEntity {
    private String name;
    private List<Scheme> schemes;

    public Department(String name, double initialBudget) {
        this.name = name;
        this.budget = new Money(initialBudget);
        this.schemes = new ArrayList<>();
    }

    public void addScheme(Scheme scheme) {
        schemes.add(scheme);
    }

    @Override
    public void allocateFunds(String source, double amount) {
        // Department class does not allocate funds
    }

    public List<Scheme> getSchemes() {
        return schemes;
    }

    public String displaySchemes() {
        StringBuilder result = new StringBuilder();
        for (Scheme scheme : schemes) {
            result.append(scheme).append("\n");
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return "Department: " + name + ", Budget: " + budget + ", Schemes:\n" + displaySchemes();
    }
}

class Employee {
    private String name;
    private FinancialEntity financialEntity;

    public Employee(String name, FinancialEntity financialEntity) {
        this.name = name;
        this.financialEntity = financialEntity;
    }

    public void requestFunds(Scheme scheme, String source, double amount) {
        financialEntity.allocateFunds(source, amount);
    }

    @Override
    public String toString() {
        return "Employee: " + name + ", Department: " + financialEntity;
    }
}

class Transaction {
    private Money source;
    private Money destination;
    private Money amount;

    public Transaction(Money source, Money destination, double amount) {
        this.source = source;
        this.destination = destination;
        this.amount = new Money(amount);
    }

    public void execute() {
        if (source.getAmount() >= amount.getAmount()) {
            source.setAmount(source.getAmount() - amount.getAmount());
            destination.setAmount(destination.getAmount() + amount.getAmount());
            System.out.println("Transaction of " + amount + " successful.");
        } else {
            System.out.println("Insufficient funds for the transaction.");
        }
    }

    @Override
    public String toString() {
        return "Transaction: " + source + " -> " + destination + ", Amount: " + amount;
    }
}

class Government extends FinancialEntity {
    private String name;
    private List<Department> departments;

    public Government(String name, double budget) {
        this.name = name;
        this.budget = new Money(budget);
        this.departments = new ArrayList<>();
    }

    public Department createDepartment(String name, double initialBudget) {
        Department department = new Department(name, initialBudget);
        departments.add(department);
        return department;
    }

    @Override
    public void allocateFunds(String source, double amount) {
        // Government class does not allocate funds
    }

    public String displayDepartments() {
        StringBuilder result = new StringBuilder();
        for (Department department : departments) {
            result.append(department).append("\n");
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return "Government: " + name + ", Budget: " + budget + ", Departments:\n" + displayDepartments();
    }
}

public class Main {
    public static void main(String[] args) {
        Government government = new Government("Central Government", 1500000);

        Department healthDepartment = government.createDepartment("Health Department", 500000);
        Department educationDepartment = government.createDepartment("Education Department", 500000);
        Department agricultureDepartment = government.createDepartment("Agriculture Department", 500000);

        Scheme healthcareScheme = new Scheme("Healthcare Scheme");
        Scheme educationScheme = new Scheme("Education Scheme");
        Scheme agricultureScheme = new Scheme("Agriculture Scheme");

        healthDepartment.addScheme(healthcareScheme);
        educationDepartment.addScheme(educationScheme);

        Employee john = new Employee("John", healthDepartment);
        Employee mary = new Employee("Mary", educationDepartment);

        System.out.println(government);

        john.requestFunds(healthcareScheme, "Donations", 50000);
        mary.requestFunds(educationScheme, "Fees", 75000);

        System.out.println(healthDepartment);
        System.out.println(educationDepartment);
        System.out.println(government);
    }
}
