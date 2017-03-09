
import java.util.ArrayList;

public class HeatResistantRocket extends Rocket {

    public HeatResistantRocket(PostOfficeLocation startLocation, ArrayList<PostOffice> postOffices, String name) {
        this.currentLocation = startLocation;
        this.postOffices = postOffices;
        this.maxWeight = 80;
        this.acceptPackage = (packet) -> PostOffice.isPlanetHot(packet.getDestinationPostOfficeLocation())||PostOffice.isPlanetHot(packet.getSourcePostOfficeLocation());
        this.fuel = 100;
        this.cosmicRayIndicator = 25;
        this.totalFuelReceived = 0;
        this.packages = new ArrayList<>();
        this.name = name;

    }

    @Override
    protected int getFuelConsumption(PostOfficeLocation toPostOffice) {
        if (PostOffice.isPlanetHot(currentLocation)) {
            // this consumes 50 fuel
            return 50;
        } else {
            // this consumes 25 fuel
            return 25;
        }

    }

}
