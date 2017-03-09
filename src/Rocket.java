
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

//Abstract because I don't want this class to be instantiated
public abstract class Rocket implements Runnable {

    protected PostOfficeLocation currentLocation;
    protected int fuel;
    protected int totalFuelReceived;
    protected boolean serviceFinished = false;
    protected int maxWeight;
    protected int totalWeight = 0;
    protected int cosmicRayIndicator;
    //packages are only added in the receivePackages() method
    protected List<Package> packages;
    //Since this class never get instantiated, the subclass uses this variable to select only Heat planet for Heat rocket and non-Heat planet for Regular rocket
    protected Predicate<Package> acceptPackage;
    protected List<PostOffice> postOffices;
    protected final long TRAVEL_TIME = 15;
    protected String name;

    //Subclass HAS to implement this method
    protected abstract int getFuelConsumption(PostOfficeLocation toPostOffice);

    @Override
    public void run() {

        //try catch necessary because land method has
        try {
            //From PostDeliverySystem when regularRocket thread is started, this is started once
            //Then it takes the array of postoffices, that are full of objects of type PostOffice.
            //New PostOffice object just takes the enum constant as argument
            //Then gets a postoffice from there according to the currentlocation(Because PostOffice is identified by enum constant)
            //CurrentLocation comes from getrandomregularplanet function
            //getindex returns as the enum constant in PostOfficeLocation class
            //Now that we have this postoffice object that corres
            //land method takes this randomly chose postoffice and lands the rocket that called this(regular,heat,post)
            // There is a point between this and postoffices because in Rocket I define list postOffices type PostOffice, in RegularRocket I inherit this list AND also have in
            //RegularRocket's constructor that "this.postoffices"(which refers to parent's variable) is the same as the argument that was given to RegularRocket object "postoffices".
            //This is why i can refer to this object's variable postoffices here, and call a method for that array.

            this.postOffices.get(PostOfficeLocation.getIndex(currentLocation)).land(this);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        while (!Thread.interrupted()) {

            if (cosmicRayIndicator == 1) {
                // only one start remaining. so should go to exchange cosmic ray indicator

                fly(PostOfficeLocation.getRandomCosmicRayServiceStation());
            } else {
            //At first, before packagrecreator is started, this loop will run empty until a package appears on the planet this rocket is.
                if (packages.size() > 0) {
                    // there are packages in the rocket
                    // fly to first package destination planet
                    fly(packages.get(0).getDestinationPostOfficeLocation());
                }
            }

        }
    }

    protected void fly(PostOfficeLocation toPostOffice) {
        
        start();
        fuel -= getFuelConsumption(toPostOffice);   // decrease the fuel count

        
        try {
            System.out.println(name+" is going to "+toPostOffice+" from "+currentLocation);
            Thread.sleep(TRAVEL_TIME);
            postOffices.get(PostOfficeLocation.getIndex(toPostOffice)).land(this);
        } catch (InterruptedException ex) {
            
            ex.printStackTrace();
        }
    }
    //argument packets on praegusel PLANEEDIL olevad pakid. See funktsioon lisab need pakid raketile ja tagastab True kui rakett sai pakke juurde
    //ja tagastab False kui ei saanud. Kui False siis väljakutsuv meetod ootab
    public synchronized boolean receivePackages(List<Package> packets) {
        int receivedWeight = 0;

        //THIS IF ONLY EXECUTES IF THIS ROCKET INSTANCE DOES HAVE PACKAGES ALREADY
        if (this.packages.size() > 0) {
            // this already has packages
            int currentWeight = this.totalWeight;
            //?? This if only executes if there is any room to take new packages, if there is not, nothing happens, this function will return false, probably a bug
            if (currentWeight < maxWeight) {
                // this can take more packages

                HashSet<PostOfficeLocation> preferred = new HashSet<>();    // to keep preferred destination locations

                // create the preferred set - i go over EVERY package and save the destination postoffice enum to preferred set
                for (int i = 0; i < this.packages.size(); i++) {
                    PostOfficeLocation destination = this.packages.get(i).getDestinationPostOfficeLocation();
                    preferred.add(destination);
                }

                // get the matching package count - i count how many packages can i deliver from this planet to another
                // that satisfy the heat planet requirement(Regular rocket from regular to regular, heat from heat to heat)
                int matchCount = (int) packets.stream().filter(acceptPackage::test).count();

                // try to receive more packages
                for (int i = 0; i < matchCount; i++) {
                    Package match = packets.stream().filter(acceptPackage::test).findFirst().get();
                    //remove from PLANET's list of packets
                    packets.remove(match);

                    if (maxWeight < totalWeight + match.getWeight()) {
                        //THIS PACKET CAN'T BE TAKEN FOR IT EXCEEDS THE WEIGHT LIMIT AND IS PUT BACK INTO PLANET'S LIST
                        packets.add(match);    // add again to the end
                    //Now we have established that: rocket had packages already before this function, we selected packages
                    //that can be delivered to appropriate planets(regular can't to heat) and that the new package CAN be
                    // added because the total weight would NOT exceed the max weight, this is when this else statement executes

                    } else {
                        // check whether this package destination location is preferred or not

                        if (preferred.contains(match.getDestinationPostOfficeLocation())) {
                            // this is preferred. So take it.
                            packages.add(match);
                            totalWeight += match.getWeight();
                            receivedWeight += match.getWeight();
                            
                        } else {
                            // skip this package
                            packets.add(match);    // add again to the end of the planet list
                        }

                    }
                }

            }

        }
        //THIS IF ONLY EXECUTES IF THIS ROCKET INSTANCE DOES NOT HAVE ANY PACKAGES CURRENTLY
        // check whether this has enough space to take more packages
        if (maxWeight > totalWeight) {
            // get the matching package count
            int matchCount = (int) packets.stream().filter(acceptPackage::test).count();

            // try to receive more packages
            for (int i = 0; i < matchCount; i++) {
                Package match = packets.stream().filter(acceptPackage::test).findFirst().get();
                packets.remove(match);

                if (maxWeight < totalWeight + match.getWeight()) {
                    // can not be taken
                    packets.add(match);    // add again to the end
                } else {
                    // this package can be taken
                    packages.add(match);
                    totalWeight += match.getWeight();
                    receivedWeight += match.getWeight();
                    
                }
            }
        }

        return receivedWeight > 0;
    }

    // remove the suitable packages from the rocket
    public synchronized ArrayList<Package> getSuitablePackages(Predicate<Package> packageCondition) {
        ArrayList<Package> toOffLoad = new ArrayList<>();
        //I think the test here ONLY selects those packages that are true according to this lambda argument in PostOffice class:
        // ArrayList<Package> offLoadedPackages = rocket.getSuitablePackages((packet) -> packet.getDestinationPostOfficeLocation() == location);
        // IF this is so, and i'm not sure, the rest is easy, I just count them, convert it into int and save it as matchCount

        //Method reference :: Does not invoke method here, just references it
        int matchCount = (int) packages.stream().filter(packageCondition::test).count();

        // add all to the offload list and remove from the rocket
        for (int i = 0; i < matchCount; i++) {
            //Here i use optional to take care of the possibility of null values
            //findFirst() returns an Optional describing the first element of this stream, or an empty Optional if the stream is empty
            Optional<Package> suitablePackage = packages.stream().filter(packageCondition::test).findFirst();

            if (suitablePackage.isPresent()) {
                //get returns the non-null value held by this Optional
                Package match = suitablePackage.get();

                // decrease the weight
                totalWeight -= match.getWeight();

                packages.remove(match);
                toOffLoad.add(match);

            }

        }

        return toOffLoad;
    }

    protected synchronized boolean isServiceFinished() {
        return serviceFinished;
    }

    public synchronized void setServiceFinished(boolean serviceFinished) {
        this.serviceFinished = serviceFinished;
    }

    public synchronized void setCurrentLocation(PostOfficeLocation newLocation) {
        this.currentLocation = newLocation;
    }

    public synchronized PostOfficeLocation getCurrentLocation() {
        return this.currentLocation;
    }

    public void fuelling() {
        // take fuels if this rocket's fuel level is less than or equals to 50 (my own decision)
        if (fuel <= 50) {
            // update total fuel received counter
            totalFuelReceived += (100 - fuel);
            fuel = 100; // increase the fuel level to 100
        }
    }

    public void serviceCosmicRayIndicator() {

        // exchange if this has only 2 or less starts left
        if (cosmicRayIndicator <= 2) {
            cosmicRayIndicator = 25;
        }

    }

    public String getName() {
        return this.name;
    }

    public void start() {
        // decrease cosmic ray indicator
        this.cosmicRayIndicator--;
    }
}
