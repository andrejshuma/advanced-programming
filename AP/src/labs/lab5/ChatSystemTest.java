package labs.lab5;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;
import java.util.stream.Collectors;

class ChatRoom {
    private final String name;
    private final Set<String> users;

    ChatRoom(String name){
        this.name = name;
        users = new TreeSet<>();
    }

    public void addUser(String username){
        users.add(username);
    }

    public void removeUser(String username){
        users.remove(username);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        if (users.isEmpty()) sb.append("EMPTY\n");
        else {
            for (String u : users){
                sb.append(u).append("\n");
            }
        }
        return sb.toString();
    }

    public boolean hasUser(String username){
        return users.contains(username);
    }

    public int numUsers(){
        return users.size();
    }
}

class NoSuchRoomException extends Exception{
    NoSuchRoomException(){
        super();
    }
}
class NoSuchUserException extends Exception{
    NoSuchUserException(String username){
        super(username);
    }
}

class ChatSystem {
    Map<String, ChatRoom> rooms;
    Set<String> users;
    ChatSystem(){
        rooms = new TreeMap<>();
        users = new TreeSet<>();
    }

    public void addRoom(String roomName){
        rooms.put(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName){
        rooms.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException{
        if (!rooms.containsKey(roomName)) throw new NoSuchRoomException();
        return rooms.get(roomName);
    }

    public void register(String userName){
        String key = rooms.entrySet()
                .stream().min(Map.Entry.<String, ChatRoom>comparingByValue
                                (Comparator.comparingInt(ChatRoom::numUsers))
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(Map.Entry::getKey).orElse(null);
        users.add(userName);
        if (key != null) rooms.get(key).addUser(userName);
    }

    public void registerAndJoin(String userName, String roomName){
        users.add(userName);
        rooms.get(roomName).addUser(userName);
    }

    public void joinRoom(String userName, String roomName)
            throws NoSuchRoomException, NoSuchUserException{
        if (!rooms.containsKey(roomName)) throw new NoSuchRoomException();
        if (!users.contains(userName)) throw new NoSuchUserException(userName);
        registerAndJoin(userName, roomName);
    }

    public void leaveRoom(String userName, String roomName)
            throws NoSuchRoomException, NoSuchUserException {
        if (!users.contains(userName)) throw new NoSuchUserException(userName);
        rooms.get(roomName).removeUser(userName);
    }

    public void followFriend(String username, String friend_username)
            throws NoSuchUserException {
        rooms.entrySet().stream()
                .filter(e -> e.getValue().hasUser(friend_username))
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new))
                .forEach(l -> rooms.get(l).addUser(username));
    }
}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs, (Object[]) params);
                    }
                }
            }
        }
    }

}