package labs.lab3.zad3;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

// TODO: Add classes and implement methods
class ApplicantComparator implements Comparator<Applicant>{

    @Override
    public int compare(Applicant o1, Applicant o2) {
        if (o1.calculatePoints()!=o2.calculatePoints()){
            return Double.compare(o2.calculatePoints(),o1.calculatePoints());
        }else{
            return Integer.compare(o2.getAppropriateCounter(),o1.getAppropriateCounter());
        }
    }
}

class Applicant{
    private int id;
    private String name;
    private double gpa;
    private List<SubjectWithGrade>subjectsWithGrades;
    private StudyProgramme studyProgramme;
    private int appropriateCounter;

    public Applicant(int id,String name,double gpa,StudyProgramme studyProgramme){
        this.id=id;
        this.name=name;
        this.gpa=gpa;
        this.subjectsWithGrades=new ArrayList<>();
        this.studyProgramme=studyProgramme;
        this.appropriateCounter=0;
    }

    public void addSubjectAndGrade(String subject, int grade){
        subjectsWithGrades.add(new SubjectWithGrade(subject,grade));
    }

    public int getAppropriateCounter() {
        return appropriateCounter;
    }

    public double calculatePoints() {
        double points = 0;
        int appropriateSubjectsCounter = 0;
        List<String> appropriateSubjects = studyProgramme.getFaculty().getAppropriateSubjects();

        for (SubjectWithGrade swg : subjectsWithGrades) {
            if (appropriateSubjects.contains(swg.getSubject())) {
                points += swg.getGrade() * 2;
                appropriateSubjectsCounter++;
            } else {
                points += swg.getGrade() * 1.2;
            }
        }
        points += gpa * 12;
        return points;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        //Id: 7, Name: Ivana, GPA: 4.6 - 89.19999999999999
        return "Id: "+id+", Name: "+name+", GPA: "+gpa+" - "+calculatePoints()+"\n";

    }
}

class StudyProgramme implements Comparable<StudyProgramme>{

    private String code;
    private String name;
    private int numPublicQuota;
    private int numPrivateQuota;
    private int enrolledInPublicQuota;
    private int enrolledInPrivateQuota;
    private List<Applicant>applicants;
    private Faculty faculty;
    private List<Applicant>publicQuota;
    private List<Applicant>privateQuota;;
    private int rejected;


    public StudyProgramme(String code,String name,Faculty faculty,int numPrivateQuota,int numPublicQuota){
        this.code=code;
        this.name=name;
        this.faculty=faculty;
        this.numPrivateQuota=numPrivateQuota;
        this.numPublicQuota=numPublicQuota;
        this.enrolledInPrivateQuota=0;
        this.enrolledInPublicQuota=0;
        this.publicQuota=new ArrayList<>();
        this.privateQuota=new ArrayList<>();
        this.rejected=0;
        this.applicants=new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void addApplicant(Applicant applicant){

        applicants.add(applicant);
    }

    public int getNumPublicQuota() {
        return numPublicQuota;
    }

    public int getNumPrivateQuota() {
        return numPrivateQuota;
    }

    public int getEnrolledInPublicQuota() {
        return enrolledInPublicQuota;
    }

    public int getEnrolledInPrivateQuota() {
        return enrolledInPrivateQuota;
    }

    public List<Applicant> getApplicants() {
        return applicants;
    }

    public double getScore(){
        calculateEnrollmentNumbers();
        double sum1= enrolledInPrivateQuota+ enrolledInPublicQuota;
        double sum2= numPublicQuota+ numPrivateQuota;
        return (sum1/sum2)*100;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void calculateEnrollmentNumbers(){
        int appSize=applicants.size();
        if (appSize-numPublicQuota<0){
            this.enrolledInPublicQuota=appSize;
            return;
        }else{
            this.enrolledInPublicQuota=numPublicQuota;
        }
        int rest=appSize-enrolledInPublicQuota;
        if (rest-numPrivateQuota<0){
            this.enrolledInPrivateQuota=rest;
            return;
        }else{
            this.enrolledInPrivateQuota=numPrivateQuota;
        }
        this.rejected= appSize-(enrolledInPrivateQuota+enrolledInPublicQuota);
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("Name: %s\n",name));
        calculateEnrollmentNumbers();
        applicants.sort(new ApplicantComparator());
        sb.append("Public Quota:\n");
        int i,j,k;
        for (i = 0; i < enrolledInPublicQuota; i++) {
            sb.append(applicants.get(i).toString());
        }
        sb.append("Private Quota:\n");
        for ( j = 0; j< enrolledInPrivateQuota; j++) {
            sb.append(applicants.get(i++).toString());
        }
        sb.append("Rejected:\n");

        for ( k = 0; k <rejected; k++) {
            sb.append(applicants.get(i++).toString());
        }

        return sb.toString();
    }

    @Override
    public int compareTo(StudyProgramme o) {
        return Double.compare(o.getScore(),this.getScore());
    }
}


class Faculty{
    private String shortName;
    private List<String>appropriateSubjects;
    private List<StudyProgramme> studyProgrammes;

    public Faculty(String shortName){
        this.shortName=shortName;
        this.appropriateSubjects=new ArrayList<>();
        this.studyProgrammes=new ArrayList<>();

    }

    public void addSubject(String s){
        appropriateSubjects.add(s);
    }
    public void addStudyProgramme(StudyProgramme studyProgramme){
        studyProgrammes.add(studyProgramme);
    }

    public List<String> getAppropriateSubjects() {
        return appropriateSubjects;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("Faculty: %s\n",shortName));
        sb.append("Subjects: [");
        for (int i = 0; i < appropriateSubjects.size(); i++) {
            sb.append(appropriateSubjects.get(i));
            if (i!=appropriateSubjects.size()-1){
                sb.append(", ");
            }
        }
        sb.append("]\n");
        sb.append("Study Programmes: \n");
        studyProgrammes.sort(Comparator.naturalOrder());
        for (StudyProgramme s:studyProgrammes){
            sb.append(s.toString());
            sb.append("\n");
        }

        return sb.toString();


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

    private Applicant applicant;
    private StudyProgramme studyProgramme;

    public Enrollment(Applicant applicant, StudyProgramme studyProgramme) {
        this.applicant = applicant;
        this.studyProgramme = studyProgramme;
    }
}

class EnrollmentsIO {
    public static void printRanked(List<Faculty> faculties) {

        for (Faculty f:faculties){
            System.out.print(f.toString());
        }
    }

    public static List<Enrollment> readEnrollments(List<StudyProgramme> studyProgrammes, InputStream inputStream) throws IOException {
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<Enrollment>enrollments=new ArrayList<>();
        while ((line=reader.readLine())!=null){


            String []parts=line.split(";");
            int id=Integer.parseInt(parts[0]);
            String name=parts[1];
            double gpa=Double.parseDouble(parts[2]);
            String programmeCode=parts[parts.length-1];

            StudyProgramme studyProgramme=studyProgrammes.stream().filter(s->s.getCode().equals(programmeCode)).findFirst().orElse(null);
            Applicant applicant=new Applicant(id,name,gpa,studyProgramme);

            String[]rest=Arrays.copyOfRange(parts,3,parts.length-1);
            for (int i=0;i<rest.length;i++){
                String sName=rest[i];
                int grade=Integer.parseInt(rest[i+1]);
                i++;
                applicant.addSubjectAndGrade(sName,grade);
            }
            assert studyProgramme != null;
            studyProgramme.addApplicant(applicant);
            enrollments.add(new Enrollment(applicant,studyProgramme));

        }

        return enrollments;

    }
}

public class EnrollmentsTest {

    public static void main(String[] args) throws IOException {
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
