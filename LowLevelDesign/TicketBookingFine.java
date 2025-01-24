import java.util.*;
import java.util.stream.Collectors;

// Enum classes for different statuses and types
public enum BookingStatus {
    REQUESTED, PENDING, CONFIRMED, CHECKED_IN, CANCELED, ABANDONED
}

public enum SeatType {
    REGULAR, PREMIUM, ACCESSIBLE, SHIPPED, EMERGENCY_EXIT, OTHER
}

public enum AccountStatus {
    ACTIVE, BLOCKED, BANNED, COMPROMISED, ARCHIVED, UNKNOWN
}

public enum PaymentStatus {
    UNPAID, PENDING, COMPLETED, FILLED, DECLINED, CANCELLED, ABANDONED, SETTLING, SETTLED, REFUNDED
}

// Supporting classes for Address and Account
public class Address {
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}

public class Account {
    private String id;
    private String password;
    private AccountStatus status;

    public boolean resetPassword() {
        System.out.println("Password reset.");
        return true;
    }
}

// Abstract Person class
public abstract class Person {
    private String name;
    private Address address;
    private String email;
    private String phone;
    private Account account;

    public Person(String name, Account account) {
        this.name = name;
        this.account = account;
    }

    public String getName() {
        return name;
    }
}

// Observer Pattern for Notifications
interface Observer {
    void update(String message);
}

class Customer extends Person implements Observer {
    private List<Booking> bookings;

    public Customer(String name, Account account) {
        super(name, account);
        bookings = new ArrayList<>();
    }

    @Override
    public void update(String message) {
        System.out.println("Notification for " + getName() + ": " + message);
    }

    public boolean makeBooking(Booking booking) {
        bookings.add(booking);
        System.out.println("Booking made successfully.");
        return true;
    }

    public List<Booking> getBookings() {
        return bookings;
    }
}

class SystemNotifier {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}

// Factory Method Pattern for creating Movies, Shows, and Bookings
abstract class BookingFactory {
    public abstract Booking createBooking(Show show, List<ShowSeat> seats);
}

class RegularBookingFactory extends BookingFactory {
    @Override
    public Booking createBooking(Show show, List<ShowSeat> seats) {
        return new Booking(show, seats);
    }
}

class AdminFactory {
    public Admin createAdmin(String name, Account account) {
        return new Admin(name, account);
    }

    public Movie createMovie(String title, String description, Admin admin) {
        return new Movie(title, description, admin);
    }

    public Show createShow(Movie movie, Date startTime, CinemaHall hall) {
        return new Show(movie, startTime, hall);
    }
}

// Command Pattern for booking and payment operations
interface Command {
    void execute();
}

class CreateBookingCommand implements Command {
    private FrontDeskOfficer officer;
    private Booking booking;

    public CreateBookingCommand(FrontDeskOfficer officer, Booking booking) {
        this.officer = officer;
        this.booking = booking;
    }

    @Override
    public void execute() {
        officer.createBooking(booking);
    }
}

class CancelBookingCommand implements Command {
    private Booking booking;

    public CancelBookingCommand(Booking booking) {
        this.booking = booking;
    }

    @Override
    public void execute() {
        booking.cancel();
    }
}

class MakePaymentCommand implements Command {
    private Payment payment;
    private Booking booking;

    public MakePaymentCommand(Payment payment, Booking booking) {
        this.payment = payment;
        this.booking = booking;
    }

    @Override
    public void execute() {
        booking.makePayment(payment);
    }
}

// Strategy Pattern for searching movies
interface SearchStrategy {
    List<Movie> search(Catalog catalog, String query);
}

class TitleSearchStrategy implements SearchStrategy {
    @Override
    public List<Movie> search(Catalog catalog, String title) {
        return catalog.searchByTitle(title);
    }
}

class GenreSearchStrategy implements SearchStrategy {
    @Override
    public List<Movie> search(Catalog catalog, String genre) {
        return catalog.searchByGenre(genre);
    }
}

class LanguageSearchStrategy implements SearchStrategy {
    @Override
    public List<Movie> search(Catalog catalog, String language) {
        return catalog.searchByLanguage(language);
    }
}

// Template Method Pattern for Payment Processing
abstract class Payment {
    protected double amount;
    protected PaymentStatus status;

    public final void processPayment() {
        initiatePayment();
        confirmPayment();
        updatePaymentStatus();
    }

    protected abstract void initiatePayment();

    private void confirmPayment() {
        System.out.println("Payment confirmed.");
    }

    private void updatePaymentStatus() {
        this.status = PaymentStatus.COMPLETED;
        System.out.println("Payment status updated to COMPLETED.");
    }
}

class CreditCardPayment extends Payment {
    @Override
    protected void initiatePayment() {
        System.out.println("Processing credit card payment of amount: " + amount);
    }
}

class DebitCardPayment extends Payment {
    @Override
    protected void initiatePayment() {
        System.out.println("Processing debit card payment of amount: " + amount);
    }
}

// Supporting classes and interfaces

public class Booking {
    private String bookingNumber;
    private int numberOfSeats;
    private Date createdOn;
    private BookingStatus status;
    private Show show;
    private List<ShowSeat> seats;
    private Payment payment;

    public Booking(Show show, List<ShowSeat> seats) {
        this.show = show;
        this.seats = seats;
        this.status = BookingStatus.REQUESTED;
    }

    public Show getShow() {
        return show;
    }

    public boolean makePayment(Payment payment) {
        this.payment = payment;
        payment.processPayment();
        this.status = BookingStatus.CONFIRMED;
        return true;
    }

    public boolean cancel() {
        this.status = BookingStatus.CANCELED;
        System.out.println("Booking has been canceled.");
        return true;
    }

    public boolean assignSeats(List<ShowSeat> seats) {
        this.seats = seats;
        System.out.println("Seats assigned.");
        return true;
    }
}

public class Show {
    private Movie movie;
    private Date startTime;
    private CinemaHall hall;

    public Show(Movie movie, Date startTime, CinemaHall hall) {
        this.movie = movie;
        this.startTime = startTime;
        this.hall = hall;
    }

    public Movie getMovie() {
        return movie;
    }
}

public class Movie {
    private String title;
    private String description;
    private Admin movieAddedBy;

    public Movie(String title, String description, Admin movieAddedBy) {
        this.title = title;
        this.description = description;
        this.movieAddedBy = movieAddedBy;
    }

    public String getTitle() {
        return title;
    }
}

public class FrontDeskOfficer extends Person {
    public FrontDeskOfficer(String name, Account account) {
        super(name, account);
    }

    public boolean createBooking(Booking booking) {
        System.out.println("Booking created for show: " + booking.getShow().getMovie().getTitle());
        return true;
    }
}

public class Admin extends Person {
    public Admin(String name, Account account) {
        super(name, account);
    }

    public boolean addMovie(Movie movie) {
        System.out.println("Movie " + movie.getTitle() + " added.");
        return true;
    }

    public boolean addShow(Show show) {
        System.out.println("Show added for movie: " + show.getMovie().getTitle());
        return true;
    }

    public boolean blockUser(Customer customer) {
        System.out.println("Customer " + customer.getName() + " blocked.");
        return true;
    }
}

public class CinemaHall {
    private int hallNumber;
    private String name;

    public CinemaHall() {
        this.hallNumber = 1; // Default for simplicity
        this.name = "Standard Hall";
    }

    public int getHallNumber() {
        return hallNumber;
    }

    public String getName() {
        return name;
    }
}

public class ShowSeat {
    private int seatNumber;
    private SeatType seatType;

    public ShowSeat(int seatNumber, SeatType seatType) {
        this.seatNumber = seatNumber;
        this.seatType = seatType;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public SeatType getSeatType() {
        return seatType;
    }
}

// Main class for demonstrating the design
public class Main {
    public static void main(String[] args) {
        // Create an Admin using Factory Method
        AdminFactory adminFactory = new AdminFactory();
        Account adminAccount = new Account();
        Admin admin = adminFactory.createAdmin("John Doe", adminAccount);

        // Create a movie and show
        Movie movie = adminFactory.createMovie("Inception", "Sci-fi Thriller", admin);
        CinemaHall hall = new CinemaHall();
        Show show = adminFactory.createShow(movie, new Date(), hall);

        // Create a booking using Command Pattern
        BookingFactory bookingFactory = new RegularBookingFactory();
        List<ShowSeat> seats = new ArrayList<>();
        Booking booking = bookingFactory.createBooking(show, seats);

        FrontDeskOfficer officer = new FrontDeskOfficer("Jane Doe", new Account());
        Command createBookingCommand = new CreateBookingCommand(officer, booking);
        createBookingCommand.execute();

        // Process payment using Template Method Pattern
        Payment payment = new CreditCardPayment();
        payment.processPayment();

        // Notify customers using Observer Pattern
        SystemNotifier notifier = new SystemNotifier();
        Customer customer = new Customer("Alice", new Account());
        notifier.addObserver(customer);
        notifier.notifyObservers("New movie added: " + movie.getTitle());
    }
}
