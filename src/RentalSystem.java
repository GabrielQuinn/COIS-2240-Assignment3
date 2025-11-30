import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;

public class RentalSystem {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
    private static RentalSystem rentalSystem; // Singleton Design
    
    public RentalSystem() {
    	loadData();
    }
    
    public static RentalSystem getInstance() {
    	if (rentalSystem == null) {
    		rentalSystem = new RentalSystem();
        }
        return rentalSystem;
    }
    
    private void loadData() {
    	// Loop through all text files and add to vehicle/customer/records
    	
    	Scanner readScanner;
    	
    	// Vehicles data
    	try {
    		File vehicleFile = new File("vehicles.txt");
        	
        	readScanner = new Scanner(vehicleFile);
        	
        	while (readScanner.hasNext()) {
				
				String data = readScanner.nextLine();
				Vehicle vehicle;
				
				// Split data by ","
				String[] fields = data.split(",");
				
				String vehicleType = fields[0];
				String licensePlate = fields[1];
				String make = fields[2];
				String model = fields[3];
				int year = Integer.parseInt(fields[4]);
				String status = fields[5];
				
				if (vehicleType.equals("Car") == true) {
					int numSeats = Integer.parseInt(fields[6]);
					
					vehicle = new Car(make, model, year, numSeats);
				} else if (vehicleType.equals("Minibus") == true) {
					boolean isAccessible = Boolean.parseBoolean(fields[6]);
					
					vehicle = new Minibus(make, model, year, isAccessible);
				} else if (vehicleType.equals("PickupTruck") == true) {
					double cargoSize = Double.parseDouble(fields[6]);
					boolean hasTrailer = Boolean.parseBoolean(fields[7]);
					
					vehicle = new PickupTruck(make, model, year, cargoSize, hasTrailer);
				} else continue;
				
				vehicle.setLicensePlate(licensePlate);
				vehicle.setStatus(Vehicle.VehicleStatus.valueOf(status));
				
				vehicles.add(vehicle);
			}
			
			readScanner.close();
			
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    	
    	// Customers data
    	try {
    		File customerFile = new File("customers.txt");
        	
        	readScanner = new Scanner(customerFile);
        	
        	while (readScanner.hasNext()) {
				
				String data = readScanner.nextLine();
				Customer customer;
				
				// Split data by ","
				String[] fields = data.split(",");
				
				int custId = Integer.parseInt(fields[0]);
				String custName = fields[1];
				
				customer = new Customer(custId, custName);
				
				customers.add(customer);
			}
			
			readScanner.close();
			
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    	
    	// Record data
    	try {
    		File recordFile = new File("rental_records.txt");
        	
        	readScanner = new Scanner(recordFile);
        	
        	while (readScanner.hasNext()) {
				
				String data = readScanner.nextLine();
				RentalRecord record;
				
				// Split data by ","
				String[] fields = data.split(",");
				
				String licensePlate = fields[0];
				int custId = Integer.parseInt(fields[1]);
				LocalDate recordDate = LocalDate.parse(fields[1]);
				double totalAmount = Double.parseDouble(fields[2]);
				String recordType = fields[3];
				
				Vehicle vehicle = null;
				Customer customer = null;
				
				// Get vehicle
				for ( int i=0; i<vehicles.size(); i++ ) {
					Vehicle cur_vehicle = vehicles.get(i);
					if (cur_vehicle.getLicensePlate().equals(licensePlate)) {
						vehicle = cur_vehicle;
					}
				}
				
				// Get customer
				for ( int i=0; i<customers.size(); i++ ) {
					Customer cur_customer = customers.get(i);
					if (cur_customer.getCustomerId() == custId) {
						customer = cur_customer;
					}
				}
				
				if (vehicle == null || customer == null) {
					continue;
				}
				
				record = new RentalRecord(vehicle, customer, recordDate, totalAmount, recordType);
				
				rentalHistory.addRecord(record);
			}
			
			readScanner.close();
			
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    }
    
    // Append to given file
    private void appendToFile(String fileName, String fileContent) {
    	try {
    		FileWriter fw = new FileWriter(fileName, true);
    		BufferedWriter bw = new BufferedWriter(fw);
        	bw.write(fileContent);
        	
        	bw.newLine();
        	bw.close();
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    	
    }
    
    public void saveVehicle(Vehicle vehicle) {
    	
    	String appendString = vehicle.getClass().getName() + "," + vehicle.getLicensePlate() + "," + vehicle.getMake() + "," + vehicle.getModel() + "," + vehicle.getYear() + "," + vehicle.getStatus();
    	
    	if (vehicle instanceof Car) {
    	    appendString += "," + ((Car) vehicle).getNumSeats();
    	} else if (vehicle instanceof Minibus) {
    		appendString += "," + ((Minibus) vehicle).getIsAccessible();
    	} else if (vehicle instanceof PickupTruck) {
    		appendString += "," + ((PickupTruck) vehicle).getCargoSize() + "," + ((PickupTruck) vehicle).hasTrailer();
    	} else {
    		System.out.println("Invalid Class");
    		return;
    	}
    	
    	appendToFile("vehicles.txt", appendString);
    }
    
    public void saveCustomer(Customer customer) {
    	appendToFile("customers.txt", "," + customer.getCustomerId() + "," + customer.getCustomerName());
    }
    
    public void saveRecord(RentalRecord record) {
    	appendToFile("rental_records.txt", "," + record.getVehicle().getLicensePlate() + "," + record.getCustomer().getCustomerId() + "," + record.getRecordDate() + "," + record.getTotalAmount() + "," + record.getRecordType());
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        saveVehicle(vehicle); // Save to file
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomer(customer); // Save to file
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Available) {
            vehicle.setStatus(Vehicle.VehicleStatus.Rented);
            
            RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");
            
            rentalHistory.addRecord(record);
            System.out.println("Vehicle rented to " + customer.getCustomerName());
            
            saveRecord(record); // Save to file
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Rented) {
            vehicle.setStatus(Vehicle.VehicleStatus.Available);
            
            RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
            
            rentalHistory.addRecord(record);
            System.out.println("Vehicle returned by " + customer.getCustomerName());
            
            saveRecord(record); // Save to file
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }    

    public void displayVehicles(Vehicle.VehicleStatus status) {
        // Display appropriate title based on status
        if (status == null) {
            System.out.println("\n=== All Vehicles ===");
        } else {
            System.out.println("\n=== " + status + " Vehicles ===");
        }
        
        // Header with proper column widths
        System.out.printf("|%-16s | %-12s | %-12s | %-12s | %-6s | %-18s |%n", 
            " Type", "Plate", "Make", "Model", "Year", "Status");
        System.out.println("|--------------------------------------------------------------------------------------------|");
    	  
        boolean found = false;
        for (Vehicle vehicle : vehicles) {
            if (status == null || vehicle.getStatus() == status) {
                found = true;
                String vehicleType;
                if (vehicle instanceof Car) {
                    vehicleType = "Car";
                } else if (vehicle instanceof Minibus) {
                    vehicleType = "Minibus";
                } else if (vehicle instanceof PickupTruck) {
                    vehicleType = "Pickup Truck";
                } else {
                    vehicleType = "Unknown";
                }
                System.out.printf("| %-15s | %-12s | %-12s | %-12s | %-6d | %-18s |%n", 
                    vehicleType, vehicle.getLicensePlate(), vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getStatus().toString());
            }
        }
        if (!found) {
            if (status == null) {
                System.out.println("  No Vehicles found.");
            } else {
                System.out.println("  No vehicles with Status: " + status);
            }
        }
        System.out.println();
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        if (rentalHistory.getRentalHistory().isEmpty()) {
            System.out.println("  No rental history found.");
        } else {
            // Header with proper column widths
            System.out.printf("|%-10s | %-12s | %-20s | %-12s | %-12s |%n", 
                " Type", "Plate", "Customer", "Date", "Amount");
            System.out.println("|-------------------------------------------------------------------------------|");
            
            for (RentalRecord record : rentalHistory.getRentalHistory()) {                
                System.out.printf("| %-9s | %-12s | %-20s | %-12s | $%-11.2f |%n", 
                    record.getRecordType(), 
                    record.getVehicle().getLicensePlate(),
                    record.getCustomer().getCustomerName(),
                    record.getRecordDate().toString(),
                    record.getTotalAmount()
                );
            }
            System.out.println();
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id)
                return c;
        return null;
    }
}