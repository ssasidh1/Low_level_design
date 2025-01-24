/**
 * AMAZON ONLINE SHOPPING SYSTEM
 * 1. Web app
 * 2. Products
 * 3. Prices
 * 4. Delivery dates
 * 5. Address
 * 6. Payment page ->Card, bank etc
 * 7. Search by name and category
 * 8. Recommendation
 * 9. Orders page
 * 10. Add/modify/delete to cart
 * 11. Autocompletion
 * 13. Billing page
 * 14. Order Confirmation page
 * 15. Login, sign up, log out page - to buy
 * 16. Sellers page -> add new Products
 * 17. Rate
 * 18. Cancel order
 * 19. Notification
 * 20. Track products
 * 
 * 
 * 
 * -- Member -> add product to sell and buy
 * -- Guest -> add product and enroll to buy
 * -- Admin -> verify and add products
 * -- System -> notification and tracking details
 * 
 * -- Products -> Pid , name , quantity, price, sellerId
 * -- ProductItems -> updateProduct(), deleteProduct() 
 * -- Seller -> organizationName, updateProduct()
 * -- Buyer -> updateProduct, updateProduct()
 * -- Admin -> UpdateProduct
 * -- Users -> (Buyers, Sellers, Admins) ->(id, name, address, userName, passwordHash);
 * -- Billing -> (different kinds of Cards)
 * -- Cards
 * -- BankAccounts
 * -- CreditCard
 * -- DebitCard
 * -- BankAccount1
 * -- TrackingPage
 * -- Logistics
 * -- Transport
 * -- Air
 * -- Road
 * -- Ship
 * 
 * 
 * -- System  
 */

// Address Class
public class Address {
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    // Constructor, getters, and setters
    public Address(String streetAddress, String city, String state, String zipCode, String country) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}

// Enum for AccountStatus
public enum AccountStatus {
    ACTIVE, BLOCKED, BANNED, COMPROMISED, ARCHIVED, UNKNOWN
}

// Payment Method Interface (Strategy Pattern)
interface PaymentMethod {
    boolean processPayment(double amount);
}

// Payment Status Enum
public enum PaymentStatus {
    UNPAID, PENDING, COMPLETED, FILLED, DECLINED, CANCELLED, ABANDONED, SETTLING, SETTLED, REFUNDED
}

// Order Status Enum
public enum OrderStatus {
    UNSHIPPED, PENDING, SHIPPED, COMPLETED, CANCELED, REFUND_APPLIED
}

// OrderLog Class
public class OrderLog {
    private String orderNumber;
    private Date creationDate;
    private OrderStatus status;
}

// ProductCategory Class
public class ProductCategory {
    private String name;
    private String description;

    public ProductCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getter and Setter methods
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

// Concrete Payment Methods (Strategy Pattern)
class CreditCardPayment implements PaymentMethod {
    @Override
    public final boolean processPayment(double amount) {
        System.out.println("Processing Credit Card Payment of $" + amount);
        return true;
    }
}

class BankTransferPayment implements PaymentMethod {
    @Override
    public final boolean processPayment(double amount) {
        System.out.println("Processing Bank Transfer Payment of $" + amount);
        return true;
    }
}

// PaymentContext Class
public class PaymentContext {
    private PaymentMethod paymentMethod;

    public PaymentContext(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean processPayment(Order order) {
        return paymentMethod.processPayment(order.getTotalAmount());
    }
}

// Account Class
public class Account {
    private String userName;
    private String password;
    private AccountStatus status;
    private String email;
    private String phone;
    private Address address;
    private List<CreditCards> creditCards;
    private List<BankTransfers> bankTransfers;

    public boolean resetPassword() {
        return true;
    }

    public void updateAccountDetails(String email, String phone, Address address) {
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}

// Item Class
public class Item {
    private String productID;
    private int quantity;
    private double price;

    public Item(String productID, int quantity, double price) {
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
    }

    public boolean updateQuantity(int quantity) {
        this.quantity = quantity;
        return true;
    }
}

// Singleton ShoppingCart Class
public class ShoppingCart {
    private List<Item> items;
    private static ShoppingCart instance;

    private ShoppingCart(){
        items = new ArrayList<>();
    }

    public static synchronized ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    public boolean addItem(Item item) {
        items.add(item);
        return true;
    }

    public boolean removeItem(Item item) {
        items.remove(item);
        return true;
    }

    public boolean updateItemQuantity(Item item, int quantity) {
        item.updateQuantity(quantity);
        return true;
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean checkout() {
        return true;
    }
}

// Customer Class - Abstract Class (does not handle order directly)
public abstract class Customer {
    private ShoppingCart cart;

    public ShoppingCart getShoppingCart() {
        return cart;
    }

    public boolean addItem(Item item) {
        return cart.addItem(item);
    }

    public boolean removeItem(Item item) {
        return cart.removeItem(item);
    }
}

// Order Class
public class Order {
    private String orderNumber;
    private OrderStatus status;
    private double totalAmount;

    public Order(String orderNumber, double totalAmount) {
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
        this.status = OrderStatus.UNSHIPPED;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }
}

// Member Class - Responsible for placing orders
public class Member extends Customer {
    private Account account;

    public Member(Account account) {
        this.account = account;
    }

    public OrderStatus placeOrder(Order order, PaymentMethod paymentMethod) {
        boolean paymentProcessed = paymentMethod.processPayment(order.getTotalAmount());

        if (paymentProcessed) {
            System.out.println("Payment successful for order: " + order.getOrderNumber());
            order.setStatus(OrderStatus.PENDING);
        } else {
            System.out.println("Payment failed for order: " + order.getOrderNumber());
            order.setStatus(OrderStatus.CANCELED);
        }

        return order.getStatus();
    }
}

// Guest Class - Responsible for browsing but cannot place orders unless registered
public class Guest extends Customer {
    public boolean registerAccount(String userName, String password) {
        Account newAccount = new Account(userName, password);
        Member newMember = new Member(newAccount);
        return true;
    }
}

// Notification Class (Observer Pattern)
public abstract class Notification {
    public abstract void sendNotification(Account account);
}

class EmailNotification extends Notification {
    @Override
    public void sendNotification(Account account) {
        System.out.println("Sending email notification to: " + account.email);
    }
}

class SMSNotification extends Notification {
    @Override
    public void sendNotification(Account account) {
        System.out.println("Sending SMS notification to: " + account.phone);
    }
}

// NotificationManager (Observer Pattern Manager)
class NotificationManager {
    private List<Notification> notifications = new ArrayList<>();

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public void removeNotification(Notification notification) {
        notifications.remove(notification);
    }

    public void sendNotifications(Account account) {
        for (Notification notification : notifications) {
            notification.sendNotification(account);
        }
    }
}

// Product Class
public class Product {
    private String productId;
    private String name;
    private String description;
    private double price;
    private boolean isApproved;

    public Product(String productId, String name, String description, double price) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isApproved = false;
    }

    public void approve() {
        this.isApproved = true;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

// Admin Class
public class Admin {
    private Catalog catalog;

    public Admin() {
        catalog = new Catalog();
    }

    public void approveProduct(ProductRequest productRequest) {
        Product product = productRequest.getProduct();
        System.out.println("Admin is approving product: " + product.getName());
        product.approve();
        catalog.addProduct(product);
        System.out.println("Product " + product.getName() + " is now approved and available for sale.");
    }

    public void displayCatalog() {
        catalog.displayProducts();
    }
}

// Seller Class
public class Seller {
    private String sellerId;
    private String name;

    public Seller(String sellerId, String name) {
        this.sellerId = sellerId;
        this.name = name;
    }

    public void requestProductAddition(Product product, Admin admin) {
        ProductRequest productRequest = new ProductRequest(product, this);
        System.out.println("Seller " + name + " is requesting to add product: " + product.getName());
        admin.approveProduct(productRequest);
    }
}

// Catalog Class
public class Catalog {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        if (product.isApproved()) {
            products.add(product);
            System.out.println("Product " + product.getName() + " added to the catalog.");
        } else {
            System.out.println("Cannot add unapproved product: " + product.getName());
        }
    }

    public void displayProducts() {
        System.out.println("Displaying all products in the catalog:");
        for (Product product : products) {
            if (product.isApproved()) {
                System.out.println(product.getName() + ": $" + product.getPrice());
            }
        }
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {
        // Step 1: Create Admin, Seller, and Customer (Member or Guest)
        Admin admin = new Admin();
        Seller seller = new Seller("S1", "John");

        // Step 2: Seller requests to add products to the system
        Product product1 = new Product("P1", "Laptop", "High-performance laptop", 1500.00);
        Product product2 = new Product("P2", "Headphones", "Noise-canceling headphones", 200.00);

        seller.requestProductAddition(product1, admin);  // Seller requests Admin to add product1
        seller.requestProductAddition(product2, admin);  // Seller requests Admin to add product2

        // Step 3: Admin approves the product and adds them to the catalog
        admin.displayCatalog();  // Display the catalog after adding products

        // Step 4: Create a Member (Customer) and subscribe to email and SMS notifications
        Member customer = new Member(new Account("john_doe", "password", AccountStatus.ACTIVE, "john@example.com", "1234567890", new Address("123 Street", "City", "State", "Zip", "Country")));

        NotificationService notificationService = new NotificationService();
        customer.subscribeEmail(notificationService);
        customer.subscribeSMS(notificationService);

        // Customer browses the catalog and adds products to cart
        System.out.println("Customer browsing products:");
        admin.displayCatalog();  // Display all available products in the catalog

        // Customer adds products to cart
        Item item1 = new Item("P1", 1, 1500.00); // Add Laptop
        Item item2 = new Item("P2", 2, 200.00); // Add 2 Headphones
        customer.addItem(item1);
        customer.addItem(item2);

        // Step 5: Customer places an order
        Order order = new Order("ORD123", 1500.00 + 2 * 200.00);  // Total amount $1500 + 2 * $200
        PaymentContext paymentContext = new PaymentContext(new CreditCardPayment());

        boolean paymentSuccess = paymentContext.processPayment(order);  // Process payment

        if (paymentSuccess) {
            System.out.println("Payment successful for order: " + order.getOrderNumber());
            order.setStatus(OrderStatus.PENDING);
            // Step 6: Notify Customer about the successful payment
            notificationService.notifySubscribers("Your payment of $" + order.getTotalAmount() + " has been successfully processed.");
        } else {
            System.out.println("Payment failed for order: " + order.getOrderNumber());
            order.setStatus(OrderStatus.CANCELED);
            // Notify Customer about the failed payment
            notificationService.notifySubscribers("Payment failed for your order: " + order.getOrderNumber());
        }

        // Step 7: Customer checks out
        customer.getShoppingCart().checkout();

        // Step 8: Display approved products in the catalog
        admin.displayCatalog();  // Display all the products in the catalog
    }
}
