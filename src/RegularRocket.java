
import java.util.ArrayList;

public class RegularRocket extends Rocket {

    public RegularRocket(PostOfficeLocation startLocation, ArrayList<PostOffice> postOffices, String name) {
        this.currentLocation = startLocation;
        this.postOffices = postOffices;
        this.maxWeight = 100;
        this.acceptPackage = (packet) -> !PostOffice.isPlanetHot(packet.getDestinationPostOfficeLocation());
        this.fuel = 100;
        this.cosmicRayIndicator = 25;
        this.totalFuelReceived = 0;
        this.packages = new ArrayList<>();
        this.name = name;

    }

    @Override
    protected int getFuelConsumption(PostOfficeLocation toPostOffice) {
        return 20;
    }

}
