
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class PostOffice {

    private final PostOfficeLocation location;
    private int receivedPackageCount;
    private int totalWeightReceived;
    private final Set<PostOfficeLocation> packageReceivedLocations;   // to keep track of location where the packages came from
    private final List<Package> receivedPackages; // to keep track of the received packages
    private final List<Package> packagesToSend;
    private boolean isServicing = false;

    public PostOffice(PostOfficeLocation location) {
        this.location = location;
        this.receivedPackageCount = 0;
        this.totalWeightReceived = 0;
        this.packageReceivedLocations = new HashSet<>();
        this.receivedPackages = new ArrayList<>();
        this.packagesToSend = new ArrayList<>();
    }

    public synchronized int getRemainingPackageCount() {
        return this.packagesToSend.size();
    }

    public int getReceivedPackageCount() {
        return receivedPackageCount;
    }

    public PostOfficeLocation getLocation() {
        return this.location;
    }

    public int getTotalWeightReceived() {
        return totalWeightReceived;
    }

    public double getAverageWeightReceived() {
        return (totalWeightReceived * 1.0) / receivedPackageCount;
    }

    public Set<PostOfficeLocation> getPackageReceivedLocations() {
        return packageReceivedLocations;
    }

    public int getSuitablePackageCount(Predicate<Package> packageCondition) {
        synchronized (receivedPackages) {
            return (int) receivedPackages.stream().filter(packageCondition::test).count();
        }
    }

    public void addPackageToSend(Package packet) {
        synchronized (packagesToSend) {
            packagesToSend.add(packet);
            packagesToSend.notifyAll();
        }
    }
    //InterruptedException because loadPackets and service use synchronized threads
    public void land(Rocket rocket) throws InterruptedException {
        rocket.setServiceFinished(false);
        //When THIS PostOffice was created a single argument was given - the enum constant
        //Next line sets THIS planet as the current location for the rocket that was sent via argument
        rocket.setCurrentLocation(location);
        offLoadPackets(rocket);
        loadPackets(rocket);
        service(rocket);
    }
    //seda kasutan ainult postiringil
    public void loadPacketsFinally(Rocket rocket) {
        rocket.receivePackages(packagesToSend);
    }
    //Load packages FROM planet TO rocket
    public void loadPackets(Rocket rocket) throws InterruptedException {
        synchronized (packagesToSend) {

            // if the rocket still in this planet
            if (rocket.getCurrentLocation() == location) {
                if (!rocket.receivePackages(packagesToSend)) {
                    // rocket has not received any packages to send
                    // PostOffice's method addPackageToSend will notify if packagecreator created a package for this planet
                    packagesToSend.wait();
                    loadPackets(rocket);
                }
            }

        }
    }
    // get the all packages that were sent to this planet FROM rocket
    public synchronized void offLoadPackets(Rocket rocket) {
        //This copies the arraylist that is returned from get suitablepackages method

        ArrayList<Package> offLoadedPackages = rocket.getSuitablePackages((packet) -> packet.getDestinationPostOfficeLocation() == location);

        // update the receive package count
        receivedPackageCount += offLoadedPackages.size();

        // all received packages to the received package list
        for (int i = 0; i < offLoadedPackages.size(); i++) {
            Package p = offLoadedPackages.get(i);
            receivedPackages.add(p);
            // update total weight received
            totalWeightReceived += p.getWeight();

            // update package received locations
            packageReceivedLocations.add(p.getSourcePostOfficeLocation());
        }
    }

    private synchronized void startServicing() {
        this.isServicing = true;
    }

    private synchronized void finishServicing() {
        this.isServicing = false;
    }

    private synchronized boolean isServicing() {
        return this.isServicing;
    }

    // two rocket cannot service at the same time
    public void service(Rocket rocket) throws InterruptedException {
        //this checks for threads conflicts while servicing. isServicing will be true if another rocket is currently being serviced on this planet
        if (!isServicing()) {
            startServicing();

            rocket.fuelling();

            // cosmic ray indicator exchanging only on jupiter and neptune
            if (location == PostOfficeLocation.JUPITER || location == PostOfficeLocation.NEPTUNE) {
                rocket.serviceCosmicRayIndicator();
            }
            // finish the servicing
            rocket.setServiceFinished(true);
            finishServicing();
        }

    }

    // check whether this is a hot planet or not
    public static boolean isPlanetHot(PostOfficeLocation planet) {
        return planet == PostOfficeLocation.MERCURY || planet == PostOfficeLocation.VENUS;
    }
}
