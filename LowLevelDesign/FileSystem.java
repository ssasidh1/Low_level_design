/**
 * FILE SYSTEM SEARCH 
 * Create, modify, delete files
 * Store metadata of the file like name, size, type, ownership,  dateOfCreation, dateOfModification, permissions etc
 * File can be organized in directory and sub-direcetories
 * Read and write operations on files
 *
 * File -> contents, name, permissions
 * 
 * Search by Name
 * Search by metadata
 * Search by content
 * Search with permissions, owners etc
 * Sort the search result with filename, size , date 
 */

// FileSystemEntries interface

public interface FileSystemEntries{
    public String getName();
    public void delete();
    public void rename(String newName); 
    public String getMetaData();
}

public class File implements FileSystemEntries{
    private Directory parent;
    private String name;
    private String contents;
    private List<Permissions> permissions;
    private long size;
    private Date creationDate;
    private Date modificationDate;
    private String owner; // users
    private String fileType; // txt, pdf, jpeg etc

    public File(String name, String owner, String fileType, Directory parent){
        this.name = name;
        this.owner = owner;
        this.fileType = fileType;
        this.parent = parent;
        permissions = new ArrayList<>();
        size = 0;
        creationDate = new Date();
        modificationDate = new Date();
        
    }

    public void write( String content){
        if(permissions.contains(Permission.WRITE)){
             this.content = content;
            this.size = content.length();
            this.modificationDate = new Date();
        }
        else{
            // handle with exception
        }
       
    }

    public void read(){
        System.out.println("Reading content - "+content);
    }

    public void modify(String content){
        write(content);
    }

    public String getName(){
        return name;
    }
    public void delete(){
        // delete the file;
    }
    public void rename(String newName){
        name = newName;
    } 
    public String getMetaData(){
        return "Name: " + name + "\n" +
               "Size: " + size + " bytes\n" +
               "Parent Directory " + parent + "\n" + 
               "Creation Date: " + creationDate + "\n" +
               "Modification Date: " + modificationDate + "\n" +
               "Owner: " + owner + "\n" +
               "Permissions: " + permissions + "\n" +
               "File Type: " + fileType;
    }

    public void addPermission(Permissions permission){
        permissions.add(permission);
    }

    public List<Permission> getPermission(){
        return permissions;
    }

    public boolean removePermission(Permission permission){
        if(permissions.contains(permission)) {
            permissions.remove(permission);
            return true;
        }
        return false;
    }

}

public class Directory implements FileSystemEntries{
    private String name;
    private HashMap<String,FileSystemEntries> fileEntries;
    private List<Permissions_DIR> permissions;
    private long size;
    private Date creationDate;
    private Date modificationDate;
    private String owner; // users

    public Directory(String name, String owner){
        this.name = name;
        this.owner = owner;
        permissions = new ArrayList<>();
        size = 0;
        creationDate = new Date();
        modificationDate = new Date();
        fileEntries = new HashMap<>();
    }

    public void addFile( File file){
        if(permissions.contains(Permissions_DIR.APPEND)){
            fileEntries.put(file.getName(), file);
            this.size += file.getSize();
            this.modificationDate = new Date();
        }
        else{
            // handle with exception
        }
       
    }

    

    public String getName(){
        return name;
    }
    public void delete(){
        // delete the file;
    }
    public void rename(String newName){
        name = newName;
    } 
    public String getMetaData(){
        return "Name: " + name + "\n" +
               "Size: " + size + " bytes\n" +
               "Creation Date: " + creationDate + "\n" +
               "Modification Date: " + modificationDate + "\n" +
               "Owner: " + owner + "\n" +
               "Permissions: " + permissions + "\n" +
    }

    public void addPermission(Permissions_DIR permission){
        permissions.add(permission);
    }

    public List<Permission> getPermission(){
        return permissions;
    }

    public boolean removePermission(Permission permission){
        if(permissions.contains(permission)) {
            permissions.remove(permission);
            return true;
        }
        return false;
    }

}

// SearchStrategy

public interface SearchStrategy{
    public Object search(HashMap<String,File> entries, String query);
}

public class SearchByName implements SearchStrategy{
    @Override
    public FileSystemEntries search(HashMap<String,File> entries, String name){
        List<FileSystemEntries> result = new ArrayList<>();

        if(!entries.containsKey(name)) return null;

        return entries.get(name);
    }
}


public class PermissionSearchStrategy implements SearchStrategy {
    @Override
    public List<FileSystemEntity> search(List<FileSystemEntity> entities, String query) {
        List<FileSystemEntity> result = new ArrayList<>();
        for (FileSystemEntity entity : entities) {
            if (entity instanceof File && ((File) entity).getPermissions().contains(query)) {
                result.add(entity);
            } else if (entity instanceof Directory && ((Directory) entity).getPermissions().contains(query)) {
                result.add(entity);
            }
        }
        return result;
    }
}


public class FileSystemSearcher{
    private SearchStrategy strategy;

    public FileSystemSearcher(SearchStrategy strategy){
        this.strategy = strategy;
    }

    public List<FileSystemEntity> search(List<FileSystemEntity> entities, String query) {
        return strategy.search(entities, query);
    }

}


public interface Command {
    void execute();
}


public class CreateFileCommand implements Command {
    private File file;

    public CreateFileCommand(File file) {
        this.file = file;
    }

    @Override
    public void execute() {
        System.out.println("Creating file: " + file.getName());
    }
}

public class DeleteFileCommand implements Command {
    private File file;

    public DeleteFileCommand(File file) {
        this.file = file;
    }

    @Override
    public void execute() {
        file.delete();
    }
}

public class RenameFileCommand implements Command {
    private File file;
    private String newName;

    public RenameFileCommand(File file, String newName) {
        this.file = file;
        this.newName = newName;
    }

    @Override
    public void execute() {
        file.rename(newName);
    }
}

public class FileSystemInvoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }
}


public class DiskManager {
    private static DiskManager instance;
    private int totalDiskSpace;
    private int usedDiskSpace;

    private DiskManager() {
        totalDiskSpace = 1000; // example size in MB
        usedDiskSpace = 0;
    }

    public static DiskManager getInstance() {
        if (instance == null) {
            instance = new DiskManager();
        }
        return instance;
    }

    public boolean allocateSpace(int space) {
        if (usedDiskSpace + space <= totalDiskSpace) {
            usedDiskSpace += space;
            return true;
        }
        return false;
    }

    public void releaseSpace(int space) {
        usedDiskSpace -= space;
    }

    public int getAvailableSpace() {
        return totalDiskSpace - usedDiskSpace;
    }
}


public class Main {
    public static void main(String[] args) {
        // Create the system notifier and customers for notifications
        Customer customer = new Customer("Alice");

        // Create file and directory objects
        File file1 = new File("file1.txt");
        File file2 = new File("file2.txt");
        Directory directory1 = new Directory("dir1");

        // Add file permissions
        PermissionManager.setPermission(file1, "read");
        PermissionManager.setPermission(directory1, "write");

        // Create and delete files using command pattern
        Command createFileCommand = new CreateFileCommand(file1);
        FileSystemInvoker invoker = new FileSystemInvoker();
        invoker.setCommand(createFileCommand);
        invoker.executeCommand();

        Command deleteFileCommand = new DeleteFileCommand(file2);
        invoker.setCommand(deleteFileCommand);
        invoker.executeCommand();

        // Search for files using search strategy
        List<FileSystemEntity> entities = new ArrayList<>();
        entities.add(file1);
        entities.add(directory1);

        SearchStrategy searchStrategy = new NameSearchStrategy();
        FileSystemSearcher searcher = new FileSystemSearcher(searchStrategy);
        List<FileSystemEntity> searchResult = searcher.search(entities, "file1");

        System.out.println("Search Results: ");
        for (FileSystemEntity entity : searchResult) {
            System.out.println(entity.getName());
        }
    }
}
