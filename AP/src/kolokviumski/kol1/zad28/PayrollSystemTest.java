package kolokviumski.kol1.zad28;

import java.util.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.Comparator;

 abstract class Employee implements Comparable<Employee> {

    String ID;
    String level;
    double rate;

    public Employee(String ID, String level, double rate) {
        this.ID = ID;
        this.level = level;
        this.rate = rate;
    }

    abstract double calculateSalary();

    public String getLevel() {
        return level;
    }

    public String getID() {
        return ID;
    }

    @Override
    public int compareTo(Employee o) {
        return Comparator
                .comparing(Employee::calculateSalary, Comparator.reverseOrder())
                .thenComparing(Employee::getLevel)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f", ID, level, calculateSalary());
    }
}
 class FreelanceEmployee extends Employee {

    List<Integer> ticketPoints;

    public FreelanceEmployee(String ID, String level, double rate, List<Integer> ticketPoints) {
        super(ID, level, rate);
        this.ticketPoints = ticketPoints;
    }

    @Override
    double calculateSalary() {
        return ticketPoints.stream().mapToInt(tp -> tp).sum() * rate;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
                " Tickets count: %d Tickets points: %d",
                ticketPoints.size(),
                ticketPoints.stream().mapToInt(i -> i).sum()
        );
    }
}
 class HourlyEmployee extends Employee {

    double hours;
    double overtime;
    double regular;

    public HourlyEmployee(String ID, String level, double rate, double hours) {
        super(ID, level, rate);
        this.hours = hours;
        this.overtime = Math.max(0, hours - 40);
        this.regular = hours - overtime;
    }

    @Override
    double calculateSalary() {
        return regular * rate + overtime * rate * 1.5;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" Regular hours: %.2f Overtime hours: %.2f", regular, overtime);
    }
}

class EmployeeFactory {

    public static Employee createEmployee(String line, Map<String, Double> hourlyRate, Map<String, Double> ticketRate) {
        String[] parts = line.split(";");
        String type = parts[0];
        String id = parts[1];
        String level = parts[2];

        if (type.equalsIgnoreCase("H")) { // HourlyEmployee
            double hours = Double.parseDouble(parts[3]);
            Double rate = hourlyRate.get(level);
            if (rate == null) {
                throw new IllegalArgumentException("Unknown hourly level: " + level);
            }
            return new HourlyEmployee(id, level, rate, hours);
        } else if (type.equalsIgnoreCase("F")) { // FreelanceEmployee
            Double rate = ticketRate.get(level);
            if (rate == null) {
                throw new IllegalArgumentException("Unknown freelance level: " + level);
            }

            List<Integer> ticketPoints = new ArrayList<>();
            for (int i = 3; i < parts.length; i++) {
                ticketPoints.add(Integer.parseInt(parts[i]));
            }

            return new FreelanceEmployee(id, level, rate, ticketPoints);
        } else {
            throw new IllegalArgumentException("Unknown employee type: " + type);
        }
    }
}
 class PayrollSystem {
    Map<String, Double> hourlyRate;
    Map<String, Double> ticketRate;
    List<Employee> employees;

    public PayrollSystem(Map<String, Double> hourlyRate, Map<String, Double> ticketRate) {
        this.hourlyRate = hourlyRate;
        this.ticketRate = ticketRate;
        this.employees = new ArrayList<>();
    }

    void readEmployeesData(InputStream is) {
        this.employees = new BufferedReader(new InputStreamReader(is))
                .lines()
                .filter(l -> l != null && !l.trim().isEmpty())
                .map(line -> EmployeeFactory.createEmployee(line, hourlyRate, ticketRate))
                .collect(Collectors.toList());
    }

    Map<String, TreeSet<Employee>> printEmployeesByLevels(OutputStream os, Set<String> levels) {
        Map<String, TreeSet<Employee>> employeesByLevel = new HashMap<>();

        for (Employee employee : employees) {
            employeesByLevel.computeIfAbsent(employee.getLevel(), k -> new TreeSet<>()).add(employee);
        }


        Map<String, TreeSet<Employee>> result = new HashMap<>();
        for (String lvl : levels) {
            TreeSet<Employee> set = employeesByLevel.get(lvl);
            if (set != null) {
                result.put(lvl, set);
            }
        }
        return result;
    }

    Map<String, Double> totalPayPerEmployee() {
        Map<String, Double> totals = new HashMap<>();
        for (Employee e : employees) {
            if (totals.containsKey(e.getID())) {
                totals.putIfAbsent(e.getID(), e.calculateSalary());
            } else {
                double currentValue = totals.get(e.getID());
                currentValue += e.calculateSalary();
                totals.put(e.getID(), currentValue);
            }
        }
        return totals;
    }
}
public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployeesData(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i=5;i<=10;i++) {
            levels.add("level"+i);
        }
        Map<String, TreeSet<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
        });


    }
}