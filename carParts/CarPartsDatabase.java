package carParts;

import carParts.CarPart;

import java.util.List;

public class CarPartsDatabase {
    public static List<CarPart> ENGINES = List.of(
            new CarPart("Eco Engine", 3000, 60),
            new CarPart("Sport Engine", 5000, 90),
            new CarPart("Diesel Engine", 4000, 70)
    );

    public static List<CarPart> WHEELS = List.of(
            new CarPart("Basic Wheels", 1000, 40),
            new CarPart("Sport Wheels", 1500, 60),
            new CarPart("Off-road Wheels", 2000, 70)
    );

    public static List<CarPart> BODIES = List.of(
            new CarPart("Sedan Body", 4000, 60),
            new CarPart("SUV Body", 6000, 80),
            new CarPart("Compact Body", 3000, 50)
    );

    public static List<CarPart> TRANSMISSIONS = List.of(
            new CarPart("Manual Transmission", 2000, 50),
            new CarPart("Automatic Transmission", 3000, 70)
    );
}
