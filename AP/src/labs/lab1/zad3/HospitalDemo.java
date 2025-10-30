package labs.lab1.zad3;

import java.util.Scanner;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.Consumer;


// TODO: Create a functional interface to evaluate the doctors
interface DoctorEvaluator {
    boolean evaluate(Doctor doctor);
}
// TODO: Create a HighExpertiseEvaluator class that will implement the functional interface and override the evaluate method
class HighExpertiseEvaluator implements DoctorEvaluator{

    @Override
    public boolean evaluate(Doctor doctor) {
        return doctor.getLevel() >= 7;
    }
}

class Doctor {
    private final int licenseNumber;    // e.g., 1234
    private String name;
    private int level;                  // 1..10 (10 = chief)

    private int patients;

    public Doctor(int licenseNumber, String name, int level, int patients) {
        this.licenseNumber = licenseNumber;
        this.name = name;
        this.level = level;
        this.patients = patients;
    }

    public int getLicenseNumber() {
        return licenseNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if (level > 10) {
            this.level = 10;
            return;
        }
        if (level < 1) {
            this.level = 1;
            return;
        }
        this.level = level;
    }

    public int getPatients() {
        return patients;
    }

    public void setPatients(int patients) {
        this.patients = patients;
    }

    @Override
    public String toString() {
        return String.format("%s (%d) %d %d %s", name, licenseNumber, level, patients, level == 10 ? "[Chief]" : "");
    }
}

class EmergencyRoom {
    private final String hospitalName;
    private final Doctor[] doctors;
    private int size = 0;

    public EmergencyRoom(String title, int doctorCapacity) {
        this.hospitalName = title;
        this.doctors = new Doctor[doctorCapacity];
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return doctors.length;
    }

    /**
     * Add a doctor provided by a Supplier. Demonstrates Supplier<T>.
     */
    public boolean treat(Supplier<Doctor> supplier) {
        if (size >= doctors.length) {
            return false;
        }
        doctors[size++] = supplier.get();
        return true;
    }

    /**
     * Apply a Consumer to each doctor (side effects allowed, e.g., print or mutate).
     */
    public void forEach(Consumer<Doctor> action) {
        for (int i = 0; i < size; i++) {
            action.accept(doctors[i]);
        }
    }

    /**
     * Count doctors satisfying a Predicate.
     */
    public int count(Predicate<Doctor> predicate) {
        int c = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(doctors[i])) {
                c++;
            }
        }
        return c;
    }

    /**
     * Find first doctor that matches; returns null if none.
     */
    public Doctor findFirst(Predicate<Doctor> predicate) {
        for (int i = 0; i < size; i++) {
            if (predicate.test(doctors[i])) {
                return doctors[i];
            }
        }
        return null;
    }

    /**
     * Filter doctors into a NEW array (still no collections).
     */
    public Doctor[] filter(Predicate<Doctor> predicate) {
        // 1st pass: count matches to size array exactly
        int matches = count(predicate);
        Doctor[] out = new Doctor[matches];
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(doctors[i])) {
                out[j++] = doctors[i];
            }
        }
        return out;
    }

    /**
     * Map doctors to Strings (labels) with a Function.
     * (We return String[] to avoid generics + array creation complexity.)
     */
    public String[] mapToLabels(Function<Doctor, String> mapper) {
        String[] out = new String[size];
        for (int i = 0; i < size; i++) {
            out[i] = mapper.apply(doctors[i]);
        }
        return out;
    }

    /**
     * In-place update using a Consumer (mutation allowed).
     * Example: increase level +1, cap at 10.
     */
    public void mutate(Consumer<Doctor> mutator) {
        for (int i = 0; i < size; i++) {
            mutator.accept(doctors[i]);
        }
    }

    public void conditionalMutate(Predicate<Doctor> condition, Consumer<Doctor> mutator) {
        for (int i = 0; i < size; i++) {
            if (condition.test(doctors[i])) {
                mutator.accept(doctors[i]);
            }
        }

    }

    public int countForEvaluation(DoctorEvaluator evaluator) {
        int c = 0;
        for (int i = 0; i < size; i++) {
            if (evaluator.evaluate(doctors[i])) {
                c++;
            }
        }
        return c;
    }

    public Doctor[] evaluate(DoctorEvaluator evaluator) {
        int outSize = countForEvaluation(evaluator);
        Doctor[] out = new Doctor[outSize];
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (evaluator.evaluate(doctors[i])) {
                out[j++] = doctors[i];
            }
        }
        return out;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hospital: " + hospitalName + " (" + size + "/" + doctors.length + " doctors)");
        for (Doctor doctor : doctors) {
            sb.append(doctor.toString()).append("\n");
        }
        return sb.toString();

    }
}

public class HospitalDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EmergencyRoom er = new EmergencyRoom("University Clinic", 10);

        int n = sc.nextInt();

        //TODO: Supplier that reads line by line
        Supplier<Doctor> doctorSupplier;
        doctorSupplier = () -> {
            int licenseNumber = sc.nextInt();
            String name = sc.next();
            int level = sc.nextInt();
            int patients = sc.nextInt();
            return new Doctor(licenseNumber, name, level, patients);
        };

        //TODO: Add n doctors to treat using the supplier

        for (int i = 0; i <n ; i++) {
            er.treat(doctorSupplier);
        }

        sc.close(); // close scanner after done

        //TODO: Print all of the doctors with forEach
        System.out.println("Doctors that are treating:");
        Consumer<Doctor> doctorConsumer = doctor -> System.out.println(doctor);
        er.forEach(doctorConsumer);


        //TODO: Print all doctors that treat using Consumer + forEach ---
        System.out.println("\n=== All Doctors ===");
        er.forEach(doctorConsumer);


        //TODO: Use Functional Interface to filter the good doctors ---
        Predicate<Doctor> goodDoctorPredicate = doctor -> doctor .getLevel() >= 7;
        Predicate<Doctor> highPatientsPredicate = doctor -> doctor.getPatients() > 20;

        Doctor[] passing = er.filter(goodDoctorPredicate.and(highPatientsPredicate));
        System.out.println("\n=== Doctors with higher number of patients and a higher level of expertise ===");
        for (Doctor d : passing) System.out.println(d);

        //TODO: Print the chief of the department (level = 10)
        Predicate<Doctor> chiefPredicate = doctor -> doctor.getLevel() == 10;
        Doctor chief = er.findFirst(chiefPredicate);
        System.out.println("\n=== Chief doctor (level = 10) ===");

        System.out.println(chief != null ? chief : "No chief found");

        //TODO: Increase the level of expertise for every doctor by 1
        System.out.println("\n=== Increase all expertise levels by 1 (max 10) ===");
        Consumer<Doctor> increaseLevelConsumer = doctor -> {
            if(doctor.getLevel() < 10)
                doctor.setLevel(doctor.getLevel() + 1);
        };
        er.mutate(increaseLevelConsumer);
        er.forEach(doctorConsumer);


        //TODO: Conditional mutation: increase the level of expertise of every doctor that has more than 30 patients
        System.out.println("\n=== Increase the level of expertise of every doctor by 1 ===");
        Predicate<Doctor> higherPatientsPredicate = doctor -> doctor.getPatients() >= 30;
        er.conditionalMutate(higherPatientsPredicate,increaseLevelConsumer);

        //TODO: Map doctors to labels in the format: Name: name, Level: level
        System.out.println("\n=== Map doctors to labels ===");
        Consumer<Doctor> stylishPrint = doctor -> {
            System.out.println("Name: "+doctor.getName()+", Level: "+doctor.getLevel());
        };
        er.forEach(stylishPrint);
    }
}