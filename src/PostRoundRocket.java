
import java.util.ArrayList;
//I extend HeatResistantRocket and not Rocket because weight is same
public class PostRoundRocket extends HeatResistantRocket{

    public PostRoundRocket(PostOfficeLocation startLocation, ArrayList<PostOffice> postOffices) {
        super(startLocation, postOffices, "Post round rocket");
        //Main difference between heatResistantRocket and this one:
        this.acceptPackage = (packet) -> true;  // accept all packages
    }
    
    @Override
    public void fly(PostOfficeLocation toPostOffice){
        //start method reduces cosmicrayindicator count
        start();
        //Starts from Mercury and Venus cost more
        if (currentLocation == PostOfficeLocation.MERCURY || currentLocation == PostOfficeLocation.VENUS) {
            // this consumes 50 fuel
            fuel -= 50;
        } else {
            // this consumes 25 fuel
            fuel -= 25;
        }
        
        System.out.println(name+" is going to " + toPostOffice + " from " + currentLocation);
    }
    
}
