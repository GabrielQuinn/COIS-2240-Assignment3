import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}
