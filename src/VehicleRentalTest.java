import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

class VehicleRentalTest {

	private RentalSystem rentalSystem;

    @BeforeEach
    void setUp() {
        rentalSystem = RentalSystem.getInstance();
    }
	
	@Test
	void testLicensePlate() {
		Car car1 = new Car("Toyota", "Corolla", 2025, 4);
		Car car2 = new Car("Toyota", "Corolla", 2025, 4);
		Car car3 = new Car("Toyota", "Corolla", 2025, 4);
		Car car4 = new Car("Toyota", "Corolla", 2025, 4);
		Car car5 = new Car("Toyota", "Corolla", 2025, 4);
		Car car6 = new Car("Toyota", "Corolla", 2025, 4);
		Car car7 = new Car("Toyota", "Corolla", 2025, 4);
		
		car1.setLicensePlate("AAA100");
		assertEquals(car1.getLicensePlate(), "AAA100");
		car2.setLicensePlate("ABC567");
		assertEquals(car2.getLicensePlate(), "ABC567");
		car3.setLicensePlate("ZZZ999");
		assertEquals(car3.getLicensePlate(), "ZZZ999");
		
		assertThrows(IllegalArgumentException.class, () -> {
	        car4.setLicensePlate("");
	    }, "Invalid plate");
		assertThrows(IllegalArgumentException.class, () -> {
	        car5.setLicensePlate(null);
	    }, "Invalid plate");
		assertThrows(IllegalArgumentException.class, () -> {
	        car6.setLicensePlate("AAA1000");
	    }, "Invalid plate");
		assertThrows(IllegalArgumentException.class, () -> {
	        car7.setLicensePlate("ZZZ99");
	    }, "Invalid plate");
	}
	
	@Test
	void testRentAndReturnVehicle() {
		Car car = new Car("Toyota", "Carolla", 2025, 4);
		car.setLicensePlate("AAA111");
		car.setStatus(Vehicle.VehicleStatus.Available);
		
		Customer customer = new Customer(1, "Gabe");
		
		assertEquals(car.getStatus(), Vehicle.VehicleStatus.Available);
		
		// Rent Vehicle
		
		assertTrue(rentalSystem.rentVehicle(car, customer, LocalDate.now(), 10));
		
		assertEquals(car.getStatus(), Vehicle.VehicleStatus.Rented);
		
		assertFalse(rentalSystem.rentVehicle(car, customer, LocalDate.now(), 10));
		
		// Return vehicle
		
		assertTrue(rentalSystem.returnVehicle(car, customer, LocalDate.now(), 10));
		
		assertEquals(car.getStatus(), Vehicle.VehicleStatus.Available);
		
		assertFalse(rentalSystem.returnVehicle(car, customer, LocalDate.now(), 10));
	}
	
	@Test
	void testSingletonRentalSystem() throws Exception {
		Constructor<RentalSystem> constructor = RentalSystem.class.getDeclaredConstructor();
		constructor.getModifiers();
		assertEquals(constructor.getModifiers(), Modifier.PRIVATE);
		assertNotEquals(rentalSystem, null);
	}

}
