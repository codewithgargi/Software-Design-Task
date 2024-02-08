import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


// Enumeration for Passenger Types
enum PassengerType {
    STANDARD, GOLD, PREMIUM
}

// Activity class
class Activity {
    private String name;
    private String description;
    private double cost;
    private int capacity;
    private int signedUp;

    public Activity(String name, String description, double cost, int capacity) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.capacity = capacity;
        this.signedUp = 0;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getCost() {
        return cost;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSignedUp() {
        return signedUp;
    }

    // Sign up a passenger for the activity
    public boolean signUp() {
        if (signedUp < capacity) {
            signedUp++;
            return true;
        }
        return false;
    }
}

// Destination class
class Destination {
    private String name;
    private List<Activity> activities;

    public Destination(String name) {
        this.name = name;
        this.activities = new ArrayList<>();
    }

    // Add activity to the destination
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    // Getters
    public String getName() {
        return name;
    }

    public List<Activity> getActivities() {
        return activities;
    }
}

// Passenger class
class Passenger {
    private String name;
    private int passengerNumber;
    private PassengerType type;
    private double balance;

    public Passenger(String name, int passengerNumber, PassengerType type) {
        this.name = name;
        this.passengerNumber = passengerNumber;
        this.type = type;
        this.balance = 0;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getPassengerNumber() {
        return passengerNumber;
    }

    public PassengerType getType() {
        return type;
    }

    public double getBalance() {
        return balance;
    }

    // Deduct balance for signed up activity
    public void deductBalance(double amount) {
        balance -= amount;
    }
}

// TravelPackage class
class TravelPackage {
    private String name;
    private int passengerCapacity;
    private List<Destination> itinerary;
    private List<Passenger> passengers;

    public TravelPackage(String name, int passengerCapacity) {
        this.name = name;
        this.passengerCapacity = passengerCapacity;
        this.itinerary = new ArrayList<>();
        this.passengers = new ArrayList<>();
    }

    // Add destination to itinerary
    public void addDestination(Destination destination) {
        itinerary.add(destination);
    }

    // Add passenger to package
    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
    }

    // Print itinerary
    public void printItinerary() {
        System.out.println("Travel Package: " + name);
        for (Destination destination : itinerary) {
            System.out.println("Destination: " + destination.getName());
            for (Activity activity : destination.getActivities()) {
                System.out.println("Activity: " + activity.getName() + ", Cost: " + activity.getCost() + ", Capacity: " + activity.getCapacity());
            }
        }
    }

    // Print passenger list
    public void printPassengerList() {
        System.out.println("Passenger List for Travel Package: " + name);
        System.out.println("Capacity: " + passengerCapacity);
        System.out.println("Number of Passengers: " + passengers.size());
        for (Passenger passenger : passengers) {
            System.out.println("Passenger: " + passenger.getName() + ", Number: " + passenger.getPassengerNumber());
        }
    }

    // Sign up a passenger for an activity
    public boolean signUpActivity(Passenger passenger, Activity activity) {
        // Check if passenger already signed up for activity
        // Check if activity has capacity
        if (passenger.getType() == PassengerType.PREMIUM || (passenger.getType() == PassengerType.GOLD && activity.signUp()) || (passenger.getType() == PassengerType.STANDARD && passenger.getBalance() >= activity.getCost() && activity.signUp())) {
            // Deduct balance if applicable
            if (passenger.getType() != PassengerType.PREMIUM)
                passenger.deductBalance(activity.getCost());
            return true;
        }
        return false;
    }

    // Get activities with available slots
    public List<Activity> getAvailableActivities() {
        List<Activity> availableActivities = new ArrayList<>();
        for (Destination destination : itinerary) {
            for (Activity activity : destination.getActivities()) {
                if (activity.getSignedUp() < activity.getCapacity()) {
                    availableActivities.add(activity);
                }
            }
        }
        return availableActivities;
    }
}


// Unit Tests using JUnit

class TravelPackageTest {
    @Test
    void signUpActivity_StandardPassenger_InsufficientBalance() {
        TravelPackage travelPackage = new TravelPackage("Test Package", 10);
        Passenger passenger = new Passenger("John Doe", 101, PassengerType.STANDARD);
        travelPackage.addPassenger(passenger);
        Activity activity = new Activity("Test Activity", "Test description", 50.0, 10);
        assertFalse(travelPackage.signUpActivity(passenger, activity));
    }

    @Test
    void signUpActivity_GoldPassenger_WithDiscount() {
        TravelPackage travelPackage = new TravelPackage("Test Package", 10);
        Passenger passenger = new Passenger("Jane Smith", 102, PassengerType.GOLD);
        travelPackage.addPassenger(passenger);
        Activity activity = new Activity("Test Activity", "Test description", 50.0, 10);
        assertTrue(travelPackage.signUpActivity(passenger, activity));
    }

    @Test
    void signUpActivity_PremiumPassenger_Free() {
        TravelPackage travelPackage = new TravelPackage("Test Package", 10);
        Passenger passenger = new Passenger("Alice Johnson", 103, PassengerType.PREMIUM);
        travelPackage.addPassenger(passenger);
        Activity activity = new Activity("Test Activity", "Test description", 50.0, 10);
        assertTrue(travelPackage.signUpActivity(passenger, activity));
    }

    @Test
    void getAvailableActivities_SomeAvailable() {
        TravelPackage travelPackage = new TravelPackage("Test Package", 10);
        Destination destination = new Destination("Test Destination");
        Activity activity1 = new Activity("Test Activity 1", "Test description 1", 50.0, 5);
        Activity activity2 = new Activity("Test Activity 2", "Test description 2", 30.0, 10);
        destination.addActivity(activity1);
        destination.addActivity(activity2);
        travelPackage.addDestination(destination);
        List<Activity> availableActivities = travelPackage.getAvailableActivities();
        assertEquals(2, availableActivities.size());
    }

    @Test
    void getAvailableActivities_NoneAvailable() {
        TravelPackage travelPackage = new TravelPackage("Test Package", 10);
        Destination destination = new Destination("Test Destination");
        Activity activity1 = new Activity("Test Activity 1", "Test description 1", 50.0, 0);
        Activity activity2 = new Activity("Test Activity 2", "Test description 2", 30.0, 0);
        destination.addActivity(activity1);
        destination.addActivity(activity2);
        travelPackage.addDestination(destination);
        List<Activity> availableActivities = travelPackage.getAvailableActivities();
        assertEquals(0, availableActivities.size());
    }
}

public class App {
    public static void main(String[] args) {
        // Sample usage
        // Create activities
        Activity activity1 = new Activity("Hiking", "Explore nature trails", 50.0, 10);
        Activity activity2 = new Activity("Sightseeing", "Visit famous landmarks", 30.0, 20);

        // Create destinations and add activities
        Destination destination1 = new Destination("Mountain Resort");
        destination1.addActivity(activity1);
        Destination destination2 = new Destination("City Tour");
        destination2.addActivity(activity2);

        // Create travel package and add destinations
        TravelPackage travelPackage = new TravelPackage("Adventure Tour", 50);
        travelPackage.addDestination(destination1);
        travelPackage.addDestination(destination2);

        // Print itinerary
        travelPackage.printItinerary();

        // Print passenger list
        travelPackage.printPassengerList();

        // Sign up a passenger for an activity
        Passenger passenger1 = new Passenger("John Doe", 101, PassengerType.STANDARD);
        Passenger passenger2 = new Passenger("Jane Smith", 102, PassengerType.GOLD);
        Passenger passenger3 = new Passenger("Alice Johnson", 103, PassengerType.PREMIUM);

        travelPackage.addPassenger(passenger1);
        travelPackage.addPassenger(passenger2);
        travelPackage.addPassenger(passenger3);

        travelPackage.signUpActivity(passenger1, activity1); // Should fail due to insufficient balance
        travelPackage.signUpActivity(passenger2, activity1); // Should succeed with discount
        travelPackage.signUpActivity(passenger3, activity1); // Should succeed for premium passenger

        // Print available activities
        List<Activity> availableActivities = travelPackage.getAvailableActivities();
        System.out.println("Available Activities:");
        for (Activity activity : availableActivities) {
            System.out.println("Activity: " + activity.getName() + ", Slots Available: " + (activity.getCapacity() - activity.getSignedUp()));
        }
    }
}

