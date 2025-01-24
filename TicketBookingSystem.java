/**
* User Types:
    * Customer (Member & Guest): Browse movies, book tickets, view booking history.
    * Admin: Manage movies, showtimes, cinemas, users, and view bookings.
* Movies: Add, update, remove movies.
* Store movie details: name, genre, language, rating, duration, etc.
* Cinemas & Theaters:Add, update, remove cinemas/theaters.
  Each cinema has multiple theaters with seat configurations.
* Showtimes: Add, update, remove showtimes.
  Each showtime includes: movie, cinema, theater, start time, available seats.
* Booking:Select movie, showtime, and seats.
* Place and cancel bookings.
* Store booking info: user, seats, status, total amount.
* Payment:Integration with a payment gateway (e.g., Stripe or PayPal).
  Process payments for bookings.
* Notifications:Send booking confirmation, reminders, cancellations (via email/SMS).
 * 
 */


public class Address{
    private String doorNo;
    private String streetName;
    private String cityName;
    private String countryName;
    private String zipCode;
}

public enum Genre{
    ROMANCE, CLASSIC, HORROR, COMEDY, THRILLER
}

public enum PaymentMethod{
    BANK_TRANSFER, CREDIT_CARD, DEBIT_CARD, PAYPAL
}

public enum PaymentStatus{
    ACCEPTED, INVALID, FAILED, EXPIRED, BILLING_ADDRESS_ERROR, BLOCKED
}

public enum RegistrationStatus{
    FAILED, SUCCESS, PENDING, BLOCKED
}

public enum TicketStatus{
    BOOKED, FAILED
}
public class Accounts{

    private String userName;
    private String password;
    private String name;
    private Address address;

// --- Setters-------------------------------
    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setAddress(Address address){
        this.address = address;
    }

// ------- Getters ---------------------------
    public String getUserName(){
        return this.userName;
    }

    public String getPassword(){
        return this.password;
    }
    public String getName(){
        return this.name;
    }
    public Address getAddress(){
        return this.address;
    }
}



public abstract class Users{
    private Account account;
    
    public List<Theatres> searchTheatresByGenre(Genre genre); 
    public List<Theatres> searchTheatresByCity(String cityName);
    public List<Theatres> searchTheatresByMovieName(String movieName); 
  

    // getter and setter for account
    public void setAccount(Account account){
        this.account = account;
    }
}
// In future, if developer wants to search to be in database can implement this
public interface SearchTheatreInterface{
    public List<Theatres> searchTheatresByGenre(Genre genre); 
    public List<Theatres> searchTheatresByCity(String cityName);
    public List<Theatres> searchTheatresByMovieName(String movieName); 
}

public class SearchServices implements SearchTheatreInterface{
    private Cinema cinema;

    public SearchServices(){
        cinema = new Cinema();
    }
    public List<Theatres> searchTheatresByGenre(Genre genre){
        return cinema.getsearchTheatresByGenre(genre);
    }

    public List<Theatres> searchTheatresByCity(String cityName){
        return cinema.getsearchTheatresByCity(cityName);
    }

    public List<Theatres> searchTheatresByMovieName(String movieName){
        return cinema.getsearchTheatresByMovieName(movieName);
    }
    
}

// Guests can access all the informations as a Member, but have to register for booking
public class Guests extends Users{
    private SearchServices search;

    public Guests(){
        search = new SearchServices();
    }

    public RegistrationStatus registerAccount(Account account){
        setAccount(account);
    }

    @Override
    public List<Theatres> searchTheatresByGenre(Genre genre){
        return search.searchTheatresByGenre(genre);
    }

}

public class Member extends Users {
    private Booking booking;
    private NotificationService notificationService;
    private PaymentService paymentService;
    private SearchServices search;

    public Member(NotificationService notificationService, PaymentService paymentService) {
        booking = new booking();
        this.notificationService = notificationService;
        this.paymentService = paymentService;
        search = new SearchServices();
    }

    public SeatStatus addSeat(Seat seat){

    }
    public boolean bookTickets(Theatre theatre, PaymentMethod paymentMethod) {

        booking.calculateTotalPrice(Theatre theatre);
        // Step 1: Verify and book seats

        if (!booking.bookSeats(seatNumbers)) {
            return false;
        }

        // Step 2: Calculate total price based on selected seats
        int totalPrice = booking.calculateTotalPrice(seatNumbers);

        // Step 3: Process payment
        PaymentStatus paymentStatus = paymentService.processPayment(paymentMethod, totalPrice);

        // Step 4: If payment is successful, confirm booking and send notifications
        if (paymentStatus == PaymentStatus.ACCEPTED) {
            notificationService.sendNotification(getAccount(), "Booking confirmed.");
            return true;
        } else {
            // Rollback seat reservation if payment fails
            booking.cancelSeats(seatNumbers);
            notificationService.sendNotification(getAccount(), "Payment failed.");
            return false;
        }
    }
}

public class SeatService {
    private Cinema cinema;

    public SeatService(Cinema cinema) {
        this.cinema = cinema;
    }

    // Check if all selected seats are available
    public boolean areSeatsAvailable(List<Integer> seatNumbers) {
        for (int seatNumber : seatNumbers) {
            Screen screen = cinema.getScreenByMovieId(seatNumber);  // Assuming we have a method that gets the screen by movie ID
            if (!screen.isSeatAvailable(seatNumber)) {
                return false;  // If any seat is not available, return false
            }
        }
        return true;
    }

    // Book the selected seats
    public boolean bookSeats(List<Integer> seatNumbers) {
        for (int seatNumber : seatNumbers) {
            Screen screen = cinema.getScreenByMovieId(seatNumber);  // Get the screen
            if (!screen.bookSeat(seatNumber)) {
                return false;  // If booking any seat fails, return false
            }
        }
        return true;
    }

    // Cancel the selected seats (in case of payment failure)
    public boolean cancelSeats(List<Integer> seatNumbers) {
        for (int seatNumber : seatNumbers) {
            Screen screen = cinema.getScreenByMovieId(seatNumber);  // Get the screen
            if (!screen.cancelSeat(seatNumber)) {
                return false;  // If cancelling any seat fails, return false
            }
        }
        return true;
    }
}


public class Cinema {
    private List<Theater> theaters;

    public Cinema() {
        theaters = new ArrayList<>();
    }

    public void addTheater(Theater theater) {
        theaters.add(theater);
    }

    // Search theaters by location or other criteria (simplified for location search)
    public List<Theater> searchTheatersByLocation(String location) {
        List<Theater> result = new ArrayList<>();
        for (Theater theater : theaters) {
            if (theater.getAddress().getCity().equalsIgnoreCase(location)) {
                result.add(theater);
            }
        }
        return result;
    }

    public List<Theater> getAllTheaters() {
        return theaters;
    }
}


public class Seat {
    private int seatNumber;
    private boolean isAvailable;
    private int price;  // Price of this seat (can be set by Admin)

    public Seat(int seatNumber, int price) {
        this.seatNumber = seatNumber;
        this.isAvailable = true;  // Initially, seats are available
        this.price = price;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getPrice() {
        return price;
    }

    public void bookSeat() {
        if (isAvailable) {
            isAvailable = false;
        }
    }

    public void cancelSeat() {
        isAvailable = true;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}



public class Screen {
    private int screenNumber;
    private int screenSize;
    private boolean isAvailable;
    private Movie movie;
    private Seats seats;
    private Genre genre;

    public Screen(int screenNumber, int screenSize, Genre genre, Movie movie) {
        this.screenNumber = screenNumber;
        this.screenSize = screenSize;
        this.genre = genre;
        this.movie = movie;
        this.seats = new Seats(screenSize);  // Initialize seats
    }

    public boolean bookSeat(int seatNumber) {
        return seats.bookSeat(seatNumber);  // Attempt to book a seat
    }

    public boolean cancelSeat(int seatNumber) {
        return seats.cancelSeat(seatNumber);  // Attempt to cancel a seat reservation
    }

    public boolean isSeatAvailable(int seatNumber) {
        return seats.isSeatAvailable(seatNumber);  // Check if a seat is available
    }
}

interface TheatreComponent{
    public int getTotalSeats();
    public double calculateTotalRevenue();
    public double calculateBookingPrice();
}

public class Seat implements TheatreComponent{
    private String seatNumber;
    private String type;
    private double price;
    private isAvailable;

    public Seat(String seatNumber, String type, double price){
        this.seatNumber = seatNumber;
        this.type = type;
        this.price = price;
        this.isAvailable = true;
    }

    @Override
    public int getTotalSeats() {
        return 1;  // A single seat
    }


    @Override
    public double calculateTotalRevenue() {
        // Revenue for a single seat
        return price;
    }

    @Override
    public double calculateBookingPrice() {
        // Price for a single seat
        return price;
    }

    public boolean selectSeat(){
        if(!isAvailable)  return false;
        isAvailable = false;
        return true;
    }

}

public class Screen implements TheatreComponent{
    private String screenName;
    private String screenPrice
    private List<TheatreComponent> seats = new ArrayList<>();

    public Screen(String screenName, String screenPrice){
        this.screenName = screenName;
        this.screenPrice = screenPrice;
    }

    public void addSeatComponent(TheatreComponent component){
        seats.add(component);
    }

    public void removeSeatComponent(TheatreComponent component){
        seat.remove(component);
    }

    @Override
    public int getTotalSeats(){
        int totalSeats = 0;
        for(TheatreComponent seat: seats){
            totalSeats += seat.getTotalSeats(); 
        }
        return totalSeats;
    }

    @Override
    public double calculateTotalRevenue(){
        double totalRevenue = 0.0;
        for (TheatreComponent seat : seats) {
            totalRevenue += seat.calculateTotalRevenue();
        }
        return totalRevenue+screenPrice;
    }

    @Override
    public double calculateBookingPrice(){
        double totalBookingPrice = 0.0;
        for (TheatreComponent seat : seats) {
            totalBookingPrice += seat.calculateBookingPrice();
        }
        return totalBookingPrice+screenPrice;
    }
}

class Theatre implements TheatreComponent {
    private String theatreName;
    private List<TheatreComponent> screens = new ArrayList<>();

    public Theatre(String theatreName) {
        this.theatreName = theatreName;
    }

    public void addComponent(TheatreComponent component) {
        screens.add(component);
    }

    public void removeComponent(TheatreComponent component) {
        screens.remove(component);
    }

    @Override
    public void showDetails() {
        System.out.println("Theatre: " + theatreName);
        for (TheatreComponent screen : screens) {
            screen.showDetails();
        }
    }

    @Override
    public int getTotalSeats() {
        int total = 0;
        for (TheatreComponent screen : screens) {
            total += screen.getTotalSeats();
        }
        return total;
    }

    @Override
    public double calculateTotalRevenue() {
        double totalRevenue = 0.0;
        for (TheatreComponent screen : screens) {
            totalRevenue += screen.calculateTotalRevenue();
        }
        return totalRevenue;
    }

    @Override
    public double calculateBookingPrice(){
        double totalBookingPrice = 0.0;
        for (TheatreComponent seat : seats) {
            totalBookingPrice += seat.calculateBookingPrice();
        }
        return totalBookingPrice;
    }
    
}


public class Theater {
    private int theaterId;
    private String theaterName;
    private Address address;  // Theater's address
    private List<Seat> seats;  // Seats are managed by the Booking class, not here.

    public Theater(int theaterId, String theaterName, Address address) {
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.address = address;
        this.seats = new ArrayList<>();
    }

    public int getTheaterId() {
        return theaterId;
    }

    public String getTheaterName() {
        return theaterName;
    }

    public Address getAddress() {
        return address;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    // You can add seats through the Booking class, not here.
}

public class AdminService {
    // Admin adds a new theater
    public void addTheater(Cinema cinema, Theater theater) {
        cinema.addTheater(theater);
        System.out.println("Theater added: " + theater.getTheaterName());
    }

    // Admin updates the price of a specific seat in a theater
    public void updateSeatPrice(Theater theater, int seatNumber, int newPrice) {
        theater.setSeatPrice(seatNumber, newPrice);
        System.out.println("Updated seat price for seat " + seatNumber + " to $" + newPrice);
    }
}



public class Booking {
    private Theater theater;

    public Booking(Theater theater) {
        this.theater = theater;
    }

    // Check if all seats are available
    public boolean areSeatsAvailable(List<Integer> seatNumbers) {
        for (int seatNumber : seatNumbers) {
            Seat seat = getSeatByNumber(seatNumber);
            if (seat == null || !seat.isAvailable()) {
                return false;
            }
        }
        return true;
    }

    // Book seats by seat number
    public boolean bookSeats(List<Integer> seatNumbers) {
        if (!areSeatsAvailable(seatNumbers)) {
            System.out.println("Error: One or more selected seats are already booked.");
            return false;
        }

        for (int seatNumber : seatNumbers) {
            Seat seat = getSeatByNumber(seatNumber);
            if (seat != null) {
                seat.bookSeat();
            }
        }
        return true;
    }

    // Cancel seat booking
    public void cancelSeats(List<Integer> seatNumbers) {
        for (int seatNumber : seatNumbers) {
            Seat seat = getSeatByNumber(seatNumber);
            if (seat != null) {
                seat.cancelSeat();
            }
        }
    }

    // Get seat by number
    private Seat getSeatByNumber(int seatNumber) {
        for (Seat seat : theater.getSeats()) {
            if (seat.getSeatNumber() == seatNumber) {
                return seat;
            }
        }
        return null;
    }

    // Calculate the total price of the selected seats
    public int calculateTotalPrice(List<Integer> seatNumbers) {
        int totalPrice = 0;
        for (int seatNumber : seatNumbers) {
            Seat seat = getSeatByNumber(seatNumber);
            if (seat != null) {
                totalPrice += seat.getPrice();
            }
        }
        return totalPrice;
    }
}


public interface Notification {
    void sendNotification(Account account, String message);
}

public class EmailNotification implements Notification {
    @Override
    public void sendNotification(Account account, String message) {
        System.out.println("Sending email to " + account.getUserName() + ": " + message);
    }
}

public class SMSNotification implements Notification {
    @Override
    public void sendNotification(Account account, String message) {
        System.out.println("Sending SMS to " + account.getUserName() + ": " + message);
    }
}

public class NotificationService {
    private List<Notification> notifications = new ArrayList<>();

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public void sendNotification(Account account, String message) {
        for (Notification notification : notifications) {
            notification.sendNotification(account, message);
        }
    }
}

public interface PaymentMethod {
    PaymentStatus makePayment(double amount);
}

public class CreditCardPayment implements PaymentMethod {
    @Override
    public PaymentStatus makePayment(double amount) {
        System.out.println("Processing Credit Card Payment of $" + amount);
        // Simulate payment success
        return PaymentStatus.ACCEPTED;
    }
}

public class PayPalPayment implements PaymentMethod {
    @Override
    public PaymentStatus makePayment(double amount) {
        System.out.println("Processing PayPal Payment of $" + amount);
        // Simulate payment success
        return PaymentStatus.ACCEPTED;
    }
}

public class PaymentService {

    // Process the payment with the selected payment method
    public PaymentStatus processPayment(PaymentMethod paymentMethod, double amount) {
        // Simulate payment processing for simplicity
        switch (paymentMethod) {
            case CREDIT_CARD:
                return PaymentStatus.ACCEPTED;  // Assume successful payment
            case DEBIT_CARD:
                return PaymentStatus.ACCEPTED;  // Assume successful payment
            case PAYPAL:
                return PaymentStatus.ACCEPTED;  // Assume successful payment
            case BANK_TRANSFER:
                return PaymentStatus.ACCEPTED;  // Assume successful payment
            default:
                return PaymentStatus.FAILED;  // Default failure
        }
    }
}


 

public class Main {
    public static void main(String[] args) {
        // Step 1: Create Notification, Seat, and Payment services
        NotificationService notificationService = new NotificationService();
        SeatService seatService = new SeatService(new Cinema());
        PaymentService paymentService = new PaymentService();

        // Step 2: Create a Member and register account
        Account account = new Account();
        account.setUserName("john_doe");
        account.setName("John Doe");
        account.setAddress(new Address("123 Street", "City", "Country", "12345"));

        Member member = new Member(notificationService, seatService, paymentService);
        member.setAccount(account);

        // Step 3: Member searches theaters by genre
        Genre genre = Genre.COMEDY;
        List<Theatres> theaters = member.searchTheatresByGenre(genre);
        System.out.println("Theaters found for genre " + genre + ": " + theaters);

        // Step 4: Member books tickets
        List<Integer> seatNumbers = Arrays.asList(1, 2, 3);  // User wants to book 3 seats
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
        boolean bookingSuccess = member.bookTickets(1, seatNumbers, paymentMethod);

        if (bookingSuccess) {
            System.out.println("Booking successfully made!");
        } else {
            System.out.println("Booking failed.");
        }
    }
}
