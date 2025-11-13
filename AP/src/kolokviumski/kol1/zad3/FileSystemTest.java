package kolokviumski.kol1.zad3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

interface IFile {
    String getFileName();
    long getFileSize();
    String getFileInfo();
    void sortBySize();
    long findLargestFile();
}

class File implements IFile {
    private String fileName;
    private long fileSize;

    public File(String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

    @Override
    public String getFileInfo() {
        return String.format("File name: %10s File size: %10d", fileName, fileSize);
    }

    public String toString(int depth) {
        String indent = "    ".repeat(depth); // 4 spaces
        return String.format("%sFile name: %10s File size: %10d", indent, fileName, fileSize);
    }

    @Override
    public String toString() {
        return toString(0);
    }

    @Override
    public void sortBySize() {}

    @Override
    public long findLargestFile() {
        return fileSize;
    }
}

class Folder implements IFile {
    private String fileName;
    private List<IFile> files;

    public Folder(String fileName) {
        this.fileName = fileName;
        this.files = new ArrayList<>();
    }

    public void addFile(IFile file) throws FileNameExistsException {
        for (IFile f : files) {
            if (f.getFileName().equals(file.getFileName())) {
                throw new FileNameExistsException(file, this.fileName);
            }
        }
        files.add(file);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public long getFileSize() {
        return files.stream().mapToLong(IFile::getFileSize).sum();
    }

    @Override
    public String getFileInfo() {
        return String.format("Folder name: %10s Folder size: %10d", fileName, getFileSize());
    }

    public String toString(int depth) {
        String indent = "    ".repeat(depth); // 4 spaces
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%sFolder name: %10s Folder size: %10d\n",
                indent, fileName, getFileSize()));

        for (IFile file : files) {
            if (file instanceof Folder) {
                sb.append(((Folder) file).toString(depth + 1)).append("\n");
            } else {
                sb.append(((File) file).toString(depth + 1)).append("\n");
            }
        }

        // Remove last newline
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n')
            sb.setLength(sb.length() - 1);

        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(0);
    }

    @Override
    public void sortBySize() {
        files.sort(Comparator.comparingLong(IFile::getFileSize));
        files.forEach(IFile::sortBySize);
    }

    @Override
    public long findLargestFile() {
        long maxSize = 0;
        for (IFile file : files) {
            long val = file.findLargestFile();
            if (val > maxSize) {
                maxSize = val;
            }
        }
        return maxSize;
    }
}

class FileNameExistsException extends Exception {
    public FileNameExistsException(IFile file, String foldername) {
        super(String.format("There is already a file named %s in the folder %s",
                file.getFileName(), foldername));
    }
}

class FileSystem {
    Folder rootDirectory;

    public FileSystem() {
        this.rootDirectory = new Folder("root");
    }

    void addFile(IFile file) throws FileNameExistsException {
        rootDirectory.addFile(file);
    }

    long findLargestFile() {
        return rootDirectory.findLargestFile();
    }

    void sortBySize() {
        rootDirectory.sortBySize();
    }

    @Override
    public String toString() {
        return rootDirectory.toString();
    }
}

public class FileSystemTest {

    public static Folder readFolder(Scanner sc) {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < totalFiles; i++) {
            String line = sc.nextLine();

            if (line.trim().startsWith("0")) {
                String fileInfo = sc.nextLine();
                String[] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0],
                            Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();

        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());
        System.out.println();
        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());
        System.out.println();
        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());
    }
}
