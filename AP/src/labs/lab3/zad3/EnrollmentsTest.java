package labs.lab3.zad3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// TODO: Add classes and implement methods
class Applicant{
    int id;
    String name;
    Double gpa;
    List<SubjectWithGrade> subjectsWithGrades;
    StudyProgramme studyProgramme;

    public Applicant(int id, String name, Double gpa, StudyProgramme studyProgramme) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
        this.studyProgramme = studyProgramme;
        this.subjectsWithGrades = new ArrayList<>();
    }
    public double calculatePoints(){
        double result = gpa* 12;
        for (SubjectWithGrade subjectWithGrade : subjectsWithGrades) {
            if(this.studyProgramme.faculty.appropriateSubjects.contains(subjectWithGrade.getSubject())){
                result += subjectWithGrade.getGrade() * 2;
            } else {
                result += subjectWithGrade.getGrade() * 1.2;
            }
        }
        return result;
    }
    public void addSubjectAndGrade(String subject, int grade){
        subjectsWithGrades.add(new SubjectWithGrade(subject, grade));
    }

}
class StudyProgramme{
    String code;
    String name;
    int numPublicQuota;
    int numPrivateQuota;
    int enrolledInPublicQuota;
    int enrolledInPrivateQuota;
    Faculty faculty;
    List<Applicant> applicants;

    public StudyProgramme(String code, String name,Faculty faculty, int numPublicQuota, int numPrivateQuota) {
        this.code = code;
        this.name = name;
        this.faculty = faculty;
        this.numPublicQuota = numPublicQuota;
        this.numPrivateQuota = numPrivateQuota;
        this.applicants = new ArrayList<>();
    }

    public void calculateEnrollmentNumbers(){
        applicants.sort(Comparator.comparingDouble(Applicant::calculatePoints).reversed());
        this.enrolledInPublicQuota = Math.min(numPublicQuota, applicants.size());
        this.enrolledInPrivateQuota = Math.min(numPrivateQuota, applicants.size() - enrolledInPublicQuota);
    }

    @Override
    public String toString() {
        //Да се преоптовари toString() така што ќе ги прикаже:
        //
        //името на програмата
        //сите апликанти примени во државна квота
        //сите апликанти примени во приватна квота
        //сите одбиени апликанти
        calculateEnrollmentNumbers();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Name: %s\n", name));
        sb.append("Public quota:\n");
        for (int i = 0; i < enrolledInPublicQuota; i++) {
            //Id: 1, Name: Ana, GPA: 4.9 - 93.6
            Applicant applicant = applicants.get(i);
            sb.append(String.format("Id: %d, Name: %s, GPA:  %.2f - %.1f\n", applicant.id, applicant.name,applicant.gpa, applicant.calculatePoints()));
        }

        sb.append("Accepted applicants (private quota):\n");
        for (int i = enrolledInPublicQuota; i < enrolledInPublicQuota + enrolledInPrivateQuota; i++) {
            Applicant applicant = applicants.get(i);
            sb.append(String.format("Id: %d, Name: %s, GPA:  %.2f - %.1f\n", applicant.id, applicant.name,applicant.gpa, applicant.calculatePoints()));
        }

        sb.append("Rejected:\n");
        for (int i = enrolledInPublicQuota + enrolledInPrivateQuota; i < applicants.size(); i++) {
            Applicant applicant = applicants.get(i);
            sb.append(String.format("Id: %d, Name: %s, GPA:  %.2f - %.1f\n", applicant.id, applicant.name,applicant.gpa, applicant.calculatePoints()));
        }

        return sb.toString();
    }
}

class Faculty{
    String shortName;
    List<String>  appropriateSubjects;
    List<StudyProgramme> studyProgrammes;

    public Faculty(String shortName) {
        this.shortName = shortName;
        this.appropriateSubjects = new ArrayList<>();
        this.studyProgrammes = new ArrayList<>();
    }
    public void addSubject(String subject){
        appropriateSubjects.add(subject);
    }
    public void addStudyProgramme(StudyProgramme studyProgramme){
        studyProgrammes.add(studyProgramme);
    }
    @Override
    public String toString() {
        //toString методот кој ќе ги печати сите студиски програми и апликанти на факултетот според следните критериуми:
        //
        //бројот на соодветни предмети на факултетот во растечки редослед
        //процентот на примени студенти во една студиска програма во опаѓачки редослед
        //((број запишани на државна квота + број запишани на приватна квота) / (број на места на државна квота + број на места на приватна квота )) * 100
        //поените на студентот во опаѓачки редослед

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Faculty: %s\n", shortName));
        List<StudyProgramme> sortedProgrammes = studyProgrammes.stream()
                .sorted(Comparator.comparingDouble(sp -> -((double)(sp.enrolledInPublicQuota + sp.enrolledInPrivateQuota) / (sp.numPublicQuota + sp.numPrivateQuota))))
                .collect(Collectors.toList());


    }
}
class SubjectWithGrade
{
    private String subject;
    private int grade;
    public SubjectWithGrade(String subject, int grade) {
        this.subject = subject;
        this.grade = grade;
    }
    public String getSubject() {
        return subject;
    }
    public int getGrade() {
        return grade;
    }
}

class Enrollment{
    Applicant applicant;
    StudyProgramme studyProgramme;

    public Enrollment(Applicant applicant, StudyProgramme studyProgramme) {
        this.applicant = applicant;
        this.studyProgramme = studyProgramme;
    }


}

class EnrollmentsIO {
    //Да се имплементираат двата методи во класата EnrollmentsIO кои служат за внесување и печатење на податоците.
    //
    //Методот readEnrollments:
    //
    //како аргументи добива листа од студиски програми и InputStream
    //ја пребарува студиската програма во листата на студиски програми според кодот од внесот
    //го креира објектот од тип Applicant, ги поставува атрибутите и го додава во листата на апликанти на соодветната студиска програма
    //Форматот за внес е следниот:
    //
    //id;name;gpa;subject1:grade1;subject2:grade2;subject3:grade3;subject4:grade4;studyProgrammeCode
    //
    //каде id е идентификаторот на апликантот, name e името на апликантот, gpa е просекот на апликантот, subjectN и gradeN се предметите кои ги полагал на матурскиот испит и studyProgrammeCode е студиската програма на која сака да се запише.
    //
    //Методот printRanked:
    //
    //како аргумент добива листа од факултети
    //ги печати сите факултети според формат кој е достапен во тест примерите и според критериумот кој е зададен во текстот.
    public static void printRanked(List<Faculty> faculties) {
        List<Faculty> sortedFaculties = faculties.stream()
                .sorted(Comparator.comparing(faculty -> faculty.shortName))
                .collect(Collectors.toList());

        for (Faculty faculty : sortedFaculties) {
            System.out.println(faculty.toString());
        }
    }

    public static List<Enrollment> readEnrollments(List<StudyProgramme> studyProgrammes, InputStream inputStream) {
        List<Enrollment> enrollments = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                String[] parts = line.split(";");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                Double gpa = Double.parseDouble(parts[2]);
                String studyProgrammeCode = parts[parts.length - 1];

                StudyProgramme studyProgramme = studyProgrammes.stream()
                        .filter(sp -> sp.code.equals(studyProgrammeCode))
                        .findFirst()
                        .orElse(null);

                if (studyProgramme != null) {
                    Applicant applicant = new Applicant(id, name, gpa, studyProgramme);
                    for (int i = 3; i < parts.length - 1; i++) {
                        String[] subjectGrade = parts[i].split(":");
                        String subject = subjectGrade[0];
                        int grade = Integer.parseInt(subjectGrade[1]);
                        applicant.addSubjectAndGrade(subject, grade);
                    }
                    studyProgramme.applicants.add(applicant);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enrollments;
    }
}

public class EnrollmentsTest {

    public static void main(String[] args) {
        Faculty finki = new Faculty("FINKI");
        finki.addSubject("Mother Tongue");
        finki.addSubject("Mathematics");
        finki.addSubject("Informatics");

        Faculty feit = new Faculty("FEIT");
        feit.addSubject("Mother Tongue");
        feit.addSubject("Mathematics");
        feit.addSubject("Physics");
        feit.addSubject("Electronics");

        Faculty medFak = new Faculty("MEDFAK");
        medFak.addSubject("Mother Tongue");
        medFak.addSubject("English");
        medFak.addSubject("Mathematics");
        medFak.addSubject("Biology");
        medFak.addSubject("Chemistry");

        StudyProgramme si = new StudyProgramme("SI", "Software Engineering", finki, 4, 4);
        StudyProgramme it = new StudyProgramme("IT", "Information Technology", finki, 2, 2);
        finki.addStudyProgramme(si);
        finki.addStudyProgramme(it);

        StudyProgramme kti = new StudyProgramme("KTI", "Computer Technologies and Engineering", feit, 3, 3);
        StudyProgramme ees = new StudyProgramme("EES", "Electro-energetic Systems", feit, 2, 2);
        feit.addStudyProgramme(kti);
        feit.addStudyProgramme(ees);

        StudyProgramme om = new StudyProgramme("OM", "General Medicine", medFak, 6, 6);
        StudyProgramme nurs = new StudyProgramme("NURS", "Nursing", medFak, 2, 2);
        medFak.addStudyProgramme(om);
        medFak.addStudyProgramme(nurs);

        List<StudyProgramme> allProgrammes = new ArrayList<>();
        allProgrammes.add(si);
        allProgrammes.add(it);
        allProgrammes.add(kti);
        allProgrammes.add(ees);
        allProgrammes.add(om);
        allProgrammes.add(nurs);

        EnrollmentsIO.readEnrollments(allProgrammes, System.in);

        List<Faculty> allFaculties = new ArrayList<>();
        allFaculties.add(finki);
        allFaculties.add(feit);
        allFaculties.add(medFak);

        allProgrammes.stream().forEach(StudyProgramme::calculateEnrollmentNumbers);

        EnrollmentsIO.printRanked(allFaculties);

    }


}

