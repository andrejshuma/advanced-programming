package kolokviumski.kol1.zad8;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Calendar;

class NonExistingItemException extends Exception {
    public NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist", id));
    }
}

abstract class Archive {
    protected int id;
    protected Date dateArchived;

    public Archive(int id) {
        this.id = id;
        this.dateArchived = new Date();
    }

    public void setDateArchived(Date dateArchived) {
        this.dateArchived = dateArchived;
    }

    public int getId() {
        return id;
    }

    public abstract String tryToOpen(Date date);
}

class LockedArchive extends Archive {
    private final Date dateToOpen;

    public LockedArchive(int id, Date dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    public String tryToOpen(Date date) {
        if (date.getTime() < dateToOpen.getTime()) {
            return ("Item " + getId() + " cannot be opened before " + dateToOpen.toString());
        }
        else {
            return ("Item " + getId() + " opened at " + date);
        }
    }
}

class SpecialArchive extends Archive {
    private final int maxOpen;
    private int timesOpen;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        timesOpen = 0;
    }

    @Override
    public String tryToOpen(Date date) {
        if (timesOpen == maxOpen) {
            return ("Item " + getId() + " cannot be opened more than " + maxOpen + " times");
        } else {
            timesOpen++;
            return ("Item " + getId() + " opened at " + date);
        }
    }
}


class ArchiveStore {
    private List<Archive> archives;
    private final StringBuilder log;

    public ArchiveStore() {
        this.archives = new ArrayList<>();
        log = new StringBuilder();
    }

    public void archiveItem(Archive item, Date date) {
        archives.add(item);
        item.setDateArchived(date);
        log.append("Item ").append(item.getId()).append(" archived at ").append(date.toString()).append('\n');
    }

    public void openItem(int id, Date date) throws NonExistingItemException {
        if (archives.stream().noneMatch(arch -> arch.getId() == id))
            throw new NonExistingItemException(id);
        Optional<Archive> archive = archives.stream().filter(arch -> arch.getId() == id).findAny();
        log.append(archive.get().tryToOpen(date)).append('\n');
    }

    public String getLog() {
        return log.toString();
    }
}

public class ArchiveStoreTest {

    public static Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        // Month in Calendar is 0-indexed (0=January, 9=October)
        cal.set(year, month-1, day, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // Helper function to add days to a date
    public static Date addDays(Date date, long days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // Add days to the date
        cal.add(Calendar.DAY_OF_YEAR, (int) days);
        return cal.getTime();
    }

    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();

        // Changed LocalDate.of(2013, 10, 7) to use createDate helper
        Date date = createDate(2013, 11, 7);

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            // Changed date arithmetic from java.time to use addDays helper
            Date dateToOpen = addDays(date, days);

            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while (scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                // The 'date' variable is now a Date type
                store.openItem(open, date);
            } catch (NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}