/**
 * AMAZON ONLIE SHOPPING SYSTEM
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

public class Address {
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    // Getter and Setter methods
}
public enum AccountStatus {
    ACTIVE, BLOCKED, BANNED, COMPROMISED, ARCHIVED, UNKNOWN
}
interface PaymentMethod {
    boolean processPayment(double amount);
}
public enum OrderStatus {
    UNSHIPPED, PENDING, SHIPPED, COMPLETED, CANCELED, REFUND_APPLIED
}
public enum PaymentStatus {
    UNPAID, PENDING, COMPLETED, FILLED, DECLINED, CANCELLED, ABANDONED, SETTLING, SETTLED, REFUNDED
}
public class OrderLog {
    private String orderNumber;
    private Date creationDate;
    private OrderStatus status;
}

public class ProductCategory {
    private String name;
    private String description;
    // Getter and Setter methods
}


// These classes can be overriden hence we can use 
//public final class CreditCardPayment , but this makes the class 
// we can make the method final
// Hence nobody can access the processPayment by extending the CreditPayment class
// Concrete Payment Methods (Strategy Pattern)
class CreditCardPayment implements PaymentMethod {
    @Override
    public final boolean processPayment(double amount) {
        // Credit Card Payment Processing Logic
        System.out.println("Processing Credit Card Payment of $" + amount);
        return true;
    }
}

class BankTransferPayment implements PaymentMethod {
    @Override
    public final boolean processPayment(double amount) {
        // Bank Transfer Payment Processing Logic
        System.out.println("Processing Bank Transfer Payment of $" + amount);
        return true;
    }
}

public class PaymentContext {
    private PaymentMethod paymentMethod;
    // you can change the constructor to have notificationServices which can further 
    //be used to send notification once payment is successful
    public PaymentContext(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean processPayment(Order order) {
        // Call the appropriate payment method (e.g., CreditCard, PayPal)
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

    // Constructor and Getter/Setter methods
    public boolean resetPassword() {
        // Logic to reset password
        return true;
    }

    public void updateAccountDetails(String email, String phone, Address address) {
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}

public class Item {
    private String productID;
    private int quantity;
    private double price;

    public boolean updateQuantity(int quantity) {
        this.quantity = quantity;
        return true;
    }
}

public class ShoppingCart {
    private List<Item> items;
    private static ShoppingCart instance;

    private ShoppingCart(){
        items = new ArrayList<>();
    }

    public static synchronized ShoppingCart getInstance(){
        if(instance == null){
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
        // Checkout logic
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

    // Remove placeOrder() method from Customer since it only applies to Members
}

// Order Class
public class Order {
    private String orderNumber;
    private OrderStatus status;
    private double totalAmount;

    // Other fields and methods...

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }
}

// Member Class - Responsible for placing orders (only for registered users)
public class Member extends Customer {
    private Account account;

    public Member(Account account) {
        this.account = account;
    }

    // Modified placeOrder to include paymentMethod
    public OrderStatus placeOrder(Order order, PaymentMethod paymentMethod) {
        // Process the payment for the order
        boolean paymentProcessed = paymentMethod.processPayment(order.getTotalAmount());
        
        if (paymentProcessed) {
            System.out.println("Payment successful for order: " + order.getOrderNumber());
            order.setStatus(OrderStatus.PENDING);  // Or COMPLETED, depending on the status
        } else {
            System.out.println("Payment failed for order: " + order.getOrderNumber());
            order.setStatus(OrderStatus.CANCELED);  // Or another status for failed payment
        }
        
        return order.getStatus();
    }

    public void subscribeEmail(NotificationService service) {
        service.addNotification(new EmailNotification(this.email));
    }

    // Subscribe to SMS notifications
    public void subscribeSMS(NotificationService service) {
        service.addNotification(new SMSNotification(this.phone));
    }

    // Receive notifications
    public void receiveNotification(String message) {
        System.out.println(name + " received: " + message);
    }
}


// Guest Class - Responsible for browsing but cannot place orders unless registered
public class Guest extends Customer {

    public boolean registerAccount(String userName, String password) {
        // Logic for registering the guest as a member
        System.out.println("Guest registered as Member.");
        Account newAccount = new Account(userName, password);
        Member newMember = new Member(newAccount);  // Convert guest to member
        return true;
    }

}

public abstract class Notification {
    private int notificationId;
    private Date createdOn;
    private String content;

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

// Observer Pattern Manager
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
    private boolean isApproved; // Tracks if the product is approved by admin

    public Product(String productId, String name, String description, double price) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isApproved = false;  // Initially not approved
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

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}
public class ProductRequest {
    private Product product;
    private Seller seller;

    public ProductRequest(Product product, Seller seller) {
        this.product = product;
        this.seller = seller;
    }

    public Product getProduct() {
        return product;
    }

    public Seller getSeller() {
        return seller;
    }
}

public class Admin {
    private Catalog catalog;
    public Admin(){
        catalog = new Catalog();
    }
    public void approveProduct(ProductRequest productRequest) {
        Product product = productRequest.getProduct();
        System.out.println("Admin is approving product: " + product.getName());
        product.approve();  // Approve the product

        // Optionally, add the product to the system (e.g., catalog)
        System.out.println("Product " + product.getName() + " is now approved and available for sale.");
    }
}

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
        admin.approveProduct(productRequest);  // Request Admin to approve the product
    }
}


public class Catalog {
    private List<Product> products;

    public Catalog() {
        this.products = new ArrayList<>();
    }

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
                System.out.println(product.getName() + ": " + product.getDescription() + " - $" + product.getPrice());
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // Step 1: Create Admin, Seller, and Customer (Member)
        Admin admin = new Admin();
        Seller seller = new Seller("S1", "John");

        // Step 2: Seller requests to add products to the system
        Product product1 = new Product("P1", "Laptop", "High-performance laptop", 1500.00);
        Product product2 = new Product("P2", "Headphones", "Noise-canceling headphones", 200.00);

        seller.requestProductAddition(product1, admin);  // Seller requests Admin to add product1
        seller.requestProductAddition(product2, admin);  // Seller requests Admin to add product2

        // Step 3: Create a Catalog to manage approved products
        Catalog catalog = new Catalog();
        
        // Add the approved products to the catalog
        catalog.addProduct(product1);  // This will work, as the product is approved by Admin
        catalog.addProduct(product2);  // This will work as well

        // Step 4: Create a Member (Customer) and subscribe to email and SMS notifications
        Customer customer = new Customer("Alice", "alice@example.com", "123-456-7890");

        NotificationService notificationService = new NotificationService();
        customer.subscribeEmail(notificationService);
        customer.subscribeSMS(notificationService);

        // Step 5: Create an Order
        Order order = new Order("ORD123", 1500.00);  // Order with a total amount of $1500

        // Step 6: Member places an order and makes a payment using CreditCardPayment
        PaymentContext paymentContext = new PaymentContext(new CreditCardPayment());
        boolean paymentSuccess = paymentContext.processPayment(order);  // Process payment

        // Step 7: If payment is successful, notify the customer
        if (paymentSuccess) {
            System.out.println("Payment successful for order: " + order.getOrderNumber());
            order.setStatus(OrderStatus.PENDING);
            notificationService.notifySubscribers("Your payment of $" + order.getTotalAmount() + " has been successfully processed.");
        } else {
            System.out.println("Payment failed for order: " + order.getOrderNumber());
            order.setStatus(OrderStatus.CANCELED);
            notificationService.notifySubscribers("Payment failed for your order: " + order.getOrderNumber());
        }

        // Step 8: Display approved products in the catalog
        catalog.displayProducts();  // Display all the products in the catalog
    }
}


